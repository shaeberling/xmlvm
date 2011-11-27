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

package org.crossmobile.source.cutils;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.crossmobile.source.ctype.CArgument;
import org.crossmobile.source.ctype.CConstructor;
import org.crossmobile.source.ctype.CEnum;
import org.crossmobile.source.ctype.CFunction;
import org.crossmobile.source.ctype.CLibrary;
import org.crossmobile.source.ctype.CMethod;
import org.crossmobile.source.ctype.CObject;
import org.crossmobile.source.ctype.CProperty;
import org.crossmobile.source.guru.Advisor;

public class CObjectOutput {
    private Writer              out             = null;
    private CLibrary            lib             = null;
    private CObject             object          = null;
    private String              objectClassName = null;
    private CMethodHelper       methodHelper    = null;
    private Set<CFunction>     func            = null;


    public CObjectOutput(Writer out, CLibrary lib, CObject obj) {
        super();
        this.out = out;
        this.lib = lib;
        this.object = obj;
        this.objectClassName = object.getcClassName();
        this.methodHelper = new CMethodHelper(this.object.name, this.objectClassName, this.lib);
        this.func = lib.getFunctions();
    }

    /*
     * Wrapper creator and Internal Constructor need to be appended for each of
     * the classes which are not structs.
     */
    public void appendWrapperCreator() throws IOException {
        String map = null;
        if (!object.name.contains("NSObject")) {
            out.append("void " + objectClassName + "_INTERNAL_CONSTRUCTOR(JAVA_OBJECT me,");
            out.append(" NSObject* wrappedObjCObj){\n\t");
            out.append("org_xmlvm_ios_NSObject_INTERNAL_CONSTRUCTOR(me, wrappedObjCObj);\n");
            
            if(Advisor.isAccumulatorNeeded(object.name)){
                out.append(objectClassName + "* thiz = (" + objectClassName +"*)me;\n");
                out.append("thiz->fields."+objectClassName+".acc_Array = XMLVMUtil_NEW_ArrayList();\n");
            }
        
            out.append("}");
            

            out.append("\n\nstatic JAVA_OBJECT __WRAPPER_CREATOR(NSObject* obj)\n{");
            out.append("\t\tif([obj class] == [" + object.name + " class]");
            
            if((map =Advisor.getInternalClassMap(object.name))!=null)
                out.append(" || ([NSStringFromClass([obj class]) isEqual:@\"" + map +"\"])");
            out.append(") {\n");
            
            out.append("\t\t[obj retain];\n");
            out.append("\t\tJAVA_OBJECT jobj = __NEW_" + objectClassName + "();\n\t\t");
            out.append(objectClassName + "_INTERNAL_CONSTRUCTOR(jobj, obj);\n");
            out.append("\t\treturn jobj;\n\t}");
            out.append("return JAVA_NULL;\n}\n");
        }
    }
    
    public String getMethodName(String methodName){
        
        for (CFunction f: func){
           
            if(f.name.toUpperCase().endsWith(methodName.toUpperCase()))
                return f.name;
        }
        return null;
    }
    
    public void appendFunction() throws IOException {

        /* Parse the methods */
        for (CMethod method : object.getMethods()) {
            Boolean notImplemented = false;
            String methodCall = null;
            String tempStr = null;

            String returnType = method.getReturnType().toString();
            
            out.append(CUtilsHelper.getWrapperComment(method.getArguments(), objectClassName, method.name));
            
            /*
             * Check if the method is a property, call appropriate getters
             * and setters
             */
            if(method.isProperty()){
                methodCall = "\nXMLVM_VAR_THIZ;\n";
                tempStr = methodHelper.getReturnString(returnType);
                if(tempStr==null)
                    notImplemented=true;
                else
                    methodCall = methodCall + tempStr;
                if(!method.derivesFromObjC())
                    methodCall = methodCall +"[thiz " +CProperty.getPropertyDef(method.name) + "];\n";
                else
                    notImplemented=true;
            }
            else {
                if(method.derivesFromObjC())
                    methodCall = parseMethod(method);
                else
                    methodCall = parseCMethod(method.getArguments(),method.name, returnType, method.isStatic());
                if(methodCall == null)
                    notImplemented = true;
            }
            
             /*
             * If the implementation is not provided then add throw error!
             */
            if(notImplemented == true)
                out.append(CUtilsHelper.NOT_IMPLEMENTED + "\n");
            else {
                out.append( methodCall +"\n");
            }
            
            out.append(CUtilsHelper.END_WRAPPER + "\n");

       }
    }
    
    public String parseCMethod(List<CArgument> arguments, String methodName, String returnType, boolean isStatic){
        StringBuilder methodCall = new StringBuilder();
        
        String returnString = " ";
        String tempName = null;
        boolean isFirst = true;
        String argType = null;
        int i = 1;
        
        if(!returnType.equals("void")){
          returnString = methodHelper.getReturnString(returnType);
          if(returnString == null)
              return null;      
          }
        
        if(isStatic){
                methodCall.append(returnString+ "(");
                if((tempName = getMethodName(methodName)) != null)
                    methodCall.append(tempName + "(");
                else
                    methodCall.append(methodName + "(");                    
        }
        else {
                return null;                          
        }
                       
        ListIterator<CArgument> iterator = arguments.listIterator();
        while (iterator.hasNext()) {

            CArgument argument = (CArgument) iterator.next();
            
            argType = argument.getType().toString();
            
            if(!isFirst) 
                methodCall.append(",");
            isFirst = false;
    
            if(!methodHelper.ignore(argType)) {
                methodCall.append(methodHelper.parseArgumentType(argType, i));
                i++;
            }
            else{
                return null;
            }
        }
          
        methodCall.append("));\n");
        
        return methodCall.toString();
    }

    public String parseMethod(CMethod method){
        List<String> nameParts = method.getNameParts();
        List<CArgument> arguments = method.getArguments();
        String methodName = method.name;
        String returnType = method.getReturnType().toString();
        boolean isStatic = method.isStatic();
        List<String> accumulativeArgs = null;
        
        if(Advisor.isAccumulatorNeeded(object.name)) {
            accumulativeArgs = Advisor.getAccumulativeArgs(object.name, method.name);
        }
        
        StringBuilder methodCall = new StringBuilder();
        StringBuilder accString = new StringBuilder();
        String returnString = " ";
        String argType = null;
        int i = 1;
        
        if(!returnType.equals("void")){
          returnString = methodHelper.getReturnString(returnType);
          if(returnString == null)
              return null;      
          }
        
        if(isStatic){
                methodCall.append(returnString+ "( [" + object.name + " ");
        }
        else {
                methodCall.append("\nXMLVM_VAR_THIZ;\n");
                methodCall.append(returnString + "( [thiz ");
                          
        }
        
        ListIterator<CArgument> iterator = arguments.listIterator();
        
        
        if(arguments.isEmpty())
            methodCall.append(methodName);

        
        for (String namePart : nameParts) {

                if (iterator.hasNext()) {

                    CArgument argument = (CArgument) iterator.next();

                    methodCall.append(" " + namePart + ":");
                    
                    argType = argument.getType().toString();
                    
                    if(accumulativeArgs!=null){
                        if(accumulativeArgs.contains(argument.name)){
                            accString.append("XMLVMUtil_ArrayList_add(jthiz->fields."+objectClassName +".acc_Array, ");
                            accString.append("((org_xmlvm_ios_NSObject*) n" + i +")");
                            accString.append("->fields.org_xmlvm_ios_NSObject.wrappedObjCObj);\n");
                        }
                    }
   
                    if (!methodHelper.ignore(argType)) {
                        methodCall.append(methodHelper.parseArgumentType(argType, i));
                        i++;
                    }
                    else
                        return null;
                    
                }
        }

        methodCall.append("]);\n");
        methodCall.append(accString);
        return methodCall.toString();
    }
    
    
    public void appendConstructor() throws IOException {

        boolean has_default_constructor = false;
        List<CArgument> arguments = null;
        CEnum cEnum = null;
        Map<String, List<String>> namePartsMap = new HashMap<String, List<String>>();

        for (CConstructor con : object.getConstructors()) {
            arguments = con.getArguments();

            /*
             * If there is default constructor, then need not add explicitly
             */
            if (arguments.isEmpty())
                has_default_constructor = true;

            /*
             * Some constructors are overloaded. Handle such cases. Need to
             * generate 'if-else' in the wrapper to check the enum value to
             * identify the right constructor.
             */
            if (con.isOverloaded()) {
                cEnum = con.getEnum();
                if (cEnum != null)
                    namePartsMap = cEnum.getNameParts();
            }
            
            out.append(CUtilsHelper.getWrapperComment(arguments, objectClassName, con
                    .isOverloaded(), cEnum == null ? null : cEnum.name));

            if (con.isOverloaded()) {
                Iterator it = namePartsMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pairs = (Map.Entry) it.next();
                    out.append("\n" + "if((" + objectClassName + "_" + cEnum.name + "*) n"
                            + (arguments.size() + 1) + " == ");
                    out.append(objectClassName + "_" + cEnum.name + "_GET_" + pairs.getKey()
                            + "()){\n");
                    parseConstructor((List<String>) pairs.getValue(), arguments);
                    out.append("}\n");
                }
            } else {
                parseConstructor(con.getNameParts(), arguments);
            }

            out.append(CUtilsHelper.END_WRAPPER + "\n");
        }

        if (!has_default_constructor)
            appendDefaultConstructor(out, object);
    }

    /*
     * Generate the actual call to bjective C constructor
     */
    private void parseConstructor(List<String> nameParts, List<CArgument> arguments)
            throws IOException {
        StringBuilder string = new StringBuilder();
        String argType = null;
        int i = 1;
        boolean flag = true;
        boolean implemented = true;
        string.append("\n" + object.name + "* objCObj = [[" + object.name + " alloc]");

        ListIterator<CArgument> iterator = arguments.listIterator();

        for (String namePart : nameParts) {

            if (!arguments.isEmpty()) {
                if (iterator.hasNext()) {

                    CArgument argument = (CArgument) iterator.next();
                    String parsedArg =null;

                    string.append(" " + namePart + ":");
                    argType = argument.getType().toString();

                    if (!methodHelper.ignore(argType)
                            && (parsedArg = methodHelper.parseArgumentType(argType, i)) != null) {
                        string.append(parsedArg);
                        i++;
                    }

                    else {
                        string.delete(0, string.length());
                        string.append("\nXMLVM_NOT_IMPLEMENTED()");
                        implemented = false;
                        flag = false;
                        break;
                    }            
                }
            } else {
                string.append("init]");
                flag = false;
            }
        }

        if (flag == true) {
            string.append("]");
        }

        string.append(";\n");
        out.append(string);

        if (implemented) {
            out.append(objectClassName + "_INTERNAL_CONSTRUCTOR(me, objCObj);\n");
        }
    }

    private void appendDefaultConstructor(Writer out, CObject object) throws IOException {
        out.append(CUtilsHelper.BEGIN_WRAPPER + "[" + objectClassName + "___INIT___");
        out.append("]\n");
        out.append(CUtilsHelper.END_WRAPPER + "\n");
    }

    /*
     * Code has to be injected to register the wrapper creator
     */
    public void appendWrapperRegistration() throws IOException {
        out.append(CUtilsHelper.BEGIN_WRAPPER + "[__INIT_" + objectClassName + "]\n");
        if (!object.name.contains("NSObject"))
            out.append("xmlvm_register_wrapper_creator(__WRAPPER_CREATOR);\n");
        out.append(CUtilsHelper.END_WRAPPER + "\n");

        out.append(CUtilsHelper.BEGIN_WRAPPER + "[__DELETE_" + objectClassName + "]\n");
        if (!object.name.contains("NSObject"))
            out.append("__DELETE_org_xmlvm_ios_NSObject(me, client_data);\n");
        out.append(CUtilsHelper.END_WRAPPER + "\n");
    }

}
