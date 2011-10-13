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

import android.graphics.Bitmap;
import android.graphics.Canvas;
import org.crossmobile.ios2a.IOSView;

public final class CGContext extends NSObject {

    private final CGSize size;
    private Canvas canvas;
    private Bitmap bitmap;

    CGContext(CGSize size) {
        this.size = new CGSize(size);
    }

    Canvas getCanvas() {
        if (canvas == null) {
            bitmap = Bitmap.createBitmap((int) (IOSView.x2Android(size.width) + 0.5f), (int) (IOSView.y2Android(size.height) + 0.5f), Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
        }
        return canvas;
    }

    UIImage getImage() {
        return UIImage.imageFromBitmap(bitmap, true); // work only with a copy of this image
    }

    void recycle() {
        bitmap.recycle();
        bitmap = null;
        canvas = null;
    }
}
