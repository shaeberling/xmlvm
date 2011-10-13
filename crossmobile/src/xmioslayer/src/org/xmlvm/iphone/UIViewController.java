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
import org.crossmobile.ios2a.ImplementationError;

public class UIViewController extends UIResponder {

    private UIView view;
    private String title;
    private boolean wantsFullScreenLayout;
    private UINavigationItem navigationItem;
    private UIBarButtonItem editButtonItem;
    private boolean editing;
    private boolean hidesBottomBarWhenPushed;
    private UITabBarItem tabBarItem;
    private ArrayList<UIBarButtonItem> toolbarItems;
    private UIViewController pcontroller;

    public UIViewController() {
        super();
        editing = false;
    }

    public void loadView() {
        view = new UIView(UIScreen.mainScreen().getApplicationFrame());
    }

    public boolean isViewLoaded() {
        return view != null;
    }

    public void viewDidLoad() {
    }

    public void viewDidUnload() {
    }

    public void viewWillAppear(boolean animated) {
    }

    public void viewDidAppear(boolean animated) {
    }

    public void viewWillDisappear(boolean animated) {
    }

    public void viewDidDisappear(boolean animated) {
    }

    public boolean shouldAutorotateToInterfaceOrientation(int UIInterfaceOrientation) {
        if (UIInterfaceOrientation == org.xmlvm.iphone.UIInterfaceOrientation.Portrait)
            return true;
        return false;
    }

    public UIView rotatingHeaderView() {
        return null;
    }

    public UIView rotatingFooterView() {
        return null;
    }

    public void willRotateToInterfaceOrientation(int UIInterfaceOrientation, double duration) {
    }

    public void willAnimateRotationToInterfaceOrientation(int UIInterfaceOrientation,
            double duration) {
    }

    public void didRotateFromInterfaceOrientation(int UIInterfaceOrientation) {
    }

    public void willAnimateFirstHalfOfRotationToInterfaceOrientation(int UIInterfaceOrientation,
            double duration) {
    }

    public void didAnimateFirstHalfOfRotationToInterfaceOrientation(int UIInterfaceOrientation) {
    }

    public void willAnimateSecondHalfOfRotationFromInterfaceOrientation(
            int orienuiInterfaceOrientationtation, double duration) {
    }

    public void didReceiveMemoryWarning() {
    }

    public UIView getView() {
        if (view == null) {
            loadView();
            view.controller = this;
            view.setFrame(UIScreen.mainScreen().getApplicationFrame());
            viewDidLoad();
        }
        return view;
    }

    public void setView(UIView view) {
        if (this.view != null) // Old
            view.controller = null;
        this.view = view;
        if (this.view != null) // New
            view.controller = this;
    }

    public String getTitle() {
        return title;
    }

    public final void setTitle(String title) {
        if (tabBarItem != null)
            tabBarItem.setTitle(title);
        if (navigationItem != null)
            navigationItem.setTitle(title);
        this.title = title;
    }

    public boolean wantsFullScreenLayout() {
        return wantsFullScreenLayout;
    }

    public void setWantsFullScreenLayout(boolean wantsFullScreenLayout) {
        this.wantsFullScreenLayout = wantsFullScreenLayout;
    }

    public int getInterfaceOrientation() {
        return UIScreen.orientation;
    }

    public UINavigationController getNavigationController() {
        UIViewController p = this;
        while (p != null) {
            if (p instanceof UINavigationController)
                return (UINavigationController) p;
            p = p.pcontroller;
        }
        return null;
    }

    public UINavigationItem getNavigationItem() {
        return navigationItem;
    }

    public UIBarButtonItem editButtonItem() {
        return editButtonItem;
    }

    public boolean isEditing() {
        return editing;
    }

    public void setEditing(boolean editing) {
        this.editing = editing;
    }

    public void setEditing(boolean editing, boolean animated) {
    }

    /* Simulator ignores animations */
    public boolean hidesBottomBarWhenPushed() {
        return hidesBottomBarWhenPushed;
    }

    /* Simulator ignores animations */
    public void setHidesBottomBarWhenPushed(boolean hidesBottomBarWhenPushed) {
        this.hidesBottomBarWhenPushed = hidesBottomBarWhenPushed;
    }

    public UITabBarController getTabBarController() {
        UIViewController p = pcontroller;
        while (p != null) {
            if (p instanceof UITabBarController)
                return (UITabBarController) p;
            p = p.pcontroller;
        }
        return null;
    }

    public UIViewController getParentController() {
        return pcontroller;
    }

    void setParentController(UIViewController parentController) {
        pcontroller = parentController;
    }

    public UITabBarItem getTabBarItem() {
        if (tabBarItem == null) {
            tabBarItem = new UITabBarItem();
            tabBarItem.setTitle(getTitle());
        }
        return tabBarItem;
    }

    public void setTabBarItem(UITabBarItem tabBarItem) {
        this.tabBarItem = tabBarItem;
    }

    public ArrayList<UIBarButtonItem> getToolbarItems() {
        return toolbarItems;
    }

    public void setToolbarItems(ArrayList<UIBarButtonItem> items) {
        setToolbarItems(items, false);
    }

    public void setToolbarItems(ArrayList<UIBarButtonItem> items, boolean animated) {
        this.toolbarItems = items;
        if (pcontroller != null)
            pcontroller.doLayout(animated);
    }

    public void presentModalViewController(UIViewController modalViewController, boolean animated) {
        throw new ImplementationError();
    }

    public void dismissModalViewControllerAnimated(boolean animated) {
        throw new ImplementationError();
    }

    void doLayoutWithDelegates(boolean animated) {
        doLayout(animated);
    }

    void doLayout(boolean animated) {
    }
}
