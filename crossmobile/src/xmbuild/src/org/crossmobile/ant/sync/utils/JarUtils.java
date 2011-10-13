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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.AntTypeDefinition;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.ComponentHelper;
import org.apache.tools.ant.Task;

public class JarUtils {

    public static JarFile getSelfJar(Task t) {
        Class classtype = SynchronizeAndroid.class;
        String jarname = File.separator + "xmbuild.jar";

        @SuppressWarnings("UseOfObsoleteCollectionType")
        java.util.Hashtable definitions = ComponentHelper.getComponentHelper(t.getProject()).getTaskDefinitions();
        for (Object o : definitions.keySet())
            if (definitions.get(o).equals(classtype)) {
                AntTypeDefinition def = ComponentHelper.getComponentHelper(t.getProject()).getDefinition((String) o);
                AntClassLoader cl = ((AntClassLoader) def.getClassLoader());

                String classpath = cl.getClasspath();
                StringTokenizer tok = new StringTokenizer(classpath, File.pathSeparator);
                String path;
                while (tok.hasMoreTokens()) {
                    path = tok.nextToken();
                    if (path.toLowerCase().endsWith(jarname))
                        try {
                            File jarfile = new File(path);
                            FileUtils.checkFileIsValid(jarfile, "JAR file");
                            return new JarFile(jarfile);
                        } catch (IOException ex) {
                        }
                }
                return null;
            }
        return null;
    }

    public static List<JarPathEntry> getListOfEntries(JarFile jarpath, String dirpath) {
        String dirpathslashed = dirpath.endsWith("/") ? dirpath : (dirpath.length() == 0 ? "" : dirpath + "/");

        int dirpathsize = dirpathslashed.length();

        ArrayList<JarPathEntry> res = new ArrayList<JarPathEntry>();
        Enumeration<JarEntry> entries = jarpath.entries();
        JarEntry entry;
        String path;
        String name;
        while (entries.hasMoreElements()) {
            entry = entries.nextElement();
            name = entry.getName();
            if (!name.endsWith("/") && name.startsWith(dirpathslashed)) {
                int lastslash = name.lastIndexOf("/");
                path = lastslash > dirpathsize ? name.substring(dirpathsize, lastslash) : "";
                res.add(new JarPathEntry(dirpath, path, name.substring(lastslash + 1)));
            }
        }
        return res;
    }

    public static void copySelfResources(Task task, String prefix, File sysout) {
        JarFile self = JarUtils.getSelfJar(task);
        if (self == null)
            throw new BuildException("Unable to find xmbuild.jar.");
        List<JarPathEntry> entries = JarUtils.getListOfEntries(self, prefix);
        if (entries == null || entries.isEmpty())
            throw new BuildException("Unable to find '" + prefix + "' directory inside xmbuild.jar");

        File resdir = sysout;
        InputStream in = null;
        FileOutputStream out = null;
        try {
            for (JarPathEntry entry : entries) {
                in = self.getInputStream(entry.getEntry());
                File parent = entry.path.length() == 0 ? resdir : new File(resdir, entry.path);
                parent.mkdirs();
                out = new FileOutputStream(new File(parent, entry.name));
                FileUtils.copyStream(in, out);

                // Required, since we re in a loop
                FileUtils.closeStreams(in, out);
                in = null;
                out = null;
            }
        } catch (Exception ex) {
            throw new BuildException(ex);
        } finally {
            FileUtils.closeStreams(in, out);
        }
    }

    public static void copyPlugin(File outfile, File infile, String srctype, String destaction, String namespace, boolean skipBlackList) {
        JarMultiOutput out = null;
        try {
            out = JarMultiOutput.getOutput(outfile);
            out.attachFile(infile, srctype, destaction, namespace, skipBlackList);
        } catch (IOException ex) {
            throw new BuildException(ex);
        } finally {
            if (out != null)
                out.close();
        }
    }

    public static class JarPathEntry {

        private final String home;
        public final String path;
        public final String name;
        private final String toString;

        private JarPathEntry(String home, String path, String name) {
            this.home = home;
            this.path = path;
            this.name = name;
            toString = (home.length() == 0 ? "" : home + "/") + (path.length() == 0 ? "" : path + "/") + name;
        }

        @Override
        public String toString() {
            return toString;
        }

        public ZipEntry getEntry() {
            return new ZipEntry(toString());
        }
    }
}
