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

/**
 * The class is used as code generator for methods for classes as well as
 * methods associated with structures. Depending on whether the method is a
 * derivative of ObjC selector, a C function or a property getter or setter, the
 * appropriate method is invoked.
 * 
 */
public class CMethodOut {

    private Writer              out                     = null;
    private CObject             object                  = null;
    private CMethodHelper       methodHelper            = null;
    private static final String AUTORELEASEPOOL_ALLOC   = "\n\tNSAutoreleasePool* p = [[NSAutoreleasePool alloc] init];\n";
    private static final String AUTORELEASEPOOL_RELEASE = "\t[ p release];\n";


    public CMethodOut(Writer out, CLibrary lib, CObject object) {
        this.out = out;
        this.object = object;
        this.methodHelper = new CMethodHelper(object.name, object.getcClassName(), lib);
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

            String returnType = method.getReturnType().toString();
            if (!returnType.equals("void")) {
                returnString = methodHelper.getReturnString(returnType);
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

            if (methodCall == null)
                notImplemented = true;

            if (notImplemented == true)
                out.append("\t" + CUtilsHelper.NOT_IMPLEMENTED);
            else {
                if (AdvisorWrapper.needsAutoReleasePool(method.getSelectorName(), object.name))
                    out.append(AUTORELEASEPOOL_ALLOC + methodCall + AUTORELEASEPOOL_RELEASE + "\t"
                            + returnString);
                else
                    out.append(methodCall + "\t" + returnString);
            }
            out.append("\n" + CUtilsHelper.END_WRAPPER + "\n");
        }
    }
}
