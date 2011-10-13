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

import android.content.Context;
import static android.view.MotionEvent.*;

import android.view.MotionEvent;
import java.lang.ref.WeakReference;
import org.crossmobile.ios2a.IOSView;
import org.crossmobile.ios2a.ImplementationError;

public class UITableViewCell extends UIView {

    private final String reuseIdentifier;
    //
    boolean selected = false;
    WeakReference<UITableView> parent;
    //
    private UIView contentV;
    private UIView unselectedBV;
    private UIView selectedBV;
    private UIView accessoryV;
    private UILabel textlabel;
    private xmEventDispatcher dispatcher = new xmEventDispatcher(this);

    public UITableViewCell() {
        this(UITableViewCellStyle.Default, null);
    }

    public UITableViewCell(int UITableViewCellStyle, String reuseIdentifier) {
        contentV = new UIView();
        addSubview(contentV);
        this.reuseIdentifier = reuseIdentifier;
    }

    @Override
    public void setFrame(CGRect frame) {
        super.setFrame(frame);
        if (contentV != null) { // Only when the object is fully initialized
            CGRect bounds = getBounds();
            contentV.setFrame(bounds);
            if (textlabel != null)
                textlabel.setFrame(bounds);
            if (unselectedBV != null)
                unselectedBV.setFrame(bounds);
            if (selectedBV != null)
                selectedBV.setFrame(bounds);
        }
    }

    public int getEditingStyle() {
        return UITableViewCellEditingStyle.None;
    }

    public void setSelected(boolean sel) {
        if (selected == sel)
            return;
        selected = sel;
        if (parent.get() != null && parent.get().isAllowsSelection())
            updatebackgroundViews();
    }

    public boolean isSelected() {
        return selected;
    }

    private void updatebackgroundViews() {
        if (selected)
            if (selectedBV == null) {
                if (unselectedBV != null)
                    unselectedBV.setHidden(false);
            } else {
                if (unselectedBV != null)
                    unselectedBV.setHidden(true);
                selectedBV.setHidden(false);
            }
        else {
            if (unselectedBV != null)
                unselectedBV.setHidden(false);
            if (selectedBV != null)
                selectedBV.setHidden(true);
        }
    }

    public void setBackgroundView(UIView backgroundView) {
        if (backgroundView == unselectedBV)
            return;

        // Remove old
        if (unselectedBV != null) {
            unselectedBV.removeFromSuperview();
            unselectedBV = null;
        }

        // Add new
        if (backgroundView != null) {
            unselectedBV = backgroundView;
            insertSubview(unselectedBV, 0);
            unselectedBV.setFrame(getBounds());
            updatebackgroundViews();
        }
    }

    public UIView getBackgroundView() {
        return unselectedBV;
    }

    public void setSelectedBackgroundView(UIView selectedBackgroundView) {
        if (selectedBackgroundView == selectedBV)
            return;

        // Remove old
        if (selectedBV != null) {
            selectedBV.removeFromSuperview();
            selectedBV = null;
        }

        // Add new
        if (selectedBackgroundView != null) {
            selectedBV = selectedBackgroundView;
            insertSubview(selectedBV, 0);
            selectedBV.setFrame(getBounds());
            updatebackgroundViews();
        }
    }

    public UIView getSelectedBackgroundView() {
        return selectedBV;
    }

    public UIView getContentView() {
        return contentV;
    }

    public UILabel getTextLabel() {
        if (textlabel == null) {
            textlabel = new UILabel(getBounds());
            contentV.addSubview(textlabel);
        }
        return textlabel;
    }

    public UILabel getDetailTextLabel() {
        throw new ImplementationError();
    }

    public UIImageView getImageView() {
        throw new ImplementationError();
    }

    public UIView getAccessoryView() {
        return accessoryV;
    }

    public void setAccessoryView(UIView accessoryView) {
        this.accessoryV = accessoryView;
    }

    public String getReuseIdentifier() {
        return reuseIdentifier;
    }

    @Override
    IOSView createBaseObject(Context cx) {
        return new IOSView(cx) {

            @Override
            public boolean dispatchTouchEvent(MotionEvent ev) {
                if (ev.getAction() == ACTION_DOWN)
                    setSelected(true);
                else if (ev.getAction() == ACTION_CANCEL || ev.getAction() == ACTION_UP)
                    setSelected(false);
                dispatcher.sendEvent(dispatcher.createEvent(ev, true));
                return super.dispatchTouchEvent(ev);   // For other native widgets to work
            }
        };
    }

    public void xm_reparent(UITableView tv) {
        parent = new WeakReference<UITableView>(tv);
    }
}
