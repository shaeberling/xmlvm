/*
 * Copyright (c) 2004-2009 XMLVM --- An XML-based Programming Language
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 675 Mass
 * Ave, Cambridge, MA 02139, USA.
 * 
 * For more information, visit the XMLVM Home Page at http://www.xmlvm.org
 */

package org.xmlvm.util.universalfile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

import org.xmlvm.Log;

/**
 * A {@link UniversalFile} based on a {@link JarFile}.
 */
public class UniversalFileFromJarFile extends UniversalFile {
    private static final String    TAG = "UniversalFileFromJarFile";

    private String                 absoluteName;
    private JarInputStream         jarStream;
    private UniversalFileDirectory directory;


    UniversalFileFromJarFile(String absoluteName, JarInputStream jarStream) {
        this.absoluteName = absoluteName;
        this.jarStream = jarStream;
    }

    @Override
    public String getAbsoluteName() {
        return absoluteName;
    }

    @Override
    public String getAbsolutePath() {
        return absoluteName;
    }

    @Override
    public byte[] getFileAsBytes() {
        return null;
    }

    @Override
    public String getFileAsString() {
        return null;
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    @Override
    public UniversalFile[] listFiles() {
        if (directory == null) {
            directory = initialize();
        }
        return directory.listFiles();
    }

    private UniversalFileDirectory initialize() {
        UniversalFileDirectory result = new UniversalFileDirectory(absoluteName);
        JarEntry entry;
        try {
            byte data[] = new byte[4096];
            while ((entry = jarStream.getNextJarEntry()) != null) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                int len = 0;
                while ((len = jarStream.read(data)) != -1) {
                    outputStream.write(data, 0, len);
                }
                put(result, entry.getName(), new ByteArrayInputStream(outputStream.toByteArray()));
            }
        } catch (IOException e) {
            Log.error(TAG, "Error reading JAR file.");
        }
        return result;
    }

    private void put(UniversalFileDirectory addToDir, String name, InputStream stream) {
        int index;

        // A JAR file is a compressed ZIP file that only gives back a list of
        // all files contained in it. It doesn't allow hierarchical requests.
        // Therefore, each file is here put into its correct hierarchical path.
        while ((index = name.indexOf("/")) != -1) {
            String subDirName = name.substring(0, index);
            if (!subDirName.isEmpty()) {
                UniversalFileDirectory subDirectory = addToDir.getDirectory(subDirName);
                if (subDirectory == null) {

                    subDirectory = new UniversalFileDirectory(addToDir.getAbsoluteName()
                            + File.separator + subDirName);
                    addToDir.add(subDirectory);
                }
                addToDir = subDirectory;
            }
            name = name.substring(index + 1);
        }
        addToDir.add(new UniversalFileFromStreamResource(addToDir.getAbsoluteName()
                + File.separator + name, stream));
    }
}
