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
#import "java_lang_Object.h"
#import "java_io_PrintStream.h"
#import "java_io_InputStream.h"


@interface java_lang_System : java_lang_Object

+ (void) initialize;
+ (java_io_PrintStream*) _GET_out;
+ (void) _PUT_out: (java_io_PrintStream*) v;
+ (void) setOut___java_io_PrintStream: (java_io_PrintStream*) ps;
+ (void) _PUT_in: (java_io_InputStream*) v;
+ (java_io_InputStream*) _GET_in;
+ (long) currentTimeMillis__;
+ (long) nanoTime__;
+ (java_lang_String *) setProperty___java_lang_String_java_lang_String: (java_lang_String *) s1: (java_lang_String *) s2;
+ (void) arraycopy___java_lang_Object_int_java_lang_Object_int_int
               :(java_lang_Object*) src
               :(int) srcPos
               :(java_lang_Object*) dest
               :(int) destPos
               :(int) length;
+ (void) gc__;
+ (int) identityHashCode___java_lang_Object: (java_lang_Object*) o;

@end
