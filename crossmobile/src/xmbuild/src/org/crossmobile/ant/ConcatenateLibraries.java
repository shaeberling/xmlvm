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
import org.crossmobile.ant.sync.utils.JarMultiOutput;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class ConcatenateLibraries extends Task {

    private File destfile;
    private String list = "";
    private String namespace = "";
    private Set<String> blacklist = new HashSet<String>();
    private boolean skipBlackList = false;

    public void setDest(File dest) {
        this.destfile = dest;
    }

    public void setList(String list) {
        this.list = list;
    }

    public void setBlacklist(String blacklist) {
        StringTokenizer tk = new StringTokenizer(blacklist, ":");
        while (tk.hasMoreElements())
            this.blacklist.add(tk.nextToken().trim().toLowerCase());
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setSkipBlackList(boolean skipBlackList) {
        this.skipBlackList = skipBlackList;
    }

    @Override
    public void execute() throws BuildException {
        if (destfile == null)
            throw new BuildException("Should define destination file");
        if (!destfile.getName().toLowerCase().endsWith(".jar"))
            throw new BuildException("Destination file should be JAR");

        File newfile = new File(destfile.getPath() + ".new");

        // Creating new application file
        JarMultiOutput out = JarMultiOutput.getOutput(newfile);
        try {
            out.attachFile(destfile, "Plugin", "Attaching", namespace, skipBlackList);
        } catch (IOException ex) {
            out.close();
            System.err.println("Error while proccessing application " + destfile.getPath());
            throw new BuildException(ex);
        }

        // Attaching all libraries
        for (File source : FileUtils.getFileList(this, list)) {
            String signature = source.getName().toLowerCase();
            if (!blacklist.contains(signature) && signature.endsWith(".jar"))
                try {
                    out.attachFile(source, "Plugin", "Attaching", namespace, skipBlackList);
                } catch (IOException ex) {
                    out.close();
                    System.err.println("Error while proccessing file " + source.getPath());
                    throw new BuildException(ex);
                }
        }
        out.close();
        if (!newfile.renameTo(destfile))
            throw new BuildException("Unable to rename destination file " + newfile.getPath());
    }
}
