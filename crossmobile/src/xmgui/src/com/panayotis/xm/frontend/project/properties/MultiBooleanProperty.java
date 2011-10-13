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
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public abstract class MultiBooleanProperty extends ProjectProperty {

    private final String[] list;
    private boolean[] value;
    private JPanel comp;
    private JCheckBox[] items;

    public MultiBooleanProperty(String name, Properties xmlvm, String[] list, boolean[] deflt) {
        super(name);
        if (list.length < 1)
            throw new ArrayIndexOutOfBoundsException("List too small");
        if (list.length != deflt.length)
            throw new ArrayIndexOutOfBoundsException("SIze of names is " + list.length + ", while size of default values is " + deflt.length);
        this.list = list;
        value = new boolean[list.length];
        for (int i = 0; i < list.length; i++) {
            value[i] = deflt[i];
            try {
                String v = xmlvm.getProperty(name).trim().toLowerCase();
                value[i] = v.equals("true") || v.equals("yes") || v.equals("enabled") || v.equals("1");
            } catch (Exception ex) {
            }
        }
    }

    @Override
    public String getValue() {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < list.length; i++)
            if (value[i])
                b.append(list[i]).append(':');
        String r = b.toString();
        return r.length() > 0 ? r.substring(0, r.length() - 1) : "";
    }

    @Override
    public JComponent getVisuals() {
        if (comp == null) {
            ActionListener listener = new ActionListener() {

                public void actionPerformed(ActionEvent ae) {
                    int which = Integer.parseInt(ae.getActionCommand());
                    value[which] = items[which].isSelected();
                }
            };
            comp = new JPanel();
            items = new JCheckBox[list.length];
            comp.setLayout(new BoxLayout(comp, BoxLayout.Y_AXIS));
            comp.add(new JLabel(getTitle()));
            for (int i = 0; i < list.length; i++) {
                JCheckBox cb = new JCheckBox(list[i]);
                cb.addActionListener(listener);
                comp.add(cb);
                cb.setActionCommand(String.valueOf(i));
                items[i] = cb;
            }
        }
        for (int i = 0; i < list.length; i++)
            items[i].setSelected(value[i]);
        return comp;
    }
}
