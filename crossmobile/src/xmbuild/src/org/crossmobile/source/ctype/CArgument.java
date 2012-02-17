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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.crossmobile.source.ctype.CSelector.ArgumentResult;
import org.crossmobile.source.guru.Reporter;
import org.crossmobile.source.utils.FieldHolder;
import org.crossmobile.source.utils.ListOfArguments;
import org.crossmobile.source.utils.StringUtils;

public class CArgument extends CAny {
    private static final long serialVersionUID = 1L;

    private static final Pattern varargs = Pattern.compile(",\\s*+\\.\\.\\.");
    //
    CType type; // Should not be final to fix id conflicts & Generics

    public CArgument(CType type, String name) {
        super(name);
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CArgument))
            return false;
        CArgument other = (CArgument) obj;
        return name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "[" + type.toString() + " " + name + "]";
    }

    public static String fromList(List<CArgument> args) {
        StringBuilder buf = new StringBuilder();
        for (CArgument arg : args)
            buf.append(arg.type).append(" ").append(arg.name).append(", ");
        String res = buf.toString();
        return res.length() > 2 ? res.substring(0, res.length() - 2) : res;
    }

    public static List<CArgument> getFunctionArgments(String block) {
        block = block.trim();
        List<CArgument> res = new ArrayList<CArgument>();
        if (!block.isEmpty() && !block.equals("void")) {
            boolean isVarargs = block.contains("...");
            if (isVarargs) {
                block = block.replace("...", "").trim();
                if (block.endsWith(","))
                    block = block.substring(0, block.length() - 1).trim();
                else
                    Reporter.VARARGS_MISSING_COMMA.report(null, block);
            }

            String token;
            int index = 1;
            while (!block.isEmpty()) {
                token = block.substring(0, StringUtils.searchForMatchingBlock(block, 0, ','));
                block = block.substring(token.length());
                if (token.endsWith(","))
                    token = token.substring(0, token.length() - 1);

                if (CType.isFunctionPointer(token, "argument"))
                    token = "Object arg" + index;

                // Replace [ with *
                int pointers = StringUtils.count(token, '[');
                pointers += StringUtils.count(token, '*');
                token = token.replaceAll("(\\[.*?\\])|\\*", "").trim();

                // Remove default values
                int lastEq = token.lastIndexOf('=');
                if (lastEq >= 0)
                    token = token.substring(0, lastEq).trim();

                int word = StringUtils.findLastWord(token);
                String arg = token.substring(word);
                String coretype;
                if (word == 0) {  // whole name
                    coretype = arg;
                    arg = arg.toLowerCase();
                } else
                    coretype = token.substring(0, word);
                String type = coretype + "*****".substring(0, pointers > 5 ? 5 : pointers) + (isVarargs && block.isEmpty() ? "..." : "");
                res.add(new CArgument(new CType(type), arg));
                index++;
            }
        }
        return res;
    }

    public static ArgumentResult getSelectorArgument(String block) {
        String namedarg = "";
        String argtype = "Object";
        String argname;

        // At first, find type - could be missing
        int closing = block.lastIndexOf(")");
        if (closing >= 0)
            if (!block.startsWith("("))
                Reporter.ARGUMENT_PARSING.report("selector argument does not start with (", block);
            else {
                argtype = block.substring(1, closing).trim();
                block = block.substring(closing + 1).trim();
            }
        // Check for varargs
        Matcher vm = varargs.matcher(block);
        if (vm.find()) {
            argtype += "...";
            block = vm.replaceAll("");
        }

        // Find named argument and previous argument name
        int lastspace = block.lastIndexOf(" "); // it is already trimmed
        if (lastspace >= 0) {
            // Named argument
            namedarg = block.substring(lastspace + 1);
            argname = block.substring(0, lastspace).trim();
        } else
            // Unnamed argument
            argname = block;
        if (argname.contains(" "))
            Reporter.ARGUMENT_PARSING.report("found to contain spaces ", argname);
        if (CType.isFunctionPointer(argtype, "argument"))
            argtype = CType.FUNCPOINT;
        return new ArgumentResult(new CArgument(new CType(argtype), argname), namedarg);
    }

    public static void create(CLibrary lib, FieldHolder parent, boolean istypedef, String block) {
        String original = block;
        block = block.trim();
        if (block.contains("typedef"))
            block = block.replaceAll("typedef", "").replaceAll("  ", " ").trim();
        if (block.charAt(block.length() - 1) == ';')
            block = block.substring(0, block.length() - 1).trim();
        if (block.length() == 0)
            return;

        int last = StringUtils.findLastWord(block);
        String name = block.substring(last).trim();
        String def = block.substring(0, last).trim();
        if (name.isEmpty() || def.isEmpty()) {
            Reporter.ARGUMENT_PARSING.report("empty argument", name + def);
            return;
        }
        if (istypedef) {
            if (def.endsWith("Ref"))
                lib.getObject(new CType(name).getProcessedName()).setSuperclass(def.substring(0, def.length() - 3));
            else
                CType.registerTypedef(def, name);
            return;
        } else {
            ListOfArguments loa = ListOfArguments.parse(block.trim());
            for (String cname : loa.names) {
                CArgument arg = new CArgument(loa.ptype, cname);
                arg.addDefinition(original);
                parent.addCArgument(arg);
            }
        }
    }

    public CType getType() {
        return type;
    }
}
