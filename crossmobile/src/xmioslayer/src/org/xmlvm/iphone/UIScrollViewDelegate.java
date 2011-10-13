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

public abstract class UIScrollViewDelegate extends NSObject {

    public void scrollViewDidScroll(UIScrollView scrollView) {
    }

    public void scrollViewWillBeginDragging(UIScrollView scrollView) {
    }

    public void scrollViewDidEndDragging(UIScrollView scrollView, boolean willDecelerate) {
    }

    public boolean scrollViewShouldScrollToTop(UIScrollView scrollView) {
        return true;
    }

    public void scrollViewDidScrollToTop(UIScrollView scrollView) {
    }

    public void scrollViewWillBeginDecelerating(UIScrollView scrollView) {
    }

    public void scrollViewDidEndDecelerating(UIScrollView scrollView) {
    }

    public UIView viewForZoomingInScrollView(UIScrollView scrollView) {
        return null;
    }

    public void scrollViewDidEndZooming(UIScrollView scrollView, UIView withView, float atScale) {
    }

    public void scrollViewDidEndScrollingAnimation(UIScrollView scrollView) {
    }
}
