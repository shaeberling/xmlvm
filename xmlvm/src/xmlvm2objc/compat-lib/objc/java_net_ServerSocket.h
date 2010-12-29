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
#import "java_net_Socket.h"


// java.net.ServerSocket
//----------------------------------------------------------------------------
@interface java_net_ServerSocket : java_lang_Object {
	int sock;
	int soTimeout;
}

- (void) __init_java_net_ServerSocket__;
- (void) __init_java_net_ServerSocket___int :(int) port;
- (void) __init_java_net_ServerSocket___int_int :(int) port:(int) backlog;

- (void) setSoTimeout___int :(int) timeout;
- (java_net_Socket*) accept__;
- (void) close__;

@end
