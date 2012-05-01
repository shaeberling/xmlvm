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

package org.crossmobile.source.xtype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class abstracts all the data from the 'class' tag from the advisor. All
 * the external advice that is essential for the C wrapper generation is stored
 * in this class. The list of methods and properties of the class that need
 * special advice are maintained. It also maintains the list of code that needs
 * to be injected at the class level.
 * 
 */
public class XObject {

    private String                 className                 = null;
    private boolean                retain                    = false;
    private boolean                replace                   = false;
    private List<String>           aliasList                 = null;
    private boolean                hasExternallyInjectedCode = false;
    private List<XProperty>        propertyList              = null;
    private List<XInjectedMethod>  externallyInjectedCode    = null;
    private List<String>           references                = null;
    private boolean                hasMandatoryMethods       = false;
    private String                 opaqueBaseType            = null;
    private boolean                noInternalConstructor     = false;

    public final static int        RETAIN                    = 0;
    public final static int        RELEASE                   = 1;
    public static final int        REPLACE                   = 2;

    private Map<String, XMethod>   methodMap                 = null;
    private Map<String, XProperty> propMap                   = null;
    private boolean                hasDelegateMethods        = false;


    public XObject(String className, List<XMethod> methodList, List<XProperty> propList,
            List<String> aliases, List<String> refList, List<XInjectedMethod> extInjectedMethodList) {
        this.className = className;
        this.aliasList = aliases;
        this.references = refList;
        this.propertyList = propList;
        createMethodMap(methodList);
        createPropertyMap(propList);
        this.externallyInjectedCode = extInjectedMethodList;
        if (!extInjectedMethodList.isEmpty())
            hasExternallyInjectedCode = true;
        setFlags(methodList, propList);
    }

    public List<XProperty> getProperties() {
        return propertyList;
    }

    public void setPropertyList(ArrayList<XProperty> propertyList) {
        this.propertyList = propertyList;
    }

    private void createPropertyMap(List<XProperty> propList) {
        propMap = new HashMap<String, XProperty>();
        for (XProperty prop : propList)
            propMap.put(prop.getName(), prop);
    }

    private void createMethodMap(List<XMethod> methodList) {
        methodMap = new HashMap<String, XMethod>();
        for (XMethod method : methodList)
            methodMap.put(method.getSelectorName(), method);
    }

    /**
     * Sets the flags in case of accumulators and replacer. These flags are
     * necessary while generating the header file for a particular class.
     * 
     * @param propList
     *            - List of properties from advice
     */
    private final void setFlags(List<XMethod> methods, List<XProperty> propList) {
        if (methods != null) {
            for (XMethod m : methods) {
                for (XArg a : m.getArgList()) {
                    if (a.isRetain())
                        retain = true;
                    else if (a.isReplace())
                        replace = true;
                }
                if (m.isMandatory())
                    hasMandatoryMethods = true;
                if (m.isDelegate())
                    hasDelegateMethods = true;
            }
        }

        if (propList != null) {
            for (XProperty p : propList) {
                if (p.isRetain())
                    retain = true;
                else if (p.isReplace())
                    replace = true;
            }
        }
    }

    public boolean isRetain() {
        return retain;
    }

    public boolean isReplace() {
        return replace;
    }

    public String getClassName() {
        return className;
    }

    public Map<String, XMethod> getMethodMap() {
        return methodMap;
    }

    public Map<String, XProperty> getPropertyMap() {
        return propMap;
    }

    public boolean hasMandatoryMethods() {
        return hasMandatoryMethods;
    }

    /**
     * Returns the injected code that is associated with the selector
     * 
     * @param selector
     *            - name of the selector for which the injected code is requesed
     *            for
     * @return - Instance of XInjectedMethod which has information about
     *         injected code specified by advisor
     */
    public XInjectedMethod getInjectedCodeForSelector(String selector) {
        return methodMap.get(selector).getInjectedCode();
    }

    /**
     * Returns the list of XInjectedMethod which contain the information about
     * the methods that are external to ObjC.
     * 
     * @return returns list of methods that are external to ObjC for a
     *         particular class
     */
    public List<XInjectedMethod> getExternallyInjectedMethods() {
        return this.externallyInjectedCode;
    }

    public List<String> getAliasList() {
        return aliasList;
    }

    public List<String> getReferences() {
        return references;
    }

    public boolean selectorHasInjectedCode(String selName) {
        XMethod m = methodMap.get(selName);
        if (m != null)
            return methodMap.get(selName).hasInjectedCode();
        else
            return false;
    }

    public boolean hasExternallyInjectedCode() {
        return hasExternallyInjectedCode;
    }

    public XMethod getMethodInstance(String selectorName) {
        return methodMap.get(selectorName);
    }

    public XProperty getPropertyInstance(String propName) {
        return propMap.get(propName);
    }

    /**
     * Method to identify if the method is mandatory for the user to implement.
     * If it is mandatory, then the implementation of the interface leaves the
     * method as abstract which will force the developer to implement the
     * method.
     * 
     * @param selName
     * @return
     */
    public boolean selectorIsMandatory(String selName) {
        return methodMap.get(selName) != null ? methodMap.get(selName).isMandatory() : false;
    }

    /**
     * The implementation for interfaces are given default return values as 0,
     * null false. But if this value has to be overridden by any specific
     * values, then it is specified using special advice.
     * 
     * @param selName
     *            - name of the selector
     * @return - return value as specified in Advisor.xml, null otherwise
     */
    public String getDefaultReturnValueForSelector(String selName) {
        return methodMap.get(selName) != null ? methodMap.get(selName).getDefaultRetunValue()
                : null;
    }

    /**
     * There are methods(Eg:UIView.drawRect) which are not part of protocols but
     * can be overridden by developer. In such cases, we need wrappers to bridge
     * the Obj-C and generated C code.
     * 
     * @param selName
     *            - Name of the selector
     * @return
     */
    public boolean isDelegate(String selName) {
        return methodMap.get(selName) != null ? methodMap.get(selName).isDelegate() : false;
    }

    /**
     * There are methods(Eg:UIView.drawRect) which are not part of protocols but
     * can be overridden by developer. In such cases, we need wrappers to bridge
     * the Obj-C and generated C code.
     * 
     * @return
     */
    public boolean hasDelegateMethods() {
        return hasDelegateMethods;
    }

    public void setOpaqueBaseType(String opaqueBaseType) {
        this.opaqueBaseType = opaqueBaseType;
    }

    /**
     * There are Core Foundation Opaque type that derive from CFType and this
     * information needs to be provided using the advisor.
     * 
     * @return - true if object derives from CFType (as specified in advisor),
     *         false otherwise
     */
    public String getOpaqueBaseType() {
        return opaqueBaseType;
    }

    public void setNoInternalConstructor(boolean noInternalConstructor) {
        this.noInternalConstructor = noInternalConstructor;
    }

    /**
     * Some classes like NSObject and CFType have their Internal constructors
     * defined in xmlvm-ios.h and need not be generated automatically since
     * these are special cases. This is specified via the advisor.
     * 
     * @return - true is object does not need Internal constructor; false
     *         otherwise.
     */
    public boolean noInternalConstructor() {
        return noInternalConstructor;
    }

    /**
     * In case a selector's actual arguments obtained by parsing the objective C
     * APIs have to be replaced with new set of arguments( Eg:
     * UIControl.addTarget) it needs to be specified via the advisor. The advise
     * should contain the entire set of arguments for the method in order.
     * 
     * @param selName
     *            - name of the selector
     * @return - returns true if selector has special arguments defined; false
     *         otherwise.
     */
    public boolean selectorHasArgumentsDefined(String selName) {
        return methodMap.get(selName) != null ? methodMap.get(selName).hasArgumentsDefined()
                : false;
    }

    /**
     * In case a property has specific type (EG: List<UIViewController>) such
     * information is provided via the advisor to have a strongly typed API.
     * 
     * @param property
     *            - name of the property
     * @return - returns the type of the property specified in the advisor
     */
    public String getPropertyType(String property) {
        return propMap.get(property) != null ? propMap.get(property).getType() : null;
    }

    /**
     * In case a selector has specific return type (EG: List<UIView>) such
     * information is provided via the advisor to have a strongly typed API.
     * 
     * @param selName
     *            - Name of the selector
     * @return - returns true if return type is specified via advisor; false
     *         otherwise.
     */
    public boolean selectorHasReturnTypeDefined(String selName) {
        return methodMap.get(selName) != null ? (methodMap.get(selName).getReturnType() != null ? true
                : false)
                : false;
    }
}