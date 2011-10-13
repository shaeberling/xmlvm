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

import org.crossmobile.ios2a.FileBridge;
import org.crossmobile.ios2a.ImplementationError;

public class NSBundle extends NSObject {

    private static NSBundle mainBundle = new NSBundle();

    private NSBundle() {
    }

    public static NSBundle mainBundle() {
        return mainBundle;
    }

    public String pathForResource(String resource, String type, String directory) {
        if (directory == null || directory.length() == 0)
            directory = "";
        else if (!directory.endsWith("/"))
            directory += "/";
        if (type != null)
            resource += "." + type;
        return directory.startsWith("/") ? directory + resource : FileBridge.BUNDLEPREFIX + "/" + directory + resource;
    }

    public String pathForResource(String resource, String type) {
        return pathForResource(resource, type, null);
    }

    public String bundlePath() {
        return FileBridge.BUNDLEPREFIX;
    }

    public String localizedStringForKey(String key, String value, String table) {
        throw new ImplementationError();
    }
}
