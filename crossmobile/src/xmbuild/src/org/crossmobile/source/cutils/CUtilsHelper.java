package org.crossmobile.source.cutils;

import java.util.List;

import org.crossmobile.source.ctype.CArgument;
import org.crossmobile.source.guru.Advisor;

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
            if (Advisor.isNativeType(argType)) {
                str.append("_" + argType);
            } else if (argType.equals("String"))
                str.append("_java_lang_String");
            else
                str.append("_java_lang_Object");
        }

        if (constructorOverloaded)
            str.append("_" + objectClassName + "_" + enumName);
        str.append("]\n");

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
