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

import android.content.Intent;
import android.net.Uri;
import android.view.Window;
import android.view.WindowManager;
import java.util.ArrayList;
import java.util.List;
import org.crossmobile.ios2a.IOSView;
import org.crossmobile.ios2a.MainActivity;

public class UIApplication extends UIResponder {

    private static UIApplication instance;
    List<UIWindow> windows;
    private boolean idleTimerDisabled;
    private UIApplicationDelegate delegate;
    private UIWindow keyWindow;
    private int statusBarStyle;
    private boolean statusbarhidden = false;
    private boolean networkActivityIndicatorVisible;

    public UIApplication() {
        windows = new ArrayList<UIWindow>();
        idleTimerDisabled = false;
    }

    public static UIApplication sharedApplication() {
        if (instance == null)
            instance = new UIApplication();
        return instance;
    }

    public UIApplicationDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(UIApplicationDelegate delegate) {
        this.delegate = delegate;
    }

    public void setIdleTimerDisabled(boolean flag) {
        this.idleTimerDisabled = flag;
    }

    public boolean isIdleTimerDisabled() {
        return this.idleTimerDisabled;
    }

    public int getStatusBarStyle() {
        return statusBarStyle;
    }

    public void setStatusBarStyle(int UIStatusBarStyle) {
        this.statusBarStyle = UIStatusBarStyle;
    }

    public void setStatusBarStyle(int UIStatusBarStyle, boolean animated) {
        this.statusBarStyle = UIStatusBarStyle;
    }

    public boolean isNetworkActivityIndicatorVisible() {
        return networkActivityIndicatorVisible;
    }

    public void setNetworkActivityIndicatorVisible(boolean networkActivityIndicatorVisible) {
        this.networkActivityIndicatorVisible = networkActivityIndicatorVisible;
    }

    void setKeyWindow(UIWindow window) {
        if (!windows.contains(window))
            windows.add(window);
        keyWindow = window;
    }

    public UIWindow getKeyWindow() {
        return keyWindow;
    }

    public List<UIWindow> getWindows() {
        return new ArrayList<UIWindow>(windows);
    }

    public void setStatusBarHidden(boolean statusbarhidden) {
        setStatusBarHidden(statusbarhidden, false);
    }

    public void setStatusBarHidden(boolean statusbarhidden, boolean animated) {
        Window w = MainActivity.current.getWindow();
        if (statusbarhidden) {
            w.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            w.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        } else {
            w.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            w.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        this.statusbarhidden = statusbarhidden;
        IOSView.updateRatios(); // call this AFTER updating statusbarhidden variable
        if (keyWindow != null) {
            keyWindow.updateStatusBarDelta();
            keyWindow.setNeedsDisplay();
        }
    }

    public boolean isStatusBarHidden() {
        return statusbarhidden;
    }

    public static void main(String[] args, Class<? extends UIApplication> UIApplication, Class<? extends UIApplicationDelegate> UIApplicationDelegate) {
        try {
            if (UIApplication == null)
                UIApplication = UIApplication.class;
            if (UIApplicationDelegate == null)
                UIApplicationDelegate = UIApplicationDelegate.class;
            instance = ((UIApplication) UIApplication.newInstance());
            instance.setDelegate((UIApplicationDelegate) UIApplicationDelegate.newInstance());
            instance.setStatusBarHidden(false);
            instance.getDelegate().applicationDidFinishLaunching(instance);
            instance.getDelegate().applicationDidBecomeActive(instance);
            if (instance.getKeyWindow() != null)
                instance.getKeyWindow().doInitialLayoutWithDelegates();
        } catch (Throwable e) {
            throw new RuntimeException("Unable to launch CrossMobile application", e);
        }
    }

    public void openURL(NSURL URLWithString) {
        Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(URLWithString.absoluteString()));
        MainActivity.current.startActivity(browserIntent);
    }
}
