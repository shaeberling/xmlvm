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

import java.util.HashSet;
import java.util.Set;

public class UIBarButtonItem extends UIBarItem {

    private UIView customView;
    private Set<String> possibleTitles = new HashSet<String>();
    private UIBarButtonItemDelegate target;
    private int style;

    public UIBarButtonItem(int UIBarButtonSystemItem, final UIBarButtonItemDelegate action) {
    }

    public UIBarButtonItem(UIView customview) {
        init(customview, UIBarButtonItemStyle.Plain);
    }

    public UIBarButtonItem(UIImage image, int UIBarButtonItemStyle, final UIBarButtonItemDelegate action) {
        UIButton button = UIButton.buttonWithType(UIButtonType.RoundedRect);
        if (action != null)
            button.addTarget(new UIControlDelegate() {

                @Override
                public void raiseEvent(UIControl sender, int eventType) {
                    action.clicked();
                }
            }, UIControlEvent.TouchUpInside);
        if (image != null) {
            button.setImage(image, UIControlState.Normal);
            button.setFrame(new CGRect(0, 0, image.getSize().width, 16));
        } else
            button.setFrame(new CGRect(0, 0, 10, 16));

        init(button, UIBarButtonItemStyle);
    }

    public UIBarButtonItem(String title, int UIBarButtonItemStyle, final UIBarButtonItemDelegate action) {
        UIButton button = UIButton.buttonWithType(UIButtonType.RoundedRect);
        if (action != null)
            button.addTarget(new UIControlDelegate() {

                @Override
                public void raiseEvent(UIControl sender, int eventType) {
                    action.clicked();
                }
            }, UIControlEvent.TouchUpInside);
        button.setTitle(title, UIControlState.Normal);
        button.setFrame(new CGRect(0, 0, button.getPrefferedWidth(), 16));
        possibleTitles.add(title);
        setTitle(title);

        init(button, UIBarButtonItemStyle);
    }

    private void init(UIView customView, int style) {
        this.customView = customView;
        this.style = style;
    }

    public UIView getCustomView() {
        return customView;
    }

    public void setCustomView(UIView customView) {
        this.customView = customView;
        if (getDelegate() != null)
            getDelegate().requestUpdate();
    }

    public Set<String> getPossibleTitles() {
        return possibleTitles;
    }

    public void setPossibleTitles(Set<String> possibleTitles) {
        this.possibleTitles = possibleTitles;
        if (getDelegate() != null)
            getDelegate().requestUpdate();
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
        if (getDelegate() != null)
            getDelegate().requestUpdate();
    }

    public UIBarButtonItemDelegate getTarget() {
        return target;
    }

    public void setTarget(UIBarButtonItemDelegate target) {
        this.target = target;
    }

    public float getWidth() {
        return customView.getFrame().size.width;
    }

    public void setWidth(float width) {
        customView.getFrame().size.width = width;
        if (getDelegate() != null)
            getDelegate().requestUpdate();
    }
}
