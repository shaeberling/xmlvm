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


#include "java_lang_Class.h"


__TIB_DEFINITION_java_lang_Class __TIB_java_lang_Class;

void __INIT_java_lang_Class()
{
	__TIB_java_lang_Class.className = "java.lang.Class";
	__TIB_java_lang_Class.extends = (__TIB_DEFINITION_TEMPLATE*) &__TIB_java_lang_Object;
	__TIB_java_lang_Class.numImplementedInterfaces = 0;
	__TIB_java_lang_Class.classInitialized = 1;
}

JAVA_OBJECT __NEW_java_lang_Class()
{
	XMLVM_ERROR("Cannot call __NEW_java_lang_Class()");
	return JAVA_NULL;
}


