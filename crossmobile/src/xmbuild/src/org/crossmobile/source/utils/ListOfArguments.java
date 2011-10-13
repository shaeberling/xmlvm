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

package org.crossmobile.source.utils;

import java.util.ArrayList;
import java.util.List;
import org.crossmobile.source.ctype.CType;
import org.crossmobile.source.guru.Reporter;

public class ListOfArguments {

    public final List<String> names;
    public final CType ptype;

    public ListOfArguments(List<String> names, CType ptype) {
        this.names = names;
        this.ptype = ptype;
    }

    public static ListOfArguments parse(String defs) {
        List<String> names = new ArrayList<String>();
        defs += ",";
        boolean hasStar = false;
        while (defs.endsWith(",")) {
            defs = defs.substring(0, defs.length() - 1).trim();
            int lastWord = StringUtils.findLastWord(defs);
            names.add(defs.substring(lastWord));
            defs = defs.substring(0, lastWord).trim();
            if (defs.endsWith("const"))
                defs = defs.substring(0, defs.length() - 5).trim();
            if (defs.endsWith("*")) {
                hasStar = true;
                defs = defs.substring(0, defs.length() - 1).trim();
            } else
                hasStar = false;    // Take care ONLY for the last one - copy type of first object to all others. So we need to keep the star if it is deleted by the last entry
        }
        if (defs.isEmpty())
            Reporter.ARGUMENT_PARSING.report("empty argument", names.toString());
        CType ptype = new CType(defs + (hasStar ? "*" : ""));
        return new ListOfArguments(names, ptype);
    }
}
