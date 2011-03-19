#include "xmlvm.h"
#include "org_xmlvm_iphone_CAMediaTiming.h"
#include "org_xmlvm_iphone_CAAction.h"
#include "java_lang_String.h"

#include "org_xmlvm_iphone_CAAnimation.h"

#define XMLVM_CURRENT_CLASS_NAME CAAnimation
#define XMLVM_CURRENT_PKG_CLASS_NAME org_xmlvm_iphone_CAAnimation

__TIB_DEFINITION_org_xmlvm_iphone_CAAnimation __TIB_org_xmlvm_iphone_CAAnimation = {
    0, // classInitializationBegan
    0, // classInitialized
    __INIT_org_xmlvm_iphone_CAAnimation, // classInitializer
    "org.xmlvm.iphone.CAAnimation", // className
    (__TIB_DEFINITION_TEMPLATE*) &__TIB_org_xmlvm_iphone_NSObject, // extends
    sizeof(org_xmlvm_iphone_CAAnimation), // sizeInstance
    XMLVM_TYPE_CLASS};

JAVA_OBJECT __CLASS_org_xmlvm_iphone_CAAnimation;
JAVA_OBJECT __CLASS_org_xmlvm_iphone_CAAnimation_3ARRAY;
JAVA_OBJECT __CLASS_org_xmlvm_iphone_CAAnimation_2ARRAY;
JAVA_OBJECT __CLASS_org_xmlvm_iphone_CAAnimation_1ARRAY;

//XMLVM_BEGIN_IMPLEMENTATION
//XMLVM_END_IMPLEMENTATION


#include "xmlvm-reflection.h"

static XMLVM_FIELD_REFLECTION_DATA __field_reflection_data[] = {
};

static JAVA_OBJECT* __constructor0_arg_types[] = {
};

static XMLVM_CONSTRUCTOR_REFLECTION_DATA __constructor_reflection_data[] = {
    {&__constructor0_arg_types[0],
    sizeof(__constructor0_arg_types) / sizeof(JAVA_OBJECT*),
    JAVA_NULL,
    0,
    0,
    "",
    JAVA_NULL,
    JAVA_NULL},
};

static JAVA_OBJECT constructor_dispatcher(JAVA_OBJECT constructor, JAVA_OBJECT arguments)
{
    JAVA_OBJECT obj = __NEW_org_xmlvm_iphone_CAAnimation();
    java_lang_reflect_Constructor* c = (java_lang_reflect_Constructor*) constructor;
    org_xmlvm_runtime_XMLVMArray* args = (org_xmlvm_runtime_XMLVMArray*) arguments;
    JAVA_ARRAY_OBJECT* argsArray = (JAVA_ARRAY_OBJECT*) args->fields.org_xmlvm_runtime_XMLVMArray.array_;
    switch (c->fields.java_lang_reflect_Constructor.slot_) {
    case 0:
        org_xmlvm_iphone_CAAnimation___INIT___(obj);
        break;
    default:
        XMLVM_INTERNAL_ERROR();
        break;
    }
    return obj;
}

static JAVA_OBJECT* __method0_arg_types[] = {
    &__CLASS_java_lang_String,
};

static JAVA_OBJECT* __method1_arg_types[] = {
};

static JAVA_OBJECT* __method2_arg_types[] = {
    &__CLASS_org_xmlvm_iphone_NSObject,
};

static JAVA_OBJECT* __method3_arg_types[] = {
};

static JAVA_OBJECT* __method4_arg_types[] = {
    &__CLASS_boolean,
};

static XMLVM_METHOD_REFLECTION_DATA __method_reflection_data[] = {
    {"defaultValueForKey",
    &__method0_arg_types[0],
    sizeof(__method0_arg_types) / sizeof(JAVA_OBJECT*),
    JAVA_NULL,
    0,
    0,
    "",
    JAVA_NULL,
    JAVA_NULL},
    {"getDelegate",
    &__method1_arg_types[0],
    sizeof(__method1_arg_types) / sizeof(JAVA_OBJECT*),
    JAVA_NULL,
    0,
    0,
    "",
    JAVA_NULL,
    JAVA_NULL},
    {"setDelegate",
    &__method2_arg_types[0],
    sizeof(__method2_arg_types) / sizeof(JAVA_OBJECT*),
    JAVA_NULL,
    0,
    0,
    "",
    JAVA_NULL,
    JAVA_NULL},
    {"isRemovedOnCompletion",
    &__method3_arg_types[0],
    sizeof(__method3_arg_types) / sizeof(JAVA_OBJECT*),
    JAVA_NULL,
    0,
    0,
    "",
    JAVA_NULL,
    JAVA_NULL},
    {"setRemovedOnCompletion",
    &__method4_arg_types[0],
    sizeof(__method4_arg_types) / sizeof(JAVA_OBJECT*),
    JAVA_NULL,
    0,
    0,
    "",
    JAVA_NULL,
    JAVA_NULL},
};

static JAVA_OBJECT method_dispatcher(JAVA_OBJECT method, JAVA_OBJECT receiver, JAVA_OBJECT arguments)
{
    JAVA_OBJECT result = JAVA_NULL; //TODO need to set result
    java_lang_Object* obj = receiver;
    java_lang_reflect_Method* m = (java_lang_reflect_Method*) method;
    org_xmlvm_runtime_XMLVMArray* args = (org_xmlvm_runtime_XMLVMArray*) arguments;
    JAVA_ARRAY_OBJECT* argsArray = (JAVA_ARRAY_OBJECT*) args->fields.org_xmlvm_runtime_XMLVMArray.array_;
    switch (m->fields.java_lang_reflect_Method.slot_) {
    case 0:
        org_xmlvm_iphone_CAAnimation_defaultValueForKey___java_lang_String(argsArray[0]);
        break;
    case 1:
        org_xmlvm_iphone_CAAnimation_getDelegate__(receiver);
        break;
    case 2:
        org_xmlvm_iphone_CAAnimation_setDelegate___org_xmlvm_iphone_NSObject(receiver, argsArray[0]);
        break;
    case 3:
        org_xmlvm_iphone_CAAnimation_isRemovedOnCompletion__(receiver);
        break;
    case 4:
        org_xmlvm_iphone_CAAnimation_setRemovedOnCompletion___boolean(receiver, ((java_lang_Boolean*) argsArray[0])->fields.java_lang_Boolean.value_);
        break;
    default:
        XMLVM_INTERNAL_ERROR();
        break;
    }
    return result;
}

void __INIT_org_xmlvm_iphone_CAAnimation()
{
    staticInitializerRecursiveLock(&__TIB_org_xmlvm_iphone_CAAnimation);
    if (!__TIB_org_xmlvm_iphone_CAAnimation.classInitialized) {
        __INIT_IMPL_org_xmlvm_iphone_CAAnimation();
    }
    staticInitializerRecursiveUnlock(&__TIB_org_xmlvm_iphone_CAAnimation);
}

void __INIT_IMPL_org_xmlvm_iphone_CAAnimation()
{
    if (!__TIB_org_xmlvm_iphone_CAAnimation.classInitializationBegan) {
        __TIB_org_xmlvm_iphone_CAAnimation.classInitializationBegan = 1;

        // Initialize base class if necessary
        if (!__TIB_org_xmlvm_iphone_NSObject.classInitialized) __INIT_IMPL_org_xmlvm_iphone_NSObject();
        __TIB_org_xmlvm_iphone_CAAnimation.newInstanceFunc = __NEW_INSTANCE_org_xmlvm_iphone_CAAnimation;
        // Copy vtable from base class
        XMLVM_MEMCPY(__TIB_org_xmlvm_iphone_CAAnimation.vtable, __TIB_org_xmlvm_iphone_NSObject.vtable, sizeof(__TIB_org_xmlvm_iphone_NSObject.vtable));
        // Initialize vtable for this class
        __TIB_org_xmlvm_iphone_CAAnimation.vtable[14] = (VTABLE_PTR) &org_xmlvm_iphone_CAAnimation_getDelegate__;
        __TIB_org_xmlvm_iphone_CAAnimation.vtable[15] = (VTABLE_PTR) &org_xmlvm_iphone_CAAnimation_setDelegate___org_xmlvm_iphone_NSObject;
        __TIB_org_xmlvm_iphone_CAAnimation.vtable[16] = (VTABLE_PTR) &org_xmlvm_iphone_CAAnimation_isRemovedOnCompletion__;
        __TIB_org_xmlvm_iphone_CAAnimation.vtable[17] = (VTABLE_PTR) &org_xmlvm_iphone_CAAnimation_setRemovedOnCompletion___boolean;
        // Initialize vtable for implementing interfaces
        __TIB_org_xmlvm_iphone_CAAnimation.numImplementedInterfaces = 2;
        __TIB_org_xmlvm_iphone_CAAnimation.implementedInterfaces = (__TIB_DEFINITION_TEMPLATE* (*)[1]) XMLVM_MALLOC(sizeof(__TIB_DEFINITION_TEMPLATE*) * 2);
        __INIT_IMPL_FOR_CLASS_org_xmlvm_iphone_CAMediaTiming(&__TIB_org_xmlvm_iphone_CAAnimation.implementedInterfaces[0][0]);
        __TIB_org_xmlvm_iphone_CAAnimation.implementedInterfaces[0][0]->vtable[11] = __TIB_org_xmlvm_iphone_CAAnimation.vtable[-1];
        __TIB_org_xmlvm_iphone_CAAnimation.implementedInterfaces[0][0]->vtable[12] = __TIB_org_xmlvm_iphone_CAAnimation.vtable[-1];
        __TIB_org_xmlvm_iphone_CAAnimation.implementedInterfaces[0][0]->vtable[13] = __TIB_org_xmlvm_iphone_CAAnimation.vtable[-1];
        __TIB_org_xmlvm_iphone_CAAnimation.implementedInterfaces[0][0]->vtable[14] = __TIB_org_xmlvm_iphone_CAAnimation.vtable[-1];
        __TIB_org_xmlvm_iphone_CAAnimation.implementedInterfaces[0][0]->vtable[15] = __TIB_org_xmlvm_iphone_CAAnimation.vtable[-1];
        __TIB_org_xmlvm_iphone_CAAnimation.implementedInterfaces[0][0]->vtable[16] = __TIB_org_xmlvm_iphone_CAAnimation.vtable[-1];
        __TIB_org_xmlvm_iphone_CAAnimation.implementedInterfaces[0][0]->vtable[17] = __TIB_org_xmlvm_iphone_CAAnimation.vtable[-1];
        __TIB_org_xmlvm_iphone_CAAnimation.implementedInterfaces[0][0]->vtable[18] = __TIB_org_xmlvm_iphone_CAAnimation.vtable[-1];
        __TIB_org_xmlvm_iphone_CAAnimation.implementedInterfaces[0][0]->vtable[19] = __TIB_org_xmlvm_iphone_CAAnimation.vtable[-1];
        __TIB_org_xmlvm_iphone_CAAnimation.implementedInterfaces[0][0]->vtable[20] = __TIB_org_xmlvm_iphone_CAAnimation.vtable[-1];
        __TIB_org_xmlvm_iphone_CAAnimation.implementedInterfaces[0][0]->vtable[21] = __TIB_org_xmlvm_iphone_CAAnimation.vtable[-1];
        __TIB_org_xmlvm_iphone_CAAnimation.implementedInterfaces[0][0]->vtable[22] = __TIB_org_xmlvm_iphone_CAAnimation.vtable[-1];
        __TIB_org_xmlvm_iphone_CAAnimation.implementedInterfaces[0][0]->vtable[23] = __TIB_org_xmlvm_iphone_CAAnimation.vtable[-1];
        __TIB_org_xmlvm_iphone_CAAnimation.implementedInterfaces[0][0]->vtable[24] = __TIB_org_xmlvm_iphone_CAAnimation.vtable[-1];
        __TIB_org_xmlvm_iphone_CAAnimation.implementedInterfaces[0][0]->vtable[25] = __TIB_org_xmlvm_iphone_CAAnimation.vtable[-1];
        __TIB_org_xmlvm_iphone_CAAnimation.implementedInterfaces[0][0]->vtable[26] = __TIB_org_xmlvm_iphone_CAAnimation.vtable[-1];
        __INIT_IMPL_FOR_CLASS_org_xmlvm_iphone_CAAction(&__TIB_org_xmlvm_iphone_CAAnimation.implementedInterfaces[0][1]);
        __TIB_org_xmlvm_iphone_CAAnimation.implementedInterfaces[0][1]->vtable[11] = __TIB_org_xmlvm_iphone_CAAnimation.vtable[-1];

        __TIB_org_xmlvm_iphone_CAAnimation.declaredFields = &__field_reflection_data[0];
        __TIB_org_xmlvm_iphone_CAAnimation.numDeclaredFields = sizeof(__field_reflection_data) / sizeof(XMLVM_FIELD_REFLECTION_DATA);
        __TIB_org_xmlvm_iphone_CAAnimation.constructorDispatcherFunc = constructor_dispatcher;
        __TIB_org_xmlvm_iphone_CAAnimation.declaredConstructors = &__constructor_reflection_data[0];
        __TIB_org_xmlvm_iphone_CAAnimation.numDeclaredConstructors = sizeof(__constructor_reflection_data) / sizeof(XMLVM_CONSTRUCTOR_REFLECTION_DATA);
        __TIB_org_xmlvm_iphone_CAAnimation.methodDispatcherFunc = method_dispatcher;
        __TIB_org_xmlvm_iphone_CAAnimation.declaredMethods = &__method_reflection_data[0];
        __TIB_org_xmlvm_iphone_CAAnimation.numDeclaredMethods = sizeof(__method_reflection_data) / sizeof(XMLVM_METHOD_REFLECTION_DATA);
        __CLASS_org_xmlvm_iphone_CAAnimation = XMLVM_CREATE_CLASS_OBJECT(&__TIB_org_xmlvm_iphone_CAAnimation);
        __TIB_org_xmlvm_iphone_CAAnimation.clazz = __CLASS_org_xmlvm_iphone_CAAnimation;
        __CLASS_org_xmlvm_iphone_CAAnimation_3ARRAY = XMLVM_CREATE_ARRAY_CLASS_OBJECT(__CLASS_org_xmlvm_iphone_CAAnimation, 3);
        __CLASS_org_xmlvm_iphone_CAAnimation_2ARRAY = XMLVM_CREATE_ARRAY_CLASS_OBJECT(__CLASS_org_xmlvm_iphone_CAAnimation, 2);
        __CLASS_org_xmlvm_iphone_CAAnimation_1ARRAY = XMLVM_CREATE_ARRAY_CLASS_OBJECT(__CLASS_org_xmlvm_iphone_CAAnimation, 1);

        //XMLVM_BEGIN_WRAPPER[__INIT_org_xmlvm_iphone_CAAnimation]
        //XMLVM_END_WRAPPER

        __TIB_org_xmlvm_iphone_CAAnimation.classInitialized = 1;
    }
}

void __DELETE_org_xmlvm_iphone_CAAnimation(void* me, void* client_data)
{
    //XMLVM_BEGIN_WRAPPER[__DELETE_org_xmlvm_iphone_CAAnimation]
    //XMLVM_END_WRAPPER
}

void __INIT_INSTANCE_MEMBERS_org_xmlvm_iphone_CAAnimation(JAVA_OBJECT me)
{
    __INIT_INSTANCE_MEMBERS_org_xmlvm_iphone_NSObject(me);
}

JAVA_OBJECT __NEW_org_xmlvm_iphone_CAAnimation()
{
    if (!__TIB_org_xmlvm_iphone_CAAnimation.classInitialized) __INIT_org_xmlvm_iphone_CAAnimation();
    org_xmlvm_iphone_CAAnimation* me = (org_xmlvm_iphone_CAAnimation*) XMLVM_MALLOC(sizeof(org_xmlvm_iphone_CAAnimation));
    me->tib = &__TIB_org_xmlvm_iphone_CAAnimation;
    __INIT_INSTANCE_MEMBERS_org_xmlvm_iphone_CAAnimation(me);
    //XMLVM_BEGIN_WRAPPER[__NEW_org_xmlvm_iphone_CAAnimation]
    //XMLVM_END_WRAPPER
    return me;
}

JAVA_OBJECT __NEW_INSTANCE_org_xmlvm_iphone_CAAnimation()
{
    JAVA_OBJECT me = JAVA_NULL;
    me = __NEW_org_xmlvm_iphone_CAAnimation();
    org_xmlvm_iphone_CAAnimation___INIT___(me);
    return me;
}

void org_xmlvm_iphone_CAAnimation___INIT___(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_CAAnimation___INIT___]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

JAVA_OBJECT org_xmlvm_iphone_CAAnimation_defaultValueForKey___java_lang_String(JAVA_OBJECT n1)
{
    if (!__TIB_org_xmlvm_iphone_CAAnimation.classInitialized) __INIT_org_xmlvm_iphone_CAAnimation();
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_CAAnimation_defaultValueForKey___java_lang_String]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

JAVA_OBJECT org_xmlvm_iphone_CAAnimation_getDelegate__(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_CAAnimation_getDelegate__]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

void org_xmlvm_iphone_CAAnimation_setDelegate___org_xmlvm_iphone_NSObject(JAVA_OBJECT me, JAVA_OBJECT n1)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_CAAnimation_setDelegate___org_xmlvm_iphone_NSObject]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

JAVA_BOOLEAN org_xmlvm_iphone_CAAnimation_isRemovedOnCompletion__(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_CAAnimation_isRemovedOnCompletion__]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

void org_xmlvm_iphone_CAAnimation_setRemovedOnCompletion___boolean(JAVA_OBJECT me, JAVA_BOOLEAN n1)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_CAAnimation_setRemovedOnCompletion___boolean]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

