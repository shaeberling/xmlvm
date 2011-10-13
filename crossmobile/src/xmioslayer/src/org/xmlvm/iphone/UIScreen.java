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

import static org.crossmobile.ios2a.IOSView.STATUSBAR_HEIGHT;
import org.crossmobile.ios2a.IOSView;

public class UIScreen extends NSObject {

    private final static UIScreen mainScreen = new UIScreen();
    static int orientation = UIDevice.currentDevice().getOrientation();
    private static CGRect applicationFrame;
    //
    private final CGRect screen;

    private UIScreen() {
        switch (UIDevice.currentDevice().getUserInterfaceIdiom()) {
            case UIUserInterfaceIdiom.Phone:
                screen = new CGRect(0, 0, 320, 480);
                break;
            default:
                screen = new CGRect(0, 0, 768, 1024);
        }
    }

    public static UIScreen mainScreen() {
        return mainScreen;
    }

    public CGRect getBounds() {
        return new CGRect(screen);
    }

    public CGRect getApplicationFrame() {
        if (applicationFrame == null) {
            float statusbar = UIApplication.sharedApplication().isStatusBarHidden() ? 0 : STATUSBAR_HEIGHT;
            switch (orientation) {
                case UIInterfaceOrientation.Portrait:
                    applicationFrame = new CGRect(screen.origin.x, screen.origin.y + statusbar,
                            screen.size.width, screen.size.height - statusbar);
                    break;
                case UIInterfaceOrientation.LandscapeLeft:
                    applicationFrame = new CGRect(screen.origin.x, screen.origin.y, screen.size.width
                            - statusbar, screen.size.height);
                    break;
                case UIInterfaceOrientation.LandscapeRight:
                    applicationFrame = new CGRect(screen.origin.x + statusbar, screen.origin.y,
                            screen.size.width - statusbar, screen.size.height);
                    break;
                case UIInterfaceOrientation.PortraitUpsideDown:
                    applicationFrame = new CGRect(screen.origin.x, screen.origin.y, screen.size.width,
                            screen.size.height - statusbar);
                    break;
            }
        }
        return new CGRect(applicationFrame);
    }

    public float getScale() {
        return IOSView.getScale();
    }
}
