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

#import "java_lang_String.h"
#import "java_lang_RuntimeException.h"
#import "java_lang_System.h"

// java.lang.String
//----------------------------------------------------------------------------
@implementation NSMutableString (cat_java_lang_String)

static BOOL instanceof(id obj, const char *className) {
	return obj != JAVA_NULL &&
    ([obj isKindOfClass: objc_getClass(className)] ||
     [obj conformsToProtocol: objc_getProtocol(className)]);
}

+ (int) encodingToNSEncoding:(NSString*) encoding {
    encoding = [encoding lowercaseString];
    if ([encoding isEqualToString:@"utf-8"]) {
        return NSUTF8StringEncoding;
    } else if ([encoding isEqualToString:@"us-ascii"]) {
        return NSASCIIStringEncoding;
    } else if ([encoding isEqualToString:@"utf-16"]) {
        return NSUTF16StringEncoding;
    } else if ([encoding isEqualToString:@"utf-16be"]) {
        return NSUTF16BigEndianStringEncoding;
    } else if ([encoding isEqualToString:@"utf-16le"]) {
        return NSUTF16LittleEndianStringEncoding;
    } else if ([encoding isEqualToString:@"utf-32"]) {
        return NSUTF32StringEncoding;
    } else if ([encoding isEqualToString:@"utf-32be"]) {
        return NSUTF32BigEndianStringEncoding;
    } else if ([encoding isEqualToString:@"utf-32le"]) {
        return NSUTF32LittleEndianStringEncoding;
    } else if ([encoding isEqualToString:@"iso-8859-1"]) {
        return NSISOLatin1StringEncoding;
    } else if ([encoding isEqualToString:@"iso-8859-2"]) {
        return NSISOLatin2StringEncoding;
    } else if ([encoding isEqualToString:@"windows-1251"]) {
        return NSWindowsCP1251StringEncoding;
    } else if ([encoding isEqualToString:@"windows-1252"]) {
        return NSWindowsCP1252StringEncoding;
    } else if ([encoding isEqualToString:@"windows-1253"]) {
        return NSWindowsCP1253StringEncoding;
    } else if ([encoding isEqualToString:@"windows-1254"]) {
        return NSWindowsCP1254StringEncoding;
    } else if ([encoding isEqualToString:@"windows-1250"]) {
        return NSWindowsCP1250StringEncoding;
    } else if ([encoding isEqualToString:@"iso-2022-jp"]) {
        return NSISO2022JPStringEncoding;
    } else if ([encoding isEqualToString:@"euc-jp"]) {
        return NSJapaneseEUCStringEncoding;
    } else if ([encoding isEqualToString:@"x-macsymbol"]) {
        return NSSymbolStringEncoding;
    } else if ([encoding isEqualToString:@"shift_jis"]) {
        return NSShiftJISStringEncoding;
    } else if ([encoding isEqualToString:@"x-macroman"]) {
        return NSMacOSRomanStringEncoding;
    } else {
        return NSUTF8StringEncoding;
    }
}

- (void) __init_java_lang_String__
{
	[self setString: @""];
}

- (void) __init_java_lang_String___java_lang_String: (java_lang_String*) str
{
	[self setString: str];
}

- (void) __init_java_lang_String___byte_ARRAYTYPE: (XMLVMArray*) bytes
{
	for(int i = 0; i < bytes->length; i++) {
		[self appendFormat:@"%c", (char) bytes->array.b[i]];
	}
}

- (void) __init_java_lang_String___byte_ARRAYTYPE_java_lang_String: (XMLVMArray*) bytes :(java_lang_String*) encoding
{
	for(int i = 0; i < bytes->length; i++) {
		[self appendFormat:@"%c", (char) bytes->array.b[i]];
	}
}

- (void) __init_java_lang_String___char_ARRAYTYPE: (XMLVMArray*) chars
{
	for(int i = 0; i < chars->length; i++) {
		[self appendFormat:@"%C", (unichar) chars->array.c[i]];
	}
}

- (void) __init_java_lang_String___char_ARRAYTYPE_int_int: (XMLVMArray*) chars :(int) offset :(int) count
{
	for(int i = offset; i < offset + count; i++) {
		[self appendFormat:@"%C", (unichar) chars->array.c[i]];
	}
}

+ (java_lang_String*) valueOf___int: (int) i
{
	NSNumber* n = [NSNumber numberWithInt: i];
	return [[NSString alloc] initWithString: [n stringValue]];
}

+ (java_lang_String*) valueOf___long: (JAVA_LONG) l {
	NSNumber* n = [NSNumber numberWithLong: l];
	return [[NSString alloc] initWithString: [n stringValue]];
}

+ (java_lang_String*) valueOf___float: (float) f
{
	NSNumber* n = [NSNumber numberWithFloat: f];
	return [[NSString alloc] initWithString: [n stringValue]];
}

+ (java_lang_String*) valueOf___double: (double) d
{
	NSNumber* n = [NSNumber numberWithDouble: d];
	return [[NSString alloc] initWithString: [n stringValue]];
}

+ (java_lang_String*) valueOf___java_lang_Object: (java_lang_Object*) o
{
	if ([o isKindOfClass: [NSString class]] == YES) {
		return [[NSString alloc] initWithString: (NSString*) o];
	}
	return [[NSString alloc] initWithString: @"Unkown type in valueOf___java_lang_Object"];
}

+ (java_lang_String*) format___java_lang_String_java_lang_Object_ARRAYTYPE: (java_lang_String*) format: (XMLVMArray*) array
{
    format = [format stringByReplacingOccurrencesOfString:@"%c" withString:@"%C"];
    
    format = [[format stringByReplacingOccurrencesOfString:@"%b" withString:@"%@"] stringByReplacingOccurrencesOfString:@"%B" withString:@"%@"];
    
    format = [format stringByReplacingOccurrencesOfString:@"%d" withString:@"%lld"];
    format = [format stringByReplacingOccurrencesOfString:@"%ο" withString:@"%llο"];
    format = [[format stringByReplacingOccurrencesOfString:@"%x" withString:@"%llx"] stringByReplacingOccurrencesOfString:@"%X" withString:@"%llX"];
    
    format = [[format stringByReplacingOccurrencesOfString:@"%s" withString:@"%@"] stringByReplacingOccurrencesOfString:@"%S" withString:@"%@"];
    format = [[format stringByReplacingOccurrencesOfString:@"%t" withString:@"%@"] stringByReplacingOccurrencesOfString:@"%T" withString:@"%@"];
    
    format =  [format stringByReplacingOccurrencesOfString:@"%n" withString:@"\n"];

    void * vararg = [array toMallocedVarArg];
    NSString * res = [[NSString alloc] initWithFormat:format arguments:vararg];
    free(vararg);
    return res;
}

- (void) getChars___int_int_char_ARRAYTYPE_int:(int)srcBegin:(int)srcEnd:(XMLVMArray*)dst:(int)dstBegin {
	if (srcBegin < 0) {
		java_lang_RuntimeException* ex = [[java_lang_RuntimeException alloc] init];
		[ex __init_java_lang_RuntimeException___java_lang_String:[NSMutableString stringWithString:@"Out of bounds: srcBegin"]];
		@throw ex;
	}
	if (srcEnd > [self length__]) {
		java_lang_RuntimeException* ex = [[java_lang_RuntimeException alloc] init];
		[ex __init_java_lang_RuntimeException___java_lang_String:[NSMutableString stringWithString:@"Out of bounds: srcEnd"]];
		@throw ex;
	}
	if (srcBegin > srcEnd) {
		java_lang_RuntimeException* ex = [[java_lang_RuntimeException alloc] init];
		[ex __init_java_lang_RuntimeException___java_lang_String:[NSMutableString stringWithString:@"Out of bounds: srcEnd - srcBegin"]];
		@throw ex;
	}

	int offset = 0;

	int j = dstBegin;
	int n = offset + srcEnd;
	int i = offset + srcBegin; //srcPos

	while (i < n) {
		dst->array.b[j++] = [self characterAtIndex:i++];
	}
}

- (XMLVMArray*) getBytes__
{
    return [self getBytes___java_lang_String:@"utf-8"];
}

-(XMLVMArray*) getBytes___java_lang_String:(java_lang_String*) encoding {
    int enc = [java_lang_String encodingToNSEncoding:encoding];
    int size = [self lengthOfBytesUsingEncoding:enc];
    return [XMLVMArray createSingleDimensionWithType:3 /*BYTES*/ size:size andData:[self cStringUsingEncoding:enc]];
}

- (XMLVMArray*) toCharArray__
{
	XMLVMArray *chars = [XMLVMArray createSingleDimensionWithType:2 /*CHAR*/ andSize: self.length];
	for (int i = 0; i < self.length; i++) {
		chars->array.c[i] = [self characterAtIndex:i];
	}
	return chars;
}

- (unichar) charAt___int: (int) idx
{
	return [self characterAtIndex: idx];
}

- (int) lastIndexOf___int: (int) ch
{
	int i;
	for (i = [self length] - 1; i >= 0; i--) {
		if ([self characterAtIndex: i] == ch)
			break;
	}
	return i;
}

- (int) contains___java_lang_CharSequence: (java_lang_CharSequence*) str
{
	return [self rangeOfString:str].location != NSNotFound;
}

- (int) endsWith___java_lang_String: (java_lang_String*) s
{
	return [self hasSuffix: s] == YES ? 1 : 0;
}

- (java_lang_String*) substring___int_int: (int) from :(int) to
{
	NSRange range;
	range.location = from;
	range.length = to - from;
	return_XMLVM(substringWithRange: range)
}

- (java_lang_String*) substring___int: (int) from
{
	return_XMLVM(substringFromIndex: from)
}

//Signature from java_lang_Comparable
- (int) compareTo___java_lang_Object: (java_lang_Object*) obj {
	if (!instanceof(obj, "java_lang_String")) {
        //TODO throw a java_lang_ClassCastException (doesn't exist yet) instead of a java_lang_RuntimeException
		java_lang_RuntimeException* ex = [[java_lang_RuntimeException alloc] init];
		[ex __init_java_lang_RuntimeException___java_lang_String:[NSMutableString stringWithString:@"ClassCastException"]];
		@throw ex;
	}
	return [self compareTo___java_lang_String:(java_lang_String*)obj];
}

- (int) compareTo___java_lang_String: (java_lang_String*) str
{
	return [self compare: str];
}

- (int) equals___java_lang_Object: (java_lang_Object*) o
{
	if ([o isKindOfClass: [NSString class]] == NO) {
		return 0;
	}
	return [self compare: (NSString*) o] == 0;
}

- (int) equalsIgnoreCase___java_lang_String: (java_lang_String*) s
{
	return [self caseInsensitiveCompare: s] == 0;
}

- (java_lang_String*) toString__
{
	return_XMLVM(self)
}

- (java_lang_String*) toLowerCase__
{
	return_XMLVM(lowercaseString)
}

 - (java_lang_String*) toUpperCase__
 {
	return_XMLVM(uppercaseString)
 }

- (int) startsWith___java_lang_String: (java_lang_String*) s {
	return [self hasPrefix: s] == YES ? 1 : 0;
}

- (int) lastIndexOf___java_lang_String: (java_lang_String*) s {
	NSRange range = [self rangeOfString: s options:NSBackwardsSearch];
	if (range.location == NSNotFound) {
		return -1;
	}

	return range.location;
}

- (int) indexOf___int: (int) ch {
	int i;
	
	for (i = 0; i < [self length]; i++) {
		if ([self characterAtIndex: i] == ch)
			return i;
	}
	
	return -1;
}
	
- (int) indexOf___java_lang_String: (java_lang_String*) s {
	NSRange range = [self rangeOfString: s];
	if (range.location == NSNotFound) {
		return -1;
	}
	return range.location;
}

- (int) length__
{
	return [self length];
}

- (java_lang_String*) replace___char_char:(unichar)from :(unichar)to
{
	NSString * res = [self stringByReplacingOccurrencesOfString:[NSString stringWithFormat:@"%C",from] withString:[NSString stringWithFormat:@"%C",to]];
	return [res retain];
}

- (java_lang_String*) replace___java_lang_CharSequence_java_lang_CharSequence:(java_lang_CharSequence*)from :(java_lang_CharSequence*)to
{
	NSString * res = [self stringByReplacingOccurrencesOfString:from withString:to];
	return [res retain];
}

- (java_lang_String*) replaceFirst___java_lang_String_java_lang_String:(java_lang_String*)regex :(java_lang_String*)replacement
{
#if __IPHONE_OS_VERSION_MIN_REQUIRED <= __IPHONE_3_1
#define NSRegularExpressionSearch NSLiteralSearch
	NSLog(@"String.replaceFirst() not supported");
#endif
	NSRange found = [self rangeOfString:regex options:NSRegularExpressionSearch];
	if (found.location == NSNotFound) {
		return_XMLVM(self);
	}
	NSString * res = [NSString stringWithFormat:@"%@%@%@", 
					  [self substringToIndex:found.location],
					  replacement,
					  [self substringFromIndex:(found.location+found.length)]];
	return [res retain];
}

- (java_lang_String*) replaceAll___java_lang_String_java_lang_String :(java_lang_String*)a :(java_lang_String*)b {
	NSMutableString *m = [[NSMutableString alloc] initWithString:self];
	NSRange range;
	range.location = 0;
	range.length = [m length__];

	[m replaceOccurrencesOfString:a withString:b options:(NSLiteralSearch) range:range];
	return m;
}

- (int) matches___java_lang_String:(java_lang_String*)regex
{
//#if __IPHONE_OS_VERSION_MIN_REQUIRED <= __IPHONE_3_1
//#define NSRegularExpressionSearch NSLiteralSearch
//	NSLog(@"String.replaceFirst() not supported");
//#endif
	NSRange found = [self rangeOfString:regex options:NSRegularExpressionSearch];
	return found.location != NSNotFound;
}

- (XMLVMArray*) split___java_lang_String :(java_lang_String*)s
{
	NSArray *chunks = [self componentsSeparatedByString:s];
	int length = [chunks count];

	XMLVMArray *result = [XMLVMArray createSingleDimensionWithType: 0 andSize: length]; // object reference array
	for (int i=0; i<length; i++) {
		result->array.o[i] = [[chunks objectAtIndex: i] retain];
	}

	return result;
}

- (java_lang_String*) trim__ {
	int len = [self length];
	int st = 0;
	int off = 0; //this should be initialized from "offset"

	while (st < len && [self charAt___int:off + st] <= ' ') {
		st++;
	}
	while (st < len && [self charAt___int:off + len - 1] <= ' ') {
		len--;
	}
	return (st > 0 || len < [self length]) ? [self substring___int_int:st:len] : [self retain];
}

- (int) isEmpty__
{
	return [self length] == 0;
}

@end
