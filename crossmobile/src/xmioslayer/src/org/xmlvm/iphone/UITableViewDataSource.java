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

public abstract class UITableViewDataSource extends NSObject {

    public abstract UITableViewCell cellForRowAtIndexPath(UITableView table, NSIndexPath idx);

    public int numberOfSectionsInTableView(UITableView table) {
        return 1;
    }

    public abstract int numberOfRowsInSection(UITableView table, int section);

    public String titleForHeaderInSection(UITableView table, int section) {
        return null;
    }

    public void commitEditingStyle(UITableView table, int editingStyle, NSIndexPath indexPath) {
    }
}
