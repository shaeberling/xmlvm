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

package org.crossmobile.source.guru;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;
import org.crossmobile.source.ctype.CObject;

public enum Reporter {

    UNKNOWN_BLOCK,
    ARGUMENT_PARSING,
    MISMATCH_ID_RESOLVER,
    STATIC_INTERFACE,
    UNKNOWN_OVERRIDE,
    UNKNOWN_ID,
    ADVISOR_LOADING_ERROR,
    INHERITANCE,
    FUNCTION_POINTER,
    VARARGS_MISSING_COMMA,
    PROPERTY_ERROR,
    PROCEDURAL_PROBLEM,
    GROUPING_ERROR,
    CONSTRUCTOR_IN_INTERFACE;
    //
    public final LinkedHashMap<Tuplet, List<Tuplet>> notepad = new LinkedHashMap<Tuplet, List<Tuplet>>();
    private static String file;
    private static String object;

    public static void setFile(String file) {
        Reporter.file = file;
    }

    public static void setObject(CObject object) {
        Reporter.object = object == null ? null : object.getName();
    }

    public void report(String info, String value) {
        value = value.trim();
        if (info != null)
            info = info.trim();
        if (value.trim().length() == 0)
            return;
        Tuplet context = new Tuplet(file, object);
        List<Tuplet> block = notepad.get(context);
        if (block == null) {
            block = new ArrayList<Tuplet>();
            notepad.put(context, block);
        }
        block.add(new Tuplet(info, value));
    }

    public static void addUnknownItem(CObject obj, String item) {
        StringBuilder out = new StringBuilder();
        StringTokenizer tk = new StringTokenizer(item, ";", false);
        while (tk.hasMoreTokens()) {
            String token = tk.nextToken().trim();
            if (token.length() > 0)
                out.append(token.trim()).append(";\n");
        }
        UNKNOWN_BLOCK.report(obj != null ? "object " + obj.getName() : null, out.toString());
    }

    public static class Tuplet {

        public final String item;
        public final String value;

        public Tuplet(String item, String value) {
            this.item = item;
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            final Tuplet other = (Tuplet) obj;
            if ((this.item == null) ? (other.item != null) : !this.item.equals(other.item))
                return false;
            if ((this.value == null) ? (other.value != null) : !this.value.equals(other.value))
                return false;
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 53 * hash + (this.item != null ? this.item.hashCode() : 0);
            hash = 53 * hash + (this.value != null ? this.value.hashCode() : 0);
            return hash;
        }
    }
}
