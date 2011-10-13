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

import android.location.Location;

public class CLLocation extends NSObject {

    private final CLLocationCoordinate2D coordinate;
    private final double altitude;
    private final double horizontalAccuracy;
    private final double verticalAccuracy;
    private final NSDate timestamp;
    private final double speed;
    private final double course;

    public CLLocation(double latitude, double longitude) {
        this(new CLLocationCoordinate2D(latitude, longitude), 0, 0, -1, 0, 0, NSDate.date());
    }

    public CLLocation(CLLocationCoordinate2D coordinate, double altitude, double horizontalAccuracy, double verticalAccuracy, NSDate timestamp) {
        this(coordinate, altitude, horizontalAccuracy, verticalAccuracy, 0, 0, timestamp);
    }

    public CLLocation(CLLocationCoordinate2D coordinate, double altitude, double horizontalAccuracy, double verticalAccuracy, double course, double speed, NSDate timestamp) {
        this.coordinate = coordinate;
        this.altitude = altitude;
        this.horizontalAccuracy = horizontalAccuracy;
        this.verticalAccuracy = verticalAccuracy;
        this.timestamp = timestamp;
        this.speed = speed;
        this.course = course;
    }

    CLLocation(Location lctn) {
        this(new CLLocationCoordinate2D(lctn.getLatitude(), lctn.getLongitude()),
                lctn.getAltitude(), lctn.getAccuracy(), -1, lctn.getBearing(), lctn.getSpeed(),
                NSDate.dateWithTimeIntervalSince1970(lctn.getTime() / 1000d));
    }

    public double getAltitude() {
        return altitude;
    }

    public CLLocationCoordinate2D getCoordinate() {
        return coordinate;
    }

    public double getCourse() {
        return course;
    }

    public double getHorizontalAccuracy() {
        return horizontalAccuracy;
    }

    public double getSpeed() {
        return speed;
    }

    public NSDate getTimestamp() {
        return timestamp;
    }

    public double getVerticalAccuracy() {
        return verticalAccuracy;
    }

    public double distanceFromLocation(CLLocation location) {
        return coordinate.distanceFromCoordinates(location.coordinate);
    }

    @Override
    public String toString() {
        return "CLLocation{" + "coordinate=" + coordinate + " alt=" + altitude + " timestamp=" + timestamp + '}';
    }
}
