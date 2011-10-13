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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import java.util.ArrayList;
import org.crossmobile.ios2a.MainActivity;
import org.crossmobile.ios2a.UIRunner;

public class UIAlertView extends UIView {

    private UIAlertViewDelegate delegate;
    private String title;
    private String message;
    private ArrayList<String> buttons;

    public UIAlertView(String title, String message, UIAlertViewDelegate delegate, String cancelButtonTitle) {
        this.title = title;
        this.message = message;
        this.delegate = delegate;
        buttons = new ArrayList<String>();
        if (cancelButtonTitle == null)
            cancelButtonTitle = "Cancel";
        buttons.add(cancelButtonTitle);
    }

    public void show() {
        UIRunner.runSynced(new UIRunner() {

            @Override
            public void exec() {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.current).create();
                dialog.setTitle(title);
                dialog.setMessage(message);
                dialog.setButton(AlertDialog.BUTTON_NEGATIVE, buttons.get(0), new OnClickListener() {

                    public void onClick(DialogInterface di, int i) {
                        if (delegate != null)
                            delegate.clickedButtonAtIndex(UIAlertView.this, 0);
                    }
                });
                if (buttons.size() > 1) {
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, buttons.get(1), new OnClickListener() {

                        public void onClick(DialogInterface di, int i) {
                            if (delegate != null)
                                delegate.clickedButtonAtIndex(UIAlertView.this, 1);
                        }
                    });
                    if (buttons.size() > 2)
                        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, buttons.get(2), new OnClickListener() {

                            public void onClick(DialogInterface di, int i) {
                                if (delegate != null)
                                    delegate.clickedButtonAtIndex(UIAlertView.this, 2);
                            }
                        });
                }
                dialog.show();
            }
        });
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMesssage() {
        return message;
    }

    public int addButtonWithTitle(String title) {
        buttons.add(title);
        return buttons.size() - 1;
    }
}
