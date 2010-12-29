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

#import "java_lang_Thread.h"
#import "java_lang_System.h"
#import "java_lang_IllegalArgumentException.h"
#include <pthread.h>

static NSMutableDictionary* threadMap;

@implementation java_lang_Thread 

+ (void) initialize
{
	threadMap = [[NSMutableDictionary alloc] init];
}

- (id) init
{
	[super init];
	thread = [[NSThread alloc] initWithTarget:self selector:@selector(threadCallback:) object:nil];
	runnable = [self retain];
	interrupted = FALSE;
	waitingObj = NULL; // no need for [NSNull null] since this object is never referenced by generated source code
	alive = FALSE;
	[threadMap setObject:self forKey:[NSValue valueWithNonretainedObject:thread]];
	return self;
}

- (id) initWithCurrentThread
{
	[super init];
	thread = [[NSThread currentThread] retain];
	runnable = nil;
	interrupted = FALSE;
	waitingObj = NULL; // no need for [NSNull null] since this object is never referenced by generated source code
	[threadMap setObject:self forKey:[NSValue valueWithNonretainedObject:thread]];
	return self;
}

- (void) dealloc
{
	[threadMap removeObjectForKey:[NSValue valueWithNonretainedObject:thread]];
	[thread release];
	[runnable release];
	[waitingObj release];
	[super dealloc];
}

- (void) __init_java_lang_Thread__
{
}

- (void) __init_java_lang_Thread___java_lang_Runnable: (id<java_lang_Runnable>) r
{
	[r retain];
	[runnable release];
	runnable = r;
}

- (void) threadCallback: (id) arg
{
    NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init];
	m_pthread_t = pthread_self();

	@synchronized (self) {
		alive = TRUE;
	}
	[runnable run__];
	@synchronized (self) {
		alive = FALSE;
	}

	[self acquireLockRecursive];
	@try {
		// Notify the thread is finished
		[self notifyAll__];
	} @finally {
		[self releaseLockRecursive];
	}

	[self release];
	[pool release];
}

- (void) run__
{
	// Will be overridden in derived class
}

- (void) start__
{
	[self retain];
	[thread start];
}

+ (void) sleep___long: (JAVA_LONG) millis
{
	java_lang_Object* obj = [[java_lang_Object alloc] init];
	[obj __init_java_lang_Object__];

	[obj acquireLockRecursive];
	@try {
		// Since no other thread has access to this object, it will never be
		// notified. It will either timeout or be interrupted.
		[obj wait___long:millis];
	} @finally {
		[obj releaseLockRecursive];
		[obj release];
	}
}

- (BOOL) isAlive__ {
	BOOL result = FALSE;
	@synchronized (self) {
		result = alive;
	}
	return result;
}

- (void) join___long:(JAVA_LONG)millis {
	[self acquireLockRecursive];
	@try {
		JAVA_LONG base = [java_lang_System currentTimeMillis__];
		JAVA_LONG now = 0;

		if (millis < 0) {
			java_lang_IllegalArgumentException* ex = [[java_lang_IllegalArgumentException alloc] init];
			[ex __init_java_lang_IllegalArgumentException___java_lang_String:[NSMutableString stringWithString:@"timeout value is negative"]];
			@throw ex;
		}

		if (millis == 0) {
			while ([self isAlive__]) {
				[self wait___long:0];
			}
		} else {
			BOOL done = FALSE;
			while (!done && [self isAlive__]) {
				JAVA_LONG delay = millis - now;
				if (delay <= 0) {
					done = TRUE;
				} else {
					[self wait___long:delay];
					now = [java_lang_System currentTimeMillis__] - base;
				}
			}
		}
	} @finally {
		[self releaseLockRecursive];
	}
}

- (void) join__ {
	[self join___long:0];
}

+ (java_lang_Thread*) currentThread__
{
	NSValue* key = [NSValue valueWithNonretainedObject:[NSThread currentThread]];
	java_lang_Thread* javaCurrentThread = [threadMap objectForKey:key];

	// javaCurrentThread will NOT be null if the thread was created using java_lang_Thread since it is added to threadMap in the constructor.
	// However, if this is the main thread or the thread was created without java_lang_Thread, it could be null.
	if (javaCurrentThread == nil) {
		javaCurrentThread = [[java_lang_Thread alloc] initWithCurrentThread];
	} else {
		[javaCurrentThread retain];
	}

	return javaCurrentThread;
}

/**
 * Set the object on which the thread is waiting. This should ONLY be called within java_lang_Object's wait(), wait(long) or wait(long, int).
 * @param obj the object the thread is synchronized and waiting on (this is used to interrupt a wait). This can be NULL
 */
- (void) setWaitingObject: (java_lang_Object*) obj {
	@synchronized (self) {
		waitingObj = obj;
	}
}

- (void) interrupt__ {
	java_lang_Object* objectForInterrupt = NULL;
	@synchronized (self) {
		interrupted = TRUE;

		if (waitingObj != NULL) {
			objectForInterrupt = waitingObj;
			[objectForInterrupt retain];
		}
	}

	// Interrupt the "wait" outside of the synchronized block.
	// Otherwise, a deadlock could occur.
	if (objectForInterrupt != NULL) {
		NSInteger threadId = (NSInteger)m_pthread_t;
		[objectForInterrupt interruptWait:threadId];
		[objectForInterrupt release];
	}
}

- (void) setInterrupted:(BOOL)isInterrupted {
	@synchronized (self) {
		interrupted = isInterrupted;
	}
}

/**
 * Clear the current thread's interrupted status and return the previous value
 * @return TRUE if the thread was already interrupted, else false
 */
+ (BOOL) interrupted__ {
	BOOL result = FALSE;
	java_lang_Thread* t = [java_lang_Thread currentThread__];
	@synchronized (t) {
		result = [t isInterrupted__];
		[t setInterrupted: FALSE];
	}
	return result;
}

- (BOOL) isInterrupted__ {
	BOOL result = FALSE;
	@synchronized (self) {
		result = interrupted;
	}
	return result;
}

- (java_lang_String*) getName__ {
	id name = [thread name];
	if (name == NULL) {
		name = @"";
	}
	return name;
}

- (void) setName___java_lang_String:(java_lang_String*)name {
	[thread setName:name];
}

@end
