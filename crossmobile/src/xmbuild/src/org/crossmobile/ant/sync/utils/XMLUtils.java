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

package org.crossmobile.ant.sync.utils;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.tools.ant.BuildException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLUtils {

    public static void saveXML(Document doc, File outfile) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new FileWriter(outfile));
            transformer.transform(source, result);
        } catch (Exception ex) {
            throw new BuildException(ex);
        }
    }

    public static int checkValidClass(String name, String clazz) {
        if (clazz == null)
            throw new BuildException(name + " should be set");
        if (clazz.indexOf(' ') >= 0)
            throw new BuildException(name + " should not contain spaces");
        if (clazz.length() == 0)
            throw new BuildException(name + " should not be empty");
        int lastdot = clazz.lastIndexOf('.');
        if (lastdot == 0)
            throw new BuildException(name + " should not start with a dot");
        if (lastdot == (clazz.length() - 1))
            throw new BuildException(name + " should not finish with a dot");
        if (lastdot < 0)
            throw new BuildException("Illegal name: class shoud be inside a package");
        return lastdot;
    }

    public static Node getNodeWithName(Node parent, String name) {
        ArrayList<Node> nodes = getNodesWithName(parent, name, false);
        if (nodes.size() < 1)
            return null;
        return nodes.get(0);
    }

    public static ArrayList<Node> getNodesWithName(Node parent, String name, boolean all_of_them) {
        ArrayList<Node> valid = new ArrayList<Node>();
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++)
            if (children.item(i).getNodeName().equalsIgnoreCase(name)) {
                valid.add(children.item(i));
                if (!all_of_them)
                    break;
            }
        return valid;
    }

    public static Node getNodeWithKey(Node parent, String key, String value) {
        HashSet<String> values = new HashSet<String>();
        values.add(value);
        ArrayList<Node> nodes = getNodesWithKey(parent, key, values, false);
        if (nodes.size() < 1)
            return null;
        return nodes.get(0);
    }

    public static ArrayList<Node> getNodesWithKey(Node parent, String key, String value, boolean all_of_them) {
        HashSet<String> values = new HashSet<String>();
        values.add(value);
        return getNodesWithKey(parent, key, values, all_of_them);
    }

    public static ArrayList<Node> getNodesWithKey(Node parent, String key, Set<String> values, boolean all_of_them) {
        ArrayList<Node> valid = new ArrayList<Node>();
        NodeList children = parent.getChildNodes();
        Node current;
        for (int i = 0; i < children.getLength(); i++) {
            current = children.item(i);
            NamedNodeMap attrs = current.getAttributes();
            if (attrs != null) {
                Node keynode = attrs.getNamedItem(key);
                if (keynode != null)
                    if (values == null || values.contains(keynode.getNodeValue())) {
                        valid.add(current);
                        if (!all_of_them)
                            break;
                    }
            }
        }
        return valid;
    }

    public static String getAttribute(Node n, String name) {
        if (n == null || name == null)
            return null;
        if (n instanceof Element)
            return ((Element) n).getAttribute(name);
        return null;
    }

    public static boolean setAttribute(Node n, String name, String value) {
        if (value == null)
            return deleteAttribute(n, name);
        if (n == null || name == null || value == null)
            return false;
        if (n instanceof Element) {
            ((Element) n).setAttribute(name, value);
            return true;
        }
        return false;
    }

    public static boolean deleteAttribute(Node n, String name) {
        if (n == null || name == null)
            return false;
        if (n instanceof Element) {
            ((Element) n).removeAttribute(name);
            return true;
        }
        return false;
    }
}
