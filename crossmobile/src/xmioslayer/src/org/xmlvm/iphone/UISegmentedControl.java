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

import static org.crossmobile.ios2a.GraphicsUtils.SegmentShape.Location.*;

import android.graphics.drawable.ShapeDrawable;
import java.util.ArrayList;
import org.crossmobile.ios2a.GraphicsUtils;
import org.crossmobile.ios2a.GraphicsUtils.SegmentShape;
import org.crossmobile.ios2a.UIRunner;

public class UISegmentedControl extends UIControl {

    private int selection = -1;
    private ArrayList<UIButton> items = new ArrayList<UIButton>();
    private int style = UISegmentedControlStyle.Plain;
    private UIColor tintColor = UIColor.blueColor;
    private UIColor clickedColor = new UIColor(GraphicsUtils.getTintedColor(tintColor.getModelColor(), 0.7f, 0.7f, 0.7f));
    private boolean momentary = false;
    //
    private boolean deferupdate = false;
    //
    private UIControlDelegate delegate = new UIControlDelegate() {

        @Override
        public void raiseEvent(UIControl sender, int event) {
            int clickedItem = sender.getTag();

            if ((event & UIControlEvent.TouchDown) > 0 && clickedItem != selection && selection >= 0 && selection < items.size())
                setSegmentSelected(items.get(selection), false);
            else if ((event & UIControlEvent.TouchUpInside) > 0)
                setSelectedSegmentIndex(clickedItem);
            else if ((event & UIControlEvent.TouchUpOutside) > 0)
                if (momentary)
                    setSegmentSelected((UIButton) sender, false);   // if momentary, always clean selection
                else if (clickedItem != selection) {
                    setSegmentSelected((UIButton) sender, false);   // clean current selection
                    if (selection >= 0 && selection < items.size())
                        setSegmentSelected(items.get(selection), true); // restore old selection
                }

            for (EventDelegate item : controldelegates) {
                if ((item.event & event) > 0)
                    item.delegate.raiseEvent(UISegmentedControl.this, event);
                if ((event & UIControlEvent.TouchUpInside) > 0
                        && (item.event & UIControlEvent.ValueChanged) > 0)  // Just make sure that it is a valid touch. It seems that this how it is implemented in iOS too
                    item.delegate.raiseEvent(UISegmentedControl.this, UIControlEvent.ValueChanged);
            }
        }
    };

    public UISegmentedControl() {
        this(CGRect.Zero());
    }

    public UISegmentedControl(CGRect rect) {
        super(rect);
    }

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public UISegmentedControl(ArrayList items) {
        if (items == null || items.isEmpty())
            return;
        deferupdate = true;
        if (items.get(0) instanceof String)
            for (int i = 0; i < items.size(); i++)
                insertSegmentWithTitle((String) items.get(i), i, false);
        else if (items.get(0) instanceof UIImage)
            for (int i = 0; i < items.size(); i++)
                insertSegmentWithImage((UIImage) items.get(i), i, false);
        deferupdate = false;
        setSelectedSegmentIndex(0);
        updateMetrics();
    }

    @Override
    public void setFrame(CGRect frame) {
        super.setFrame(frame);
        updateMetrics();
    }

    public void setTitle(final String title, final int index) {
        items.get(index).setTitle(title, UIControlState.Normal);
    }

    public String titleForSegmentAtIndex(int index) {
        return items.get(index).getCurrentTitle();
    }

    public void setImage(final UIImage image, final int index) {
        items.get(index).setImage(image, UIControlState.Normal);
    }

    public UIImage imageForSegmentAtIndex(final int index) {
        return items.get(index).getCurrentImage();
    }

    public final void insertSegmentWithTitle(final String title, final int index, boolean animated) {
        addSegment(index).setTitle(title, UIControlState.Normal);
    }

    public final void insertSegmentWithImage(UIImage img, int index, boolean animated) {
        addSegment(index).setImage(img, UIControlState.Normal);
    }

    private UIButton addSegment(int index) {
        UIButton segment = new UIButton(UIButtonType.Custom) {

            @Override
            public void setHighlighted(boolean highlighted) {
                if (highlighted)
                    setSegmentSelected(this, true);
                super.setHighlighted(highlighted);
            }
        };
        segment.setUpdatableBackImage(false);  // REQUIRED!
        segment.setAdjustsImageWhenHighlighted(false);
        segment.setImageFillsArea(false);
        segment.setTag(index);
        segment.addTarget(delegate, UIControlEvent.AllEvents);
        ShapeDrawable back = new ShapeDrawable(new SegmentShape());
        segment.xm_base().setBackgroundDrawable(back);
        int cindex = (index < 0) ? 0 : ((index > items.size()) ? items.size() : index);
        items.add(cindex, segment);

        if (selection >= cindex)
            selection++;
        setSegmentSelected(segment, false);
        if (!deferupdate)
            updateMetrics();

        return segment;
    }

    public void removeAllSegments() {
        items.clear();
        setSelectedSegmentIndex(-1);
        updateMetrics();
    }

    public void removeSegmentAtIndex(int index, boolean animated) {
        if (selection > index)
            selection--;
        else if (selection == index)
            setSelectedSegmentIndex(-1);
        items.remove(index);
        updateMetrics();
    }

    public int numberOfSegments() {
        return items.size();
    }

    public int getSelectedSegmentIndex() {
        return selection;
    }

    public void setSelectedSegmentIndex(int index) {
        setSegmentSelected(items.get(index), !isMomentary());
        selection = index;
        setNeedsDisplay();
    }

    public int getSegmentedControlStyle() {
        return style;
    }

    public void setSegmentedControlStyle(int UISegmentedControlStyle) {
        this.style = UISegmentedControlStyle;
        // frame.size.height = (style ==
        // UISegmentedControlStyle.UISegmentedControlStyleBar) ?
        // kSegmentedControlHeightBar : kSegmentedControlHeightDefault;
        setNeedsDisplay();
    }

    public UIColor getTintColor() {
        return tintColor;
    }

    public void setTintColor(UIColor tintColor) {
        int modelcolor = tintColor.getModelColor() | 0xff000000;
        this.tintColor = new UIColor(modelcolor);
        this.clickedColor = new UIColor(GraphicsUtils.getTintedColor(tintColor.getModelColor(), 0.7f, 0.7f, 0.7f));

        for (int i = 0; i < items.size(); i++)
            setSegmentSelected(items.get(i), i == selection && !momentary);
    }

    public boolean isMomentary() {
        return momentary;
    }

    public void setMomentary(boolean momentary) {
        if (this.momentary != momentary) {
            this.momentary = momentary;
            if (selection >= 0)
                setSegmentSelected(items.get(selection), !momentary);
        }
    }

    private void setSegmentSelected(UIButton segment, boolean selected) {
        ((ShapeDrawable) segment.xm_base().getBackground()).getPaint().setColor(selected ? clickedColor.getModelColor() : tintColor.getModelColor());
    }

    private void updateMetrics() {
        if (items == null)  // be safe with early initialization, due to overiding of setFrame method
            return;
        for (UIView v : getSubviews())
            v.removeFromSuperview();

        if (items.size() < 1)
            return;
        UIRunner.runSynced(new UIRunner() {

            @Override
            public void exec() {
                CGRect frame = getFrame();
                int actualX = 0;
                int actualWidth = 0;
                float xRunner = 0;
                float desiredWidth = frame.size.width / (items.size() > 0 ? items.size() : 1);
                float height = frame.size.height;
                int itemmax = items.size() - 1;

                for (int i = 0; i <= itemmax; i++) {
                    UIButton button = items.get(i);
                    /* Update metrics */
                    xRunner += desiredWidth;
                    actualWidth = (int) (xRunner - actualX + 0.5);
                    button.setFrame(new CGRect(actualX, 0, actualWidth, height));
                    addSubview(button);
                    actualX += actualWidth;

                    /* Update shape */
                    SegmentShape.Location loc = CENTER;
                    if (i == 0)
                        loc = LEFT;
                    else if (i == itemmax)
                        loc = RIGHT;
                    ((SegmentShape) ((ShapeDrawable) button.xm_base().getBackground()).getShape()).updateShape(loc);
                }
                setNeedsDisplay();
            }
        });
    }
}
