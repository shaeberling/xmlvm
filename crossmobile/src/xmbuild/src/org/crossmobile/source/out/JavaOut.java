/* Copyright (c) 2011 by crossmobile.org
 *
 * CrossMobile is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 2.
 *
 * CrossMobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CrossMobile; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.crossmobile.source.out;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.crossmobile.source.ctype.CArgument;
import org.crossmobile.source.ctype.CConstructor;
import org.crossmobile.source.ctype.CEnum;
import org.crossmobile.source.ctype.CLibrary;
import org.crossmobile.source.ctype.CMethod;
import org.crossmobile.source.ctype.CObject;
import org.crossmobile.source.ctype.CType;
import org.crossmobile.source.ctype.ObjCSelector;
import org.crossmobile.source.ctype.ObjCSelector.Parameter;
import org.crossmobile.source.guru.Advisor;
import org.crossmobile.source.guru.Reporter;
import org.crossmobile.source.guru.Reporter.Tuplet;
import org.crossmobile.source.out.cutils.ObjCSelectorUtil;
import org.crossmobile.source.utils.FileUtils;
import org.crossmobile.source.utils.WriteCallBack;
import org.crossmobile.source.xtype.AdvisorWrapper;
import org.crossmobile.source.xtype.XArg;
import org.crossmobile.source.xtype.XInjectedMethod;

public class JavaOut implements Generator {

    protected final static String DUMMYBODY = "{\n\t\tthrow new RuntimeException(\"Stub\");\n\t}\n";
    protected final static String ABSTRACTBODY = ";\n";
    private final String outdir;
    private String objectprefix = "";
    private String methodprefix = "";
    private String constructorprefix = "";

    public JavaOut(String outdir) {
        this.outdir = outdir;
    }

    @Override
    public void generate(final CLibrary library) {
        File out = new File(outdir);
        FileUtils.delete(out);

        for (CObject o : library.getObjects()) {
            final CObject fo = o;
            FileUtils.putFile(new File(out, o.getName() + ".java"), new WriteCallBack<Writer>() {

                @Override
                public void exec(Writer out) throws IOException {
                    parseObject(library, fo, out);
                }
            });
        }
        FileUtils.putFile(new File(out, "Reference.java"), new WriteCallBack<Writer>() {

            @Override
            public void exec(Writer out) throws IOException {
                out.append("package ").append(library.getPackagename()).append(";\n\n");
                out.append(FileUtils.getReader(getClass().getResourceAsStream("/org/crossmobile/source/extra/Reference.txt")));
            }
        });
    }

    public void report() {
        for (Reporter r : Reporter.values()) {
            final Reporter fr = r;
            FileUtils.putFile(new File(outdir, "report" + File.separator + r.name().toLowerCase() + ".xml"), new WriteCallBack<Writer>() {

                @Override
                public void exec(Writer out) throws IOException {
                    parseReporter(fr, out);
                }
            });
        }
    }

    private void parseObject(CLibrary library, CObject object, Writer out) throws IOException {
        out.append("package ").append(library.getPackagename()).append(";\n");
        out.append("import java.util.*;\n\n");
        
        out.append(objectprefix);

        List<String> references = null;
        if((references = AdvisorWrapper.getReferencesForObject(object.name)) != null){
            out.append("(references={");
            for(int i=0; i<references.size(); i++){
                out.append(references.get(i) + ".class");
                if(i < references.size()-1)
                    out.append(",");
            }
            out.append("})\n");
        }
        
        String type = "class";
        if (object.isProtocol()) {
            type = object.hasOptionalMethod() ? "abstract class" : "interface";
            out.append("@org.xmlvm.XMLVMDelegate(protocolType = \"" + object.getName() + "\")\n");
        }
        out.append("public ").append(type).append(" ");

        out.append(object.getName());
        if (object.getGenericsCount() > 0) {
            out.append("<");
            int size = object.getGenericsCount();
            for (int i = 0; i < size; i++) {
                out.append((char) ('A' + i)).append(",");
                if (i < size)
                    out.append(",");
            }
            out.append(">");
        }
        boolean alreadyDefinedExtensionForInterface = false;
        if (object.getSuperclass() != null) {
            out.append(" extends ");
            parseType(object.getSuperclass(), true, out);
            if (object.isProtocol())
                alreadyDefinedExtensionForInterface = true;
        }

//        if (object.getInterfaces().size() > 0) {
//            if (alreadyDefinedExtensionForInterface)
//                out.append(", ");
//            else
//                out.append(object.isProtocol() ? " extends " : " implements ");
//
//            int size = object.getInterfaces().size();
//            int count = 0;
//            for (CType interf : object.getInterfaces()) {
//                parseType(interf, true, out);
//                count++;
//                if (count < size)
//                    out.append(", ");
//            }
//        }
        out.append(" {\n");

        if (object.hasConstructorEnums()) {
            out.append("\n\t/*\n\t * Initialization enumerations\n\t */\n");
            for (CConstructor c : object.getConstructors())
                if (c.getEnum() != null)
                    parseEnum(c.getEnum(), out);
        }

        if (object.hasVariables()) {
            out.append("\n\t/*\n\t * Variables\n\t */\n");
            for (CArgument var : object.getVariables()) {
                out.append("\t public ");
                parseArgument(var, out);
                out.append(";\n");
            }
        }

        if (object.hasStaticMethods()) {
            out.append("\n\t/*\n\t * Static methods\n\t */\n");
            for (CMethod m : object.getMethods())
                if (m.isStatic())
                    parseMethod(object, m, out);
        }

        if (!object.isProtocol()) {
            out.append("\n\t/*\n\t * Constructors\n\t */\n");
            boolean has_default_constructor = false;
            boolean hasSuperclass = object.getSuperclass() != null ? true : false;
            if (hasSuperclass) {
                // SuperClass Constructors
                Map<String, Boolean> subclassArgTypes = new HashMap<String, Boolean>();
                for (CConstructor c:  object.getConstructors()) 
                    subclassArgTypes.put(c.getCommaSeparatedArgumentTypes(), true);
                parseSuperClassConstructor(library.getObjectIfPresent(object.getSuperclass().getProcessedName()), object, library, subclassArgTypes, out);
            }            
            for (CConstructor c : object.getConstructors()) {
                if (c.getArguments().isEmpty())
                    has_default_constructor = true;
                parseConstructor(object, c, out);
            }
            if (!has_default_constructor)
                parseDefaultConstructor(object.getName(), hasSuperclass, out);
        }

        if (object.hasProperties()) {
            out.append("\n\t/*\n\t * Properties\n\t */\n");
            for (CMethod m : object.getMethods())
                if (m.isProperty())
                    parseMethod(object, m, out);
        }

        if (object.hasInstanceMethods()) {
            out.append("\n\t/*\n\t * Instance methods\n\t */\n");
            for (CMethod m : object.getMethods())
                if (!m.isStatic() && !m.isProperty())
                    parseMethod(object, m, out);
        }
        
        if (AdvisorWrapper.classHasExternallyInjectedCode(object.name)){
            out.append("\n\t/*\n\t * Injected methods\n\t */\n");
            List<XInjectedMethod> iMethods = AdvisorWrapper.getExternallyInjectedCode(object.name);
            for (XInjectedMethod im : iMethods){
                parseInjectedMethods(object, im, out);
            }
        }
        out.append("}\n");
    }

    /**
     * Every subclass needs to have a constructor that corresponds to the
     * constructor in all of the superclass along the inheritance tree. This
     * method is used to emit constructors in subclasses corresponding to
     * constructors in superclass. The method is called recursively to add all
     * the constructors to a subclass along its inheritance tree.
     * 
     * @param superclass
     *            - CObject instance of the superclass of the current object
     * @param object
     *            - The current object being parsed
     * @param lib
     *            - The CLibrary instance
     * @param subclassArgTypes
     *            - Map containing the comma separated argument types for all
     *            the constructors that are present in the current object. This
     *            is required to prevent generation of constructors
     *            corresponding to superclass where the signatures conflict with
     *            that in subclass
     * @param out
     * @throws IOException
     */
    private void parseSuperClassConstructor(CObject superclass, CObject object, CLibrary lib, Map<String, Boolean> subclassArgTypes, Writer out) throws IOException {        
        for (CConstructor c : superclass.getConstructors()) {
            if (!c.getArguments().isEmpty()) {
                List<CArgument> args = c.getArguments();
                int size = args.size();
                
                if(subclassArgTypes.containsKey(c.getCommaSeparatedArgumentTypes()))
                    continue;               
                out.append("\tpublic ").append(object.name + "(");
                parseArgumentList(c.getArguments(), superclass, c.getEnum(), out);
                out.append(") {\n\t\t").append("super(");
                for (int i = 0; i < args.size(); i++) {
                    out.append(args.get(i).name);
                    if (i < size - 1)
                        out.append(", ");
                }
                CEnum overloadenum = c.getEnum();
                if (overloadenum != null) {
                    if (!args.isEmpty())
                        out.append(", ");
                    out.append(overloadenum.name.toLowerCase());
                }
                out.append(");\n\t}\n");
                subclassArgTypes.put(c.getCommaSeparatedArgumentTypes().toString(), true);
            }
        }
        if(superclass.getSuperclass()!=null)
            parseSuperClassConstructor(lib.getObjectIfPresent(superclass.getSuperclass().getProcessedName()), object, lib, subclassArgTypes, out);
    }
    
    private void parseInjectedMethods(CObject object, XInjectedMethod im, Writer out) throws IOException {
        out.append("\t");
        
        out.append(im.getModifier() +" "+im.getReturnType() +" "+ im.getName()+"(");
        List<XArg> arguments = im.getArguments();
        if(arguments!=null) {
            for(int i=0; i<arguments.size(); i++) {
                out.append(arguments.get(i).getType() + " arg" +i);
                if (i < arguments.size() - 1)
                    out.append(", ");
            }
        }
        out.append(")");
        out.append(DUMMYBODY);        
    }

    /**
     * @param selector the non-null Obj-C selector
     * @return
     */
    private static String parseDelegateMethod(ObjCSelector selector) {
        int i = 0;
        StringBuilder sb = new StringBuilder();
        for (Parameter parm : selector.getParameters()) {
            // Get the original Obj-C type, such as "int" or "UIInterfaceOrientation"
            String origObjCType = parm.getType().getType();
            // Considering the origObjCType may be a "typedef", get the actual type, such as "int", but NOT "UIInterfaceOrientation"
            String type = getParseType(new CType(origObjCType), false);
            if ("Object".equals(type) || !Advisor.isNativeType(type)) {
                type = origObjCType;
            }
            String typeMapping = Advisor.getDataTypeMapping(type);
            if (typeMapping != null) {
                type = typeMapping;
            }

            if (i++ == 0)
                sb.append("\n\t\t");
            else
                sb.append(",\n\t\t");
            sb.append("@org.xmlvm.XMLVMDelegateMethod.Param(");
            sb.append("type = \"" + type + "\"");
            if (!parm.getType().isPointer()) {
                sb.append(", isStruct = true");
            }
            if (parm.getSelectorParamName() != null && !parm.getSelectorParamName().trim().equals("")) {
                sb.append(", name = \"" + parm.getSelectorParamName() + "\"");
            }
            sb.append(")");
        }
        return "\t@org.xmlvm.XMLVMDelegateMethod(selector = \"" + selector.getLeadingName()
                + "\", params = {" + sb.toString() + "\n\t})\n";
    }

    private void parseMethod(CObject parent, CMethod m, Writer out) throws IOException {
        out.append(methodprefix);
        parseJavadoc(m.getDefinitions(), out);
// TODO Also handle non-protocols requiring wrappers, such as UIView
        if (parent.isProtocol() && !m.isProperty() && !m.getDefinitions().isEmpty()) {
            String selectorDefinition = m.getDefinitions().get(0);
            ObjCSelector selector = ObjCSelectorUtil.toObjCSelector(selectorDefinition);
            if (selector == null) {
                throw new NullPointerException("Did not expect null ObjCSelector, but found one for \"" + selectorDefinition + "\"");
            } else {
                out.append(parseDelegateMethod(selector));
            }
        }
        out.append("\tpublic ");
        if (m.isStatic())
            out.append("static ");
        else if (m.isAbstract())
            out.append("abstract ");
        parseType(m.getReturnType(), false, out);
        out.append(" ").append(m.getCanonicalName()).append("(");
        parseArgumentList(m.getArguments(), parent, null, out);
        out.append(")").append(m.isAbstract() ? ABSTRACTBODY : DUMMYBODY);
    }

    private void parseConstructor(CObject parent, CConstructor c, Writer out) throws IOException {
        out.append(constructorprefix);
        parseJavadoc(c.getDefinitions(), out);
        out.append("\tpublic ").append(parent.getName()).append("(");
        parseArgumentList(c.getArguments(), parent, c.getEnum(), out);
        out.append(") {}\n");
    }

    private void parseDefaultConstructor(String name, boolean hasSuperclass, Writer out) throws IOException {
        out.append(constructorprefix);
        out.append("\n\t/** Default constructor */\n\t");
        out.append("public ").append(name).append("() {");
        if (hasSuperclass)
            out.append("\n\t\tsuper();\n\t");
        out.append("}\n");
    }

    private static String getParseType(CType type, boolean originalName) {
        return originalName ? type.getProcessedName() : type.toString();
    }

    private void parseType(CType type, boolean originalname, Writer out) throws IOException {
        out.append(getParseType(type, originalname));
    }

    private void parseArgumentList(List<CArgument> args, CObject parent, CEnum overloadenum, Writer out) throws IOException {
        int size = args.size();
        for (int i = 0; i < args.size(); i++) {
            parseArgument(args.get(i), out);
            if (i < size - 1)
                out.append(", ");
        }
        if (overloadenum != null) {
            if (!args.isEmpty())
                out.append(", ");
            out.append(parent.getName()).append(".").append(overloadenum.name).append(" ").append(overloadenum.name.toLowerCase());
        }
    }

    private void parseArgument(CArgument arg, Writer out) throws IOException {
        parseType(arg.getType(), false, out);
        out.append(" ").append(arg.name);
    }

    private void parseJavadoc(Iterable<String> definitions, Writer out) throws IOException {
        out.append("\n\t/**\n");
        for (String def : definitions)
            out.append("\t * ").append(def).append("\n");
        out.append("\t */\n");
    }

    private void parseEnum(CEnum enm, Writer out) throws IOException {
        out.append("\n\tpublic static enum ");
        out.append(enm.name).append(" {\n\t\t");
        List<String> values = enm.getValues();
        int size = values.size();
        for (int i = 0; i < values.size(); i++) {
            out.append(values.get(i));
            if (i < size - 1)
                out.append(", ");
        }
        out.append(";\n\t}\n");
    }

    private void parseReporter(Reporter reporter, Writer out) throws IOException {
        if (reporter.notepad.isEmpty())
            return;
        for (Tuplet ctx : reporter.notepad.keySet()) {
            out.append("<context");
            if (ctx.item != null)
                out.append(" file=\"").append(new File(ctx.item).getName()).append("\"");
            if (ctx.value != null)
                out.append(" object=\"").append(ctx.value).append("\"");
            out.append(">\n");
            for (Tuplet item : reporter.notepad.get(ctx)) {
                out.append("\t<item");
                if (item.item != null && !item.item.equals(""))
                    out.append(" info=\"").append(item.item).append("\"");
                out.append(">").append(item.value).append("</item>\n");
            }
            out.append("</context>\n");
        }
    }
    
    public void setConstructorPrefix(String constructorprefix) {
        this.constructorprefix = constructorprefix;
    }

    public void setMethodPrefix(String methodprefix) {
        this.methodprefix = methodprefix;
    }

    public void setObjectPrefix(String objectprefix) {
        this.objectprefix = objectprefix;       
    }
}
