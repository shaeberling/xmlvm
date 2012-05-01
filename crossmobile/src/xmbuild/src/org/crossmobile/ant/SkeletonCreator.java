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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.FileChannel;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.crossmobile.source.ctype.CLibrary;
import org.crossmobile.source.ctype.CType;
import org.crossmobile.source.out.COut;
import org.crossmobile.source.out.JavaOut;

public class SkeletonCreator extends Task {

    private static final String serializedCLibraryFileName = "CLibrary.ser";

    private File    sdkpath;
    private File    serializedLibraryDirectory;
    private File    javaoutput;
    private File    coutput;
    private File    codepath;
    private boolean debug;
    private String  option;
    private String objectprefix = "";
    private String packagename = "org.xmlvm.ios";
    private boolean genReport = true;

    public void setSdkpath(File sdkpath) {
        this.sdkpath = sdkpath;
    }

    public void setSerializedLibraryDirectory(File serializedLibraryDirectory) {
        this.serializedLibraryDirectory = serializedLibraryDirectory;
    }

    public void setJavaoutput(File javaoutput) {
        this.javaoutput = javaoutput;
    }

    public void setCoutput(File coutput) {
        this.coutput = coutput;
    }
    
    public void setCodepath(File codepath) {
        this.codepath = codepath;
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
        if (option == null)
            throw new BuildException("Parameter option should be defined.");

        CLibrary library = null;
        library = readSerializedCLibrary(serializedLibraryDirectory);
        if (library == null) {
            System.out.println("Unable to read the serialized CLibrary. Attempting to read from SDK path");
            library = readCLibraryFromSdkPath();

            writeSerializedCLibrary(library, serializedLibraryDirectory);
        } else {
            // The CLibrary was loaded from a serialized instance. Populate the typedefs for CType from the CLibrary.
            // E.g. Make sure that new CType("UIInterfaceOrientation") has a TypeID of "int" instead of "UIInterfaceOrientation".
            CType.reregisterTypedefs(library);
        }
        
        if (option.equals("gen-c-wrapper")) {
            if (coutput == null)
                throw new BuildException("Parameter coutput should be defined.");
            createCOutputDir();
            generateCWrapper(library);
            //copyCCode();
        } 
        else if (option.equals("gen-java-wrapper")) {
            if (javaoutput == null)
                throw new BuildException("Parameter javaoutput should be defined.");
            createJavaOutputDir();
            generateJavaWrapper(library);
            //copyJavaCode();
        } 
        else {
            if (coutput == null || javaoutput==null)
                throw new BuildException("Parameters coutput and javaoutput should be defined.");
            createCOutputDir();
            createJavaOutputDir();
            generateJavaWrapper(library);
            generateCWrapper(library);     
            if(codepath == null)
                throw new BuildException("Parameter codepath not defined.");
            try {
                copyCode("c");
                copyCode("java");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * @throws IOException 
     * 
     */
    private void copyCode(String lang) throws IOException {
        File file = null;
        String outdir = null;

        if(lang.equals("c")) {
            file = new File(codepath.getPath() + File.separatorChar+"c");
            outdir = coutput.getPath();
        } else if (lang.equals("java")) {
            file = new File(codepath.getPath() + File.separatorChar+"java");
            outdir = javaoutput.getPath();
        }
        
        File[] files = file.listFiles();
        if(files == null)
            throw new BuildException("The folder containing the code to be copied is missing");
        for(int i=0; i<files.length; i++){
            FileChannel in = new FileInputStream(files[i]).getChannel();
            FileChannel out = new FileOutputStream(outdir +File.separatorChar+files[i].getName()).getChannel();
            in.transferTo(0, in.size(), out);
        }
    }


    /**
     * @return the CLibrary from the SDK path
     * @throws BuildException if the CLibrary couldn't be read from the SDK path
     */
    private CLibrary readCLibraryFromSdkPath() {
        if (sdkpath == null)
            throw new BuildException("Parameter sdkpath should be defined.");
        if (!sdkpath.isDirectory())
            throw new BuildException("Not valid sdkpath directory: " + sdkpath);
        if (!sdkpath.getName().equals("Frameworks"))
            throw new BuildException(
                    "SDK directory \""
                            + sdkpath.getPath()
                            + "\" should be named \"Frameworks\", e.g. it should be something like /Developer/Platforms/iPhoneOS.platform/Developer/SDKs/iPhoneOS4.3.sdk/System/Library/Frameworks");
        return CLibrary.construct(packagename, sdkpath, debug);
    }

    /**
     * @param serializedLibDir the directory in which the serialized CLibrary exists
     * @return the CLibrary from the serialized file or null if it couldn't be read
     */
    private static CLibrary readSerializedCLibrary(File serializedLibDir) {
        CLibrary cLibrary = null;

        if (serializedLibDir == null) {
            System.out.println("Parameter serializedLibraryDirectory should be defined.");
        } else {

            File f = new File(serializedLibDir.getPath() + File.separatorChar
                    + serializedCLibraryFileName);
            if (f != null && f.exists()) {
                try {
                    ObjectInputStream oin = new ObjectInputStream(
                            new FileInputStream(f));
                    cLibrary = (CLibrary) oin.readObject();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Serialized CLibrary file does not exist");
            }
        }

        return cLibrary;
    }

    /**
     * @param clib the CLibrary to serialized
     * @param serializedLibDir the directory into which the serialized CLibrary should be placed
     * @throws BuildException if the serializedLibDir is not null and the library couldn't be serialized
     */
    private static void writeSerializedCLibrary(CLibrary clib, File serializedLibDir) {
        if (serializedLibDir == null) {
            System.out.println("Unable to write serialized CLibrary. Parameter serializedLibraryDirectory should be defined.");
        } else {
            try {
                File f = new File(serializedLibDir.getPath() + "/"
                        + serializedCLibraryFileName);
                f.delete();
                serializedLibDir.mkdirs();
                f.createNewFile();
                ObjectOutputStream oos = new ObjectOutputStream(
                        new FileOutputStream(f));
                oos.writeObject(clib);

            } catch (IOException e) {
                throw new BuildException("Error serializing CLibrary", e);
            }
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
