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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.crossmobile.source.ctype.CLibrary;
import org.crossmobile.source.ctype.CMethod;
import org.crossmobile.source.ctype.CObject;
import org.crossmobile.source.ctype.CStruct;
import org.crossmobile.source.ctype.CType;
import org.crossmobile.source.xtype.AdvisorMediator;

/**
 * The class is used as code generator for methods for classes as well as
 * methods associated with structures. Depending on whether the method is a
 * derivative of ObjC selector, a C function or a property getter or setter, the
 * appropriate method is invoked.
 * 
 */
public class CMethodOut {

    private Writer        out          = null;
    private CObject       object       = null;
    private CMethodHelper methodHelper = null;
    private CLibrary      lib          = null;


    public CMethodOut(Writer out, CLibrary lib, CObject object) {
        this.out = out;
        this.object = object;
        this.lib = lib;
        this.methodHelper = new CMethodHelper(object.name, lib);
    }

    /**
     * Used to generate the code for wrappers of methods of a particular class
     * 
     * @param isStruct
     *            - true if it is a structure
     * @throws IOException
     * @throws IOException
     */

    public void emitMethods() throws IOException {
        for (CMethod method : object.getMethods()) {
            emitMethod(method, null);
        }
        if (object.hasInterfaces()) {
            List<CMethod> methodList = new ArrayList<CMethod>();
            for (CMethod m : object.getMethods())
                methodList.add(m);
            emitInterfaceMethods(object, object.getInterfaces(), methodList);
        }
    }

    /**
     * There are instances where a class conforms to a protocol and the
     * properties of the protocol can be accessed by the instance of the class
     * conforming to a protocol. Eg:UITextInput conforms to UITextInputTraits.
     * In this case, the properties of UITextInputTraits can be accessed by
     * instance of UITextInput. Currently only properties of the protocol the
     * class is conforming to are made available within the class. The methods
     * of the protocol are not accessible from the class. This can be extended
     * to handle methods as well.
     */
    private void emitInterfaceMethods(CObject currObject, Set<CType> interfaces,
            List<CMethod> methodList) throws IOException {
        boolean flag = false;

        for (CType c : interfaces) {
            CObject ifcObj = lib.getObject(c.getProcessedName());
            for (CMethod m : ifcObj.getMethods())
                if (m.isProperty()) {
                    for (CMethod im : methodList) {
                        if (im.isProperty()
                                && im.getDefinitions().get(0).equals(m.getDefinitions().get(0))
                                && im.name.equals(m.name)) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        flag = false;
                        continue;
                    }
                    emitMethod(m, ifcObj);
                    methodList.add(m);
                }
            if (ifcObj.hasInterfaces())
                emitInterfaceMethods(currObject, ifcObj.getInterfaces(), methodList);
        }

    }

    public void emitMethod(CMethod method, CObject currObject) throws IOException {
        currObject = currObject == null ? object : currObject;
        if (!AdvisorMediator.methodIsIgnore(method.getSelectorName(), currObject.name)) {
            Boolean notImplemented = false;
            String methodCall = null;
            String returnString = "";
            CAnyMethodOut methodType = null;

            StringBuilder initialInjectedCode = new StringBuilder();
            StringBuilder replaceableCode = new StringBuilder();
            StringBuilder finalInjectedCode = new StringBuilder();

            String returnType = method.getReturnType().toString();
            if (!returnType.equals("void")) {
                returnString = methodHelper.getReturnString(returnType);

                if (returnString == null)
                    notImplemented = true;
            }

            if (!method.isProperty()
                    && AdvisorMediator.hasSpecialArgumentsDefined(method.getSelectorName(),
                            currObject.name, method.isProperty()))
                out.append(CUtilsHelper.getWrapperComment(AdvisorMediator.getArgumentsForMethod(
                        method.getSelectorName(), currObject.name), object.getcClassName(),
                        method.name, AdvisorMediator.methodIsOverridden(method.getSelectorName(),
                                currObject.name)));
            else
                out.append(CUtilsHelper.getWrapperComment(method.getArguments(), object
                        .getcClassName(), method.name, AdvisorMediator.methodIsOverridden(method
                        .getSelectorName(), currObject.name)));

            if (method.isProperty()) {
                methodType = new ObjCPropertyOut(object, currObject);
            } else if (CStruct.isStruct(object.name)) {
                methodType = new CFunctionOut(object);
            } else {
                if (method.derivesFromObjC())
                    methodType = new ObjCMethodOut(object);
                else
                    methodType = new CFunctionOut(object);
            }

            methodCall = methodType.emit(method, CStruct.isStruct(object.name), methodHelper);

            CMethodHelper.setCodeForInjection(method.getSelectorName(), currObject.name, true,
                    false, initialInjectedCode, replaceableCode, finalInjectedCode);

            if (methodCall == null)
                notImplemented = true;

            if (!replaceableCode.toString().isEmpty())
                out.append(replaceableCode);
            else if (notImplemented == true)
                out.append(C.NOT_IMPLEMENTED);
            else {
                if (!initialInjectedCode.toString().isEmpty())
                    out.append(initialInjectedCode);
                if (AdvisorMediator.needsAutoReleasePool(method.getSelectorName(), currObject.name))
                    out.append(C.AUTORELEASEPOOL_ALLOC + methodCall).append(
                            C.AUTORELEASEPOOL_RELEASE + C.NT);
                else
                    out.append(methodCall).append(C.NT);
                if (!finalInjectedCode.toString().isEmpty())
                    out.append(finalInjectedCode);
                out.append(returnString);
            }
            out.append(C.N + C.END_WRAPPER + C.N);
        }
    }

}
