/*
 * Copyright (c) 2004-2009 XMLVM --- An XML-based Programming Language
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 675 Mass
 * Ave, Cambridge, MA 02139, USA.
 * 
 * For more information, visit the XMLVM Home Page at http://www.xmlvm.org
 */

package android.view;

import org.xmlvm.iphone.CGAffineTransform;
import org.xmlvm.iphone.CGRect;
import org.xmlvm.iphone.UIApplication;
import org.xmlvm.iphone.UIScreen;
import org.xmlvm.iphone.UIWindow;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.internal.LayoutManager;

/**
 * iPhone Implementation of Android's Window class.
 * 
 * @see http://developer.android.com/reference/android/view/Window.html
 */
public class Window {
    public static final int FEATURE_NO_TITLE = 1;
    private Activity        activity;
    private UIWindow        iWindow;
    private CGRect          rect;
    private View            contentView;

    public Window(Activity parent) {
        this.activity = parent;
        UIScreen screen = UIScreen.mainScreen();
        rect = screen.applicationFrame();
        iWindow = new UIWindow(rect);
    }

    public void setContentView(View view) {
        contentView = view;
        adjustFrameSize();
        CGRect viewRect = new CGRect(rect);
        viewRect.origin.x = viewRect.origin.y = 0;
        view.xmlvmGetUIView().setFrame(viewRect);
        iWindow.addSubview(view.xmlvmGetUIView());
        iWindow.makeKeyAndVisible();
    }

    public void setContentView(int id) {
        ViewGroup vg = LayoutManager.getLayout(activity, id);
        setContentView(vg);
    }

    public void setFlags(int flags, int mask) {
        int maskedFlags = (flags & mask);
        if ((maskedFlags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0) {
            UIApplication.sharedApplication().setStatusBarHidden(true);
            adjustFrameSize();
        }
    }

    /**
     * Internal. Not part of Android API. Called whenever the size or
     * orientation of the top-level window has changed (e.g., when the status
     * bar is made invisible).
     */
    public void adjustFrameSize() {
        UIScreen screen = UIScreen.mainScreen();
        rect = screen.applicationFrame();
        iWindow.setTransform(null);
        iWindow.setFrame(rect);
        if (activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            float t = rect.size.height;
            rect.size.height = rect.size.width;
            rect.size.width = t;
            iWindow.setFrame(rect);
            CGAffineTransform trans = CGAffineTransform
                    .makeRotation((float) ((Math.PI / 180) * 90));
            // TODO Translate should be 90, 90 for visible status bar (i.e.,
            // non-fullscreen)
            trans.translate(80, 80);
            iWindow.setTransform(trans);
        }
    }

    /**
     * Internal. Not part of Android API.
     */
    public CGRect getCGRect() {
        return rect;
    }

    /**
     * @param id
     * @return
     */
    public View findViewById(int id) {
        if (contentView instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) contentView;
            return vg.getXmlvmViewMap().get(new Integer(id));
        } else {
            return null;
        }
    }
}
