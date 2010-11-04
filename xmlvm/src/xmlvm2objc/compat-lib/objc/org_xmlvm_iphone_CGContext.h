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
#import "java_lang_Object.h"
#import "org_xmlvm_iphone_CGRect.h"
#import "org_xmlvm_iphone_CGSize.h"
#import "org_xmlvm_iphone_CGFont.h"
#import "org_xmlvm_iphone_CGImage.h"
#import "org_xmlvm_iphone_UIImage.h"

@class org_xmlvm_iphone_CGLayer;

// CGContext
//----------------------------------------------------------------------------
@interface org_xmlvm_iphone_CGContext : java_lang_Object {
@public CGContextRef context;
@public CGFontRef localFont;
@public float localSize;
}

- (id) initWithCGContextRef:(CGContextRef)ref;
- (CGContextRef) getCGContextRef;
- (void) setFillColor___float_ARRAYTYPE: (XMLVMArray*) color;
- (void) setStrokeColor___float_ARRAYTYPE: (XMLVMArray*) color;
- (void) fillRect___org_xmlvm_iphone_CGRect: (org_xmlvm_iphone_CGRect*)rect;
- (void) strokeRect___org_xmlvm_iphone_CGRect: (org_xmlvm_iphone_CGRect*)rect;
- (void) fillEllipseInRect___org_xmlvm_iphone_CGRect: (org_xmlvm_iphone_CGRect*)rect;
- (void) clipToRect___org_xmlvm_iphone_CGRect: (org_xmlvm_iphone_CGRect*)rect;
- (void) translate___float_float :(float)x :(float)y;
- (void) rotate___float :(float)ang;
- (void) scale___float_float :(float) sx :(float) sy;
- (void) setAlpha___float :(float)alpha;
- (void) setFont___org_xmlvm_iphone_CGFont: (org_xmlvm_iphone_CGFont*)font;
- (void) setFontSize___float :(float)size;
- (void) setShadowWithColor___float_float_float_float_ARRAYTYPE: (float) dx :(float) dy :(float) blur_radius :(XMLVMArray*) color;
- (void) showTextAtPoint___float_float_java_lang_String: (float)x: (float)y: (NSString*)text;
- (void) showText___java_lang_String: (NSString*)text;
- (org_xmlvm_iphone_CGPoint*) getTextPosition__;
- (void) setTextDrawingMode___int :(int)mode;
- (void) beginPath__;
- (void) moveToPoint___float_float: (float) x :(float) y;
- (void) addLineToPoint___float_float: (float) x :(float) y;
- (void) drawPath___int: (int) mode;

- (void) storeState__;
- (void) restoreState__;
- (org_xmlvm_iphone_CGRect*)getClip__;

- (void) drawImage___org_xmlvm_iphone_CGRect_org_xmlvm_iphone_CGImage: (org_xmlvm_iphone_CGRect*)rect: (org_xmlvm_iphone_CGImage*)image;

@end
