package org.crossmobile.source.cutils;

import java.util.List;

import org.crossmobile.source.ctype.CArgument;
import org.crossmobile.source.ctype.CLibrary;
import org.crossmobile.source.ctype.CStruct;
import org.crossmobile.source.guru.Advisor;

public class CMethodHelper {

    private String   objectName  = null;
    private String   objectCName = null;
    private CLibrary lib         = null;


    CMethodHelper(String objectName, String objectCName, CLibrary lib) {
        this.objectName = objectName;
        this.objectCName = objectCName;
        this.lib = lib;
    }

    /* parse the return type for the method */
    public String getReturnString(String retType) {
        if (!ignore(retType)) {
            if (retType.equals("Object"))
                return "\nreturn xmlvm_get_associated_c_object";
            else if (Advisor.isNativeType(retType))
                return "\nreturn ";
            else if (CStruct.isStruct(retType))
                return "\nreturn from" + retType;
            else if (retType.equals("String"))
                return "\nreturn fromNSString";
            else
                return "\nreturn xmlvm_get_associated_c_object";
        } else
            return null;

    }

    /* Check if the class needs to be ignored currently */
    boolean ignore(String name) {
        // Array not handled yet!
        if (name.contains("Reference"))
            return true;
        else if (name.contains("[]") || name.contains("...") || Advisor.isInIgnoreList(name)
                || lib.getObject(name).isProtocol())
            return true;
        else
            return false;
    }

    public boolean isProtocol(String name) {
        return lib.getObject(name).isProtocol();
    }

    /* Parse the method name and return the actual method Name */
    public String getMethodString(String methodName) {
        return (objectName + methodName.substring(0, 1).toUpperCase() + methodName.substring(1));
    }

    /*
     * Parse the lis of arguments and return the modified list of parameters to
     * make Objective C call in case of structures
     */
    public String getArgList(List<CArgument> arguments, boolean isStatic) {

        StringBuilder argList = new StringBuilder();
        boolean isFirst = true;
        int i = 1;

        argList.append("(");

        if (!isStatic) {
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

    /*
     * The argument type should be checked for before passing t to the objective
     * C method
     */
    public String parseArgumentType(String argType, int i) {
        if (argType.equals("Object")) {
            return "((org_xmlvm_ios_NSObject*) n" + i
                    + ")->fields.org_xmlvm_ios_NSObject.wrappedObjCObj";
        } else if (Advisor.isNativeType(argType)) {
            return "n" + i;
        } else if (CStruct.isStruct(argType)) {
            return "to" + argType + "(n" + i + ")";
        } else if (argType.contains("String")) {
            return "toNSString" + "(n" + i + ")";
        } else {
            return "((" + objectName + "*) (" + objectCName + "*) n" + i
                    + ")->fields.org_xmlvm_ios_NSObject.wrappedObjCObj";
        }
    }
}