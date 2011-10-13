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

import java.util.ArrayList;
import org.crossmobile.ios2a.GraphicsUtils;

public class UINavigationBar extends UIView {

    private ArrayList<UINavigationItem> items;
    private UINavigationBarDelegate delegate;
    UINavigationController nbcontroller;
    private boolean translucent;
    private int barStyle;
    private UIColor tintColor = UIColor.colorWithRGBA(0.4f, 0.5f, 0.7f, 1);

    public UINavigationBar() {
        this(CGRect.Zero());
    }

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public UINavigationBar(CGRect rect) {
        super(rect);
        items = new ArrayList<UINavigationItem>();
        translucent = false;
        setTintColor(tintColor);
    }

    public void pushNavigationItem(UINavigationItem item, boolean animated) {
        if (delegate != null)
            if (!delegate.shouldPushItem(this, item))
                return;
        items.add(item);
        item.setToolbar(this);
        updateViews();
        if (delegate != null)
            delegate.didPushItem(this, item);
    }

    public UINavigationItem popNavigationItemAnimated(boolean animated) {
        if (delegate != null)
            if (!delegate.shouldPopItem(this, null))
                return null;
        UINavigationItem pop = items.get(items.size() - 1);
        items.remove(items.size() - 1);
        pop.setToolbar(this);
        updateViews();
        if (delegate != null)
            delegate.didPopItem(this, pop);
        return pop;
    }

    public void setItems(ArrayList<UINavigationItem> items) {
        setItems(items, false);
    }

    public void setItems(ArrayList<UINavigationItem> items, boolean animated) {
        this.items = items;
        for (UINavigationItem item : items)
            item.setToolbar(this);
        updateViews();
    }

    public UINavigationBarDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(UINavigationBarDelegate delegate) {
        this.delegate = delegate;
    }

    public UINavigationItem getTopItem() {
        if (items == null || items.isEmpty())
            return null;
        return items.get(items.size() - 1);
    }

    public UINavigationItem getBackitem() {
        return items.get(items.size() - 2);
    }

    public ArrayList<UINavigationItem> getItems() {
        return items;
    }

    public int getBarStyle() {
        return barStyle;
    }

    public void setBarStyle(int UIBarStyle) {
        this.barStyle = UIBarStyle;
        updateViews();
    }

    public UIColor getTintColor() {
        return tintColor;
    }

    public void setTintColor(UIColor tintColor) {
        int color = tintColor.getModelColor() | 0xFF000000;
        this.tintColor = new UIColor(color);
        if (translucent)
            color &= 0xE0FFFFFF;
        xm_base().setBackgroundDrawable(new GraphicsUtils.TintedDrawable(color));
    }

    public boolean isTranslucent() {
        return translucent;
    }

    public void setTranslucent(boolean translucent) {
        this.translucent = translucent;
        setTintColor(tintColor);
        if (nbcontroller != null)
            nbcontroller.doLayout(false);
    }

    void updateViews() {
        UINavigationItem item = getTopItem();
        if (item == null)
            return;

        /* First clear lists */
        for (UIView old : getSubviews())
            old.removeFromSuperview();

        /* Find metrics */
        CGRect size = getFrame();
        CGRect r;

        UILabel prompt = item.getPromptView();
        if (prompt != null) {
            r = prompt.getFrame();
            r.origin.x = 20;
            r.origin.y = 0;
            r.size.width = UIScreen.mainScreen().getApplicationFrame().size.width - 120;
            r.size.height = 20;
            prompt.setFrame(getFrame());
            addSubview(prompt);
        }

        UIView label = item.getTitleView();
        if (label != null) {
            r = label.getFrame();
            r.origin.x = 0;
            r.origin.y = 0;
            r.size.width = UIScreen.mainScreen().getApplicationFrame().size.width;
            r.size.height = 30;
            label.setFrame(r);
            addSubview(label);
        }

        /* Create right bar */
//        UIBarButtonItem right = item.getRightBarButtonItem();
//        if (right != null) {
//            right.getCustomView().setFrame(new CGRect(w - right.getWidth() - offset_from_edge, deltaY, right.getWidth(), h - deltaY - BOTTOM_OFFSET));
//            addSubview(right.getCustomView());
//            right_width = right.getWidth();
//        }
//
//        /* Create left bar */
//        if (items.size() > 1) {
//            UIBarButtonItem left = item.getLeftBarButtonItem() == null ? item.getBackBarButtonItem() : item.getLeftBarButtonItem();
//            left.getCustomView().setFrame(new CGRect(offset_from_edge, deltaY, left.getWidth(), h - deltaY - BOTTOM_OFFSET));
//            addSubview(left.getCustomView());
//            left_width = left.getWidth();
//        }
//
//        /* Create title */
//        UIView v = item.getTitleView();
//        float nw = OFFSET * 2 + offset_from_edge * 2 + left_width + right_width;
//        v.setFrame(new CGRect(offset_from_edge + left_width + OFFSET, deltaY, w - (nw), h - deltaY - BOTTOM_OFFSET));
//        addSubview(v);

        /* Redisplay */
        setNeedsDisplay();

    }
}
