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

import android.app.Activity;
import android.app.ActivityGroup;
import android.os.Bundle;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.xmlvm.iphone.NSLog;
import org.xmlvm.iphone.UIApplication;
import org.xmlvm.iphone.UINavigationController;
import org.xmlvm.iphone.UIView;
import org.xmlvm.iphone.UIViewController;

public abstract class MainActivity extends ActivityGroup {

    public static Activity current;
    //
    private final static String[] args = {};
    private final static Field field_controller;

    static {
        Field contr = null;
        try {
            contr = UIView.class.getDeclaredField("controller");
            contr.setAccessible(true);
        } catch (Exception ex) {
            NSLog.log(ex);
        }
        field_controller = contr;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        current = this;
        ExceptionHandler.setActive();
        LifeCycle.onCreate();

        // Find iphone main class name
        final String classname = getMainClass();
        if (classname == null) {
            LifeCycle.finishWithError("Not a valid CrossMobile application, missing main class definition", null);
            return;
        }

        // Initialize main class through reflection
        Method main = null;
        try {
            Class clazz = getClassLoader().loadClass(classname);
            main = clazz.getMethod("main", args.getClass());
            if (main == null)
                throw new IllegalArgumentException("Class " + classname + " does not support method with signature 'public static void main(String[])'");
        } catch (Exception ex) {
            LifeCycle.finishWithError("Unable to find CrossMobile application " + classname, ex);
            return;
        }

        // Execute main class
        try {
            main.invoke(null, new Object[]{args});
        } catch (Exception ex) {
            LifeCycle.finishWithError("Unable to initialize CrossMobile application " + classname, ex);
            return;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        LifeCycle.onStop();
        if (UIApplication.sharedApplication() != null && UIApplication.sharedApplication().getDelegate() != null)
            UIApplication.sharedApplication().getDelegate().applicationWillTerminate(UIApplication.sharedApplication());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (UIApplication.sharedApplication() != null && UIApplication.sharedApplication().getDelegate() != null)
            UIApplication.sharedApplication().getDelegate().applicationWillResignActive(UIApplication.sharedApplication());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (UIApplication.sharedApplication() != null && UIApplication.sharedApplication().getDelegate() != null)
            UIApplication.sharedApplication().getDelegate().applicationDidBecomeActive(UIApplication.sharedApplication());
    }

    @Override
    public void onBackPressed() {
        if (field_controller != null && UIApplication.sharedApplication() != null && UIApplication.sharedApplication().getDelegate() != null) { // Application is active
            UINavigationController navigation = null;
            if (UIApplication.sharedApplication().getKeyWindow() != null) {
                UIViewController vc = getViewController(UIApplication.sharedApplication().getKeyWindow());
                while (vc != null) {
                    if (vc instanceof UINavigationController)
                        navigation = (UINavigationController) vc;
                    if (backbuttonIsBlocked(vc)) {
                        super.onBackPressed();
                        return;
                    } else
                        vc = getViewController(vc.getView());
                }
                if (navigation != null && navigation.getNavigationController().popViewControllerAnimated(true) != null)
                    return;
            }
        }
        super.onBackPressed();
    }

    private boolean backbuttonIsBlocked(UIViewController vc) {
        try {
            return (Boolean) vc.getClass().getMethod("shouldBlockBackButton", (Class<?>[]) null).invoke(vc, (Object[]) null);
        } catch (Exception ex) {
        }
        return false;
    }

    private UIViewController getViewController(UIView view) {
        for (UIView child : view.getSubviews())
            try {
                UIViewController vc = (UIViewController) field_controller.get(child);
                if (vc != null)
                    return vc;
            } catch (Exception ex) {
            }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LifeCycle.onDestroy();
        current = null;
    }

    protected abstract String getMainClass();
}
