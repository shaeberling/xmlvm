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

public class CLHeading extends NSObject {

    private double magneticHeading;
    private double trueHeading;
    private double headingAccuracy;
    private NSDate timestamp;
    private double x;
    private double y;
    private double z;

    private CLHeading() {
    }

    public double getHeadingAccuracy() {
        return headingAccuracy;
    }

    public double getMagneticHeading() {
        return magneticHeading;
    }

    public NSDate getTimestamp() {
        return timestamp;
    }

    public double getTrueHeading() {
        return trueHeading;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public String description() {
        return toString();
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append("magneticHeading ").append(magneticHeading);
        out.append(" trueHeading ").append(trueHeading);
        out.append(" accuracy ").append(headingAccuracy);
        out.append(" x ").append(x);
        out.append(" y ").append(y);
        out.append(" z ").append(z);
        out.append(" @ ").append(timestamp.toString());
        return out.toString();
    }
}
