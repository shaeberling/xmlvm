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
import org.crossmobile.source.xtype.AdvisorMediator;

/**
 * This class is used to get the code that is related to the methods which are C
 * functions and are not derived from objective C selectors. This class extends
 * the class <code>CAnyMethodOut</code> overriding the method <code>emit</code>
 * for getting the wrapper code for the C function.
 * 
 */
public class CFunctionOut extends CAnyMethodOut {

    public CFunctionOut(CObject object) {
        super(object);
    }

    /**
     * There are methods that are C functions and are not selectors. In such
     * cases, this method is called for getting the code for wrappers.
     * 
     * @param method
     *            - instance of CMethod for the particular C function
     * @param parentIsStruct
     *            - true if the method is associated with a structure
     * @param methodHelper
     *            - instance of methodHelper used to get the code for argument
     * 
     * @return Constructed string for the wrapper of C function; null if not
     *         implemented
     */
    public String emit(CMethod method, boolean parentIsStruct, CMethodHelper methodHelper) {

        StringBuilder methodCall = new StringBuilder();
        String argList = null;
        String tempName = null;
        String returnVariableStr = null;
        StringBuilder beginVarConversion = new StringBuilder("");
        StringBuilder releaseVar = new StringBuilder("");
        int i = 1;

        if (method.isStatic() || parentIsStruct || AdvisorMediator.isCFOpaqueType(object.name)) {

            List<CArgument> arguments = method.getArguments();
            methodCall.append(methodHelper.getRequiredMacros(arguments, method.isStatic(), false));

            for (CArgument arg : arguments) {
                if (CMethodHelper.requiresConversion(arg.getType().toString())) {
                    beginVarConversion.append(CMethodHelper.getCodeToConvertVariables(i, arg
                            .getType().toString()));
                    releaseVar.append(CMethodHelper.getCodeToReleaseVar(i));
                }
                i++;
            }

            methodCall.append(beginVarConversion + C.NT);

            if ((returnVariableStr = CMethodHelper.getReturnVariable(method.getReturnType()
                    .toString())) != null)
                methodCall.append(returnVariableStr);
            else
                return null;

            if (parentIsStruct || AdvisorMediator.isCFOpaqueType(object.name)) {
                methodCall.append(methodHelper.getModifiedFunctionName(method.name, true));
                argList = getArgumentsToPass(arguments, method.isStatic(), parentIsStruct,
                        methodHelper);
            } else if ((tempName = methodHelper.getModifiedFunctionName(method.name, false)) != null) {
                methodCall.append(tempName);
                argList = getArgumentsToPass(arguments, method.isStatic(), false, methodHelper);
            } else {
                methodCall.append(method.name);
                argList = getArgumentsToPass(arguments, method.isStatic(), false, methodHelper);
            }

        } else
            return null;

        if (argList == null)
            return null;

        methodCall.append(argList + ";" + C.NT);
        methodCall.append(releaseVar);
        if (AdvisorMediator.isCFOpaqueType(method.getReturnType().toString()))
            methodCall.append(C.T + "XMLVM_VAR_INIT_REF(" + method.getReturnType().toString()
                    + ", objCObj);" + C.N);
        return methodCall.toString();
    }

    /**
     * Parses the list of arguments for a C-function and returns the string
     * containing comma separated argument list
     * 
     * @param arguments
     *            - List of arguments for a function
     * @param isStatic
     *            - if the method is static.
     * @param parentIsStruct
     *            - if the method is associated with a structure
     * @param methodHelper
     *            - an instance of CMethodHelper used to get the code for
     *            arguments
     * @return returns the string with comma separated arguments; If the type of
     *         argument is currently in ignore list then return null.
     */
    public String getArgumentsToPass(List<CArgument> arguments, boolean isStatic,
            boolean parentIsStruct, CMethodHelper methodHelper) {

        StringBuilder argList = new StringBuilder();
        boolean isFirst = true;
        int i = 1;

        argList.append("(");

        if (!isStatic) {

            if (parentIsStruct)
                argList.append("to" + object.name + "(me)");
            else if (AdvisorMediator.getOpaqueBaseType(object.name) != null)
                argList.append("thiz");
            isFirst = false;
        }

        for (CArgument arg : arguments) {

            String argType = arg.getType().toString();
            String parsedArg = null;

            if (!isFirst)
                argList.append(",");
            isFirst = false;

            if (!methodHelper.ignore(argType)
                    && (parsedArg = methodHelper.parseArgumentType(argType, i)) != null) {
                argList.append(parsedArg);
                i++;
            } else {
                return null;
            }
        }
        argList.append(")");
        return argList.toString();
    }
}
