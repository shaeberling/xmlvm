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

public class SupportedOrientationsProperty extends MultiBooleanProperty {

    public SupportedOrientationsProperty(Properties xmlvm) {
        super("orientations.supported", xmlvm, new String[]{"UIInterfaceOrientationPortrait", "UIInterfaceOrientationPortraitUpsideDown", "UIInterfaceOrientationLandscapeLeft", "UIInterfaceOrientationLandscapeRight"}, new boolean[]{true, false, false, false});
    }

    @Override
    public String getTitle() {
        return "Supported orientations";
    }

    @Override
    public String getHelp() {
        return "Set the supported orientations for this application. \nThis is a colon ( : ) separated list of possible values. \nValid options are the same as with the interfaceorientation parameter. \nFor example, to support only portrait modes, this is how it can be done. \norientations.supported=UIInterfaceOrientationPortrait:UIInterfaceOrientationPortraitUpsideDown\nIf this value is not set, all orientations are supported.";
    }
}
