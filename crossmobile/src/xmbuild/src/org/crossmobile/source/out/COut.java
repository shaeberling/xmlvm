/* Copyright (c) 2002-2011 by XMLVM.org
 *
 * Project Info:  http://www.xmlvm.org
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.crossmobile.source.out;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

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

        CObject o = null;
        int i = 0;

        Collection<CObject> col = (Collection<CObject>) lib.getObjects();
        Object[] objs = col.toArray();
        
        for (i = 0; i < objs.length; i++) {
            o = (CObject) objs[i];
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
            if (Advisor.isAccumulatorNeeded(object.name))
                out.append(" JAVA_OBJECT acc_Array;");
            out.append("\n");
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
            if (Advisor.isAccumulatorNeeded(object.name))
                out.append(" JAVA_OBJECT acc_Array;");
            out.append("\n");
            out.append(END_DECL);
        }
    }
}
