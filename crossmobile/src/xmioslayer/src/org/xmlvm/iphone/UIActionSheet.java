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
import android.view.View;
import android.widget.Spinner;

public class UIActionSheet extends UIView {

    private UIActionSheet() {
    }

    public static UIActionSheet init(String title, UIActionSheetDelegate delegate,
            String cancelButtonTitle, String destructiveButtonTitle, String... otherButtonTitles) {
        return new UIActionSheet();
    }

    public void showFromTabBar(UITabBar bar) {
    }

    public void showFromToolbar(UIToolbar bar) {
    }

    public void showInView(UIView view) {
    }

    public void dismissWithClickedButtonIndex(int buttonIndex, boolean animated) {
    }

    @Override
    View createModelObject(Context cx) {
        // TODO : nothing works :)
        Spinner spin = new Spinner(cx);
//        spin.setAdapter(ad);

        final String[] items = new String[]{"Item1", "Item2", "Item3", "Item4"};
//        ArrayAdapter ad = new ArrayAdapter(this, items);
//        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spin.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//            public void onItemSelected(AdapterView arg0, View arg1, int arg2, long arg3) {
//                TextView txt = (TextView) findViewById(R.id.txt);
//                TextView temp = (TextView) arg1;
//                txt.setText(temp.getText());
//
//            }
//        };
        return spin;

    }
}
