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

import android.util.Xml;


import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class NSXMLParser extends NSObject {

    NSXMLParserDelegate delegate;
    private NSData data;
    private XmlPullParser parser;
    private boolean shouldReportNamespacePrefixes;

    public NSXMLParser(NSData data) {
        this.data = data;
        parser = Xml.newPullParser();
    }

    public void setDelegate(NSXMLParserDelegate delegate) {
        this.delegate = delegate;
        delegate.setParser(this);
    }

    public void setShouldProcessNamespaces(boolean flag) {
        try {
            parser.setFeature(parser.FEATURE_PROCESS_NAMESPACES, flag);
        } catch (XmlPullParserException ex) {
        }
    }

    public boolean shouldProcessNamespaces() {
        return parser.getFeature(parser.FEATURE_PROCESS_NAMESPACES);
    }

    public void setShouldReportNamespacePrefixes(boolean flag) {
        this.shouldReportNamespacePrefixes = flag;
    }

    public boolean shouldReportNamespacePrefixes() {
        return this.shouldReportNamespacePrefixes;
    }

    public boolean parse() {
        if (data == null)
            return false;
        try {
            Xml.parse(data.toString(), delegate.handler);
        } catch (SAXException ex) {
        }
        return true;
    }
}
