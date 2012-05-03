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
import java.util.List;

/**
 * In some cases, the code needs to injected via special advice, where the API
 * is external to Obj-C. Eg: toString() method in NSString class. This special
 * advice provided via advisor is abstracted in this class. It maintains the
 * signature of the method and the code itself, using which the code can be
 * injected.
 * 
 */
public class XInjectedMethod {

    private String      name;
    private String      value;
    private String      returnType;
    private List<XCode> code      = new ArrayList<XCode>();
    private List<XArg>  arguments = new ArrayList<XArg>();


    public void setName(String name) {
        this.name = name;
    }

    public void setModifier(String modifier) {
        this.value = modifier;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public void setArguments(List<XArg> args) {
        this.arguments = args;
    }

    public String getName() {
        return this.name;
    }

    public String getModifier() {
        return this.value;
    }

    public String getReturnType() {
        return this.returnType;
    }

    public List<XArg> getArguments() {
        return this.arguments;
    }

    public void addCode(XCode code) {
        this.code.add(code);
    }

    public List<XCode> getInjectedCode() {
        return this.code;
    }

}
