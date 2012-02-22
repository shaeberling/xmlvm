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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import org.crossmobile.source.ctype.CArgument;
import org.crossmobile.source.ctype.CConstructor;
import org.crossmobile.source.ctype.CEnum;
import org.crossmobile.source.ctype.CLibrary;
import org.crossmobile.source.ctype.CObject;
import org.crossmobile.source.ctype.CStruct;
import org.crossmobile.source.guru.Advisor;
import org.crossmobile.source.out.COut;
import org.crossmobile.source.xtype.AdvisorWrapper;

/**
 * This class is used to generate code for the constructors for classes as well
 * as constructors associated with the structures. In some cases, the
 * constructors are overloaded and Enums are used for the identification of the
 * actual constructor being called. The code generation in such cases is also
 * handled here.
 * 
 */
public class CConstructorOut {

    private Writer        out          = null;
    private CObject       object       = null;
    private CMethodHelper methodHelper = null;


    public CConstructorOut(Writer out, CLibrary lib, CObject object) {
        this.out = out;
        this.object = object;
        this.methodHelper = new CMethodHelper(this.object.name, this.object.getcClassName(), lib);
    }

    /**
     * Used to generate code for constructors
     * 
     * @param isStruct
     *            - Indicates if the constructor is generated for structs
     * @throws IOException
     */
    public void emitConstructors(boolean isStruct) throws IOException {

        boolean has_default_constructor = false;
        List<CArgument> arguments = null;
        CEnum cEnum = null;
        Map<String, List<String>> namePartsMap = new HashMap<String, List<String>>();

        for (CConstructor con : object.getConstructors()) {
            arguments = con.getArguments();

            cEnum = con.getEnum();
            if (cEnum != null)
                namePartsMap = cEnum.getNameParts();

            out.append(CUtilsHelper.getWrapperComment(arguments, object.getcClassName(), con
                    .isOverloaded(), cEnum == null ? null : cEnum.name));

            if (AdvisorWrapper.needsAutoReleasePool(con.getSelectorName(), object.name))
                out.append(Constants.AUTORELEASEPOOL_ALLOC);

            if (isStruct) {
                emitStructConstructor(arguments);
            } else {
                if (arguments.isEmpty())
                    has_default_constructor = true;

                if (con.isOverloaded() && cEnum != null)
                    emitOverloadedConstructor(cEnum.name, namePartsMap, arguments);
                else
                    emitObjectConstructor(con.getNameParts(), arguments);
            }
            if (AdvisorWrapper.needsAutoReleasePool(con.getSelectorName(), object.name))
                out.append(Constants.AUTORELEASEPOOL_RELEASE);
            out.append(Constants.END_WRAPPER + Constants.N);
        }

        if (!has_default_constructor)
            appendDefaultConstructor();
    }

    /**
     * There are few constructors which are overloaded and hence enums are used
     * for this purpose. The code generation in such cases has to be handled in
     * a different way.
     * 
     * @param cEnumName
     *            - represents the enum name as in the java api
     * @param namePartsMap
     *            - map containing the List of nameparts of a selector
     * @param arguments
     *            - List of arguments
     * @throws IOException
     */
    private void emitOverloadedConstructor(String cEnumName,
            Map<String, List<String>> namePartsMap, List<CArgument> arguments) throws IOException {
        Iterator<Entry<String, List<String>>> it = namePartsMap.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, List<String>> pairs = it.next();
            out.append(Constants.NT + "if((" + object.getcClassName() + "_" + cEnumName + "*) n"
                    + (arguments.size() + 1) + " == ");
            out.append(object.getcClassName() + "_" + cEnumName + "_GET_" + pairs.getKey() + "())"
                    + Constants.NT + "{");
            emitObjectConstructor(pairs.getValue(), arguments);
            out.append(Constants.T + "}" + Constants.N);
        }
    }

    /**
     * In case of structures, the constructors should make call to the
     * ObjectiveC 'Make' when new structures have to be created.
     * 
     * @param arguments
     *            - List of arguments
     * @throws IOException
     */
    private void emitStructConstructor(List<CArgument> arguments) throws IOException {
        boolean isFirst = true;
        int i = 1;

        out.append(Constants.NT + object.name + " objCObj = " + object.name + "Make(");

        for (CArgument arg : arguments) {
            if (!isFirst)
                out.append(",");
            isFirst = false;

            if (Advisor.isNativeType(arg.getType().toString())) {
                out.append("n" + (i++));
            } else if (CStruct.isStruct(arg.getType().toString()))
                out.append("to" + arg.getType().toString() + "(n" + (i++) + ")");
            else
                out.append(object.getcClassName() + "* n" + (i++) + ")->fields." + COut.packageName
                        + "NSObject.wrappedObjCObj");
        }
        out.append(");" + Constants.NT);

        out.append(object.getcClassName() + "* jObj = me;" + Constants.N);

        for (CArgument arg : object.getVariables()) {
            out.append(Constants.T + "jObj->fields." + object.getcClassName() + "." + arg.name
                    + "_ = ");
            if (Advisor.isNativeType(arg.getType().toString())) {
                out.append("objCObj." + arg.name + ";" + Constants.N);
            } else if (CStruct.isStruct(arg.getType().toString()))
                out.append("from" + arg.getType().toString() + "(objCObj." + arg.name + ");"
                        + Constants.N);
            else
                out.append("xmlvm_get_associated_c_object(" + "objCObj." + arg.name + ");"
                        + Constants.N);
        }
    }

    /**
     * This method is used to emit the code for constructors in case of general
     * classes.
     * 
     * @param nameParts
     *            - List of nameparts of a selector
     * @param arguments
     *            - List of arguments
     * @throws IOException
     */
    private void emitObjectConstructor(List<String> nameParts, List<CArgument> arguments)
            throws IOException {
        StringBuilder objCCall = new StringBuilder();
        String argType = null;
        int i = 1;
        boolean flag = true;
        boolean implemented = true;
        StringBuilder methodCode = new StringBuilder();
        StringBuilder beginListConversion = new StringBuilder("");
        StringBuilder releaseList = new StringBuilder("");

        objCCall.append(Constants.NT + object.name + "* objCObj = [[" + object.name + " alloc]");

        ListIterator<CArgument> iterator = arguments.listIterator();

        for (String namePart : nameParts) {

            if (!arguments.isEmpty()) {
                if (iterator.hasNext()) {

                    CArgument argument = (CArgument) iterator.next();
                    String parsedArg = null;

                    if (argument.getType().toString().equals("List")) {
                        beginListConversion.append(CMethodHelper.getCodeToConvertToNSArray(i));
                        releaseList.append(CMethodHelper.getCodeToReleaseList(i));
                    }

                    objCCall.append(" " + namePart + ":");
                    argType = argument.getType().toString();

                    if (!methodHelper.ignore(argType)
                            && (parsedArg = methodHelper.parseArgumentType(argType, i)) != null) {
                        objCCall.append(parsedArg);
                        i++;
                    } else {
                        objCCall.delete(0, objCCall.length());
                        objCCall.append(Constants.NT + "XMLVM_NOT_IMPLEMENTED();" + Constants.N);
                        implemented = false;
                        break;
                    }
                }
            } else {
                objCCall.append("init];" + Constants.N);
                flag = false;
            }
        }

        if (implemented) {
            if (flag == true)
                objCCall.append("];");
            methodCode.append(beginListConversion).append(objCCall).append(
                    releaseList + Constants.N);
            out.append(methodCode);
            out.append(Constants.T + object.getcClassName() + "_INTERNAL_CONSTRUCTOR(me, objCObj);"
                    + Constants.N);
        } else {
            out.append(objCCall);
        }
    }

    /**
     * Java API has default constructors for the classes. Such cases has to be
     * handled by wrappers
     * 
     * @throws IOException
     */
    private void appendDefaultConstructor() throws IOException {
        out.append(Constants.BEGIN_WRAPPER + "[" + object.getcClassName() + "___INIT___");
        out.append("]" + Constants.N);
        out.append(Constants.END_WRAPPER + Constants.N);
    }
}
