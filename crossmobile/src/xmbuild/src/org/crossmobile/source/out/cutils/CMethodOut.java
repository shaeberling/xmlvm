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
import org.crossmobile.source.ctype.CMethod;
import org.crossmobile.source.ctype.CObject;
import org.crossmobile.source.xtype.AdvisorWrapper;
import org.crossmobile.source.xtype.XCode;

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


    public CMethodOut(Writer out, CLibrary lib, CObject object) {
        this.out = out;
        this.object = object;
        this.methodHelper = new CMethodHelper(object.name, lib);
    }

    /**
     * Used to generate the code for wrappers of methods of a particular class
     * 
     * @param isStruct
     *            - true if it is a structure
     * @throws IOException
     */
    public void emitMethods(boolean isStruct) throws IOException {

        for (CMethod method : object.getMethods()) {

            if (!AdvisorWrapper.methodIsIgnore(method.getSelectorName(), object.name)) {
                Boolean notImplemented = false;
                String methodCall = null;
                String returnString = "";
                CAnyMethodOut methodType = null;
                StringBuilder classInitializer = new StringBuilder("");

                String initialInjectedCode = null;
                String replaceableCode = null;
                String finalInjectedCode = null;

                String returnType = method.getReturnType().toString();
                if (!returnType.equals("void")) {
                    returnString = methodHelper.getReturnString(returnType, classInitializer);

                    if (returnString == null)
                        notImplemented = true;
                }

                if (!method.isProperty()
                        && AdvisorWrapper.hasSpecialArgumentsDefined(method.getSelectorName(),
                                object.name, method.isProperty()))
                    out.append(CUtilsHelper.getWrapperComment(AdvisorWrapper.getArgumentsForMethod(
                            method.getSelectorName(), object.name), object.getcClassName(),
                            method.name));
                else
                    out.append(CUtilsHelper.getWrapperComment(method.getArguments(), object
                            .getcClassName(), method.name));

                if (method.isProperty()) {
                    methodType = new ObjCPropertyOut(object);
                } else if (isStruct) {
                    methodType = new CFunctionOut(object);
                } else {
                    if (method.derivesFromObjC())
                        methodType = new ObjCMethodOut(object);
                    else
                        methodType = new CFunctionOut(object);
                }

                methodCall = methodType.emit(method, isStruct, methodHelper);

                if (AdvisorWrapper.selectorHasCodeInjection(method.getSelectorName(), object.name)) {
                    List<XCode> iCodeList = AdvisorWrapper.getInjectedCodeForSelector(method
                            .getSelectorName(), object.name);
                    int index = 0;
                    while (index < iCodeList.size()) {
                        if (iCodeList.get(index).getMode().equals("before"))
                            initialInjectedCode = iCodeList.get(index).getCode();
                        else if (iCodeList.get(index).getMode().equals("after"))
                            finalInjectedCode = iCodeList.get(index).getCode();
                        else if (iCodeList.get(index).getMode().equals("replace"))
                            replaceableCode = iCodeList.get(index).getCode();
                        index++;
                    }

                }

                if (methodCall == null)
                    notImplemented = true;

                if (replaceableCode != null)
                    out.append(replaceableCode);
                else if (notImplemented == true)
                    out.append(C.NOT_IMPLEMENTED);
                else {
                    if (initialInjectedCode != null)
                        out.append(initialInjectedCode);
                    if (AdvisorWrapper.needsAutoReleasePool(method.getSelectorName(), object.name))
                        out.append(C.AUTORELEASEPOOL_ALLOC + methodCall).append(classInitializer)
                                .append(C.AUTORELEASEPOOL_RELEASE + C.T + returnString);
                    else
                        out.append(methodCall).append(classInitializer).append(C.T + returnString);
                    if (finalInjectedCode != null)
                        out.append(finalInjectedCode);
                }
                out.append(C.N + C.END_WRAPPER + C.N);
            }
        }
    }
}
