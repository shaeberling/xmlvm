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

import org.crossmobile.source.ctype.CLibrary;
import org.crossmobile.source.ctype.CMethod;
import org.crossmobile.source.ctype.CObject;
import org.crossmobile.source.xtype.AdvisorWrapper;
import org.crossmobile.source.xtype.XInjectedMethod;

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

            out.append(CUtilsHelper.getWrapperComment(method.getArguments(),
                    object.getcClassName(), method.name));

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
                XInjectedMethod iMethods = AdvisorWrapper.getInjectedCodeForSelector(method
                        .getSelectorName(), object.name);
                int index = 0;
                while (index < iMethods.getInjectedCode().size()) {
                    if (iMethods.getInjectedCode().get(index).getMode().equals("before"))
                        initialInjectedCode = iMethods.getInjectedCode().get(index).getCode();
                    else if (iMethods.getInjectedCode().get(index).getMode().equals("after"))
                        finalInjectedCode = iMethods.getInjectedCode().get(index).getCode();
                    else if (iMethods.getInjectedCode().get(index).getMode().equals("replace"))
                        replaceableCode = iMethods.getInjectedCode().get(index).getCode();
                    index++;
                }

            }

            if (methodCall == null)
                notImplemented = true;

            if (notImplemented == true)
                out.append(Constants.T + Constants.NOT_IMPLEMENTED);
            else if (replaceableCode != null)
                out.append(replaceableCode);
            else {
                if (initialInjectedCode != null)
                    out.append(initialInjectedCode);
                if (AdvisorWrapper.needsAutoReleasePool(method.getSelectorName(), object.name))
                    out.append(Constants.AUTORELEASEPOOL_ALLOC + methodCall).append(
                            classInitializer).append(
                            getArrayConversionString(returnType)
                                    + Constants.AUTORELEASEPOOL_RELEASE + Constants.T
                                    + returnString);
                else
                    out.append(methodCall).append(classInitializer).append(
                            getArrayConversionString(returnType) + Constants.T + returnString);
                if (finalInjectedCode != null)
                    out.append(finalInjectedCode);
            }
            out.append(Constants.N + Constants.END_WRAPPER + Constants.N);
        }
    }

    /**
     * In case of a return type being an List, necessary conversion has to be
     * made from Obj-C NSArray.
     * 
     * @param returnType
     *            - return type of the method
     * @return - returns the code that needs to be generated if return type is
     *         List
     */
    private String getArrayConversionString(String returnType) {
        if (returnType.equals("List")) {
            StringBuilder convString = new StringBuilder();
            convString.append(Constants.NT + "JAVA_OBJECT jvc = XMLVMUtil_NEW_ArrayList();");
            convString.append(Constants.NT + "int i = 0;");
            convString.append(Constants.NT + "for (i = 0; i < [objCObj count]; i++) {");
            convString.append(Constants.NTT + "NSObject* c = [objCObj objectAtIndex:i];");
            convString.append(Constants.NTT + "JAVA_OBJECT jc = xmlvm_get_associated_c_object(c);");
            convString.append(Constants.NTT + "if (jc == JAVA_NULL) {");
            convString.append(Constants.NTTT + "XMLVM_INTERNAL_ERROR();" + Constants.NTT + "}");
            convString.append(Constants.NTT + "XMLVMUtil_ArrayList_add(jvc, jc);" + Constants.NT
                    + "}" + Constants.N);
            return convString.toString();
        } else
            return "";
    }
}
