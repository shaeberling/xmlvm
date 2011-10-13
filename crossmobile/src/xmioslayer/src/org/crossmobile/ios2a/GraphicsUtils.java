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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.shapes.RectShape;
import org.crossmobile.ios2a.GraphicsUtils.SegmentShape.Location;

public class GraphicsUtils {

    public static class SegmentShape extends RectShape {

        private Location loc;

        public SegmentShape() {
        }

        public void updateShape(Location loc) {
            this.loc = loc;
        }

        @Override
        public void draw(Canvas canvas, Paint paint) {
            Path path = new Path();
            float capwidth = Math.min(12, rect().width());
            float restwidth = rect().width() - capwidth;
            float height = rect().height();
            float capx = 0.7f;
            float capy = 0.7f;
            switch (loc) {
                case RIGHT:
                    path.lineTo(restwidth, 0);
                    path.quadTo(restwidth + capwidth * capx, height * (1 - capy) / 2, restwidth + capwidth, height / 2);
                    path.quadTo(restwidth + capwidth * capy, height * (1 + capy) / 2, restwidth, height);
                    path.lineTo(0, height);
                    path.lineTo(0, 0);
                    canvas.drawPath(path, paint);
                    break;
                case LEFT:
                    path.moveTo(capwidth, 0);
                    path.lineTo(capwidth + restwidth, 0);
                    path.lineTo(capwidth + restwidth, height);
                    path.lineTo(capwidth, height);
                    path.quadTo((1 - capx) * capwidth, height * (1 + capy) / 2, 0, height / 2);
                    path.quadTo((1 - capx) * capwidth, height * (1 - capy) / 2, capwidth, 0);
                    canvas.drawPath(path, paint);
                    break;
                default:
                    canvas.drawRect(rect(), paint);
            }
        }

        public enum Location {

            LEFT, CENTER, RIGHT;
        }
    }

    public static Bitmap getTintedBitmap(Bitmap bm, float r, float g, float b) {
        Bitmap out = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        int color = 0xFF000000 + (int) (0xFF0000 * r) + (int) (0xFF00 * g) + (int) (0xFF * b);
        Paint p = new Paint(color);
        p.setColorFilter(new LightingColorFilter(color, 0));
        Canvas c = new Canvas(out);
        c.drawBitmap(bm, 0, 0, p);
        return out;
    }

    public static int getTintedColor(int val, float r, float g, float b) {
        int red = (int) (r * ((val & 0xff0000) >> 16));
        int green = (int) (g * ((val & 0xff00) >> 8));
        int blue = (int) (b * (val & 0xff));
        if (red > 255)
            red = 255;
        if (green > 255)
            green = 255;
        if (blue > 255)
            blue = 255;
        return (val & 0xff000000) | red << 16 | green << 8 | blue;
    }

    public static class TintedDrawable extends GradientDrawable {

        public TintedDrawable(int baseColor) {
            super(GradientDrawable.Orientation.BOTTOM_TOP, tintColors(baseColor));
        }

        private static int[] tintColors(int base) {
            float hsv[] = new float[3];
            Color.colorToHSV(base, hsv);
            int alpha = (base & 0xFF000000) >> 24;

            int[] colors = new int[3];
            colors[1] = base;
            hsv[2] *= 0.7f;
            colors[0] = Color.HSVToColor(alpha, hsv);
            hsv[2] *= 1.7f; // Also redo previous lighten
            colors[2] = Color.HSVToColor(alpha, hsv);
            return colors;
        }
    }

    /**
     * This part of the code is a companion of Yuri kanivets' android wheel project
     * http://code.google.com/p/android-wheel/
     * With this file it is possible to eliminate the resource files and
     * perform the the drawables in code, instead of in XML.
     * 
     * The code here is an exact copy of the drawables found in the
     * android-wheel project
     */
    public static Drawable getWheelCenterDrawable() {
        if (wheelcenter == null) {
            wheelcenter = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{0x70222222, 0x70222222, 0x70EEEEEE});
            wheelcenter.setStroke(1, 0x70333333);
        }
        return wheelcenter;
    }
    private static GradientDrawable wheelcenter = null;

    public static Drawable getWheelBackgroundDrawable() {
        if (wheelback == null) {
            GradientDrawable l1 = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{0xff333333, 0xffdddddd, 0xff333333});
            l1.setStroke(1, 0xFF333333);
            GradientDrawable l2 = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{0xffaaaaaa, 0xffffffff, 0xffaaaaaa});
            wheelback = new LayerDrawable(new Drawable[]{l1, l2});
        }
        return wheelback;
    }
    private static LayerDrawable wheelback = null;
}
