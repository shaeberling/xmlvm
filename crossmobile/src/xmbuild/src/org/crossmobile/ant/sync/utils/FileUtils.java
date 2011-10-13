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

import org.crossmobile.ant.paintshop.IconPainter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

public class FileUtils {

    private final Task task;
    private ArrayList<File> resourcelist;
    private static final Set<String> blacklist;

    static {
        blacklist = new HashSet<String>();
        blacklist.add("Default.png");
        blacklist.add("Default@2x.png");
        blacklist.add("Icon.png");
        blacklist.add("Icon@2x.png");
    }

    public FileUtils(Task task) {
        this.task = task;
    }

    public static List<File> getFileList(Task task, String items) {
        ArrayList<File> result = new ArrayList<File>();
        StringTokenizer tk = new StringTokenizer(items, ":");
        while (tk.hasMoreTokens())
            try {
                String item = tk.nextToken();
                File file = new File(item);
                if (!file.isAbsolute())
                    file = new File(task.getProject().getBaseDir(), item).getAbsoluteFile();
                result.add(file.getCanonicalFile());
            } catch (IOException ex) {
            }
        return result;
    }

    /* Probably use getFileList instead? What about the "/" character? */
    public List<File> getResourceList(String iosresources) {
        if (resourcelist == null) {
            resourcelist = new ArrayList<File>();
            StringTokenizer tk = new StringTokenizer(iosresources, ":");
            while (tk.hasMoreTokens()) {
                String item = tk.nextToken();
                File file = new File(item);
                if (!file.isAbsolute())
                    file = new File(task.getProject().getBaseDir(), item).getAbsoluteFile();
                try {
                    file = file.getCanonicalFile();
                } catch (IOException ex) {
                }
                if (item.endsWith("/"))
                    resourcelist.addAll(Arrays.asList(file.listFiles()));
                else
                    resourcelist.add(file);
            }
        }
        return resourcelist;
    }

    public void copyAppFiles(File outputdir, String iosresources) {
        for (File source : getResourceList(iosresources))
            if (!blacklist.contains(source.getName()))
                FileUtils.copyInSync(source, new File(outputdir, source.getName()));
    }

    private File findIcon(String img, String iosresources) {
        img += ".png";
        for (File item : getResourceList(iosresources))
            if (item.getName().equals(img))
                return item;
        return null;
    }

    public void copyIcon(String toimg, File sysout, String iosresources, boolean prerender) {
        File parent = new File(sysout, "drawable");
        if ((!parent.isDirectory()) && (!parent.mkdirs()))
            return;
        File outfile = new File(parent, toimg + ".png");
        File infile = findIcon("Icon@2x", iosresources);
        if (infile == null)
            infile = findIcon("Icon", iosresources);
        if (infile != null)
            if (prerender)
                copyFile(infile, outfile);
            else
                IconPainter.createIcon(infile, outfile);
        // keep default application icon if nothing ofund
    }

    public static void createFile(File fout, String content) {
        File parent = fout.getParentFile();
        parent.mkdirs();
        if (!parent.isDirectory())
            throw new BuildException("Unable to create directory " + parent.getPath());
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(fout));
            out.write(content);
        } catch (IOException ex) {
            throw new BuildException(ex);
        } finally {
            if (out != null)
                try {
                    out.close();
                } catch (IOException ex1) {
                }
        }
    }

    public static void copyFile(File inF, File outF) {
        checkFileIsValid(inF, "Source");
        if (outF == null)
            throw new BuildException("Destination file should not be null");
        if (outF.isFile())
            delete(outF);

        outF.getParentFile().mkdirs();
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(inF);
            out = new FileOutputStream(outF);
            copyStream(in, out);
        } catch (Exception ex) {
            throw new BuildException(ex);
        } finally {
            closeStreams(in, out);
        }
    }

    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        if (in != null && out != null) {
            byte buffer[] = new byte[1024];
            int length = 0;
            while ((length = in.read(buffer)) > 0)
                out.write(buffer, 0, length);
        }
    }

    public static void copyReaders(Reader in, Writer out) throws IOException {
        if (in != null && out != null) {
            char buffer[] = new char[1024];
            int length = 0;
            while ((length = in.read(buffer)) > 0)
                out.write(buffer, 0, length);
        }
    }

    public static void closeStreams(InputStream in, OutputStream out) {
        if (in != null)
            try {
                in.close();
            } catch (IOException ex) {
            }
        if (out != null)
            try {
                out.close();
            } catch (IOException ex) {
            }
    }

    public static void delete(File current) {
        if (current.isDirectory())
            for (File sub : current.listFiles())
                delete(sub);
        if (current.exists() && (!current.delete()))
            throw new BuildException("Unable to remove " + current.getPath());
    }

    public static void copyInSync(File source, File target) {
        if (target.exists() && source.isDirectory() != target.isDirectory())
            FileUtils.delete(target);

        if (source.isDirectory()) {
            if ((!target.exists()) && (!target.mkdirs()))
                throw new BuildException("Unable to create directory " + target.getPath());
            for (File item : source.listFiles())
                copyInSync(item, new File(target, item.getName()));
        } else if (source.length() != target.length())
            copyFile(source, target);
    }

    public static void checkFileIsValid(File test, String type) {
        if (test == null)
            throw new BuildException(type + " file should not be null");
        if (!test.isFile())
            throw new BuildException(type + " '" + test.getPath() + "' is not a file");
        if (!test.canRead())
            throw new BuildException(type + " file '" + test.getPath() + "' is not readable");
    }

    public static String readFile(File input) {
        if (input == null)
            throw new BuildException("Input file not defined");
        StringWriter out = new StringWriter();
        Reader in = null;
        try {
            in = new InputStreamReader(new FileInputStream(input), "UTF-8");
            copyReaders(in, out);
            return out.toString();
        } catch (Exception ex) {
            throw new BuildException(ex);
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException ex) {
                }
        }
    }

    public static void writeFile(File output, String data) {
        if (output == null)
            throw new BuildException("Input file not defined");
        StringReader in = new StringReader(data == null ? "" : data);
        Writer out = null;
        try {
            out = new OutputStreamWriter(new FileOutputStream(output), "UTF-8");
            copyReaders(in, out);
        } catch (Exception ex) {
            throw new BuildException(ex);
        } finally {
            if (out != null)
                try {
                    out.close();
                } catch (IOException ex) {
                }
        }
    }
}
