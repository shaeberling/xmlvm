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

import java.util.Set;

import org.xmlvm.iphone.CGPoint;
import org.xmlvm.iphone.UIEvent;
import org.xmlvm.iphone.UIResponderDelegate;
import org.xmlvm.iphone.UITouch;
import org.xmlvm.iphone.UIView;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.internal.Assert;
import android.util.AttributeSet;

/**
 * iPhone implementation of Android's View class.
 * 
 * @see http://developer.android.com/reference/android/view/View.html
 */
public class View {

    public static final int VISIBLE = 0;
    public static final int INVISIBLE = 4;
    public static final int GONE    = 8;

    public static interface OnTouchListener {
        /**
         * Called when a touch event is dispatched to a view. This allows
         * listeners to get a chance to respond before the target view.
         * 
         * @param v
         *            The view the touch event has been dispatched to.
         * @param event
         *            he MotionEvent object containing full information about
         *            the event.
         * 
         * @return True if the listener has consumed the event, false otherwise.
         */
        public boolean onTouch(View v, MotionEvent event);
    }

    public static interface OnClickListener {
        public void onClick(View view);
    }

    private ViewGroup.LayoutParams layoutParams;
    private Context                c;
    private UIView                 uiView;
    private ViewParent             parent;
    private OnTouchListener        listener;
    private UIResponderDelegate    responderDelegate;
    private int                    id;

    // Temporarily used
    private static int             nextY;

    protected int getNextY() {
        int y = nextY;
        nextY += 40;
        return y;
    }

    public View(Context c) {
        this.c = c;
        uiView = xmlvmCreateUIView();

        responderDelegate = new UIResponderDelegate() {

            @Override
            public void touchesBegan(Set<UITouch> touches, UIEvent event) {
                processTouchesEvent(MotionEvent.ACTION_DOWN, touches, event);
            }

            @Override
            public void touchesMoved(Set<UITouch> touches, UIEvent event) {
                processTouchesEvent(MotionEvent.ACTION_MOVE, touches, event);
            }

            @Override
            public void touchesCancelled(Set<UITouch> touches, UIEvent event) {
                processTouchesEvent(MotionEvent.ACTION_CANCEL, touches, event);
            }

            @Override
            public void touchesEnded(Set<UITouch> touches, UIEvent event) {
                processTouchesEvent(MotionEvent.ACTION_UP, touches, event);
            }

        };

        uiView.setDelegate(responderDelegate);

    }

    public View(Context c, AttributeSet attrs) {
        this(c);
        parseAttributes(attrs);
    }

    public ViewGroup.LayoutParams getLayoutParams() {
        return layoutParams;
    }

    public void invalidate() {
        uiView.setNeedsDisplay();
    }

    public void setLayoutParams(ViewGroup.LayoutParams l) {
        layoutParams = l;
    }

    public final Context getContext() {
        return c;
    }

    public void bringToFront() {
        Assert.NOT_IMPLEMENTED();
    }

    public ViewParent getParent() {
        return parent;
    }

    public void setOnTouchListener(OnTouchListener listener) {
        this.listener = listener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        Assert.NOT_IMPLEMENTED();
    }

    private boolean processTouchesEvent(int action, Set<UITouch> touches, UIEvent event) {
        if (this.listener == null) {
            return false;
        }
        UITouch firstTouch = touches.iterator().next();
        CGPoint point = firstTouch.locationInView(uiView);
        MotionEvent motionEvent = new MotionEvent(action, (int) point.x, (int) point.y);
        return this.listener.onTouch(this, motionEvent);
    }

    protected UIView xmlvmCreateUIView() {
        return new UIView();
    }

    public UIView xmlvmGetUIView() {
        return uiView;
    }

    public void xmlvmSetParent(ViewParent parent) {
        this.parent = parent;
    }

    protected void finalize() {
        uiView.setDelegate(null);
    }

    protected void parseAttributes(AttributeSet attrs) {
        id = attrs.getIdAttributeResourceValue(0);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public View findViewById(int id) {
        Assert.NOT_IMPLEMENTED();
        return null;
    }

    public Resources getResources() {
        Assert.NOT_IMPLEMENTED();
        return null;
    }

    public void setBackgroundDrawable(Drawable drawable) {
        Assert.NOT_IMPLEMENTED();
    }

    public void setVisibility(int flag) {
        Assert.NOT_IMPLEMENTED();
    }

    public boolean postDelayed(Runnable runnable, long delay) {
        Assert.NOT_IMPLEMENTED();
        return true;
    }

}
