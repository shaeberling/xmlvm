
#include "org_xmlvm_iphone_CLLocationCoordinate2D.h"

__TIB_DEFINITION_org_xmlvm_iphone_CLLocationCoordinate2D __TIB_org_xmlvm_iphone_CLLocationCoordinate2D = {
    0, // classInitialized
    "org.xmlvm.iphone.CLLocationCoordinate2D", // className
    (__TIB_DEFINITION_TEMPLATE*) &__TIB_org_xmlvm_iphone_NSObject, // extends
};

//XMLVM_BEGIN_IMPLEMENTATION
//XMLVM_END_IMPLEMENTATION


void __INIT_org_xmlvm_iphone_CLLocationCoordinate2D()
{
    __TIB_org_xmlvm_iphone_CLLocationCoordinate2D.classInitialized = 1;
    // Initialize base class if necessary
    if (!__TIB_org_xmlvm_iphone_NSObject.classInitialized) __INIT_org_xmlvm_iphone_NSObject();
    __TIB_org_xmlvm_iphone_CLLocationCoordinate2D.newInstanceFunc = __NEW_INSTANCE_org_xmlvm_iphone_CLLocationCoordinate2D;
    // Copy vtable from base class
    XMLVM_MEMCPY(__TIB_org_xmlvm_iphone_CLLocationCoordinate2D.vtable, __TIB_org_xmlvm_iphone_NSObject.vtable, sizeof(__TIB_org_xmlvm_iphone_NSObject.vtable));
    // Initialize vtable for this class
    // Initialize vtable for implementing interfaces
    __TIB_org_xmlvm_iphone_CLLocationCoordinate2D.numImplementedInterfaces = 0;
    __TIB_org_xmlvm_iphone_CLLocationCoordinate2D.implementedInterfaces = (__TIB_DEFINITION_TEMPLATE* (*)[1]) XMLVM_MALLOC(sizeof(__TIB_DEFINITION_TEMPLATE*) * 0);


    //XMLVM_BEGIN_WRAPPER[__INIT_org_xmlvm_iphone_CLLocationCoordinate2D]
    //XMLVM_END_WRAPPER
}

void __DELETE_org_xmlvm_iphone_CLLocationCoordinate2D(void* me, void* client_data)
{
    //XMLVM_BEGIN_WRAPPER[__DELETE_org_xmlvm_iphone_CLLocationCoordinate2D]
    //XMLVM_END_WRAPPER
}

JAVA_OBJECT __NEW_org_xmlvm_iphone_CLLocationCoordinate2D()
{
    if (!__TIB_org_xmlvm_iphone_CLLocationCoordinate2D.classInitialized) __INIT_org_xmlvm_iphone_CLLocationCoordinate2D();
    org_xmlvm_iphone_CLLocationCoordinate2D* me = (org_xmlvm_iphone_CLLocationCoordinate2D*) XMLVM_MALLOC(sizeof(org_xmlvm_iphone_CLLocationCoordinate2D));
    me->tib = &__TIB_org_xmlvm_iphone_CLLocationCoordinate2D;
    me->fields.org_xmlvm_iphone_CLLocationCoordinate2D.latitude_ = 0;
    me->fields.org_xmlvm_iphone_CLLocationCoordinate2D.longitude_ = 0;
    //XMLVM_BEGIN_WRAPPER[__NEW_org_xmlvm_iphone_CLLocationCoordinate2D]
    //XMLVM_END_WRAPPER
    return me;
}

JAVA_OBJECT __NEW_INSTANCE_org_xmlvm_iphone_CLLocationCoordinate2D()
{
    JAVA_OBJECT me = JAVA_NULL;
    return me;
}

void org_xmlvm_iphone_CLLocationCoordinate2D___INIT____double_double(JAVA_OBJECT me, JAVA_DOUBLE n1, JAVA_DOUBLE n2)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_CLLocationCoordinate2D___INIT____double_double]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

