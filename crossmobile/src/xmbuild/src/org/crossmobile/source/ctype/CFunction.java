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

import java.util.List;
import org.crossmobile.source.utils.StringUtils;

public class CFunction extends CProcedural {
    private static final long serialVersionUID = 1L;

    private final CType result;
    private final List<CArgument> args;
    public final String framework;

    public CFunction(CType result, String name, List<CArgument> params, String original, String filename, String framework) {
        super(name, original, filename);
        this.result = result;
        this.args = params;
        this.framework = framework;
    }

    @Override
    public String toString() {
        return "[" + name + " (" + result.toString() + ") " + args.toString() + "]";
    }

    public static void create(CLibrary parent, String entry) {
        String original = entry;
        entry = entry.trim();

        if (entry.endsWith("}")) {
            entry = entry.substring(0, StringUtils.matchFromEnd(entry, '{', '}')).trim();
            original = entry + ";";
        } else
            entry = entry.substring(0, entry.length() - 1).trim();

        int begin = StringUtils.matchFromEnd(entry, '(', ')');
        String prefix = entry.substring(0, begin).trim();
        String args = entry.substring(begin + 1, entry.length() - 1).trim();
        int last = StringUtils.findLastWord(prefix);
        String type = prefix.substring(0, last);
        parent.addCFunction(new CFunction(
                new CType(type),
                prefix.substring(last),
                CArgument.getFunctionArgments(args),
                original,
                parent.getCurrentFile(),
                parent.getCurrentFrameWork()));
    }

    public List<CArgument> getParameters() {
        return args;
    }

    public CType getResult() {
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof CProcedural))
            return false;
        return ((CFunction) obj).signature().equals(signature());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    private String signature() {
        return name + args.toString();
    }
}
