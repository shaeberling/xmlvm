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

package org.crossmobile.ios2a;

import android.content.Context;
import android.view.View;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import org.xmlvm.iphone.UIPickerView;
import org.xmlvm.iphone.UIView;

public class SingleWheelView extends UIView {

    private UIPickerView picker;
    private int component;
    private int lastrow = -1;

    public SingleWheelView(UIPickerView picker) {
        this.picker = picker;
    }

    public void setComponent(int component) {
        this.component = component;
    }

    public int selectedRow() {
        return ((WheelView) xm_model()).getCurrentItem();
    }

    public void selectRow(int row, boolean animated) {
        lastrow = row;
        ((WheelView) xm_model()).setCurrentItem(row, animated);
    }

    private void rowHasChanged(int newrow) {
        if (lastrow == newrow)
            return;
        picker.getDelegate().didSelectRow(picker, newrow, component);
    }

    /* This method actually overrides the UIResponder method, but can not
     * be tagged as @Overrides due to packaging/private issues
     */
    public View createModelObject(Context cx) {
        final WheelView view = new WheelView(cx);
        view.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                rowHasChanged(view.getCurrentItem());
            }
        });
        view.setViewAdapter(new SingleAdapter(cx));
        return view;
    }

    public void reloadData() {
        ((WheelView) xm_model()).invalidateWheel(true);
    }

    private class SingleAdapter extends AbstractWheelTextAdapter {

        public SingleAdapter(Context context) {
            super(context);
        }

        @Override
        protected CharSequence getItemText(int index) {
            return picker.getDelegate().titleForRow(picker, index, component);
        }

        public int getItemsCount() {
            return picker.getDataSource().numberOfRowsInComponent(picker, component);
        }
    }
}
