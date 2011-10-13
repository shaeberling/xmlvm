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
import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.xmlvm.iphone.NSLog;
import org.xmlvm.iphone.NSTimer;
import org.xmlvm.iphone.UIGraphics;

public class LifeCycle {

    private static final ArrayList<WeakReference<Activity>> activitylist = new ArrayList<WeakReference<Activity>>();
    private static final ArrayList<WeakReference<LocationListener>> locationlist = new ArrayList<WeakReference<LocationListener>>();
    private static final Set<NSTimer> registry = new HashSet<NSTimer>();

    public static void register(Activity mapactivity) {
        activitylist.add(new WeakReference<Activity>(mapactivity));
    }

    public static void register(NSTimer timer) {
        registry.add(timer);
    }

    public static void register(LocationListener manager) {
        locationlist.add(new WeakReference<LocationListener>(manager));
    }

    public static void unregister(NSTimer timer) {
        registry.remove(timer);
    }

    public static void onCreate() {
        FileBridge.cleanTemporaryLocation();
    }

    public static void onStop() {
        for (NSTimer timer : registry)
            timer.invalidate();
    }

    public static void onDestroy() {
        for (WeakReference<Activity> item : activitylist)
            try {
                if (item.get() != null)
                    item.get().finish();
            } catch (Exception e) {
            }

        LocationManager manager = (LocationManager) MainActivity.current.getSystemService(Context.LOCATION_SERVICE);
        for (WeakReference<LocationListener> item : locationlist)
            try {
                if (item.get() != null)
                    manager.removeUpdates(item.get());
            } catch (Exception e) {
            }

        // Clean up cache
        FileBridge.cleanTemporaryLocation();

        // remove all graphics contexts (usually is empty, but just in case)
        try {
            while (true)
                UIGraphics.popContext();
        } catch (Exception e) {
        }

        System.exit(0); // Bad but... required
    }

    public static void finishWithError(final String error, final Throwable throwable) {
        final Activity activity = MainActivity.current;
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (error != null) {
                    NSLog.log(error);
                    Toast.makeText(activity, error, Toast.LENGTH_LONG).show();
                }
                if (throwable != null)
                    NSLog.log(throwable);
                activity.finish();
            }
        });
    }
}
