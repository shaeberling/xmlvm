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

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import org.crossmobile.ios2a.UIRunner;

public class UIImageView extends UIView {

    public UIImageView() {
        this(CGRect.Zero());
    }

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public UIImageView(CGRect rect) {
        super(rect);
        setContentMode(UIViewContentMode.Center);
        setUserInteractionEnabled(false);
    }

    public void setImage(final UIImage image) {
        UIRunner.runSynced(new UIRunner() {

            @Override
            public void exec() {
                if (image == null)
                    xm_model().setBackgroundDrawable(null);
                else {
                    xm_model().setBackgroundDrawable(image.getModel());
                    setContentMode(getContentMode());
                }
            }
        });
    }

    public UIImage getImage() {
        Drawable d = xm_model().getBackground();
        if (d instanceof BitmapDrawable)
            return UIImage.imageFromBitmap(((BitmapDrawable) d).getBitmap(), false);
        return null;
    }
}
