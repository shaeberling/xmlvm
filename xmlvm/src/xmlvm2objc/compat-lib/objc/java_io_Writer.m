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

#import "java_io_Writer.h"
#import "java_lang_NullPointerException.h"

// java.io.Writer
//----------------------------------------------------------------------------
@implementation java_io_Writer;

- (id) init
{
	[super init];
	self->lock = (java_lang_Object*) JAVA_NULL;
	writeBuffer = (XMLVMArray*) JAVA_NULL;
	return self;
}

- (java_lang_Object*) getProtectedLock
{
	return self->lock;
}

- (void) __init_java_io_Writer__
{
	[self __init_java_io_Writer__java_lang_Object:self];
}

- (void) __init_java_io_Writer__java_lang_Object: (java_lang_Object*)lockObject
{
	if (lockObject == (java_lang_Object*) JAVA_NULL) {
		java_lang_NullPointerException* ex = [[java_lang_NullPointerException alloc] init];
//		[ex __init_java_lang_NullPointerException___java_lang_String:@"Locking object cannot be NULL!"];
		@throw ex;
	}

	self->lock = lockObject;
	// Avoid cyclic dependency where self is never released if it references itself in self->lock. Similar logic is in "dealloc".
	// If self were to be retained again, the "retainCount" would never get to 0 and would not be deallocated.
	if (self != self->lock) {
		[self->lock retain];
	}
}


- (void) close__
{
	[self xmlvmSubclassResponsibility];
}

- (void) flush__
{
	[self xmlvmSubclassResponsibility];
}

- (void) write___char_ARRAYTYPE: (XMLVMArray *) cbuf
{
	[self write___char_ARRAYTYPE_int_int:cbuf:0:[cbuf count]];
}

- (void) write___char_ARRAYTYPE_int_int: (XMLVMArray *) cbuf: (int) off: (int) len
{
	[self xmlvmSubclassResponsibility];
}

- (void) write___int: (int) c
{
	int writeBufferSize = 1024;

	@synchronized(lock) {
		if (writeBuffer == (XMLVMArray*) JAVA_NULL) {
			writeBuffer = [XMLVMArray createSingleDimensionWithType: 2 andSize:writeBufferSize]; //char array
			[writeBuffer retain];
		}
		writeBuffer->array.c[0] = (char)c;
		[self write___char_ARRAYTYPE_int_int:writeBuffer:0:1];
	}
}

- (void) write___java_lang_String: (java_lang_String*) str
{
	[self write___java_lang_String_int_int:str:0:[str length__]];
}

- (void) write___java_lang_String_int_int: (java_lang_String*) str: (int) off: (int) len
{
	int writeBufferSize = 1024;

	@synchronized(lock) {
		XMLVMArray* cbuf;
		if (len <= writeBufferSize) {
			if (writeBuffer == (XMLVMArray*) JAVA_NULL) {
				writeBuffer = [XMLVMArray createSingleDimensionWithType: 2 andSize:writeBufferSize]; //char array
				[writeBuffer retain];
			}
			cbuf = writeBuffer;
		} else { // Don't permanently allocate very large buffers.
			cbuf = [XMLVMArray createSingleDimensionWithType: 2 andSize:len]; //char array
		}

		//TODO throw StringIndexOutOfBoundsException if "off" is < 0 or "off + len" > [str count] or "len" < 0

		//The following is: str.getChars(off, (off + len), cbuf, 0);
        for (int i = 0; i < len; i++) {
            cbuf->array.c[i] = [str charAt___int:i + off];
        }

		[self write___char_ARRAYTYPE_int_int:cbuf:0:len];
	}
}

- (void) dealloc
{
	// In order to avoid cyclic dependency, self->lock was NOT retained during initialization if it was equal to self.
	// Otherwise, the "retainCount" would never get to 0 and this "dealloc" method wouldn't even be called, causing a memory leak.
	// Since it was not retained, it should not be released either. It already has a "retainCount" of 0 right now.
	if (self != self->lock) {
		[self->lock release];
	}
	[writeBuffer release];
	[super dealloc];
}

@end
