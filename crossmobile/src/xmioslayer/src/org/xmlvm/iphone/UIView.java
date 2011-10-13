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

import static org.xmlvm.iphone.UIViewContentMode.*;

import org.crossmobile.ios2a.transf.CoreTransf;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import org.crossmobile.ios2a.IOSView;
import java.util.ArrayList;
import java.util.List;
import org.crossmobile.ios2a.IOSChild;
import org.crossmobile.ios2a.ImplementationError;
import org.crossmobile.ios2a.UIRunner;

public class UIView extends UIResponder {

    // Animation variables
    static CoreTransf transf = CoreTransf.instant();
    //
    private UIColor background;
    private int tag;
    UIViewController controller;    // This variable is used in MainActivity to support hardware back button
    private int contentMode;
    private CALayer layer;

    public UIView() {
        this(CGRect.Zero());
    }

    @SuppressWarnings({"LeakingThisInConstructor", "OverridableMethodCallInConstructor"})
    public UIView(CGRect frame) {
        xm_base().setTag(this);
        setTag(0);
        setFrame(frame);
        setUserInteractionEnabled(true);
    }

    public CGRect getFrame() {
        try {
            return ((IOSView.LayoutParams) xm_base().getLayoutParams()).getFrame();
        } catch (Exception e) {
            LayoutParams lp = xm_base().getLayoutParams();
            return new CGRect(0, 0, IOSView.x2IOS(lp.width), IOSView.y2IOS(lp.height));
        }
    }

    public void setFrame(CGRect frame) {
        if (xm_model() != xm_base())
            xm_model().setLayoutParams(new IOSView.LayoutParams(frame.size));
        transf.setFrame(xm_base(), frame);
    }

    public CGRect getBounds() {
        CGRect bounds = getFrame();
        bounds.origin.x = 0;
        bounds.origin.y = 0;
        return bounds;
    }

    public void setBounds(CGRect rect) {
        setFrame(rect);
    }

    public UIColor getBackgroundColor() {
        return background;
    }

    public void setBackgroundColor(final UIColor col) {
        UIRunner.runSynced(new UIRunner() {

            @Override
            public void exec() {
                UIView.this.background = col;
                transf.setBackgroundColor(xm_base(), col == null ? null : col.getModelDrawable(), col == null ? 0 : col.getModelColor());
            }
        });
    }

    public CGAffineTransform getTransform() {
        return xm_base().getTransform();
    }

    public void setTransform(CGAffineTransform transform) {
        transf.setTransform(xm_base(), transform);
    }

    public float getAlpha() {
        return xm_base().getAlpha();
    }

    public void setAlpha(float alpha) {
        transf.setAlpha(xm_base(), alpha);
    }

    public void addSubview(UIView subView) {
        insertSubview(subView, Integer.MAX_VALUE);
    }

    public void insertSubview(UIView subView, final int idx) {
        if (subView != null)
            subView.changeParent(this, idx);
    }

    public void removeFromSuperview() {
        changeParent(null, 0);
    }

    void changeParent(final UIView newParent, final int index) {
        UIRunner.runSynced(new UIRunner() {

            @Override
            public void exec() {
                UIView oldParent = getSuperview();

                if (newParent == oldParent)
                    return;

                /* Inform delegates before any change */
                if (oldParent != null)
                    oldParent.willRemoveSubview(UIView.this);
                if (newParent != null)
                    newParent.willAddSubview(UIView.this);
                UIView.this.willMoveToSuperview(newParent);

                /* Perform actual move around */
                if (oldParent != null)
                    oldParent.getContainerLayer().removeView(getContentLayer());
                if (newParent != null)
                    if (index == Integer.MAX_VALUE)
                        newParent.getContainerLayer().addView(getContentLayer());
                    else
                        newParent.getContainerLayer().addView(getContentLayer(), index);

                /* Inform delegates after change */
                UIView.this.didMoveToSuperview();
                if (newParent != null)
                    newParent.didAddSubview(UIView.this);
                if (oldParent != null)
                    oldParent.didRemoveSubview(UIView.this);
            }
        });
    }

    void willAddSubview(UIView subview) {
    }

    void didRemoveSubview(UIView subview) {
    }

    public void didAddSubview(UIView subview) {
    }

    public void willRemoveSubview(UIView subview) {
    }

    public void willMoveToSuperview(UIView newSuperview) {
    }

    public void didMoveToSuperview() {
    }

    public void willMoveToWindow(UIWindow newWindow) {
    }

    public void didMoveToWindow() {
    }

    public void sendSubviewToBack(UIView subView) {
//        if (!subviews.contains(subView)) {
//            subviews.remove(subView);
//            subviews.add(0, subView);
//        }
        throw new ImplementationError();
    }

    public void bringSubviewToFront(final UIView subView) {
        if (subView.getSuperview() == this)
            UIRunner.runSynced(new UIRunner() {

                @Override
                public void exec() {
                    subView.getContentLayer().bringToFront();
                }
            });
    }

    public List<UIView> getSubviews() {
        ArrayList<UIView> children = new ArrayList<UIView>();
        for (int i = 0; i < getContainerLayer().getChildCount(); i++)
            try {
                children.add((UIView) ((IOSChild) getContainerLayer().getChildAt(i)).getIOSView().getTag());
            } catch (Exception e) {
            }
        return children;
    }

    public UIView getSuperview() {
        try {
            return (UIView) ((IOSView) getContentLayer().getParent()).getTag();
        } catch (Exception e) {
            return null;
        }
    }

    public void layoutSubviews() {
    }

    public UIWindow getWindow() {
        UIView parent = this;
        while ((parent = parent.getSuperview()) != null)
            if (parent instanceof UIWindow)
                return (UIWindow) parent;
        return null;
    }

    public final void setTag(int tag) {
        this.tag = tag;
    }

    public final int getTag() {
        return tag;
    }

    public CGPoint getCenter() {
        CGRect frame = getFrame();
        return new CGPoint((frame.origin.x + frame.size.width) / 2, (frame.origin.y + frame.size.width) / 2);
    }

    public void setCenter(CGPoint center) {
        CGRect frame = getFrame();
        CGPoint oldcenter = getCenter();
        oldcenter.x -= center.x;
        oldcenter.y -= center.y;
        setFrame(new CGRect(frame.origin.x - oldcenter.x, frame.origin.y - oldcenter.y, frame.size.width, frame.size.height));
    }

    public void setLocation(float x, float y) {
        CGRect frame = getFrame();
        setFrame(new CGRect(x, y, frame.size.width, frame.size.height));
    }

    public void setSize(float width, float height) {
        CGRect frame = getFrame();
        setFrame(new CGRect(frame.origin.x, frame.origin.y, width, height));
    }

    public void setNeedsDisplay() {
        UIRunner.runFree(new UIRunner() {

            @Override
            public void exec() {
                xm_base().requestLayout();
            }
        });
    }

    public void setOpaque(boolean opaque) {
        throw new ImplementationError();
    }

    public boolean isOpaque() {
        throw new ImplementationError();
    }

    public void setClearsContextBeforeDrawing(boolean clear) {
        throw new ImplementationError();
    }

    public boolean isHidden() {
        return xm_base().getVisibility() == View.INVISIBLE;
    }

    public void setHidden(final boolean hidden) {
        UIRunner.runSynced(new UIRunner() {

            @Override
            public void exec() {
                xm_base().setVisibility(hidden ? View.INVISIBLE : View.VISIBLE);
            }
        });
    }

    public void setContentMode(int UIViewContentMode) {
        contentMode = UIViewContentMode;
        Drawable d = xm_model().getBackground();
        if (!(d instanceof BitmapDrawable))
            return;
        BitmapDrawable bm = (BitmapDrawable) d;

        switch (contentMode) {
            case Top:
                bm.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                break;
            case TopRight:
                bm.setGravity(Gravity.TOP | Gravity.RIGHT);
                break;
            case Right:
                bm.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
                break;
            case BottomRight:
                bm.setGravity(Gravity.BOTTOM | Gravity.RIGHT);
                break;
            case Bottom:
                bm.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
                break;
            case BottomLeft:
                bm.setGravity(Gravity.BOTTOM | Gravity.LEFT);
                break;
            case Left:
                bm.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
                break;
            case TopLeft:
                bm.setGravity(Gravity.TOP | Gravity.LEFT);
                break;
            case Center:
                bm.setGravity(Gravity.CENTER);
                break;
            case ScaleAspectFit:
            // Not directly supported under Android?
            case ScaleAspectFill:
            // Not directly supported under Android?
            case ScaleToFill:
                bm.setGravity(Gravity.FILL);
                break;
            case Redraw:
                break;
        }
    }

    public int getContentMode() {
        return contentMode;
    }

    public boolean isUserInteractionEnabled() {
        return xm_model().isClickable();
    }

    public final void setUserInteractionEnabled(boolean userinteaction) {
        xm_model().setClickable(userinteaction);
    }

    public boolean clipsToBounds() {
        return true;
    }

    public void setClipsToBounds(boolean clipsToBounds) {
        throw new ImplementationError();
    }

    public CGPoint convertPointToView(CGPoint point, UIView view) {
        if (view == null)
            view = getWindow();

        return view.convertPointFromView(point, this);
    }

    public CGPoint convertPointFromView(CGPoint point, UIView view) {
        // TODO take care of transformations
        CGPoint p = new CGPoint(point);
        if (view == this)
            return p;
        if (view == null)
            view = getWindow();

        UIView parent;
        UIView lastfrom = this;
        while ((parent = lastfrom.getSuperview()) != null) {
            CGRect frame = lastfrom.getFrame();
            p.x -= frame.origin.x;
            p.y -= frame.origin.y;
            lastfrom = parent;
            if (parent == view)
                return p;
        }

        UIView lastto = view == null ? getWindow() : view;
        while ((parent = lastto.getSuperview()) != null) {
            CGRect frame = lastto.getFrame();
            p.x += frame.origin.x;
            p.y += frame.origin.y;
            lastto = parent;
            if (parent == lastfrom)
                return p;
        }
        // They don't have a common parent
        throw new RuntimeException("UIView do not have a common anchestor");
    }

    public CGRect convertRectToView(CGRect rect, UIView view) {
        throw new ImplementationError();
    }

    public CGRect convertRectFromView(CGRect rect, UIView view) {
        throw new ImplementationError();
    }

    public CALayer getLayer() {
        if (layer == null) {
            layer = new CALayer();
            layer.setDelegate(this);
        }
        return layer;
    }

    public int getAutoresizingMask() {
        throw new ImplementationError();
    }

    public void setAutoresizingMask(int UIViewAutoresizing) {
        throw new ImplementationError();
    }

    public boolean isAutoresizesSubviews() {
        throw new ImplementationError();
    }

    public void setAutoresizesSubviews(boolean autoresizesSubviews) {
        throw new ImplementationError();
    }

    /* View animations */
    public static void beginAnimations(String animationID) {
        transf = CoreTransf.create(animationID);
    }

    public static void commitAnimations() {
        // Late execution of commit, to make sure that animation delegates will bump against the instant transformation
        CoreTransf current = transf;
        transf = CoreTransf.instant();
        current.commit();
    }

    public static void setAnimationStartDate(NSDate startTime) {
    }

    public static void setAnimationsEnabled(boolean enabled) {
        CoreTransf.setAnimationsEnabled(enabled);
    }

    public static void setAnimationDuration(double duration) {
        transf.setDuration(duration);
    }

    public static void setAnimationDelay(double delay) {
    }

    public static void setAnimationCurve(int UIViewAnimationCurve) {
    }

    public static void setAnimationRepeatCount(float repeatCount) {
    }

    public static void setAnimationRepeatAutoreverses(boolean repeatAutoreverses) {
    }

    public static void setAnimationBeginsFromCurrentState(boolean fromCurrentState) {
    }

    public static void setAnimationTransitionForView(int UIViewAnimationTransition, UIView view, boolean cache) {
    }

    public static boolean areAnimationsEnabled() {
        return CoreTransf.areAnimationsEnabled();
    }

    public static void setAnimationDelegate(UIViewAnimationDelegate delegate) {
        transf.setAnimationDelegate(delegate);
    }

    public CGSize sizeThatFits(CGSize size) {
        throw new ImplementationError();
    }

    public void sizeToFit() {
        throw new ImplementationError();
    }

    @Override
    public String toString() {
        String classname = getClass().getName();
        int point = classname.lastIndexOf('.');
        if (point >= 0)
            classname = classname.substring(point + 1);
        return "[" + classname + " " + getFrame().toString() + " tag=" + tag + "]";
    }

    public void drawRect(CGRect rect) {
        // Do nothing
    }

    public UIView hitTest(CGPoint point, UIEvent event) {
        if (isUserInteractionEnabled() && !isHidden() && getAlpha() >= 0.1f) {
            // do transformations here
            CGRect frame = getFrame();
            float x = point.x - frame.origin.x;
            float y = point.y - frame.origin.y;

            point = new CGPoint(x, y);

            // Highest layer & highest Z-order
            List<UIView> children = getSubviews();
            for (int i = children.size() - 1; i >= 0; i--) {
                UIView found = children.get(i).hitTest(point, event);
                if (found != null)
                    return found;
            }
            if (pointInside(point, event))
                return this;
        }
        return null;
    }

    public boolean pointInside(CGPoint point, UIEvent event) {
        CGRect frame = getFrame();
        return !(point.x < 0 || point.y < 0 || point.x > frame.size.width - 1 || point.y > frame.size.height - 1);
    }
}
