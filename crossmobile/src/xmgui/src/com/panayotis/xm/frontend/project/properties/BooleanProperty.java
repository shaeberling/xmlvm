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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;
import javax.swing.JCheckBox;
import javax.swing.JComponent;

public abstract class BooleanProperty extends ProjectProperty {

    private boolean value;
    private JCheckBox comp;

    public BooleanProperty(String name, Properties xmlvm, boolean deflt) {
        super(name);
        value = deflt;
        try {
            String v = xmlvm.getProperty(name).trim().toLowerCase();
            value = v.equals("true") || v.equals("yes") || v.equals("enabled") || v.equals("1");
        } catch (Exception ex) {
        }
    }

    @Override
    public String getValue() {
        return Boolean.toString(value);
    }

    @Override
    public JComponent getVisuals() {
        if (comp == null) {
            comp = new JCheckBox(getTitle());
            comp.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent ae) {
                    value = comp.isSelected();
                }
            });
        }
        comp.setSelected(value);
        return comp;
    }
}
