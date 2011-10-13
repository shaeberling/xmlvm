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

import android.graphics.drawable.BitmapDrawable;
import org.crossmobile.ios2a.GraphicsUtils;
import static org.xmlvm.iphone.UIControlState.*;

class xmButtonStates {

    private static final int NormalIdx = 0;
    private static final int SelectedIdx = 1;
    private static final int HighlightedIdx = 2;
    private static final int DisabledIdx = 3;
    //
    private final State[] states;
    private int current = 0;
    boolean adjustsImageWhenHighlighted = true;
    boolean adjustsImageWhenDisabled = true;
    boolean showsTouchWhenHighlighted = true;

    {
        states = new State[4];
        states[NormalIdx] = new State();
    }

    void setState(int newstate) {
        current = newstate;
    }

    String getTitle(int state) {
        state = stateToIdx(state);
        if (states[state] != null && states[state].title != null)
            return states[state].title;
        return states[NormalIdx].title;
    }

    String getTitle() {
        return getTitle(current);
    }

    void setTitle(int state, String title) {
        getState(state).title = title;
    }

    UIColor getTitlecolor(int state) {
        state = stateToIdx(state);
        if (states[state] != null && states[state].titlecolor != null)
            return states[state].titlecolor;
        return states[NormalIdx].titlecolor;
    }

    UIColor getTitlecolor() {
        return getTitlecolor(current);
    }

    void setTitlecolor(int state, UIColor titlecolor) {
        getState(state).titlecolor = titlecolor;
    }

    UIColor getShadowColor(int state) {
        state = stateToIdx(state);
        if (states[state] != null && states[state].shadowColor != null)
            return states[state].shadowColor;
        return states[NormalIdx].shadowColor;
    }

    UIColor getShadowColor() {
        return getShadowColor(current);
    }

    void setShadowColor(int state, UIColor shadowColor) {
        getState(state).shadowColor = shadowColor;
    }

    UIImage getBack(int state) {
        state = stateToIdx(state);
        if (states[state] != null && states[state].back != null)
            return states[state].back;
        if (adjustsImageWhenHighlighted && state == HighlightedIdx && states[NormalIdx].back != null) {
            getIdx(HighlightedIdx).back = UIImage.imageFromBitmap(GraphicsUtils.getTintedBitmap(((BitmapDrawable) states[NormalIdx].back.getModel()).getBitmap(), .6f, 0.6f, 0.6f), false);
            return states[HighlightedIdx].back;
        } else
            return states[NormalIdx].back;
    }

    UIImage getBack() {
        return getBack(current);
    }

    void setBack(int state, UIImage back) {
        getState(state).back = back;
    }

    UIImage getFore(int state) {
        state = stateToIdx(state);
        if (states[state] != null && states[state].fore != null)
            return states[state].fore;
        if (adjustsImageWhenHighlighted && state == HighlightedIdx && states[NormalIdx].fore != null) {
            getIdx(HighlightedIdx).fore = UIImage.imageFromBitmap(GraphicsUtils.getTintedBitmap(((BitmapDrawable) states[NormalIdx].fore.getModel()).getBitmap(), .6f, 0.6f, 0.6f), false);
            return states[HighlightedIdx].fore;
        } else
            return states[NormalIdx].fore;
    }

    UIImage getFore() {
        return getFore(current);
    }

    void setFore(int state, UIImage fore) {
        getState(state).fore = fore;
    }

    private State getIdx(int index) {
        if (states[index] == null)
            states[index] = new State(states[NormalIdx]);
        return states[index];
    }

    private State getState(int state) {
        return getIdx(stateToIdx(state));
    }

    private int stateToIdx(int controlstate) {
        int which;
        switch (controlstate) {
            case Selected:
                which = SelectedIdx;
                break;
            case Highlighted:
                which = HighlightedIdx;
                break;
            case Disabled:
                which = DisabledIdx;
            default:
                which = NormalIdx;
        }
        return which;
    }

    boolean isInState(int UIControlState) {
        return UIControlState == current;
    }

    private class State {

        private String title;
        private UIColor titlecolor;
        private UIColor shadowColor;
        private UIImage back;
        private UIImage fore;

        private State() {
        }

        private State(State other) {
            this.title = other.title;
            this.titlecolor = other.titlecolor;
            this.shadowColor = other.shadowColor;
            this.back = other.back;
            this.fore = other.fore;
        }
    }
}
