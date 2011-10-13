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

package org.crossmobile.ant.paintshop;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.tools.ant.BuildException;

public class IconPainter {

    private static final String RESOURCEPREFIX = "/artwork/icon/";
    private static final BufferedImage overlay = getSysImage("overlay.png");
    private static final BufferedImage shape = getSysImage("shape.png");

    public static void createIcon(File from, File to) {
        BufferedImage in = null;
        try {
            in = ImageIO.read(from);
        } catch (IOException ex) {
            throw new BuildException("Unable to load image " + from);
        }
        int width = in.getWidth();
        int height = in.getHeight();

        BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = out.createGraphics();
        g.drawImage(in, 0, 0, null);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        g.setComposite(AlphaComposite.SrcOver);
        g.drawImage(overlay, 0, 0, width, height, null);

        g.setComposite(AlphaComposite.DstIn);
        g.drawImage(shape, 0, 0, width, height, null);

        g.dispose();

        try {
            ImageIO.write(out, "png", to);
        } catch (IOException ex) {
            throw new BuildException("Unable to store image " + to);
        }
    }

    private static BufferedImage getSysImage(String name) {
        try {
            return ImageIO.read(IconPainter.class.getResourceAsStream(RESOURCEPREFIX + name));
        } catch (IOException ex) {
            throw new BuildException("Unable to locate system image " + RESOURCEPREFIX + name);
        }
    }
}
