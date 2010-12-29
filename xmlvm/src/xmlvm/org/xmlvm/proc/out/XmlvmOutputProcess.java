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

package org.xmlvm.proc.out;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xmlvm.main.Arguments;
import org.xmlvm.proc.XmlvmProcessImpl;
import org.xmlvm.proc.XmlvmResource;
import org.xmlvm.proc.XmlvmResourceProvider;
import org.xmlvm.proc.in.file.XmlvmFile;

/**
 * This process takes XMLVM and writes it out as pure XML.
 */
public class XmlvmOutputProcess extends XmlvmProcessImpl<XmlvmResourceProvider> {
    private List<OutputFile> outputFiles = new ArrayList<OutputFile>();


    public XmlvmOutputProcess(Arguments arguments) {
        super(arguments);
        addSupportedInput(RecursiveResourceLoadingProcess.class);
    }

    @Override
    public List<OutputFile> getOutputFiles() {
        return outputFiles;
    }

    @Override
    public boolean process() {
        List<XmlvmResourceProvider> preprocesses = preprocess();
        for (XmlvmResourceProvider process : preprocesses) {
            List<XmlvmResource> xmlvmResources = process.getXmlvmResources();
            for (XmlvmResource xmlvm : xmlvmResources) {
                if (xmlvm != null) {
                    outputFiles.add(createOutputFromDocument(xmlvm));
                }
            }
        }
        return true;
    }

    /**
     * Creates an {@link OutputFile} from the given {@link XmlvmResource}.
     */
    private OutputFile createOutputFromDocument(XmlvmResource resource) {
        Document document = resource.getXmlvmDocument();
        XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
        StringWriter writer = new StringWriter();
        try {
            outputter.output(document, writer);
        } catch (IOException e) {
            e.printStackTrace();
            return new OutputFile("");
        }
        OutputFile result = new OutputFile(writer.toString());
        result.setFileName(resource.getFullName().replace('.', '_').replace('$', '_')
                + XmlvmFile.XMLVM_ENDING);
        result.setLocation(arguments.option_out());
        return result;
    }
}