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

import java.awt.BorderLayout;
import java.util.Properties;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public abstract class FreeTextProperty extends ProjectProperty {

    private String value;
    private JPanel comp;
    private JTextField data;

    public FreeTextProperty(String name, Properties xmlvm, String deflt) {
        super(name);
        this.value = deflt;
        try {
            value = xmlvm.getProperty(name).trim();
        } catch (Exception ex) {
        }
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public JComponent getVisuals() {
        if (comp == null) {
            comp = new JPanel();
            data = new JTextField(value);
            JLabel lab = new JLabel(getTitle());
            lab.setBorder(new EmptyBorder(0, 0, 0, 8));
            comp.setLayout(new BorderLayout());
            comp.add(lab, BorderLayout.WEST);
            comp.add(data, BorderLayout.CENTER);
        }
        data.setText(value);
        return comp;
    }
}
