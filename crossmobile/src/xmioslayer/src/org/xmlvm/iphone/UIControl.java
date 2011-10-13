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

import static org.xmlvm.iphone.UIControlEvent.*;

import java.util.HashSet;
import java.util.Set;

public class UIControl extends UIView {

    Set<EventDelegate> controldelegates;
    private boolean selected;
    private boolean enabled;
    private boolean highlighted;
    private boolean last_touch_inside = true;

    public UIControl() {
        this(CGRect.Zero());
    }

    public UIControl(CGRect rect) {
        super(rect);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void addTarget(UIControlDelegate delegate, int UIControlEvent) {
        // Lazy initialization of delegates
        if (controldelegates == null)
            controldelegates = new HashSet<EventDelegate>();
        controldelegates.add(new EventDelegate(UIControlEvent, delegate));
    }

    public Set<UIControlDelegate> allTargets() {
        HashSet<UIControlDelegate> targets = new HashSet<UIControlDelegate>();
        if (controldelegates != null)
            for (EventDelegate item : controldelegates)
                targets.add(item.delegate);
        return targets;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    private void raiseEvent(int event) {
        if (controldelegates == null)
            return;
        for (EventDelegate item : controldelegates)
            if ((item.event & event) > 0)
                item.delegate.raiseEvent(this, event);
    }

    @Override
    public void touchesBegan(Set<UITouch> touches, UIEvent event) {
        setHighlighted(true);
        last_touch_inside = true;
        raiseEvent(TouchDown);
    }

    @Override
    public void touchesMoved(Set<UITouch> touches, UIEvent event) {
        if (pointInside(event.firsttouch.locationInView(this), event)) {
            if (!last_touch_inside) {
                last_touch_inside = true;
                setHighlighted(true);
                raiseEvent(TouchDragEnter);
            }
            raiseEvent(TouchDragInside);
        } else {
            if (last_touch_inside) {
                last_touch_inside = false;
                setHighlighted(false);
                raiseEvent(TouchDragExit);
            }
            raiseEvent(TouchDragOutside);
        }
    }

    @Override
    public void touchesEnded(Set<UITouch> touches, UIEvent event) {
        setHighlighted(false);
        raiseEvent(pointInside(event.firsttouch.locationInView(this), event) ? TouchUpInside : TouchUpOutside);
    }

    static class EventDelegate {

        int event;
        UIControlDelegate delegate;

        private EventDelegate(int event, UIControlDelegate delegate) {
            this.event = event;
            this.delegate = delegate;
        }
    }
}
