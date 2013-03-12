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
#import "java_io_Writer.h"
#import "java_lang_String.h"
#import "java_io_OutputStream.h"


// java.io.PrintWriter
//----------------------------------------------------------------------------
@interface java_io_PrintWriter : java_io_Writer {
	java_io_Writer* outWriter;
	int autoFlush;
	int trouble;
}

- (void) __init_java_io_PrintWriter___java_io_OutputStream: (java_io_OutputStream*)outStream;
- (void) __init_java_io_PrintWriter___java_io_OutputStream_boolean: (java_io_OutputStream*)outStream: (int)autoFlushBoolean;
- (void) __init_java_io_PrintWriter___java_io_Writer: (java_io_Writer*)writer;
- (void) __init_java_io_PrintWriter___java_io_Writer_boolean: (java_io_Writer*)writer: (int)autoFlushBoolean;

- (void) ensureOpen;

- (void) print___java_lang_String: (java_lang_String*)s;
- (void) println___java_lang_String: (java_lang_String*)x;
- (void) println__;
- (void) newLine__;
- (void) close__;
- (void) flush__;
- (void) write___char_ARRAYTYPE: (XMLVMArray *) cbuf;
- (void) write___char_ARRAYTYPE_int_int: (XMLVMArray *) cbuf: (int) off: (int) len;
- (void) write___int: (int) c;
- (void) write___java_lang_String: (java_lang_String*) str;
- (void) write___java_lang_String_int_int: (java_lang_String*) str: (int) off: (int) len;

- (java_io_PrintWriter*) append___char: (unichar) c;
- (void) print___boolean: (int) b;
- (void) println___boolean: (int) x;
- (void) print___char: (unichar) c;
- (void) println___char: (unichar) x;
- (void) print___char_ARRAYTYPE: (XMLVMArray *) s;
- (void) println___char_ARRAYTYPE: (XMLVMArray *) x;
- (void) print___double: (double) d;
- (void) println___double: (double) x;
- (void) print___float: (float) f;
- (void) println___float: (float) x;
- (void) print___int: (int) i;
- (void) println___int: (int) x;
- (void) print___long: (JAVA_LONG) l;
- (void) println___long: (JAVA_LONG) x;

// TODO implement these
//append(CharSequence)
//append(CharSequence, int, int)
//checkError()
//clearError()
//format(String, Object...)
//format(Locale, String, Object...)
//print(Object)
//printf(String, Object...)
//printf(Locale, String, Object...)
//println(Object)
//setError()

@end
