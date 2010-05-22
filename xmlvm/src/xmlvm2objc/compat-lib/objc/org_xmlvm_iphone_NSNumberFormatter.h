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

/** @author teras */

typedef NSNumberFormatter org_xmlvm_iphone_NSNumberFormatter;

@interface NSNumberFormatter (cat_org_xmlvm_iphone_NSNumberFormatter)

- (void) __init_org_xmlvm_iphone_NSNumberFormatter__;
- (void) setNumberStyle___int :(int)style;
- (int) numberStyle__;
- (int) maximumFractionDigits__;
- (void) setMaximumFractionDigits___int :(int)digits;

@end

