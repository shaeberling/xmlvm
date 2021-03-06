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

import org.crossmobile.source.ctype.CArgument;
import org.crossmobile.source.guru.Advisor;
import org.crossmobile.source.out.COut;
import org.crossmobile.source.xtype.XArg;
import org.crossmobile.source.ctype.CStruct;

/**
 * 
 * Helper class to help in generating the comments required for the wrappers
 * These comments are essential in order to identify a wrapper during the code
 * injection process.
 * 
 */
public class CUtilsHelper {

    private static String  objectClassName = null;
    private static List<?> arguments       = null;


    /**
     * Method to construct the wrapper comments that are used for identification
     * during code injection process
     * 
     * @param type
     *            - indicates if it is a method or a constructor
     * @param methodName
     *            - If a method, the name of the method
     * @param constructorOverloaded
     *            - In some cases, we have overloaded constructors in which
     *            case, we use the enums to distinguish
     * @param enumName
     *            - in case constructor is overloaded, the enum name
     * @return constructed string of the comment
     */
    @SuppressWarnings("unchecked")
    private static String getWrapperComment(String methodName, String enumName, boolean isOverridden) {
        StringBuilder str = new StringBuilder();

        str.append(C.BEGIN_WRAPPER + "[" + objectClassName + "_" + methodName + "_");
        if (isOverridden)
            str.append(objectClassName + "_");
        str.append("_");

        if (arguments != null && !arguments.isEmpty()) {
            if (arguments.get(0) instanceof CArgument) {
                for (CArgument arg : (List<CArgument>) arguments)
                    str.append(getWrapperCommentArgument(arg.getType().toString()));
            } else if (arguments.get(0) instanceof XArg) {
                for (XArg arg : (List<XArg>) arguments)
                    str.append(getWrapperCommentArgument(arg.getType()));
            }
        }
        if (enumName != null)
            str.append("_" + objectClassName + "_" + enumName);
        str.append("]" + C.N);

        return str.toString();

    }

    /**
     * This method is used to construct the wrapper comment required for
     * identification of a method during code injection process. If methods need
     * a wrapper comment then this particular method is called
     * 
     * @param args
     *            - list of arguments of a method
     * @param objClassName
     *            - class the method belongs to
     * @param methodName
     *            - name of the method
     * @return constructed string of the comment
     */
    public static String getWrapperComment(List<?> args, String objClassName, String methodName,
            boolean overridden) {
        arguments = args;
        objectClassName = objClassName;
        return getWrapperComment(methodName, null, overridden);
    }

    /**
     * This method is used to construct the wrapper comment required for
     * identification of a method during code injection process. If a
     * constructor need a wrapper comment then this particular method is called
     * 
     * @param args
     *            - list of arguments for a constructor
     * @param objClassName
     *            - name of the class
     * @param constructorOverloaded
     *            - if constructor is overloaded
     * @param enumName
     *            - Enum name in case constructor is overloaded
     * @return constructed string of the comment
     */
    public static String getWrapperComment(List<?> args, String objClassName, String enumName) {
        arguments = args;
        objectClassName = objClassName;
        return getWrapperComment("__INIT_", enumName, false);

    }

    public static String getWrapperCommentArgument(String argType) {
        // May be move this mapping to Advisor later!
        String replacedArgType = argType.replace("[]", "");

        if (argType.equals("Object"))
            return "_java_lang_Object";
        else if (Advisor.isNativeType(argType))
            return "_" + argType;
        else if (argType.equals("String"))
            return "_java_lang_String";
        else if (argType.contains("String") && argType.contains("[][]"))
            return "_java_lang_String_2ARRAY";
        else if (argType.contains("String") && argType.contains("[]"))
            return "_java_lang_String_1ARRAY";
        else if (Advisor.isNativeType(replacedArgType) && argType.contains("[][]"))
            return "_" + replacedArgType + "_2ARRAY";
        else if (Advisor.isNativeType(replacedArgType) && argType.contains("[]"))
            return "_" + replacedArgType + "_1ARRAY";
        else if (CStruct.isStruct(argType))
            return "_" + COut.packageName + argType;
        else if (argType.equals("List"))
            return "_java_util_List";
        else if (argType.equals("Map"))
            return "_java_util_Map";
        else if (argType.equals("Set"))
            return "_java_util_Set";
        else if (argType.matches("Class<.*>"))
            return "_java_lang_Class";
        else if (CMethodHelper.isDoublePointer(argType))
            return "_" + COut.packageName + argType.substring(0, argType.indexOf("<"));
        else if (!argType.contains("[]") || !argType.contains("..."))
            return "_" + COut.packageName + argType;
        else
            return null;
    }
}
