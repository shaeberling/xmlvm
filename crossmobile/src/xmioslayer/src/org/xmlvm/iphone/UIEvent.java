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

import static android.view.MotionEvent.*;
import static org.xmlvm.iphone.UITouchPhase.*;

import android.view.MotionEvent;
import java.util.HashSet;
import java.util.Set;
import org.crossmobile.ios2a.IOSView;

public class UIEvent extends NSObject {

    private static long last_tap_timestamp = 0;
    private static int tapcount = 1;
    private HashSet<UITouch> touches;
    UITouch firsttouch;

    UIEvent(xmEventDispatcher dispatcher, MotionEvent ev, boolean ignoreBar) {
        float deltaY = ignoreBar ? 0 : IOSView.androidBarHeight();
        int phase;
        switch (ev.getAction()) {
            case ACTION_DOWN:
                
                // Calculate tap count
                long current_tap_timestamp = System.currentTimeMillis();
                if (current_tap_timestamp - last_tap_timestamp < 800)
                    tapcount++;
                else
                    tapcount = 1;
                last_tap_timestamp = current_tap_timestamp;
                
                phase = Began;
                break;
            case ACTION_MOVE:
                phase = Moved;
                break;
            case ACTION_UP:
                phase = Ended;
                break;
            case ACTION_CANCEL:
            default:
                phase = Cancelled;
                break;
        }

        touches = new HashSet<UITouch>();
        for (int p = ev.getPointerCount() - 1; p >= 0; p--) {
            firsttouch = new UITouch(
                    dispatcher.source,
                    phase,
                    System.currentTimeMillis() / 1000d,
                    tapcount,
                    new CGPoint(IOSView.x2IOS((int) (0.5f + ev.getX(p))), IOSView.y2IOS((int) (0.5f + ev.getY(p) + deltaY))));
            touches.add(firsttouch);
            // with this trick, the variable firsttouch will always hold the "first touch" event
        }
    }

    public Set<UITouch> allTouches() {
        return touches;
    }

    void updateToView(UIView view) {
        for (UITouch t : touches)
            t.view = view;
    }
}
