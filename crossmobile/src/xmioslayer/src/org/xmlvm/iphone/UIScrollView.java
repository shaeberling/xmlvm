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

import android.view.View;
import org.crossmobile.ios2a.IOSChild;
import org.crossmobile.ios2a.IOSView;
import org.crossmobile.ios2a.ImplementationError;
import org.crossmobile.ios2a.MainActivity;
import org.crossmobile.ios2a.IOSScrollView;

public class UIScrollView extends UIView {

    private final IOSScroller scroller;
    //
    private boolean scrollEnabled = true;
    private boolean showsHorizontalScrollIndicator = true;
    private boolean showsVerticalScrollIndicator = true;
    private boolean scrollsToTop = true;
    private UIScrollViewDelegate delegate = null;

    public UIScrollView() {
        this(CGRect.Zero());
    }

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public UIScrollView(CGRect frame) {
        super(frame);
        if (!skipCustomMethods()) {
            scroller = new IOSScroller();
            setFrame(frame);
            setContentSize(frame.size);
            scroller.addView(xm_base());
        } else
            scroller = null;
    }

    public CGSize getContentSize() {
        return ((IOSView.LayoutParams) xm_base().getLayoutParams()).getFrame().size;
    }

    public void setContentSize(CGSize contentSize) {
        xm_base().setLayoutParams(new IOSView.LayoutParams(contentSize));
        scroller.setContentSize(contentSize);
    }

    @Override
    public CGRect getFrame() {
        if (skipCustomMethods())
            return super.getFrame();
        else
            return ((IOSView.LayoutParams) scroller.getLayoutParams()).getFrame();
    }

    @Override
    public void setFrame(CGRect frame) {
        if (skipCustomMethods()) // When initializing, this property is not set yet
            super.setFrame(frame);
        else if (scroller != null)
            transf.setFrame(scroller, frame);
    }

    @Override
    public View getContentLayer() {
        if (skipCustomMethods())
            return super.getContentLayer();
        else
            return scroller;
    }

    public void setContentOffset(CGPoint offset) {
        setContentOffset(offset, false);
    }

    public void setContentOffset(CGPoint offset, boolean animated) {
        scroller.scrollTo((int) (IOSView.x2Android(offset.x) + 0.5f), (int) (IOSView.y2Android(offset.y) + 0.5f));
    }

    public CGPoint getContentOffset() {
        throw new ImplementationError();
    }

    public void setScrollEnabled(boolean scrollEnabled) {
        this.scrollEnabled = scrollEnabled;
    }

    public boolean isScrollEnabled() {
        return this.scrollEnabled;
    }

    public boolean isPagingEnabled() {
        return scroller.isPagingEnabled();
    }

    public void setPagingEnabled(boolean pagingEnabled) {
        scroller.setPagingEnabled(pagingEnabled);
    }

    public UIScrollViewDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(UIScrollViewDelegate delegate) {
        this.delegate = delegate;
    }

    public boolean isScrollsToTop() {
        return scrollsToTop;
    }

    public void setScrollsToTop(boolean scrollsToTop) {
        this.scrollsToTop = scrollsToTop;
    }

    public boolean isShowsHorizontalScrollIndicator() {
        return showsHorizontalScrollIndicator;
    }

    public void setShowsHorizontalScrollIndicator(boolean showsHorizontalScrollIndicator) {
        this.showsHorizontalScrollIndicator = showsHorizontalScrollIndicator;
    }

    public boolean isShowsVerticalScrollIndicator() {
        return showsVerticalScrollIndicator;
    }

    public void setShowsVerticalScrollIndicator(boolean showsVerticalScrollIndicator) {
        this.showsVerticalScrollIndicator = showsVerticalScrollIndicator;
    }

    public void scrollRectToVisible(CGRect rect, boolean animated) {
        if (animated)
            scroller.smoothScrollTo((int) (IOSView.x2Android(rect.origin.x) + 0.5f), (int) (IOSView.y2Android(rect.origin.y) + 0.5f));
        else
            scroller.scrollTo((int) (IOSView.x2Android(rect.origin.x) + 0.5f), (int) (IOSView.y2Android(rect.origin.y) + 0.5f));
    }

    private boolean skipCustomMethods() {
        return this instanceof UITableView;
    }

    private static class IOSScroller extends IOSScrollView implements IOSChild {

        public IOSScroller() {
            super(MainActivity.current);
        }

        @Override
        public IOSView getIOSView() {
            return (IOSView) getChildAt(0);
        }
    }
}
