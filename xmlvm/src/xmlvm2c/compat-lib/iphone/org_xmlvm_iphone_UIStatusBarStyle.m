#include "xmlvm.h"

#include "org_xmlvm_iphone_UIStatusBarStyle.h"

#define XMLVM_CURRENT_CLASS_NAME UIStatusBarStyle
#define XMLVM_CURRENT_PKG_CLASS_NAME org_xmlvm_iphone_UIStatusBarStyle

__TIB_DEFINITION_org_xmlvm_iphone_UIStatusBarStyle __TIB_org_xmlvm_iphone_UIStatusBarStyle = {
    0, // classInitializationBegan
    0, // classInitialized
    -1, // initializerThreadId
    __INIT_org_xmlvm_iphone_UIStatusBarStyle, // classInitializer
    "org.xmlvm.iphone.UIStatusBarStyle", // className
    "org.xmlvm.iphone", // package
    JAVA_NULL, // enclosingClassName
    JAVA_NULL, // enclosingMethodName
    JAVA_NULL, // signature
    (__TIB_DEFINITION_TEMPLATE*) &__TIB_java_lang_Object, // extends
    sizeof(org_xmlvm_iphone_UIStatusBarStyle), // sizeInstance
    XMLVM_TYPE_CLASS};

JAVA_OBJECT __CLASS_org_xmlvm_iphone_UIStatusBarStyle;
JAVA_OBJECT __CLASS_org_xmlvm_iphone_UIStatusBarStyle_1ARRAY;
JAVA_OBJECT __CLASS_org_xmlvm_iphone_UIStatusBarStyle_2ARRAY;
JAVA_OBJECT __CLASS_org_xmlvm_iphone_UIStatusBarStyle_3ARRAY;
//XMLVM_BEGIN_IMPLEMENTATION
//XMLVM_END_IMPLEMENTATION

static JAVA_INT _STATIC_org_xmlvm_iphone_UIStatusBarStyle_Default;
static JAVA_INT _STATIC_org_xmlvm_iphone_UIStatusBarStyle_BlackTranslucent;
static JAVA_INT _STATIC_org_xmlvm_iphone_UIStatusBarStyle_BlackOpaque;

#include "xmlvm-reflection.h"

static XMLVM_FIELD_REFLECTION_DATA __field_reflection_data[] = {
    {"Default",
    &__CLASS_int,
    0 | java_lang_reflect_Modifier_PUBLIC | java_lang_reflect_Modifier_STATIC,
    0,
    &_STATIC_org_xmlvm_iphone_UIStatusBarStyle_Default,
    "",
    JAVA_NULL},
    {"BlackTranslucent",
    &__CLASS_int,
    0 | java_lang_reflect_Modifier_PUBLIC | java_lang_reflect_Modifier_STATIC,
    0,
    &_STATIC_org_xmlvm_iphone_UIStatusBarStyle_BlackTranslucent,
    "",
    JAVA_NULL},
    {"BlackOpaque",
    &__CLASS_int,
    0 | java_lang_reflect_Modifier_PUBLIC | java_lang_reflect_Modifier_STATIC,
    0,
    &_STATIC_org_xmlvm_iphone_UIStatusBarStyle_BlackOpaque,
    "",
    JAVA_NULL},
};

static XMLVM_CONSTRUCTOR_REFLECTION_DATA __constructor_reflection_data[] = {
};

static JAVA_OBJECT constructor_dispatcher(JAVA_OBJECT constructor, JAVA_OBJECT arguments)
{
    JAVA_OBJECT obj = __NEW_org_xmlvm_iphone_UIStatusBarStyle();
    java_lang_reflect_Constructor* c = (java_lang_reflect_Constructor*) constructor;
    org_xmlvm_runtime_XMLVMArray* args = (org_xmlvm_runtime_XMLVMArray*) arguments;
    JAVA_ARRAY_OBJECT* argsArray = (JAVA_ARRAY_OBJECT*) args->fields.org_xmlvm_runtime_XMLVMArray.array_;
    switch (c->fields.java_lang_reflect_Constructor.slot_) {
    default:
        XMLVM_INTERNAL_ERROR();
        break;
    }
    return obj;
}

static XMLVM_METHOD_REFLECTION_DATA __method_reflection_data[] = {
};

static JAVA_OBJECT method_dispatcher(JAVA_OBJECT method, JAVA_OBJECT receiver, JAVA_OBJECT arguments)
{
    JAVA_OBJECT result = JAVA_NULL;
    java_lang_Object* obj = receiver;
    java_lang_reflect_Method* m = (java_lang_reflect_Method*) method;
    org_xmlvm_runtime_XMLVMArray* args = (org_xmlvm_runtime_XMLVMArray*) arguments;
    JAVA_ARRAY_OBJECT* argsArray = (JAVA_ARRAY_OBJECT*) args->fields.org_xmlvm_runtime_XMLVMArray.array_;
    XMLVMElem conversion;
    switch (m->fields.java_lang_reflect_Method.slot_) {
    default:
        XMLVM_INTERNAL_ERROR();
        break;
    }
    return result;
}

void __INIT_org_xmlvm_iphone_UIStatusBarStyle()
{
    staticInitializerLock(&__TIB_org_xmlvm_iphone_UIStatusBarStyle);

    // While the static initializer mutex is locked, locally store the value of
    // whether class initialization began or not
    int initBegan = __TIB_org_xmlvm_iphone_UIStatusBarStyle.classInitializationBegan;

    // Whether or not class initialization had already began, it has begun now
    __TIB_org_xmlvm_iphone_UIStatusBarStyle.classInitializationBegan = 1;

    staticInitializerUnlock(&__TIB_org_xmlvm_iphone_UIStatusBarStyle);

    JAVA_LONG curThreadId = (JAVA_LONG)pthread_self();
    if (initBegan) {
        if (__TIB_org_xmlvm_iphone_UIStatusBarStyle.initializerThreadId != curThreadId) {
            // Busy wait until the other thread finishes initializing this class
            while (!__TIB_org_xmlvm_iphone_UIStatusBarStyle.classInitialized) {
                // do nothing
            }
        }
    } else {
        __TIB_org_xmlvm_iphone_UIStatusBarStyle.initializerThreadId = curThreadId;
        XMLVM_CLASS_USED("org.xmlvm.iphone.UIStatusBarStyle")
        __INIT_IMPL_org_xmlvm_iphone_UIStatusBarStyle();
    }
}

void __INIT_IMPL_org_xmlvm_iphone_UIStatusBarStyle()
{
    // Initialize base class if necessary
    XMLVM_CLASS_INIT(java_lang_Object)
    __TIB_org_xmlvm_iphone_UIStatusBarStyle.newInstanceFunc = __NEW_INSTANCE_org_xmlvm_iphone_UIStatusBarStyle;
    // Copy vtable from base class
    XMLVM_MEMCPY(__TIB_org_xmlvm_iphone_UIStatusBarStyle.vtable, __TIB_java_lang_Object.vtable, sizeof(__TIB_java_lang_Object.vtable));
    // Initialize vtable for this class
    // Initialize interface information
    __TIB_org_xmlvm_iphone_UIStatusBarStyle.numImplementedInterfaces = 0;
    __TIB_org_xmlvm_iphone_UIStatusBarStyle.implementedInterfaces = (__TIB_DEFINITION_TEMPLATE* (*)[1]) XMLVM_MALLOC(sizeof(__TIB_DEFINITION_TEMPLATE*) * 0);

    // Initialize interfaces if necessary and assign tib to implementedInterfaces
    _STATIC_org_xmlvm_iphone_UIStatusBarStyle_Default = 0;
    _STATIC_org_xmlvm_iphone_UIStatusBarStyle_BlackTranslucent = 1;
    _STATIC_org_xmlvm_iphone_UIStatusBarStyle_BlackOpaque = 2;

    __TIB_org_xmlvm_iphone_UIStatusBarStyle.declaredFields = &__field_reflection_data[0];
    __TIB_org_xmlvm_iphone_UIStatusBarStyle.numDeclaredFields = sizeof(__field_reflection_data) / sizeof(XMLVM_FIELD_REFLECTION_DATA);
    __TIB_org_xmlvm_iphone_UIStatusBarStyle.constructorDispatcherFunc = constructor_dispatcher;
    __TIB_org_xmlvm_iphone_UIStatusBarStyle.declaredConstructors = &__constructor_reflection_data[0];
    __TIB_org_xmlvm_iphone_UIStatusBarStyle.numDeclaredConstructors = sizeof(__constructor_reflection_data) / sizeof(XMLVM_CONSTRUCTOR_REFLECTION_DATA);
    __TIB_org_xmlvm_iphone_UIStatusBarStyle.methodDispatcherFunc = method_dispatcher;
    __TIB_org_xmlvm_iphone_UIStatusBarStyle.declaredMethods = &__method_reflection_data[0];
    __TIB_org_xmlvm_iphone_UIStatusBarStyle.numDeclaredMethods = sizeof(__method_reflection_data) / sizeof(XMLVM_METHOD_REFLECTION_DATA);
    __CLASS_org_xmlvm_iphone_UIStatusBarStyle = XMLVM_CREATE_CLASS_OBJECT(&__TIB_org_xmlvm_iphone_UIStatusBarStyle);
    __TIB_org_xmlvm_iphone_UIStatusBarStyle.clazz = __CLASS_org_xmlvm_iphone_UIStatusBarStyle;
    __TIB_org_xmlvm_iphone_UIStatusBarStyle.baseType = JAVA_NULL;
    __CLASS_org_xmlvm_iphone_UIStatusBarStyle_1ARRAY = XMLVM_CREATE_ARRAY_CLASS_OBJECT(__CLASS_org_xmlvm_iphone_UIStatusBarStyle);
    __CLASS_org_xmlvm_iphone_UIStatusBarStyle_2ARRAY = XMLVM_CREATE_ARRAY_CLASS_OBJECT(__CLASS_org_xmlvm_iphone_UIStatusBarStyle_1ARRAY);
    __CLASS_org_xmlvm_iphone_UIStatusBarStyle_3ARRAY = XMLVM_CREATE_ARRAY_CLASS_OBJECT(__CLASS_org_xmlvm_iphone_UIStatusBarStyle_2ARRAY);
    //XMLVM_BEGIN_WRAPPER[__INIT_org_xmlvm_iphone_UIStatusBarStyle]
    //XMLVM_END_WRAPPER

    __TIB_org_xmlvm_iphone_UIStatusBarStyle.classInitialized = 1;
}

void __DELETE_org_xmlvm_iphone_UIStatusBarStyle(void* me, void* client_data)
{
    //XMLVM_BEGIN_WRAPPER[__DELETE_org_xmlvm_iphone_UIStatusBarStyle]
    //XMLVM_END_WRAPPER
}

void __INIT_INSTANCE_MEMBERS_org_xmlvm_iphone_UIStatusBarStyle(JAVA_OBJECT me, int derivedClassWillRegisterFinalizer)
{
    __INIT_INSTANCE_MEMBERS_java_lang_Object(me, 0 || derivedClassWillRegisterFinalizer);
    //XMLVM_BEGIN_WRAPPER[__INIT_INSTANCE_MEMBERS_org_xmlvm_iphone_UIStatusBarStyle]
    //XMLVM_END_WRAPPER
}

JAVA_OBJECT __NEW_org_xmlvm_iphone_UIStatusBarStyle()
{    XMLVM_CLASS_INIT(org_xmlvm_iphone_UIStatusBarStyle)
org_xmlvm_iphone_UIStatusBarStyle* me = (org_xmlvm_iphone_UIStatusBarStyle*) XMLVM_MALLOC(sizeof(org_xmlvm_iphone_UIStatusBarStyle));
    me->tib = &__TIB_org_xmlvm_iphone_UIStatusBarStyle;
    __INIT_INSTANCE_MEMBERS_org_xmlvm_iphone_UIStatusBarStyle(me, 0);
    //XMLVM_BEGIN_WRAPPER[__NEW_org_xmlvm_iphone_UIStatusBarStyle]
    //XMLVM_END_WRAPPER
    return me;
}

JAVA_OBJECT __NEW_INSTANCE_org_xmlvm_iphone_UIStatusBarStyle()
{
    JAVA_OBJECT me = JAVA_NULL;
    return me;
}

JAVA_INT org_xmlvm_iphone_UIStatusBarStyle_GET_Default()
{
    XMLVM_CLASS_INIT(org_xmlvm_iphone_UIStatusBarStyle)
    return _STATIC_org_xmlvm_iphone_UIStatusBarStyle_Default;
}

void org_xmlvm_iphone_UIStatusBarStyle_PUT_Default(JAVA_INT v)
{
    XMLVM_CLASS_INIT(org_xmlvm_iphone_UIStatusBarStyle)
_STATIC_org_xmlvm_iphone_UIStatusBarStyle_Default = v;
}

JAVA_INT org_xmlvm_iphone_UIStatusBarStyle_GET_BlackTranslucent()
{
    XMLVM_CLASS_INIT(org_xmlvm_iphone_UIStatusBarStyle)
    return _STATIC_org_xmlvm_iphone_UIStatusBarStyle_BlackTranslucent;
}

void org_xmlvm_iphone_UIStatusBarStyle_PUT_BlackTranslucent(JAVA_INT v)
{
    XMLVM_CLASS_INIT(org_xmlvm_iphone_UIStatusBarStyle)
_STATIC_org_xmlvm_iphone_UIStatusBarStyle_BlackTranslucent = v;
}

JAVA_INT org_xmlvm_iphone_UIStatusBarStyle_GET_BlackOpaque()
{
    XMLVM_CLASS_INIT(org_xmlvm_iphone_UIStatusBarStyle)
    return _STATIC_org_xmlvm_iphone_UIStatusBarStyle_BlackOpaque;
}

void org_xmlvm_iphone_UIStatusBarStyle_PUT_BlackOpaque(JAVA_INT v)
{
    XMLVM_CLASS_INIT(org_xmlvm_iphone_UIStatusBarStyle)
_STATIC_org_xmlvm_iphone_UIStatusBarStyle_BlackOpaque = v;
}

