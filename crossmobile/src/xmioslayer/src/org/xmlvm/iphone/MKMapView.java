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

import android.app.Activity;
import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.crossmobile.ios2a.ImplementationError;
import org.crossmobile.ios2a.FileBridge;
import org.crossmobile.ios2a.MainActivity;
import org.crossmobile.ios2a.LifeCycle;

public class MKMapView extends UIView {

    private static final String MAP_ACTIVITY = MainActivity.current.getClass().getPackage().getName() + ".XMMAP";
    private static int last_id = 0;
    private static final Object population_lock = new Object();
    //
    private String currentID;
    private int mapType;
    private boolean zoomEnabled;
    private MKMapViewDelegate delegate;
    //
    private MKCoordinateRegion region;
    private CLLocationCoordinate2D centerCoordinate;
    private MKMapRect visibleMapRect;
    //
    private boolean showsUserLocation;
    private boolean userLocationVisible;
    private MKUserLocation userLocation;
    //
    private AnnotationsOverlay annotations;
    private boolean isPopulated = false;
    private ArrayList<MKAnnotation> selectedAnnotations = new ArrayList<MKAnnotation>();
    private ArrayList<MKOverlay> overlays = new ArrayList<MKOverlay>();

    public MKMapView() {
        this(CGRect.Zero());
    }

    @SuppressWarnings({"OverridableMethodCallInConstructor", "LeakingThisInConstructor"})
    public MKMapView(CGRect frame) {
        super(frame);
        setScrollEnabled(true);
        setZoomEnabled(true);
        MKUserLocationView.registerMap(this);
    }

    public int getMapType() {
        return mapType;
    }

    public void setMapType(int MKMapType) {
        MapView map = (MapView) xm_model();
        switch (MKMapType) {
            case org.xmlvm.iphone.MKMapType.Standard:
                map.setSatellite(false);
                break;
            case org.xmlvm.iphone.MKMapType.Hybrid:
            case org.xmlvm.iphone.MKMapType.Satellite:
                map.setSatellite(true);
        }
        mapType = MKMapType;
    }

    public boolean isScrollEnabled() {
        return xm_model().isClickable();
    }

    public void setScrollEnabled(boolean scrollEnabled) {
        xm_model().setClickable(scrollEnabled);
    }

    public boolean isZoomEnabled() {
        return zoomEnabled;
    }

    public void setZoomEnabled(boolean zoomEnabled) {
        this.zoomEnabled = zoomEnabled;
        ((MapView) xm_model()).setBuiltInZoomControls(zoomEnabled);
    }

    public MKMapViewDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(MKMapViewDelegate delegate) {
        this.delegate = delegate;
    }

    public MKCoordinateRegion getRegion() {
        return region;
    }

    public void setRegion(MKCoordinateRegion region) {
        setRegion(region, false);
    }

    public void setRegion(MKCoordinateRegion region, boolean animated) {
        this.region = region;
        setCenterCoordinate(region.center, animated);
        int zoom = (int) Math.round(Math.log(720 / region.span.latitudeDelta) / Math.log(2));   // use only latitude - needs half value (360/0.5 = 720)
        ((MapView) xm_model()).getController().setZoom(zoom);
    }

    public CLLocationCoordinate2D getCenterCoordinate() {
        GeoPoint point = ((MapView) xm_model()).getMapCenter();
        return new CLLocationCoordinate2D(point.getLatitudeE6() / 1e6, point.getLongitudeE6() / 1e6);
    }

    public void setCenterCoordinate(CLLocationCoordinate2D centerCoordinate) {
        setCenterCoordinate(centerCoordinate, false);
    }

    public void setCenterCoordinate(CLLocationCoordinate2D centerCoordinate, boolean animated) {
        GeoPoint point = new GeoPoint((int) (centerCoordinate.latitude * 1e6), (int) (centerCoordinate.longitude * 1e6));
        if (animated)
            ((MapView) xm_model()).getController().animateTo(point);
        else
            ((MapView) xm_model()).getController().setCenter(point);
    }

    public MKMapRect getVisibleMapRect() {
        return visibleMapRect;
    }

    public void setVisibleMapRect(MKMapRect visibleMapRect) {
        setVisibleMapRect(visibleMapRect, false);
    }

    public void setVisibleMapRect(MKMapRect visibleMapRect, boolean animated) {
        setVisibleMapRect(visibleMapRect, new UIEdgeInsets(0, 0, 0, 0), animated);
    }

    public void setVisibleMapRect(MKMapRect visibleMapRect, UIEdgeInsets edgePadding,
            boolean animated) {
        this.visibleMapRect = visibleMapRect;
    }

    public boolean isShowsUserLocation() {
        return showsUserLocation;
    }

    public void setShowsUserLocation(boolean showsUserLocation) {
        this.showsUserLocation = showsUserLocation;
        annotations.doPopulate();
    }

    public MKUserLocation getUserLocation() {
        return userLocation;
    }

    public boolean isUserLocationVisible() {
        return userLocationVisible;
    }

    public CGPoint convertCoordinateToPointToView(CLLocationCoordinate2D coordinate, UIView view) {
        throw new ImplementationError();
    }

    public CLLocationCoordinate2D convertPointToCoordinateFromView(CGPoint point, UIView view) {
        throw new ImplementationError();
    }

    public CGRect convertRegionToRectToView(MKCoordinateRegion region, UIView view) {
        throw new ImplementationError();
    }

    public MKCoordinateRegion convertRectToRegionFromView(CGRect rect, UIView view) {
        throw new ImplementationError();
    }

    public MKCoordinateRegion regionThatFits(MKCoordinateRegion region) {
        throw new ImplementationError();
    }

    public MKMapRect mapRectThatFits(MKMapRect mapRect) {
        throw new ImplementationError();
    }

    public MKMapRect mapRectThatFits(MKMapRect mapRect, UIEdgeInsets insets) {
        throw new ImplementationError();
    }

    public ArrayList<MKAnnotation> getAnnotations() {
        return annotations.getAnnotations();
    }

    public void addAnnotation(MKAnnotation annotation) {
        annotations.add(annotation);
    }

    public void addAnnotations(ArrayList<MKAnnotation> annotations) {
        this.annotations.addAll(annotations);
    }

    public void removeAnnotation(MKAnnotation annotation) {
        annotations.remove(annotation);
    }

    public void removeAnnotations(ArrayList<MKAnnotation> annotations) {
        this.annotations.removeAll(annotations);
    }

    public MKAnnotationView viewForAnnotation(MKAnnotation annotation) {
        return null;
    }

    public Set<MKAnnotation> annotationsInMapRect(MKMapRect rect) {
        // TODO: Java implementation
        return new HashSet<MKAnnotation>();
    }

    public CGRect getAnnotationVisibleRect() {
        // TODO: Java implementation
        return null;
    }

    public MKAnnotationView dequeueReusableAnnotationViewWithIdentifier(String id) {
        // No reusable pin-points supported for Android.
        // No support for this functionality in the API.
        // Lazy initialization is used instead (which should not be enough).
        return null;
    }

    public ArrayList<MKAnnotation> getSelectedAnnotations() {
        return selectedAnnotations;
    }

    public void setSelectedAnnotations(ArrayList<MKAnnotation> selectedAnnotations) {
        this.selectedAnnotations = new ArrayList<MKAnnotation>();
        if (selectedAnnotations != null && selectedAnnotations.size() > 0)
            this.selectedAnnotations.add(selectedAnnotations.get(0));
    }

    public void selectAnnotation(MKAnnotation annotation, boolean animated) {
//        if (annotations.contains(annotation))
        selectedAnnotations.add(annotation); // TODO : update visuals
    }

    public void deselectAnnotation(MKAnnotation annotation, boolean animated) {
        selectedAnnotations.remove(annotation);
        // TODO : update visuals
    }

    public ArrayList<MKOverlay> getOverlays() {
        return overlays;
    }

    public void addOverlay(MKOverlay overlay) {
        overlays.add(overlay);
    }

    public void addOverlays(ArrayList<MKOverlay> overlays) {
        this.overlays.addAll(overlays);
    }

    public void removeOverlay(MKOverlay overlay) {
        overlays.remove(overlay);
    }

    public void removeOverlays(ArrayList<MKOverlay> overlays) {
        this.overlays.removeAll(overlays);
    }

    public void insertOverlay(MKOverlay overlay, int index) {
        overlays.add(index, overlay);
    }

    public void exchangeOverlay(int index1, int index2) {
        MKOverlay ol1 = overlays.get(index1);
        MKOverlay ol2 = overlays.get(index2);
        overlays.set(index2, ol1);
        overlays.set(index1, ol2);
    }

    public void insertOverlayAboveOverlay(MKOverlay overlay, MKOverlay sibling) {
        int pos = overlays.indexOf(overlay);
        insertOverlay(sibling, pos + 1);
    }

    public void insertOverlayBelowOverlay(MKOverlay overlay, MKOverlay sibling) {
        int pos = overlays.indexOf(overlay);
        insertOverlay(sibling, pos);
    }

    public MKOverlayView viewForOverlay(MKOverlay overlay) {
        // TODO : Java implementation
        return null;
    }

    @Override
    View createModelObject(Context cx) {
        currentID = "map" + (last_id++);

        /// This should be initialized guarandeed before the annotations object
        if (userLocation == null)
            userLocation = new MKUserLocation();
        // Create the annotations overlay 
        annotations = new AnnotationsOverlay(FileBridge.getDrawable("empty"));

        /* TODO: We need to create a new MapActivity for every MapView.
         * There should be a way to properly destroy this view, when the MapView is not required any more
         * We can't simply relay on the GC mechanism, since we currently use ActivityGroup, which
         * keeps track of all activities.
         * The only thing is done right now, is to kill all map activities, when the core activity gets killed
         * It is strange though, although a new activity is launched for every map object, all
         * map objects have sunchronized views
         */
        LocalActivityManager mng = ((ActivityGroup) cx).getLocalActivityManager();
        mng.startActivity(currentID, new Intent(MAP_ACTIVITY));
        Activity cma = mng.getActivity(currentID);
        LifeCycle.register(cma);
        MapView mv = new MapView(cma, System.getProperty("xm.map.apikey"));
        mv.getOverlays().add(annotations);
        return mv;
    }

    @Override
    public void willMoveToSuperview(UIView newSuperview) {
        if (newSuperview != null && !isPopulated) {
            isPopulated = true;
            annotations.doPopulate();
        }
    }

    void updateUserLocation(CLLocation newLocation) {
        userLocation.setLocation(newLocation);
        annotations.userview.setImage(UIImage.imageWithContentsOfFile(FileBridge.RESOURCEPREFIX + "bluedot"));
        annotations.doPopulate();
    }

    class AnnotationsOverlay extends ItemizedOverlay {

        private ArrayList<MKAnnotation> annotations = new ArrayList<MKAnnotation>();
        private ArrayList<MKAnnotationView> views = new ArrayList<MKAnnotationView>();
        private MKUserLocationView userview = new MKUserLocationView(userLocation, null);

        public AnnotationsOverlay(Drawable drawable) {
            super(boundCenterBottom(drawable));
        }

        @Override
        protected OverlayItem createItem(int i) {
            // ID 0 reserved for self location
            if (i == 0)
                return userview.getOverlayItem(this, showsUserLocation);

            i--;
            MKAnnotationView view = views.get(i);
            if (view == null) {
                MKAnnotation an = annotations.get(i);
                if (delegate != null)
                    view = delegate.viewForAnnotation(MKMapView.this, an);
                if (view == null)
                    view = viewForAnnotation(an);
                if (view == null)
                    view = new MKPinAnnotationView(an, null);
                views.set(i, view);
            }
            return view.createOverlayItem(this);
        }

        @Override
        public int size() {
            return annotations.size() + 1;
        }

        public ArrayList<MKAnnotation> getAnnotations() {
            return annotations;
        }

        public void add(MKAnnotation annotation) {
            annotations.add(annotation);
            views.add(null);    // Lazy initialization, to give time for the delegate to be defined
            doPopulate();
        }

        public void addAll(ArrayList<MKAnnotation> annotations) {
            this.annotations.addAll(annotations);
            for (int i = 0; i < annotations.size(); i++)
                views.add(null);    // Lazy initialization, to give time for the delegate to be defined
            doPopulate();
        }

        public void remove(MKAnnotation annotation) {
            int which = annotations.indexOf(annotation);
            if (which >= 0) {
                annotations.remove(which);
                views.remove(which);
            }
            doPopulate();
        }

        public void removeAll(ArrayList<MKAnnotation> annotations) {
            int which;
            for (MKAnnotation an : annotations) {
                which = this.annotations.indexOf(an);
                if (which >= 0) {
                    this.annotations.remove(which);
                    views.remove(which);
                }
            }
            doPopulate();
        }

        @Override
        /* Remove shadow on all items */
        public void draw(Canvas c, MapView m, boolean shadow) {
            if (shadow)
                return;
            super.draw(c, m, false);
        }

        @Override
        protected boolean onTap(int index) {
            // ID 0 reserved for self location
            if (index == 0)
                return userview.onTap();
            index--;
            return views.get(index).onTap();
        }

        void doPopulate() {
            if (isPopulated)
                synchronized (population_lock) {
                    populate();
                }
        }
    }
}
