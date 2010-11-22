#include "org_xmlvm_iphone_SKRequestDelegate.h"

#include "org_xmlvm_iphone_SKRequest.h"

__CLASS_DEFINITION_org_xmlvm_iphone_SKRequest __CLASS_org_xmlvm_iphone_SKRequest = {
    0, // classInitialized
    "org.xmlvm.iphone.SKRequest", // className
    (__CLASS_DEFINITION_TEMPLATE*) &__CLASS_org_xmlvm_iphone_NSObject, // extends
};

//XMLVM_BEGIN_IMPLEMENTATION
//XMLVM_END_IMPLEMENTATION


void __INIT_org_xmlvm_iphone_SKRequest()
{
    __CLASS_org_xmlvm_iphone_SKRequest.classInitialized = 1;
    // Initialize base class if necessary
    if (!__CLASS_org_xmlvm_iphone_NSObject.classInitialized) __INIT_org_xmlvm_iphone_NSObject();
    __CLASS_org_xmlvm_iphone_SKRequest.newInstanceFunc = __NEW_INSTANCE_org_xmlvm_iphone_SKRequest;
    // Copy vtable from base class
    XMLVM_MEMCPY(__CLASS_org_xmlvm_iphone_SKRequest.vtable, __CLASS_org_xmlvm_iphone_NSObject.vtable, sizeof(__CLASS_org_xmlvm_iphone_NSObject.vtable));
    // Initialize vtable for this class
    __CLASS_org_xmlvm_iphone_SKRequest.vtable[14] = (VTABLE_PTR) &org_xmlvm_iphone_SKRequest_start__;
    __CLASS_org_xmlvm_iphone_SKRequest.vtable[15] = (VTABLE_PTR) &org_xmlvm_iphone_SKRequest_cancel__;
    __CLASS_org_xmlvm_iphone_SKRequest.vtable[16] = (VTABLE_PTR) &org_xmlvm_iphone_SKRequest_getDelegate__;
    __CLASS_org_xmlvm_iphone_SKRequest.vtable[17] = (VTABLE_PTR) &org_xmlvm_iphone_SKRequest_setDelegate___org_xmlvm_iphone_SKRequestDelegate;
    // Initialize vtable for implementing interfaces
    __CLASS_org_xmlvm_iphone_SKRequest.numImplementedInterfaces = 0;
    __CLASS_org_xmlvm_iphone_SKRequest.implementedInterfaces = (__CLASS_DEFINITION_TEMPLATE* (*)[1]) XMLVM_MALLOC(sizeof(__CLASS_DEFINITION_TEMPLATE*) * 0);


    //XMLVM_BEGIN_WRAPPER[__INIT_org_xmlvm_iphone_SKRequest]
    //XMLVM_END_WRAPPER
}

GC_CALLBACK __DELETE_org_xmlvm_iphone_SKRequest(void * me, void * client_data)
{
    //XMLVM_BEGIN_WRAPPER[__DELETE_org_xmlvm_iphone_SKRequest]
    //XMLVM_END_WRAPPER
}

JAVA_OBJECT __NEW_org_xmlvm_iphone_SKRequest()
{
    if (!__CLASS_org_xmlvm_iphone_SKRequest.classInitialized) __INIT_org_xmlvm_iphone_SKRequest();
    org_xmlvm_iphone_SKRequest* me = (org_xmlvm_iphone_SKRequest*) XMLVM_MALLOC(sizeof(org_xmlvm_iphone_SKRequest));
    me->__class = &__CLASS_org_xmlvm_iphone_SKRequest;
    //XMLVM_BEGIN_WRAPPER[__NEW_org_xmlvm_iphone_SKRequest]
    //XMLVM_END_WRAPPER
    // Tell the GC to finalize us
    XMLVM_FINALIZE(me, __DELETE_org_xmlvm_iphone_SKRequest);
    return me;
}

JAVA_OBJECT __NEW_INSTANCE_org_xmlvm_iphone_SKRequest()
{
    JAVA_OBJECT me = JAVA_NULL;
    me = __NEW_org_xmlvm_iphone_SKRequest();
    org_xmlvm_iphone_SKRequest___INIT___(me);
    return me;
}

void org_xmlvm_iphone_SKRequest___INIT___(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_SKRequest___INIT___]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

void org_xmlvm_iphone_SKRequest_start__(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_SKRequest_start__]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

void org_xmlvm_iphone_SKRequest_cancel__(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_SKRequest_cancel__]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

JAVA_OBJECT org_xmlvm_iphone_SKRequest_getDelegate__(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_SKRequest_getDelegate__]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

void org_xmlvm_iphone_SKRequest_setDelegate___org_xmlvm_iphone_SKRequestDelegate(JAVA_OBJECT me, JAVA_OBJECT n1)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_SKRequest_setDelegate___org_xmlvm_iphone_SKRequestDelegate]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

