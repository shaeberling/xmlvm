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

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.crossmobile.source.ctype.CLibrary;
import org.crossmobile.source.out.COut;
import org.crossmobile.source.out.JavaOut;

public class SkeletonCreator extends Task {

    private File    sdkpath;
    private File    output;
    private boolean debug;
    private String  option;


    public void setSdkpath(File sdkpath) {
        this.sdkpath = sdkpath;
    }

    public void setOutput(File output) {
        this.output = output;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setOption(String option) {
        this.option = option;
    }

    @Override
    public void execute() throws BuildException {
        if (sdkpath == null)
            throw new BuildException("Parameter sdkpath should be defined.");
        if (output == null)
            throw new BuildException("Parameter output should be defined.");
        if (option == null)
            throw new BuildException("Parameter option should be defined.");

        if (!sdkpath.isDirectory())
            throw new BuildException("Not valid sdkpath directory: " + sdkpath);
        if (!sdkpath.getName().equals("Frameworks"))
            throw new BuildException(
                    "SDK directory \""
                            + sdkpath.getPath()
                            + "\" should be named \"Frameworks\", e.g. it should be something like /Developer/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS4.3.sdk/System/Library/Frameworks");

        output.mkdirs();
        if (!output.isDirectory())
            throw new BuildException("Output directory " + output.getPath()
                    + " should be a directorty.");

        CLibrary library = CLibrary.construct("org.xmlvm.ios", sdkpath, debug);

        if (option.equals("gen-c-wrappers")) {
            COut out = new COut(output.getPath().concat(File.separator + "c" + File.separator));
            out.generate(library);
        } else if (option.equals("gen-java-wrappers")) {
            JavaOut out = new JavaOut(output.getPath().concat(
                    File.separator + "java" + File.separator));
            out.generate(library);
            out.report();
        } else {
            JavaOut out = new JavaOut(output.getPath().concat(
                    File.separator + "java" + File.separator));
            out.generate(library);
            out.report();

            COut cout = new COut(output.getPath().concat(File.separator + "c" + File.separator));
            cout.generate(library);
        }

    }
}
