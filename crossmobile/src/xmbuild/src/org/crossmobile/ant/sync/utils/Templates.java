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

package org.crossmobile.ant.sync.utils;

public class Templates {

    public static final String PACKAGENAME_ANCHOR = "__PACKAGENAME__";
    public static final String MAINACTIVITY_ANCHOR = "__MAINACTIVITY__";
    //
    public static final String ICONDRAWABLE = "appicon";
    //
    public static final String MAINTAG = ".MAIN";
    public static final String MAPTAG = ".XMMAP";
    //
    public static final String MAINACTIVITY_TEMPLATE =
            "/* AUTO-GENERATED FILE. DO NOT MODIFY.\n"
            + " *\n"
            + " * This file was created automatically by the CrossMobile tools.\n"
            + " * It should not be modified by hand.\n"
            + " */\n"
            + "\n"
            + "package " + PACKAGENAME_ANCHOR + ";\n"
            + "public class " + MAINACTIVITY_ANCHOR + " extends org.crossmobile.backend.android.MainActivity {\n"
            + "}\n";
    public static final String MAINLAYOUT_TEMPLATE = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
            + "<org.crossmobile.backend.android.MainView android:id=\"@+id/mainview\" xmlns:android=\"http://schemas.android.com/apk/res/android\" "
            + "android:layout_width=\"fill_parent\" android:layout_height=\"fill_parent\"/>\n";
    public static final String INFO_PLIST =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n"
            + "<plist version=\"1.0\">\n"
            + "<dict>\n"
            + "\t<key>UIStatusBarHidden</key>\n"
            + "\t<PROPERTY_STATUSBARHIDDEN/>\n"
            + "\t<key>UIPrerenderedIcon</key>\n"
            + "\t<PROPERTY_PRERENDEREDICON/>\n"
            + "\t<key>UIApplicationExitsOnSuspend</key>\n"
            + "\t<PROPERTY_APPLICATIONEXITS/>\n"
            + "\t<key>CFBundleDevelopmentRegion</key>\n"
            + "\t<string>English</string>\n"
            + "\t<key>CFBundleDisplayName</key>\n"
            + "\t<string>PROPERTY_BUNDLEDISPLAYNAME</string>\n"
            + "\t<key>CFBundleExecutable</key>\n"
            + "\t<string>XMLVM_APP</string>\n"
            + "\t<key>CFBundleIdentifier</key>\n"
            + "\t<string>PROPERTY_BUNDLEIDENTIFIER</string>\n"
            + "\t<key>CFBundleInfoDictionaryVersion</key>\n"
            + "\t<string>6.0</string>\n"
            + "\t<key>CFBundleName</key>\n"
            + "\t<string>XMLVM_APP</string>\n"
            + "\t<key>CFBundlePackageType</key>\n"
            + "\t<string>APPL</string>\n"
            + "\t<key>CFBundleSignature</key>\n"
            + "\t<string>????</string>\n"
            + "\t<key>CFBundleVersion</key>\n"
            + "\t<string>PROPERTY_BUNDLEVERSION</string>\n"
            + "\t<key>LSRequiresIPhoneOS</key>\n"
            + "\t<true/>\n"
            + "\t<key>UIInterfaceOrientation</key>\n"
            + "\t<string>PROPERTY_INTERFACE_ORIENTATION</string>\n"
            + "PROPERTY_SUPPORTED_INTERFACE_ORIENTATIONS\n"
            + "PROPERTY_FONTS\n"
            + "</dict>\n"
            + "</plist>\n";
}
