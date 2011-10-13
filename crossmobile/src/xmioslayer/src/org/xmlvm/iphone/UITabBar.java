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

public class UITabBar extends UIView {

    private UITabBarDelegate delegate;
    private ArrayList<UITabBarItem> items;
    private UITabBarItem selectedItem;
    /* For connectivity with UITabBarController */
    UITabBarController tbcontrol;

    public UITabBar() {
        this(CGRect.Zero());
    }

    public UITabBar(CGRect rect) {
        super(rect);
        setBackgroundColor(UIColor.purpleColor);
    }

    public UITabBarDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(UITabBarDelegate delegate) {
        this.delegate = delegate;
    }

    public ArrayList<UITabBarItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<UITabBarItem> items) {
        setItems(items, false);
    }

    public void setItems(ArrayList<UITabBarItem> items, boolean animated) {
        this.items = items;
        doLayout();
        setSelectedItem(items.get(0));
    }

    public UITabBarItem getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(UITabBarItem selectedItem) {
        if (selectedItem != this.selectedItem) {
            int which = items.indexOf(selectedItem);
            if (which < 0)
                return;
            this.selectedItem = selectedItem; // First this
            if (tbcontrol != null)
                tbcontrol.setSelectedIndex(which); // Then this for cyclic reference
            if (delegate != null)
                delegate.didSelectItem(this, selectedItem);
        }
    }

    public void beginCustomizingItems(ArrayList<UITabBarItem> items) {
        if (delegate != null)
            delegate.willBeginCustomizingItems(this, items);
        if (delegate != null)
            delegate.didBeginCustomizingItems(this, items);
    }

    public boolean endCustomizingAnimated(boolean animated) {
        boolean changed = false;
        if (delegate != null)
            delegate.willEndCustomizingItems(this, items, changed);
        if (delegate != null)
            delegate.didEndCustomizingItems(this, items, changed);
        return changed;
    }

    public boolean isCustomizing() {
        return false;
    }

    private void doLayout() {
        if (items == null || items.size() < 1)
            return;

        for (UIView v : getSubviews())
            v.removeFromSuperview();

        CGRect frame = getFrame();
        float width = frame.size.width / items.size();
        frame.size.width = width - 1;
        frame.origin.y = 0;
        for (int i = 0; i < items.size(); i++) {
            UITabBarItem item = items.get(i);
            frame.origin.x = width * i;
            UIButton b = UIButton.buttonWithType(UIButtonType.RoundedRect);
            b.setTitle(item.getTitle(), UIControlState.Normal);
            b.setFrame(frame);
            b.addTarget(new UIControlDelegate() {

                public void raiseEvent(UIControl sender, int UIControlEvent) {
                    setSelectedItem(items.get(sender.getTag()));
                }
            }, UIControlEvent.TouchUpInside);
            b.setTag(i);
            addSubview(b);
        }
        setNeedsDisplay();

    }
}
