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

import java.io.FileNotFoundException;
import java.io.IOException;
import org.crossmobile.ios2a.ImplementationError;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Random;
import org.crossmobile.ios2a.FileBridge;

/**
 * UIImage does not hold a reference of a Bitmap dataset. For this reason is a
 * list of weak references of images which can be loaded at any time from the
 * file system. In case a drawable is presented as a source for the image, then
 * a copy of the bitmap is saved to a cached directory 
 */
public class UIImage extends NSObject {

    private static final String TEMPIMAGEPREFIX = "/.uiimage.bitmaps/";
    private static final HashMap<String, WeakReference<Bitmap>> cache = new HashMap<String, WeakReference<Bitmap>>();
    private static final Random random = new Random(System.currentTimeMillis());
    //
    private String path;

    /* @param path the path of this image - the image should already be there
     * @param bm the bitmap in case it exists already
     * @param disposable if no copy of the bitmap should be kept (i.e. is candidate for recycle
     * @throws IOException  if the image is not found
     */
    UIImage(String path, Bitmap bm, boolean disposable) throws IOException {
        this.path = path;
        if (FileBridge.isInReadonlyFilesystem(path) && cache.containsKey(path)) // The file was attempted to loaded before and it is safe in the readonly filesystem
            return;
        if (bm == null)
            bm = FileBridge.loadBitmap(path);
        if (bm == null)
            throw new IOException("Unable to load bitmap");
        else
            cache.put(path, new WeakReference<Bitmap>(disposable ? null : bm));
    }

    static UIImage imageFromBitmap(Bitmap bm, boolean disposable) {
        if (bm == null)
            return null;
        String fname = getRandomFilename();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(fname);
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);   // save image
            return new UIImage(fname, bm, disposable);
        } catch (IOException ex) {
            NSLog.log(ex);
            return null;
        } finally {
            if (out != null)
                try {
                    out.close();
                } catch (IOException ex) {
                }
        }
    }

    public static UIImage imageNamed(String filename) {
        return imageWithContentsOfFile(FileBridge.BUNDLEPREFIX + "/" + filename);   // These images are always cached!
    }

    public static UIImage imageWithData(NSData data) {
        String fname = getRandomFilename();
        data.writeToFile(fname, false);
        return imageWithContentsOfFile(fname);
    }

    public static UIImage imageWithContentsOfFile(String filename) {
        try {
            return new UIImage(filename, null, false);
        } catch (IOException ex) {
            return null;
        }
    }

    public UIImage stretchableImage(int leftCapWidth, int topCapHeight) {
        // TODO REQ: stretchableImage
        return this;
    }

    private static String getRandomFilename() {
        new File(Foundation.NSTemporaryDirectory() + TEMPIMAGEPREFIX).mkdirs();
        return Foundation.NSTemporaryDirectory() + TEMPIMAGEPREFIX + Integer.toHexString(random.nextInt());
    }

    public CGImage getCGImage() {
        return new CGImage(getModel().getBitmap());
    }

    public void drawInRect(CGRect rect) {
        throw new ImplementationError();
    }

    public void drawAtPoint(CGPoint point) {
        throw new ImplementationError();
    }

    public CGSize getSize() {
        Bitmap bm = getModel().getBitmap();
        return new CGSize(bm.getWidth(), bm.getHeight());
    }

    public UIImage cropImage(int x, int y, int width, int height) {
        throw new ImplementationError();
    }

    public NSData PNGRepresentation() {
        if (path.startsWith(Foundation.NSTemporaryDirectory() + TEMPIMAGEPREFIX)) {
            ByteArrayOutputStream out = null;
            try {
                out = new ByteArrayOutputStream();
                FileBridge.copyStreams(new FileInputStream(path), out);
                return new NSData(out.toByteArray());
            } catch (FileNotFoundException ex) {
                throw new NullPointerException("Unable to retrieve active UIImage " + path);
            } finally {
                if (out != null)
                    try {
                        out.close();
                    } catch (IOException ex) {
                    }
            }
        }
        return getImageRepresentation(Bitmap.CompressFormat.PNG, 1);
    }

    public NSData JPEGRepresentation(float compressionQuality) {
        return getImageRepresentation(Bitmap.CompressFormat.JPEG, compressionQuality);
    }

    private NSData getImageRepresentation(Bitmap.CompressFormat compress, float quality) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        getModel().getBitmap().compress(compress, (int) (quality * 100), out);
        NSData result = new NSData(out.toByteArray());
        try {
            out.close();
        } catch (IOException ex) {
        }
        return result;
    }

    BitmapDrawable getModel() {
        Bitmap bm = cache.get(path).get();
        if (bm == null) {
            bm = FileBridge.loadBitmap(path);
            if (bm == null)
                throw new NullPointerException("Unable to retrieve active UIImage " + path);
            cache.put(path, new WeakReference<Bitmap>(bm));
        }
        return new BitmapDrawable(bm);
    }
}
