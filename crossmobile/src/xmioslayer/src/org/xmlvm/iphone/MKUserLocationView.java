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

import com.google.android.maps.OverlayItem;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import org.xmlvm.iphone.MKMapView.AnnotationsOverlay;

class MKUserLocationView extends MKAnnotationView {

    private static final CLLocationManager user_callback;
    static ArrayList<WeakReference<MKMapView>> maps;

    static {
        maps = new ArrayList<WeakReference<MKMapView>>();

        user_callback = new CLLocationManager();
        user_callback.setDelegate(new CLLocationManagerDelegate() {

            @Override
            public void didUpdateToLocation(CLLocationManager manager, CLLocation newLocation, CLLocation oldLocation) {
                ArrayList<WeakReference<MKMapView>> dead = new ArrayList<WeakReference<MKMapView>>();
                for (WeakReference<MKMapView> refmap : maps) {
                    MKMapView map = refmap.get();
                    if (map != null)
                        map.updateUserLocation(newLocation);
                }
            }
        });
        user_callback.startUpdatingLocation();
    }

    static void registerMap(MKMapView map) {
        maps.add(new WeakReference<MKMapView>(map));
        if (user_callback.getLocation() != null)
            map.updateUserLocation(user_callback.getLocation());

        // It's a good time to demove old references 
        for (WeakReference<MKMapView> refmap : maps)
            if (refmap.get() == null)
                maps.remove(refmap);
    }

    MKUserLocationView(MKUserLocation location, String reuseIdentifier) {
        super(location, reuseIdentifier);
    }

    OverlayItem getOverlayItem(AnnotationsOverlay overlay, boolean showsUserLocation) {
        return createOverlayItem(overlay);
    }
}
