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
import org.crossmobile.source.ctype.CProperty;
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
        File protocolOut = new File(outdir+"/adapter");
        FileUtils.delete(out);

        for (CObject o : library.getObjects()) {
            final CObject fo = o;
            FileUtils.putFile(new File(out, o.getName() + ".java"), new WriteCallBack<Writer>() {

                @Override
                public void exec(Writer out) throws IOException {
                    parseObject(library, fo, false, out);
                }
            });
            
            if(o.isProtocol()) {
                FileUtils.putFile(new File(protocolOut, o.getName() + ".java"), new WriteCallBack<Writer>() {

                    @Override
                    public void exec(Writer out) throws IOException {
                        parseObject(library, fo, true, out);
                    }
                });  
            }
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
    
    private void parseObject(CLibrary library, CObject object, boolean isAdapterImpl, Writer out) throws IOException {
        if(isAdapterImpl) {
            CType superclass = object.getSuperclass();
            if(superclass!=null && library.getObject(superclass.getProcessedName()).isProtocol())
                throw new RuntimeException("Superclass of a protocol is a Protocol");
            out.append("package ").append(library.getPackagename()+".adapter").append(";\n");
        }
        else
            out.append("package ").append(library.getPackagename()).append(";\n");
        out.append("import java.util.*;\n\n");
        
        if(isAdapterImpl)
            out.append("import " +library.getPackagename()+".*;\n\n");
        else
            out.append(objectprefix);

        List<String> references = null;
        if(!isAdapterImpl && (references = AdvisorWrapper.getReferencesForObject(object.name)) != null){
            out.append("(references={");
            for(int i=0; i<references.size(); i++){
                out.append(references.get(i) + ".class");
                if(i < references.size()-1)
                    out.append(",");
            }
            out.append("})\n");
        }
        
        String type = "class";
        
        if(isAdapterImpl) {
            type = object.hasMandatoryMethods() ? "abstract class" : "class";
        } else if (object.isProtocol()) {
            type = object.hasOptionalMethod() ? "abstract class" : "interface";
            out.append("@org.xmlvm.XMLVMDelegate(protocolType = \"" + object.getName() + "\")\n");
        } else if (AdvisorWrapper.classHasDelegateMethods(object.name)) {
            out.append("@org.xmlvm.XMLVMDelegate\n");
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
        if (object.getSuperclass() != null && !isAdapterImpl) {
            out.append(" extends ");
            parseType(object.getSuperclass(), true, getPackageName(object.getSuperclass(), library), out);
            if (object.isProtocol())
                alreadyDefinedExtensionForInterface = true;
        }
        
        if(isAdapterImpl) {
            out.append(" implements ").append(library.getPackagename() +".").append(object.name);
        }
        
        String opaqueBaseType = AdvisorWrapper.getOpaqueBaseType(object.name);
        if(opaqueBaseType != null)
            out.append(" extends ").append(opaqueBaseType);   // Eg CFType

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
                parseArgument(var, getPackageName(var.getType(), library), out);
                out.append(";\n");
            }
        }

        if (object.hasStaticMethods()) {
            out.append("\n\t/*\n\t * Static methods\n\t */\n");
            for (CMethod m : object.getMethods())
                if (m.isStatic()) {
                    if(isAdapterImpl)
                        parseMethod(object, m, library, true, out);
                    else
                        parseMethod(object, m, library, false, out);
                }
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
                parseConstructor(object, c, library, out);
            }
            if (!has_default_constructor)
                parseDefaultConstructor(object.getName(), hasSuperclass, out);
        }

        if (object.hasProperties()) {
            out.append("\n\t/*\n\t * Properties\n\t */\n");
            for (CMethod m : object.getMethods())
                if (m.isProperty()) {
                    if(isAdapterImpl)
                        parseMethod(object, m, library, true, out);
                    else
                        parseMethod(object, m, library, false, out);
                }
        }

        if (object.hasInstanceMethods()) {
            out.append("\n\t/*\n\t * Instance methods\n\t */\n");
            for (CMethod m : object.getMethods())
                if (!m.isStatic() && !m.isProperty()) {
                    if(isAdapterImpl)
                        parseMethod(object, m, library, true, out);
                    else
                        parseMethod(object, m, library, false, out);
                }
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
                parseArgumentList(c.getArguments(), superclass, c.getEnum(), lib, out);
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

    private void parseMethod(CObject parent, CMethod m, CLibrary lib, boolean isAdapterImpl, Writer out) throws IOException {
        
        out.append(methodprefix);
        parseJavadoc(m.getDefinitions(), out);
        if ((parent.isProtocol() && !m.isProperty() && !m.getDefinitions().isEmpty() && !isAdapterImpl) || 
                 AdvisorWrapper.isDelegateMethod(m.getSelectorName(), parent.name)) {
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
        else if ((!isAdapterImpl && m.isAbstract())  
                || (isAdapterImpl && m.isMandatory()))
            out.append("abstract ");
        
        String name = m.isProperty() ? CProperty.getPropertyDef(m.name) : m.getSelectorName();
        if (AdvisorWrapper.hasSpecialReturnType(name, parent.name, m.isProperty())
                && (!m.isProperty() || (m.isProperty() && m.getArguments().isEmpty()))) //getter
            parseSpecialReturnType(name, parent.name, m.isProperty(), out);
        else
            parseType(m.getReturnType(), false, getPackageName(m.getReturnType(), lib), out);
        out.append(" ").append(m.getCanonicalName()).append("(");
        
        if(AdvisorWrapper.hasSpecialArgumentsDefined(name, parent.name, m.isProperty())
                && (!m.isProperty() || (m.isProperty() && !m.getArguments().isEmpty()))) //setter
            parseSpecialArgumentList(name, parent.name, m.isProperty(), out);
        else
            parseArgumentList(m.getArguments(), parent, null, lib, out);
        
        if(isAdapterImpl && !AdvisorWrapper.methodIsMandatoryForObject(m.getSelectorName(), parent.name)) {
            out.append(")");
            parseInterfaceImplementationBody(parent, m, out);
        }
        else
            out.append(")").append(m.isAbstract() ? ABSTRACTBODY : DUMMYBODY);
    }
    
    private void parseSpecialReturnType(String name, String parent, boolean isProperty, Writer out)
            throws IOException {
        if (isProperty) {
            out.append(AdvisorWrapper.getPropertyType(name, parent));
        } else {
            out.append(AdvisorWrapper.getSelectorReturnType(name, parent));
        }

    }

    /**
     * In some cases, the argument list obtained by parsing the objective C API
     * need to be replaced with some special case (EG: UIControl.addTarget). In
     * such cases, the entire set of arguments for the specific API need to
     * specified in order via the advisor. This method is also used to alter the
     * argument of setters for properties, where the properties have specific
     * types (Eg: List<UIViewControllers>)
     * 
     * @param name
     *            - name of the selector or property
     * @param parent
     *            - name of the class
     * @param isProperty
     *            - true if it is a property
     * @param out
     * @throws IOException
     */
    private void parseSpecialArgumentList(String name, String parent, boolean isProperty, Writer out)
            throws IOException {
        if (isProperty) {
            out.append(AdvisorWrapper.getPropertyType(name, parent) + " arg0");
        } else {
            List<XArg> args = AdvisorWrapper.getArgumentsForMethod(name, parent);
            int size = args.size();
            for (int i = 0; i < args.size(); i++) {
                out.append(args.get(i).getType() + " arg" + i);
                if (i < size - 1)
                    out.append(", ");
            }
        }
    }

    /**
     * The default return types at present for interface implementations is
     * null, 0 or false. If the default return types have to be overriden with
     * some specific values, it needs to be specified via special advice. TODO:
     * This is not handled yet (via advice)
     * 
     * @param returnType
     *            - return type of the method
     * @throws IOException
     * 
     */
    private void parseInterfaceImplementationBody(CObject parent, CMethod method, Writer out)
            throws IOException {
        CType returnType = method.getReturnType();
        String defaultReturnValue = null;

        if ((defaultReturnValue = AdvisorWrapper.getDefaultReturnValue(method.getSelectorName(),
                parent.name)) != null)
            out.append("{\n\t\treturn ").append(defaultReturnValue).append(";\n\t}");
        else if (returnType.toString().equals("void"))
            out.append("{};");
        else if (returnType.toString().equals("boolean"))
            out.append("{\n\t\treturn false;\n\t}");
        else if (returnType.toString().equals("int") || returnType.toString().equals("short")
                || returnType.toString().equals("long") || returnType.toString().equals("float")
                || returnType.toString().equals("double"))
            out.append("{\n\t\treturn 0;\n\t}");
        else
            out.append("{\n\t\treturn null;\n\t}");
    }

    private String getPackageName(CType type, CLibrary lib) {
        CObject obj = lib.getObjectIfPresent(type.toString());
        if (obj!=null)
            return obj.isProtocol()? lib.getPackagename() : null;
        return null;
    }

    private void parseConstructor(CObject parent, CConstructor c, CLibrary lib, Writer out) throws IOException {
        out.append(constructorprefix);
        parseJavadoc(c.getDefinitions(), out);
        out.append("\tpublic ").append(parent.getName()).append("(");
        parseArgumentList(c.getArguments(), parent, c.getEnum(), lib, out);
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

    private void parseType(CType type, boolean originalname, String packageName, Writer out) throws IOException {
        if(packageName!=null)
            out.append(packageName + "." + getParseType(type, originalname));
        else
            out.append(getParseType(type, originalname));

    }

    private void parseArgumentList(List<CArgument> args, CObject parent, CEnum overloadenum, CLibrary lib, Writer out) throws IOException {
        int size = args.size();
        for (int i = 0; i < args.size(); i++) {
            parseArgument(args.get(i), getPackageName(args.get(i).getType(), lib), out);
            if (i < size - 1)
                out.append(", ");
        }
        if (overloadenum != null) {
            if (!args.isEmpty())
                out.append(", ");
            out.append(parent.getName()).append(".").append(overloadenum.name).append(" ").append(overloadenum.name.toLowerCase());
        }
    }

    private void parseArgument(CArgument arg, String packageName, Writer out) throws IOException {
        parseType(arg.getType(), false, packageName, out);
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
