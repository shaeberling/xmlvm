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

import android.graphics.Color;
import android.graphics.drawable.Drawable;

public class UIColor extends NSObject {

    public final static UIColor blackColor = new UIColor(Color.BLACK);
    public final static UIColor darkGrayColor = new UIColor(Color.DKGRAY);
    public final static UIColor lightGrayColor = new UIColor(Color.LTGRAY);
    public final static UIColor whiteColor = new UIColor(Color.WHITE);
    public final static UIColor grayColor = new UIColor(Color.GRAY);
    public final static UIColor redColor = new UIColor(Color.RED);
    public final static UIColor greenColor = new UIColor(Color.GREEN);
    public final static UIColor blueColor = new UIColor(Color.BLUE);
    public final static UIColor cyanColor = new UIColor(Color.CYAN);
    public final static UIColor yellowColor = new UIColor(Color.YELLOW);
    public final static UIColor magentaColor = new UIColor(Color.MAGENTA);
    public final static UIColor orangeColor = new UIColor(1, 0.5f, 0, 1);
    public final static UIColor purpleColor = new UIColor(0.5f, 0, 0.5f, 1);
    public final static UIColor brownColor = new UIColor(153f / 255, 102f / 255, 51f / 255, 1);
    public final static UIColor clearColor = new UIColor(Color.TRANSPARENT);
    public final static UIColor lightTextColor = new UIColor(183f / 255, 183f / 255, 183f / 255, 1);
    public final static UIColor darkTextColor = new UIColor(Color.BLACK);
    public final static UIColor groupTableViewBackgroundColor = new UIColor(197f / 255, 204f / 255, 212f / 255, 1);
    public final static UIColor viewFlipsideBackgroundColor = new UIColor(Color.BLACK);
    //
    private final Drawable draw;
    private final int color;

    UIColor(int ARGB) {
        draw = null;
        color = ARGB;
    }

    private UIColor(UIImage image) {
        draw = image.getModel();
        color = 0;
    }

    private UIColor(float red, float green, float blue, float alpha) {
        this((((int) (alpha * 255.0f)) << 24) | (((int) (red * 255.0f)) << 16) | (((int) (green * 255.0f)) << 8) | ((int) (blue * 255.0f)));
    }

    public static UIColor colorWithWhiteAlpha(float white, float alpha) {
        return new UIColor(white, white, white, alpha);
    }

    public static UIColor colorWithRGBA(float red, float green, float blue, float alpha) {
        return new UIColor(red, green, blue, alpha);
    }

    public static UIColor colorWithHSBA(float h, float s, float b, float a) {
        float hsv[] = new float[3];
        hsv[0] = h * 360;
        hsv[1] = s;
        hsv[2] = b;
        return new UIColor(Color.HSVToColor(hsv) & (((int) (a * 255)) << 24));
    }

    public static UIColor colorWithPatternImage(UIImage patternImage) {
        return new UIColor(patternImage);
    }

    Drawable getModelDrawable() {
        return draw;
    }

    int getModelColor() {
        return color;
    }
}
