/*
 * Copyright (c) 2004-2010 XMLVM --- An XML-based Programming Language
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

package org.xmlvm.proc.in;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.xmlvm.Log;
import org.xmlvm.util.universalfile.UniversalFile;
import org.xmlvm.util.universalfile.UniversalFileCreator;

/**
 * This class contains methods for creating {@link InputProcess} instances from
 * ZIP and JAR archives.
 */
public class ZipArchiveExtractor {
    private static final String   TAG             = "ZipInputProcessFactory";

    private static final String[] SUPPORTED_TYPES = { "zip", "jar" };


    /**
     * Returns whether the file is an archive that this class can handle.
     * 
     * @param filename
     *            the name of the file
     * @return Whether the file is a ZIP/JAR archive.
     */
    public static boolean isZipArchive(String filename) {
        for (String supportedType : SUPPORTED_TYPES) {
            if (filename.toLowerCase().endsWith("." + supportedType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a list of {@link UniversalFile} instances based on the given
     * ZIP/Archive.
     */
    public static List<UniversalFile> createFilesForArchive(String location) {
        Log.debug(TAG, "Creating input processes for archive: " + location);
        List<UniversalFile> result = new ArrayList<UniversalFile>();
        File archiveFile = new File(location);

        // Sanity checks.
        if (!archiveFile.exists() || !archiveFile.isFile()) {
            Log.error(TAG, "Could not find archive file: " + location);
            return result;
        }

        ZipInputStream zipStream;
        try {
            zipStream = new ZipInputStream(new FileInputStream(archiveFile));

            byte data[] = new byte[4096];
            ZipEntry entry;
            while ((entry = zipStream.getNextEntry()) != null) {
                // We are only interested in file entries.
                if (entry.isDirectory()) {
                    continue;
                }

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                int len = 0;
                while ((len = zipStream.read(data)) != -1) {
                    outputStream.write(data, 0, len);
                }
                String name = archiveFile.getAbsolutePath() + File.separator + entry.getName();
                result.add(UniversalFileCreator.createFile(name, new ByteArrayInputStream(
                        outputStream.toByteArray())));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.error(TAG, "Could not find archive: " + location);
        } catch (IOException e) {
            e.printStackTrace();
            Log.error(TAG, "Error reading archive: " + location);
        }
        return result;
    }
}
