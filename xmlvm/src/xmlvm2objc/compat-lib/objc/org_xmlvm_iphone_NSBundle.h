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
#import "java_lang_String.h"



// NSBundle
//----------------------------------------------------------------------------
typedef NSBundle org_xmlvm_iphone_NSBundle;
@interface NSBundle (cat_NSBundle)

+ (NSBundle*) mainBundle__;
- (java_lang_String*) pathForResource___java_lang_String_java_lang_String: (java_lang_String*) resource
                                                                         : (java_lang_String*) type;
- (java_lang_String*) pathForResource___java_lang_String_java_lang_String_java_lang_String
																		 : (java_lang_String*) resource
                                                                         : (java_lang_String*) type
                                                                         : (java_lang_String*) directory;
- (NSString*) bundlePath__;

@end
