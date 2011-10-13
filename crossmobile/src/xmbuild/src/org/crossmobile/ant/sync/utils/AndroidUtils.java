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

import org.crossmobile.ant.SynchronizeAndroid;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.tools.ant.BuildException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class AndroidUtils extends XMLUtils {

    private static final String MANIFEST = "AndroidManifest.xml";
    private static final String MAINSUFFIX = "XMActivity";
    private final SynchronizeAndroid task;


    public AndroidUtils(SynchronizeAndroid task) {
        this.task = task;
    }

    public void createActivities(String packname, String mainclassname, File sysout) {
        String mainactivity = mainclassname + MAINSUFFIX;

        /* Create main activity */
        String main_ac = Templates.MAINACTIVITY_TEMPLATE.replace(
                Templates.PACKAGENAME_ANCHOR, packname).replace(
                Templates.MAINACTIVITY_ANCHOR, mainactivity);
        FileUtils.createFile(getClassFile(packname, mainactivity), main_ac);

        /* Create main layout */
        FileUtils.createFile(new File(sysout, "layout/main.xml"), Templates.MAINLAYOUT_TEMPLATE);
    }

    public void updateAndroidManifest(String packname, String classname, boolean debuggable) {
        String fullclassname = packname + "." + classname;
        String icon = "@drawable/" + Templates.ICONDRAWABLE;

        try {
            // Load AndroidManifest.xml file
            File outfile = new File(task.getProject().getBaseDir(), MANIFEST);
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(outfile);
            Node manifest = doc.getDocumentElement();

            // Fix "manifest" node
            setAttribute(manifest, "package", packname);

            // Fix application node
            Node application = getNodeWithName(manifest, "application");
            setAttribute(application, "android:label", task.getDisplayName());
            setAttribute(application, "android:icon", icon);
            setAttribute(application, "android:theme", "@android:style/Theme.NoTitleBar");
            setAttribute(application, "android:debuggable", debuggable ? "true" : "false");

            for (Node activity : getNodesWithName(application, "activity", true))
                if (actionNameEndsWith(activity, Templates.MAINTAG))
                    setMainActivity(activity, fullclassname + MAINSUFFIX);
                else if (actionNameEndsWith(activity, Templates.MAPTAG))
                    setMapActivity(activity, packname);
            saveXML(doc, outfile);
        } catch (Exception ex) {
            if (ex instanceof BuildException)
                throw (BuildException) ex;
            else
                throw new BuildException(ex);
        }
    }

    private boolean actionNameEndsWith(Node activity, String tag) {
        Node intent = getNodeWithName(activity, "intent-filter");
        if (intent == null)
            return false;
        Node action = getNodeWithName(intent, "action");
        if (action == null)
            return false;
        String name = getAttribute(action, "android:name");
        return name != null && name.endsWith(tag);
    }

    private void setMainActivity(Node activity, String fullclassname) {
        setAttribute(activity, "android:launchMode", "singleInstance");
        setAttribute(activity, "android:name", fullclassname);
        setAttribute(activity, "android:screenOrientation", "portrait");

        Node intent = getNodeWithName(activity, "intent-filter");
        setAttribute(getNodeWithName(intent, "action"), "android:name", "android.intent.action.MAIN");
        setAttribute(getNodeWithName(intent, "category"), "android:name", "android.intent.category.LAUNCHER");
    }

    private void setMapActivity(Node activity, String packname) {
        setAttribute(activity, "android:launchMode", "singleInstance");
        setAttribute(activity, "android:name", "org.crossmobile.ios2a.IOSMapActivity");
        setAttribute(activity, "android:screenOrientation", "portrait");

        Node intent = getNodeWithName(activity, "intent-filter");
        setAttribute(getNodeWithName(intent, "action"), "android:name", packname + Templates.MAPTAG);
        setAttribute(getNodeWithName(intent, "category"), "android:name", "android.intent.category.DEFAULT");
    }


    private File getClassFile(String packname, String classname) {
        return new File(new File(task.getGenerated(), packname.replace('.', File.separatorChar)), classname + ".java");
    }

    private String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    private String lower(String s) {
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }
}
