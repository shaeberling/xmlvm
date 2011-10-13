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

public class UINavigationItem extends NSObject {

    private String title;
    private UIView titleView;
    private String prompt;
    private UILabel promptView;
    private boolean hidesBackButton;
    private UIBarButtonItem backBarButtonItem;
    private UIBarButtonItem leftBarButtonItem;
    private UIBarButtonItem rightBarButtonItem;
    private UINavigationBar bar;               // Back reference

    public UINavigationItem(String title) {
        setTitle(title);
        backBarButtonItem = new UIBarButtonItem("Back", UIBarButtonItemStyle.Bordered,
                new UIBarButtonItemDelegate() {

                    public void clicked() {
                        if (bar != null)
                            if (bar.nbcontroller != null)
                                bar.nbcontroller.popViewControllerAnimated(true);
                            else
                                bar.popNavigationItemAnimated(true);
                    }
                });
        //    ((UIBarButtonItemRenderer) backBarButtonItem.getCustomView().xmlvmGetRenderer()).setBack(true);
        backBarButtonItem.setTitle(backBarButtonItem.getTitle()); // Needed to update the button size
    }

    void setToolbar(UINavigationBar bar) {
        this.bar = bar;
//        backBarButtonItem.setBar(bar);
//        if (leftBarButtonItem != null)
//            leftBarButtonItem.setBar(bar);
//        if (rightBarButtonItem != null)
//            rightBarButtonItem.setBar(bar);
    }

    public UIBarButtonItem getBackBarButtonItem() {
        if (leftBarButtonItem != null)
            return null;
        return backBarButtonItem;
    }

    public void setBackBarButtonItem(UIBarButtonItem backBarButtonItem) {
        this.backBarButtonItem = backBarButtonItem;
        //  backBarButtonItem.setBar(bar);
        if (bar != null)
            bar.updateViews();
    }

    public boolean hidesBackButton() {
        return hidesBackButton;
    }

    public void setHidesBackButton(boolean hidesBackButton) {
        setHidesBackButton(hidesBackButton, false);
    }

    public void setHidesBackButton(boolean hidesBackButton, boolean animated) {
        this.hidesBackButton = hidesBackButton;
        if (bar != null)
            bar.updateViews();
    }

    public UIBarButtonItem getLeftBarButtonItem() {
        return leftBarButtonItem;
    }

    public void setLeftBarButtonItem(UIBarButtonItem leftBarButtonItem) {
        setLeftBarButtonItem(leftBarButtonItem, false);
    }

    public void setLeftBarButtonItem(UIBarButtonItem leftBarButtonItem, boolean animated) {
        //       leftBarButtonItem.setBar(bar);
        if (bar != null)
            bar.updateViews();
    }

    public UIBarButtonItem getRightBarButtonItem() {
        return rightBarButtonItem;
    }

    public void setRightBarButtonItem(UIBarButtonItem rightBarButtonItem) {
        setRightBarButtonItem(rightBarButtonItem, false);
    }

    public void setRightBarButtonItem(UIBarButtonItem rightBarButtonItem, boolean animated) {
//        rightBarButtonItem.setBar(bar);
        if (bar != null)
            bar.updateViews();
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        // ignore set, if it is the same
        if (this.prompt == null ? prompt == null : this.prompt.equals(prompt))
            return;

        this.prompt = prompt;
        // set promptView if prompt is set
        if (prompt != null) {
            if (promptView == null)
                promptView = getLabel(true);
            promptView.setText(prompt);
        } else
            promptView = null;
        if (bar != null)
            bar.updateViews();
    }

    UILabel getPromptView() {
        return promptView;
    }

    public String getTitle() {
        return title;
    }

    public final void setTitle(String title) {
        if (this.title == null ? title == null : this.title.equals(title))
            return;
        this.title = title;

        if (title != null) {
            if (titleView == null)
                titleView = getLabel(false);
            if (titleView instanceof UILabel)
                ((UILabel) titleView).setText(title);
        } else if (titleView instanceof UILabel)
            titleView = null;
        if (bar != null)
            bar.updateViews();
    }

    public UIView getTitleView() {
        return titleView;
    }

    public void setTitleView(UIView titleView) {
        if (titleView == this.titleView)
            return;
        this.titleView = titleView;
        if (bar != null)
            bar.updateViews();
    }

    private UILabel getLabel(boolean bigfont) {
        UILabel label = new UILabel();
        label.setBackgroundColor(UIColor.clearColor);
        label.setTextColor(UIColor.whiteColor);
        label.setFont(UIFont.fontWithNameSize("Arial Bold", bigfont ? 19 : 24));
        label.setTextAlignment(UITextAlignment.Center);
        return label;
    }
}
