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

public class CLRegion extends NSObject {

    private final CLLocationCoordinate2D center;
    private final double radius;
    private final String identifier;

    public CLRegion(CLLocationCoordinate2D center, double radius, String identifier) {
        this.center = center;
        this.radius = radius;
        this.identifier = identifier;
    }

    public CLLocationCoordinate2D getCenter() {
        return center;
    }

    public String getIdentifier() {
        return identifier;
    }

    public double getRadius() {
        return radius;
    }

    public boolean containsCoordinate(CLLocationCoordinate2D coordinate) {
        return center.distanceFromCoordinates(coordinate) < radius;
    }
}
