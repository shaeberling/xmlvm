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

import com.panayotis.xm.frontend.project.properties.MultiBooleanProperty;
import java.util.Properties;

public class LibrariesProperty extends MultiBooleanProperty {

    public LibrariesProperty(Properties xmlvm) {
        super("xmlvm.lib", xmlvm, new String[]{"lib1", "lib2"}, new boolean[]{false, false});
    }

    @Override
    public String getTitle() {
        return "Application libraries";
    }

    @Override
    public String getHelp() {
        return "Add extra libraries required for the project.\nIn order to create an android project, then the android\nlibrary should be present like this:\nxmlvm.lib=android";
    }
}
