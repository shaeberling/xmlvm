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
import android.text.Editable;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TextKeyListener;
import android.text.method.TransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import org.crossmobile.ios2a.MainActivity;

public class UITextField extends UIView {

    private int autocapitalizationType = UITextAutocapitalizationType.None;
    private int autocorrectionType = UITextAutocorrectionType.Default;
    private boolean enablesReturnKeyAutomatically = false;
    private int keyboardAppearance = UIKeyboardAppearance.Default;
    private int keyboardType = UIKeyboardType.Default;
    private int returnKeyType = UIReturnKeyType.Default;
    private TransformationMethod defaultTransfMode;
    private UIFont font = UIFont.fontWithNameSize("Arial", 16);
    private int borderStyle = UITextBorderStyle.RoundedRect;
    private UITextFieldDelegate delegate;
    private boolean adjustsFontSizeToFitWidth = false;

    public UITextField() {
        this(CGRect.Zero());
    }

    public UITextField(CGRect rect) {
        super(rect);
    }

    public int getAutocapitalizationType() {
        return autocapitalizationType;
    }

    public void setAutocapitalizationType(int UITextAutocapitalizationType) {
        this.autocapitalizationType = UITextAutocapitalizationType;
    }

    public int getAutocorrectionType() {
        return autocorrectionType;
    }

    public void setAutocorrectionType(int UITextAutocorrectionType) {
        this.autocorrectionType = UITextAutocorrectionType;
    }

    public boolean isEnablesReturnKeyAutomatically() {
        return enablesReturnKeyAutomatically;
    }

    public void setEnablesReturnKeyAutomatically(boolean enablesReturnKeyAutomatically) {
        this.enablesReturnKeyAutomatically = enablesReturnKeyAutomatically;
    }

    public int getKeyboardAppearance() {
        return keyboardAppearance;
    }

    public void setKeyboardAppearance(int UIKeyboardAppearance) {
        this.keyboardAppearance = UIKeyboardAppearance;
    }

    public int getKeyboardType() {
        return keyboardType;
    }

    public void setKeyboardType(int UIKeyboardType) {
        this.keyboardType = UIKeyboardType;
    }

    public int getReturnKeyType() {
        return returnKeyType;
    }

    public void setReturnKeyType(int UIReturnKeyType) {
        this.returnKeyType = UIReturnKeyType;
    }

    public boolean isSecureTextEntry() {
        return ((EditText) xm_model()).getTransformationMethod() instanceof PasswordTransformationMethod;
    }

    public void setSecureTextEntry(boolean secureTextEntry) {
        if (isSecureTextEntry() == secureTextEntry)
            return;
        EditText model = (EditText) xm_model();
        if (secureTextEntry) {
            defaultTransfMode = model.getTransformationMethod();
            model.setTransformationMethod(new PasswordTransformationMethod());
        } else
            model.setTransformationMethod(defaultTransfMode);
    }

    public void setText(String text) {
        ((EditText) xm_model()).setText(text);
    }

    public String getText() {
        return ((EditText) xm_model()).getText().toString();
    }

    public void setTextColor(UIColor color) {
        ((EditText) xm_model()).setTextColor(color.getModelColor());
    }

    public UIColor getTextColor() {
        return new UIColor(((EditText) xm_model()).getTextColors().getDefaultColor());
    }

    public void setBorderStyle(int UITextBorderStyle) {
        this.borderStyle = UITextBorderStyle;
    }

    public int getBorderStyle() {
        return borderStyle;
    }

    public void setFont(UIFont font) {
        this.font = font;
    }

    public UIFont getFont() {
        return font;
    }

    public boolean isAdjustsFontSizeToFitWidth() {
        return adjustsFontSizeToFitWidth;
    }

    public void setAdjustsFontSizeToFitWidth(boolean adjustsFontSizeToFitWidth) {
        this.adjustsFontSizeToFitWidth = adjustsFontSizeToFitWidth;
    }

    public int getTextAlignment() {
        return UITextAlignment.gravityToAlignment(((EditText) xm_model()).getGravity());
    }

    public void setTextAlignment(int UITextAlignment) {
        ((EditText) xm_model()).setGravity(org.xmlvm.iphone.UITextAlignment.alignmentToGravity(UITextAlignment));
    }

    public void setPlaceholder(String placeholder) {
        ((EditText) xm_model()).setHint(placeholder);
    }

    public String getPlaceholder() {
        return ((EditText) xm_model()).getHint().toString();
    }

    public void setDelegate(UITextFieldDelegate delegate) {
        this.delegate = delegate;
    }

    public UITextFieldDelegate getDelegate() {
        return delegate;
    }

    @Override
    public boolean resignFirstResponder() {
        if (delegate == null && (!delegate.textFieldShouldEndEditing(this)))
            return false;
        InputMethodManager manager = (InputMethodManager) MainActivity.current.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(xm_model().getWindowToken(), 0);
        if (delegate != null)
            delegate.textFieldDidEndEditing(this);
        return false;
    }

    @Override
    public boolean becomeFirstResponder() {
        if (delegate != null && (!delegate.textFieldShouldBeginEditing(this)))
            return false;
        InputMethodManager manager = (InputMethodManager) MainActivity.current.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.showSoftInput(xm_model(), InputMethodManager.SHOW_FORCED);
        if (delegate != null)
            delegate.textFieldDidEndEditing(this);

//        InputMethodManager imm = (InputMethodManager) MainActivity.current.getSystemService(Context.INPUT_METHOD_SERVICE);
//        android.content.res.Configuration config = MainActivity.current.getResources().getConfiguration();
//        if (config.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
// //            xm_model().requestFocus();
// //            xm_model().setSelected(true);
//            imm.restartInput(xm_model());
//            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//        }
        return true;
    }

    @Override
    View createModelObject(Context cx) {
        EditText text = new EditText(cx);
        text.setKeyListener(new TextKeyListener(TextKeyListener.Capitalize.NONE, true) {

            @Override
            public boolean onKeyDown(View view, Editable content, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)
                        && delegate != null && delegate.textFieldShouldReturn(UITextField.this))
                    return true;
                else
                    return super.onKeyDown(view, content, keyCode, event);
            }
        });
        return text;
    }
}
