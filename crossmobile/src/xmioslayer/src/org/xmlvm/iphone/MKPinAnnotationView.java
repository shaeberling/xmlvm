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

import org.crossmobile.ios2a.FileBridge;
import static org.xmlvm.iphone.MKPinAnnotationColor.*;

public class MKPinAnnotationView extends MKAnnotationView {

    private int pinColor = MKPinAnnotationColor.Red;
    private boolean animatesDrop = true;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public MKPinAnnotationView(MKAnnotation annotation, String reuseIdentifier) {
        super(annotation, reuseIdentifier);
        setPinColor(Red);
        setCenterOffset(new CGPoint(0, 13));    // 32/2 - 3 offset
    }

    public boolean isAnimatesDrop() {
        return animatesDrop;
    }

    public void setAnimatesDrop(boolean animatesDrop) {
        this.animatesDrop = animatesDrop;
    }

    public int getPinColor() {
        return pinColor;
    }

    public void setPinColor(int MKPinAnnotationColor) {
        this.pinColor = MKPinAnnotationColor;
        switch (MKPinAnnotationColor) {
            case Purple:
                setImage(UIImage.imageWithContentsOfFile(FileBridge.RESOURCEPREFIX + "pinpurple"));
                break;
            case Green:
                setImage(UIImage.imageWithContentsOfFile(FileBridge.RESOURCEPREFIX + "pingreen"));
                break;
            case Red:
                setImage(UIImage.imageWithContentsOfFile(FileBridge.RESOURCEPREFIX + "pinred"));
                break;
        }
    }
}
