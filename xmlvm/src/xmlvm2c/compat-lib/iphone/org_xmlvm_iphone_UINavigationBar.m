#include "xmlvm.h"
#include "org_xmlvm_iphone_UINavigationItem.h"
#include "java_util_ArrayList.h"
#include "org_xmlvm_iphone_UINavigationController.h"
#include "org_xmlvm_iphone_CGRect.h"
#include "org_xmlvm_iphone_UINavigationBarDelegate.h"
#include "org_xmlvm_iphone_UIColor.h"

#include "org_xmlvm_iphone_UINavigationBar.h"

#define XMLVM_CURRENT_CLASS_NAME UINavigationBar
#define XMLVM_CURRENT_PKG_CLASS_NAME org_xmlvm_iphone_UINavigationBar

__TIB_DEFINITION_org_xmlvm_iphone_UINavigationBar __TIB_org_xmlvm_iphone_UINavigationBar = {
    0, // classInitializationBegan
    0, // classInitialized
    __INIT_org_xmlvm_iphone_UINavigationBar, // classInitializer
    "org.xmlvm.iphone.UINavigationBar", // className
    (__TIB_DEFINITION_TEMPLATE*) &__TIB_org_xmlvm_iphone_UIView, // extends
    sizeof(org_xmlvm_iphone_UINavigationBar), // sizeInstance
    XMLVM_TYPE_CLASS};

JAVA_OBJECT __CLASS_org_xmlvm_iphone_UINavigationBar;
JAVA_OBJECT __CLASS_org_xmlvm_iphone_UINavigationBar_3ARRAY;
JAVA_OBJECT __CLASS_org_xmlvm_iphone_UINavigationBar_2ARRAY;
JAVA_OBJECT __CLASS_org_xmlvm_iphone_UINavigationBar_1ARRAY;

//XMLVM_BEGIN_IMPLEMENTATION
//XMLVM_END_IMPLEMENTATION


#include "xmlvm-reflection.h"

static XMLVM_FIELD_REFLECTION_DATA __field_reflection_data[] = {
    {"nbcontroller",
    &__CLASS_org_xmlvm_iphone_UINavigationController,
    0,
    XMLVM_OFFSETOF(org_xmlvm_iphone_UINavigationBar, fields.org_xmlvm_iphone_UINavigationBar.nbcontroller_),
    0,
    "",
    JAVA_NULL},
};

static JAVA_OBJECT* __constructor0_arg_types[] = {
};

static JAVA_OBJECT* __constructor1_arg_types[] = {
    &__CLASS_org_xmlvm_iphone_CGRect,
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
    {&__constructor1_arg_types[0],
    sizeof(__constructor1_arg_types) / sizeof(JAVA_OBJECT*),
    JAVA_NULL,
    0,
    0,
    "",
    JAVA_NULL,
    JAVA_NULL},
};

static JAVA_OBJECT constructor_dispatcher(JAVA_OBJECT constructor, JAVA_OBJECT arguments)
{
    JAVA_OBJECT obj = __NEW_org_xmlvm_iphone_UINavigationBar();
    java_lang_reflect_Constructor* c = (java_lang_reflect_Constructor*) constructor;
    org_xmlvm_runtime_XMLVMArray* args = (org_xmlvm_runtime_XMLVMArray*) arguments;
    JAVA_ARRAY_OBJECT* argsArray = (JAVA_ARRAY_OBJECT*) args->fields.org_xmlvm_runtime_XMLVMArray.array_;
    switch (c->fields.java_lang_reflect_Constructor.slot_) {
    case 0:
        org_xmlvm_iphone_UINavigationBar___INIT___(obj);
        break;
    case 1:
        org_xmlvm_iphone_UINavigationBar___INIT____org_xmlvm_iphone_CGRect(obj, argsArray[0]);
        break;
    default:
        XMLVM_INTERNAL_ERROR();
        break;
    }
    return obj;
}

static JAVA_OBJECT* __method0_arg_types[] = {
    &__CLASS_org_xmlvm_iphone_UINavigationItem,
    &__CLASS_boolean,
};

static JAVA_OBJECT* __method1_arg_types[] = {
    &__CLASS_boolean,
};

static JAVA_OBJECT* __method2_arg_types[] = {
    &__CLASS_java_util_ArrayList,
};

static JAVA_OBJECT* __method3_arg_types[] = {
    &__CLASS_java_util_ArrayList,
    &__CLASS_boolean,
};

static JAVA_OBJECT* __method4_arg_types[] = {
};

static JAVA_OBJECT* __method5_arg_types[] = {
    &__CLASS_org_xmlvm_iphone_UINavigationBarDelegate,
};

static JAVA_OBJECT* __method6_arg_types[] = {
};

static JAVA_OBJECT* __method7_arg_types[] = {
};

static JAVA_OBJECT* __method8_arg_types[] = {
};

static JAVA_OBJECT* __method9_arg_types[] = {
};

static JAVA_OBJECT* __method10_arg_types[] = {
    &__CLASS_int,
};

static JAVA_OBJECT* __method11_arg_types[] = {
};

static JAVA_OBJECT* __method12_arg_types[] = {
    &__CLASS_org_xmlvm_iphone_UIColor,
};

static JAVA_OBJECT* __method13_arg_types[] = {
};

static JAVA_OBJECT* __method14_arg_types[] = {
    &__CLASS_boolean,
};

static JAVA_OBJECT* __method15_arg_types[] = {
};

static XMLVM_METHOD_REFLECTION_DATA __method_reflection_data[] = {
    {"pushNavigationItem",
    &__method0_arg_types[0],
    sizeof(__method0_arg_types) / sizeof(JAVA_OBJECT*),
    JAVA_NULL,
    0,
    0,
    "",
    JAVA_NULL,
    JAVA_NULL},
    {"popNavigationItemAnimated",
    &__method1_arg_types[0],
    sizeof(__method1_arg_types) / sizeof(JAVA_OBJECT*),
    JAVA_NULL,
    0,
    0,
    "",
    JAVA_NULL,
    JAVA_NULL},
    {"setItems",
    &__method2_arg_types[0],
    sizeof(__method2_arg_types) / sizeof(JAVA_OBJECT*),
    JAVA_NULL,
    0,
    0,
    "",
    JAVA_NULL,
    JAVA_NULL},
    {"setItems",
    &__method3_arg_types[0],
    sizeof(__method3_arg_types) / sizeof(JAVA_OBJECT*),
    JAVA_NULL,
    0,
    0,
    "",
    JAVA_NULL,
    JAVA_NULL},
    {"getDelegate",
    &__method4_arg_types[0],
    sizeof(__method4_arg_types) / sizeof(JAVA_OBJECT*),
    JAVA_NULL,
    0,
    0,
    "",
    JAVA_NULL,
    JAVA_NULL},
    {"setDelegate",
    &__method5_arg_types[0],
    sizeof(__method5_arg_types) / sizeof(JAVA_OBJECT*),
    JAVA_NULL,
    0,
    0,
    "",
    JAVA_NULL,
    JAVA_NULL},
    {"getTopItem",
    &__method6_arg_types[0],
    sizeof(__method6_arg_types) / sizeof(JAVA_OBJECT*),
    JAVA_NULL,
    0,
    0,
    "",
    JAVA_NULL,
    JAVA_NULL},
    {"getBackitem",
    &__method7_arg_types[0],
    sizeof(__method7_arg_types) / sizeof(JAVA_OBJECT*),
    JAVA_NULL,
    0,
    0,
    "",
    JAVA_NULL,
    JAVA_NULL},
    {"getItems",
    &__method8_arg_types[0],
    sizeof(__method8_arg_types) / sizeof(JAVA_OBJECT*),
    JAVA_NULL,
    0,
    0,
    "",
    JAVA_NULL,
    JAVA_NULL},
    {"getBarStyle",
    &__method9_arg_types[0],
    sizeof(__method9_arg_types) / sizeof(JAVA_OBJECT*),
    JAVA_NULL,
    0,
    0,
    "",
    JAVA_NULL,
    JAVA_NULL},
    {"setBarStyle",
    &__method10_arg_types[0],
    sizeof(__method10_arg_types) / sizeof(JAVA_OBJECT*),
    JAVA_NULL,
    0,
    0,
    "",
    JAVA_NULL,
    JAVA_NULL},
    {"getTintColor",
    &__method11_arg_types[0],
    sizeof(__method11_arg_types) / sizeof(JAVA_OBJECT*),
    JAVA_NULL,
    0,
    0,
    "",
    JAVA_NULL,
    JAVA_NULL},
    {"setTintColor",
    &__method12_arg_types[0],
    sizeof(__method12_arg_types) / sizeof(JAVA_OBJECT*),
    JAVA_NULL,
    0,
    0,
    "",
    JAVA_NULL,
    JAVA_NULL},
    {"isTranslucent",
    &__method13_arg_types[0],
    sizeof(__method13_arg_types) / sizeof(JAVA_OBJECT*),
    JAVA_NULL,
    0,
    0,
    "",
    JAVA_NULL,
    JAVA_NULL},
    {"setTranslucent",
    &__method14_arg_types[0],
    sizeof(__method14_arg_types) / sizeof(JAVA_OBJECT*),
    JAVA_NULL,
    0,
    0,
    "",
    JAVA_NULL,
    JAVA_NULL},
    {"updateViews",
    &__method15_arg_types[0],
    sizeof(__method15_arg_types) / sizeof(JAVA_OBJECT*),
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
        org_xmlvm_iphone_UINavigationBar_pushNavigationItem___org_xmlvm_iphone_UINavigationItem_boolean(receiver, argsArray[0], ((java_lang_Boolean*) argsArray[1])->fields.java_lang_Boolean.value_);
        break;
    case 1:
        org_xmlvm_iphone_UINavigationBar_popNavigationItemAnimated___boolean(receiver, ((java_lang_Boolean*) argsArray[0])->fields.java_lang_Boolean.value_);
        break;
    case 2:
        org_xmlvm_iphone_UINavigationBar_setItems___java_util_ArrayList(receiver, argsArray[0]);
        break;
    case 3:
        org_xmlvm_iphone_UINavigationBar_setItems___java_util_ArrayList_boolean(receiver, argsArray[0], ((java_lang_Boolean*) argsArray[1])->fields.java_lang_Boolean.value_);
        break;
    case 4:
        org_xmlvm_iphone_UINavigationBar_getDelegate__(receiver);
        break;
    case 5:
        org_xmlvm_iphone_UINavigationBar_setDelegate___org_xmlvm_iphone_UINavigationBarDelegate(receiver, argsArray[0]);
        break;
    case 6:
        org_xmlvm_iphone_UINavigationBar_getTopItem__(receiver);
        break;
    case 7:
        org_xmlvm_iphone_UINavigationBar_getBackitem__(receiver);
        break;
    case 8:
        org_xmlvm_iphone_UINavigationBar_getItems__(receiver);
        break;
    case 9:
        org_xmlvm_iphone_UINavigationBar_getBarStyle__(receiver);
        break;
    case 10:
        org_xmlvm_iphone_UINavigationBar_setBarStyle___int(receiver, ((java_lang_Integer*) argsArray[0])->fields.java_lang_Integer.value_);
        break;
    case 11:
        org_xmlvm_iphone_UINavigationBar_getTintColor__(receiver);
        break;
    case 12:
        org_xmlvm_iphone_UINavigationBar_setTintColor___org_xmlvm_iphone_UIColor(receiver, argsArray[0]);
        break;
    case 13:
        org_xmlvm_iphone_UINavigationBar_isTranslucent__(receiver);
        break;
    case 14:
        org_xmlvm_iphone_UINavigationBar_setTranslucent___boolean(receiver, ((java_lang_Boolean*) argsArray[0])->fields.java_lang_Boolean.value_);
        break;
    case 15:
        org_xmlvm_iphone_UINavigationBar_updateViews__(receiver);
        break;
    default:
        XMLVM_INTERNAL_ERROR();
        break;
    }
    return result;
}

void __INIT_org_xmlvm_iphone_UINavigationBar()
{
    staticInitializerRecursiveLock(&__TIB_org_xmlvm_iphone_UINavigationBar);
    if (!__TIB_org_xmlvm_iphone_UINavigationBar.classInitialized) {
        __INIT_IMPL_org_xmlvm_iphone_UINavigationBar();
    }
    staticInitializerRecursiveUnlock(&__TIB_org_xmlvm_iphone_UINavigationBar);
}

void __INIT_IMPL_org_xmlvm_iphone_UINavigationBar()
{
    if (!__TIB_org_xmlvm_iphone_UINavigationBar.classInitializationBegan) {
        __TIB_org_xmlvm_iphone_UINavigationBar.classInitializationBegan = 1;

        // Initialize base class if necessary
        if (!__TIB_org_xmlvm_iphone_UIView.classInitialized) __INIT_IMPL_org_xmlvm_iphone_UIView();
        __TIB_org_xmlvm_iphone_UINavigationBar.newInstanceFunc = __NEW_INSTANCE_org_xmlvm_iphone_UINavigationBar;
        // Copy vtable from base class
        XMLVM_MEMCPY(__TIB_org_xmlvm_iphone_UINavigationBar.vtable, __TIB_org_xmlvm_iphone_UIView.vtable, sizeof(__TIB_org_xmlvm_iphone_UIView.vtable));
        // Initialize vtable for this class
        __TIB_org_xmlvm_iphone_UINavigationBar.vtable[70] = (VTABLE_PTR) &org_xmlvm_iphone_UINavigationBar_pushNavigationItem___org_xmlvm_iphone_UINavigationItem_boolean;
        __TIB_org_xmlvm_iphone_UINavigationBar.vtable[71] = (VTABLE_PTR) &org_xmlvm_iphone_UINavigationBar_popNavigationItemAnimated___boolean;
        __TIB_org_xmlvm_iphone_UINavigationBar.vtable[72] = (VTABLE_PTR) &org_xmlvm_iphone_UINavigationBar_setItems___java_util_ArrayList;
        __TIB_org_xmlvm_iphone_UINavigationBar.vtable[73] = (VTABLE_PTR) &org_xmlvm_iphone_UINavigationBar_setItems___java_util_ArrayList_boolean;
        __TIB_org_xmlvm_iphone_UINavigationBar.vtable[74] = (VTABLE_PTR) &org_xmlvm_iphone_UINavigationBar_getDelegate__;
        __TIB_org_xmlvm_iphone_UINavigationBar.vtable[75] = (VTABLE_PTR) &org_xmlvm_iphone_UINavigationBar_setDelegate___org_xmlvm_iphone_UINavigationBarDelegate;
        __TIB_org_xmlvm_iphone_UINavigationBar.vtable[76] = (VTABLE_PTR) &org_xmlvm_iphone_UINavigationBar_getTopItem__;
        __TIB_org_xmlvm_iphone_UINavigationBar.vtable[77] = (VTABLE_PTR) &org_xmlvm_iphone_UINavigationBar_getBackitem__;
        __TIB_org_xmlvm_iphone_UINavigationBar.vtable[78] = (VTABLE_PTR) &org_xmlvm_iphone_UINavigationBar_getItems__;
        __TIB_org_xmlvm_iphone_UINavigationBar.vtable[79] = (VTABLE_PTR) &org_xmlvm_iphone_UINavigationBar_getBarStyle__;
        __TIB_org_xmlvm_iphone_UINavigationBar.vtable[80] = (VTABLE_PTR) &org_xmlvm_iphone_UINavigationBar_setBarStyle___int;
        __TIB_org_xmlvm_iphone_UINavigationBar.vtable[81] = (VTABLE_PTR) &org_xmlvm_iphone_UINavigationBar_getTintColor__;
        __TIB_org_xmlvm_iphone_UINavigationBar.vtable[82] = (VTABLE_PTR) &org_xmlvm_iphone_UINavigationBar_setTintColor___org_xmlvm_iphone_UIColor;
        __TIB_org_xmlvm_iphone_UINavigationBar.vtable[83] = (VTABLE_PTR) &org_xmlvm_iphone_UINavigationBar_isTranslucent__;
        __TIB_org_xmlvm_iphone_UINavigationBar.vtable[84] = (VTABLE_PTR) &org_xmlvm_iphone_UINavigationBar_setTranslucent___boolean;
        __TIB_org_xmlvm_iphone_UINavigationBar.vtable[85] = (VTABLE_PTR) &org_xmlvm_iphone_UINavigationBar_updateViews__;
        // Initialize vtable for implementing interfaces
        __TIB_org_xmlvm_iphone_UINavigationBar.numImplementedInterfaces = 0;
        __TIB_org_xmlvm_iphone_UINavigationBar.implementedInterfaces = (__TIB_DEFINITION_TEMPLATE* (*)[1]) XMLVM_MALLOC(sizeof(__TIB_DEFINITION_TEMPLATE*) * 0);

        __TIB_org_xmlvm_iphone_UINavigationBar.declaredFields = &__field_reflection_data[0];
        __TIB_org_xmlvm_iphone_UINavigationBar.numDeclaredFields = sizeof(__field_reflection_data) / sizeof(XMLVM_FIELD_REFLECTION_DATA);
        __TIB_org_xmlvm_iphone_UINavigationBar.constructorDispatcherFunc = constructor_dispatcher;
        __TIB_org_xmlvm_iphone_UINavigationBar.declaredConstructors = &__constructor_reflection_data[0];
        __TIB_org_xmlvm_iphone_UINavigationBar.numDeclaredConstructors = sizeof(__constructor_reflection_data) / sizeof(XMLVM_CONSTRUCTOR_REFLECTION_DATA);
        __TIB_org_xmlvm_iphone_UINavigationBar.methodDispatcherFunc = method_dispatcher;
        __TIB_org_xmlvm_iphone_UINavigationBar.declaredMethods = &__method_reflection_data[0];
        __TIB_org_xmlvm_iphone_UINavigationBar.numDeclaredMethods = sizeof(__method_reflection_data) / sizeof(XMLVM_METHOD_REFLECTION_DATA);
        __CLASS_org_xmlvm_iphone_UINavigationBar = XMLVM_CREATE_CLASS_OBJECT(&__TIB_org_xmlvm_iphone_UINavigationBar);
        __TIB_org_xmlvm_iphone_UINavigationBar.clazz = __CLASS_org_xmlvm_iphone_UINavigationBar;
        __CLASS_org_xmlvm_iphone_UINavigationBar_3ARRAY = XMLVM_CREATE_ARRAY_CLASS_OBJECT(__CLASS_org_xmlvm_iphone_UINavigationBar, 3);
        __CLASS_org_xmlvm_iphone_UINavigationBar_2ARRAY = XMLVM_CREATE_ARRAY_CLASS_OBJECT(__CLASS_org_xmlvm_iphone_UINavigationBar, 2);
        __CLASS_org_xmlvm_iphone_UINavigationBar_1ARRAY = XMLVM_CREATE_ARRAY_CLASS_OBJECT(__CLASS_org_xmlvm_iphone_UINavigationBar, 1);

        //XMLVM_BEGIN_WRAPPER[__INIT_org_xmlvm_iphone_UINavigationBar]
        //XMLVM_END_WRAPPER

        __TIB_org_xmlvm_iphone_UINavigationBar.classInitialized = 1;
    }
}

void __DELETE_org_xmlvm_iphone_UINavigationBar(void* me, void* client_data)
{
    //XMLVM_BEGIN_WRAPPER[__DELETE_org_xmlvm_iphone_UINavigationBar]
    //XMLVM_END_WRAPPER
}

void __INIT_INSTANCE_MEMBERS_org_xmlvm_iphone_UINavigationBar(JAVA_OBJECT me)
{
    __INIT_INSTANCE_MEMBERS_org_xmlvm_iphone_UIView(me);
    ((org_xmlvm_iphone_UINavigationBar*) me)->fields.org_xmlvm_iphone_UINavigationBar.nbcontroller_ = (org_xmlvm_iphone_UINavigationController*) JAVA_NULL;
}

JAVA_OBJECT __NEW_org_xmlvm_iphone_UINavigationBar()
{
    if (!__TIB_org_xmlvm_iphone_UINavigationBar.classInitialized) __INIT_org_xmlvm_iphone_UINavigationBar();
    org_xmlvm_iphone_UINavigationBar* me = (org_xmlvm_iphone_UINavigationBar*) XMLVM_MALLOC(sizeof(org_xmlvm_iphone_UINavigationBar));
    me->tib = &__TIB_org_xmlvm_iphone_UINavigationBar;
    __INIT_INSTANCE_MEMBERS_org_xmlvm_iphone_UINavigationBar(me);
    //XMLVM_BEGIN_WRAPPER[__NEW_org_xmlvm_iphone_UINavigationBar]
    //XMLVM_END_WRAPPER
    return me;
}

JAVA_OBJECT __NEW_INSTANCE_org_xmlvm_iphone_UINavigationBar()
{
    JAVA_OBJECT me = JAVA_NULL;
    me = __NEW_org_xmlvm_iphone_UINavigationBar();
    org_xmlvm_iphone_UINavigationBar___INIT___(me);
    return me;
}

void org_xmlvm_iphone_UINavigationBar___INIT___(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_UINavigationBar___INIT___]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

void org_xmlvm_iphone_UINavigationBar___INIT____org_xmlvm_iphone_CGRect(JAVA_OBJECT me, JAVA_OBJECT n1)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_UINavigationBar___INIT____org_xmlvm_iphone_CGRect]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

void org_xmlvm_iphone_UINavigationBar_pushNavigationItem___org_xmlvm_iphone_UINavigationItem_boolean(JAVA_OBJECT me, JAVA_OBJECT n1, JAVA_BOOLEAN n2)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_UINavigationBar_pushNavigationItem___org_xmlvm_iphone_UINavigationItem_boolean]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

JAVA_OBJECT org_xmlvm_iphone_UINavigationBar_popNavigationItemAnimated___boolean(JAVA_OBJECT me, JAVA_BOOLEAN n1)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_UINavigationBar_popNavigationItemAnimated___boolean]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

void org_xmlvm_iphone_UINavigationBar_setItems___java_util_ArrayList(JAVA_OBJECT me, JAVA_OBJECT n1)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_UINavigationBar_setItems___java_util_ArrayList]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

void org_xmlvm_iphone_UINavigationBar_setItems___java_util_ArrayList_boolean(JAVA_OBJECT me, JAVA_OBJECT n1, JAVA_BOOLEAN n2)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_UINavigationBar_setItems___java_util_ArrayList_boolean]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

JAVA_OBJECT org_xmlvm_iphone_UINavigationBar_getDelegate__(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_UINavigationBar_getDelegate__]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

void org_xmlvm_iphone_UINavigationBar_setDelegate___org_xmlvm_iphone_UINavigationBarDelegate(JAVA_OBJECT me, JAVA_OBJECT n1)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_UINavigationBar_setDelegate___org_xmlvm_iphone_UINavigationBarDelegate]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

JAVA_OBJECT org_xmlvm_iphone_UINavigationBar_getTopItem__(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_UINavigationBar_getTopItem__]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

JAVA_OBJECT org_xmlvm_iphone_UINavigationBar_getBackitem__(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_UINavigationBar_getBackitem__]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

JAVA_OBJECT org_xmlvm_iphone_UINavigationBar_getItems__(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_UINavigationBar_getItems__]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

JAVA_INT org_xmlvm_iphone_UINavigationBar_getBarStyle__(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_UINavigationBar_getBarStyle__]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

void org_xmlvm_iphone_UINavigationBar_setBarStyle___int(JAVA_OBJECT me, JAVA_INT n1)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_UINavigationBar_setBarStyle___int]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

JAVA_OBJECT org_xmlvm_iphone_UINavigationBar_getTintColor__(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_UINavigationBar_getTintColor__]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

void org_xmlvm_iphone_UINavigationBar_setTintColor___org_xmlvm_iphone_UIColor(JAVA_OBJECT me, JAVA_OBJECT n1)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_UINavigationBar_setTintColor___org_xmlvm_iphone_UIColor]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

JAVA_BOOLEAN org_xmlvm_iphone_UINavigationBar_isTranslucent__(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_UINavigationBar_isTranslucent__]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

void org_xmlvm_iphone_UINavigationBar_setTranslucent___boolean(JAVA_OBJECT me, JAVA_BOOLEAN n1)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_UINavigationBar_setTranslucent___boolean]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

void org_xmlvm_iphone_UINavigationBar_updateViews__(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_UINavigationBar_updateViews__]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

