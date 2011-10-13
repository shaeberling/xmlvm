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

package org.crossmobile.ios2a.transf;

import android.graphics.drawable.Drawable;
import android.view.View;
import org.xmlvm.iphone.CGAffineTransform;
import org.xmlvm.iphone.CGRect;
import org.crossmobile.ios2a.IOSView;

public class StaticTranfs extends CoreTransf {

    @Override
    public void setFrame(IOSView view, CGRect frame) {
        view.setLayoutParams(new IOSView.LayoutParams(frame));
    }

    @Override
    public void setBackgroundColor(View view, Drawable back, int color) {
        if (back == null)
            view.setBackgroundColor(color);
        else
            view.setBackgroundDrawable(back);
    }

    @Override
    public void setAlpha(IOSView view, float alpha) {
        view.setAlpha(alpha);
    }

    @Override
    public void setTransform(IOSView view, CGAffineTransform transform) {
        view.setTransform(transform);
    }

    @Override
    public void commit() {
    }
}
