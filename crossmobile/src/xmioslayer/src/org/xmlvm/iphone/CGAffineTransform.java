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

import android.graphics.Matrix;
import org.crossmobile.ios2a.IOSView;

public class CGAffineTransform extends NSObject {

    private Matrix m;

    private CGAffineTransform() {
        this.m = new Matrix();
    }

    private CGAffineTransform(Matrix m) {
        this.m = new Matrix(m);
    }

    public static CGAffineTransform make(float a, float b, float c, float d, float tx, float ty) {
        CGAffineTransform res = new CGAffineTransform();
        res.m.setValues(new float[]{a, b, 0, c, d, 0, IOSView.x2Android(tx), IOSView.y2Android(ty), 1});
        return res;
    }

    public static CGAffineTransform makeRotation(float alpha) {
        CGAffineTransform res = new CGAffineTransform();
        res.m.setRotate((float) (alpha * 180 / Math.PI));
        return res;
    }

    public static CGAffineTransform makeScale(float sx, float sy) {
        CGAffineTransform res = new CGAffineTransform();
        res.m.setScale(sx, sy);
        return res;
    }

    public static CGAffineTransform makeTranslation(float tx, float ty) {
        CGAffineTransform res = new CGAffineTransform();
        res.m.setTranslate(IOSView.x2Android(tx), IOSView.y2Android(ty));
        return res;
    }

    public static CGAffineTransform rotate(CGAffineTransform transf, float alpha) {
        CGAffineTransform res = new CGAffineTransform(transf.m);
        res.m.postRotate((float) (alpha * 180 / Math.PI));
        return res;
    }

    public static CGAffineTransform scale(CGAffineTransform transf, float sx, float sy) {
        CGAffineTransform res = new CGAffineTransform(transf.m);
        res.m.postScale(sx, sy);
        return res;
    }

    public static CGAffineTransform translate(CGAffineTransform transf, float tx, float ty) {
        CGAffineTransform res = new CGAffineTransform(transf.m);
        res.m.postTranslate(IOSView.x2Android(tx), IOSView.y2Android(ty));
        return res;
    }

    public static CGAffineTransform concat(CGAffineTransform transf1, CGAffineTransform transf2) {
        CGAffineTransform res = new CGAffineTransform(transf1.m);
        res.m.postConcat(transf2.m);
        return res;
    }

    public static CGAffineTransform identity() {
        return new CGAffineTransform();
    }

    @Override
    public String toString() {
        return m.toShortString();
    }

    public static CGAffineTransform xm_new(Matrix m) {
        return new CGAffineTransform(m);
    }

    /**
     *
     * @return a copy of the matrix. This is a safe operation since most Android
     * objects tend to change the same matrix.
     */
    public Matrix xm_matrixclone() {
        return new Matrix(m);
    }
}
