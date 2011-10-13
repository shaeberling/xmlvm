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

import static org.crossmobile.ios2a.FileBridge.DEFAULTPATHS;
import java.util.ArrayList;
import org.crossmobile.ios2a.FileBridge;

public class Foundation {

    public static ArrayList<String> NSSearchPathForDirectoriesInDomains(int NSSearchPathDirectory, int NSSearchPathDomainMask, boolean expandTilde) {
        ArrayList<String> res = new ArrayList<String>();
        if (NSSearchPathDirectory >= 0 && NSSearchPathDirectory < DEFAULTPATHS.length && DEFAULTPATHS[NSSearchPathDirectory] != null)
            res.add(NSHomeDirectory() + DEFAULTPATHS[NSSearchPathDirectory]);
        return res;
    }

    public static String NSTemporaryDirectory() {
        return FileBridge.getTemporaryLocation();
    }

    public static String NSHomeDirectory() {
        return FileBridge.getUserLocation();
    }
}
