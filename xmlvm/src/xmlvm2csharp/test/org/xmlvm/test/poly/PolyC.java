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
package org.xmlvm.test.poly;

/*
 * tests inheritence of a field from both an interface and a parent
 */
public class PolyC {
    public static void main(String[] args) {
        PolyCParent a = new PolyCParent();
        PolyCParent b = new PolyCChild();
        PolyCChild c = new PolyCChild();
        PolyCInterface d = new PolyCChild();
        System.out.println(a.str);
        System.out.println(b.str);
        System.out.println(c.str);
        System.out.println(d.str);
    }
}

interface PolyCInterface {
    public String str = "interface's string";
}

class PolyCChild extends PolyCParent implements PolyCInterface {
    public String str = "child's string";
}

class PolyCParent {
    public String str = "parent's string";
}
