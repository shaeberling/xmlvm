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

import java.util.List;

/**
 * This class abstracts the advice from the 'method' tag of the advisor. It
 * maintains a list of arguments that need special advice. It also maintains
 * properties that are required at method level during the c-wrapper generation
 * process.
 * 
 */
public class XMethod {

    private String          selectorName           = null;
    private List<XArg>      argumentList;
    private boolean         requireAutoReleasePool = false;
    private XInjectedMethod injectedCode           = null;
    private boolean         hasInjectedCode        = false;
    private boolean         isMandatory            = false;
    private String          returnType           = null;

    private String          defaultRetunValue      = null;
    private boolean         isDelegate             = false;


    public XMethod(String selectorName, List<XArg> argList, String requireAutoReleasePool,
            boolean isMandatory, String defaultRetunValue) {
        this.selectorName = selectorName;
        argumentList = argList;
        this.isMandatory = isMandatory;
        this.defaultRetunValue = defaultRetunValue;
        if (requireAutoReleasePool != null)
            this.requireAutoReleasePool = true;
    }

    public List<XArg> getArgList() {
        return argumentList;
    }

    public String getSelectorName() {
        return selectorName;
    }

    public boolean isAutoReleasePoolRequired() {
        return requireAutoReleasePool;
    }

    public void setInjectedCode(XInjectedMethod injCode) {
        this.injectedCode = injCode;
        hasInjectedCode = true;
    }

    public XInjectedMethod getInjectedCode() {
        return this.injectedCode;
    }

    public boolean hasInjectedCode() {
        return hasInjectedCode;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public void setMandatory(boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public String getDefaultRetunValue() {
        return defaultRetunValue;
    }

    public void setReturnValue(String defaultRetunValue) {
        this.defaultRetunValue = defaultRetunValue;
    }

    public void setDelegate(boolean isDelegate) {
        this.isDelegate = isDelegate;
    }

    public boolean isDelegate() {
        return this.isDelegate;
    }

    public boolean hasArgumentsDefined() {
        return argumentList.isEmpty() ? false : true;
    }

    public String getReturnType() {
        return this.returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }
}
