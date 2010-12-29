/* Copyright (c) 2002-2011 by XMLVM.org
 *
 * Project Info:  http://www.xmlvm.org
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package android.content.res;


import java.util.Locale;

/**
 * This class describes all device configuration information that can
 * impact the resources the application retrieves.  This includes both
 * user-specified configuration options (locale and scaling) as well
 * as dynamic device configuration (various types of input devices).
 */
public final class Configuration implements Comparable<Configuration> {
    /**
     * Current user preference for the scaling factor for fonts, relative
     * to the base density scaling.
     */
    public float fontScale;

    /**
     * IMSI MCC (Mobile Country Code).  0 if undefined.
     */
    public int mcc;
    
    /**
     * IMSI MNC (Mobile Network Code).  0 if undefined.
     */
    public int mnc;
    
    /**
     * Current user preference for the locale.
     */
    public Locale locale;

    /**
     * Locale should persist on setting.  This is hidden because it is really
     * questionable whether this is the right way to expose the functionality.
     * @hide
     */
    public boolean userSetLocale;

    public static final int SCREENLAYOUT_SIZE_MASK = 0x0f;
    public static final int SCREENLAYOUT_SIZE_UNDEFINED = 0x00;
    public static final int SCREENLAYOUT_SIZE_SMALL = 0x01;
    public static final int SCREENLAYOUT_SIZE_NORMAL = 0x02;
    public static final int SCREENLAYOUT_SIZE_LARGE = 0x03;
    
    public static final int SCREENLAYOUT_LONG_MASK = 0x30;
    public static final int SCREENLAYOUT_LONG_UNDEFINED = 0x00;
    public static final int SCREENLAYOUT_LONG_NO = 0x10;
    public static final int SCREENLAYOUT_LONG_YES = 0x20;
    
    /**
     * Special flag we generate to indicate that the screen layout requires
     * us to use a compatibility mode for apps that are not modern layout
     * aware.
     * @hide
     */
    public static final int SCREENLAYOUT_COMPAT_NEEDED = 0x10000000;
    
    /**
     * Bit mask of overall layout of the screen.  Currently there are two
     * fields:
     * <p>The {@link #SCREENLAYOUT_SIZE_MASK} bits define the overall size
     * of the screen.  They may be one of
     * {@link #SCREENLAYOUT_SIZE_SMALL}, {@link #SCREENLAYOUT_SIZE_NORMAL},
     * or {@link #SCREENLAYOUT_SIZE_LARGE}.
     * 
     * <p>The {@link #SCREENLAYOUT_LONG_MASK} defines whether the screen
     * is wider/taller than normal.  They may be one of
     * {@link #SCREENLAYOUT_LONG_NO} or {@link #SCREENLAYOUT_LONG_YES}.
     */
    public int screenLayout;
    
    public static final int TOUCHSCREEN_UNDEFINED = 0;
    public static final int TOUCHSCREEN_NOTOUCH = 1;
    public static final int TOUCHSCREEN_STYLUS = 2;
    public static final int TOUCHSCREEN_FINGER = 3;
    
    /**
     * The kind of touch screen attached to the device.
     * One of: {@link #TOUCHSCREEN_NOTOUCH}, {@link #TOUCHSCREEN_STYLUS}, 
     * {@link #TOUCHSCREEN_FINGER}. 
     */
    public int touchscreen;
    
    public static final int KEYBOARD_UNDEFINED = 0;
    public static final int KEYBOARD_NOKEYS = 1;
    public static final int KEYBOARD_QWERTY = 2;
    public static final int KEYBOARD_12KEY = 3;
    
    /**
     * The kind of keyboard attached to the device.
     * One of: {@link #KEYBOARD_NOKEYS}, {@link #KEYBOARD_QWERTY},
     * {@link #KEYBOARD_12KEY}.
     */
    public int keyboard;
    
    public static final int KEYBOARDHIDDEN_UNDEFINED = 0;
    public static final int KEYBOARDHIDDEN_NO = 1;
    public static final int KEYBOARDHIDDEN_YES = 2;
    /** Constant matching actual resource implementation. {@hide} */
    public static final int KEYBOARDHIDDEN_SOFT = 3;
    
    /**
     * A flag indicating whether any keyboard is available.  Unlike
     * {@link #hardKeyboardHidden}, this also takes into account a soft
     * keyboard, so if the hard keyboard is hidden but there is soft
     * keyboard available, it will be set to NO.  Value is one of:
     * {@link #KEYBOARDHIDDEN_NO}, {@link #KEYBOARDHIDDEN_YES}.
     */
    public int keyboardHidden;
    
    public static final int HARDKEYBOARDHIDDEN_UNDEFINED = 0;
    public static final int HARDKEYBOARDHIDDEN_NO = 1;
    public static final int HARDKEYBOARDHIDDEN_YES = 2;
    
    /**
     * A flag indicating whether the hard keyboard has been hidden.  This will
     * be set on a device with a mechanism to hide the keyboard from the
     * user, when that mechanism is closed.  One of:
     * {@link #HARDKEYBOARDHIDDEN_NO}, {@link #HARDKEYBOARDHIDDEN_YES}.
     */
    public int hardKeyboardHidden;
    
    public static final int NAVIGATION_UNDEFINED = 0;
    public static final int NAVIGATION_NONAV = 1;
    public static final int NAVIGATION_DPAD = 2;
    public static final int NAVIGATION_TRACKBALL = 3;
    public static final int NAVIGATION_WHEEL = 4;
    
    /**
     * The kind of navigation method available on the device.
     * One of: {@link #NAVIGATION_NONAV}, {@link #NAVIGATION_DPAD},
     * {@link #NAVIGATION_TRACKBALL}, {@link #NAVIGATION_WHEEL}.
     */
    public int navigation;
    
    public static final int ORIENTATION_UNDEFINED = 0;
    public static final int ORIENTATION_PORTRAIT = 1;
    public static final int ORIENTATION_LANDSCAPE = 2;
    public static final int ORIENTATION_SQUARE = 3;
    
    /**
     * Overall orientation of the screen.  May be one of
     * {@link #ORIENTATION_LANDSCAPE}, {@link #ORIENTATION_PORTRAIT},
     * or {@link #ORIENTATION_SQUARE}.
     */
    public int orientation;
    
    /**
     * Construct an invalid Configuration.  You must call {@link #setToDefaults}
     * for this object to be valid.  {@more}
     */
    public Configuration() {
        setToDefaults();
    }

    /**
     * Makes a deep copy suitable for modification.
     */
    public Configuration(Configuration o) {
        fontScale = o.fontScale;
        mcc = o.mcc;
        mnc = o.mnc;
        if (o.locale != null) {
            locale = (Locale) o.locale.clone();
        }
        userSetLocale = o.userSetLocale;
        touchscreen = o.touchscreen;
        keyboard = o.keyboard;
        keyboardHidden = o.keyboardHidden;
        hardKeyboardHidden = o.hardKeyboardHidden;
        navigation = o.navigation;
        orientation = o.orientation;
        screenLayout = o.screenLayout;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("{ scale=");
        sb.append(fontScale);
        sb.append(" imsi=");
        sb.append(mcc);
        sb.append("/");
        sb.append(mnc);
        sb.append(" loc=");
        sb.append(locale);
        sb.append(" touch=");
        sb.append(touchscreen);
        sb.append(" keys=");
        sb.append(keyboard);
        sb.append("/");
        sb.append(keyboardHidden);
        sb.append("/");
        sb.append(hardKeyboardHidden);
        sb.append(" nav=");
        sb.append(navigation);
        sb.append(" orien=");
        sb.append(orientation);
        sb.append(" layout=");
        sb.append(screenLayout);
        sb.append('}');
        return sb.toString();
    }

    /**
     * Set this object to the system defaults.
     */
    public void setToDefaults() {
        fontScale = 1;
        mcc = mnc = 0;
        locale = Locale.getDefault();
        userSetLocale = false;
        touchscreen = TOUCHSCREEN_UNDEFINED;
        keyboard = KEYBOARD_UNDEFINED;
        keyboardHidden = KEYBOARDHIDDEN_UNDEFINED;
        hardKeyboardHidden = HARDKEYBOARDHIDDEN_UNDEFINED;
        navigation = NAVIGATION_UNDEFINED;
        orientation = ORIENTATION_UNDEFINED;
        screenLayout = SCREENLAYOUT_SIZE_UNDEFINED;
    }

    /** {@hide} */
    @Deprecated public void makeDefault() {
        setToDefaults();
    }
    
    /**
     * Copy the fields from delta into this Configuration object, keeping
     * track of which ones have changed.  Any undefined fields in
     * <var>delta</var> are ignored and not copied in to the current
     * Configuration.
     * @return Returns a bit mask of the changed fields, as per
     * {@link #diff}.
     */
    /*
    public int updateFrom(Configuration delta) {
        int changed = 0;
        if (delta.fontScale > 0 && fontScale != delta.fontScale) {
            changed |= ActivityInfo.CONFIG_FONT_SCALE;
            fontScale = delta.fontScale;
        }
        if (delta.mcc != 0 && mcc != delta.mcc) {
            changed |= ActivityInfo.CONFIG_MCC;
            mcc = delta.mcc;
        }
        if (delta.mnc != 0 && mnc != delta.mnc) {
            changed |= ActivityInfo.CONFIG_MNC;
            mnc = delta.mnc;
        }
        if (delta.locale != null
                && (locale == null || !locale.equals(delta.locale))) {
            changed |= ActivityInfo.CONFIG_LOCALE;
            locale = delta.locale != null
                    ? (Locale) delta.locale.clone() : null;
        }
        if (delta.userSetLocale && (!userSetLocale || ((changed & ActivityInfo.CONFIG_LOCALE) != 0)))
        {
            userSetLocale = true;
            changed |= ActivityInfo.CONFIG_LOCALE;
        }
        if (delta.touchscreen != TOUCHSCREEN_UNDEFINED
                && touchscreen != delta.touchscreen) {
            changed |= ActivityInfo.CONFIG_TOUCHSCREEN;
            touchscreen = delta.touchscreen;
        }
        if (delta.keyboard != KEYBOARD_UNDEFINED
                && keyboard != delta.keyboard) {
            changed |= ActivityInfo.CONFIG_KEYBOARD;
            keyboard = delta.keyboard;
        }
        if (delta.keyboardHidden != KEYBOARDHIDDEN_UNDEFINED
                && keyboardHidden != delta.keyboardHidden) {
            changed |= ActivityInfo.CONFIG_KEYBOARD_HIDDEN;
            keyboardHidden = delta.keyboardHidden;
        }
        if (delta.hardKeyboardHidden != HARDKEYBOARDHIDDEN_UNDEFINED
                && hardKeyboardHidden != delta.hardKeyboardHidden) {
            changed |= ActivityInfo.CONFIG_KEYBOARD_HIDDEN;
            hardKeyboardHidden = delta.hardKeyboardHidden;
        }
        if (delta.navigation != NAVIGATION_UNDEFINED
                && navigation != delta.navigation) {
            changed |= ActivityInfo.CONFIG_NAVIGATION;
            navigation = delta.navigation;
        }
        if (delta.orientation != ORIENTATION_UNDEFINED
                && orientation != delta.orientation) {
            changed |= ActivityInfo.CONFIG_ORIENTATION;
            orientation = delta.orientation;
        }
        if (delta.screenLayout != SCREENLAYOUT_SIZE_UNDEFINED
                && screenLayout != delta.screenLayout) {
            changed |= ActivityInfo.CONFIG_SCREEN_LAYOUT;
            screenLayout = delta.screenLayout;
        }
        
        return changed;
    }
*/
    
    /**
     * Return a bit mask of the differences between this Configuration
     * object and the given one.  Does not change the values of either.  Any
     * undefined fields in <var>delta</var> are ignored.
     * @return Returns a bit mask indicating which configuration
     * values has changed, containing any combination of
     * {@link android.content.pm.ActivityInfo#CONFIG_FONT_SCALE
     * PackageManager.ActivityInfo.CONFIG_FONT_SCALE},
     * {@link android.content.pm.ActivityInfo#CONFIG_MCC
     * PackageManager.ActivityInfo.CONFIG_MCC},
     * {@link android.content.pm.ActivityInfo#CONFIG_MNC
     * PackageManager.ActivityInfo.CONFIG_MNC},
     * {@link android.content.pm.ActivityInfo#CONFIG_LOCALE
     * PackageManager.ActivityInfo.CONFIG_LOCALE},
     * {@link android.content.pm.ActivityInfo#CONFIG_TOUCHSCREEN
     * PackageManager.ActivityInfo.CONFIG_TOUCHSCREEN},
     * {@link android.content.pm.ActivityInfo#CONFIG_KEYBOARD
     * PackageManager.ActivityInfo.CONFIG_KEYBOARD},
     * {@link android.content.pm.ActivityInfo#CONFIG_NAVIGATION
     * PackageManager.ActivityInfo.CONFIG_NAVIGATION},
     * {@link android.content.pm.ActivityInfo#CONFIG_ORIENTATION
     * PackageManager.ActivityInfo.CONFIG_ORIENTATION}, or
     * {@link android.content.pm.ActivityInfo#CONFIG_SCREEN_LAYOUT
     * PackageManager.ActivityInfo.CONFIG_SCREEN_LAYOUT}.
     */
    /*
    public int diff(Configuration delta) {
        int changed = 0;
        if (delta.fontScale > 0 && fontScale != delta.fontScale) {
            changed |= ActivityInfo.CONFIG_FONT_SCALE;
        }
        if (delta.mcc != 0 && mcc != delta.mcc) {
            changed |= ActivityInfo.CONFIG_MCC;
        }
        if (delta.mnc != 0 && mnc != delta.mnc) {
            changed |= ActivityInfo.CONFIG_MNC;
        }
        if (delta.locale != null
                && (locale == null || !locale.equals(delta.locale))) {
            changed |= ActivityInfo.CONFIG_LOCALE;
        }
        if (delta.touchscreen != TOUCHSCREEN_UNDEFINED
                && touchscreen != delta.touchscreen) {
            changed |= ActivityInfo.CONFIG_TOUCHSCREEN;
        }
        if (delta.keyboard != KEYBOARD_UNDEFINED
                && keyboard != delta.keyboard) {
            changed |= ActivityInfo.CONFIG_KEYBOARD;
        }
        if (delta.keyboardHidden != KEYBOARDHIDDEN_UNDEFINED
                && keyboardHidden != delta.keyboardHidden) {
            changed |= ActivityInfo.CONFIG_KEYBOARD_HIDDEN;
        }
        if (delta.hardKeyboardHidden != HARDKEYBOARDHIDDEN_UNDEFINED
                && hardKeyboardHidden != delta.hardKeyboardHidden) {
            changed |= ActivityInfo.CONFIG_KEYBOARD_HIDDEN;
        }
        if (delta.navigation != NAVIGATION_UNDEFINED
                && navigation != delta.navigation) {
            changed |= ActivityInfo.CONFIG_NAVIGATION;
        }
        if (delta.orientation != ORIENTATION_UNDEFINED
                && orientation != delta.orientation) {
            changed |= ActivityInfo.CONFIG_ORIENTATION;
        }
        if (delta.screenLayout != SCREENLAYOUT_SIZE_UNDEFINED
                && screenLayout != delta.screenLayout) {
            changed |= ActivityInfo.CONFIG_SCREEN_LAYOUT;
        }
        
        return changed;
    }
*/
    /**
     * Determine if a new resource needs to be loaded from the bit set of
     * configuration changes returned by {@link #updateFrom(Configuration)}.
     * 
     * @param configChanges The mask of changes configurations as returned by
     * {@link #updateFrom(Configuration)}.
     * @param interestingChanges The configuration changes that the resource
     * can handled, as given in {@link android.util.TypedValue#changingConfigurations}.
     * 
     * @return Return true if the resource needs to be loaded, else false.
     */
    /*
    public static boolean needNewResources(int configChanges, int interestingChanges) {
        return (configChanges & (interestingChanges|ActivityInfo.CONFIG_FONT_SCALE)) != 0;
    }
    */
    
    /**
     * Parcelable methods
     */
    public int describeContents() {
        return 0;
    }

    /*
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(fontScale);
        dest.writeInt(mcc);
        dest.writeInt(mnc);
        if (locale == null) {
            dest.writeInt(0);
        } else {
            dest.writeInt(1);
            dest.writeString(locale.getLanguage());
            dest.writeString(locale.getCountry());
            dest.writeString(locale.getVariant());
        }
        if(userSetLocale) {
            dest.writeInt(1);
        } else {
            dest.writeInt(0);
        }
        dest.writeInt(touchscreen);
        dest.writeInt(keyboard);
        dest.writeInt(keyboardHidden);
        dest.writeInt(hardKeyboardHidden);
        dest.writeInt(navigation);
        dest.writeInt(orientation);
        dest.writeInt(screenLayout);
    }

    public static final Parcelable.Creator<Configuration> CREATOR
            = new Parcelable.Creator<Configuration>() {
        public Configuration createFromParcel(Parcel source) {
            return new Configuration(source);
        }

        public Configuration[] newArray(int size) {
            return new Configuration[size];
        }
    };
*/
    
    /**
     * Construct this Configuration object, reading from the Parcel.
     */
    /*
    private Configuration(Parcel source) {
        fontScale = source.readFloat();
        mcc = source.readInt();
        mnc = source.readInt();
        if (source.readInt() != 0) {
            locale = new Locale(source.readString(), source.readString(),
                    source.readString());
        }
        userSetLocale = (source.readInt()==1);
        touchscreen = source.readInt();
        keyboard = source.readInt();
        keyboardHidden = source.readInt();
        hardKeyboardHidden = source.readInt();
        navigation = source.readInt();
        orientation = source.readInt();
        screenLayout = source.readInt();
    }
*/
    
    public int compareTo(Configuration that) {
        int n;
        float a = this.fontScale;
        float b = that.fontScale;
        if (a < b) return -1;
        if (a > b) return 1;
        n = this.mcc - that.mcc;
        if (n != 0) return n;
        n = this.mnc - that.mnc;
        if (n != 0) return n;
        n = this.locale.getLanguage().compareTo(that.locale.getLanguage());
        if (n != 0) return n;
        n = this.locale.getCountry().compareTo(that.locale.getCountry());
        if (n != 0) return n;
        n = this.locale.getVariant().compareTo(that.locale.getVariant());
        if (n != 0) return n;
        n = this.touchscreen - that.touchscreen;
        if (n != 0) return n;
        n = this.keyboard - that.keyboard;
        if (n != 0) return n;
        n = this.keyboardHidden - that.keyboardHidden;
        if (n != 0) return n;
        n = this.hardKeyboardHidden - that.hardKeyboardHidden;
        if (n != 0) return n;
        n = this.navigation - that.navigation;
        if (n != 0) return n;
        n = this.orientation - that.orientation;
        if (n != 0) return n;
        n = this.screenLayout - that.screenLayout;
        //if (n != 0) return n;
        return n;
    }

    public boolean equals(Configuration that) {
        if (that == null) return false;
        if (that == this) return true;
        return this.compareTo(that) == 0;
    }

    public boolean equals(Object that) {
        try {
            return equals((Configuration)that);
//        } catch (ClassCastException e) {
        } catch (Exception e) {
        }
        return false;
    }
    
    public int hashCode() {
        return ((int)this.fontScale) + this.mcc + this.mnc
                + this.locale.hashCode() + this.touchscreen
                + this.keyboard + this.keyboardHidden + this.hardKeyboardHidden
                + this.navigation + this.orientation + this.screenLayout;
    }
}
