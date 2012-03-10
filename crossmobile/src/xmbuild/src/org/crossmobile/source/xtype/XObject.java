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

    public final static int        RETAIN                    = 0;
    public final static int        RELEASE                   = 1;
    public static final int        REPLACE                   = 2;

    private Map<String, XMethod>   methodMap                 = null;
    private Map<String, XProperty> propMap                   = null;


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
}