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
import org.crossmobile.source.ctype.CStruct;

/**
 * 
 * Helper class to help in generating the comments required for the wrappers
 * These comments are essential in order to identify a wrapper during the code
 * injection process.
 * 
 */
public class CUtilsHelper {

    public static final String     BEGIN_WRAPPER   = "\n//XMLVM_BEGIN_WRAPPER";
    public static final String     END_WRAPPER     = "//XMLVM_END_WRAPPER";
    public static final String     NOT_IMPLEMENTED = "\nXMLVM_NOT_IMPLEMENTED();";
    public static final String     BEGIN_IMPL      = "\n//XMLVM_BEGIN_IMPLEMENTATION";
    public static final String     END_IMPL        = "//XMLVM_END_IMPLEMENTATION";

    private static String          objectClassName = null;
    private static List<CArgument> arguments       = null;
    private final static int       METHOD          = 1;
    private final static int       CONSTRUCTOR     = 2;


    /**
     * Method to construct the wrapper comments that are used for identifcation
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
    private static String getWrapperComment(int type, String methodName,
            boolean constructorOverloaded, String enumName) {
        StringBuilder str = new StringBuilder();
        String argType = null;

        if (type == METHOD)
            str.append(BEGIN_WRAPPER + "[" + objectClassName + "_" + methodName + "__");
        if (type == CONSTRUCTOR)
            str.append(BEGIN_WRAPPER + "[" + objectClassName + "___INIT___");
        for (CArgument arg : arguments) {
            argType = arg.getType().toString();
            if (argType.equals("Object"))
                str.append("_java_lang_Object");
            else if (Advisor.isNativeType(argType))
                str.append("_" + argType);
            else if (argType.equals("String"))
                str.append("_java_lang_String");
            else if (CStruct.isStruct(argType))
                str.append("_" + COut.packageName + argType);
            else
                str.append("_java_lang_Object");
        }

        if (constructorOverloaded)
            str.append("_" + objectClassName + "_" + enumName);
        str.append("]");

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
    public static String getWrapperComment(List<CArgument> args, String objClassName,
            String methodName) {
        arguments = args;
        objectClassName = objClassName;
        return getWrapperComment(METHOD, methodName, false, null);
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
    public static String getWrapperComment(List<CArgument> args, String objClassName,
            boolean constructorOverloaded, String enumName) {
        arguments = args;
        objectClassName = objClassName;
        return getWrapperComment(CONSTRUCTOR, null, constructorOverloaded, enumName);

    }
}
