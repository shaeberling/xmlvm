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

package org.xmlvm.iphone;

public final class UIKeyboardType {

    public static final int Default = 0;
    public static final int ASCIICapable = 1;
    public static final int NumbersAndPunctuation = 2;
    public static final int URL = 3;
    public static final int NumberPad = 4;
    public static final int PhonePad = 5;
    public static final int NamePhonePad = 6;
    public static final int EmailAddress = 7;
    public static final int Alphabet = ASCIICapable;

    private UIKeyboardType() {
    }
}
