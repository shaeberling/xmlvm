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

package org.crossmobile.source.ctype;

import java.io.Serializable;
import java.util.List;
import org.crossmobile.source.guru.Oracle;

public class CProcedural implements Serializable {
    private static final long serialVersionUID = 1L;

    public final String name;
    private final List<String> allnames;
    public final String definition;
    protected String filename;

    public CProcedural(String name, String original, String filename) {
        this.name = Oracle.nameBeautifier(name);
        this.allnames = Oracle.canonical(this.name);
        this.definition = original;
        this.filename = filename;
    }
}
