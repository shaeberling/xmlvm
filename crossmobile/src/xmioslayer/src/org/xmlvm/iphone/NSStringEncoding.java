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

public final class NSStringEncoding {

    public static final int ASCII = 1;
    public static final int NEXTSTEP = 2;
    public static final int JapaneseEUC = 3;
    public static final int UTF8 = 4;
    public static final int ISOLatin1 = 5;
    public static final int Symbol = 6;
    public static final int NonLossyASCII = 7;
    public static final int ShiftJIS = 8;
    public static final int ISOLatin2 = 9;
    public static final int Unicode = 10;
    public static final int WindowsCP1251 = 11;
    public static final int WindowsCP1252 = 12;
    public static final int WindowsCP1253 = 13;
    public static final int WindowsCP1254 = 14;
    public static final int WindowsCP1250 = 15;
    public static final int ISO2022JP = 21;
    public static final int MacOSRoman = 30;
    public static final int UTF16 = Unicode;
    public static final int UTF16BigEndian = 0x90000100;
    public static final int UTF16LittleEndian = 0x94000100;
    public static final int UTF32 = 0x8c000100;
    public static final int UTF32BigEndian = 0x98000100;
    public static final int UTF32LittleEndian = 0x9c000100;

    private NSStringEncoding() {
    }

    static String convertIntToString(int value) {
        switch (value) {
            case ASCII:
                return "US-ASCII";
            case NEXTSTEP:
                return null;
            case JapaneseEUC:
                return "EUC-JP";
            case UTF8:
                return "UTF-8";
            case ISOLatin1:
                return "ISO-8859-1";
            case Symbol:
                return "x-MacSymbol";
            case NonLossyASCII:
                return "US-ASCII";
            case ShiftJIS:
                return "Shift_JIS";
            case ISOLatin2:
                return "ISO-8859-2";
            case Unicode:
                return "UTF-16";
            case WindowsCP1251:
                return "windows-1251";
            case WindowsCP1252:
                return "windows-1252";
            case WindowsCP1253:
                return "windows-1253";
            case WindowsCP1254:
                return "windows-1254";
            case WindowsCP1250:
                return "windows-1250";
            case ISO2022JP:
                return "ISO-2022-JP";
            case MacOSRoman:
                return "x-MacRoman";
            case UTF16BigEndian:
                return "UTF-16BE";
            case UTF16LittleEndian:
                return "UTF-16LE";
            case UTF32:
                return null;
            case UTF32BigEndian:
                return null;
            case UTF32LittleEndian:
                return null;
            default:
                return "UTF-8";
        }
    }
}
