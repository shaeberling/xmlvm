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
import org.crossmobile.source.xtype.AdvisorWrapper;

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
        StringBuilder methodCall = new StringBuilder();
        String argType = null;
        int i = 1;
        String returnVariableStr = null;
        String accString = "";

        if (AdvisorWrapper.needsAccumulator(object.name)
                || AdvisorWrapper.needsReplacer(object.name)) {
            accString = injectAccumulatorReplacerCode(selName);
        }

        if (!method.isStatic())
            methodCall.append(XMLVM_VAR_THIZ + "\n\t");

        if ((returnVariableStr = CMethodHelper.getReturnVariable(method.getReturnType().toString())) != null)
            methodCall.append(returnVariableStr);
        else
            return null;

        if (method.isStatic())
            methodCall.append(" [" + object.name + " ");
        else
            methodCall.append("[thiz ");

        ListIterator<CArgument> iterator = arguments.listIterator();

        if (arguments.isEmpty())
            methodCall.append(methodName);

        for (String namePart : nameParts) {
            if (iterator.hasNext()) {
                CArgument argument = (CArgument) iterator.next();
                methodCall.append(" " + namePart + ":");
                argType = argument.getType().toString();

                if (!methodHelper.ignore(argType)) {
                    methodCall.append(methodHelper.parseArgumentType(argType, i));
                    i++;
                } else
                    return null;
            }
        }

        methodCall.append("];");
        methodCall.append(accString + "\n");
        return methodCall.toString();
    }
}
