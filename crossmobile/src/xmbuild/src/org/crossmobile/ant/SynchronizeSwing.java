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

package org.crossmobile.ant;

import org.crossmobile.ant.sync.utils.FileUtils;
import org.crossmobile.ant.sync.utils.JarUtils;
import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class SynchronizeSwing extends Task {

    private File sysout;
    private File appout;
    private String iosresources;

    public void setAppout(File appout) {
        this.appout = appout;
    }

    public void setIosresources(String iosresources) {
        this.iosresources = iosresources;
    }

    public void setSysout(File sysout) {
        this.sysout = sysout;
    }

    @Override
    public void execute() throws BuildException {
        if (sysout == null)
            throw new BuildException("Property sysout should be set");

        if (appout == null)
            throw new BuildException("Property appout should be set");

        if (iosresources == null)
            iosresources = "";

        System.out.println("Copying resources for Swing emulator");
        FileUtils futils = new FileUtils(this);
        futils.copyAppFiles(appout, iosresources);
        JarUtils.copySelfResources(this, "res/drawable", sysout);
        JarUtils.copySelfResources(this, "artwork/images", sysout);
        JarUtils.copySelfResources(this, "artwork/sounds", sysout);
    }
}
