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

import android.view.MotionEvent;
import static org.xmlvm.iphone.UITouchPhase.*;

class xmEventDispatcher {

    UIView source;
    private UIView lastViewFound;

    xmEventDispatcher(UIView source) {
        this.source = source;
    }

    UIEvent createEvent(MotionEvent ev, boolean ignoreBar) {
        return new UIEvent(this, ev, ignoreBar);
    }

    void sendEvent(UIEvent event) {
        switch (event.firsttouch.phase) {
            /**
             * Send events to all responders or only to the first one?
             * Right now only the first touch event is taken into account.
             * Also check if every touch event points to different view
             * (and properly define UIEvent.updateToView)
             */
            case Began:
                lastViewFound = source.hitTest(new CGPoint(event.firsttouch.wloc.x, event.firsttouch.wloc.y), event);
                if (lastViewFound != null) {
                    event.updateToView(lastViewFound);
                    lastViewFound.touchesBegan(event.allTouches(), event);
                }
                break;
            case Moved:
                if (lastViewFound != null) {
                    event.updateToView(lastViewFound);
                    lastViewFound.touchesMoved(event.allTouches(), event);
                }
                break;
            case Ended:
                if (lastViewFound != null) {
                    event.updateToView(lastViewFound);
                    lastViewFound.touchesEnded(event.allTouches(), event);
                }
                break;
            case Cancelled:
            default:
                if (lastViewFound != null) {
                    event.updateToView(lastViewFound);
                    lastViewFound.touchesCancelled(event.allTouches(), event);
                }
                break;
        }
    }
}
