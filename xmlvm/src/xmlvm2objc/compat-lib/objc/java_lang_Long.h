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

// java.lang.Long
//----------------------------------------------------------------------------
@interface java_lang_Long : java_lang_Object <NSCopying, java_lang_Comparable> {

JAVA_LONG number;

}

+ (java_lang_Class*) _GET_TYPE;
- (id) init;
- (id) copyWithZone:(NSZone *)zone;
- (NSUInteger) hash;
- (void) __init_java_lang_Long___long :(JAVA_LONG) l;
- (BOOL) isEqual:(id)anObject;
- (JAVA_LONG) longValue__;
+ (JAVA_LONG) parseLong___java_lang_String: (java_lang_String *) str;
+ (JAVA_LONG) parseLong___java_lang_String_int: (java_lang_String*) str :(int) radix;
+ (java_lang_String*) toString___long: (JAVA_LONG) l;
+ (java_lang_Long*) valueOf___long: (JAVA_LONG) l;
- (int) compareTo___java_lang_Object: (java_lang_Object*) obj;
- (int) compareTo___java_lang_Long: (java_lang_Long*) l;

@end
