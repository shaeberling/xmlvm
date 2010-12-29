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


#include <sys/socket.h> // for socket(), bind(), connect(), recv(), send() and accept()
#include <arpa/inet.h>  // for sockaddr_in and inet_ntoa()
#include <unistd.h>     // for close()
#include <sys/ioctl.h>  // for ioctl()

#import "java_net_Socket.h"
#import "java_io_IOException.h"
#import "java_lang_ArrayIndexOutOfBoundsException.h"

//----------------------------------------------------------------------------
// java.net.Socket
//----------------------------------------------------------------------------
@implementation java_net_Socket

- (id) initWithSocket:(int)socket
{
	[super init];
	self->sock = socket;
	return self;
}

- (void) __init_java_net_Socket__
{
}

- (void) __init_java_net_Socket___java_lang_String_int: (java_lang_String*)host: (int) port
{
	struct sockaddr_in servAddr;
	memset(&servAddr, 0, sizeof(servAddr));         // Zero out structure
	servAddr.sin_family      = AF_INET;             // Internet address family
	servAddr.sin_addr.s_addr = inet_addr([host UTF8String]);   // Server IP address
	servAddr.sin_port        = htons((unsigned short)port);    // Server port

	if ((sock = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP)) < 0) {
		java_io_IOException* ex = [[java_io_IOException alloc] init];
		NSMutableString* str = [[NSMutableString alloc] initWithString:@"socket() failed"];
		[ex __init_java_io_IOException___java_lang_String:str];
		[str release];
		@throw ex;
	}

	if (connect(sock, (struct sockaddr *) &servAddr, sizeof(servAddr)) < 0) {
		java_io_IOException* ex = [[java_io_IOException alloc] init];
		NSMutableString* str = [[NSMutableString alloc] initWithString:@"connect() failed"];
		[ex __init_java_io_IOException___java_lang_String:str];
		[str release];
		@throw ex;
	}
}

- (java_io_InputStream*) getInputStream__
{
	return [[java_net_SocketInputStreamImpl alloc] initWithSocket:sock];
}

- (java_io_OutputStream*) getOutputStream__
{
	return [[java_net_SocketOutputStreamImpl alloc] initWithSocket:sock];
}

- (void) close__
{
	// TODO: don't close if already closed
	close(sock);
}

@end


//----------------------------------------------------------------------------
// java.net.SocketInputStreamImpl
//----------------------------------------------------------------------------
@implementation java_net_SocketInputStreamImpl

- (id) init {
	[super init];
	eof = FALSE;
	return self;
}

- (id) initWithSocket:(int)socket
{
	[super init];
	[self init];
	self->sock = socket;
	return self;
}

- (void) dealloc
{
	// TODO: cleanup
	[super dealloc];
}

- (int) available__
{
	int bytesAvailable = 0;

// TODO does ioctl work on all systems?
	int result = ioctl(sock, FIONREAD, &bytesAvailable);
	if (result != 0) {
		// request failed
		java_io_IOException* ex = [[java_io_IOException alloc] init];
		NSMutableString* str = [[NSMutableString alloc] initWithString:@"Unable to determine number of bytes available to read!"];
		[ex __init_java_io_IOException___java_lang_String:str];
		[str release];
		@throw ex;
	}
//	else {
//		NSLog(@"Bytes available: %i", bytesAvailable);
//	}
	return bytesAvailable;
}

- (int) read___byte_ARRAYTYPE_int_int: (XMLVMArray *) b: (int) off: (int) length
{
	int n = 0;

	// EOF already encountered
	if (eof) {
		return -1;
	}

// TODO
//	// connection reset
//	if (impl.isConnectionReset()) {
//		throw new SocketException("Connection reset");
//	}

	// bounds check
	if (length <= 0 || off < 0 || off + length > [b count]) {
		if (length == 0) {
			return 0;
		}
		java_lang_ArrayIndexOutOfBoundsException* ex = [[java_lang_ArrayIndexOutOfBoundsException alloc] init];
		[ex __init_java_lang_ArrayIndexOutOfBoundsException__];
		@throw ex;
	}

	int avail = [self available__];
	if (avail < length) {
		if (avail == 0) {
			// If there's nothing available, block until at least one byte is available
			length = 1;
// TODO if this occurs, once recv() stops blocking, another call to available() could be made in order use more of the buffer. The smaller of "length - 1" & "available" could be read.
		} else {
			length = avail;
		}
	}

// TODO introduce BOOL "gotReset" and FileDescriptor "fd" as used in Java's SocketInputStream

	char buffer[length];

	n = recv(sock, buffer, length, 0);
	if (n > 0) {
		// copy results into "b" and use "off"
		for (int i = 0; i < n; i++) {
			b->array.b[i + off] = buffer[i];
		}
		return n;
	} else if (n < 0) {
		//An error occurred
		java_io_IOException* ex = [[java_io_IOException alloc] init];
		NSMutableString* str = [[NSMutableString alloc] initWithString:@"An error occurred reading from the socket!"];
		[ex __init_java_io_IOException___java_lang_String:str];
		[str release];
		@throw ex;
	}

// TODO
//	// If we get here we are at EOF, the socket has been closed, or the connection has been reset.
//	if (impl.isClosedOrPending()) {
//		throw new SocketException("Socket closed");
//	}
//	if (impl.isConnectionResetPending()) {
//		impl.setConnectionReset();
//	}
//	if (impl.isConnectionReset()) {
//		throw new SocketException("Connection reset");
//	}

	eof = TRUE;
	return -1;
}

- (int) read__
{
	int result = -1;
	if (!eof) {
		int bufferSize = 1;
		XMLVMArray* buffer = [XMLVMArray createSingleDimensionWithType:3 andSize:bufferSize]; // byte array

		int n = [self read___byte_ARRAYTYPE_int_int:buffer:0:bufferSize];

		if (n > 0) {
			result = (int)buffer->array.b[0] & 0xff;
		}
		[buffer release];
	}
	return result;
}

- (void) close__
{
	// TODO: don't close if already closed
	close(sock);
}

- (void) flush__
{
	// TODO: flush stream
}

@end


//----------------------------------------------------------------------------
// java.net.SocketOutputStreamImpl
//----------------------------------------------------------------------------
@implementation java_net_SocketOutputStreamImpl

- (id) initWithSocket:(int)socket
{
	[super init];
	self->sock = socket;
	return self;
}

- (void) dealloc
{
	// TODO: cleanup
	[super dealloc];
}

- (void) write___int: (int) c
{
	int bufferSize = 1;
	char buffer[bufferSize];

	buffer[0] = (char)c;

	if(bufferSize != send(sock, buffer, bufferSize, 0)) {
		java_io_IOException* ex = [[java_io_IOException alloc] init];
		NSMutableString* str = [[NSMutableString alloc] initWithString:@"send() failed"];
		[ex __init_java_io_IOException___java_lang_String:str];
		[str release];
		@throw ex;
	}
}

- (void) close__
{
	// TODO: don't close if already closed
	close(sock);
}

- (void) flush__
{
	// TODO: flush stream
}

@end
