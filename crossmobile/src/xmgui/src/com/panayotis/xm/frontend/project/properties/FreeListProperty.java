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

package com.panayotis.xm.frontend.project.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.swing.JComponent;
import javax.swing.JLabel;

public abstract class FreeListProperty extends ProjectProperty {

    private ArrayList<String> value = new ArrayList<String>();

    public FreeListProperty(String name, Properties xmlvm, String[] deflt) {
        super(name);
        try {
            String v = xmlvm.getProperty(name).trim().toLowerCase();
            StringTokenizer tk = new StringTokenizer(v, ":");
            while (tk.hasMoreTokens())
                value.add(tk.nextToken());
        } catch (Exception ex) {
            value.addAll(Arrays.asList(deflt));
        }

    }

    @Override
    public String getValue() {
        StringBuilder b = new StringBuilder();
        for (String item : value)
            b.append(item).append(':');
        String r = b.toString();
        return r.length() > 0 ? r.substring(0, r.length() - 1) : "";
    }

    public JComponent getVisuals() {
        return new JLabel(getValue());
    }
}
