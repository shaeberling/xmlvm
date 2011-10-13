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
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import org.crossmobile.ios2a.ImplementationError;
import java.util.ArrayList;
import org.crossmobile.ios2a.IOSTableAdapter;
import org.crossmobile.ios2a.UIRunner;

public class UITableView extends UIScrollView {

    private UITableViewDataSource dataSource;
    private UITableViewDelegate delegate;
    private IOSTableAdapter adapter;
    private UIColor separatorColor = UIColor.whiteColor;
    private float rowHeight = 30;
    private boolean allowsSelection = true;

    public UITableView() {
        this(CGRect.Zero());
    }

    public UITableView(CGRect rect) {
        this(rect, UITableViewStyle.Plain);
    }

    public UITableView(CGRect rect, int UITableViewStyle) {
        super(rect);
        adapter = new IOSTableAdapter(this);
        ((ListView) xm_model()).setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> av, View view, int position, long id) {
                delegate.didSelectRowAtIndexPath(UITableView.this, new NSIndexPath(1, position));
            }
        });
    }

    public void setDataSource(UITableViewDataSource dataSource) {
        this.dataSource = dataSource;
        reloadData();
    }

    public void setDelegate(UITableViewDelegate delegate) {
        this.delegate = delegate;
    }

    public UITableViewDelegate getTableViewDelegate() {
        return delegate;
    }

    public UITableViewDataSource getDataSource() {
        return dataSource;
    }

    public UITableViewCell dequeueReusableCellWithIdentifier(String identifier) {
        if (adapter.offeredCellView == null || identifier == null)
            return null;
        Object offer = adapter.offeredCellView.getTag();
        if (offer == null || (!(offer instanceof UITableViewCell)))
            return null;
        UITableViewCell tcell = (UITableViewCell) offer;
        if (!identifier.equals(tcell.getReuseIdentifier()))
            return null;
        return tcell;
    }

    public void deleteRowsAtIndexPaths(ArrayList<NSIndexPath> indexPaths, boolean animation) {
        throw new ImplementationError();
    }

    public void reloadData() {
        UIRunner.runSynced(new UIRunner() {

            @Override
            public void exec() {
                ((ListView) xm_model()).setAdapter(adapter);
            }
        });
    }

    public void reloadRowsAtIndexPaths(ArrayList<NSIndexPath> indexPaths, int UITableViewRowAnimation) {
        NSLog.log("reloadRowsAtIndexPaths not yet implemented");
    }

    public void deselectRowAtIndexPath(NSIndexPath indexPath, boolean animation) {
        throw new ImplementationError();
    }

    public UIColor getSeparatorColor() {
        return separatorColor;
    }

    public void setSeparatorColor(UIColor separatorColor) {
        this.separatorColor = separatorColor;
        ((ListView) xm_model()).setDivider(new ColorDrawable(separatorColor.getModelColor()));
    }

    public int getSeparatorStyle() {
        if (((ListView) xm_model()).getDividerHeight() > 0)
            return UITableViewCellSeparatorStyle.SingleLine;
        else
            return UITableViewCellSeparatorStyle.None;
    }

    public void setSeparatorStyle(int UITableViewCellSeparatorStyle) {
        int height = UITableViewCellSeparatorStyle == org.xmlvm.iphone.UITableViewCellSeparatorStyle.None ? 0 : 1;
        ((ListView) xm_model()).setDividerHeight(height);
    }

    public boolean isAllowsSelection() {
        return allowsSelection;
    }

    public void setAllowsSelection(boolean allowsSelection) {
        this.allowsSelection = allowsSelection;
    }

    public boolean isEditing() {
        throw new ImplementationError();
    }

    public void setEditing(boolean editing) {
        throw new ImplementationError();
    }

    public void setEditing(boolean editing, boolean animated) {
        throw new ImplementationError();
    }

    public float getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(float rowHeight) {
        this.rowHeight = 30;
    }

    @Override
    View createModelObject(Context cx) {
        return new ListView(cx);
    }
}
