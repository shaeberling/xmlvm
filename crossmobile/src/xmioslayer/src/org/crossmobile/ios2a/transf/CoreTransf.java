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
import org.xmlvm.iphone.UIViewAnimationDelegate;
import org.crossmobile.ios2a.IOSView;

public abstract class CoreTransf {

    private static boolean animationsEnabled = true;
    private static final CoreTransf instant = new StaticTranfs();

    public static boolean areAnimationsEnabled() {
        return animationsEnabled;
    }

    public static void setAnimationsEnabled(boolean enabled) {
        animationsEnabled = enabled;
    }

    public static CoreTransf instant() {
        return instant;
    }

    public static CoreTransf create(String ID) {
        return new AnimatedTransf(ID);
    }

    public abstract void setFrame(IOSView view, CGRect frame);

    public abstract void setBackgroundColor(View view, Drawable back, int color);

    public abstract void setAlpha(IOSView base, float to);

    public abstract void setTransform(IOSView base, CGAffineTransform transform);

    public void setDuration(double d) {
    }

    public abstract void commit();

    public void setAnimationDelegate(UIViewAnimationDelegate delegate) {
    }
}
