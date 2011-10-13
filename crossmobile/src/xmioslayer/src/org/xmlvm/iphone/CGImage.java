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

import org.crossmobile.ios2a.ImplementationError;
import android.graphics.Bitmap;

public class CGImage extends NSObject {

    private Bitmap image;

    CGImage(Bitmap image) {
        this.image = image;
    }

    public static CGImage createWithImageInRect(CGImage image, CGRect rect) {
        throw new ImplementationError();
    }

    public static CGImage createPNGFromDataProvider(CGDataProvider provider) {
        throw new ImplementationError();
    }

    public CGSize getSize() {
        return new CGSize(image.getWidth(), image.getHeight());
    }
}
