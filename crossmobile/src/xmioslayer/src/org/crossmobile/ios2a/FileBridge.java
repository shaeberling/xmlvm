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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.xmlvm.iphone.UIScreen;
import org.xmlvm.iphone.UIView;

public class FileBridge {

    public static final String RESOURCEPREFIX = "android.resource://";
    private static final int RP_length = RESOURCEPREFIX.length();
    public static final String BUNDLEPREFIX = "file:///android_asset";
    private static final int BP_length = BUNDLEPREFIX.length() + 1;// Take care of the "/" character at the end of the pathname
    //
    // Should not directly use these variables, since it is important to create some directories first.
    // Use instead the getTemporaryLocation() and getUserLocation() instead
    private static final File userlocation = new File(MainActivity.current.getFilesDir().getPath());
    private static final File templocation = new File(userlocation, "/temp");
    //
    public static final String[] DEFAULTPATHS = {
        null,
        "/Applications",
        "/Applications/Demos",
        "/Developer/Applications",
        "/Applications/Utilities",
        "/Library",
        "/Developer",
        null,
        "/Library/Documentation",
        "/Documents",
        null,
        "/Library/Autosave Information",
        "/Desktop",
        "/Library/Caches",
        "/Library/Application Support",
        "/Downloads",
        "/Library/Input Methods",
        "/Movies",
        "/Music",
        "/Pictures",
        null,
        "/Public",
        "/Library/PreferencePanes"};

    public static boolean isInReadonlyFilesystem(String name) {
        return name.startsWith(RESOURCEPREFIX) || name.startsWith(BUNDLEPREFIX);
    }

    public static Bitmap loadBitmap(String name) {

        String hires;
        String lowres;
        String lowcase = name.toLowerCase();
        boolean isHires = UIScreen.mainScreen().getScale() > 1.5f;
        boolean isResource = lowcase.startsWith(RESOURCEPREFIX);
        boolean isInBoundle = lowcase.startsWith(BUNDLEPREFIX);


        if ((lowcase.endsWith(".jpg") || lowcase.endsWith(".jpeg") || lowcase.endsWith(".gif"))) {
            lowres = name;
            hires = name;
        } else if (lowcase.endsWith(".png")) {
            lowres = name;
            hires = isHires ? name.substring(0, name.length() - 4) + "@2x.png" : lowres;
        } else if (isResource) {
            lowres = name;
            hires = isHires ? name + "_2x" : lowres;
        } else {
            lowres = name + ".png";
            hires = isHires ? name + "@2x.png" : lowres;
        }
        Bitmap result = null;
        if (isResource) {
            try {
                result = ((BitmapDrawable) getDrawable(hires.substring(RP_length))).getBitmap();
            } catch (Exception ex) {
            }
            if (result == null)
                try {
                    result = ((BitmapDrawable) getDrawable(lowres.substring(RP_length))).getBitmap();
                } catch (Exception ex) {
                }
        } else if (isInBoundle) {
            try {
                result = BitmapFactory.decodeStream(MainActivity.current.getAssets().open(hires.substring(BP_length)));
            } catch (Exception ex) {
            }
            if (result == null)
                try {
                    result = BitmapFactory.decodeStream(MainActivity.current.getAssets().open(lowres.substring(BP_length)));
                } catch (Exception ex) {
                }
        } else {
            try {
                result = BitmapFactory.decodeFile(hires);
            } catch (Exception ex) {
            }
            if (result == null)
                try {
                    result = BitmapFactory.decodeFile(lowres);
                } catch (Exception ex) {
                }
        }
        return result;
    }

    public static InputStream getInputFileStream(String name) {
        try {
            if (name.startsWith(BUNDLEPREFIX))
                return MainActivity.current.getAssets().open(name.substring(BP_length));
            else
                return new FileInputStream(name);
        } catch (Exception ex) {
        }
        return null;
    }

    private static int getID(String type, String item) {
        Class clazz;
        try {
            clazz = Class.forName(MainActivity.current.getPackageName() + ".R$" + type);
        } catch (ClassNotFoundException ex) {
            System.err.println("Resource directory not found: " + type);
            return 0;
        }

        try {
            return ((Integer) clazz.getField(item).get(null)).intValue();
        } catch (Exception ex) {
            System.err.println("Resource item not found: " + type + "/" + item);
            return 0;
        }
    }

    public static Drawable getDrawable(String name) {
        int id = getID("drawable", name);
        return id == 0 ? null : MainActivity.current.getResources().getDrawable(id);
    }

    public static View getLayout(String name) {
        int id = getID("layout", name);
        return MainActivity.current.getLayoutInflater().inflate(id, new UIView().xm_base(), false);
    }

    public static void deleteRecursive(File f) {
        if (f.exists()) {
            File[] list = f.listFiles();
            if (list != null)
                for (File c : list)
                    deleteRecursive(c);
            f.delete();
        }
    }

    public static boolean copyStreams(InputStream in, OutputStream out) {
        try {
            byte[] buffer = new byte[1024];
            int count;
            while ((count = in.read(buffer)) > 0)
                out.write(buffer, 0, count);
            return true;
        } catch (IOException ex) {
            return false;
        } finally {
            try {
                if (out != null)
                    out.flush();
            } catch (IOException ex) {
            }
            try {
                if (out != null)
                    out.close();
            } catch (IOException ex) {
            }
            try {
                if (in != null)
                    in.close();
            } catch (IOException ex) {
            }
        }
    }

    public static String getTemporaryLocation() {
        templocation.mkdirs();
        return templocation.getAbsolutePath();
    }

    public static void cleanTemporaryLocation() {
        deleteRecursive(templocation);
    }

    public static String getUserLocation() {
        new File(userlocation + DEFAULTPATHS[org.xmlvm.iphone.NSSearchPathDirectory.Document]).mkdirs();
        new File(userlocation + DEFAULTPATHS[org.xmlvm.iphone.NSSearchPathDirectory.Caches]).mkdirs();
        return userlocation.getAbsolutePath();
    }
}
