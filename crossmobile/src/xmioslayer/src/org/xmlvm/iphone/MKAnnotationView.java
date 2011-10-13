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

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.Window;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;
import java.lang.ref.WeakReference;
import org.crossmobile.ios2a.IOSView;
import org.crossmobile.ios2a.MainActivity;
import org.crossmobile.ios2a.FileBridge;

public class MKAnnotationView extends UIView {

    private boolean enabled;
    private UIImage image;
    private boolean highlighted;
    private CGPoint centerOffset;
    private CGPoint calloutOffset;
    private String reuseIdentifier;
    private boolean selected;
    private boolean canShowCallout;
    private UIView leftCalloutAccessoryView;
    private UIView rightCalloutAccessoryView;
    private boolean draggable;
    private int dragState;
    //
    private xmEventDispatcher dispatcher = new xmEventDispatcher(this);
    private UILabel title;
    private UILabel subtitle;
    private UIButton close;
    //
    private WeakReference<Dialog> dialogref;
    //
    MKAnnotation annotation;

    public MKAnnotationView(MKAnnotation annotation, String reuseIdentifier) {
        super(new CGRect(60, 60, 200, 70));
        this.annotation = annotation;
        this.reuseIdentifier = reuseIdentifier;

        title = new UILabel(new CGRect(0, 0, 200, 26));
        title.setBackgroundColor(UIColor.clearColor);
        title.setFont(UIFont.boldSystemFontOfSize(18));
        title.setTextColor(UIColor.whiteColor);
        addSubview(title);

        subtitle = new UILabel(new CGRect(0, 30, 200, 20));
        subtitle.setBackgroundColor(UIColor.clearColor);
        subtitle.setTextColor(UIColor.whiteColor);
        addSubview(subtitle);

        close = UIButton.buttonWithType(UIButtonType.Custom);
        close.setFrame(new CGRect(184, 0, 24, 24));
        close.setImage(UIImage.imageWithContentsOfFile(FileBridge.RESOURCEPREFIX + "close"), UIControlState.Normal);
        close.addTarget(new UIControlDelegate() {

            @Override
            public void raiseEvent(UIControl sender, int UIControlEvent) {
                if (dialogref == null)
                    return;
                Dialog dialog = dialogref.get();
                if (dialog == null)
                    dialogref = null;
                else
                    dialog.dismiss();
            }
        }, UIControlEvent.TouchUpInside);
        addSubview(close);

        centerOffset = new CGPoint(0, 0);
    }

    public void prepareForReuse() {
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public MKAnnotation getAnnotation() {
        return annotation;
    }

    public void setAnnotation(MKAnnotation annotation) {
        this.annotation = annotation;
    }

    public CGPoint getCalloutOffset() {
        return calloutOffset;
    }

    public void setCalloutOffset(CGPoint calloutOffset) {
        this.calloutOffset = calloutOffset;
    }

    public CGPoint getCenterOffset() {
        return centerOffset;
    }

    public void setCenterOffset(CGPoint centerOffset) {
        this.centerOffset = centerOffset;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public UIImage getImage() {
        return image;
    }

    public void setImage(UIImage image) {
        this.image = image;
    }

    public String getReuseIdentifier() {
        return reuseIdentifier;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        setSelected(selected, false);
    }

    public void setSelected(boolean selected, boolean animated) {
        this.selected = selected;
    }

    public boolean isCanShowCallout() {
        return canShowCallout;
    }

    public void setCanShowCallout(boolean canShowCallout) {
        this.canShowCallout = canShowCallout;
    }

    public UIView getLeftCalloutAccessoryView() {
        return leftCalloutAccessoryView;
    }

    public void setLeftCalloutAccessoryView(UIView leftCalloutAccessoryView) {
        this.leftCalloutAccessoryView = leftCalloutAccessoryView;
    }

    public UIView getRightCalloutAccessoryView() {
        return rightCalloutAccessoryView;
    }

    public void setRightCalloutAccessoryView(UIView rightCalloutAccessoryView) {
        this.rightCalloutAccessoryView = rightCalloutAccessoryView;
        rightCalloutAccessoryView.setLocation(180, 30);
        addSubview(rightCalloutAccessoryView);
    }

    public int getDragState() {
        return dragState;
    }

    public void setDragState(int MKAnnotationViewDragState) {
        setDragState(dragState, false);
    }

    public void setDragState(int MKAnnotationViewDragState, boolean animated) {
        this.dragState = MKAnnotationViewDragState;
    }

    public boolean isDraggable() {
        return draggable;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    final OverlayItem createOverlayItem(MKMapView.AnnotationsOverlay aover) {
        OverlayItem overlay = annotation == null
                ? new OverlayItem(new GeoPoint(0, 0), "", "")
                : new OverlayItem(annotation.getCoordinate().getGeoPoint(), annotation.title(), annotation.subtitle());
        if (image != null) {
            Drawable icon = image.getModel();
            int width = icon.getIntrinsicWidth();
            int height = icon.getIntrinsicHeight();
            int deltax = (int) (width / 2f + IOSView.x2Android(centerOffset.x) + 0.5f);
            int deltay = (int) (height / 2f + IOSView.y2Android(centerOffset.y) + 0.5f);
            icon.setBounds(-deltax, -deltay, width - deltax, height - deltay);
            overlay.setMarker(icon);
        }
        return overlay;
    }

    final boolean onTap() {
        if (!isCanShowCallout())
            return false;
        Dialog dialog = new Dialog(MainActivity.current);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        title.setText(annotation.title());
        subtitle.setText(annotation.subtitle());

        if (xm_base().getParent() != null)
            ((ViewGroup) xm_base().getParent()).removeView(xm_base());
        dialog.setContentView(xm_base());
        dialogref = new WeakReference<Dialog>(dialog);
        dialog.show();

        return true;
    }

    @Override
    IOSView createBaseObject(Context cx) {
        return new IOSView(cx) {

            @Override
            public boolean dispatchTouchEvent(MotionEvent ev) {
                dispatcher.sendEvent(dispatcher.createEvent(ev, true));
                return super.dispatchTouchEvent(ev);   // For other native widgets to work
            }
        };
    }
}
