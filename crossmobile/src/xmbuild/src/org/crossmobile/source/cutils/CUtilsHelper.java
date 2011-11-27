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

package org.crossmobile.source.cutils;

import java.util.List;

import org.crossmobile.source.ctype.CArgument;
import org.crossmobile.source.guru.Advisor;
import org.crossmobile.source.ctype.CStruct;

/*
 * Helper class to get the comments required  for the wrappers
 */
public class CUtilsHelper {

    public static final String     BEGIN_WRAPPER   = "\n//XMLVM_BEGIN_WRAPPER";
    public static final String     END_WRAPPER     = "//XMLVM_END_WRAPPER";
    public static final String     NOT_IMPLEMENTED = "\nXMLVM_NOT_IMPLEMENTED();";

    private static String          objectClassName = null;
    private static List<CArgument> arguments       = null;
    private final static int       METHOD          = 1;
    private final static int       CONSTRUCTOR     = 2;


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
                str.append("_org_xmlvm_ios_" + argType);
            else
                str.append("_java_lang_Object");
        }

        if (constructorOverloaded)
            str.append("_" + objectClassName + "_" + enumName);
        str.append("]");

        return str.toString();

    }

    public static String getWrapperComment(List<CArgument> args, String objClassName,
            String methodName) {
        arguments = args;
        objectClassName = objClassName;
        return getWrapperComment(METHOD, methodName, false, null);
    }

    public static String getWrapperComment(List<CArgument> args, String objClassName,
            boolean constructorOverloaded, String enumName) {
        arguments = args;
        objectClassName = objClassName;
        return getWrapperComment(CONSTRUCTOR, null, constructorOverloaded, enumName);

    }
}
