#include "org_xmlvm_iphone_CGRect.h"

#include "org_xmlvm_iphone_UIScreen.h"

__CLASS_DEFINITION_org_xmlvm_iphone_UIScreen __CLASS_org_xmlvm_iphone_UIScreen = {
    0, // classInitialized
    "org.xmlvm.iphone.UIScreen", // className
    (__CLASS_DEFINITION_TEMPLATE*) &__CLASS_org_xmlvm_iphone_NSObject, // extends
};

//XMLVM_BEGIN_IMPLEMENTATION
#import "UIKit/UIScreen.h"
//XMLVM_END_IMPLEMENTATION


void __INIT_org_xmlvm_iphone_UIScreen()
{
    __CLASS_org_xmlvm_iphone_UIScreen.classInitialized = 1;
    // Initialize base class if necessary
    if (!__CLASS_org_xmlvm_iphone_NSObject.classInitialized) __INIT_org_xmlvm_iphone_NSObject();
    __CLASS_org_xmlvm_iphone_UIScreen.newInstanceFunc = __NEW_INSTANCE_org_xmlvm_iphone_UIScreen;
    // Copy vtable from base class
    XMLVM_MEMCPY(__CLASS_org_xmlvm_iphone_UIScreen.vtable, __CLASS_org_xmlvm_iphone_NSObject.vtable, sizeof(__CLASS_org_xmlvm_iphone_NSObject.vtable));
    // Initialize vtable for this class
    __CLASS_org_xmlvm_iphone_UIScreen.vtable[14] = (VTABLE_PTR) &org_xmlvm_iphone_UIScreen_getBounds__;
    __CLASS_org_xmlvm_iphone_UIScreen.vtable[15] = (VTABLE_PTR) &org_xmlvm_iphone_UIScreen_getApplicationFrame__;
    // Initialize vtable for implementing interfaces
    __CLASS_org_xmlvm_iphone_UIScreen.numImplementedInterfaces = 0;
    __CLASS_org_xmlvm_iphone_UIScreen.implementedInterfaces = (__CLASS_DEFINITION_TEMPLATE* (*)[1]) XMLVM_MALLOC(sizeof(__CLASS_DEFINITION_TEMPLATE*) * 0);

    org_xmlvm_iphone_UIScreen___CLINIT_();
    //XMLVM_BEGIN_WRAPPER[__INIT_org_xmlvm_iphone_UIScreen]
    //XMLVM_END_WRAPPER
}

JAVA_OBJECT __NEW_org_xmlvm_iphone_UIScreen()
{
    if (!__CLASS_org_xmlvm_iphone_UIScreen.classInitialized) __INIT_org_xmlvm_iphone_UIScreen();
    org_xmlvm_iphone_UIScreen* me = (org_xmlvm_iphone_UIScreen*) XMLVM_MALLOC(sizeof(org_xmlvm_iphone_UIScreen));
    me->__class = &__CLASS_org_xmlvm_iphone_UIScreen;
    //XMLVM_BEGIN_WRAPPER[__NEW_org_xmlvm_iphone_UIScreen]
    //XMLVM_END_WRAPPER
    return me;
}

JAVA_OBJECT __NEW_INSTANCE_org_xmlvm_iphone_UIScreen()
{
    JAVA_OBJECT me = JAVA_NULL;
    return me;
}

void __DELETE_org_xmlvm_iphone_UIScreen(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[__DELETE_org_xmlvm_iphone_UIScreen]
    //XMLVM_END_WRAPPER
}

void org_xmlvm_iphone_UIScreen___CLINIT_()
{
    if (!__CLASS_org_xmlvm_iphone_UIScreen.classInitialized) __INIT_org_xmlvm_iphone_UIScreen();
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_UIScreen___CLINIT___]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

JAVA_OBJECT org_xmlvm_iphone_UIScreen_mainScreen__()
{
    if (!__CLASS_org_xmlvm_iphone_UIScreen.classInitialized) __INIT_org_xmlvm_iphone_UIScreen();
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_UIScreen_mainScreen__]
	org_xmlvm_iphone_UIScreen* toRet = __NEW_org_xmlvm_iphone_UIScreen();
	toRet->org_xmlvm_iphone_UIScreen.ocScreen = [UIScreen mainScreen];
	return toRet;
    //XMLVM_END_WRAPPER
}

JAVA_OBJECT org_xmlvm_iphone_UIScreen_getBounds__(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_UIScreen_getBounds__]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

JAVA_OBJECT org_xmlvm_iphone_UIScreen_getApplicationFrame__(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_UIScreen_getApplicationFrame__]
	org_xmlvm_iphone_UIScreen* thiz = me;
	UIScreen *screen = thiz->org_xmlvm_iphone_UIScreen.ocScreen;
	CGRect rect = [screen applicationFrame];
	JAVA_OBJECT toRet = __NEW_org_xmlvm_iphone_CGRect();
	org_xmlvm_iphone_CGRect___INIT____float_float_float_float(
		toRet, rect.origin.x, rect.origin.y, rect.size.width, rect.size.height);
	return toRet;
    //XMLVM_END_WRAPPER
}

