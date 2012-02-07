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

/**
 * This class abstracts the advice at argument level( i.e, the advice for 'arg'
 * tag from the advisor). It maintains the position of the argument, the type of
 * the argument and the properties associated with it for keeping C-reference to
 * tell GC about the associations.
 * 
 */
public class XArg {

    private boolean isRetain  = false;
    private boolean isRelease = false;
    private boolean isReplace = false;
    private int     position;
    private String  type      = null;


    public XArg(int position, String type, int flags) {
        this.position = position;
        this.type = type;
        setFlags(flags);
    }

    /**
     * Sets flag for a particular argument based on the advice- if an argument
     * is accumulative or if it has to be replaced or released in a method call.
     * 
     * @param flags
     */
    private final void setFlags(int flags) {
        if (flags == XObject.RETAIN)
            isRetain = true;
        else if (flags == XObject.RELEASE)
            isRelease = true;
        else if (flags == XObject.REPLACE)
            isReplace = true;
    }

    public boolean isRetain() {
        return isRetain;
    }

    public boolean isRelease() {
        return isRelease;
    }

    public int getPosition() {
        return position;
    }

    public String getType() {
        return type;
    }

    public boolean isReplace() {
        return isReplace;
    }

}
