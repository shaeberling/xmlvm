package org.crossmobile.source.out;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import org.crossmobile.source.ctype.CLibrary;
import org.crossmobile.source.ctype.CObject;
import org.crossmobile.source.ctype.CStruct;
import org.crossmobile.source.utils.FileUtils;
import org.crossmobile.source.utils.WriteCallBack;
import org.crossmobile.source.cutils.CStructOutput;

public class COut implements Generator {

    private final String outdir;
    private final String BEGIN_IMPL = "\n//XMLVM_BEGIN_IMPLEMENTATION";
    private final String END_IMPL   = "//XMLVM_END_IMPLEMENTATION";
    private final String BEGIN_DECL = "\n//XMLVM_BEGIN_DECLARATIONS";
    private final String END_DECL   = "//XMLVM_END_DECLARATIONS";


    public COut(String outdir) {
        this.outdir = outdir;
    }

    @Override
    public void generate(final CLibrary library) {
        File out = new File(outdir);
        FileUtils.delete(out);

        for (CObject o : library.getObjects()) {
            final CObject fo = o;
            FileUtils.putFile(new File(out, o.getcClassName() + ".m"),
                    new WriteCallBack<Writer>() {

                        @Override
                        public void exec(Writer out) throws IOException {
                            /*
                             * Generate the *.m files 
                             */
                            generateImpl(library, fo, out);
                        }
                    });

            FileUtils.putFile(new File(out, o.getcClassName() + ".h"),
                    new WriteCallBack<Writer>() {

                        @Override
                        public void exec(Writer out) throws IOException {
                            /*
                             * Generate the *.h files
                             */
                            generateHeader(library, fo, out);
                        }
                    });
        }
    }

    private void generateImpl(CLibrary library, CObject object, Writer out) throws IOException {

        /*
         * Handle the Structs
         */
        if (CStruct.isStruct(object.name)) {
            out.append(BEGIN_IMPL + "\n");
            out.append("#include \"xmlvm-ios.h\"\n\n");
            CStructOutput cStructOutput = new CStructOutput(out, library, object);
            cStructOutput.appendConversionToObjCObject();
            cStructOutput.appendConversionToJavaObject();
            out.append(END_IMPL + "\n");
            cStructOutput.appendNewObjectCreation();
            cStructOutput.appendContructors();
        }

    }

    private void generateHeader(CLibrary library, CObject object, Writer out) throws IOException {

        if (CStruct.isStruct(object.name)) {
            out.append(BEGIN_DECL + "\n");
            // TO DO: Change the below import statement. The list of include
            // files that define this object
            // has to be included in the CObject.
            out.append("#import <UIKit/UIKit.h>");
            out.append("\n" + object.getName() + " to" + object.getName() + "(void * obj);\n");
            out.append("JAVA_OBJECT from" + object.getName() + "(" + object.getName() + " obj);\n");
            out.append("#define __ADDITIONAL_INSTANCE_FIELDS_" + object.getcClassName() + "\n");
            out.append(END_DECL);
        }
    }
}
