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

package com.panayotis.xm.frontend.project;

import com.panayotis.xm.frontend.project.properties.ProjectProperty;
import com.panayotis.xm.frontend.project.properties.impl.ApplicationExitsProperty;
import com.panayotis.xm.frontend.project.properties.impl.BackendProperty;
import com.panayotis.xm.frontend.project.properties.impl.DefaultTargetProperty;
import com.panayotis.xm.frontend.project.properties.impl.FontsProperty;
import com.panayotis.xm.frontend.project.properties.impl.IdentifierProperty;
import com.panayotis.xm.frontend.project.properties.impl.InitialOrientationProperty;
import com.panayotis.xm.frontend.project.properties.impl.LibrariesProperty;
import com.panayotis.xm.frontend.project.properties.impl.NameProperty;
import com.panayotis.xm.frontend.project.properties.impl.PrerenderIconProperty;
import com.panayotis.xm.frontend.project.properties.impl.ProjectTypeProperty;
import com.panayotis.xm.frontend.project.properties.impl.RandomSeedProperty;
import com.panayotis.xm.frontend.project.properties.impl.ResourcesProperty;
import com.panayotis.xm.frontend.project.properties.impl.StatusBarHiddenProperty;
import com.panayotis.xm.frontend.project.properties.impl.SupportedOrientationsProperty;
import com.panayotis.xm.frontend.project.properties.impl.TrimmerProperty;
import com.panayotis.xm.frontend.project.properties.impl.VersionProperty;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class Project {

    private String path;
    private String name;
    private ArrayList<ProjectProperty> properties = new ArrayList<ProjectProperty>();
    private JPanel comp;

    public Project(String path) {
        this.path = path;
        populateName();
    }

    public boolean isWritable() {
        return true;
    }

    public boolean exists() {
        return new File(path + File.separator + "xmlvm.properties").exists();
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    private void populateName() {
        try {
            name = "NewProject";
            Properties xmlvm = new Properties();
            xmlvm.load(new FileInputStream(getPropFile()));
            name = xmlvm.getProperty(NameProperty.NAMEPROPERTY);
        } catch (IOException ex) {
        }
    }

    private void populateItems() {
        if (properties.size() > 0)
            return;

        Properties xmlvm = new Properties();
        try {
            xmlvm.load(new FileInputStream(getPropFile()));
        } catch (IOException ex) {
        }
        properties.clear();
        properties.add(new NameProperty(xmlvm, name));
        properties.add(new ProjectTypeProperty(xmlvm));
        properties.add(new DefaultTargetProperty(xmlvm));
        properties.add(new LibrariesProperty(xmlvm));
        properties.add(new ResourcesProperty(xmlvm));
        properties.add(new VersionProperty(xmlvm));
        properties.add(new IdentifierProperty(xmlvm, name));
        properties.add(new PrerenderIconProperty(xmlvm));
        properties.add(new StatusBarHiddenProperty(xmlvm));
        properties.add(new ApplicationExitsProperty(xmlvm));
        properties.add(new BackendProperty(xmlvm));
        properties.add(new FontsProperty(xmlvm));
        properties.add(new InitialOrientationProperty(xmlvm));
        properties.add(new SupportedOrientationsProperty(xmlvm));
        properties.add(new TrimmerProperty(xmlvm));
        properties.add(new RandomSeedProperty(xmlvm));
    }

    public void storeProperties() {
        populateItems();
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(getPropFile()));
            for (ProjectProperty prop : properties) {
                out.write(prop.getPrefix());
                out.newLine();
                out.write(prop.getName());
                out.write("=");
                out.write(prop.getValue());
                out.newLine();
                out.newLine();
            }
        } catch (IOException ex) {
        } finally {
            if (out != null)
                try {
                    out.close();
                } catch (IOException ex) {
                }
        }
    }

    private String getPropFile() {
        return path + File.separator + "xmlvm.properties";
    }

    public JComponent getVisuals() {
        populateItems();
        if (comp == null) {
            comp = new JPanel();
            comp.setLayout(new BoxLayout(comp, BoxLayout.Y_AXIS));
            for (ProjectProperty prop : properties) {
                JPanel cp = new JPanel(new BorderLayout());
                cp.add(prop.getVisuals(), BorderLayout.WEST);
                comp.add(cp);
                cp.setBorder(new MatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
            }

            comp.setBorder(new EmptyBorder(8, 8, 8, 8));
        }
        return comp;
    }
}
