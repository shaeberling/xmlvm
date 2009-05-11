package org.xmlvm.iphone;

import org.xmlvm.iphone.internal.Simulator;

public class UIScreen {

    private UIScreen() {
    }

    public static UIScreen mainScreen() {
        return new UIScreen();
    }

    public CGRect bounds() {
        return new CGRect(new CGRect(0.0f, 0.0f, 320.0f, 480.0f));
    }

    public CGRect applicationFrame() {
        CGRect applicationFrame = null;
        float offset = Simulator.getStatusBarHeight();
        int orientation = Simulator.getStatusBarOrientation();
        switch (orientation) {
        case UIInterfaceOrientation.UIInterfaceOrientationPortrait:
            applicationFrame = new CGRect(0, offset, 320, 480 - offset);
            break;
        case UIInterfaceOrientation.UIInterfaceOrientationLandscapeLeft:
            applicationFrame = new CGRect(0, 0, 320 - offset, 480);
            break;
        case UIInterfaceOrientation.UIInterfaceOrientationLandscapeRight:
            applicationFrame = new CGRect(offset, 0, 320 - offset, 480);
            break;
        case UIInterfaceOrientation.UIInterfaceOrientationPortraitUpsideDown:
            applicationFrame = new CGRect(0, 0, 320, 480 - offset);
            break;
        }
        return applicationFrame;
    }
}
