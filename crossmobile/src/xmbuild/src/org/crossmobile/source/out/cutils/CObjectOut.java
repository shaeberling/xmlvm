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
import org.crossmobile.source.xtype.AdvisorWrapper;
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
        out.append(Constants.BEGIN_IMPL + Constants.N);
        emitWrapperCreator();
        out.append(Constants.END_IMPL + Constants.N);
        emitWrapperRegistration();
        CConstructorOut cConsOut = new CConstructorOut(out, lib, object);
        cConsOut.emitConstructors(false);
        CMethodOut cMethodOut = new CMethodOut(out, lib, object);
        cMethodOut.emitMethods(false);
    }

    /**
     * The wrapper creator of the particular class has to be registered
     * 
     * @throws IOException
     */
    private void emitWrapperRegistration() throws IOException {
        out.append(Constants.BEGIN_WRAPPER + "[__INIT_" + object.getcClassName() + "]"
                + Constants.N);
        if (!object.name.contains("NSObject"))
            out.append("xmlvm_register_wrapper_creator(__WRAPPER_CREATOR);" + Constants.N);
        out.append(Constants.END_WRAPPER + Constants.N);

        out.append(Constants.BEGIN_WRAPPER + "[__DELETE_" + object.getcClassName() + "]"
                + Constants.N);
        if (!object.name.contains("NSObject"))
            out.append("__DELETE_" + COut.packageName + "NSObject(me, client_data);" + Constants.N);
        out.append(Constants.END_WRAPPER + Constants.N);

    }

    /**
     * An Internal constructor and a wrapper creator has to be emitted
     * 
     * @throws IOException
     */
    private void emitWrapperCreator() throws IOException {
        if (!object.name.contains("NSObject")) {
            XObject obj = AdvisorWrapper.getSpecialClass(object.name);
            List<String> aliasList = null;

            out.append("void " + object.getcClassName() + "_INTERNAL_CONSTRUCTOR(JAVA_OBJECT me,");
            out.append(" NSObject* wrappedObjCObj){" + Constants.NT);
            out.append("" + COut.packageName + "NSObject_INTERNAL_CONSTRUCTOR(me, wrappedObjCObj);"
                    + Constants.N);

            if (AdvisorWrapper.needsAccumulator(object.name)) {
                out.append(object.getcClassName() + "* thiz = (" + object.getcClassName() + "*)me;"
                        + Constants.N);
                out.append("thiz->fields." + object.getcClassName() + ".acc_array_" + object.name
                        + " = XMLVMUtil_NEW_ArrayList();" + Constants.N);
            }

            out.append("}" + Constants.N);

            out.append(Constants.N + "static JAVA_OBJECT __WRAPPER_CREATOR(NSObject* obj)"
                    + Constants.N + "{");
            out.append(Constants.NT + "if([obj class] == [" + object.name + " class]");

            if (obj != null) {
                if ((aliasList = obj.getAliasList()) != null)
                    for (String alias : aliasList)
                        out.append(" || ([NSStringFromClass([obj class]) isEqual:@\"" + alias
                                + "\"])");
            }

            out.append(") " + Constants.NT + "{" + Constants.N);

            out.append(Constants.TT + "[obj retain];" + Constants.N);
            out.append(Constants.TT + "JAVA_OBJECT jobj = __NEW_" + object.getcClassName() + "();"
                    + Constants.NTT);
            out.append(object.getcClassName() + "_INTERNAL_CONSTRUCTOR(jobj, obj);" + Constants.N);
            out.append(Constants.TT + "return jobj;" + Constants.NT + "}" + Constants.NT);
            out.append("return JAVA_NULL;" + Constants.N + "}" + Constants.N);
        }

    }

}
