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
import org.xmlvm.iphone.UIColor;
import org.xmlvm.iphone.UIScreen;
import org.xmlvm.iphone.UIWindow;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.internal.LayoutManager;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;

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
        rect = screen.getApplicationFrame();
        iWindow = new UIWindow(rect);
        iWindow.setBackgroundColor(UIColor.colorWithRGBA(0.0941f, 0.0941f, 0.0941f, 1.0f));
    }

    public void setContentView(View view) {
        if (contentView != null) {
            xmlvmSetHidden(true);
            contentView.xmlvmGetUIView().removeFromSuperview();
            contentView.xmlvmSetParent(null);
        }
        contentView = view;
        if (contentView.getLayoutParams() == null) {
            contentView.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.FILL_PARENT));
        }

        adjustFrameSize();
        CGRect viewRect = new CGRect(rect);
        viewRect.origin.x = viewRect.origin.y = 0;
        view.xmlvmGetUIView().setFrame(viewRect);
        iWindow.addSubview(view.xmlvmGetUIView());
    }

    public void setContentView(int id) {
        View v = LayoutManager.getLayout(activity, id, null);
        setContentView(v);
    }

    public void setFlags(int flags, int mask) {
        int maskedFlags = (flags & mask);
        if ((maskedFlags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0) {
            UIApplication.sharedApplication().setStatusBarHidden(true);
            adjustFrameSize();
        }
    }

    public void xmlvmSetHidden(boolean flag) {
        if (flag) {
            iWindow.setHidden(true);
        } else {
            iWindow.makeKeyAndVisible();
            iWindow.setHidden(false);
        }
    }

    public void xmlvmRemoveWindow() {
        xmlvmSetHidden(true);
        if (contentView != null) {
            contentView.xmlvmGetUIView().removeFromSuperview();
            contentView.xmlvmSetParent(null);
        }
        iWindow = null;
    }

    /**
     * Internal. Not part of Android API. Called whenever the size or
     * orientation of the top-level window has changed (e.g., when the status
     * bar is made invisible).
     */
    public void adjustFrameSize() {
        UIScreen screen = UIScreen.mainScreen();
        rect = screen.getApplicationFrame();
        iWindow.setTransform(null);
        iWindow.setFrame(rect);
        if (activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            float t = rect.size.height;
            rect.size.height = rect.size.width;
            rect.size.width = t;
            iWindow.setFrame(rect);
            CGAffineTransform rotation = CGAffineTransform
                    .makeRotation((float) ((Math.PI / 180) * 90));
            // TODO Translate should be 90, 90 for visible status bar (i.e.,
            // non-fullscreen)
            CGAffineTransform translation = CGAffineTransform.translate(rotation, 80, 80);
            iWindow.setTransform(translation);
        }

        if (contentView != null) {
            layoutContentView();
        }
    }

    private void layoutContentView() {
        int widthMeasureSpec;
        int heightMeasureSpec;
        LayoutParams lp = contentView.getLayoutParams();

        if (lp == null || lp.width == LayoutParams.FILL_PARENT) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) rect.size.width,
                    MeasureSpec.EXACTLY);
        } else if (lp.width == LayoutParams.WRAP_CONTENT) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) rect.size.width,
                    MeasureSpec.AT_MOST);
        } else {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(lp.width, MeasureSpec.EXACTLY);
        }

        if (lp == null || lp.height == LayoutParams.FILL_PARENT) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) rect.size.height,
                    MeasureSpec.EXACTLY);
        } else if (lp.height == LayoutParams.WRAP_CONTENT) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) rect.size.height,
                    MeasureSpec.AT_MOST);
        } else {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY);
        }

        contentView.xmlvmSetMeasureSpec(widthMeasureSpec, heightMeasureSpec);
        contentView.requestLayout();
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
        return contentView.findViewById(id);
    }
}
