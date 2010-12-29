/* Copyright (c) 2002-2011 by XMLVM.org
 *
 * Project Info:  http://www.xmlvm.org
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

#import "xmlvm.h"
#import "java_lang_Object.h"
#import "java_lang_String.h"
#import "java_lang_Comparable.h"

// java.lang.Integer
//----------------------------------------------------------------------------
@interface java_lang_Integer : java_lang_Object <NSCopying, java_lang_Comparable> {

int number;

}

+ (void) initialize;
- (id) init;
- (id) copyWithZone:(NSZone *)zone;
- (NSUInteger) hash;
+ (java_lang_Class*) _GET_TYPE;
- (void) __init_java_lang_Integer___int :(int) i;
- (BOOL)isEqual:(id)anObject;
- (BOOL)equals___java_lang_Object:(java_lang_Object*)anObject;
- (int) intValue__;
+ (int) parseInt___java_lang_String: (java_lang_String *) str;
+ (java_lang_String*) toString___int: (int) i;
+ (java_lang_Integer*) valueOf___int: (int) i;
- (int) compareTo___java_lang_Object: (java_lang_Object*) obj;
- (int) compareTo___java_lang_Integer: (java_lang_Integer*) i;

@end
