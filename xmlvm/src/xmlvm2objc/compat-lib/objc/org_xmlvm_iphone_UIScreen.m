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

#import "org_xmlvm_iphone_UIScreen.h"



@implementation org_xmlvm_iphone_UIScreen;

+ (org_xmlvm_iphone_UIScreen*) mainScreen__
{
	org_xmlvm_iphone_UIScreen* xmlvmScreen = [[org_xmlvm_iphone_UIScreen alloc] init];
	xmlvmScreen->screen = [UIScreen mainScreen];
	return xmlvmScreen;
}

- (org_xmlvm_iphone_CGRect*) getBounds__
{
	CGRect rect = [screen bounds];
    org_xmlvm_iphone_CGRect* xmlvmCGRect = [[org_xmlvm_iphone_CGRect alloc] init];
    xmlvmCGRect->origin->x = rect.origin.x;
    xmlvmCGRect->origin->y = rect.origin.y;
    xmlvmCGRect->size->width = rect.size.width;
    xmlvmCGRect->size->height = rect.size.height;
    return xmlvmCGRect;
}

- (org_xmlvm_iphone_CGRect*) getApplicationFrame__
{
	CGRect rect = [screen applicationFrame];
    org_xmlvm_iphone_CGRect* xmlvmCGRect = [[org_xmlvm_iphone_CGRect alloc] init];
    xmlvmCGRect->origin->x = rect.origin.x;
    xmlvmCGRect->origin->y = rect.origin.y;
    xmlvmCGRect->size->width = rect.size.width;
    xmlvmCGRect->size->height = rect.size.height;
    return xmlvmCGRect;
}
@end
