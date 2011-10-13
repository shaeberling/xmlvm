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

import android.graphics.Typeface;

public class UIFont extends NSObject {

    private final Typeface font;
    private final float size;
    private final String familyname;
    private final String fontname;

    private UIFont(String name, float fontsize) {
        fontname = name;
        int type = Typeface.NORMAL;
        name = name == null ? "" : name;
        String lower = name.toLowerCase();
        if (lower.indexOf("bold") >= 0) {
            type |= Typeface.BOLD;
            name = name.replaceAll("[bB][oO][lL][dD]", "");
        }
        if (lower.indexOf("italic") >= 0) {
            type |= Typeface.ITALIC;
            name = name.replaceAll("[iI][tT][aA][lL][iI][cC]", "");
        }
        name = name.replace("  ", " ");
        name = name.trim();
        familyname = name;

        font = Typeface.create(name, type);
        size = fontsize;
    }

    UIFont(Typeface tf, float fontsize) {
        font = tf;
        size = fontsize;
        familyname = tf.toString().replaceAll("[bB][oO][lL][dD]", "").replaceAll("[iI][tT][aA][lL][iI][cC]", "");
        fontname = (familyname + " " + (tf.isBold() ? "Bold" : "") + (tf.isItalic() ? "Italic" : "")).trim();
    }

    public static UIFont systemFontOfSize(float fontSize) {
        return new UIFont("Arial", fontSize);
    }

    public static UIFont boldSystemFontOfSize(float fontSize) {
        return new UIFont("Arial bold", fontSize);
    }

    public static UIFont italicSystemFontOfSize(float fontSize) {
        return new UIFont("Arial italic", fontSize);
    }

    public UIFont fontWithSize(float fontSize) {
        return new UIFont(fontname, Math.round(fontSize));
    }

    public static UIFont fontWithNameSize(String name, float fontsize) {
        return new UIFont(name, fontsize);
    }

    public static float buttonFontSize() {
        return 18.0f;
    }

    public static float labelFontSize() {
        return 17.0f;
    }

    public String familyName() {
        return familyname;
    }

    public String fontName() {
        return fontname;
    }

    public float pointSize() {
        return size;
    }

    Typeface __Typeface() {
        return font;
    }

    float __AndroidSize() {
        return size;
    }
}
