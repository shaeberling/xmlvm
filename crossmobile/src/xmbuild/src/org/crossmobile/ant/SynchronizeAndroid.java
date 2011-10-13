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

import org.crossmobile.ant.sync.utils.AndroidUtils;
import org.crossmobile.ant.sync.utils.FileUtils;
import org.crossmobile.ant.sync.utils.JarUtils;
import org.crossmobile.ant.sync.utils.Templates;
import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class SynchronizeAndroid extends Task {

    private String mainclass;
    private String displayname;
    private File generated;
    private File sysout;
    private File appout;
    private String iosresources;
    private boolean ignore = false;
    private boolean debuggable = false;
    private boolean prerender = false;

    public void setMainClass(String mainclass) {
        this.mainclass = mainclass;
    }

    public String getMainClass() {
        return mainclass;
    }

    public void setDisplayName(String displayname) {
        this.displayname = displayname;
    }

    public String getDisplayName() {
        return displayname;
    }

    public void setGenerated(File generated) {
        this.generated = generated;
    }

    public File getGenerated() {
        return generated;
    }

    public void setSysout(File resources) {
        this.sysout = resources;
    }

    public void setIOSResources(String iosresources) {
        this.iosresources = iosresources;
    }

    public void setAppout(File appout) {
        this.appout = appout;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public void setDebuggable(boolean debuggable) {
        this.debuggable = debuggable;
    }

    public void setPrerender(boolean prerender) {
        this.prerender = prerender;
    }

    @Override
    public void execute() throws BuildException {
        if (ignore)
            return;

        AndroidUtils.checkValidClass("mainclass", mainclass);
        int lastdot = mainclass.lastIndexOf('.');
        String packname = mainclass.substring(0, lastdot);
        String classname = mainclass.substring(lastdot + 1);
        System.out.println("Updating project with main class " + mainclass);

        if (displayname == null)
            throw new BuildException("Property displayname should be set");

        if (generated == null)
            generated = new File(getProject().getBaseDir(), "gen");

        if (sysout == null)
            sysout = new File(getProject().getBaseDir(), "res");

        if (appout == null)
            appout = new File(getProject().getBaseDir(), "asset");

        if (iosresources == null)
            iosresources = "";

        FileUtils futils = new FileUtils(this);
        AndroidUtils autils = new AndroidUtils(this);

        autils.createActivities(packname, classname, sysout);
        autils.updateAndroidManifest(packname, classname, debuggable);
        futils.copyAppFiles(appout, iosresources);
        JarUtils.copySelfResources(this, "res", sysout);
        futils.copyIcon(Templates.ICONDRAWABLE, sysout, iosresources, prerender);
    }
}
