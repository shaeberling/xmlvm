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
import org.crossmobile.ios2a.BarItemDelegate;
import org.crossmobile.ios2a.UIRunner;

public class UIToolbar extends UIView {

    private static final int LEFTOFFSET = 4;
    private static final int VERTICALOFFSET = 3;
    private static final int BETWEENOFFSET = 4;
    private ArrayList<UIBarButtonItem> items = new ArrayList<UIBarButtonItem>();
    private boolean translucent = false;
    private int barStyle;
    private BarItemDelegate itemscallback = new BarItemDelegate() {

        public void requestUpdate() {
            updateViews();
        }

        public void detachItem(UIBarItem item) {
            if (item instanceof UIBarButtonItem)
                items.remove((UIBarButtonItem) item);
        }
    };

    public UIToolbar() {
        this(CGRect.Zero());
    }

    public UIToolbar(CGRect frame) {
        super(frame);
    }

    @Override
    public void setFrame(CGRect frame) {
        super.setFrame(frame);
        updateViews();
    }

    public ArrayList<UIBarButtonItem> getItems() {
        return new ArrayList<UIBarButtonItem>(items);
    }

    public void setItems(ArrayList<UIBarButtonItem> items) {
        setItems(items, false);
    }

    public void setItems(ArrayList<UIBarButtonItem> items, boolean animated) {
        if (items == null)
            items = new ArrayList<UIBarButtonItem>();
        this.items = items;
        for (UIBarButtonItem item : items)
            item.setDelegate(itemscallback);
        updateViews();
    }

    public int getBarStyle() {
        return barStyle;
    }

    public void setBarStyle(int UIBarStyle) {
        this.barStyle = UIBarStyle;
        updateViews();
    }

    public UIColor getTintColor() {
        int color = getBackgroundColor().getModelColor() & 0xffffff;
        color |= 0xff000000;
        return new UIColor(color);
    }

    public final void setTintColor(UIColor tintColor) {
        int color = tintColor.getModelColor() & 0xffffff;
        if (translucent)
            color |= 0xaa000000;
        else
            color |= 0xff000000;
        setBackgroundColor(new UIColor(color));
    }

    public boolean isTranslucent() {
        return translucent;
    }

    public void setTranslucent(boolean translucent) {
        this.translucent = translucent;
        updateViews();
    }

    void updateViews() {
        UIRunner.runSynced(new UIRunner() {

            @Override
            public void exec() {
                if (items == null)  // be safe with early initialization, due to overiding of setFrame method
                    return;
                for (UIView v : getSubviews())
                    v.removeFromSuperview();

                CGRect frame = getFrame();
                float height = frame.size.height - VERTICALOFFSET * 2;
                float runningX = LEFTOFFSET;

                for (UIBarButtonItem item : items) {
                    UIView iview = item.getCustomView();
                    float width = item.getWidth();
                    iview.setFrame(new CGRect(runningX, VERTICALOFFSET, width, height));
                    addSubview(iview);
                    runningX += width + BETWEENOFFSET;
                }
                setNeedsDisplay();
            }
        });
    }
}
