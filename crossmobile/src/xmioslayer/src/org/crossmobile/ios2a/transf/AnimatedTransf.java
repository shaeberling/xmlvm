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
import android.view.animation.Animation;
import java.util.HashMap;
import org.xmlvm.iphone.CGAffineTransform;
import org.xmlvm.iphone.CGRect;
import org.xmlvm.iphone.UIViewAnimationDelegate;
import org.crossmobile.ios2a.IOSView;

public class AnimatedTransf extends StaticTranfs implements Animation.AnimationListener {

    private String ID;
    private HashMap<IOSView, IOSAnimation> map = new HashMap<IOSView, IOSAnimation>();
    private long duration = 300;
    private UIViewAnimationDelegate delegate;

    AnimatedTransf(String ID) {
        this.ID = ID;
    }

    @Override
    public void setFrame(IOSView view, CGRect frame) {
        if (!areAnimationsEnabled()) {
            super.setFrame(view, frame);
            return;
        }
        super.setFrame(view, frame);
    }

    @Override
    public void setBackgroundColor(View view, Drawable back, int color) {
        if (!areAnimationsEnabled()) {
            super.setBackgroundColor(view, back, color);
            return;
        }
        super.setBackgroundColor(view, back, color);
    }

    @Override
    public void setAlpha(IOSView view, float to) {
        if (!areAnimationsEnabled()) {
            super.setAlpha(view, to);
            return;
        }
        getParameters(view).setAlpha(view.getAlpha(), to);
    }

    @Override
    public void setTransform(IOSView view, CGAffineTransform transform) {
        if (!areAnimationsEnabled()) {
            super.setTransform(view, transform);
            return;
        }
        getParameters(view).setTransform(view.getTransform(), transform);
    }

    @Override
    public void setDuration(double d) {
        this.duration = (long) (d * 1000);
    }

    @Override
    public void commit() {
        IOSAnimation anim = null;
        for (IOSView view : map.keySet()) {
            anim = map.get(view);
            view.setTransformationParameters(anim);
            anim.setDuration(duration);
            view.startAnimation(anim);
        }
        if (anim != null // There is indeed an animation
                && delegate != null)    // and there is a delegate
            anim.setAnimationListener(this);
    }

    @Override
    public void setAnimationDelegate(UIViewAnimationDelegate delegate) {
        this.delegate = delegate;
    }

    private IOSAnimation getParameters(IOSView view) {
        IOSAnimation anim = map.get(view);
        if (anim != null)
            return anim;
        anim = new IOSAnimation();
        map.put(view, anim);
        return anim;
    }

    public void onAnimationStart(Animation anmtn) {
        if (delegate != null)
            delegate.animationWillStart(ID);
    }

    public void onAnimationEnd(Animation anmtn) {
        if (delegate != null)
            delegate.animationDidStop(ID, anmtn.hasEnded());
    }

    public void onAnimationRepeat(Animation anmtn) {
    }
}
