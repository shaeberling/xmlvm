
#include "org_xmlvm_iphone_MKCoordinateSpan.h"

__TIB_DEFINITION_org_xmlvm_iphone_MKCoordinateSpan __TIB_org_xmlvm_iphone_MKCoordinateSpan = {
    0, // classInitialized
    "org.xmlvm.iphone.MKCoordinateSpan", // className
    (__TIB_DEFINITION_TEMPLATE*) &__TIB_org_xmlvm_iphone_NSObject, // extends
};

//XMLVM_BEGIN_IMPLEMENTATION
//XMLVM_END_IMPLEMENTATION


void __INIT_org_xmlvm_iphone_MKCoordinateSpan()
{
    __TIB_org_xmlvm_iphone_MKCoordinateSpan.classInitialized = 1;
    // Initialize base class if necessary
    if (!__TIB_org_xmlvm_iphone_NSObject.classInitialized) __INIT_org_xmlvm_iphone_NSObject();
    __TIB_org_xmlvm_iphone_MKCoordinateSpan.newInstanceFunc = __NEW_INSTANCE_org_xmlvm_iphone_MKCoordinateSpan;
    // Copy vtable from base class
    XMLVM_MEMCPY(__TIB_org_xmlvm_iphone_MKCoordinateSpan.vtable, __TIB_org_xmlvm_iphone_NSObject.vtable, sizeof(__TIB_org_xmlvm_iphone_NSObject.vtable));
    // Initialize vtable for this class
    // Initialize vtable for implementing interfaces
    __TIB_org_xmlvm_iphone_MKCoordinateSpan.numImplementedInterfaces = 0;
    __TIB_org_xmlvm_iphone_MKCoordinateSpan.implementedInterfaces = (__TIB_DEFINITION_TEMPLATE* (*)[1]) XMLVM_MALLOC(sizeof(__TIB_DEFINITION_TEMPLATE*) * 0);


    //XMLVM_BEGIN_WRAPPER[__INIT_org_xmlvm_iphone_MKCoordinateSpan]
    //XMLVM_END_WRAPPER
}

void __DELETE_org_xmlvm_iphone_MKCoordinateSpan(void* me, void* client_data)
{
    //XMLVM_BEGIN_WRAPPER[__DELETE_org_xmlvm_iphone_MKCoordinateSpan]
    //XMLVM_END_WRAPPER
}

JAVA_OBJECT __NEW_org_xmlvm_iphone_MKCoordinateSpan()
{
    if (!__TIB_org_xmlvm_iphone_MKCoordinateSpan.classInitialized) __INIT_org_xmlvm_iphone_MKCoordinateSpan();
    org_xmlvm_iphone_MKCoordinateSpan* me = (org_xmlvm_iphone_MKCoordinateSpan*) XMLVM_MALLOC(sizeof(org_xmlvm_iphone_MKCoordinateSpan));
    me->tib = &__TIB_org_xmlvm_iphone_MKCoordinateSpan;
    me->fields.org_xmlvm_iphone_MKCoordinateSpan.latitudeDelta_ = 0;
    me->fields.org_xmlvm_iphone_MKCoordinateSpan.longitudeDelta_ = 0;
    //XMLVM_BEGIN_WRAPPER[__NEW_org_xmlvm_iphone_MKCoordinateSpan]
    //XMLVM_END_WRAPPER
    return me;
}

JAVA_OBJECT __NEW_INSTANCE_org_xmlvm_iphone_MKCoordinateSpan()
{
    JAVA_OBJECT me = JAVA_NULL;
    return me;
}

void org_xmlvm_iphone_MKCoordinateSpan___INIT____double_double(JAVA_OBJECT me, JAVA_DOUBLE n1, JAVA_DOUBLE n2)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_MKCoordinateSpan___INIT____double_double]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

