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

public class UITextFieldDelegate extends NSObject {

    public boolean textFieldShouldBeginEditing(UITextField textField) {
        return true;
    }

    public void textFieldDidBeginEditing(UITextField textField) {
    }

    public boolean textFieldShouldEndEditing(UITextField textField) {
        return true;
    }

    public void textFieldDidEndEditing(UITextField textField) {
    }

    public boolean textFieldShouldChangeCharactersInRange(UITextField textField, NSRange range, String replacementString) {
        return true;
    }

    public boolean textFieldShouldClear(UITextField textField) {
        return true;
    }

    public boolean textFieldShouldReturn(UITextField textField) {
        return true;
    }
}
