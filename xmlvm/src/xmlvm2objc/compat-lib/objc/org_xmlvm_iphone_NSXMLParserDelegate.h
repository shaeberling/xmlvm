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

#import "xmlvm.h"
#import <Foundation/Foundation.h>
#import "org_xmlvm_iphone_NSXMLParser.h"
#import "org_xmlvm_iphone_NSData.h"
#import "java_lang_String.h"
#import "java_util_Map.h"



// NSXMLParserDelegate
//----------------------------------------------------------------------------
@interface org_xmlvm_iphone_NSXMLParserDelegate : NSObject

- (void) __init_org_xmlvm_iphone_NSXMLParserDelegate__;

- (void) parser:(NSXMLParser *)parser didStartMappingPrefix:(NSString *)prefix toURI:(NSString *)namespaceURI;
- (void) didStartMappingPrefix___org_xmlvm_iphone_NSXMLParser_java_lang_String_java_lang_String
     :(org_xmlvm_iphone_NSXMLParser*) parser
     :(java_lang_String*) prefix
     :(java_lang_String*) namespaceURI;

- (void) parser:(NSXMLParser *)parser
         didStartElement:(NSString *)elementName
         namespaceURI:(NSString *)namespaceURI
         qualifiedName:(NSString *)qualifiedName
         attributes:(NSDictionary *)attributeDict;
- (void) didStartElement___org_xmlvm_iphone_NSXMLParser_java_lang_String_java_lang_String_java_lang_String_java_util_Map
     :(org_xmlvm_iphone_NSXMLParser*) parser
     :(java_lang_String*) elementName
     :(java_lang_String*) namespaceURI
     :(java_lang_String*) qualifiedName
     :(java_util_Map*) attributeDict;
     
- (void) parser:(NSXMLParser *)parser
         didEndElement:(NSString *)elementName
         namespaceURI:(NSString *)namespaceURI
         qualifiedName:(NSString *)qName;
- (void) didEndElement___org_xmlvm_iphone_NSXMLParser_java_lang_String_java_lang_String_java_lang_String
     :(org_xmlvm_iphone_NSXMLParser*) parser
     :(java_lang_String*) elementName
     :(java_lang_String*) namespaceURI
     :(java_lang_String*) qualifiedName;

- (void) parser:(NSXMLParser *)parser
         foundCharacters:(NSString *)string;
- (void) foundCharacters___org_xmlvm_iphone_NSXMLParser_java_lang_String
     :(org_xmlvm_iphone_NSXMLParser*) parser
     :(java_lang_String*) characters;

- (void)parser:(NSXMLParser *)parser
	    foundCDATA:(NSData *)CDATABlock;
- (void) foundCDATA___org_xmlvm_iphone_NSXMLParser_org_xmlvm_iphone_NSData
     :(org_xmlvm_iphone_NSXMLParser*)parser
     :(org_xmlvm_iphone_NSData*)CDATABlock;

@end
