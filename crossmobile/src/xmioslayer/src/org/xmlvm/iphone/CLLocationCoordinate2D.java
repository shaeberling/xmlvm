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

import com.google.android.maps.GeoPoint;

public class CLLocationCoordinate2D extends NSObject {

    public double latitude;
    public double longitude;

    public CLLocationCoordinate2D(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "CLLocationCoordinate2D{" + "latitude=" + latitude + ",longitude=" + longitude + '}';
    }

    double distanceFromCoordinates(CLLocationCoordinate2D other) {
        double R = 6371000; // m
        double dLat = Math.toRadians(other.latitude - latitude);
        double dLon = Math.toRadians(other.longitude - longitude);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(other.latitude))
                * Math.cos(Math.toRadians(latitude)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        return d;
    }

    GeoPoint getGeoPoint() {
        return new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));
    }
}
