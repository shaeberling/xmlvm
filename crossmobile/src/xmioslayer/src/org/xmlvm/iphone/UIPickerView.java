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

import android.graphics.drawable.GradientDrawable;
import org.crossmobile.ios2a.SingleWheelView;
import java.util.ArrayList;
import org.crossmobile.ios2a.UIRunner;

public class UIPickerView extends UIView {

    private static final float BORDER = 4;
    //
    private static final UIPickerViewDataSource EMPTYDATASOURCE = new UIPickerViewDataSource() {

        @Override
        public int numberOfComponentsInPickerView(UIPickerView view) {
            return 1;
        }

        @Override
        public int numberOfRowsInComponent(UIPickerView view, int component) {
            return 0;
        }
    };
    private static final UIPickerViewDelegate EMPTYDELEGATE = new UIPickerViewDelegate() {
    };
    //
    //
    private UIPickerViewDataSource dataSource = EMPTYDATASOURCE;
    private UIPickerViewDelegate delegate = EMPTYDELEGATE;
    private boolean showsSelectionIndicator;
    private ArrayList<SingleWheelView> wheels = new ArrayList<SingleWheelView>();

    public UIPickerView() {
        this(CGRect.Zero());
    }

    public UIPickerView(CGRect rect) {
        super(rect);
        xm_base().setBackgroundDrawable(new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{0xff222222, 0xff666666, 0xff111111}));
    }

    public int getNumberOfComponents() {
        return dataSource.numberOfComponentsInPickerView(this);
    }

    public int numberOfRowsInComponent(int component) {
        return dataSource.numberOfRowsInComponent(this, component);
    }

    public CGSize rowSizeForComponent(int component) {
        return null;
    }

    public UIView viewForRow(int row, int component) {
        return delegate.viewForRow(this, row, component, null);
    }

    public UIPickerViewDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(UIPickerViewDataSource dataSource) {
        if (dataSource == null)
            dataSource = EMPTYDATASOURCE;
        if (this.dataSource == dataSource)
            return;
        this.dataSource = dataSource;
        reloadAllComponents();
    }

    public UIPickerViewDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(UIPickerViewDelegate delegate) {
        if (delegate == null)
            delegate = EMPTYDELEGATE;
        if (this.delegate == delegate)
            return;
        this.delegate = delegate;
        reloadAllComponents();
    }

    public boolean showsSelectionIndicator() {
        return showsSelectionIndicator;
    }

    public void setShowsSelectionIndicator(boolean showsSelectionIndicator) {
        this.showsSelectionIndicator = showsSelectionIndicator;
    }

    public void reloadAllComponents() {
        UIRunner.runSynced(new UIRunner() {

            public void exec() {
                /* Create single wheelview objects */
                int comp = dataSource.numberOfComponentsInPickerView(UIPickerView.this);
                while (wheels.size() > comp)
                    wheels.remove(wheels.size() - 1).removeFromSuperview();
                while (wheels.size() < (comp + 1)) {
                    SingleWheelView item = new SingleWheelView(UIPickerView.this);
                    wheels.add(item);
                    addSubview(item);
                }

                CGRect frame = getFrame();
                float runningX = BORDER;
                float height = frame.size.height - BORDER * 2;
                float maxwidth = frame.size.width - BORDER;

                for (int i = 0; i < comp; i++) {
                    SingleWheelView item = wheels.get(i);
                    // Set Size
                    float width = delegate.widthForComponent(UIPickerView.this, i);
                    if (runningX + width > maxwidth)
                        width = maxwidth - runningX;
                    if (width < 0)
                        width = 0;
                    item.setFrame(new CGRect(runningX, BORDER, width, height));
                    runningX += width + BORDER;

                    // Set Data
                    item.setComponent(i);
                    item.reloadData();
                }
                setNeedsDisplay();
            }
        });
    }

    public void reloadComponent(int component) {
        wheels.get(component).reloadData();
    }

    public int selectedRowInComponent(int component) {
        return wheels.get(component).selectedRow();
    }

    public void selectRow(int row, int component, boolean animated) {
        wheels.get(component).selectRow(row, animated);
    }
}
