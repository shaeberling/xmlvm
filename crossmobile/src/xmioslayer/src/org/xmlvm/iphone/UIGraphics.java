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

import java.util.Stack;

public final class UIGraphics {

    private static Stack<CGContext> contextStack = new Stack<CGContext>();

    private UIGraphics() {
    }

    public static CGContext getCurrentContext() {
        return contextStack.peek();
    }

    public static void pushContext(CGContext context) {
        contextStack.push(context);
    }

    public static void popContext() {
        contextStack.pop().recycle();
    }

    public static void beginImageContext(CGSize size) {
        beginImageContextWithOptions(size, true, 1);
    }

    public static void beginImageContextWithOptions(CGSize size, boolean opaque, float scale) {
        contextStack.push(new CGContext(new CGSize(size.width * scale, size.height * scale)));
    }

    public static UIImage getImageFromCurrentImageContext() {
        return getCurrentContext().getImage();
    }

    public static void endImageContext() {
        popContext();
    }
}
