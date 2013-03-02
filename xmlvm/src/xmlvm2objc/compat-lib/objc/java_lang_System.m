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

#import "java_lang_System.h"
#import "java_lang_RuntimeException.h"
#import "java_util_Properties.h"


@interface ConsoleOutputStream : java_io_OutputStream {
    FILE * stream;
}
- (void) write___int: (int) b;
- (void) setStream:(FILE *) stream;
@end

@implementation ConsoleOutputStream

- (id) init {
    [super init];
    self->stream = stdout;
    return self;
}

- (void) setStream:(FILE *) stream
{
    self->stream = stream;
}

- (void) write___int: (int) b
{
    fputc(b, stream);
}

@end


// java.lang.System
//----------------------------------------------------------------------------
java_io_PrintStream* _STATIC_java_lang_System_out;
java_io_PrintStream* _STATIC_java_lang_System_err;
java_io_InputStream* _STATIC_java_lang_System_in;

java_util_Properties* _STATIC_java_util_Properties_props = nil;

@implementation java_lang_System;


+ (JAVA_LONG) currentTimeMillis__
{
    return (JAVA_LONG)([[NSDate date] timeIntervalSince1970]*1000+0.5);
}

+ (JAVA_LONG) nanoTime__
{
    static NSDate* now_time;
    
    if (now_time == nil) {
        now_time = [[NSDate date] retain];
    }
    double time = [now_time timeIntervalSinceNow];
    time *= -1000.0; // milli
    time *= 1000.0; // micro
    time *= 1000.0; // nano
    return (JAVA_LONG) floor(time + 0.5);
}

+ (void) initialize
{
	// System.out
	ConsoleOutputStream* outcons = [[ConsoleOutputStream alloc] init];
    java_io_PrintStream* outstream = [[java_io_PrintStream alloc] init];
	[outstream __init_java_io_PrintStream___java_io_OutputStream:outcons];
	[outcons release];
    _STATIC_java_lang_System_out = outstream;
    
    // System.err
	ConsoleOutputStream* errcons = [[ConsoleOutputStream alloc] init];
    [errcons setStream:stderr];
    java_io_PrintStream* errstream = [[java_io_PrintStream alloc] init];
	[errstream __init_java_io_PrintStream___java_io_OutputStream:errcons];
	[errcons release];
    _STATIC_java_lang_System_err = errstream;
    
	// TODO System.in
    _STATIC_java_lang_System_in = JAVA_NULL;
}

+ (java_io_PrintStream*) _GET_out
{
    return _STATIC_java_lang_System_out;
}

+ (java_io_PrintStream*) _GET_err
{
    return _STATIC_java_lang_System_err;
}

+ (java_io_InputStream*) _GET_in
{
    return _STATIC_java_lang_System_in;
}

+ (void) setOut___java_io_PrintStream: (java_io_PrintStream*) ps
{
	[ps retain];
    [_STATIC_java_lang_System_out release];
    _STATIC_java_lang_System_out = ps;
}

+ (void) setErr___java_io_PrintStream: (java_io_PrintStream*) ps
{
	[ps retain];
    [_STATIC_java_lang_System_err release];
    _STATIC_java_lang_System_err = ps;
}

/*
 TODO - This currently does nothing with the properties passed-in, it might be worthwhile
 to actually store the values and provide other related methods, like getProperty. 
 The function was only added to allow Java code that sets properties required by the OpenGL 
 support in the xmlvm iPhone Simulator to cross-compile without modification.
 */

+ (java_lang_String *) setProperty___java_lang_String_java_lang_String: (java_lang_String *) s1: (java_lang_String *)s2
{
    return NULL;
}

+ (void) arraycopy___java_lang_Object_int_java_lang_Object_int_int
            :(java_lang_Object*) src
            :(int) srcPos
            :(java_lang_Object*) dest
            :(int) destPos
            :(int) length
{
	if ([src class] != [XMLVMArray class] ||
		[dest class] != [XMLVMArray class] ||
		((XMLVMArray*) src)->type != ((XMLVMArray*) dest)->type) {
		// TODO need to do much more thorough error checking
		java_lang_RuntimeException* ex = [[java_lang_RuntimeException alloc] init];
		@throw ex;
	}
	if (length == 0) {
		// Nothing to do
		return;
	}
	XMLVMArray* srcArr = (XMLVMArray*) src;
	XMLVMArray* destArr = (XMLVMArray*) dest;
	int d = 1;
	if (srcArr == destArr) {
		if (srcPos == destPos) {
			// Nothing to do
			return;
		}
		if (srcPos < destPos) {
			// We have to copy backwards to ensure we don't overwrite ourselves while copying
			srcPos += length - 1;
			destPos += length - 1;
			d = -1;
		}
	}
	switch (srcArr->type) {
	case 0:
		// We copy object references
		for (int i = 0; i < length; i++) {
			id o = [srcArr objectAtIndex:srcPos];
			[destArr replaceObjectAtIndex:destPos withObject:o];
			srcPos += d;
			destPos += d;
		}
		break;
    case 1: // boolean
    case 3: // byte
		for (int i = 0; i < length; i++) {
			destArr->array.b[destPos] = srcArr->array.b[srcPos];
			srcPos += d;
			destPos += d;
		}
        break;
    case 2: // char
    case 4: // short
		for (int i = 0; i < length; i++) {
			destArr->array.s[destPos] = srcArr->array.s[srcPos];
			srcPos += d;
			destPos += d;
		}
        break;
    case 5: // int
    case 6: // float
		for (int i = 0; i < length; i++) {
			destArr->array.i[destPos] = srcArr->array.i[srcPos];
			srcPos += d;
			destPos += d;
		}
        break;
    case 7: // double
    case 8: // long
		for (int i = 0; i < length; i++) {
			destArr->array.l[destPos] = srcArr->array.l[srcPos];
			srcPos += d;
			destPos += d;
		}
        break;
	}
}

+ (void) gc__
{
	// Do nothing
}

+ (int) identityHashCode___java_lang_Object: (java_lang_Object*) o
{
	return (int) o;
}

+ (java_lang_String*) getProperty___java_lang_String:(java_lang_String*)key
{
	return [self getProperty___java_lang_String_java_lang_String:key :JAVA_NULL];
}

+ (java_lang_String*) getProperty___java_lang_String_java_lang_String:(java_lang_String*)key :(java_lang_String*)defaultValue
{
	if (_STATIC_java_util_Properties_props==nil) {
		_STATIC_java_util_Properties_props=[[java_util_Properties alloc] init];
		[_STATIC_java_util_Properties_props __init_java_util_Properties__];
		[_STATIC_java_util_Properties_props put___java_lang_Object_java_lang_Object:@"line.separator" :@"\n"];
		[_STATIC_java_util_Properties_props put___java_lang_Object_java_lang_Object:@"file.separator" :@"/"];
		[_STATIC_java_util_Properties_props put___java_lang_Object_java_lang_Object:@"os.name" :@"iOS"];
	}
	return [_STATIC_java_util_Properties_props getProperty___java_lang_String_java_lang_String:key :defaultValue];// No retain required - object was already retained
}

+ (void) exit___int:(int)exitcode
{
	exit(exitcode);
}

@end
