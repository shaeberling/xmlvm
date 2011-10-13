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

import android.util.Log;

public final class ImplementationError extends UnsupportedOperationException {

    public ImplementationError() {
        super("Not implemented: " + getLastElementInfo());
    }

    // Static methods do not appear in the call stack yet!
    private static String getLastElementInfo() {
        String loc = "<unknown>";
        try {
            throw new RuntimeException();
        } catch (RuntimeException e) {
            StackTraceElement[] elements = e.getStackTrace();
            loc = elements[0].toString();
        }
        return loc;
    }

    public static void warn() {
        try {
            throw new RuntimeException();
        } catch (RuntimeException e) {
            StackTraceElement[] elements = e.getStackTrace();
            Log.e("NSLog", "Implementation missing: " + elements[0]);
            Log.e("NSLog", "        requested from: " + elements[1]);
        }
    }
}
