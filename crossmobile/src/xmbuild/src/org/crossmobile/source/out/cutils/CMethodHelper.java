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

import java.util.List;
import java.util.Set;

import org.crossmobile.source.ctype.CArgument;
import org.crossmobile.source.ctype.CFunction;
import org.crossmobile.source.ctype.CLibrary;
import org.crossmobile.source.ctype.CStruct;
import org.crossmobile.source.guru.Advisor;
import org.crossmobile.source.out.COut;

/**
 * This class servers as a helper class for wrapper generation of the methods.
 * The helper class helps in constructing the string for arguments of method,
 * the return statement for a method, the method name (in case the IR represents
 * the method name different from that of actual Objective-C method).
 * 
 */
public class CMethodHelper {

    private String         objectName  = null;
    private String         objectCName = null;
    private CLibrary       lib         = null;
    private Set<CFunction> func        = null;


    CMethodHelper(String objectName, String objectCName, CLibrary lib) {
        this.objectName = objectName;
        this.objectCName = objectCName;
        this.lib = lib;
        this.func = lib.getFunctions();
    }

    /**
     * Inspects the return type of a method and returns the return statement for
     * a particular method
     * 
     * @param retType
     *            - return type of the method
     * @return 'return statement' for a particular method
     */
    public String getReturnString(String retType) {
        if (!ignore(retType)) {
            if (retType.equals("Object"))
                return "return xmlvm_get_associated_c_object (objCObj);";
            else if (Advisor.isNativeType(retType))
                return "return (objCObj);";
            else if (CStruct.isStruct(retType))
                return "return from" + retType + "(objCObj);";
            else if (retType.equals("String"))
                return "return fromNSString (objCObj);";
            else
                return "return xmlvm_get_associated_c_object (objCObj);";
        } else
            return null;

    }

    /**
     * Checks if a particular class has to be ignored while generation of
     * wrappers. AT this moment there are some special cases which are not
     * handled by the code generation and the list is maintained in the Advice
     * temporarily.
     * 
     * @param name
     * @return true if class is ignored; false otherwise.
     */
    boolean ignore(String name) {
        // Array not handled yet!
        if (name.contains("Reference"))
            return true;
        else if (name.contains("[]") || name.contains("...") || Advisor.isInIgnoreList(name)
                || lib.getObject(name).isProtocol() || name.equals("Map") || name.equals("List")
                || name.equals("Set"))
            return true;
        else
            return false;
    }

    /**
     * Some C functions have name difference between java and objectiveC
     * counterparts. In such cases, this method takes in the java API's function
     * name and return the Obj-C function name.
     * 
     * @param methodName
     *            - methodName in java API (as represented in the IR)
     * @param isStruct
     *            - if the parent is a struct. This is required because,
     *            functions associated with structs have the struc name
     *            prepended to them.
     * @return returns the processed function name if it differs; Null
     *         otherwise.
     */
    public String getModifiedFunctionName(String methodName, boolean isStruct) {
        if (isStruct)
            return (objectName + methodName.substring(0, 1).toUpperCase() + methodName.substring(1));
        else
            for (CFunction f : func) {
                if (f.name.toUpperCase().endsWith(methodName.toUpperCase()))
                    return f.name;
            }
        return null;
    }

    /**
     * Parses the list of arguments for a C-function and returns the string
     * containing comma separated argument list
     * 
     * @param arguments
     *            - List of arguments for a function
     * @param isStatic
     *            - if the method is static.
     * @param parentIsStruct
     *            - if the method is associated with a structure
     * @return returns the string with comma separated arguments; If the type of
     *         argument is currently in ignore list then return null.
     */
    public String getArgList(List<CArgument> arguments, boolean isStatic, boolean parentIsStruct) {

        StringBuilder argList = new StringBuilder();
        boolean isFirst = true;
        int i = 1;

        argList.append("(");

        if (!isStatic && parentIsStruct) {
            argList.append("to" + objectName + "(me)");
            isFirst = false;
        }
        if (arguments.isEmpty())
            argList.append(")");

        else {
            for (CArgument arg : arguments) {

                String argType = arg.getType().toString();
                String parsedArg = null;

                if (!isFirst)
                    argList.append(",");
                isFirst = false;

                if (!ignore(argType) && (parsedArg = parseArgumentType(argType, i)) != null) {
                    argList.append(parsedArg);
                    i++;
                } else
                    return null;
            }
            argList.append(")");
        }
        return argList.toString();
    }

    /**
     * The necessary processing needs to be done based on the data type of the
     * argument
     * 
     * @param argType
     *            - type of the argument
     * @param i
     *            - argument position
     * @return - returns processed string for a particular argument
     */
    public String parseArgumentType(String argType, int i) {
        if (argType.equals("Object")) {
            return "((" + COut.packageName + "NSObject*) n" + i + ")->fields." + COut.packageName
                    + "NSObject.wrappedObjCObj";
        } else if (Advisor.isNativeType(argType)) {
            return "n" + i;
        } else if (CStruct.isStruct(argType)) {
            return "to" + argType + "(n" + i + ")";
        } else if (argType.contains("String")) {
            return "toNSString" + "(n" + i + ")";
        } else {
            return "((" + objectName + "*) (" + objectCName + "*) n" + i + ")->fields."
                    + COut.packageName + "NSObject.wrappedObjCObj";
        }
    }

    /**
     * There are some variants in data types such as byte and boolean is Byte
     * and BOOL in Objective-C
     * 
     * @param dataType
     *            - dataType for which a mapping needs to be returned
     * @return if there is a mapping, return the mapped data type; else return
     *         the type as is.
     */
    public static String getMappedDataType(String dataType) {
        String mappedType = null;
        if ((mappedType = Advisor.getDataTypeMapping(dataType)) != null) {
            if (mappedType == "") {
                return null;
            } else {
                return mappedType;
            }
        } else {
            return dataType;
        }
    }

    /**
     * Constructs the return variable with appropriate return type
     * 
     * @param returnType
     *            - return type of the method
     * @return - constructed string for the return variable
     */
    public static String getReturnVariable(String returnType) {
        String mappedType = null;
        StringBuilder returnVariable = new StringBuilder();

        if (!returnType.equals("void")) {
            if ((mappedType = getMappedDataType(returnType)) != null)
                returnVariable.append("\n\t" + mappedType);
            else
                return null;

            if ((!Advisor.isNativeType(returnType) && !CStruct.isStruct(returnType))
                    || returnType.equals("Object"))
                returnVariable.append("*");

            returnVariable.append(" objCObj = ");
        } else
            returnVariable.append("");
        return returnVariable.toString();
    }
}