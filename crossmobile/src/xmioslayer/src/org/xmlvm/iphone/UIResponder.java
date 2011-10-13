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
import org.crossmobile.ios2a.ImplementationError;
import java.util.Set;
import org.crossmobile.ios2a.IOSView;
import org.crossmobile.ios2a.MainActivity;
import org.crossmobile.ios2a.UIRunner;

public abstract class UIResponder extends NSObject {

    private IOSView base;
    private View model;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public UIResponder() {
        UIRunner.runSynced(new UIRunner() {

            @Override
            public void exec() {
                /* Create master */
                base = createBaseObject(MainActivity.current);
                /* Create pupil */
                View cpupil = createModelObject(MainActivity.current);
                if (cpupil == null)
                    model = base;
                else {
                    model = cpupil;
                    base.addView(model);
                }
            }
        });
    }

    public void touchesBegan(Set<UITouch> touches, UIEvent event) {
    }

    public void touchesMoved(Set<UITouch> touches, UIEvent event) {
    }

    public void touchesEnded(Set<UITouch> touches, UIEvent event) {
    }

    public void touchesCancelled(Set<UITouch> touches, UIEvent event) {
    }

    public UIResponder getNextResponder() {
        throw new ImplementationError();
    }

    public boolean resignFirstResponder() {
        return true;
    }

    public boolean becomeFirstResponder() {
        return true;
    }

    /**
     * @hide
     */
    public final View xm_model() {
        return model;
    }

    /**
     * @hide
     */
    public final IOSView xm_base() {
        return base;
    }

    IOSView getContainerLayer() {
        return base;
    }

    View getContentLayer() {
        return base;
    }

    IOSView createBaseObject(Context cx) {
        return new IOSView(cx);
    }

    View createModelObject(Context cx) {
        return null;
    }
}
