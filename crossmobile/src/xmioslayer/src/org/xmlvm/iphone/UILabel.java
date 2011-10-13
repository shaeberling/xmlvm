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
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import org.crossmobile.ios2a.UIRunner;

public class UILabel extends UIView {

    private static final UIFont LABELFONT = UIFont.boldSystemFontOfSize(UIFont.buttonFontSize());
//
    private boolean adjustsFontSizeToFitWidth;
    private UIColor shadowColor;
    private CGSize shadowOffset;
    private int lineBreakMode;
    private int numberOfLines;

    public UILabel() {
        this(CGRect.Zero());
    }

    public UILabel(CGRect frame) {
        super(frame);
        setUserInteractionEnabled(false);
        setBackgroundColor(UIColor.whiteColor);
        setTextAlignment(UITextAlignment.Left);
        UIRunner.runSynced(new UIRunner() {

            @Override
            public void exec() {
                TextView tv = (TextView) xm_model();
                tv.setTextColor(Color.BLACK);
                tv.setTypeface(LABELFONT.__Typeface());
                tv.setTextSize(LABELFONT.__AndroidSize());
            }
        });
    }

    public void setText(final String string) {
        UIRunner.runSynced(new UIRunner() {

            @Override
            public void exec() {
                ((TextView) xm_model()).setText(string);
            }
        });
    }

    public String getText() {
        return ((TextView) xm_model()).getText().toString();
    }

    public void setFont(final UIFont font) {
        UIRunner.runSynced(new UIRunner() {

            @Override
            public void exec() {
                TextView tv = (TextView) xm_model();
                tv.setTypeface(font.__Typeface());
                tv.setTextSize(font.__AndroidSize());
            }
        });
    }

    public UIFont getFont() {
        TextView tv = (TextView) xm_model();
        return new UIFont(tv.getTypeface(), tv.getTextSize());
    }

    public void setTextColor(UIColor color) {
        ((TextView) xm_model()).setTextColor(color.getModelColor());
    }

    public UIColor getTextColor() {
        return new UIColor(((TextView) xm_model()).getTextColors().getDefaultColor());
    }

    public final void setTextAlignment(int UITextAlignment) {
        ((TextView) xm_model()).setGravity(org.xmlvm.iphone.UITextAlignment.alignmentToGravity(UITextAlignment));
    }

    public int getTextAlignment() {
        return UITextAlignment.gravityToAlignment(((TextView) xm_model()).getGravity());
    }

    public int getLineBreakMode() {
        return lineBreakMode;
    }

    public void setLineBreakMode(int lineBreakMode) {
        this.lineBreakMode = lineBreakMode;
    }

    public int getNumberOfLines() {
        return numberOfLines;
    }

    public void setNumberOfLines(int numberOfLines) {
        this.numberOfLines = numberOfLines;
    }

    public CGSize getShadowOffset() {
        return shadowOffset;
    }

    public void setShadowOffset(CGSize shadowOffset) {
        this.shadowOffset = shadowOffset;
        redoShadow();
    }

    public UIColor getShadowColor() {
        return shadowColor;
    }

    public void setShadowColor(UIColor shadowColor) {
        this.shadowColor = shadowColor;
        redoShadow();
    }

    private void redoShadow() {
        if (shadowOffset == null)
            ((TextView) xm_model()).setShadowLayer(0, 0, 0, 0);
        else
            ((TextView) xm_model()).setShadowLayer(1, shadowOffset.width, shadowOffset.height, shadowColor.getModelColor());
    }

    public boolean isAdjustsFontSizeToFitWidth() {
        return adjustsFontSizeToFitWidth;
    }

    public void setAdjustsFontSizeToFitWidth(boolean adjustsFontSizeToFitWidth) {
        this.adjustsFontSizeToFitWidth = adjustsFontSizeToFitWidth;
    }

    @Override
    View createModelObject(Context cx) {
        return new TextView(cx);
    }
}
