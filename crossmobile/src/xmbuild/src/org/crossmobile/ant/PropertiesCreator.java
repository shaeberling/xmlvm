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
import org.crossmobile.ant.sync.utils.InfoPlist;
import org.crossmobile.ant.sync.utils.Templates;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class PropertiesCreator extends Task {

    private Properties prop;
    private File outdir;
    private String mainclass;

    public void setInput(File input) {
        prop = new Properties();
        try {
            prop.load(new InputStreamReader(new FileInputStream(input), "UTF-8"));
        } catch (Exception ex) {
            throw new BuildException(ex);
        }
    }

    public void setOutdir(File output) {
        this.outdir = output;
    }

    public void setMainclass(String mainclass) {
        this.mainclass = mainclass;
    }

    @Override
    public void execute() throws BuildException {
        if (prop == null)
            throw new BuildException("Input file is missing");
        if (outdir == null)
            throw new BuildException("Output directory is missing");
        if (mainclass == null)
            throw new BuildException("Definition of mainclass is missing");
        System.out.println("Writing CrossMobile properties files");
        outdir.mkdirs();

        // Write Info.plist file
        InfoPlist info = new InfoPlist(Templates.INFO_PLIST);
        info.setApplication(prop("bundle.displayname", "CrossMobile"));
        info.setIdentifier(prop("bundle.identifier", "com.mycompany"));
        info.setVersion(prop("bundle.version", "1.0"));
        info.setDisplayName(prop("bundle.displayname", "CrossMobile"));
        info.setStatusBarHidden(prop("statusbarhidden", "false"));
        info.setPrerenderIcon(prop("prerenderedicon", "false"));
        info.setApplicationExits(prop("applicationexits", "true"));
        info.setDefaultOrientation(prop("orientations.initial", "UIInterfaceOrientationPortrait"));
        info.setSupportedOrientations(prop("orientations.supported", "UIInterfaceOrientationPortrait"));
        info.setFonts(prop("appfonts", ""));
        FileUtils.writeFile(new File(outdir, "Info.plist"), info.toString());

        // Write crossmobile.properties
        Properties crossmobile = new Properties();
        crossmobile.setProperty("xm.splash.delay", prop("xm.splash.delay", "1"));
        crossmobile.setProperty("xm.map.apikey", prop("xm.map.apikey", ""));
        crossmobile.setProperty("xm.about", prop("xm.about", ""));
        crossmobile.setProperty("xm.device", prop("xmlvm.project", "iphone"));
        crossmobile.setProperty("xm.statusbar.hidden", InfoPlist.toBoolean(prop("statusbarhidden", "false")));
        crossmobile.setProperty("xm.main.class", mainclass);
        crossmobile.setProperty("xm.display.name", prop("bundle.displayname", "CrossMobile"));
        try {
            crossmobile.store(new FileWriter(outdir + File.separator + "crossmobile.properties"), null);
        } catch (IOException ex) {
            throw new BuildException(ex);
        }
    }

    private String prop(String property, String defval) {
        String val = prop.getProperty(property);
        return val == null || val.trim().isEmpty() ? defval : val.trim();
    }
}
