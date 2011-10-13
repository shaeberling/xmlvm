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

public class UINavigationController extends UIViewController {

    private ArrayList<UIViewController> items;
    private UINavigationControllerDelegate delegate;
    private UINavigationBar navigationBar;
    private boolean navigationBarHidden;
    private UIToolbar toolbar;
    private boolean toolbarHidden;

    /* Required for MFMessageComposeViewController */
    UINavigationController() {
        this(null);
    }

    @SuppressWarnings("LeakingThisInConstructor")
    public UINavigationController(UIViewController rootViewController) {
        super();
        toolbarHidden = true;
        navigationBarHidden = false;
        navigationBar = new UINavigationBar(new CGRect(0, 0, UIScreen.mainScreen().getApplicationFrame().size.width, 40));
        navigationBar.nbcontroller = this;
        toolbar = new UIToolbar();
        items = new ArrayList<UIViewController>();
        pushViewController(rootViewController, false);
    }

    public UIViewController getTopViewController() {
        int size = items.size();
        if (size < 1)
            return null;
        return items.get(size - 1);
    }

    public UIViewController getVisibleViewController() {
        return getTopViewController();
    }

    public ArrayList<UIViewController> getViewControllers() {
        return new ArrayList<UIViewController>(items);
    }

    public void setViewControllers(ArrayList<UIViewController> controllers) {
        setViewControllers(controllers, false);
    }

    public void setViewControllers(ArrayList<UIViewController> controllers, boolean animated) {
        if (controllers == null || controllers.size() < 1)
            throw new RuntimeException("Attempting to assing empty Navigation list!");

        UIViewController old = getTopViewController();
        UIViewController cur = controllers.get(controllers.size() - 1);

        items = new ArrayList<UIViewController>(controllers);
        ArrayList<UINavigationItem> navitems = new ArrayList<UINavigationItem>();
        for (UIViewController c : items) {
            c.setParentController(this);
            navitems.add(new UINavigationItem(c.getTitle()));
        }
        navigationBar.setItems(navitems);

        doExchange(old, cur, animated);
    }

    public final void pushViewController(UIViewController controller, boolean animated) {
        if (controller == null)
            return;
        controller.setParentController(this);
        UIViewController old = getTopViewController();
        items.add(controller);
        navigationBar.pushNavigationItem(new UINavigationItem(controller.getTitle()), animated);

        doExchange(old, controller, animated);
    }

    public UIViewController popViewControllerAnimated(boolean animated) {
        int idx = items.size() - 1;
        if (idx < 1)
            return null;

        UIViewController old = items.get(idx);
        old.setParentController(this);

        items.remove(idx);
        UIViewController cur = items.get(idx - 1);
        navigationBar.popNavigationItemAnimated(animated);

        doExchange(old, cur, animated);
        return old;
    }

    public ArrayList<UIViewController> popToRootViewControllerAnimated(boolean animated) {
        if (items.size() == 1)
            return new ArrayList<UIViewController>();

        UIViewController old = getTopViewController();
        ArrayList<UIViewController> res = items;
        items = new ArrayList<UIViewController>();
        items.add(res.remove(0));
        UIViewController cur = items.get(0);

        ArrayList<UINavigationItem> olditems = navigationBar.getItems();
        ArrayList<UINavigationItem> curitems = new ArrayList<UINavigationItem>();
        curitems.add(olditems.get(0));
        navigationBar.setItems(curitems, animated);

        doExchange(old, cur, animated);
        return res;
    }

    public ArrayList<UIViewController> popToViewController(UIViewController controller, boolean animated) {
        int idx = items.lastIndexOf(controller);
        if (idx <= 0 || idx == (items.size() - 1))
            return new ArrayList<UIViewController>();

        UIViewController old = getTopViewController();

        ArrayList<UIViewController> res = new ArrayList<UIViewController>();
        ArrayList<UINavigationItem> navitems = navigationBar.getItems();
        int howmany = res.size() - 1 - idx;
        for (int i = 0; i < howmany; i++) {
            res.add(items.remove(idx + 1));
            navitems.remove(idx + 1);
        }
        navigationBar.setItems(navitems);

        doExchange(old, controller, animated);
        return res;
    }

    public boolean isNavigationBarHidden() {
        return navigationBarHidden;
    }

    public void setNavigationBarHidden(boolean navigationBarHidden) {
        setNavigationBarHidden(navigationBarHidden, false);
    }

    public void setNavigationBarHidden(boolean navigationBarHidden, boolean animated) {
        this.navigationBarHidden = navigationBarHidden;
        doLayout(animated);
    }

    public UINavigationBar getNavigationBar() {
        return navigationBar;
    }

    public boolean isToolbarHidden() {
        return toolbarHidden;
    }

    public void setToolbarHidden(boolean toolbarHidden) {
        setToolbarHidden(toolbarHidden, false);
    }

    public void setToolbarHidden(boolean toolbarHidden, boolean animated) {
        this.toolbarHidden = toolbarHidden;
        doLayout(animated);
    }

    public UIToolbar getToolbar() {
        return toolbar;
    }

    public void setDelegate(UINavigationControllerDelegate delegate) {
        this.delegate = delegate;
    }

    public UINavigationControllerDelegate getDelegate() {
        return this.delegate;
    }

    @Override
    public void viewWillAppear(boolean animated) {
        getTopViewController().viewWillAppear(animated);
    }

    @Override
    public void viewDidAppear(boolean animated) {
        getTopViewController().viewDidAppear(animated);
    }

    @Override
    public void viewWillDisappear(boolean animated) {
        getTopViewController().viewWillDisappear(animated);
    }

    @Override
    public void viewDidDisappear(boolean animated) {
        getTopViewController().viewDidDisappear(animated);
    }

    private void doExchange(UIViewController old, UIViewController cur, boolean animated) {
        if (!isViewVisible())
            return;

        if (old != null)
            old.viewWillDisappear(animated);
        if (cur != null)
            cur.viewWillAppear(animated);

        if (delegate != null)
            delegate.willShowViewController(this, getTopViewController(), animated);

        doLayout(animated);

        if (delegate != null)
            delegate.didShowViewController(this, getTopViewController(), animated);

        if (old != null)
            old.viewDidDisappear(animated);
        if (cur != null)
            cur.viewDidAppear(animated);
    }

    private boolean isViewVisible() {
        if (!isViewLoaded())
            return false;
        UIView v = getView();
        if (v == null)
            return false;
        while ((v = v.getSuperview()) != null)
            if ((v instanceof UIWindow) && UIApplication.sharedApplication().getKeyWindow() == v)
                return true;
        return false;
    }

    @Override
    void doLayout(boolean animated) {
        /* Remove old visual items */
        UIView thisview = getView();
        for (UIView child : thisview.getSubviews())
            child.removeFromSuperview();

        /* Calculate metrics */
        CGRect frame = thisview.getBounds();
        float barsheight = (navigationBarHidden || navigationBar.isTranslucent()) ? 0 : navigationBar.getFrame().size.height;
        frame.size.height -= barsheight + (toolbarHidden ? 0 : toolbar.getFrame().size.height);
        frame.origin.y += barsheight;

        /* Add base view */
        UIView childview = getTopViewController().getView();
        childview.setFrame(frame);
        thisview.addSubview(childview);
        getTopViewController().doLayout(animated);

        /* Add other elements */
        if (!navigationBarHidden)
            thisview.addSubview(navigationBar);
        if (!toolbarHidden)
            thisview.addSubview(toolbar);

        /* Update display */
        thisview.setNeedsDisplay();
    }
}
