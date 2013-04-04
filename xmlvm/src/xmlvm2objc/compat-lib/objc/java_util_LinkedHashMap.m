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

#import "java_util_LinkedHashMap.h"
#import "java_util_IteratorImpl.h"
#import "java_util_HashMap_EntrySet.h"
#import "java_util_ArrayList.h"


@implementation java_util_LinkedHashMap 

- (id) init
{
    return [self initWithCapacity:0];
}

- (id)initWithCapacity:(int) size
{
    [super init];
    dict = [[NSMutableDictionary alloc] initWithCapacity:size];
    keys = [[NSMutableArray alloc] initWithCapacity:size];
	return self;
}

- (id)initWithObjects:(NSArray *)objects forKeys:(NSArray *)keys
{
    [super init];
    dict = [[NSMutableDictionary alloc] initWithObjects:objects forKeys:keys];
    keys = [[NSMutableArray alloc] initWithArray:keys];
    return self;
}

- (void)dealloc
{
    [dict release];
    [keys release];
    [super dealloc];
}


- (NSUInteger)count
{
	return [keys count];
}

- (id)objectForKey:(id) key
{
	return [dict objectForKey:key];
}

- (NSEnumerator *)keyEnumerator
{
	return [keys objectEnumerator];
}

- (void)setObject:(id)object forKey:(id < NSCopying >) key
{
	if (![dict objectForKey:key]) {
		[keys addObject:key];
	}
	[dict setObject:object forKey:key];
}

- (void)removeObjectForKey:(id) key
{
	[dict removeObjectForKey:key];
	[keys removeObject:key];
}


- (void) __init_java_util_LinkedHashMap__
{
}

- (void) __init_java_util_LinkedHashMap___int: (int) size
{
}

- (void) clear__
{
	[self removeAllObjects];
}

- (java_util_Collection*) values__
{
	return [self retain];
}

- (java_util_Iterator*) iterator__
{
	return [[java_util_IteratorImpl alloc] init: [self objectEnumerator]];
}

- (int) size__
{
	return [self count];
}

- (java_util_Set*) keySet__ {
    NSMutableOrderedSet * keyset = [[NSMutableOrderedSet alloc] initWithArray:keys];
    return keyset;
}

- (java_util_Set*) entrySet__
{
    [self sortMap];

	//Since we're using an NSMutableDictionary, we will construct a new EntrySet
	java_util_HashMap_EntrySet* es = [[java_util_HashMap_EntrySet alloc] init];
	[es __init_java_util_HashMap_EntrySet___java_util_HashMap:self];
	return es;
}

- (java_lang_Object*) put___java_lang_Object_java_lang_Object:(java_lang_Object*) key: (java_lang_Object*) value {
	java_lang_Object* oldObj = [self get___java_lang_Object:key];
	id k = [key conformsToProtocol: @protocol(NSCopying)] ? key : [NSValue valueWithPointer: key];
	[self setObject:value forKey:k];
	return oldObj;
}

- (java_lang_Object*) get___java_lang_Object:(java_lang_Object*) key {
	id k = [key conformsToProtocol: @protocol(NSCopying)] ? key : [NSValue valueWithPointer: key];
	return_XMLVM(objectForKey: k);
}

- (BOOL) containsKey___java_lang_Object: (java_lang_Object*) key
{
	return [self objectForKey:key] != nil;
}

- (BOOL) containsValue___java_lang_Object: (java_lang_Object*) value
{
	NSEnumerator *enumerator = [self objectEnumerator];
	id enumval;
	while ((enumval = [enumerator nextObject])) {
		if (enumval==value) {
			return YES;
		}
	}
	return NO;
}


- (java_lang_Object*) remove___java_lang_Object:(java_lang_Object*) key
{
	id k = [key conformsToProtocol: @protocol(NSCopying)] ? key : [NSValue valueWithPointer: key];
	java_lang_Object* item = [[self objectForKey:k] retain];
	item = XMLVM_NIL2NULL(item);
	[self removeObjectForKey:k];
	return item;
}

@end
