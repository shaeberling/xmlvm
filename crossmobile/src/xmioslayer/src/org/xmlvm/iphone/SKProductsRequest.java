/* Copyright (c) 2011 by crossmobile.org
 *
 * CrossMobile is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 2.
 *
 * CrossMobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CrossMobile; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.xmlvm.iphone;

import java.util.Set;
import org.crossmobile.ios2a.ImplementationError;

public class SKProductsRequest extends SKRequest {

    private SKProductsRequestDelegate productsDelegate;

    public SKProductsRequest(Set productIdentifiers) {
        throw new ImplementationError();
    }

    public SKProductsRequestDelegate getProductsDelegate() {
        return productsDelegate;
    }

    public void setProductsDelegate(SKProductsRequestDelegate delegate) {
        this.productsDelegate = delegate;
    }
}
