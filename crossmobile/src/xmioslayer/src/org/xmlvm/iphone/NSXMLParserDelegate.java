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

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class NSXMLParserDelegate extends NSObject {

    private NSXMLParser parser;
    DefaultHandler handler = new DefaultHandler() {

        @Override
        public void startPrefixMapping(String prefix, String uri) {
            if (parser.shouldReportNamespacePrefixes())
                didStartMappingPrefix(parser, prefix, uri);
        }

        @Override
        public void endPrefixMapping(String prefix) {
            if (parser.shouldReportNamespacePrefixes())
                didEndMappingPrefix(parser, prefix);
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            Map<String, String> attribs = new HashMap<String, String>();
            for (int i = 0; i < attributes.getLength(); i++)
                attribs.put(attributes.getLocalName(i), attributes.getValue(i));
            didStartElement(parser, localName, uri, qName, attribs);
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            didEndElement(parser, localName, uri, qName);
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            String characters = String.copyValueOf(ch, start, length);
            foundCharacters(parser, characters);
        }
    };

    public void didStartMappingPrefix(NSXMLParser parser, String prefix, String namespaceURI) {
    }

    public void didEndMappingPrefix(NSXMLParser parser, String prefix) {
    }

    public void didStartElement(NSXMLParser parser, String elementName, String namespaceURI, String qualifiedName, Map<String, String> attributes) {
    }

    public void didEndElement(NSXMLParser parser, String elementName, String namespaceURI, String qualifiedName) {
    }

    public void foundCharacters(NSXMLParser parser, String characters) {
    }

    public void foundCDATA(NSXMLParser parser, NSData CDATABlock) {
        // TODO : call me from DefaultHandler
    }

    void setParser(NSXMLParser parser) {
        this.parser = parser;
    }
}
