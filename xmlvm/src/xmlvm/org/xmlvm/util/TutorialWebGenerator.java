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

package org.xmlvm.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xmlvm.util.universalfile.UniversalFile;
import org.xmlvm.util.universalfile.UniversalFileCreator;

/**
 * Takes the overview.xml file from the tutorials folder and generates a website
 * from it.
 */
public class TutorialWebGenerator {

    private static class Params {
        public final String         overviewPath;
        public final String         outputPath;
        public final String         templatePath;
        private static final String PARAM_OVERVIEW_XML = "--overview=";
        private static final String PARAM_OUTPUT       = "--output=";
        private static final String PARAM_TEMPLATE     = "--template=";


        public Params(String overviewPath, String outputPath, String template) {
            this.overviewPath = overviewPath;
            this.outputPath = outputPath;
            this.templatePath = template;
        }

        public static Params parse(String[] args) {
            String overviewPath = "";
            String outputPath = "";
            String templatePath = "";
            for (String arg : args) {
                if (arg.startsWith(PARAM_OVERVIEW_XML)) {
                    overviewPath = arg.substring(PARAM_OVERVIEW_XML.length());
                }
                if (arg.startsWith(PARAM_OUTPUT)) {
                    outputPath = arg.substring(PARAM_OUTPUT.length());
                }
                if (arg.startsWith(PARAM_TEMPLATE)) {
                    templatePath = arg.substring(PARAM_TEMPLATE.length());
                }
            }
            return new Params(overviewPath, outputPath, templatePath);
        }
    }


    private static final String OUTPUT_FILENAME = "xmlvm_tutorials.html";


    public static void main(String[] args) {
        Params params = Params.parse(args);
        String template = readFromFile(params.templatePath);
        String html = generateHtml(readOverviewXml(params.overviewPath), template);
        writeHtmlToDisk(html, params.outputPath);
        System.out.println("All done.");
    }

    private static String generateHtml(Document overview, String template) {
        @SuppressWarnings("unchecked")
        List<Element> iosApplications = overview.getRootElement().getChild("ios-tutorials")
                .getChild("tutorials").getChildren("application");
        List<Element> androidApplications = overview.getRootElement().getChild("android-tutorials")
                .getChild("tutorials").getChildren("application");

        StringBuilder tutorialEntries = new StringBuilder();
        tutorialEntries.append("<div class=\"ios\">");
        tutorialEntries.append("<div class=\"platformTitle\">iOS</div>");
        int i = 0;
        for (Element iosApplication : iosApplications) {
            tutorialEntries.append(generateTutorialEntry(++i, "ios", iosApplication));
        }
        tutorialEntries.append("</div>");

        tutorialEntries.append("<div class=\"android\">");
        tutorialEntries.append("<div class=\"platformTitle\">Android</div>");
        i = 0;
        for (Element androidApplication : androidApplications) {
            tutorialEntries.append(generateTutorialEntry(++i, "android", androidApplication));
        }
        tutorialEntries.append("</div>");

        template = template.replace("{{TUTORIAL_LIST}}", tutorialEntries);
        return template;
    }

    private static String generateTutorialEntry(int index, String platform, Element application) {
        String title = application.getAttributeValue("name");
        String id = md5(platform + title);
        String slidesUrl = application.getAttributeValue("slides");
        String description = application.getChildText("text");
        // TODO: Files.

        StringBuilder html = new StringBuilder();
        html.append("<script>slidesUrl['" + id + "'] = '" + slidesUrl + "';</script>");
        html.append("<div class=\"tutorialEntry\" onclick=\"javascript:switchToTutorial('" + id
                + "');\">");
        html.append("<div class=\"tutorialTitle\">");
        html.append(index + ". " + title);
        html.append("</div>");
        html.append("<div class=\"description\">");
        html.append(description);
        html.append("</div></div>");
        return html.toString();
    }

    private static Document readOverviewXml(String path) {
        SAXBuilder builder = new SAXBuilder();
        File overviewFile = new File(path);
        if (!overviewFile.exists()) {
            System.err.println("Overview file doesn't exist.");
            return null;
        }
        try {
            return builder.build(overviewFile);
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void writeHtmlToDisk(String html, String pathStr) {
        if (pathStr == null || pathStr.equals("")) {
            pathStr = ".";
        }
        File path = new File(pathStr);
        if (path.exists() && path.isFile()) {
            System.err.println("Output path is a file.");
            return;
        }

        if (!path.exists()) {
            if (!path.mkdirs()) {
                System.err.println("Couldn't create output folder.");
                return;
            }
        }

        File outputFile = new File(path.getAbsoluteFile() + File.separator + OUTPUT_FILENAME);
        try {
            System.out.println("Writing to: " + outputFile.getAbsolutePath());
            FileWriter writer = new FileWriter(outputFile);
            writer.write(html);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readFromFile(String path) {
        UniversalFile file = UniversalFileCreator.createFile(new File(path));
        if (!file.exists()) {
            System.err.println("File doesn't exist: " + path);
            return null;
        }
        return file.getFileAsString();
    }

    private static String md5(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(text.getBytes("UTF-8"));
            byte[] messageDigest = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
