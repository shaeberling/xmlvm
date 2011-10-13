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

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import org.crossmobile.ios2a.UIRunner;

public class UIProgressView extends UIView {

    private static final int MAXVALUE = 10000;
    //
    private float progress = 0f;
    private int progressViewStyle = -1; //Unknown

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public UIProgressView(int UIProgressViewStyle) {
        setProgressViewStyle(UIProgressViewStyle);
    }

    public void setProgress(float val) {
        progress = val;
        if (progress < 0)
            progress = 0;
        if (progress > 1)
            progress = 1;
        UIRunner.runSynced(new UIRunner() {

            @Override
            public void exec() {
                ((ProgressBar) xm_model()).setProgress((int) (MAXVALUE * progress));
            }
        });
    }

    public float getProgress() {
        return progress;
    }

    public int getProgressViewStyle() {
        return progressViewStyle;
    }

    public void setProgressViewStyle(int UIProgressViewStyle) {
        progressViewStyle = UIProgressViewStyle;
    }

    @Override
    View createModelObject(Context cx) {
        ProgressBar bar = new ProgressBar(cx, null, android.R.attr.progressBarStyleHorizontal);
        bar.setMax(MAXVALUE);
        bar.setIndeterminate(false);
        return bar;
    }
}
