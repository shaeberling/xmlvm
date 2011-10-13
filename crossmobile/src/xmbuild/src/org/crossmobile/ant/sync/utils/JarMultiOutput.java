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

import org.crossmobile.ant.sync.utils.JarUtils.JarPathEntry;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import org.apache.tools.ant.BuildException;

public class JarMultiOutput {

    private final JarOutputStream out;
    private final Set<String> entries = new HashSet<String>();

    public static JarMultiOutput getOutput(File dest) {
        try {
            return new JarMultiOutput(dest);
        } catch (IOException ex) {
            throw new BuildException("Unable to process destination file " + dest.getPath());
        }
    }

    public JarMultiOutput(File dest) throws IOException {
        out = new JarOutputStream(new FileOutputStream(dest));
    }

    public void close() {
        if (out != null)
            try {
                out.close();
            } catch (IOException ex) {
            }
    }

    public void attachFile(File infile, String srctype, String destaction, String namespace, boolean skipBlackList) throws IOException {
        JarFile injar = null;
        try {
            FileUtils.checkFileIsValid(infile, srctype);
            System.out.println(destaction + " " + infile.getName());
            injar = new JarFile(infile);
            BlackList blacklist = new BlackList(injar, namespace, skipBlackList);
            for (JarPathEntry entry : JarUtils.getListOfEntries(injar, "")) // All files
                if (!blacklist.matches(entry)) {
                    String entryname = entry.toString();
                    if (!entries.contains(entryname)) {
                        entries.add(entryname);
                        out.putNextEntry(new JarEntry(entry.toString()));
                        FileUtils.copyStream(injar.getInputStream(entry.getEntry()), out);
                        out.closeEntry();
                    }
                }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (injar != null)
                try {
                    injar.close();
                } catch (IOException ex) {
                }
        }
    }
}
