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

package org.crossmobile.source.out.cutils;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.crossmobile.source.ctype.CLibrary;
import org.crossmobile.source.ctype.CObject;
import org.crossmobile.source.out.COut;
import org.crossmobile.source.xtype.AdvisorMediator;
import org.crossmobile.source.xtype.XCode;
import org.crossmobile.source.xtype.XObject;

/**
 * This class serves as a code generator for wrappers for Objective-C classes
 * apart from protocols. This class emits code for INTERNAL_CONSTRUCTOR,
 * WRAPPER_CREATOR, registration of WRAPPER_CREATOR. CConstructorOut and
 * CMethodOut classes are used to generate constructors and methods associated
 * with the class.
 * 
 */
public class CObjectOut {

    private Writer   out                 = null;
    private CObject  object              = null;
    private CLibrary lib                 = null;

    String           initialInjectedCode = null;
    String           finalInjectedCode   = null;
    String           replaceableCode     = null;


    public CObjectOut(Writer out, CLibrary lib, CObject object) {
        this.out = out;
        this.object = object;
        this.lib = lib;
    }

    /**
     * Used to emit the code for structures
     * 
     * @throws IOException
     */
    public void emitImpl() throws IOException {
        if (!object.isFramework()) {
            setGlobalCodeForEmission();
            if (AdvisorMediator.needsInternalConstructor(object.name)) {
                out.append(C.BEGIN_IMPL + C.N);
                if (replaceableCode != null) {
                    out.append(replaceableCode);
                } else {
                    if (initialInjectedCode != null)
                        out.append(initialInjectedCode);
                    emitInternalConstructor();
                    if (AdvisorMediator.getOpaqueBaseType(object.name) == null)
                        emitWrapperCreator();
                    if (finalInjectedCode != null)
                        out.append(finalInjectedCode);
                }
                out.append(C.END_IMPL + C.N);
                if (AdvisorMediator.getOpaqueBaseType(object.name) == null)
                    emitWrapperRegistration();
                emitObjectDeletion();
            }

            CConstructorOut cConsOut = new CConstructorOut(out, lib, object);
            cConsOut.emitConstructors(false);
        }
        CMethodOut cMethodOut = new CMethodOut(out, lib, object);
        cMethodOut.emitMethods(false);
    }

    /**
     * 
     */
    private void setGlobalCodeForEmission() {

        if (AdvisorMediator.objectHasGlobalCodeInjection(object.name)) {
            List<XCode> iCode = AdvisorMediator.getInjectedCodeForObject(object.name);
            int index = 0;
            while (index < iCode.size()) {
                if (iCode.get(index).getMode().equals("before"))
                    initialInjectedCode = iCode.get(index).getCode();
                else if (iCode.get(index).getMode().equals("after"))
                    finalInjectedCode = iCode.get(index).getCode();
                else if (iCode.get(index).getMode().equals("replace"))
                    replaceableCode = iCode.get(index).getCode();
                index++;
            }

        }

    }

    /**
     * Every class has a Internal Constructor which calls the Internal
     * Constructor of its base class
     * 
     * @throws IOException
     */
    private void emitInternalConstructor() throws IOException {
        out.append("void " + object.getcClassName() + "_INTERNAL_CONSTRUCTOR(JAVA_OBJECT me,");

        out.append(AdvisorMediator.isCFOpaqueType(object.name) ? "CFTypeRef" : "NSObject*");
        out.append(" wrappedObj){" + C.NT);
        out.append(COut.packageName);
        if (object.getSuperclass() != null)
            out.append(object.getSuperclass().getProcessedName());
        else if (AdvisorMediator.isCFOpaqueType(object.name))
            out.append("CFType");
        else
            out.append("NSObject");
        out.append("_INTERNAL_CONSTRUCTOR(me, wrappedObj);" + C.N);
        if (AdvisorMediator.classHasRetainPolicy(object.name)) {
            out.append(object.getcClassName() + "* thiz = (" + object.getcClassName() + "*)me;"
                    + C.N);
            out.append("thiz->fields." + object.getcClassName() + ".acc_array_" + object.name
                    + " = XMLVMUtil_NEW_ArrayList();" + C.N);
        }

        out.append("}" + C.N);
    }

    /**
     * Wrapper creator has to be emitted for every class
     * 
     * @throws IOException
     */
    private void emitWrapperCreator() throws IOException {
        XObject obj = AdvisorMediator.getSpecialClass(object.name);
        List<String> aliasList = null;
        out.append(C.N + "static JAVA_OBJECT __WRAPPER_CREATOR(NSObject* obj)" + C.N + "{");
        out.append(C.NT + "if(");
        if (AdvisorMediator.classHasDelegateMethods(object.name))
            out.append("[obj class] == [" + object.name + "Wrapper class] || ");

        out.append("[obj class] == [" + object.name + " class]");

        if (obj != null) {
            if ((aliasList = obj.getAliasList()) != null)
                for (String alias : aliasList)
                    out.append(" || ([NSStringFromClass([obj class]) isEqual:@\"" + alias + "\"])");
        }

        out.append(") " + C.NT + "{" + C.N);

        out.append(C.TT + "[obj retain];" + C.N);
        out.append(C.TT + "JAVA_OBJECT jobj = __NEW_" + object.getcClassName() + "();" + C.NTT);
        out.append(object.getcClassName() + "_INTERNAL_CONSTRUCTOR(jobj, obj);" + C.N);
        out.append(C.TT + "return jobj;" + C.NT + "}" + C.NT);
        out.append("return JAVA_NULL;" + C.N + "}" + C.N);

    }

    /**
     * The wrapper creator of the particular class has to be registered
     * 
     * @throws IOException
     */
    private void emitWrapperRegistration() throws IOException {
        out.append(C.BEGIN_WRAPPER + "[__INIT_" + object.getcClassName() + "]" + C.N);
        out.append("xmlvm_register_wrapper_creator(__WRAPPER_CREATOR);" + C.N);
        out.append(C.END_WRAPPER + C.N);

    }

    /**
     * The C version of the obj C object has to be cleaned
     * 
     * @throws IOException
     */
    private void emitObjectDeletion() throws IOException {
        out.append(C.BEGIN_WRAPPER + "[__DELETE_" + object.getcClassName() + "]" + C.N);
        out.append("__DELETE_" + COut.packageName);
        if (object.getSuperclass() != null)
            out.append(object.getSuperclass().getProcessedName());

        else if (AdvisorMediator.isCFOpaqueType(object.name))
            out.append("CFType");
        else
            out.append("NSObject");
        out.append("(me, client_data);" + C.N);

        out.append(C.END_WRAPPER + C.N);
    }

}