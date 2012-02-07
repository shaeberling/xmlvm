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
 * This class abstracts the data of the 'property' tag from the advisor. It
 * contains the external advice for a property of a particular class - if the
 * property is accumulative or replaceable (while keeping a C-reference for GC);
 * the data type of the property and such.
 * 
 */
public class XProperty {

    private String  name      = null;
    private String  type      = null;
    private boolean isReplace = false;
    private boolean isRetain  = false;


    public XProperty(String name, String type, int flag) {
        this.name = name;
        this.type = type;
        setFlags(flag);
    }

    /**
     * Sets the flags if the property has to be retained or replaced
     * 
     * @param flags
     */
    private final void setFlags(int flags) {
        if (flags == XObject.RETAIN)
            isRetain = true;
        else if (flags == XObject.REPLACE)
            isReplace = true;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isRetain() {
        return isRetain;
    }

    public boolean isReplace() {
        return isReplace;
    }

}
