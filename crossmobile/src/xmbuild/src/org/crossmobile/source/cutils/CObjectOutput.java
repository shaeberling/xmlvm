package org.crossmobile.source.cutils;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.crossmobile.source.ctype.CArgument;
import org.crossmobile.source.ctype.CConstructor;
import org.crossmobile.source.ctype.CEnum;
import org.crossmobile.source.ctype.CLibrary;
import org.crossmobile.source.ctype.CMethod;
import org.crossmobile.source.ctype.CObject;
import org.crossmobile.source.ctype.CProperty;
import org.crossmobile.source.ctype.CStruct;
import org.crossmobile.source.guru.Advisor;

public class CObjectOutput {
    private Writer   out             = null;
    private CLibrary lib             = null;
    private CObject  object          = null;
    private String   objectClassName = null;


    public CObjectOutput(Writer out, CLibrary lib, CObject obj) {
        super();
        this.out = out;
        this.lib = lib;
        this.object = obj;
        this.objectClassName = object.getcClassName();
    }

    /*
     * Wrapper creator and Internal Constructor need to be appended for each of
     * the classes which are not structs.
     */
    public void appendWrapperCreator() throws IOException {
        if (!object.name.contains("NSObject")) {
            out.append("void " + objectClassName + "_INTERNAL_CONSTRUCTOR(JAVA_OBJECT me,");
            out.append(" NSObject* wrappedObjCObj){\n\t");
            out.append("org_xmlvm_ios_NSObject_INTERNAL_CONSTRUCTOR(me, wrappedObjCObj);\n}");

            out.append("\n\nstatic JAVA_OBJECT __WRAPPER_CREATOR(NSObject* obj)\n{");
            out.append("\t\tif([obj class] == [" + object.name + " class]) {\n");
            out.append("\t\t[obj retain];\n");
            out.append("\t\tJAVA_OBJECT jobj = __NEW_" + objectClassName + "();\n\t\t");
            out.append(objectClassName + "_INTERNAL_CONSTRUCTOR(jobj, obj);\n");
            out.append("\t\treturn jobj;\n\t}");
            out.append("return JAVA_NULL;\n}\n");
        }
    }

    /*
     * The functions are appended for each of the classes
     */
    public void appendFunction() throws IOException {

        /* Parse the methods */
        for (CMethod method : object.getMethods()) {

            String returnType = method.getReturnType().toString();

            out.append(CUtilsHelper.getWrapperComment(method.getArguments(), objectClassName,
                    method.name));

            if (method.getArguments().isEmpty() && !method.isStatic()
                    && Advisor.isNativeType(returnType)) {
                out.append("\nXMLVM_VAR_THIZ;\n");

                /*
                 * Check if the method is a property, call appropriate getters
                 * and setters
                 */
                if (method.isProperty()) {
                    out.append("return [thiz " + CProperty.getPropertyDef(method.name) + "];\n");
                } else {
                    out.append("return [thiz " + method.name + "];\n");
                }
            }
            /*
             * If the implementation is not provided then add throw error!
             */
            else {
                out.append(CUtilsHelper.NOT_IMPLEMENTED + "\n");
            }
            out.append(CUtilsHelper.END_WRAPPER + "\n");

        }
    }

    /*
     * Append the Constructors
     */
    public void appendConstructor() throws IOException {

        boolean has_default_constructor = false;
        List<CArgument> arguments = null;
        CEnum cEnum = null;
        Map<String, List<String>> namePartsMap = new HashMap<String, List<String>>();

        for (CConstructor con : object.getConstructors()) {
            arguments = con.getArguments();

            /*
             * If there is default constructor, then need not add explicitly
             */
            if (arguments.isEmpty())
                has_default_constructor = true;

            /*
             * Some constructors are overloaded. Handle such cases. Need to
             * generate 'if-else' in the wrapper to check the enum value to
             * identify the right constructor.
             */
            if (con.isOverloaded()) {
                cEnum = con.getEnum();
                if (cEnum != null)
                    namePartsMap = cEnum.getNameParts();
            }

            out.append(CUtilsHelper.getWrapperComment(arguments, objectClassName, con
                    .isOverloaded(), cEnum == null ? null : cEnum.name));

            if (con.isOverloaded()) {
                Iterator it = namePartsMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pairs = (Map.Entry) it.next();
                    out.append("\n" + "if((" + objectClassName + "_" + cEnum.name + "*) n"
                            + (arguments.size() + 1) + " == ");
                    out.append(objectClassName + "_" + cEnum.name + "_GET_" + pairs.getKey()
                            + "()){\n");
                    callObjCConstructor((List<String>) pairs.getValue(), arguments);
                    out.append("}\n");
                }
            } else {
                callObjCConstructor(con.getNameParts(), arguments);
            }

            out.append(CUtilsHelper.END_WRAPPER + "\n");
        }

        if (!has_default_constructor)
            appendDefaultConstructor(out, object);
    }

    /*
     * Generate the actual call to bjective C constructor
     */
    private void callObjCConstructor(List<String> nameParts, List<CArgument> arguments)
            throws IOException {
        StringBuilder string = new StringBuilder();
        String argType = null;
        int i = 1;
        boolean flag = true;
        boolean implemented = true;
        string.append("\n" + object.name + "* objCObj = [[" + object.name + " alloc]");

        ListIterator<CArgument> iterator = arguments.listIterator();

        for (String namePart : nameParts) {

            if (!arguments.isEmpty()) {
                if (iterator.hasNext()) {

                    CArgument argument = (CArgument) iterator.next();

                    string.append(" " + namePart + ":");
                    argType = argument.getType().toString();

                    /*
                     * Currently cases where the arguments are native types
                     * structures, Strings or other objects are handled. Arrays,
                     * variable list arguments are not handled.
                     */
                    if (Advisor.isNativeType(argType)) {
                        string.append("n" + i);
                    } else if (CStruct.isStruct(argType)) {
                        string.append("to" + argType + "(n" + i + ")");
                    } else if (argType.equals("String")) {
                        string.append("toNSString(n" + i + ")");
                    } else if (!(argType.contains("[]")) && !(argType.contains("..."))) {
                        // Array not handled yet!
                        string.append("XMLVM_VAR_IOS(" + argType + ", obj, n" + i + ")");
                    } else {
                        string.delete(0, string.length());
                        string.append("\nXMLVM_NOT_IMPLEMENTED()");
                        implemented = false;
                        flag = false;
                        break;
                    }
                    i++;
                }
            } else {
                string.append("init]");
                flag = false;
            }
        }

        if (flag == true) {
            string.append("]");
        }

        string.append(";\n");
        out.append(string);

        if (implemented) {
            out.append(objectClassName + "_INTERNAL_CONSTRUCTOR(me, objCObj);\n");
        }
    }

    private void appendDefaultConstructor(Writer out, CObject object) throws IOException {
        out.append(CUtilsHelper.BEGIN_WRAPPER + "[" + objectClassName + "___INIT___");
        out.append("]\n");
        out.append(CUtilsHelper.END_WRAPPER + "\n");
    }

    /*
     * Code has to be injected to register the wrapper creator
     */
    public void appendWrapperRegistration() throws IOException {
        out.append(CUtilsHelper.BEGIN_WRAPPER + "[__INIT_" + objectClassName + "]\n");
        if (!object.name.contains("NSObject"))
            out.append("xmlvm_register_wrapper_creator(__WRAPPER_CREATOR);\n");
        out.append(CUtilsHelper.END_WRAPPER + "\n");

        out.append(CUtilsHelper.BEGIN_WRAPPER + "[__DELETE_" + objectClassName + "]\n");
        if (!object.name.contains("NSObject"))
            out.append("__DELETE_org_xmlvm_ios_NSObject(me, client_data);\n");
        out.append(CUtilsHelper.END_WRAPPER + "\n");
    }

}
