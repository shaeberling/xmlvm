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

package org.crossmobile.source.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

public final class FileUtils {

    private final static String NL = System.getProperty("line.separator");

    public static String getReader(InputStream input) {
        BufferedReader in = null;
        StringBuilder out = new StringBuilder();
        try {
            in = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                out.append(line);
                out.append(NL);
            }
            return out.toString();
        } catch (IOException ex) {
            System.err.println(ex.toString());
            return null;
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException ex) {
                }
        }
    }

    public static String getFile(String filename) {
        try {
            return getReader(new FileInputStream(filename));
        } catch (FileNotFoundException ex) {
            System.err.println(ex.toString());
            return null;
        }
    }

    public static boolean delete(File current) {
        boolean status = true;
        if (current.isDirectory())
            for (File sub : current.listFiles())
                status &= delete(sub);
        if (current.exists() && (!current.delete()))
            return false;
        return status;
    }

    public static void putFile(File file, WriteCallBack<Writer> callBack) {
        file.getParentFile().mkdirs();
        Writer out = null;
        try {
            out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            callBack.exec(out);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            if (out != null)
                try {
                    out.close();
                } catch (IOException ex) {
                }
        }
        if (file.length() == 0)
            file.delete();
    }
}
