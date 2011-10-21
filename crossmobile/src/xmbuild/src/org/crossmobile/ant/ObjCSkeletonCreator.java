package org.crossmobile.ant;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.crossmobile.source.ctype.CLibrary;
import org.crossmobile.source.out.JavaOut;

public class ObjCSkeletonCreator extends Task {

    private File    sdkpath;
    private File    output;
    private boolean debug;
    private String objectprefix = "";
    private String methodprefix = "";
    private String constructorprefix = "";
    private String packagename = "org.xmlvm.ios";

    public void setSdkpath(File sdkpath) {
        this.sdkpath = sdkpath;
    }

    public void setOutput(File output) {
        this.output = output;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
    
    public void setConstructorprefix(String constructorprefix) {
        this.constructorprefix = constructorprefix;
    }

    public void setMethodprefix(String methodprefix) {
        this.methodprefix = methodprefix;
    }
    
    public void setObjectprefix(String objectprefix) {
        this.objectprefix = objectprefix;
    }
    
    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }
    
    @Override
    public void execute() throws BuildException {
        if (sdkpath == null)
            throw new BuildException("Parameter sdkpath should be defined.");
        if (output == null)
            throw new BuildException("Parameter output should be defined.");

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

        CLibrary library = CLibrary.construct(packagename, sdkpath, debug);
        JavaOut out = new JavaOut(output.getPath());
        out.setConstructorPrefix(constructorprefix);
        out.setMethodPrefix(methodprefix);
        out.setObjectPrefix(objectprefix);
        out.generate(library);
        out.report();
    }
}
