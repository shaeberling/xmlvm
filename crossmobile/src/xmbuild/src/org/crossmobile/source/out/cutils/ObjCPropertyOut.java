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

import org.crossmobile.source.ctype.CArgument;
import org.crossmobile.source.ctype.CMethod;
import org.crossmobile.source.ctype.CObject;
import org.crossmobile.source.ctype.CProperty;
import org.crossmobile.source.xtype.AdvisorMediator;
import org.crossmobile.source.xtype.XObject;
import org.crossmobile.source.xtype.XProperty;

/**
 * This class is used to get the code that is related to the getters and setters
 * of a property. This class extends the class <code>CAnyMethodOut</code>
 * overriding the method <code>emit()</code> for getting the wrapper code for
 * the getters and setters.
 * 
 */
public class ObjCPropertyOut extends CAnyMethodOut {

    public ObjCPropertyOut(CObject object) {
        super(object);
    }

    /**
     * In case of properties, generate the getters and setters for the property
     * 
     * @param method
     *            - instance of CMethod associated with the property
     * @param parentIsStruct
     *            - true if the parent of the particular method is a structure
     * @param methodHelper
     *            - instance of methodHelper used to get the code for argument
     *            list
     * @return - return the constructed code for getters and setters
     */
    @Override
    public String emit(CMethod method, boolean parentIsStruct, CMethodHelper methodHelper) {
        if (method.getArguments().isEmpty()) {
            return getGetterCode(method);
        } else {
            return getSetterCode(method, methodHelper);
        }
    }

    /**
     * Method to get the code for a getter of the property
     * 
     * @param method
     *            - the instance of CMethod that represents the getter
     * @return - returns the wrapper code for the getter
     */
    private String getGetterCode(CMethod method) {

        StringBuilder methodCall = new StringBuilder();
        String returnVariableStr = "";
        methodCall.append(C.XMLVM_VAR_THIZ);

        if (((returnVariableStr = CMethodHelper
                .getReturnVariable(method.getReturnType().toString())) != null)
                && (method.derivesFromObjC())) {
            methodCall.append(returnVariableStr);
            methodCall.append("[thiz " + CProperty.getPropertyDef(method.name) + "];");
            if (AdvisorMediator.isCFOpaqueType(method.getReturnType().toString()))
                methodCall.append("XMLVM_VAR_INIT_REF(" + method.getReturnType().toString()
                        + ", objCObj);");
        } else
            return null;

        return methodCall.toString();
    }

    /**
     * Method to get the code related to a setter of a property
     * 
     * @param method
     *            - the instance of CMethod that represents the getter
     * @param methodHelper
     *            - instance of methodHelper used to get the code for argument
     *            list
     * @return returns the wrapper code for the setter
     */
    private String getSetterCode(CMethod method, CMethodHelper methodHelper) {
        String accString = "";
        StringBuilder objCCall = new StringBuilder();
        String beginVarConversion = "";
        String releaseVar = "";
        StringBuilder methodCode = new StringBuilder();

        if (AdvisorMediator.classHasRetainPolicy(object.name)
                || AdvisorMediator.classHasReplacePolicy(object.name)) {
            accString = injectAccumulatorReplacerCode(method.name);
        }

        methodCode.append(methodHelper.getRequiredMacros(method.getArguments(), method.isStatic(),
                false));

        if (method.derivesFromObjC())
            objCCall.append("[thiz " + method.name + ":");
        else
            throw new RuntimeException("Setter does not derive from objective C!");

        List<CArgument> arg = method.getArguments();
        String argType = arg.get(0).getType().toString();
        if ((arg.isEmpty()) || (arg.size() > 1))
            throw new RuntimeException("Argument list is empty or more thn 1");

        if (CMethodHelper.requiresConversion(argType)) {
            beginVarConversion = CMethodHelper.getCodeToConvertVariables(1, argType);
            releaseVar = CMethodHelper.getCodeToReleaseVar(1);
        }

        if (!methodHelper.ignore(arg.get(0).getType().toString()))
            objCCall.append(methodHelper.parseArgumentType(arg.get(0).getType().toString(), 1));
        else
            return null;

        objCCall.append("];");
        methodCode.append(beginVarConversion).append(objCCall).append(accString).append(
                releaseVar + C.N);

        return methodCode.toString();
    }

    /**
     * Method to get code for keeping C-reference of a property to tell GC about
     * the association
     */
    @Override
    protected String injectAccumulatorReplacerCode(String methodName) {
        XProperty prop = null;
        String accString = "";
        XObject obj = AdvisorMediator.getSpecialClass(object.name);
        if ((prop = obj.getPropertyInstance(CProperty.getPropertyDef(methodName))) != null) {
            if (prop.isRetain())
                accString = getAccumulativeCode(1, prop.getType());
            else if (prop.isReplace())
                accString = C.NT + "jthiz->fields." + object.getcClassName() + "." + prop.getName()
                        + " = n1;";
        }

        return accString.toString();
    }

}
