/*
 * Copyright (c) 2004-2009 XMLVM --- An XML-based Programming Language
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 675 Mass
 * Ave, Cambridge, MA 02139, USA.
 * 
 * For more information, visit the XMLVM Home Page at http://www.xmlvm.org
 */

package android.app;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * IPhone implementation of Android's Activity class.
 * 
 * @see http://developer.android.com/reference/android/app/Activity.html}
 */
public class Activity extends ContextThemeWrapper {
    private ActivityWrapper myIphoneWrapper;
    private Window          window;

    protected void onCreate(Bundle savedInstanceState) {
        window = new Window(this);
        onContentChanged();
    }

    public void setContentView(View view) {
        window.setContentView(view);
    }

    /**
     * TODO: Implement for real.
     */
    public WindowManager getWindowManager() {
        return new WindowManager();
    }

    /**
     * Can be overridden by subclasses.
     */
    public void onContentChanged() {
    }

    public void showDialog(int id) {
        Dialog dialog = onCreateDialog(id);
        if (dialog != null)
            dialog.show();
    }

    protected Dialog onCreateDialog(int id) {
        return null;
    }

    /**
     * Can be overridden by subclasses that want to create a menu.
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    /**
     * Can be overridden by subclasses that want to handle menu button presses.
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    /**
     * TODO: Implement for real.
     */
    public void startActivity(Intent intent) {
    }

    public void requestWindowFeature(int feature) {
        /*
         * TODO: can't handle jvm:lookupswitch
         * 
         * switch (feature) {
         * 
         * case Window.FEATURE_NO_TITLE: // TODO: This will remove the title
         * bar, but not get rid of the // status bar. On the iPhone we don't
         * have a title, but maybe we should // implement this as a
         * compatibility feature. Once we have it, this is the // place to
         * disable it. break; }
         */
    }

    /**
     * Retrieve the current {@link Window} for the activity.
     */
    public Window getWindow() {
        return window;
    }

    /**
     * Internal. Not part of Android API.
     */
    public void setMyIphoneWrapper(ActivityWrapper myIphoneWrapper) {
        this.myIphoneWrapper = myIphoneWrapper;
    }

    /**
     * Internal. Not part of Android API.
     */
    public ActivityWrapper getMyIphoneWrapper() {
        return myIphoneWrapper;
    }

    /**
     * Change the desired orientation of this activity.
     * <p>
     * TODO: Implement for real!
     */
    public void setRequestedOrientation(int requestedOrientation) {
        switch(requestedOrientation) {
        case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:
            break;
        case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
            break;
        }
    }
}
