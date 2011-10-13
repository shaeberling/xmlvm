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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public abstract class SelectionListProperty extends ProjectProperty {

    private String[] list;
    private int value;
    private JPanel comp;
    private JComboBox item;

    public SelectionListProperty(String name, Properties xmlvm, String[] list, int deflt) {
        super(name);
        this.list = list;
        this.value = deflt;
        try {
            String v = xmlvm.getProperty(name).trim().toLowerCase();
            for (int i = 0; i < list.length; i++)
                if (list[i].toLowerCase().endsWith(v)) {
                    value = i;
                    break;
                }
        } catch (Exception ex) {
        }
    }

    @Override
    public String getValue() {
        return list[value];
    }

    @Override
    public JComponent getVisuals() {
        if (comp == null) {
            comp = new JPanel();
            item = new JComboBox(list);
            JLabel lab = new JLabel(getTitle());
            lab.setBorder(new EmptyBorder(0, 0, 0, 8));

            comp.setLayout(new BorderLayout());
            comp.add(lab, BorderLayout.WEST);
            comp.add(item, BorderLayout.CENTER);
            item.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent ae) {
                    value = item.getSelectedIndex();
                }
            });
        }
        item.setSelectedIndex(value);
        return comp;
    }
}
