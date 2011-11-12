package org.crossmobile.source.cutils;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.crossmobile.source.ctype.CArgument;
import org.crossmobile.source.ctype.CConstructor;
import org.crossmobile.source.ctype.CLibrary;
import org.crossmobile.source.ctype.CMethod;
import org.crossmobile.source.ctype.CObject;
import org.crossmobile.source.ctype.CStruct;
import org.crossmobile.source.guru.Advisor;

public class CStructOutput {
    private Writer   out;
    private CLibrary lib;
    private CObject  object;
    private String   objectClassName = null;


    public CStructOutput(Writer out, CLibrary lib, CObject obj) {
        super();
        this.out = out;
        this.lib = lib;
        this.object = obj;
        this.objectClassName = object.getcClassName();
    }

    public void appendNewObjectCreation() throws IOException {

        out.append(CUtilsHelper.BEGIN_WRAPPER + "[__NEW_" + objectClassName + "]\n");
        for (CArgument var : object.getVariables()) {

            if (CStruct.isStruct(var.getType().toString())) {
                out.append("\tme->fields." + objectClassName + "." + var.name + "_");
                out.append(" = __NEW_" + lib.getPackagename().replace(".", "_") + "_"
                        + var.getType().toString() + "();\n");
            }
        }
        out.append(CUtilsHelper.END_WRAPPER + "\n");
    }

    /*
     * Append the implementation for function calls
     */
    public void appendFunction() throws IOException {

        for (CMethod method : object.getMethods()) {

            String returnType = method.getReturnType().toString();

            out.append(CUtilsHelper.getWrapperComment(method.getArguments(), objectClassName,
                    method.name));

            /*
             * Currently only methods that do not take arguments and return type
             * is native type or a structure are handled
             */
            if (method.getArguments().isEmpty()) {
                String methodCall = object.name + method.name.substring(0, 1).toUpperCase()
                        + method.name.substring(1) + "(to" + object.name + "(me))";

                if (returnType.equals("void")) {
                    out.append("\n" + methodCall + ";\n");
                } else if (Advisor.isNativeType(returnType)) {
                    out.append("\nreturn " + methodCall + ";\n");
                } else if (CStruct.isStruct(returnType)) {
                    out.append("\nreturn from" + returnType + "(" + methodCall + ");\n");
                } else {
                    out.append(CUtilsHelper.NOT_IMPLEMENTED + "\n");
                }
            } else {
                out.append(CUtilsHelper.NOT_IMPLEMENTED + "\n");
            }
            out.append(CUtilsHelper.END_WRAPPER + "\n");

        }
    }

    /*
     * Append the constructors for the structures TODO: Need to structure the
     * code in order to call the original 'make' for the structures.
     */
    public void appendContructors() throws IOException {

        int i = 0;
        int j = 1;
        boolean has_default_constructor = false;
        List<CArgument> arguments = null;

        for (CConstructor con : object.getConstructors()) {

            arguments = con.getArguments();

            if (arguments.isEmpty())
                has_default_constructor = true;

            out.append(CUtilsHelper.getWrapperComment(arguments, objectClassName, false, null));

            out.append("\t" + objectClassName + "* thiz = me;\n");

            /* Check for structure with a structure */
            for (CArgument var : object.getVariables()) {

                if (CStruct.isStruct(var.getType().toString())) {
                    CObject obj = lib.getObject(var.getType().toString());
                    out.append("\t" + obj.getcClassName() + "* obj" + i + " = thiz->fields."
                            + objectClassName + "." + var.name + "_;\n");

                    for (CArgument variable : obj.getVariables()) {
                        out.append("\tobj" + i + "->fields." + obj.getcClassName() + "."
                                + variable.name + "_ = n" + (j++) + ";\n");
                    }

                    i++;
                } else {

                    out.append("\tthiz->fields." + objectClassName + "." + var.name + "_ = n"
                            + (j++) + ";\n");
                }
            }
            out.append(CUtilsHelper.END_WRAPPER + "\n");
        }
        if (!has_default_constructor)
            appendDefaultConstructor(out, object);
    }

    private void appendDefaultConstructor(Writer out, CObject object) throws IOException {
        out.append(CUtilsHelper.BEGIN_WRAPPER + "[" + objectClassName + "___INIT___");
        out.append("]\n");
        out.append(CUtilsHelper.END_WRAPPER + "\n");
    }

    /*
     * For structure, we need conversion to the ObjectiveC Object
     */
    public void appendConversionToObjCObject() throws IOException {

        int i = 0;
        String decl = object.name + " " + "to" + object.name + "(void *obj)";

        out.append(decl + "\n{\n");
        out.append("\t" + objectClassName + "*").append(" cObj = obj;\n");
        out.append("\t" + object.getName() + " toRet;\n");

        if (object.hasVariables()) {

            for (CArgument var : object.getVariables()) {

                String variableType = var.getType().toString();

                if (CStruct.isStruct(variableType)) {

                    CObject obj = lib.getObject(variableType);
                    out.append("\t" + obj.getcClassName() + "* obj" + i + " = cObj->fields."
                            + objectClassName + "." + var.name + "_;\n");

                    for (CArgument variable : obj.getVariables()) {
                        out.append("\ttoRet." + var.name + "." + variable.name + " = ");
                        out.append("obj" + i + "->fields." + obj.getcClassName() + "."
                                + variable.name + "_;\n");
                    }

                    i++;
                } else {

                    out.append("\ttoRet." + var.name).append(
                            " = cObj->fields." + objectClassName + "." + var.name + "_;\n");
                }
            }
        }

        out.append("\treturn toRet;\n");
        out.append("}\n");

    }

    /*
     * For Structures we need conversion from ObjectiveC object
     */
    public void appendConversionToJavaObject() throws IOException {

        int i = 0;
        String decl = "JAVA_OBJECT from" + object.getName() + "(" + object.getName() + " obj)";

        out.append(decl + "\n{\n");
        out.append("\t" + objectClassName + "* jObj = __NEW_" + objectClassName + "();\n");
        out.append("\t" + objectClassName + "___INIT___(jObj);\n");

        if (object.hasVariables()) {

            for (CArgument var : object.getVariables()) {

                String variableType = var.getType().toString();
                if (CStruct.isStruct(variableType)) {

                    CObject obj = lib.getObject(variableType);
                    out.append("\t" + obj.getcClassName() + "* obj" + i + " = jObj->fields."
                            + objectClassName + "." + var.name + "_;\n");

                    for (CArgument variable : obj.getVariables()) {
                        out.append("\tobj" + i + "->fields." + obj.getcClassName() + "."
                                + variable.name + "_ = obj." + var.name + "." + variable.name
                                + ";\n");
                    }

                    i++;
                } else {

                    out.append("\tjObj->fields." + objectClassName + "." + var.name + "_ = obj."
                            + var.name + ";\n");
                }
            }
        }
        out.append("\treturn jObj;\n}\n");
    }
}
