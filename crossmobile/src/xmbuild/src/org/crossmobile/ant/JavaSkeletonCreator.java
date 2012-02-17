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

package org.crossmobile.ant;

import org.crossmobile.ant.sync.utils.ClassList;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Comparator;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class JavaSkeletonCreator extends Task {

    private String pkgname;
    private File extrapath;
    private File outdir;
    private String defaultobject;
    private int tabDepth = 0;

    public void setExtrapath(File extrapath) {
        this.extrapath = extrapath;
    }

    public void setOutdir(File outdir) {
        this.outdir = outdir;
    }

    public void setPackage(String pkg) {
        if (pkg.equals(""))
            throw new BuildException("Default package not supported");
        pkgname = pkg;
    }

    public void setDefaultobject(String defaultobject) {
        this.defaultobject = defaultobject;
    }

    @Override
    public void execute() throws BuildException {
        if (pkgname == null)
            throw new BuildException("Property 'package' should be defined");
        if (outdir == null)
            throw new BuildException("Property 'outdir' should be properly defined");
        if (defaultobject == null)
            throw new BuildException("Property 'defaultobject' should be defined, with the name of a representative class for this package");
        File packagedir = new File(outdir.getPath() + File.separator + pkgname.replace('.', File.separatorChar));
        if (!packagedir.isDirectory() && !packagedir.mkdirs())
            throw new BuildException("Unable to create target directory " + packagedir);
        if (!packagedir.isDirectory())
            System.out.println("Invalid output directory " + packagedir.getPath());

        AntClassLoader cl = (AntClassLoader) getClass().getClassLoader();
        if (extrapath != null)
            cl.addPathComponent(extrapath);
        int modifier;
        //   String name;
        String classfilename;
        BufferedWriter out = null;
        int count = 0;
        for (Class<?> clazz : new ClassList(pkgname)) {
            classfilename = outdir.getPath() + File.separator + clazz.getName().replace('.', File.separatorChar) + ".java";
            modifier = clazz.getModifiers();
            if (classfilename.endsWith("_.java") || !Modifier.isPublic(modifier))
                continue;
            count++;
            try {
                tabDepth = 0;
                out = new BufferedWriter(new FileWriter(classfilename));
                out.write("package " + pkgname + ";\n\n");
                out.write("import org.xmlvm.XMLVMSkeletonOnly;\nimport org.xmlvm.XMLVMIgnore;\n");
                writeClass(out, clazz, modifier);
                out.close();
                out = null;
            } catch (IOException ex) {
            } finally {
                if (out != null)
                    try {
                        out.close();
                    } catch (IOException ex) {
                    }
            }
        }
        if (count == 0)
            throw new BuildException("Unable to create skeletons for any object.");
        System.out.println("Successfully created skeletons for " + count + " objects in package " + pkgname);
    }

    private String tabs() throws IOException {
        return "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t".substring(0, tabDepth);
    }

    private String getType(Type type) {
        if (type instanceof Class) {
            Class<?> cl = (Class<?>) type;
            return cl.getPackage() != null ? cl.getPackage().getName() + "." + cl.getSimpleName() : cl.getSimpleName();
        } else
            return type.toString();
    }

    private String getTypes(Type[] vars) {
        String out = "";
        if (vars != null && vars.length > 0) {
            out = "<";
            for (int i = 0; i < vars.length - 1; i++)
                out += getType(vars[i]) + ", ";
            out += getType(vars[vars.length - 1]) + "> ";
        }
        return out;
    }

    private void writeParams(BufferedWriter out, Type[] params) throws IOException {
        int counter = 1;
        out.write("(");
        if (params != null && params.length > 0) {
            for (int i = 0; i < (params.length - 1); i++)
                out.write(getType(params[i]) + " p" + (counter++) + ", ");
            out.write(getType(params[params.length - 1]) + " p" + (counter++));
        }
        out.write(")");
    }

    private void writeExceptions(BufferedWriter out, Type[] exceptions) throws IOException {
        if (exceptions == null || exceptions.length == 0)
            return;
        out.write(" throws ");
        for (int i = 0; i < exceptions.length - 1; i++)
            out.write(getType(exceptions[i]) + ", ");
        out.write(getType(exceptions[exceptions.length - 1]));
    }

    private String getMods(int mod, boolean skipAbstract) {
        String res = "";
        if (Modifier.isPublic(mod))
            res += "public ";
        if (Modifier.isProtected(mod))
            res += "protected ";
        if (!(skipAbstract && Modifier.isInterface(mod)) && Modifier.isAbstract(mod))
            res += "abstract ";
        if (Modifier.isStatic(mod))
            res += "static ";
        if (Modifier.isFinal(mod))
            res += "final ";
        if (Modifier.isSynchronized(mod))
            res += "synchronized ";
        return res;
    }

    private void writeClass(BufferedWriter out, Class<?> clazz, int modifier) throws IOException {
        if (!Modifier.isPublic(modifier))
            return;

        out.write("\n");
        boolean isFinal = writeHeader(out, clazz, modifier);
        tabDepth++;
        boolean hasFinalDeclarations = writeFields(out, clazz);
        if (!Modifier.isInterface(modifier))
            writeConstructors(out, clazz, isFinal && hasFinalDeclarations);
        writeMethods(out, clazz);
        for (Class<?> c : clazz.getDeclaredClasses())
            writeClass(out, c, c.getModifiers());
        tabDepth--;
        out.write(tabs() + "}\n");
    }

    private boolean writeHeader(BufferedWriter out, Class<?> clazz, int modifier) throws IOException {
        Type superclass;
        Type[] interfaces;
        String simplename = clazz.getSimpleName();
        out.write(tabs() + "@XMLVMSkeletonOnly\n");
        out.write(tabs() + getMods(modifier, true));
        if (Modifier.isInterface(modifier))
            out.write("interface ");
        else
            out.write("class ");

        out.write(simplename);
        out.write(getTypes(clazz.getTypeParameters()) + " ");

        superclass = clazz.getGenericSuperclass();
        if (superclass != null)
            if (!superclass.equals(Object.class))
                out.write("extends " + getType(superclass) + " ");
//  TODO          else if (!Modifier.isInterface(modifier))
//                System.out.println("Warning: class " + clazz.getName() + " inherits from Object");
        interfaces = clazz.getGenericInterfaces();
        if (interfaces != null && interfaces.length > 0) {
            out.write("implements ");
            for (int i = 0; i < (interfaces.length - 1); i++)
                out.write(getType(interfaces[i]) + ", ");
            out.write(getType(interfaces[interfaces.length - 1]) + " ");
        }
        out.write("{\n");
        return Modifier.isFinal(modifier);
    }

    private void writeMethods(BufferedWriter out, Class<?> clazz) throws IOException {
        Method[] methods = clazz.getDeclaredMethods();
        Arrays.sort(methods, new Comparator<Method>() {

            @Override
            public int compare(Method o1, Method o2) {
                int c = o1.getName().compareTo(o2.getName());
                if (c == 0)
                    c = o1.getParameterTypes().length - o2.getParameterTypes().length;
                return c;
            }
        });
        for (Method m : methods) {
            int mod = m.getModifiers();
            if (!(Modifier.isPublic(mod) || Modifier.isProtected(mod)) || Modifier.isVolatile(mod) || m.getName().startsWith("xmlvm") || fromParent(clazz, m))
                continue;

            out.write("\n");
            out.write(tabs() + getMods(mod, false));
            out.write(getTypes(m.getTypeParameters()));

            if (m.getReturnType() == null)
                out.write("void ");
            else
                out.write(getType(m.getGenericReturnType()) + " ");
            out.write(m.getName());
            writeParams(out, m.getGenericParameterTypes());
            writeExceptions(out, m.getGenericExceptionTypes());
            if (Modifier.isAbstract(mod))
                out.write(";\n");
            else {
                out.write(" {\n");
                tabDepth++;
                out.write(tabs() + "throw new RuntimeException(\"Stub code\");\n");
                tabDepth--;
                out.write(tabs() + "}\n");
            }
        }
    }

    private void writeConstructors(BufferedWriter out, Class<?> clazz, boolean writePrivate) throws IOException {
        Constructor<?>[] constrlist = clazz.getDeclaredConstructors();
        Arrays.sort(constrlist, new Comparator<Constructor<?>>() {

            @Override
            public int compare(Constructor<?> o1, Constructor<?> o2) {
                return o1.getParameterTypes().length - o2.getParameterTypes().length;
            }
        });

        String simplename = clazz.getSimpleName();
        boolean hasPublicConstructor = false;
        boolean hasEmptyContructor = false;
        for (Constructor<?> c : constrlist) {
            int mod = c.getModifiers();
            if (!(Modifier.isProtected(mod) || Modifier.isPublic(mod)))
                continue;

            hasPublicConstructor = true;
            out.write("\n");
            out.write(tabs() + getMods(mod, false));
            out.write(simplename + " ");
            Type[] params = c.getGenericParameterTypes();
            if (params == null || params.length == 0)
                hasEmptyContructor = true;
            writeParams(out, params);
            writeExceptions(out, c.getGenericExceptionTypes());
            out.write(" {\n" + tabs() + "}\n");
        }
        if (writePrivate && hasPublicConstructor)
            throw new BuildException("Final class " + clazz.getName() + " with static fields recognized, and a public constructor was defined.");
        if (!hasEmptyContructor)
            out.write("\n" + tabs() + "@XMLVMIgnore\n" + tabs() + simplename + " () {\n" + tabs() + "}\n");
// TODO        else
//            System.out.println("Not empty constructor: " + cname);
    }

    private boolean writeFields(BufferedWriter out, Class<?> clazz) throws IOException {
        Field[] fields = clazz.getDeclaredFields();
        boolean hasFinal = false;
        if (fields != null && fields.length > 0) {
            Arrays.sort(fields, new Comparator<Field>() {

                @Override
                public int compare(Field o1, Field o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            for (Field f : fields) {
                int mod = f.getModifiers();
                if (!(Modifier.isPublic(mod) || Modifier.isProtected(mod)))
                    continue;

                out.write(tabs() + getMods(mod, false));
                Type type = f.getGenericType();
                out.write(getType(type) + " ");
                out.write(f.getName());
                try {
                    Object value = f.get(null);
                    if (value != null) {
                        out.write(" = ");
                        if (type instanceof Class && ((Class<?>) type).isPrimitive())
                            out.write(value.toString()
                                    + (type.equals(Float.TYPE) ? "f" : ""));
                        else if (type.equals(String.class))
                            out.write("\"" + value.toString() + "\"");
                        else
                            out.write("null");
                    }
                } catch (Exception ex) {
                }
                out.write(";\n");
            }
        }
        return hasFinal;
    }

    private boolean fromParent(Class<?> clazz, Method m) {
        try {
            return m.getModifiers() == clazz.getSuperclass().getMethod(m.getName(), m.getParameterTypes()).getModifiers();
        } catch (Exception ex) {
            return false;
        }
    }
}
