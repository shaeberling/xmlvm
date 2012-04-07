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

package org.crossmobile.source.ctype;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import org.crossmobile.source.guru.Advisor;
import org.crossmobile.source.guru.Oracle;
import org.crossmobile.source.guru.Reporter;
import org.crossmobile.source.parser.Stream;
import org.crossmobile.source.utils.FieldHolder;
import org.crossmobile.source.xtype.AdvisorWrapper;

public class CObject extends CAny implements FieldHolder, Serializable {
    private static final long serialVersionUID = 1L;

    private final CLibrary library;
    private CType superclass = null;
    private Set<CType> interfaces = new HashSet<CType>();
    private final boolean isProtocol;
    private boolean hasOptionalMethod = false;
    private List<CConstructor> constructors = new ArrayList<CConstructor>();
    private List<CMethod> methods = new ArrayList<CMethod>();
    private List<CProperty> properties = new ArrayList<CProperty>();
    private final int genericsCount;
    private boolean hasConstructorEnums = false;
    private boolean hasStaticMethods = false;
    private boolean hasInstanceMethods = false;
    private boolean hasProperties = false;
    private List<CArgument> variables = new ArrayList<CArgument>();
    private boolean isStruct = false;
    private String cClassName = null;
    private boolean hasMandatoryMethods = false;
    private boolean isFramework =  false;

    CObject(CLibrary library, String name, boolean isProtocol) {
        super(name);
        this.library = library;
        this.isProtocol = isProtocol;
        genericsCount = Advisor.genericsSupport(name);
        this.cClassName = library.getPackagename().replace(".", "_") + "_" + name;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CObject))
            return false;
        return ((CObject) obj).name.equals(name);
    }

    public void addSelector(CSelector sel) {
        boolean isConstructor = sel instanceof CConstructor;

        // First check if this is a constructor in an interface
        if (isProtocol && isConstructor) {
            Reporter.CONSTRUCTOR_IN_INTERFACE.report("object " + name, sel.getSignature(name));
            return; // Not valid
        }

        if (isConstructor)
            constructors.add((CConstructor) sel);
        else {
            CMethod meth = (CMethod) sel;
            if (meth.isStatic()) {
                hasStaticMethods = true;
                if (isProtocol)
                    Reporter.STATIC_INTERFACE.report(null, sel.getSignature(name));
            } else if (!meth.isProperty())
                hasInstanceMethods = true;
            methods.add(meth);
        }
    }

    public void addProperty(CProperty pro) {
        CMethod m;
        List<String> names;

        names = new ArrayList<String>();
        names.add(pro.getGetterName());
        m = new CMethod(pro.getGetterName(), pro.isAbstract(), true, new ArrayList<CArgument>(), names, false, pro.getType());
        for (String def : pro.getDefinitions())
            m.addDefinition(def);
        m.setProperty();
        addSelector(m);

        if (pro.getSetterName() != null) {
            List<CArgument> setargs = new ArrayList<CArgument>();
            setargs.add(new CArgument(pro.getType(), pro.name));
            names = new ArrayList<String>();
            names.add(pro.getSetterName());

            m = new CMethod(pro.getSetterName(), pro.isAbstract(), true, setargs, names, false, new CType("void"));
            m.setProperty();
            for (String def : pro.getDefinitions())
                m.addDefinition(def);
            addSelector(m);
        }
        hasProperties = true;
        properties.add(pro);
    }

    public String getName() {
        return name;
    }

    public int getGenericsCount() {
        return genericsCount;
    }

    public String getcClassName() {
        return cClassName;
    }
    
    public CLibrary getLibrary() {
        return library;
    }

    public void setSuperclass(String superclass) {
        this.superclass = new CType(superclass);
    }

    public void addInterface(String interf) {
        if (interf.equals("NSObject"))
            return;
        interfaces.add(new CType(interf));
    }

    public boolean isProtocol() {
        return isProtocol;
    }

    public boolean hasOptionalMethod() {
        return hasOptionalMethod;
    }

    public void finalizeObject() {
        /*
         * Fix Constructors
         */
        CConstructor base, running;
        String basesig;
        Set<CConstructor> doubles = new HashSet<CConstructor>();
        for (int i = 0; i < constructors.size() - 1; i++) {
            base = constructors.get(i);
            basesig = base.getSignature(name);
            for (int j = i + 1; j < constructors.size(); j++) {
                running = constructors.get(j);
                if (!doubles.contains(running) && running.getSignature(name).equals(basesig)) {
                    base.updateEnum(basesig);  // The enumaration definition happens HERE, because we want to finish with typedefs first and be sure that an override REALLY exists
                    if (base.isOverloaded()) {
                        if (base.getEnum() != null) // Might be overloaded but we don't support this in Java
                            hasConstructorEnums = true;
                        for (String def : running.getDefinitions())
                            base.addDefinition(def);
                        doubles.add(running);
                    } else
                        Reporter.UNKNOWN_OVERRIDE.report("constructor " + basesig,
                                base.getDefinitions().iterator().next()
                                + " ## "
                                + running.getDefinitions().iterator().next());
                }
            }
        }
        for (CConstructor obs : doubles)
            constructors.remove(obs);


        /*
         * Fix Methods
         */

        // Search if this is a delegate, so that the selector name aggregator will be more aggressive
        boolean isDelegate = false;
        for (String pattern : Advisor.getDelegatePatterns())
            isDelegate |= name.matches(pattern);

        Map<String, List<CMethod>> maps = new HashMap<String, List<CMethod>>();
        List<CMethod> toRemove = new ArrayList<CMethod>();

        // Put methods in order
        for (CMethod m : methods) {
            String givenName = Advisor.getMethodCanonical((m.isStatic() ? "+" : "-") + m.getSignature(name));
            if (givenName != null)
                if (givenName.equals(""))
                    toRemove.add(m);
                else
                    m.setCanonicalName(givenName);
            else {
                String key = isDelegate ? m.getNameParts().get(0) : m.getSignature("");  // if it is a delegate, group by first type, to minimize namespace pollution
                List<CMethod> list = maps.get(key);
                if (list == null) {
                    list = new ArrayList<CMethod>();
                    maps.put(key, list);
                }
                list.add(m);
            }
            m.setMandatory(AdvisorWrapper.methodIsMandatoryForObject(m.getSelectorName(), this.name));
        }

        // find conflicting methods
        for (List<CMethod> conflict : maps.values())    // get a list of all methods in categorized format
            if (conflict.size() > 1) {  // only affect methods with conflict

                List<List<String>> parts = new ArrayList<List<String>>();   // aggregate parameter names
                List<Boolean> statics = new ArrayList<Boolean>();
                for (CMethod meth : conflict) {
                    parts.add(meth.getNameParts());
                    statics.add(meth.isStatic());
                }

                List<String> newnames = Oracle.findUniqueNames(parts, statics, isDelegate); // find new names
                String cname;
                for (int i = 0; i < conflict.size(); i++) {
                    cname = newnames.get(i);
                    if (cname == null)
                        methods.remove(conflict.get(i));
                    else
                        conflict.get(i).setCanonicalName(newnames.get(i));
                }
            }

        // Remove methods which are not valid
        for (CMethod m : toRemove)
            methods.remove(m);
        
        if(AdvisorWrapper.objectHasMandatoryMethods(this.name))
            hasMandatoryMethods  = true;
    }

    public CType getSuperclass() {
        return superclass;
    }

    public Set<CType> getInterfaces() {
        return interfaces;
    }

    public boolean hasConstructorEnums() {
        return hasConstructorEnums;
    }

    public List<CConstructor> getConstructors() {
        return constructors;
    }

    public boolean hasStaticMethods() {
        return hasStaticMethods;
    }

    public Iterable<CMethod> getMethods() {
        return methods;
    }

    public boolean hasConstructors() {
        return constructors.size() > 0;
    }

    public boolean hasInstanceMethods() {
        return hasInstanceMethods;
    }

    public boolean hasProperties() {
        return hasProperties;
    }

    public static CObject parse(CLibrary parent, boolean protocol, Stream s) {
        String name = s.consumeID();
        CObject obj = protocol ? parent.getInterface(name) : parent.getObject(name);
        Reporter.setObject(obj);

        // Remove categories
        if (s.peekChar() == '(')
            s.consumeBalanced('(', ')');

        // Find superclass
        if (s.peekChar() == ':') {
            s.consumeChars(1);
            obj.setSuperclass(s.consumeID());
        }

        // Find interfaces
        if (s.peekChar() == '<') {
            String interfs = s.consumeBalanced('<', '>');
            interfs = interfs.substring(1, interfs.length() - 1).trim();
            StringTokenizer tk = new StringTokenizer(interfs, ",");
            while (tk.hasMoreTokens())
                obj.addInterface(tk.nextToken().trim());
        }
        if (s.peekChar() == '{')
            s.consumeBalanced('{', '}');
        if (s.peekChar() == ';')
            s.consumeChars(1);

        return obj;
    }

    public Iterable<CArgument> getVariables() {
        return variables;
    }

    protected void addVariable(CArgument arg, boolean isProtected) {
        if (!variables.contains(arg))
            variables.add(arg);
    }

    public boolean hasVariables() {
        return !variables.isEmpty();
    }

    public boolean isStruct() {
        return isStruct;
    }

    public void setQStruct() {
        this.isStruct = true;
    }

    @Override
    public void addCArgument(CArgument arg) {
        if (!variables.contains(arg))
            variables.add(arg);
    }
    
    public boolean hasMandatoryMethods(){
        return hasMandatoryMethods;
    }

    public void setFramework(boolean b) {
        this.isFramework = true;
    }
    
    public boolean isFramework() {
        return this.isFramework;
    }
}
