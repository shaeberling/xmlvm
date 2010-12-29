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


#import "java_lang_IndexOutOfBoundsException.h"

// java.lang.IndexOutOfBoundsException
//----------------------------------------------------------------------------
@implementation java_lang_IndexOutOfBoundsException

- (id) init
{
    return [self initWithName: @"java_lang_IndexOutOfBoundsException" reason: nil userInfo: nil];
}

- (void) __init_java_lang_IndexOutOfBoundsException__
{
    // Do nothing
}

- (void) __init_java_lang_IndexOutOfBoundsException___java_lang_String: (java_lang_String*) msg
{
}

- (void) __init_java_lang_IndexOutOfBoundsException___java_lang_String_java_lang_Throwable: (java_lang_String*) msg: (java_lang_Throwable*) cause
{
}

- (java_lang_IndexOutOfBoundsException *) initCause___java_lang_Throwable: (java_lang_Throwable*) cause
{
	[self retain];
	return self;
}

- (void) printStackTrace__
{
}

@end
