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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import org.crossmobile.ios2a.FileBridge;
import org.crossmobile.ios2a.ImplementationError;

public class NSString extends NSObject {

    private NSString() {
    }

    public static NSData dataUsingEncoding(String string, int NSStringEncoding) {
        try {
            if (string == null)
                return null;
            return new NSData(string.getBytes(org.xmlvm.iphone.NSStringEncoding.convertIntToString(NSStringEncoding)));
        } catch (UnsupportedEncodingException ex) {
        }
        return null;
    }

    public static String stringWithContentsOfURL(NSURL url, int NSStringEncoding) {
        if (url == null)
            return null;
        return stringWithContentsOfURL(url.getModel(), org.xmlvm.iphone.NSStringEncoding.convertIntToString(NSStringEncoding));
    }

    public static String stringWithContentsOfURL(NSURL url) {
        if (url == null)
            return null;
        return stringWithContentsOfURL(url.getModel(), "UTF-8");
    }

    private static String stringWithContentsOfURL(URL url, String encoding) {
        try {
            return stringWithContentsOfReader(new InputStreamReader(url.openStream(), encoding));
        } catch (IOException ex) {
            return null;
        }
    }

    public static String stringWithContentsOfFile(String path) {
        if (path != null)
            try {
                return stringWithContentsOfReader(new InputStreamReader(FileBridge.getInputFileStream(path), "UTF-8"));
            } catch (Exception ex) {
            }
        return null;
    }

    private static String stringWithContentsOfReader(Reader reader) {
        BufferedReader in = null;
        StringBuilder out = new StringBuilder();
        try {
            char[] buffer = new char[1000];
            int howmany;
            in = new BufferedReader(reader);
            while ((howmany = in.read(buffer)) > 0)
                out.append(buffer, 0, howmany);
            in.close();
        } catch (Exception e) {
            return null;
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException ex) {
                }
        }
        return out.toString();
    }

    public static String stringByAddingPercentEscapesUsingEncoding(String URL, int NSStringEncoding) {
        if (URL == null)
            return null;
        try {
            byte[] bytes = URL.getBytes(org.xmlvm.iphone.NSStringEncoding.convertIntToString(NSStringEncoding));
            StringBuilder out = new StringBuilder();
            for (byte b : bytes)
                if ((b >= 'a' && b <= 'z') || (b >= 'A' && b <= 'Z') || (b >= '0' && b <= '9'))
                    out.append((char) b);
                else
                    switch (b) {
                        case '!':
                        case '*':
                        case '\'':
                        case '(':
                        case ')':
                        case ';':
                        case ':':
                        case '@':
                        case '&':
                        case '=':
                        case '+':
                        case '$':
                        case ',':
                        case '/':
                        case '?':
                        case '#':
                        case '[':
                        case ']':
                        case '-':
                        case '_':
                        case '.':
                        case '~':
                            out.append((char) b);
                            break;
                        default:
                            out.append("%").append(hex((b & 0xf0) >> 4)).append(hex(b & 0xf));
                    }
            return out.toString();
        } catch (UnsupportedEncodingException ex) {
        }
        return null;
    }

    private static char hex(int num) {
        return num > 9 ? (char) (num + 55) : (char) (num + '0');
    }

    public static ArrayList<String> componentsSeparatedByString(String stringtodivide, String separator) {
        return new ArrayList<String>(Arrays.asList(stringtodivide.split(separator)));
    }

    public static String initWithData(NSData data, int NSStringEncoding) {
        try {
            return new String(data.getBytes(), org.xmlvm.iphone.NSStringEncoding.convertIntToString(NSStringEncoding));
        } catch (UnsupportedEncodingException ex) {
        }
        return null;
    }

    public static void drawAtPoint(String texttodisplay, CGPoint point, UIFont font) {
        throw new ImplementationError();
    }

    public static CGSize sizeWithFont(String text, UIFont font) {
        throw new ImplementationError();
    }

    public static CGSize sizeWithFont(String text, UIFont font, CGSize size, int lineBreakMode) {
        throw new ImplementationError();
    }

    public static boolean writeToFile(String content, String path, boolean atomically, int NSStringEncoding) {
        // TODO: take care of encoding and atomic write
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(path));
            out.write(content);
            return true;
        } catch (IOException ex) {
            return false;
        } finally {
            if (out != null)
                try {
                    out.close();
                } catch (IOException ex) {
                }
        }
    }
}
