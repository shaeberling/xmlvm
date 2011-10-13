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
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class CloneLibraries extends Task {

    private File destdir;
    private String list = "";
    private Set<String> blacklist = new HashSet<String>();
    private String namespace = "";
    private boolean skipBlackList = false;

    public void setDest(File dest) {
        this.destdir = dest;
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
        if (destdir == null)
            throw new NullPointerException("Should define destination directory");
        if (destdir.exists())
            FileUtils.delete(destdir);
        destdir.mkdirs();

        for (File in : FileUtils.getFileList(this, list)) {
            String signature = in.getName().toLowerCase();
            if (!blacklist.contains(signature) && signature.endsWith(".jar")) {
                File out = new File(destdir, in.getName());
                JarUtils.copyPlugin(out, in, "Plugin", "Copy Plugin", namespace, skipBlackList);
            }
        }
    }
}
