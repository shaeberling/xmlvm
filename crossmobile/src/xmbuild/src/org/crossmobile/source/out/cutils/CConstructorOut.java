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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import org.crossmobile.source.ctype.CArgument;
import org.crossmobile.source.ctype.CConstructor;
import org.crossmobile.source.ctype.CEnum;
import org.crossmobile.source.ctype.CLibrary;
import org.crossmobile.source.ctype.CObject;
import org.crossmobile.source.ctype.CStruct;
import org.crossmobile.source.guru.Advisor;
import org.crossmobile.source.xtype.AdvisorMediator;

/**
 * This class is used to generate code for the constructors for classes as well
 * as constructors associated with the structures. In some cases, the
 * constructors are overloaded and Enums are used for the identification of the
 * actual constructor being called. The code generation in such cases is also
 * handled here.
 * 
 */
public class CConstructorOut {

    private Writer        out          = null;
    private CObject       object       = null;
    private CMethodHelper methodHelper = null;
    private CLibrary      library      = null;


    public CConstructorOut(Writer out, CLibrary lib, CObject object) {
        this.out = out;
        this.object = object;
        this.library = lib;
        this.methodHelper = new CMethodHelper(this.object.name, lib);
    }

    /**
     * Used to generate code for constructors
     * 
     * @param isStruct
     *            - Indicates if the constructor is generated for structs
     * @throws IOException
     */
    public void emitConstructors() throws IOException {
        emitConstructors(object, null);
        Map<String, Boolean> subclassArgTypes = new HashMap<String, Boolean>();
        for (CConstructor c : object.getConstructors())
            subclassArgTypes.put(c.getCommaSeparatedArgumentTypes(), true);
        emitSuperClassConstructor(object, subclassArgTypes);
    }

    /**
     * Used to emit constructors in subclasses corresponding to constructors in
     * superclass. The method is called recursively to add all the constructors
     * to a subclass along its inheritance tree.
     * 
     * @param isStruct
     *            - Indicates if the constructor is generated for structs
     * @param obj
     *            - the object for which the constructors are currently looked
     *            for
     * @param subclassArgTypes
     *            - Map containing the comma separated argument types for all
     *            the constructors that are present in the current object. This
     *            is required to prevent generation of constructors
     *            corresponding to superclass where the signatures conflict with
     *            that in subclass
     * @throws IOException
     */
    private void emitSuperClassConstructor(CObject currentObj, Map<String, Boolean> subclassArgTypes)
            throws IOException {
        if (currentObj.getSuperclass() != null) {
            CObject superclass = library.getObjectIfPresent(currentObj.getSuperclass()
                    .getProcessedName());

            emitConstructors(superclass, subclassArgTypes);
            if (superclass.getSuperclass() != null) {
                for (CConstructor c : superclass.getConstructors())
                    subclassArgTypes.put(c.getCommaSeparatedArgumentTypes(), true);
                emitSuperClassConstructor(superclass, subclassArgTypes);
            }
        }
    }

    /**
     * Used to generate code for constructors
     * 
     * @param isStruct
     *            - Indicates if the constructor is generated for structs
     * @throws IOException
     */
    private void emitConstructors(CObject currentObj, Map<String, Boolean> subclassArgType)
            throws IOException {

        boolean has_default_constructor = false;
        List<CArgument> arguments = null;
        CEnum cEnum = null;
        Map<String, List<String>> namePartsMap = new HashMap<String, List<String>>();

        for (CConstructor con : currentObj.getConstructors()) {

            StringBuilder initialInjectedCode = new StringBuilder();
            StringBuilder replaceableCode = new StringBuilder();
            StringBuilder finalInjectedCode = new StringBuilder();

            CMethodHelper.setCodeForInjection(con.getSelectorName(), object.name, true, false,
                    initialInjectedCode, replaceableCode, finalInjectedCode);

            if (!replaceableCode.toString().isEmpty()) {
                out.append(replaceableCode);
                continue;
            }

            if (!AdvisorMediator.methodIsIgnore(con.getSelectorName(), object.name)) {
                if (subclassArgType != null
                        && subclassArgType.containsKey(con.getCommaSeparatedArgumentTypes()))
                    continue;

                arguments = con.getArguments();

                cEnum = con.getEnum();
                if (cEnum != null)
                    namePartsMap = cEnum.getNameParts();

                if (!initialInjectedCode.toString().isEmpty())
                    out.append(initialInjectedCode);

                out.append(CUtilsHelper.getWrapperComment(arguments, object.getcClassName(),
                        cEnum == null ? null : cEnum.name));

                if (AdvisorMediator.needsAutoReleasePool(con.getSelectorName(), object.name))
                    out.append(C.AUTORELEASEPOOL_ALLOC);

                if (CStruct.isStruct(currentObj.name)
                        || AdvisorMediator.isCFOpaqueType(object.name)) {
                    emitStructConstructor(arguments, AdvisorMediator.isCFOpaqueType(object.name));
                } else {
                    if (arguments.isEmpty())
                        has_default_constructor = true;

                    if (con.isOverloaded() && cEnum != null)
                        emitOverloadedConstructor(cEnum.name, namePartsMap, arguments);
                    else
                        emitObjectConstructor(con.getNameParts(), arguments);
                }
                if (AdvisorMediator.needsAutoReleasePool(con.getSelectorName(), object.name))
                    out.append(C.AUTORELEASEPOOL_RELEASE);

                if (!finalInjectedCode.toString().isEmpty())
                    out.append(finalInjectedCode);

                out.append(C.END_WRAPPER + C.N);

            }
        }

        if (!has_default_constructor)
            emitDefaultConstructor(CStruct.isStruct(currentObj.name));
    }

    /**
     * There are few constructors which are overloaded and hence enums are used
     * for this purpose. The code generation in such cases has to be handled in
     * a different way.
     * 
     * @param cEnumName
     *            - represents the enum name as in the java api
     * @param namePartsMap
     *            - map containing the List of nameparts of a selector
     * @param arguments
     *            - List of arguments
     * @throws IOException
     */
    private void emitOverloadedConstructor(String cEnumName,
            Map<String, List<String>> namePartsMap, List<CArgument> arguments) throws IOException {
        Iterator<Entry<String, List<String>>> it = namePartsMap.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, List<String>> pairs = it.next();
            out.append(C.NT + "if((" + object.getcClassName() + "_" + cEnumName + "*) n"
                    + (arguments.size() + 1) + " == ");
            out.append(object.getcClassName() + "_" + cEnumName + "_GET_" + pairs.getKey() + "())"
                    + C.NT + "{");
            emitObjectConstructor(pairs.getValue(), arguments);
            out.append(C.T + "}" + C.N);
        }
    }

    /**
     * In case of structures, the constructors should make call to the
     * ObjectiveC 'Make' when new structures have to be created.
     * 
     * @param arguments
     *            - List of arguments
     * @throws IOException
     */
    private void emitStructConstructor(List<CArgument> arguments, boolean isCFOpaqueType)
            throws IOException {
        boolean isFirst = true;
        int i = 1;
        String setReferenceObj = null;
        StringBuilder code = new StringBuilder();

        code.append(CMethodHelper.getRequiredMacros(object.name, arguments, false, true));

        if (isCFOpaqueType)
            code.append(C.NT + object.name + "Ref var0 = " + object.name + "Create(");
        else
            code.append(C.NT + object.name + " var0 = " + object.name + "Make(");

        for (CArgument arg : arguments) {
            if (!isFirst)
                code.append(",");
            isFirst = false;

            if (!methodHelper.ignore(arg.getType().toString()))
                code.append(CMethodHelper.parseArgumentType(arg.getType().toString(), i));
            else {
                out.append(C.NOT_IMPLEMENTED);
                return;
            }

            if (CMethodHelper.isDoublePointer(arg.getType().toString()))
                setReferenceObj = CMethodHelper.setReferenceVariable(arg.getType(), i);
            i++;
        }
        code.append(");" + C.NT);

        if (isCFOpaqueType) {
            code.append(emitCallToInternalConstructor());
        } else {
            code.append(object.getcClassName() + "* jObj = me;" + C.N);

            for (CArgument arg : object.getVariables()) {
                code.append(C.T + "jObj->fields." + object.getcClassName() + "." + arg.name
                        + "_ = ");
                if (Advisor.isNativeType(arg.getType().toString())) {
                    code.append("var0." + arg.name + ";" + C.N);
                } else if (CStruct.isStruct(arg.getType().toString()))
                    code.append("from" + arg.getType().toString() + "(var0." + arg.name + ");"
                            + C.N);
                else
                    code.append("xmlvm_get_associated_c_object(" + "var0." + arg.name + ");" + C.N);
            }
        }
        code.append(setReferenceObj);
        out.append(code);
    }

    /**
     * This method is used to emit the code for constructors in case of general
     * classes.
     * 
     * @param nameParts
     *            - List of nameparts of a selector
     * @param arguments
     *            - List of arguments
     * @throws IOException
     */
    private void emitObjectConstructor(List<String> nameParts, List<CArgument> arguments)
            throws IOException {
        StringBuilder objCCall = new StringBuilder();
        String argType = null;
        int i = 1;
        boolean flag = true;
        boolean implemented = true;
        StringBuilder methodCode = new StringBuilder();
        StringBuilder releaseVar = new StringBuilder("");
        String setReferenceObj = "";

        objCCall.append(CMethodHelper.getRequiredMacros(object.name, arguments, false, true));
        objCCall.append(C.NT + object.name + "* ");
        objCCall.append("var0 = [[" + objectToBeInitialized() + " alloc]");

        ListIterator<CArgument> iterator = arguments.listIterator();

        for (String namePart : nameParts) {

            if (!arguments.isEmpty()) {
                if (iterator.hasNext()) {

                    CArgument argument = (CArgument) iterator.next();
                    argType = argument.getType().toString();
                    String parsedArg = null;

                    if (CMethodHelper.requiresConversion(argType))
                        releaseVar.append(CMethodHelper.getCodeToReleaseVar(i));

                    if (CMethodHelper.isDoublePointer(argType))
                        setReferenceObj = CMethodHelper.setReferenceVariable(argument.getType(), i);

                    objCCall.append(" " + namePart + ":");

                    if (!methodHelper.ignore(argType)
                            && (parsedArg = CMethodHelper.parseArgumentType(argType, i)) != null) {

                        objCCall.append(parsedArg);
                        i++;
                    } else {
                        objCCall.delete(0, objCCall.length());
                        objCCall.append(C.NOT_IMPLEMENTED + C.N);
                        implemented = false;
                        break;
                    }
                }
            } else {
                objCCall.append("init];" + C.N);
                objCCall.append(setReferenceObj);
                flag = false;
            }
        }

        if (implemented) {
            if (flag == true)
                objCCall.append("];");
            methodCode.append(objCCall).append(releaseVar + C.N);
            out.append(methodCode);
            out.append(emitCallToInternalConstructor());
        } else {
            out.append(objCCall);
        }
    }

    /**
     * Java API has default constructors for the classes. Such cases has to be
     * handled by wrappers
     * 
     * @throws IOException
     */
    private void emitDefaultConstructor(boolean isStruct) throws IOException {
        out.append(C.BEGIN_WRAPPER + "[" + object.getcClassName() + "___INIT___");
        out.append("]" + C.N);
        if (AdvisorMediator.isCFOpaqueType(object.name) || object.name.equals("CFType"))
            out.append(C.NOT_IMPLEMENTED + C.N);
        else if (!isStruct) {
            out.append(C.T + objectToBeInitialized()).append("* var0 = [[");
            out.append(objectToBeInitialized());
            out.append(" alloc ] init];").append(C.N);
            out.append(emitCallToInternalConstructor());
        }
        out.append(C.END_WRAPPER + C.N);
    }

    private String emitCallToInternalConstructor() throws IOException {
        return C.T + object.getcClassName() + "_INTERNAL_CONSTRUCTOR(me, var0);" + C.N;
    }

    private String objectToBeInitialized() {
        return AdvisorMediator.classHasDelegateMethods(object.name) ? object.name + "Wrapper"
                : object.name;
    }
}
