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

public class NSDate extends NSObject {

    private static final double MILLENIUMDIFF = 978307200;
    private final double seconds;

    public static NSDate date() {
        return new NSDate(System.currentTimeMillis() / 1000d);
    }

    public static NSDate dateWithTimeIntervalSince1970(double seconds) {
        return new NSDate(seconds);
    }

    public static NSDate dateWithTimeIntervalSinceReferenceDate(double seconds) {
        return new NSDate(seconds + MILLENIUMDIFF);
    }

    private NSDate(double seconds) {
        this.seconds = seconds;
    }

    public double timeIntervalSinceReferenceDate() {
        return seconds;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof NSDate))
            return false;
        return ((NSDate) o).seconds == seconds;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (int) (Double.doubleToLongBits(this.seconds) ^ (Double.doubleToLongBits(this.seconds) >>> 32));
        return hash;
    }
}
