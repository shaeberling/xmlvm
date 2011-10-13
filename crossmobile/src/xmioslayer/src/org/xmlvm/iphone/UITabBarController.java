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

public class UITabBarController extends UIViewController {

    private UITabBarControllerDelegate delegate;
    private final UITabBar tabBar;
    private ArrayList<UIViewController> viewControllers;
    private ArrayList<UIViewController> customizableViewControllers;
    private int selectedIndex;

    @SuppressWarnings("LeakingThisInConstructor")
    public UITabBarController() {
        super();
        delegate = null;
        tabBar = new UITabBar(new CGRect(0, 0, UIScreen.mainScreen().getApplicationFrame().size.width, 49));
        tabBar.tbcontrol = this;
        viewControllers = null;
        customizableViewControllers = null;
        selectedIndex = 0;
    }

    public ArrayList<UIViewController> getCustomizableViewControllers() {
        return new ArrayList<UIViewController>(customizableViewControllers);
    }

    public void setCustomizableViewControllers(ArrayList<UIViewController> viewControllers) {
        this.customizableViewControllers = new ArrayList<UIViewController>(viewControllers);
    }

    public UITabBarControllerDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(UITabBarControllerDelegate delegate) {
        this.delegate = delegate;
    }

    public UINavigationController getMoreNavigationController() {
        return null;
    }

    public UIViewController getSelectedViewController() {
        return viewControllers.get(selectedIndex);
    }

    public void setSelectedViewController(UIViewController selectedViewController) {
        setSelectedIndex(viewControllers.indexOf(selectedViewController));
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        if (selectedIndex < 0 || selectedIndex >= viewControllers.size())
            throw new ArrayIndexOutOfBoundsException("UITabbar index " + selectedIndex + " is out of bounds");
        if (selectedIndex == this.selectedIndex)
            return;
        this.selectedIndex = selectedIndex; // First this
        tabBar.setSelectedItem(tabBar.getItems().get(selectedIndex)); // Then this for cyclic reference
        doLayoutWithDelegates(false);
    }

    public UITabBar getTabBar() {
        return tabBar;
    }

    public ArrayList<UIViewController> getViewControllers() {
        return new ArrayList<UIViewController>(viewControllers);
    }

    public void setViewControllers(ArrayList<UIViewController> viewControllers) {
        setViewControllers(viewControllers, false);
    }

    public void setViewControllers(ArrayList<UIViewController> viewControllers, boolean animated) {
        ArrayList<UITabBarItem> tabs = new ArrayList<UITabBarItem>();
        this.viewControllers = new ArrayList<UIViewController>(viewControllers);
        for (UIViewController contr : viewControllers) {
            contr.setParentController(this);
            tabs.add(contr.getTabBarItem());
        }
        tabBar.setItems(tabs, animated);
        doLayoutWithDelegates(animated);
    }

    @Override
    void doLayoutWithDelegates(boolean animated) {
        doLayout(animated);
        if (delegate != null)
            delegate.didSelectViewController(this, getSelectedViewController());
    }

    @Override
    void doLayout(boolean animated) {
        /* Remove old visual items */
        UIView thisview = getView();
        for (UIView child : thisview.getSubviews())
            child.removeFromSuperview();

        /* Calculate metrics */
        CGRect frame = thisview.getFrame();
        float barsheight = tabBar.getFrame().size.height;
        frame.size.height -= barsheight;
        frame.origin.y = barsheight;

        /* Add base view */
        UIView childview = getSelectedViewController().getView();
        childview.setFrame(frame);
        thisview.addSubview(childview);
        getSelectedViewController().doLayout(animated);

        /* Add other elements */
        thisview.addSubview(tabBar);

        thisview.setNeedsDisplay();
    }
}
