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

package org.crossmobile.ios2a.transf;

import android.graphics.Matrix;
import android.view.animation.Transformation;
import org.xmlvm.iphone.CGAffineTransform;

public class RichTransformation extends Transformation {

    public static RichTransformation setAlpha(RichTransformation old, float alpha) {
        if (alpha < 0)
            alpha = 0;
        if (alpha >= 1)
            if (old == null)
                return null;
            else if (old.mMatrix.isIdentity())
                return null;
            else {
                old.mAlpha = 1;
                return old;
            }
        else {
            if (old == null)
                old = new RichTransformation();
            old.mAlpha = alpha;
            return old;
        }
    }

    public static RichTransformation setTransform(RichTransformation old, CGAffineTransform cgtrans) {
        Matrix newmatrix;
        if (cgtrans == null)
            newmatrix = new Matrix();
        else
            newmatrix = cgtrans.xm_matrixclone();
        if (newmatrix.isIdentity())
            if (old == null)
                return null;
            else if (old.mAlpha >= 1)
                return null;
            else {
                old.mMatrix = newmatrix;
                return old;
            }
        else {
            if (old == null)
                old = new RichTransformation();
            old.mMatrix = newmatrix;
            return old;
        }
    }

    public static RichTransformation setParameters(IOSAnimation params) {
        return setTransform(setAlpha(null, params.getAlpha()), params.getTransform());
    }

    public CGAffineTransform getTransformation() {
        return CGAffineTransform.xm_new(mMatrix);
    }
}
