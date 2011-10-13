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

public class ResourcesProperty extends FreeListProperty {

    public ResourcesProperty(Properties xmlvm) {
        super("xmlvm.resource", xmlvm, new String[]{"resources/"});
    }

    @Override
    public String getTitle() {
        return "Application resources";
    }

    @Override
    public String getHelp() {
        return "Add extra resources required for the project\nThis is a colon separated list of files that will be used verbatim in Xcode\nproject.\nIf a directory is provided, then if the name ends with \"/\", the contents\nof this directory will be used. If not, a verbatim copy of the directory will\nbe used.\nThis can also be used to add extra source files to the project. Only one\nlevel deep will be searched for source files (thus either a file should be\ngiven or a directory ending with \"/\").";
    }
}
