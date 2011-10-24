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
import java.util.List;
import org.crossmobile.source.ctype.CArgument;
import org.crossmobile.source.ctype.CConstructor;
import org.crossmobile.source.ctype.CEnum;
import org.crossmobile.source.ctype.CLibrary;
import org.crossmobile.source.ctype.CMethod;
import org.crossmobile.source.ctype.CObject;
import org.crossmobile.source.ctype.CType;
import org.crossmobile.source.guru.Reporter;
import org.crossmobile.source.guru.Reporter.Tuplet;
import org.crossmobile.source.utils.WriteCallBack;
import org.crossmobile.source.utils.FileUtils;

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

        String type = object.isProtocol() ? (object.hasOptionalMethod() ? "abstract class" : "interface") : "class";
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
            for (CConstructor c : object.getConstructors()) {
                if (c.getArguments().isEmpty())
                    has_default_constructor = true;
                parseConstructor(object, c, out);
            }
            if (!has_default_constructor)
                parseDefaultConstructor(object.getName(), out);
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
        out.append("}\n");
    }

    private void parseMethod(CObject parent, CMethod m, Writer out) throws IOException {
        out.append(methodprefix);
        parseJavadoc(m.getDefinitions(), out);
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

    private void parseDefaultConstructor(String name, Writer out) throws IOException {
        out.append(constructorprefix);
        out.append("\n\t/** Default constructor */\n\t");
        out.append(name).append("() {}\n");
    }

    private void parseType(CType type, boolean originalname, Writer out) throws IOException {
        out.append(originalname ? type.getProcessedName() : type.toString());
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
