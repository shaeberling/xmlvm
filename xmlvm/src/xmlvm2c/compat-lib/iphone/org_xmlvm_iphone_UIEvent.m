#include "java_util_Set.h"

#include "org_xmlvm_iphone_UIEvent.h"

__TIB_DEFINITION_org_xmlvm_iphone_UIEvent __TIB_org_xmlvm_iphone_UIEvent = {
    0, // classInitialized
    "org.xmlvm.iphone.UIEvent", // className
    (__TIB_DEFINITION_TEMPLATE*) &__TIB_org_xmlvm_iphone_NSObject, // extends
};

JAVA_OBJECT __CLASS_org_xmlvm_iphone_UIEvent;
//TODO _ARRAYTYPE not initialized
JAVA_OBJECT __CLASS_org_xmlvm_iphone_UIEvent_ARRAYTYPE;

//XMLVM_BEGIN_IMPLEMENTATION
//XMLVM_END_IMPLEMENTATION


static XMLVM_FIELD_REFLECTION_DATA __field_reflection_data[] = {
};

void __INIT_org_xmlvm_iphone_UIEvent()
{
    __TIB_org_xmlvm_iphone_UIEvent.classInitialized = 1;
    // Initialize base class if necessary
    if (!__TIB_org_xmlvm_iphone_NSObject.classInitialized) __INIT_org_xmlvm_iphone_NSObject();
    __TIB_org_xmlvm_iphone_UIEvent.newInstanceFunc = __NEW_INSTANCE_org_xmlvm_iphone_UIEvent;
    // Copy vtable from base class
    XMLVM_MEMCPY(__TIB_org_xmlvm_iphone_UIEvent.vtable, __TIB_org_xmlvm_iphone_NSObject.vtable, sizeof(__TIB_org_xmlvm_iphone_NSObject.vtable));
    // Initialize vtable for this class
    __TIB_org_xmlvm_iphone_UIEvent.vtable[14] = (VTABLE_PTR) &org_xmlvm_iphone_UIEvent_allTouches__;
    // Initialize vtable for implementing interfaces
    __TIB_org_xmlvm_iphone_UIEvent.numImplementedInterfaces = 0;
    __TIB_org_xmlvm_iphone_UIEvent.implementedInterfaces = (__TIB_DEFINITION_TEMPLATE* (*)[1]) XMLVM_MALLOC(sizeof(__TIB_DEFINITION_TEMPLATE*) * 0);

    __TIB_org_xmlvm_iphone_UIEvent.declaredFields = &__field_reflection_data[0];
    __TIB_org_xmlvm_iphone_UIEvent.numDeclaredFields = sizeof(__field_reflection_data) / sizeof(XMLVM_FIELD_REFLECTION_DATA);
    __CLASS_org_xmlvm_iphone_UIEvent = __NEW_XMLVMClass(&__TIB_org_xmlvm_iphone_UIEvent);
    __TIB_org_xmlvm_iphone_UIEvent.clazz = __CLASS_org_xmlvm_iphone_UIEvent;

    //XMLVM_BEGIN_WRAPPER[__INIT_org_xmlvm_iphone_UIEvent]
    //XMLVM_END_WRAPPER
}

void __DELETE_org_xmlvm_iphone_UIEvent(void* me, void* client_data)
{
    //XMLVM_BEGIN_WRAPPER[__DELETE_org_xmlvm_iphone_UIEvent]
    //XMLVM_END_WRAPPER
}

JAVA_OBJECT __NEW_org_xmlvm_iphone_UIEvent()
{
    if (!__TIB_org_xmlvm_iphone_UIEvent.classInitialized) __INIT_org_xmlvm_iphone_UIEvent();
    org_xmlvm_iphone_UIEvent* me = (org_xmlvm_iphone_UIEvent*) XMLVM_MALLOC(sizeof(org_xmlvm_iphone_UIEvent));
    me->tib = &__TIB_org_xmlvm_iphone_UIEvent;
    //XMLVM_BEGIN_WRAPPER[__NEW_org_xmlvm_iphone_UIEvent]
    //XMLVM_END_WRAPPER
    return me;
}

JAVA_OBJECT __NEW_INSTANCE_org_xmlvm_iphone_UIEvent()
{
    JAVA_OBJECT me = JAVA_NULL;
    me = __NEW_org_xmlvm_iphone_UIEvent();
    org_xmlvm_iphone_UIEvent___INIT___(me);
    return me;
}

void org_xmlvm_iphone_UIEvent___INIT___(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_UIEvent___INIT___]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

JAVA_OBJECT org_xmlvm_iphone_UIEvent_allTouches__(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_UIEvent_allTouches__]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

