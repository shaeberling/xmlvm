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

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class NSFileManager extends NSObject {

    private static final NSFileManager defaultMngr = new NSFileManager();

    private NSFileManager() {
    }

    public static NSFileManager defaultManager() {
        return defaultMngr;
    }

    public boolean fileExistsAtPath(String path) {
        return new File(path).exists();
    }

    public boolean createDirectoryAtPath(String path, boolean createIntermediates,
            Map<String, String> attributes) {
        if (createIntermediates)
            return new File(path).mkdirs();
        else
            return new File(path).mkdir();
    }

    public List<String> contentsOfDirectoryAtPath(String path, NSErrorHolder error) {
        File f = new File(path);
        if (!f.exists()) {
            if (error != null)
                error.error = new NSError("AndroidFS", 1, null);
            return null;
        }
        if (!f.isDirectory()) {
            if (error != null)
                error.error = new NSError("AndroidFS", 2, null);
            return null;
        }
        List<String> files = new ArrayList<String>();
        files.addAll(Arrays.asList(new File(path).list()));
        if (error != null)
            error.error = null;
        return files;
    }
}
