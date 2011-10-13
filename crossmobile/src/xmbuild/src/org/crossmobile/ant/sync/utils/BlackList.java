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

package org.crossmobile.ant.sync.utils;

import org.crossmobile.ant.sync.utils.JarUtils.JarPathEntry;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarFile;

public class BlackList {

    /**
     * Where to store the blacklist file
     * It is in the form of
     * [namespace]: blacklist_pattern
     * [namespace]: blacklist_pattern
     * ...
     *
     * If "skipblacklist" is set, then the blacklist is not taken into account.
     * Useful if you create plugin JARs.
     */
    private final String BLACLIST_ENTRY = "META-INF/BLACK.LIST";
    private Set<String> list = new HashSet<String>();
    private final boolean skipBlackList;

    public BlackList(JarFile injar, String namespace, boolean skipBlackList) {
        this.skipBlackList = skipBlackList;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(injar.getInputStream(injar.getEntry(BLACLIST_ENTRY)), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                int colon = line.indexOf(':');
                if (colon < 0)
                    System.err.println("Namespace not properly defined in " + injar.getName() + ": missing colon.");
                else if (namespace.startsWith(line.substring(0, colon)))
                    list.add(line.substring(colon + 1).trim());
            }
        } catch (Exception ex) {    // No blacklist found
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException ex) {
                }
        }
    }

    public boolean matches(JarPathEntry entry) {
        if (skipBlackList)
            return false;
        if (entry.toString().equals(BLACLIST_ENTRY))
            return true;
        for (String bl : list)
            if (entry.toString().toLowerCase().startsWith(bl))
                return true;
        return false;
    }
}
