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

package com.panayotis.xm.frontend.project.properties.impl;

import com.panayotis.xm.frontend.project.properties.FreeListProperty;
import java.util.Properties;

public class FontsProperty extends FreeListProperty {

    public FontsProperty(Properties xmlvm) {
        super("appfonts", xmlvm, new String[]{});
    }

    @Override
    public String getTitle() {
        return "Application fonts";
    }

    @Override
    public String getHelp() {
        return "Use custom fonts for your application. This feature is supported for\niOS version 3.2 onwards. You need to provide the TTF font file name. \nIf more than one font is desired, you need to separate each TTF file\nname with a colon ( : ).";
    }
}
