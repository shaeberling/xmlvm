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

import java.util.ArrayList;
import java.util.HashMap;

public class CALayer extends NSObject {

    private HashMap<String, CAAnimation> animation;
    private UIView delegate;

    public static CALayer layer() {
        return new CALayer();
    }

    public CALayer() {
        animation = new HashMap<String, CAAnimation>();
    }

    public void addAnimation(CAAnimation animation, String key) {
        this.animation.put(key, animation);
    }

    public CAAnimation animationForKey(String key) {
        return animation.get(key);
    }

    public void removeAllAnimations() {
        animation.clear();
    }

    public void removeAnimationForKey(String key) {
        animation.remove(key);
    }

    public ArrayList<String> animationKeys() {
        return new ArrayList<String>(animation.keySet());
    }

    public void renderInContext(CGContext context) {
        delegate.xm_model().draw(context.getCanvas());
    }

    public UIView getDelegate() {
        return delegate;
    }

    public void setDelegate(UIView delegate) {
        this.delegate = delegate;
    }
}
