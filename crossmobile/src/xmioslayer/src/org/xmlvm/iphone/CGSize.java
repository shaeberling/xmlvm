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

public class CGSize extends NSObject {

    public float width;
    public float height;

    public CGSize(CGSize size) {
        width = size.width;
        height = size.height;
    }

    public CGSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return "[" + width + "," + height + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CGSize))
            return false;

        CGSize size = (CGSize) obj;
        return size.width == this.width && size.height == this.height;
    }
}
