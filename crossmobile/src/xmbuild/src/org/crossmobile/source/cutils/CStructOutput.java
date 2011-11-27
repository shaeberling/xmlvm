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
    private Writer        out             = null;
    private CLibrary      lib             = null;
    private CObject       object          = null;
    private String        objectClassName = null;
    private CMethodHelper methodHelper    = null;
    static int            n               = 0;
    static int            m               = 0;


    public CStructOutput(Writer out, CLibrary lib, CObject obj) {
        super();
        this.out = out;
        this.lib = lib;
        this.object = obj;
        this.objectClassName = object.getcClassName();
        this.methodHelper = new CMethodHelper(this.object.name, this.objectClassName, this.lib);
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
            String returnString = "";
            String methodString = null;
            String argList = null;
            Boolean notImplemented = false;

            out.append(CUtilsHelper.getWrapperComment(method.getArguments(), objectClassName,
                    method.name));
            if (!returnType.equals("void")) {
                returnString = methodHelper.getReturnString(returnType);
                if (returnString == null)
                    notImplemented = true;
            }
            methodString = methodHelper.getMethodString(method.name);

            List<CArgument> arguments = method.getArguments();
            argList = methodHelper.getArgList(arguments, method.isStatic());
            if (argList == null)
                notImplemented = true;

            if (notImplemented)
                out.append(CUtilsHelper.NOT_IMPLEMENTED + "\n");
            else {
                out.append(returnString + "(" + methodString + argList + ");\n");
            }
            out.append(CUtilsHelper.END_WRAPPER + "\n");

        }
    }

    /*
     * Append the constructors for the structures TODO: Need to structure the
     * code in order to call the original 'make' for the structures.
     */
    public void appendContructors() throws IOException {

        int i = 1;
        List<CArgument> arguments = null;

        for (CConstructor con : object.getConstructors()) {

            arguments = con.getArguments();

            out.append(CUtilsHelper.getWrapperComment(arguments, objectClassName, false, null));

            out.append("\n" + object.name + " objCObj = " + object.name + "Make(");
            boolean isFirst = true;
            for (CArgument arg : con.getArguments()) {
                if (!isFirst)
                    out.append(",");
                isFirst = false;

                if (Advisor.isNativeType(arg.getType().toString())) {
                    out.append("n" + (i++));
                } else if (CStruct.isStruct(arg.getType().toString()))
                    out.append("to" + arg.getType().toString() + "(n" + (i++) + ")");
                else
                    out.append(objectClassName + "* n" + (i++)
                            + ")->fields.org_xmlvm_ios_NSObject.wrappedObjCObj");
            }
            out.append(");\n");

            out.append(objectClassName + "* jObj = me;\n");

            for (CArgument arg : object.getVariables()) {
                out.append("jObj->fields." + objectClassName + "." + arg.name + "_ = ");
                if (Advisor.isNativeType(arg.getType().toString())) {
                    out.append("objCObj." + arg.name + ";\n");
                } else if (CStruct.isStruct(arg.getType().toString()))
                    out.append("from" + arg.getType().toString() + "(objCObj." + arg.name + ");\n");
                else
                    out.append("xmlvm_get_associated_c_object(" + "objCObj." + arg.name + ");\n");

            }
            out.append(CUtilsHelper.END_WRAPPER + "\n");
        }
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
