package org.crossmobile.source.cutils;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.crossmobile.source.ctype.CArgument;
import org.crossmobile.source.ctype.CConstructor;
import org.crossmobile.source.ctype.CLibrary;
import org.crossmobile.source.ctype.CObject;
import org.crossmobile.source.ctype.CStruct;

public class CStructOutput {
    private Writer       out;
    private CLibrary     lib;
    private CObject      object;
    private List<String> declarations  = new ArrayList<String>();

    private final String BEGIN_WRAPPER = "\n//XMLVM_BEGIN_WRAPPER";
    private final String END_WRAPPER   = "//XMLVM_END_WRAPPER";


    public CStructOutput(Writer out, CLibrary lib, CObject obj) {
        super();
        this.out = out;
        this.lib = lib;
        this.object = obj;
    }

    public void appendNewObjectCreation() throws IOException {

        out.append(BEGIN_WRAPPER + "[__NEW_" + object.getcClassName() + "]\n");

        for (CArgument var : object.getVariables()) {

            if (CStruct.isStruct(var.getType().toString())) {
                out.append("\tme->fields." + object.getcClassName() + "." + var.name + "_");
                out.append(" = __NEW_" + lib.getPackagename().replace(".", "_") + "_"
                        + var.getType().toString() + "();\n");
            }
        }
        out.append(END_WRAPPER + "\n");
    }

    public void appendContructors() throws IOException {

        int i = 0;
        int j = 1;

        for (CConstructor con : object.getConstructors()) {
            out.append(BEGIN_WRAPPER + "[" + object.getcClassName() + "___INIT___");

            for (CArgument arg : con.getArguments()) {
                out.append("_" + arg.getType().toString());
            }

            out.append("]\n");
            out.append("\t" + object.getcClassName() + "* thiz = me;\n");

            for (CArgument var : object.getVariables()) {

                if (CStruct.isStruct(var.getType().toString())) {
                    CObject obj = lib.getObject(var.getType().toString());
                    out.append("\t" + obj.getcClassName() + "* obj" + i + " = thiz->fields."
                            + object.getcClassName() + "." + var.name + "_;\n");

                    for (CArgument variable : obj.getVariables()) {
                        out.append("\tobj" + i + "->fields." + obj.getcClassName() + "."
                                + variable.name + "_ = n" + (j++) + ";\n");
                    }

                    i++;
                } else {

                    out.append("\tthiz->fields." + object.getcClassName() + "." + var.name
                            + "_ = n" + (j++) + ";\n");
                }
            }
            out.append(BEGIN_WRAPPER + "\n");
        }
    }

    public void appendConversionToObjCObject() throws IOException {

        int i = 0;
        String decl = object.getName() + " " + "to" + object.getName() + "(void *obj)";
        declarations.add(decl);

        out.append(decl + "\n{\n");
        out.append("\t" + object.getcClassName() + "*").append(" cObj = obj;\n");
        out.append("\t" + object.getName() + " toRet;\n");

        if (object.hasVariables()) {

            for (CArgument var : object.getVariables()) {

                if (CStruct.isStruct(var.getType().toString())) {

                    CObject obj = lib.getObject(var.getType().toString());
                    out.append("\t" + obj.getcClassName() + "* obj" + i + " = cObj->fields."
                            + object.getcClassName() + "." + var.name + "_;\n");

                    for (CArgument variable : obj.getVariables()) {
                        out.append("\ttoRet." + var.name + "." + variable.name + " = ");
                        out.append("obj" + i + "->fields." + obj.getcClassName() + "."
                                + variable.name + "_;\n");
                    }

                    i++;
                } else {

                    out.append("\ttoRet." + var.name).append(
                            " = cObj->fields." + object.getcClassName() + "." + var.name + "_;\n");
                }
            }
        }// else
         // System.out.println(object.name + " Structure has no Variables");
        out.append("\treturn toRet;\n");
        out.append("}\n");

    }

    public void appendConversionToJavaObject() throws IOException {

        int i = 0;
        String decl = "JAVA_OBJECT from" + object.getName() + "(" + object.getName() + " obj)";
        declarations.add(decl);

        out.append(decl + "\n{\n");
        out.append("\t" + object.getcClassName() + "* jObj = __NEW_" + object.getcClassName()
                + "();\n");
        out.append("\t" + object.getcClassName() + "___INIT___(jObj);\n");

        if (object.hasVariables()) {

            for (CArgument var : object.getVariables()) {

                if (CStruct.isStruct(var.getType().toString())) {

                    CObject obj = lib.getObject(var.getType().toString());
                    out.append("\t" + obj.getcClassName() + "* obj" + i + " = jObj->fields."
                            + object.getcClassName() + "." + var.name + "_;\n");

                    for (CArgument variable : obj.getVariables()) {
                        out.append("\tobj" + i + "->fields." + obj.getcClassName() + "."
                                + variable.name + "_ = obj." + var.name + "." + variable.name
                                + ";\n");
                    }

                    i++;
                } else {

                    out.append("\tjObj->fields." + object.getcClassName() + "." + var.name
                            + "_ = obj." + var.name + ";\n");
                }
            }
        } // else
          // System.out.println(object.name + " Structure has no Variables");
        out.append("\treturn jObj;\n}\n");
    }

    public List<String> getDeclarations() {
        return declarations;
    }

}
