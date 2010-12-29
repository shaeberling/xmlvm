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


// java.io.OutputStreamWriter
//----------------------------------------------------------------------------
@interface java_io_OutputStreamWriter : java_io_Writer {
	java_io_OutputStream* outputStream;
}

- (id) initWithOutputStream:(java_io_OutputStream*)outStream;

- (void) __init_java_io_OutputStreamWriter___java_io_OutputStream: (java_io_OutputStream*)outStream;

- (void) write___java_lang_String_int_int: (java_lang_String*)str: (int)off: (int)len;
- (void) write___char_ARRAYTYPE_int_int: (XMLVMArray *) cbuf: (int) off: (int) len;
- (void) write___int: (int) c;
- (void) close__;
- (void) flush__;

@end
