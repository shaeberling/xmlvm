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
    private File    javaoutput;
    private File    coutput;
    private boolean debug;
    private String  option;
    private String objectprefix = "";
    private String packagename = "org.xmlvm.ios";
    private boolean genReport = true;

    public void setSdkpath(File sdkpath) {
        this.sdkpath = sdkpath;
    }

    public void setJavaoutput(File javaoutput) {
        this.javaoutput = javaoutput;
    }

    public void setCoutput(File coutput) {
        this.coutput = coutput;
    }
    
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void setOption(String option) {
        this.option = option;
    }
    
    public void setObjectprefix(String objectprefix) {
        this.objectprefix = objectprefix;
    }
    
    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public void setGenReport(boolean genReport){
        this.genReport =  genReport;
    }
    
    @Override
    public void execute() throws BuildException {
        if (sdkpath == null)
            throw new BuildException("Parameter sdkpath should be defined.");
        if (option == null)
            throw new BuildException("Parameter option should be defined.");

        if (!sdkpath.isDirectory())
            throw new BuildException("Not valid sdkpath directory: " + sdkpath);
        if (!sdkpath.getName().equals("Frameworks"))
            throw new BuildException(
                    "SDK directory \""
                            + sdkpath.getPath()
                            + "\" should be named \"Frameworks\", e.g. it should be something like /Developer/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS4.3.sdk/System/Library/Frameworks");


        CLibrary library = CLibrary.construct(packagename, sdkpath, debug);

        if (option.equals("gen-c-wrapper")) {
            if (coutput == null)
                throw new BuildException("Parameter output should be defined.");
            createCOutputDir();
            generateCWrapper(library);
        } 
        else if (option.equals("gen-java-wrapper")) {
            if (javaoutput == null)
                throw new BuildException("Parameter output should be defined.");
            createJavaOutputDir();
            generateJavaWrapper(library);
        } 
        else {
            if (coutput == null || javaoutput==null)
                throw new BuildException("Parameter output should be defined.");
            createCOutputDir();
            createJavaOutputDir();
            generateJavaWrapper(library);
            generateCWrapper(library);         
        }

    }
    
    private void createJavaOutputDir(){
       javaoutput.mkdirs();
        if (!javaoutput.isDirectory())
            throw new BuildException("Output directory " + javaoutput.getPath()
                    + " should be a directorty.");
    }
    
    private void createCOutputDir(){
        coutput.mkdirs();
        if (!coutput.isDirectory())
            throw new BuildException("Output directory " + coutput.getPath()
                    + " should be a directorty.");
    }
    
    private void generateJavaWrapper(CLibrary library){
        JavaOut out = new JavaOut(javaoutput.getPath());
        out.setObjectPrefix(objectprefix);
        out.generate(library);
        if(this.genReport == true)
            out.report();
    }
    
    private void generateCWrapper(CLibrary library){
        COut cout = new COut(coutput.getPath());
        cout.generate(library);
    }
}
