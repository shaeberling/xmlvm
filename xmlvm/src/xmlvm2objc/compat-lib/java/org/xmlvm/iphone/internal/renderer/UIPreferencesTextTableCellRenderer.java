/*
 * Copyright (c) 2004-2009 XMLVM --- An XML-based Programming Language
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 675 Mass
 * Ave, Cambridge, MA 02139, USA.
 * 
 * For more information, visit the XMLVM Home Page at http://www.xmlvm.org
 */

package org.xmlvm.iphone.internal.renderer;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import org.xmlvm.iphone.CGRect;
import org.xmlvm.iphone.UIGraphics;
import org.xmlvm.iphone.UIPreferencesTextTableCell;

public class UIPreferencesTextTableCellRenderer extends UIViewRenderer<UIPreferencesTextTableCell> {

    public UIPreferencesTextTableCellRenderer(UIPreferencesTextTableCell view) {
        super(view);
    }

    @Override
    public void paint() {
        Graphics2D g = UIGraphics.getCurrentContext().xmlvmGetGraphics2D();
        CGRect displayRect = view.getFrame();
        g.setColor(Color.BLACK);
        FontMetrics fm = g.getFontMetrics();
        int x = (int) (displayRect.origin.x + view.getFrame().size.width / 2);
        int y = (int) displayRect.origin.y + ((int) view.getFrame().size.height + fm.getHeight())
                / 2 - fm.getDescent();
        g.drawString(view.getValue(), x, y);
        x += fm.stringWidth(view.getValue());
        g.drawLine(x, y - fm.getHeight(), x, y);

    }
}
