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
import java.util.StringTokenizer;
import org.crossmobile.source.guru.Reporter;
import org.crossmobile.source.utils.StringUtils;
import org.crossmobile.source.parser.Stream;
import org.crossmobile.source.utils.ListOfArguments;

public class CProperty extends CAnyMethod {

    private final CType type;
    private final String getter;
    private final String setter;

    public CProperty(String name, boolean isAbstract, CType type, String getter, String setter) {
        super(name, isAbstract);
        this.type = type;
        this.getter = getter;
        this.setter = setter;
    }

    public String getGetterName() {
        return getter;
    }

    public String getSetterName() {
        return setter;
    }

    public CType getType() {
        return type;
    }

    public static void parse(CObject parent, Stream s) {
        String property = s.consumeBlock().trim();
        String mods = null;
        String defs = property.substring(0, property.length() - 1).trim();
        if (defs.charAt(0) == '(') { // Has modifiers
            int end = StringUtils.matchFromStart(defs, '(', ')');
            mods = defs.substring(1, end).trim();
            defs = defs.substring(end + 1).trim();
        }

        if (CType.isFunctionPointer(defs, "property"))
            return;

        // Find all names
        List<String> getterL = new ArrayList<String>();
        List<String> setterL = new ArrayList<String>();
        ListOfArguments loa = ListOfArguments.parse(defs);
        for (String bname : loa.names) {
            String camelName = bname.substring(0, 1).toUpperCase() + bname.substring(1);
            getterL.add("get" + camelName);
            setterL.add("set" + camelName);
        }

        // Find modifiers (name only for first property)
        boolean writable = true;
        if (mods != null) {
            StringTokenizer tk = new StringTokenizer(mods, ",");
            while (tk.hasMoreElements()) {
                String cmod = tk.nextToken().trim();
                if (cmod.equals("readonly"))
                    writable = false;
                else if (cmod.startsWith("getter"))
                    getterL.set(0, getParameterDefinition(cmod.substring(6)));
                else if (cmod.startsWith("setter"))
                    setterL.set(0, getParameterDefinition(cmod.substring(6)));
            }
        }

        // Add properties
        String definition = "@property" + property;
        for (int i = 0; i < loa.names.size(); i++) {
            CProperty prop = new CProperty(loa.names.get(i), parent.isProtocol(), loa.ptype, getterL.get(i), writable ? setterL.get(i) : null);
            prop.addDefinition(definition);
            parent.addProperty(prop);
        }
    }

    private static String getParameterDefinition(String cmod) {
        cmod = cmod.trim();
        if (cmod.charAt(0) != '=')
            Reporter.PROPERTY_ERROR.report("missing = sign", cmod);
        cmod = cmod.substring(1).trim();
        return cmod;
    }
}
