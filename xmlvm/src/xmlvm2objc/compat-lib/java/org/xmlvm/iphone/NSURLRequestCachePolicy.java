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

package org.xmlvm.iphone;

import org.xmlvm.XMLVMSkeletonOnly;

@XMLVMSkeletonOnly
public final class NSURLRequestCachePolicy {

    public static final int UseProtocolCachePolicy                = 0;
    public static final int ReloadIgnoringLocalCacheData          = 1;
    public static final int ReloadIgnoringLocalAndRemoteCacheData = 4;
    public static final int ReloadIgnoringCacheData               = ReloadIgnoringLocalCacheData;
    public static final int ReturnCacheDataElseLoad               = 2;
    public static final int ReturnCacheDataDontLoad               = 3;
    public static final int ReloadRevalidatingCacheData           = 5;

    private NSURLRequestCachePolicy() {
    }
}
