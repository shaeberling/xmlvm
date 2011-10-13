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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import org.crossmobile.ios2a.FileBridge;

public class NSData extends NSObject {

    private final byte[] data;

    private class DataChunk {

        private byte[] chunk = new byte[1000];
        private int size = 0;
        private DataChunk next = null;

        private DataChunk(InputStream in) {
            try {
                size = in.read(chunk);
            } catch (IOException ex) {
                size = 0;
                chunk = null;
            }
        }

        private byte[] consumeBytes() {
            DataChunk current = this;
            int total = 0;
            // Calculate data size
            while (current != null) {
                total += current.size;
                current = current.next;
            }
            if (total < 1)
                return null;

            // reconstruct array
            byte[] res = new byte[total];
            int loc = 0;
            current = this;
            while (current != null) {
                System.arraycopy(current.chunk, 0, res, loc, current.size);
                current.chunk = null;

                loc += current.size;
                current.size = 0;

                DataChunk cnext = current.next;
                current.next = null;
                current = cnext;
            }
            return res;
        }

        private boolean isValid() {
            return size > 0;
        }
    }

    NSData(byte[] data) {
        this.data = data;
    }

    NSData(InputStream inputstream) {
        DataChunk head = null;
        DataChunk queue = null;
        DataChunk current = null;
        if (inputstream != null) {
            do {
                current = new DataChunk(inputstream);
                if (current.isValid()) {
                    if (queue != null)
                        queue.next = current;
                    else
                        head = current;
                    queue = current;
                }
            } while (current.isValid());
            if (head != null)
                data = head.consumeBytes();
            else
                data = null;
        } else
            data = null;
    }

    public static NSData dataWithBytes(byte[] data, int length) {
        byte[] fdata;
        if (length >= data.length)
            fdata = data;
        else {
            fdata = new byte[length];
            System.arraycopy(data, 0, fdata, 0, length);
        }
        return new NSData(fdata);
    }

    public static NSData dataWithContentsOfFile(String path) {
        NSData data = new NSData(FileBridge.getInputFileStream(path));
        if (data.data != null)
            return data;
        return null;
    }

    public static NSData dataWithContentsOfURL(NSURL url) {
        try {
            NSData data = new NSData(url.getModel().openStream());
            if (data.data != null)
                return data;
        } catch (IOException ex) {
        }
        return null;
    }

    public boolean writeToFile(String path, boolean atomically) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            out.write(data);
            out.close();
            return true;
        } catch (IOException ex) {
            if (out != null)
                try {
                    out.close();
                } catch (IOException ex1) {
                }
        }
        return false;
    }

    public int length() {
        return data.length;
    }

    public byte[] getBytes() {
        return data;
    }

    @Override
    public String toString() {
        try {
            return new String(data, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
        }
        return new String(data);
    }
}
