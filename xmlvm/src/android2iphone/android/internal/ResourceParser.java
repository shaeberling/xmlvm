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

package android.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlvm.iphone.NSBundle;
import org.xmlvm.iphone.NSData;
import org.xmlvm.iphone.NSXMLParser;
import org.xmlvm.iphone.NSXMLParserDelegate;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.view.InflateException;

class DrawableParser extends NSXMLParserDelegate {

    private Context  context;
    private String   prefix;
    private Drawable drawable;

    public DrawableParser(Context context) {
        this.context = context;
    }

    @Override
    public void didStartMappingPrefix(NSXMLParser parser, String prefix, String namespaceURI) {
        if (namespaceURI.equals("http://schemas.android.com/apk/res/android")) {
            this.prefix = prefix + ":";
        }
    }

    @Override
    public void didStartElement(NSXMLParser parser, String elementName, String namespaceURI,
            String qualifiedName, Map<String, String> attributes) {
        AttributeSet attrs = new ResourceAttributes(context, prefix, attributes);

        if (qualifiedName.equals("selector")) {
            drawable = StateListDrawable.xmlvmCreateStateListDrawable(attrs);
        } else if (qualifiedName.equals("item")) {
            processStateListItem(attrs);
        }
    }

    public void didEndElement(NSXMLParser parser, String elementName, String namespaceURI,
            String qualifiedName) {
    }

    private void processStateListItem(AttributeSet attrs) {
        List<Integer> states = new ArrayList<Integer>();
        boolean b = attrs.getAttributeBooleanValue(null, State.STATE_PRESSED_NAME, false);
        if (b) {
            states.add(new Integer(State.STATE_PRESSED_ID));
        }
        b = attrs.getAttributeBooleanValue(null, State.STATE_CHECKED_NAME, false);
        if (b) {
            states.add(new Integer(State.STATE_CHECKED_ID));
        }

        int drawableId = attrs.getAttributeResourceValue(null, "drawable", -1);
        Drawable d = context.getResources().getDrawable(drawableId);

        int stateArray[] = new int[states.size()];
        for (int i = 0; i < states.size(); i++) {
            stateArray[i] = states.get(i).intValue();
        }

        ((StateListDrawable) drawable).xmlvmAddState(stateArray, d);
    }

    Drawable getDrawable() {
        return drawable;
    }
}

class StringsParser extends NSXMLParserDelegate {

    private Context              context;
    private String               prefix;
    private Map<String, Integer> nameToIdMap;
    private Map<Integer, String> stringMap;
    private Integer              currentId;
    private StringBuffer         currentValue;

    public StringsParser(Context context, Map<String, Integer> nameToIdMap) {
        this.context = context;
        this.nameToIdMap = nameToIdMap;
        stringMap = new HashMap<Integer, String>();
    }

    @Override
    public void didStartMappingPrefix(NSXMLParser parser, String prefix, String namespaceURI) {
        if (namespaceURI.equals("http://schemas.android.com/apk/res/android")) {
            this.prefix = prefix + ":";
        }
    }

    @Override
    public void didStartElement(NSXMLParser parser, String elementName, String namespaceURI,
            String qualifiedName, Map<String, String> attributes) {
        AttributeSet attrs = new ResourceAttributes(context, prefix, attributes);

        if (qualifiedName.equals("string")) {
            currentValue = new StringBuffer();
            String name = attrs.getAttributeValue("", "name");
            currentId = nameToIdMap.get("string/" + name);
        }
    }

    @Override
    public void didEndElement(NSXMLParser parser, String elementName, String namespaceURI,
            String qualifiedName) {
        if (qualifiedName.equals("string")) {
            stringMap.put(currentId, currentValue.toString());
            currentId = null;
        }
    }

    @Override
    public void foundCharacters(NSXMLParser parser, String characters) {
        if (currentId != null) {
            currentValue.append(characters);
        }
    }

    Map<Integer, String> getStringMap() {
        return stringMap;
    }
}

public class ResourceParser {

    public static Drawable parseDrawable(Context context, String fileName) {
        String filePath = NSBundle.mainBundle().pathForResource(fileName, "xml");
        NSData content = NSData.dataWithContentsOfFile(filePath);

        DrawableParser delegate = new DrawableParser(context);
        NSXMLParser xmlParser = createParser(context, content, delegate);
        boolean success = xmlParser.parse();
        if (!success) {
            throw new InflateException("Unable to inflate drawable resource: " + fileName + ".xml");
        }

        return delegate.getDrawable();
    }

    public static Map<Integer, String> parseStrings(Context context, String fileName,
            Map<String, Integer> nameToIdMap) {
        String filePath = NSBundle.mainBundle().pathForResource(fileName, "xml");
        NSData content = NSData.dataWithContentsOfFile(filePath);

        StringsParser delegate = new StringsParser(context, nameToIdMap);
        NSXMLParser xmlParser = createParser(context, content, delegate);
        boolean success = xmlParser.parse();
        if (!success) {
            throw new InflateException("Unable to inflate string resourcey: " + fileName + ".xml");
        }

        return delegate.getStringMap();
    }

    private static NSXMLParser createParser(Context context, NSData content,
            NSXMLParserDelegate delegate) {
        NSXMLParser xmlParser = new NSXMLParser(content);
        xmlParser.setShouldProcessNamespaces(true);
        xmlParser.setShouldReportNamespacePrefixes(true);
        xmlParser.setDelegate(delegate);

        return xmlParser;
    }

}
