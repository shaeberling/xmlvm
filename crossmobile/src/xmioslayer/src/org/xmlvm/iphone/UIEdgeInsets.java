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

public class UIEdgeInsets extends NSObject {

    public float top, left, bottom, right;

    public UIEdgeInsets() {
        top = 0;
        left = 0;
        bottom = 0;
        right = 0;
    }

    public UIEdgeInsets(float top, float left, float bottom, float right) {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }
}
