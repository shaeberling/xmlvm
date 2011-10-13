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
import android.graphics.RectF;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import org.xmlvm.iphone.CGAffineTransform;

public class IOSAnimation extends Animation {

    private float fromalpha = 1;
    private float toalpha = 1;
    //
    private float[] frommatrix = null;
    private float[] tomatrix = null;
    //
    private float[] scratch = null;

    public void setAlpha(float from, float to) {
        if (to >= 1 || to < 0)
            to = 1;
        fromalpha = from;
        toalpha = to;
    }

    public void setTransform(CGAffineTransform from, CGAffineTransform to) {
        Matrix mFrom = from.xm_matrixclone();
        Matrix mTo = to.xm_matrixclone();

        if (mFrom.isIdentity() && mTo.isIdentity()) {
            frommatrix = null;
            tomatrix = null;
            scratch = null;
            return;
        }

        if (frommatrix == null)
            frommatrix = new float[9];
        if (tomatrix == null)
            tomatrix = new float[9];
        if (scratch == null)
            scratch = new float[9];

        mFrom.getValues(frommatrix);
        mTo.getValues(tomatrix);
    }

    public float getAlpha() {
        return toalpha;
    }

    public CGAffineTransform getTransform() {
        if (tomatrix == null)
            return null;
        Matrix matrix = new Matrix();
        matrix.setValues(tomatrix);
        return CGAffineTransform.xm_new(matrix);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        t.setAlpha(fromalpha + (toalpha - fromalpha) * interpolatedTime);
        if (tomatrix != null) {
            for (int i = 0; i < frommatrix.length; i++)
                scratch[i] = frommatrix[i] + (tomatrix[i] - frommatrix[i]) * interpolatedTime;
            t.getMatrix().setValues(scratch);
        }
    }

    @Override
    public boolean willChangeBounds() {
        return tomatrix != null;
    }

    @Override
    public boolean willChangeTransformationMatrix() {
        return tomatrix != null;
    }
}
