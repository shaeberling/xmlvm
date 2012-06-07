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

import java.util.List;
import java.util.ListIterator;

import org.crossmobile.source.ctype.CArgument;
import org.crossmobile.source.ctype.CMethod;
import org.crossmobile.source.ctype.CObject;
import org.crossmobile.source.xtype.AdvisorMediator;

/**
 * This class is used to get the code that is related to methods that are
 * derived from objective-C. This class extends the class
 * <code>CAnyMethodOut</code> overriding the method <code>emit()</code> for
 * getting the wrapper code for methods.
 * 
 */
public class ObjCMethodOut extends CAnyMethodOut {

    public ObjCMethodOut(CObject object) {
        super(object);
    }

    /**
     * This method is used to construct the call for an objC selector (unlike a
     * C function)
     * 
     * @param method
     *            - instance of CMethod for the particular method whose wrapper
     *            has to be generated
     * @param parentIsStruct
     *            - true if the parent of the particular method is a structure
     * @param methodHelper
     *            - instance of methodHelper used to get the code for argument
     * @return Constructed string for a objC selector
     */
    public String emit(CMethod method, boolean parentIsStruct, CMethodHelper methodHelper) {

        List<String> nameParts = method.getNameParts();
        List<CArgument> arguments = method.getArguments();
        String methodName = method.name;
        String selName = method.getSelectorName();
        StringBuilder objCCall = new StringBuilder();
        String argType = null;
        int i = 1;
        String returnVariableStr = null;
        String accString = "";
        StringBuilder releaseList = new StringBuilder("");
        StringBuilder methodCode = new StringBuilder();
        String setReferenceObj = "";

        if (AdvisorMediator.classHasRetainPolicy(object.name)
                || AdvisorMediator.classHasReplacePolicy(object.name)) {
            accString = injectRetainPolicy(selName);
        }

        methodCode.append(CMethodHelper.getRequiredMacros(object.name, arguments,
                method.isStatic(), false));

        if ((returnVariableStr = getReturnVariable(method.getReturnType().toString())) != null)
            objCCall.append(returnVariableStr);
        else
            return null;

        if (method.isStatic())
            objCCall.append(" [" + object.name + " ");
        else
            objCCall.append("[thiz ");

        ListIterator<CArgument> iterator = arguments.listIterator();

        if (arguments.isEmpty())
            objCCall.append(methodName);

        for (String namePart : nameParts) {
            if (iterator.hasNext()) {
                CArgument argument = (CArgument) iterator.next();
                argType = argument.getType().toString();

                if (CMethodHelper.requiresConversion(argType))
                    releaseList.append(CMethodHelper.getCodeToReleaseVar(i));

                if (CMethodHelper.isDoublePointer(argType))
                    setReferenceObj = CMethodHelper.setReferenceVariable(argument.getType(), i);

                objCCall.append(" " + namePart + ":");

                if (!methodHelper.ignore(argType)) {
                    objCCall.append(CMethodHelper.parseArgumentType(argType, i));
                    i++;
                } else
                    return null;
            }
        }

        objCCall.append("];");
        methodCode.append(objCCall);
        objCCall.append(setReferenceObj);
        if (AdvisorMediator.isCFOpaqueType(method.getReturnType().toString()))
            methodCode.append(C.T + "XMLVM_VAR_INIT_REF(" + method.getReturnType().toString()
                    + ", refVar0, var0);" + C.N);
        methodCode.append(accString).append(releaseList + C.N);
        return methodCode.toString();
    }
}
