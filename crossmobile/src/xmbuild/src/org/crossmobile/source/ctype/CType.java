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

import java.util.HashMap;
import java.util.Map;
import org.crossmobile.source.guru.Advisor;
import org.crossmobile.source.guru.Oracle;
import org.crossmobile.source.guru.Reporter;
import org.crossmobile.source.utils.StringUtils;

public class CType {

    private static Map<String, String> typedefs = new HashMap<String, String>();
    public static final String FUNCPOINT = "Object";
    //
    private final TypeID typeid;
    private final String processed;
    private boolean varargs = false;
    private int reference = 0;

    public CType(String name) {
        // Remove unused identifiers
        name = name.trim().
                replaceAll("\\*\\s*const", "").
                replaceAll("const\\s", "").
                replaceAll("struct\\s", "").
                replaceAll("inline\\s", "").
                replaceAll("extern\\s", "").
                replaceAll("typedef\\s", "").
                replaceAll("__strong", "").trim();
        if (name.startsWith("out "))
            name = name.substring(4).trim();
        if (name.startsWith("in "))
            name = name.substring(3).trim();
        if (name.startsWith("inout "))
            name = name.substring(6).trim();
        if (name.startsWith("oneway "))
            name = name.substring(7).trim();

        // Failsafe for unmatched types
        if (name.isEmpty())
            Reporter.ARGUMENT_PARSING.report(null, "empty name");

        /*  Resolve pointers */
        reference = StringUtils.count(name, '*');
        if (reference > 0)
            name = name.replaceAll("\\*", "");
        int openBracket = name.indexOf('[');
        if (openBracket >= 0) {
            int closeBracket = name.lastIndexOf(']');
            reference += StringUtils.count(name, '[');
            name = name.substring(0, openBracket) + name.substring(closeBracket + 1, name.length());
        }
        // WHAT TO DO WITH THIS?
        if (name.endsWith("&"))
            name = name.substring(0, name.length() - 1).trim();

        varargs = name.contains("...");
        if (varargs)
            name = name.replace("...", "");

        // Fix naming conventions
        if (name.contains("signed")) {
            name = name.replaceAll("unsigned", "").replaceAll("signed", "").replaceAll("\\s\\s", " ").trim();
            if (name.isEmpty())
                name = "int";
        } else
            name = name.replaceAll("\\s\\s", " ").trim();
        if (name.equals("short int"))
            name = "short";
        if (name.equals("long int"))
            name = "long";
        if (name.equals("long long"))
            name = "long";
        if (name.equals("long long int"))
            name = "long";
        if (name.equals("long double"))
            name = "double";

        // Replace protocol with Object name
        if (name.endsWith(">"))
            if (name.startsWith("id")) {
                int prot = StringUtils.matchFromEnd(name, '<', '>');
                name = name.substring(prot + 1, name.length() - 1).trim();
                if (name.startsWith(","))
                    throw new RuntimeException("Protocol type starts with comma: " + name);
                if (name.contains(","))
                    name = name.substring(0, name.indexOf(','));
            } else {
                int where = StringUtils.findFirstWord(name);
                name = name.substring(0, where);
            }

        if (name.contains(" "))
            throw new RuntimeException("type \'" + name + "\' contains spaces");

        // Get type
        typeid = TypeID.get(name);
        processed = name;
    }

    @Override
    public String toString() {
        String name = typeid.name;
        int level = reference + typeid.reference;
        // Deal with void* pointer
        if (name.equals("void") && level > 0)
            name = "byte";
        if (level > 0 && !Advisor.isNativeType(name)) {
            if (!CStruct.isStruct(name))
                level--;
            if (level > 0) {
                name = "Reference<" + name + ">";
                level--;
            }
        }
        StringBuilder b = new StringBuilder(name);
        for (; level > 0; level--)
            b.append("[]");
        if (varargs)
            b.append("...");
        return b.toString();
    }

    public String getProcessedName() {
        return processed;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CType))
            return false;
        return processed.equals(((CType) obj).processed);
    }

    @Override
    public int hashCode() {
        return processed.hashCode();
    }

    private static class TypeID {

        private final static Map<String, TypeID> types = new HashMap<String, TypeID>();
        //
        private String name;
        private int reference = 0;

        private TypeID(String name) {
            this.name = name;
        }

        private static TypeID get(String name) {
            TypeID res = types.get(name);
            if (res == null) {
                res = new TypeID(name);
                types.put(name, res);
            }
            return res;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public boolean isID() {
        return typeid.name.equals("id");
    }

    // New type will be equal to nativetype
    public static String registerTypedef(String systemtype, String newtype) {
        if (systemtype.equals(newtype))
            return systemtype;
        if ((systemtype.toLowerCase().startsWith("opaque") && !newtype.toLowerCase().startsWith("opaque"))) {
            String swap = systemtype;
            systemtype = newtype;
            newtype = swap;
        }
        if (systemtype.contains(",") || newtype.contains(","))
            throw new RuntimeException("typedef " + systemtype + " " + newtype + " contains comma");
        typedefs.put(newtype, systemtype);
        return systemtype;
    }

    public static void finalizeTypedefs() {
        Advisor.addDefaultTypedefs();
        for (String newtype_name : typedefs.keySet()) {
            CType newtype = new CType(newtype_name);
            String from = newtype.typeid.name;

            String systype_name = typedefs.get(newtype_name);
            CType stype = new CType(systype_name);
            String to = stype.typeid.name;
            int addedRef = stype.reference + stype.typeid.reference;

            if (from.equals(to))
                continue;
            if (Advisor.isNativeType(from) && !(from.equals("char") && to.equals("byte")))  // Only case that a native typedef is performed, is with C's char instead of byte
                throw new RuntimeException("Native type override: from " + from + " to " + to);

            for (TypeID t : TypeID.types.values())
                if (t.name.equals(from)) {
                    t.name = to;
                    t.reference += addedRef;
                }
        }
        // Beautify names
        for (TypeID id : TypeID.types.values()) {
            if (id.name.endsWith("Ref"))
                id.reference--;
            id.name = Oracle.nameBeautifier(id.name);
        }
    }

    public static boolean isFunctionPointer(String block, String context) {
        String orig = block;
        int from = block.indexOf('^');
        if (from >= 0) {
            Reporter.FUNCTION_POINTER.report(context + " is block", block);
            block = block.substring(from + 1);
            int to = StringUtils.findFirstWord(block);
            if (to > 0)
                registerTypedef(FUNCPOINT, block.substring(0, to));
            return true;
        }

        from = block.indexOf('(');
        if (from >= 0) {
            block = block.substring(from);
            int to = StringUtils.matchFromStart(block, '(', ')');
            String def = block.substring(1, to).trim();
            block = block.substring(to + 1).trim();
            if (block.charAt(0) == '(') {
                String odef = def;
                if (def.startsWith("void"))
                    def = def.substring(4).trim();
                if (def.startsWith(","))
                    def = def.substring(1).trim();
                if (!def.contains(",")) {
                    if (def.startsWith("*"))
                        def = def.substring(1).trim();
                    String defl = def.toLowerCase();
                    if (defl.contains("callback") || defl.contains("proc") || defl.contains("function") || defl.contains("listener"))
                        registerTypedef(FUNCPOINT, def);
                }
                Reporter.FUNCTION_POINTER.report(context + " is function pointer", orig);
                return true;
            }
        }

        return false;
    }
}
