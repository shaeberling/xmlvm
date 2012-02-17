/* Copyright (c) 2011 by crossmobile.org
 *
 * CrossMobile is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 2.
 *
 * CrossMobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CrossMobile; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.crossmobile.source.ctype;

import java.util.List;

public class CMethod extends CSelector {
    private static final long serialVersionUID = 1L;

    private final CType returnType;
    private final boolean isStatic;
    private String canonicalName;
    private boolean isProperty = false;

    public CMethod(String name, boolean isAbstract, boolean derivesFromObjC, List<CArgument> arguments, List<String> nameparts, boolean isStatic, CType returnType) {
        super(name, isAbstract, derivesFromObjC, arguments, nameparts);
        this.isStatic = isStatic;
        this.returnType = returnType;
        this.canonicalName = name;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setCanonicalName(String cname) {
        this.canonicalName = cname;
    }

    public CType getReturnType() {
        return returnType;
    }

    public String getCanonicalName() {
        return canonicalName;
    }

    public boolean isProperty() {
        return isProperty;
    }

    public void setProperty() {
        isProperty = true;
    }

    @Override
    public String toString() {
        return name;
    }
}
