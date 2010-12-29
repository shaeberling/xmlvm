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

#import "java_lang_Object.h"
#import "java_util_Map_Entry.h"

@interface java_util_Map_Entry_Impl : java_lang_Object <java_util_Map_Entry> {
	java_lang_Object* myKey;
	java_lang_Object* myValue;
}

- (id) initWithKey:(java_lang_Object*)key andValue:(java_lang_Object*)value;
- (java_lang_Object*) getKey__;
- (java_lang_Object*) getValue__;

@end
