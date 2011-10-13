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

import org.crossmobile.ios2a.OptionalSelectorException;

public abstract class UIPickerViewDelegate extends NSObject {

    /**
     * This is an optional method.
     * 
     * @see org.xmlvm.iphone.internal.OptionalSelectorException
     */
    public float rowHeightForComponent(UIPickerView view, int component) {
        throw new OptionalSelectorException();
    }

    /**
     * This is an optional method.
     * 
     * @see org.xmlvm.iphone.internal.OptionalSelectorException
     */
    public float widthForComponent(UIPickerView view, int component) {
        throw new OptionalSelectorException();
    }

    public UIView viewForRow(UIPickerView view, int row, int component, UIView reusedView) {
        return null;
    }

    public String titleForRow(UIPickerView view, int row, int component) {
        return null;
    }

    public void didSelectRow(UIPickerView view, int row, int component) {
    }
}
