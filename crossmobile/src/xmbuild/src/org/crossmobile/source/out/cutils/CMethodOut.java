/* Copyright (c) 2002-2011 by XMLVM.org
 *
 * Project Info:  http://www.xmlvm.org
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.crossmobile.source.out.cutils;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.ListIterator;

import org.crossmobile.source.ctype.CArgument;
import org.crossmobile.source.ctype.CLibrary;
import org.crossmobile.source.ctype.CMethod;
import org.crossmobile.source.ctype.CObject;
import org.crossmobile.source.ctype.CProperty;
import org.crossmobile.source.ctype.CStruct;
import org.crossmobile.source.guru.Advisor;
import org.crossmobile.source.xtype.AdvisorWrapper;
import org.crossmobile.source.xtype.XArg;
import org.crossmobile.source.xtype.XMethod;
import org.crossmobile.source.xtype.XObject;
import org.crossmobile.source.xtype.XProperty;

/**
 * The class is used as code generator for methods for classes as well as
 * methods associated with structures. In case of properties, the getters and
 * setters are emitted. Cases where methods are plain C functions and are not
 * derived from Obj-C are also handled. TODO At a later stage, code related to
 * properties (getters and setters) can be segregated.
 * 
 */
public class CMethodOut {

    private Writer              out                     = null;
    private CObject             object                  = null;
    private CLibrary            lib                     = null;
    private String              objectClassName         = null;
    private CMethodHelper       methodHelper            = null;
    private static final String AUTORELEASEPOOL_ALLOC   = "\n\tNSAutoreleasePool* p = [[NSAutoreleasePool alloc] init];\n";
    private static final String AUTORELEASEPOOL_RELEASE = "\t[ p release];\n";
    private static final String XMLVM_VAR_THIZ          = "\n\tXMLVM_VAR_THIZ;";


    public CMethodOut(Writer out, CLibrary lib, CObject object) {
        this.out = out;
        this.object = object;
        this.lib = lib;
        this.objectClassName = object.getcClassName();
        this.methodHelper = new CMethodHelper(this.object.name, this.objectClassName, this.lib);
    }

    /**
     * Used to generate the code for wrappers of methods of a particular class
     * 
     * @param isStruct
     *            - true if it is a structure
     * @throws IOException
     */
    public void emitMethods(boolean isStruct) throws IOException {

        for (CMethod method : object.getMethods()) {

            Boolean notImplemented = false;
            String methodCall = null;
            String returnString = "";

            String returnType = method.getReturnType().toString();
            if (!returnType.equals("void")) {
                returnString = methodHelper.getReturnString(returnType);
                if (returnString == null)
                    notImplemented = true;
            }

            out.append(CUtilsHelper.getWrapperComment(method.getArguments(), objectClassName,
                    method.name));

            if (method.isProperty()) {
                methodCall = emitGettersAndSetters(method, returnType);
            } else if (isStruct) {
                methodCall = emitCMethod(method, returnType, true);
            } else {
                if (method.derivesFromObjC())
                    methodCall = emitObjCDerivedMethod(method, returnType);
                else
                    methodCall = emitCMethod(method, returnType, false);
            }

            if (methodCall == null)
                notImplemented = true;

            if (notImplemented == true)
                out.append("\t" + CUtilsHelper.NOT_IMPLEMENTED);
            else {
                if (AdvisorWrapper.needsAutoReleasePool(method.getSelectorName(), object.name))
                    out.append(AUTORELEASEPOOL_ALLOC + methodCall + AUTORELEASEPOOL_RELEASE + "\t"
                            + returnString);
                else
                    out.append(methodCall + "\t" + returnString);
            }
            out.append("\n" + CUtilsHelper.END_WRAPPER + "\n");
        }
    }

    /**
     * In case of properties, generate the getters and setters for the property
     * 
     * @param method
     *            - instance of CMethod associated with the property
     * @param returnType
     *            - return type if it is a getter
     * @return - return the constructed code for getters and setters
     */
    private String emitGettersAndSetters(CMethod method, String returnType) {
        StringBuilder methodCall = new StringBuilder();
        String returnVariableStr = null;
        // Getters:
        if (method.getArguments().isEmpty()) {

            methodCall.append(XMLVM_VAR_THIZ);

            if ((returnVariableStr = getReturnVariable(returnType)) != null)
                methodCall.append(returnVariableStr);
            else
                return null;

            if (method.derivesFromObjC())
                methodCall.append("[thiz " + CProperty.getPropertyDef(method.name) + "];");
            else
                return null;

        } else {
            // Setters: TODO : Still in progress
            if (AdvisorWrapper.needsAccumulator(object.name)
                    || AdvisorWrapper.needsReplacer(object.name)) {
                XProperty prop = null;
                XObject obj = AdvisorWrapper.getSpecialClass(object.name);
                if ((prop = obj.getPropertyInstance(CProperty.getPropertyDef(method.name))) != null) {

                    StringBuilder accString = new StringBuilder();

                    if (prop.isRetain())
                        accString.append(getAccumulativeCode(1, prop.getType()));
                    else if (prop.isReplace())
                        accString.append("\n\tjthiz->fields." + objectClassName + "."
                                + prop.getName() + " = n1;");
                }
            }

            methodCall.append(XMLVM_VAR_THIZ + "\n");

            if (!returnType.equals("void")) {
                System.out.println("ERROR: Setter has return type");
            }

            if (method.derivesFromObjC())
                methodCall.append("[thiz " + method.name + "" + "];");
            else
                return null;
            return null; // TODO
        }
        return methodCall.toString();
    }

    /**
     * There are methods that are C functions and are not selectors. In such
     * cases, this method is called for the wrapper generation
     * 
     * @param method
     *            - instance of CMethod for the particular C function
     * @param returnType
     *            - return type of the function
     * @param parentIsStruct
     *            - true if the method is associated with a structure
     * @return Constructed string for the C function
     */
    public String emitCMethod(CMethod method, String returnType, boolean parentIsStruct) {

        StringBuilder methodCall = new StringBuilder();
        String argList = null;
        String tempName = null;
        String returnVariableStr = null;

        if (method.isStatic() || parentIsStruct) {
            if ((returnVariableStr = getReturnVariable(returnType)) != null)
                methodCall.append(returnVariableStr);
            else
                return null;

            List<CArgument> arguments = method.getArguments();
            if (parentIsStruct) {
                methodCall
                        .append(methodHelper.getModifiedFunctionName(method.name, parentIsStruct));
                argList = methodHelper.getArgList(arguments, method.isStatic(), true);
            } else {
                if ((tempName = methodHelper.getModifiedFunctionName(method.name, false)) != null) {
                    methodCall.append(tempName);
                    argList = methodHelper.getArgList(arguments, method.isStatic(), false);
                } else {
                    methodCall.append(method.name);
                    argList = methodHelper.getArgList(arguments, method.isStatic(), false);
                }
            }
        } else {
            return null;
        }
        if (argList == null)
            return null;

        methodCall.append(argList + ";\n");
        return methodCall.toString();
    }

    /**
     * This method is used to construct the call for an objC selector (unlike a
     * C function)
     * 
     * @param method
     *            - instance of CMethod for the particular method whose wrapper
     *            has to be generated
     * @param returnType
     *            - returnType of the method
     * @return Constructed string for a objC selector
     */
    public String emitObjCDerivedMethod(CMethod method, String returnType) {

        List<String> nameParts = method.getNameParts();
        List<CArgument> arguments = method.getArguments();
        String methodName = method.name;
        String selName = method.getSelectorName();
        StringBuilder methodCall = new StringBuilder();
        String argType = null;
        int i = 1;
        String returnVariableStr = null;
        String accString = "";

        if (AdvisorWrapper.needsAccumulator(object.name)
                || AdvisorWrapper.needsReplacer(object.name)) {
            accString = injectAccumulatorReplacerCode(selName);
        }

        if (!method.isStatic())
            methodCall.append(XMLVM_VAR_THIZ + "\n\t");

        if ((returnVariableStr = getReturnVariable(returnType)) != null)
            methodCall.append(returnVariableStr);
        else
            return null;

        if (method.isStatic())
            methodCall.append(" [" + object.name + " ");
        else
            methodCall.append("[thiz ");

        ListIterator<CArgument> iterator = arguments.listIterator();

        if (arguments.isEmpty())
            methodCall.append(methodName);

        for (String namePart : nameParts) {
            if (iterator.hasNext()) {
                CArgument argument = (CArgument) iterator.next();
                methodCall.append(" " + namePart + ":");
                argType = argument.getType().toString();

                if (!methodHelper.ignore(argType)) {
                    methodCall.append(methodHelper.parseArgumentType(argType, i));
                    i++;
                } else
                    return null;
            }
        }

        methodCall.append("];");
        methodCall.append(accString + "\n");
        return methodCall.toString();
    }

    private String getReturnVariable(String returnType) {
        String mappedType = null;
        StringBuilder returnVariable = new StringBuilder();

        if (!returnType.equals("void")) {
            if ((mappedType = CMethodHelper.getMappedDataType(returnType)) != null)
                returnVariable.append("\n\t" + mappedType);
            else
                return null;

            if ((!Advisor.isNativeType(returnType) && !CStruct.isStruct(returnType))
                    || returnType.equals("Object"))
                returnVariable.append("*");

            returnVariable.append(" objCObj = ");
        }
        return returnVariable.toString();
    }

    /**
     * Emit code to keep a C-reference in order to tell the garbage collector
     * about the association
     * 
     * @param selName
     *            - the selector for which the code has to be emitted
     * @return returns the constructed code
     */
    private String injectAccumulatorReplacerCode(String selName) {
        List<XArg> splArgs = null;
        StringBuilder accumulativeCode = new StringBuilder();
        XMethod method = null;

        XObject obj = AdvisorWrapper.getSpecialClass(object.name);
        if ((method = obj.getMethodInstance(selName)) != null)
            splArgs = method.getArgList();

        if (splArgs != null) {
            for (XArg sArg : splArgs) {
                if (sArg.isRetain()) {
                    accumulativeCode.append(getAccumulativeCode(sArg.getPosition() + 1, sArg
                            .getType()));
                } else if (sArg.isReplace()) {
                    // TODO is Replace only for properties?
                }
            }
        }
        return accumulativeCode.toString();
    }

    private String getAccumulativeCode(int position, String type) {
        StringBuilder accString = new StringBuilder();
        accString.append("\n\tXMLVMUtil_ArrayList_add(jthiz->fields." + objectClassName
                + ".acc_Array, ");
        accString.append("((org_xmlvm_ios_" + type + "*) n" + position + ")");
        // TODO Change for cases where type is List<>
        accString.append("->fields.org_xmlvm_ios_NSObject.wrappedObjCObj);");
        return accString.toString();
    }

}
