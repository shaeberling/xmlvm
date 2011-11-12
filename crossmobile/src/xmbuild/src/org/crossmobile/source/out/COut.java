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
import org.crossmobile.source.guru.Advisor;
import org.crossmobile.source.cutils.CObjectOutput;

public class COut implements Generator {

    private final String outdir;
    private CLibrary     lib;
    private final String BEGIN_IMPL = "\n//XMLVM_BEGIN_IMPLEMENTATION";
    private final String END_IMPL   = "//XMLVM_END_IMPLEMENTATION";
    private final String BEGIN_DECL = "\n//XMLVM_BEGIN_DECLARATIONS";
    private final String END_DECL   = "//XMLVM_END_DECLARATIONS";


    public COut(String outdir) {
        this.outdir = outdir;
    }

    @Override
    public void generate(final CLibrary library) {
        this.lib = library;
        File out = new File(outdir);
        FileUtils.delete(out);

        for (CObject o : lib.getObjects()) {
            final CObject fo = o;
            if (!Advisor.isInIgnoreList(fo.name)) {

                FileUtils.putFile(new File(out, o.getcClassName() + ".m"),
                        new WriteCallBack<Writer>() {

                            @Override
                            public void exec(Writer out) throws IOException {
                                /*
                                 * Generate the *.m files
                                 */
                                generateImpl(fo, out);
                            }
                        });

                FileUtils.putFile(new File(out, o.getcClassName() + ".h"),
                        new WriteCallBack<Writer>() {

                            @Override
                            public void exec(Writer out) throws IOException {
                                /*
                                 * Generate the *.h files
                                 */
                                generateHeader(fo, out);
                            }
                        });
            }
        }
    }

    private void generateImpl(CObject object, Writer out) throws IOException {

        /*
         * Handle the Structs
         */
        if (CStruct.isStruct(object.name)) {
            out.append(BEGIN_IMPL + "\n");
            CStructOutput cStructOutput = new CStructOutput(out, lib, object);
            cStructOutput.appendConversionToObjCObject();
            cStructOutput.appendConversionToJavaObject();
            out.append(END_IMPL + "\n");
            cStructOutput.appendNewObjectCreation();
            cStructOutput.appendContructors();
            cStructOutput.appendFunction();
        }
        /*
         * Handle the Classes apart from protocols
         */
        else if (!(object.isProtocol())) {
            CObjectOutput cObjectOutput = new CObjectOutput(out, lib, object);
            out.append(BEGIN_IMPL + "\n");
            cObjectOutput.appendWrapperCreator();
            out.append(END_IMPL + "\n");
            cObjectOutput.appendWrapperRegistration();
            cObjectOutput.appendConstructor();
            cObjectOutput.appendFunction();
        }

    }

    private void generateHeader(CObject object, Writer out) throws IOException {

        if (CStruct.isStruct(object.name)) {
            out.append(BEGIN_DECL + "\n");
            out.append("#include \"xmlvm-ios.h\"\n");
            out.append("\n" + object.getName() + " to" + object.getName() + "(void * obj);\n");
            out.append("JAVA_OBJECT from" + object.getName() + "(" + object.getName() + " obj);\n");
            out.append("#define __ADDITIONAL_INSTANCE_FIELDS_" + object.getcClassName() + "\n");
            out.append(END_DECL);
        }

        else if (!(object.isProtocol())) {
            out.append(BEGIN_DECL + "\n");
            /*
             * Including xmlvm-ios.h in NSObject causes cyclic dependencies
             */
            if (!object.name.contains("NSObject"))
                out.append("#include \"xmlvm-ios.h\"\n");
            out.append("#define __ADDITIONAL_INSTANCE_FIELDS_" + object.getcClassName());
            if (object.name.contains("NSObject"))
                out.append(" void *wrappedObjCObj;");
            out.append("\n");
            out.append(END_DECL);
        }
    }
}
