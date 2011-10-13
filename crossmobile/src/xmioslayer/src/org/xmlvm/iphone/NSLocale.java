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

public class NSLocale extends NSObject {

    private static NSLocale system;
    private static NSLocale current;
    //
    private String language;
    private String country;

    public static NSLocale systemLocale() {
        if (system == null)
            system = new NSLocale("en", "US");
        return system;
    }

    public static NSLocale currentLocale() {
        if (current == null)
            current = new NSLocale(System.getProperty("user.language"), System.getProperty("user.country"));
        return current;
    }

    private NSLocale(String language, String country) {
        this.language = language;
        this.country = country;
    }

    public String localeIdentifier() {
        return language + "_" + country;
    }

    public static ArrayList<String> preferredLanguages() {
        ArrayList<String> result = new ArrayList<String>();
        result.add(currentLocale().language);
        return result;
    }
}
