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
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import java.util.Set;
import org.crossmobile.ios2a.ImplementationError;
import org.crossmobile.ios2a.MainActivity;
import org.crossmobile.ios2a.LifeCycle;

public class CLLocationManager extends NSObject {

    private static final LocationManager manager = (LocationManager) MainActivity.current.getSystemService(Context.LOCATION_SERVICE);
    private static final long MINUPDATE = 100;
    private static CLLocation backup_location;
    //
    private CLLocationManagerDelegate delegate;
    private double distanceFilter = CLDistanceFilter.None;
    private double desiredAccuracy = CLLocationAccuracy.Best;
    private double headingFilter = CLHeadingFilter.None;
    private int headingOrientation;
    private Set<CLRegion> monitoredRegions;
    private double maximumRegionMonitoringDistance = -1;
    private CLHeading heading;
    private String purpose;
    private CLLocation location;
    //
    private IOSLocationListener fine;
    private IOSLocationListener significant;

    private class IOSLocationListener implements LocationListener {

        @SuppressWarnings("LeakingThisInConstructor")
        public IOSLocationListener() {
            LifeCycle.register(this);
        }

        @Override
        public void onLocationChanged(Location lctn) {
            if (lctn != null) {
                CLLocation newlocation = new CLLocation(lctn);
                CLLocation oldlocation = location;
                location = newlocation;
                backup_location = location;
                delegate.didUpdateToLocation(CLLocationManager.this, newlocation, oldlocation);
            }
        }

        @Override
        public void onStatusChanged(String string, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String string) {
        }

        @Override
        public void onProviderDisabled(String string) {
        }
    };

    public static boolean locationServicesEnabled() {
        return manager == null ? false : manager.isProviderEnabled(LocationManager.GPS_PROVIDER) | manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public static boolean significantLocationChangeMonitoringAvailable() {
        return manager == null ? false : manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public static boolean headingAvailable() {
        throw new ImplementationError();
    }

    public static boolean regionMonitoringAvailable() {
        return locationServicesEnabled();
    }

    public static boolean regionMonitoringEnabled() {
        throw new ImplementationError();
    }

    public void startUpdatingLocation() {
        fine = startMonitoring(fine, manager.isProviderEnabled(LocationManager.GPS_PROVIDER) ? LocationManager.GPS_PROVIDER : LocationManager.NETWORK_PROVIDER);
    }

    public void stopUpdatingLocation() {
        if (fine == null)
            return;
        manager.removeUpdates(fine);
        fine = null;
    }

    public void startMonitoringSignificantLocationChanges() {
        significant = startMonitoring(significant, LocationManager.NETWORK_PROVIDER);
    }

    public void stopMonitoringSignificantLocationChanges() {
        if (significant == null)
            return;
        manager.removeUpdates(significant);
        significant = null;
    }

    private IOSLocationListener startMonitoring(IOSLocationListener old, String provider) {
        if (delegate == null)
            return old;
        if (old != null)
            return old;
        if (!manager.isProviderEnabled(provider)) {
            new UIAlertView("GPS Error", "Unable to suppor GPS location on this device with provider: \"" + provider + "\"", null, "Cancel").show();
            return old;
        }
        IOSLocationListener listener = new IOSLocationListener();
        manager.requestLocationUpdates(provider, MINUPDATE, (float) distanceFilter, listener);
        Location here_now = manager.getLastKnownLocation(provider);
        if (here_now != null) {
            location = new CLLocation(here_now);
            if (backup_location == null)
                backup_location = location;
        } else
            location = backup_location;
        if (location != null)
            delegate.didUpdateToLocation(this, location, location);
        return listener;
    }

    public void startUpdatingHeading() {
        throw new ImplementationError();
    }

    public void stopUpdatingHeading() {
        throw new ImplementationError();
    }

    public void dismissHeadingCalibrationDisplay() {
        throw new ImplementationError();
    }

    public void startMonitoringForRegion(CLRegion region, double accuracy) {
        throw new ImplementationError();
    }

    public void stopMonitoringForRegion(CLRegion region) {
        throw new ImplementationError();
    }

    public CLLocationManagerDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(CLLocationManagerDelegate delegate) {
        this.delegate = delegate;
    }

    public double getDesiredAccuracy() {
        return desiredAccuracy;
    }

    public void setDesiredAccuracy(double desiredAccuracy) {
        this.desiredAccuracy = desiredAccuracy;
    }

    public double getDistanceFilter() {
        return distanceFilter;
    }

    public void setDistanceFilter(double distanceFilter) {
        this.distanceFilter = distanceFilter;
    }

    public double getHeadingFilter() {
        return headingFilter;
    }

    public void setHeadingFilter(double headingFilter) {
        this.headingFilter = headingFilter;
    }

    public int getHeadingOrientation() {
        return headingOrientation;
    }

    public void setHeadingOrientation(int headingOrientation) {
        this.headingOrientation = headingOrientation;
    }

    public Set<CLRegion> getMonitoredRegions() {
        return monitoredRegions;
    }

    public double getMaximumRegionMonitoringDistance() {
        return maximumRegionMonitoringDistance;
    }

    public CLHeading getHeading() {
        return heading;
    }

    public CLLocation getLocation() {
        return location;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
