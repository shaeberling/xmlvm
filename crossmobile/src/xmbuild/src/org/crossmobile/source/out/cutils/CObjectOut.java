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
import org.crossmobile.source.guru.Advisor;
import org.crossmobile.source.out.COut;
import org.crossmobile.source.xtype.AdvisorMediator;
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

    private Writer   out    = null;
    private CObject  object = null;
    private CLibrary lib    = null;


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
            StringBuilder initialInjectedCode = new StringBuilder();
            StringBuilder finalInjectedCode = new StringBuilder();
            StringBuilder replaceableCode = new StringBuilder();

            CMethodHelper.setCodeForInjection(null, object.name, false, false, initialInjectedCode,
                    replaceableCode, finalInjectedCode);
            if (AdvisorMediator.needsInternalConstructor(object.name)) {
                out.append(C.BEGIN_IMPL + C.N);

                if (!replaceableCode.toString().isEmpty()) {
                    out.append(replaceableCode);
                } else {
                    if (!initialInjectedCode.toString().isEmpty())
                        out.append(initialInjectedCode + C.N);

                    if (AdvisorMediator.getOpaqueBaseType(object.name) == null
                            && !Advisor.getDefinedObjects().contains(object.name))
                        emitInitializer();

                    emitInternalConstructor();
                    if (AdvisorMediator.getOpaqueBaseType(object.name) == null)
                        emitWrapperCreator();
                    if (!finalInjectedCode.toString().isEmpty())
                        out.append(finalInjectedCode);
                }
                out.append(C.END_IMPL + C.N);
                if (AdvisorMediator.getOpaqueBaseType(object.name) == null)
                    emitWrapperRegistration();
                emitObjectDeletion();
            }

            CConstructorOut cConsOut = new CConstructorOut(out, lib, object);
            cConsOut.emitConstructors();
        }
        CMethodOut cMethodOut = new CMethodOut(out, lib, object);
        cMethodOut.emitMethods();
    }

    /**
     * Initializer is added to every class via the category inorder to check if
     * a class has been initialized previously. If it was not initialized, then
     * the _INIT_ method for the particular class is called. This is essetial
     * when an object has to be created on the fly. (If wrapper creator was not
     * previously registered, in such cases, error is thrown)
     * 
     * @throws IOException
     * 
     */
    private void emitInitializer() throws IOException {
        out.append("@interface " + object.name + " (" + object.name + "WrapperCategory)" + C.N);
        out.append("+ (void) initialize_class;" + C.N);
        out.append("@end" + C.N);
        out.append(C.N + "@implementation " + object.name + " (" + object.name + "WrapperCategory)"
                + C.N);
        out.append("+ (void) initialize_class {" + C.NT);
        out.append("if(!__TIB_" + object.getcClassName() + ".classInitialized)" + C.NTT);
        out.append("__INIT_" + object.getcClassName() + "();" + C.N);
        out.append("}" + C.N + "@end" + C.N + C.N);
    }

    /**
     * Every class has a Internal Constructor which calls the Internal
     * Constructor of its base class
     * 
     * @throws IOException
     */

    private void emitInternalConstructor() throws IOException {

        StringBuilder initialInjectedCode = new StringBuilder();
        StringBuilder finalInjectedCode = new StringBuilder();
        StringBuilder replaceableCode = new StringBuilder();

        out.append("void " + object.getcClassName() + "_INTERNAL_CONSTRUCTOR(JAVA_OBJECT me,");

        out.append(AdvisorMediator.isCFOpaqueType(object.name) ? "CFTypeRef" : "NSObject*");
        out.append(" wrappedObj){" + C.NT);

        CMethodHelper.setCodeForInjection(null, object.name, false, true, initialInjectedCode,
                replaceableCode, finalInjectedCode);

        if (!replaceableCode.toString().isEmpty()) {
            out.append(replaceableCode);
        } else {
            if (!initialInjectedCode.toString().isEmpty())
                out.append(initialInjectedCode + C.N);
            out.append(COut.packageName);
            if (object.getSuperclass() != null)
                out.append(object.getSuperclass().getProcessedName());
            else if (AdvisorMediator.isCFOpaqueType(object.name))
                out.append("CFType");
            else
                out.append("NSObject");
            out.append("_INTERNAL_CONSTRUCTOR(me, wrappedObj);" + C.NT);
            if (!finalInjectedCode.toString().isEmpty())
                out.append(finalInjectedCode + C.N);
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
     * The C version of the objC object has to be cleaned
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