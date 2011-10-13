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

import org.crossmobile.ios2a.BarItemDelegate;

public abstract class UIBarItem extends NSObject {

    private boolean enabled;
    private UIImage image;
    private UIEdgeInsets imageInsets;
    private int tag;
    private String title;
    private BarItemDelegate delegate;

    public UIBarItem() {
        enabled = true;
        image = null;
        imageInsets = new UIEdgeInsets();
        tag = 0;
        title = null;
        delegate = null;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (delegate != null)
            delegate.requestUpdate();
    }

    public UIImage getImage() {
        return image;
    }

    public void setImage(UIImage image) {
        this.image = image;
        if (delegate != null)
            delegate.requestUpdate();
    }

    public UIEdgeInsets getImageInsets() {
        return imageInsets;
    }

    public void setImageInsets(UIEdgeInsets imageInsets) {
        this.imageInsets = imageInsets;
        if (delegate != null)
            delegate.requestUpdate();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        if (delegate != null)
            delegate.requestUpdate();
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    void setDelegate(BarItemDelegate delegate) {
        if (this.delegate != null)
            this.delegate.detachItem(this);
        this.delegate = delegate;
    }

    BarItemDelegate getDelegate() {
        return delegate;
    }
}
