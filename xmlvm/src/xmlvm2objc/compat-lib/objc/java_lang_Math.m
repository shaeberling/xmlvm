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

#import "java_lang_Math.h"



// java.lang.Math
//----------------------------------------------------------------------------
@implementation java_lang_Math;

+ (double) random__
{
    return (double)random()/2147483647.0;
}

+ (float) pow___float_float: (float) x : (float) y
{
    return pow(x, y);
}

+ (double) pow___double_double: (double) x : (double) y
{
    return pow(x, y);
}

+ (double) sqrt___double: (double) x
{
    return sqrt(x);
}

+ (double) sin___double: (double) a
{
	return sin(a);
}

+ (double) cos___double: (double) a
{
	return cos(a);
}

+ (double) tan___double: (double) a
{
    return tan(a);
}

+ (double) atan2___double_double: (double) y :(double) x
{
	return atan2(y,x);
}

+ (double) asin___double: (double) x
{
    return asin(x);
}

+ (int) abs___int: (int) i
{
    return i < 0 ? i * -1 : i;
}

+ (float) abs___float: (float) f
{
    return f < 0.0 ? f * -1.0 : f;
}

+ (float) max___float_float :(float) x :(float) y
{
	return x < y ? y : x;
}

+ (double) max___double_double :(double) x :(double) y
{
	return x < y ? y : x;
}

+ (int) max___int_int :(int) x :(int) y
{
	return x < y ? y : x;
}

+ (int) max___long_long :(JAVA_LONG) x :(JAVA_LONG) y
{
	return x < y ? y : x;
}

+ (float) min___float_float :(float) x :(float) y
{
	return x >  y ? y : x;
}

+ (double) min___double_double :(double) x :(double) y
{
	return x >  y ? y : x;
}

+ (int) min___int_int :(int) x :(int) y
{
	return x >  y ? y : x;
}

+ (int) min___long_long :(JAVA_LONG) x :(JAVA_LONG) y
{
	return x >  y ? y : x;
}

+ (float) round___float: (float) a
{
	return roundf(a);
}

+ (double) round___double: (double) a
{
	return round(a);
}

+ (double) floor___double:(double) a
{
	return floor(a);
}

+ (double) ceil___double:(double) a
{
	return ceil(a);
}

@end
