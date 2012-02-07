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

    private String                 className          = null;
    private boolean                retain             = false;
    private boolean                replace            = false;
    private List<String>           aliasList          = null;
    private boolean                hasInjectedCode    = false;
    private List<XInjectedMethod>  injectedMethodList = null;
    private List<XProperty>        propertyList       = null;

    public final static int        RETAIN             = 0;
    public final static int        RELEASE            = 1;
    public static final int        REPLACE            = 2;

    private Map<String, XMethod>   methodMap          = null;
    private Map<String, XProperty> propMap            = null;


    public XObject(String className, List<XMethod> methodList, List<XProperty> propList,
            List<String> aliases, List<XInjectedMethod> injectedMethodList) {
        this.className = className;
        this.aliasList = aliases;
        this.injectedMethodList = injectedMethodList;
        this.propertyList = propList;
        if (injectedMethodList != null)
            hasInjectedCode = true;
        createMethodMap(methodList);
        createPropertyMap(propList);
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

    public List<XInjectedMethod> getInjectedMethods() {
        return this.injectedMethodList;
    }

    public List<String> getAliasList() {
        return aliasList;
    }

    public boolean hasInjectedCode() {
        return hasInjectedCode;
    }

    public XMethod getMethodInstance(String selectorName) {
        return methodMap.get(selectorName);
    }

    public XProperty getPropertyInstance(String propName) {
        return propMap.get(propName);
    }
}