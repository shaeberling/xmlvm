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

package org.xmlvm.iphone;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import org.crossmobile.ios2a.MiGBase64;

public class NSDictionary {

    public static Map<String, Object> dictionaryWithContentsOfFile(String path) {
        final ArrayList<Map<String, Object>> root_dict = new ArrayList<Map<String, Object>>();
        final Stack<Container> stack = new Stack<Container>();

        NSXMLParser p = new NSXMLParser(NSData.dataWithContentsOfFile(path));
        p.setDelegate(new NSXMLParserDelegate() {

            private StringBuffer text;
            private String keyname;

            @Override
            public void didStartElement(NSXMLParser parser, String elementName, String namespaceURI, String qualifiedName, Map<String, String> attributes) {
                if (elementName.equals("dict")) {
                    NSDictionaryDict dict = new NSDictionaryDict();
                    dict.key = keyname; // Should be early, since the variable will be overriden
                    stack.push(dict);
                    if (root_dict.isEmpty())
                        root_dict.add(dict);
                    return;
                }

                if (stack.isEmpty())    // Should already have something to add to
                    return;

                if (elementName.equals("array")) {
                    NSDictionaryArray list = new NSDictionaryArray();
                    list.key = keyname; // Should be early, since the variable will be overriden
                    stack.push(list);
                    return;
                }

                if (elementName.equals("key")
                        || elementName.equals("string")
                        || elementName.equals("integer")
                        || elementName.equals("real")
                        || elementName.equals("date")
                        || elementName.equals("data"))
                    text = new StringBuffer();
            }

            @Override
            public void foundCharacters(NSXMLParser parser, String characters) {
                if (text != null)// Only when expecting text
                    text.append(characters);
            }

            @Override
            public void didEndElement(NSXMLParser parser, String elementName, String namespaceURI, String qualifiedName) {
                if (stack.isEmpty())
                    return;

                if (elementName.equals("dict")
                        || elementName.equals("array")) {
                    Container child = stack.pop();
                    if (!stack.isEmpty())
                        stack.peek().store(child.getkey(), child);
                    keyname = null;
                    text = null;
                    return;
                }

                if (elementName.equals("key")) {
                    keyname = text.toString();
                    text = null;
                    return;
                }

                Object item = null;
                if (elementName.equals("true"))
                    item = Boolean.TRUE;
                else if (elementName.equals("false"))
                    item = Boolean.FALSE;
                else if (elementName.equals("string"))
                    item = text.toString();
                else if (elementName.equals("integer"))
                    item = Integer.valueOf(text.toString());
                else if (elementName.equals("real"))
                    item = Double.valueOf(text.toString());
                else if (elementName.equals("date"))
                    try {
                        item = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ").parse(text.toString());
                    } catch (ParseException ex) {
                    }
                else if (elementName.equals("data"))
                    item = new NSData(MiGBase64.decode(text.toString()));
                if (item != null)
                    stack.peek().store(keyname, item);
                keyname = null;
                text = null;
            }
        });
        p.parse();
        return root_dict.isEmpty() ? null : root_dict.get(0);
    }

    private static interface Container {

        String getkey();

        void store(String key, Object item);
    }

    private static class NSDictionaryDict extends HashMap<String, Object> implements Container {

        private String key;

        @Override
        public void store(String key, Object item) {
            if (key == null)
                throw new NullPointerException("NSDictionary <dict> error: <key> not defined");
            put(key, item);
        }

        @Override
        public String getkey() {
            return key;
        }
    }

    private static class NSDictionaryArray extends ArrayList<Object> implements Container {

        private String key;

        @Override
        public void store(String key, Object item) {
            add(item);
        }

        @Override
        public String getkey() {
            return key;
        }
    }
}
