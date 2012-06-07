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
import org.crossmobile.source.out.COut;
import org.crossmobile.source.xtype.AdvisorMediator;

/**
 * This class is used to get the code that is related to the getters and setters
 * of a property. This class extends the class <code>CAnyMethodOut</code>
 * overriding the method <code>emit()</code> for getting the wrapper code for
 * the getters and setters.
 * 
 */
public class ObjCPropertyOut extends CAnyMethodOut {

    private CObject conformsTo = null;


    public ObjCPropertyOut(CObject object, CObject interfaceObject) {
        super(object);
        conformsTo = interfaceObject;
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

        if (((returnVariableStr = getReturnVariable(method.getReturnType().toString())) != null)
                && (method.derivesFromObjC())) {
            methodCall.append(returnVariableStr);
            methodCall.append("[thiz " + CProperty.getPropertyDef(method.name) + "];" + C.NT);
            if (AdvisorMediator.isCFOpaqueType(method.getReturnType().toString()))
                methodCall.append("XMLVM_VAR_INIT_REF(" + method.getReturnType().toString()
                        + ", refVar0, var0);");
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
        CObject obj = conformsTo == null ? object : conformsTo;
        String accString = "";
        StringBuilder objCCall = new StringBuilder();
        String releaseVar = "";
        String initDelegateWrapper = "";
        String releaseDelegate = "";
        String setAssociation = "";
        StringBuilder methodCode = new StringBuilder();
        String propertyName = CProperty.getPropertyDef(method.name);

        if (AdvisorMediator.propertyNeedsToBeReplaced(propertyName, obj.name)
                || AdvisorMediator.propertyNeedsToBeRetained(propertyName, obj.name)) {
            releaseDelegate = getReleaseDelegateCode(propertyName);
            setAssociation = getSetAssociationCode();
            accString = getAccumulativeCode(1);
        }

        methodCode.append(CMethodHelper.getRequiredMacros(object.name, method.getArguments(),
                method.isStatic(), false));

        if (method.derivesFromObjC())
            objCCall.append("[thiz " + method.name + ":");
        else
            throw new RuntimeException("Setter does not derive from objective C!");

        List<CArgument> arg = method.getArguments();
        String argType = arg.get(0).getType().toString();
        if ((arg.isEmpty()) || (arg.size() > 1))
            throw new RuntimeException("Argument list is empty or more thn 1");

        if (CMethodHelper.requiresConversion(argType))
            releaseVar = CMethodHelper.getCodeToReleaseVar(1);

        if (methodHelper.isDelegateProperty(arg.get(0).getType().toString())) {
            initDelegateWrapper = initDelegateWrapper(arg.get(0), propertyName);
            objCCall.append("jwrapper->nativeDelegateWrapper_");
        } else if (!methodHelper.ignore(arg.get(0).getType().toString()))
            objCCall.append(CMethodHelper.parseArgumentType(arg.get(0).getType().toString(), 1));
        else
            return null;

        objCCall.append("];");
        methodCode.append(releaseDelegate).append(initDelegateWrapper).append(objCCall).append(
                accString).append(setAssociation).append(releaseVar + C.N);

        return methodCode.toString();
    }

    private String getSetAssociationCode() {
        StringBuilder setAssociation = new StringBuilder();
        setAssociation
                .append("objc_setAssociatedObject(thiz, &key, jwrapper->nativeDelegateWrapper_, OBJC_ASSOCIATION_RETAIN);"
                        + C.NT);
        setAssociation.append("[jwrapper->nativeDelegateWrapper_ release];" + C.NT);
        return setAssociation.toString();
    }

    private String getReleaseDelegateCode(String property) {
        return "if(thiz." + property + "!= nil) [thiz." + property + " release];" + C.NT;

    }

    /**
     * If the setter is for delegate property then the delegate wrapper object
     * has to be initialized and the reference has to be stored.
     * 
     * @param delegate
     *            - Argument to setter - which is a delegate
     * @return - string for initializing the delegate wrapper object
     */
    private String initDelegateWrapper(CArgument delegate, String property) {
        StringBuilder initializer = new StringBuilder("");
        String delegateClassName = COut.packageName + delegate.getType().toString();

        initializer.append(delegateClassName
                + "_Wrapper* jwrapper = __ALLOC_INIT_DELEGATE_WRAPPER_" + delegateClassName
                + "(n1);" + C.NT);
        initializer.append("[jwrapper->nativeDelegateWrapper_ addSource: jthiz: thiz];" + C.NT);

        return initializer.toString();
    }

}
