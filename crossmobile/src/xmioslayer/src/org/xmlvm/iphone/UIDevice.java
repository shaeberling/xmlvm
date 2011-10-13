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
import android.os.Build;
import android.telephony.TelephonyManager;
import java.util.UUID;
import org.crossmobile.ios2a.ImplementationError;
import org.crossmobile.ios2a.MainActivity;

public class UIDevice extends NSObject {

    private static final UIDevice current = new UIDevice();
    private boolean proximityMonitoringEnabled = false;
    private boolean batteryMonitoringEnabled = false;
    private int orientation = UIDeviceOrientation.Portrait;
    private String udid;
    private int userinterfaceidiom;

    private UIDevice() {
        String device = System.getProperty("xm.device");
        if (device == null)
            device = "iphone";
        device = device.toLowerCase();
        if (device.equals("iphone"))
            userinterfaceidiom = UIUserInterfaceIdiom.Phone;
        else if (device.equals("ipad"))
            userinterfaceidiom = UIUserInterfaceIdiom.Pad;
        else
            throw new IllegalStateException("Unknown device " + device);
    }

    public static UIDevice currentDevice() {
        return current;
    }

    public boolean getMultitaskingSupported() {
        return false;
    }

    public String getUniqueIdentifier() {
        if (udid == null) {
            Context cx = MainActivity.current;
            final TelephonyManager tm = (TelephonyManager) cx.getSystemService(Context.TELEPHONY_SERVICE);
            final String tmDevice, tmSerial, tmPhone, androidId;
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = "" + android.provider.Settings.Secure.getString(cx.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
            udid = deviceUuid.toString();
        }
        return udid;
    }

    public String getName() {
        return "Android CrossMobile for " + Build.MODEL;
    }

    public String getSystemName() {
        return "Android";
    }

    public String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    public String getModel() {
        return Build.MODEL;
    }

    public String getLocalizedModel() {
        return Build.MODEL;
    }

    public int getUserInterfaceIdiom() {
        return userinterfaceidiom;
    }

    public int getOrientation() {
        return orientation;
    }

    public boolean isGeneratingDeviceOrientationNotifications() {
        return false;
    }

    public void beginGeneratingDeviceOrientationNotifications() {
        throw new ImplementationError();
    }

    public void endGeneratingDeviceOrientationNotifications() {
        throw new ImplementationError();
    }

    public float getBatteryLevel() {
        if (batteryMonitoringEnabled)
            return 0.5f;
        else
            return -1;
    }

    public boolean isBatteryMonitoringEnabled() {
        return batteryMonitoringEnabled;
    }

    public void setBatteryMonitoringEnabled(boolean batteryMonitoringEnabled) {
        this.batteryMonitoringEnabled = batteryMonitoringEnabled;
    }

    public int getBatteryState() {
        return UIDeviceBatteryState.Unknown;
    }

    public boolean isProximityMonitoringEnabled() {
        return proximityMonitoringEnabled;
    }

    public void setProximityMonitoringEnabled(boolean proximityMonitoringEnabled) {
        this.proximityMonitoringEnabled = proximityMonitoringEnabled;
    }

    public boolean getProximityState() {
        return false;
    }
}
