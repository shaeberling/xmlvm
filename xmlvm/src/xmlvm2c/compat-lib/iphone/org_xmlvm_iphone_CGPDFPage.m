#include "xmlvm.h"
#include "org_xmlvm_iphone_CGAffineTransform.h"
#include "org_xmlvm_iphone_CGPDFDocument.h"
#include "org_xmlvm_iphone_CGRect.h"

#include "org_xmlvm_iphone_CGPDFPage.h"

#define XMLVM_CURRENT_CLASS_NAME CGPDFPage
#define XMLVM_CURRENT_PKG_CLASS_NAME org_xmlvm_iphone_CGPDFPage

__TIB_DEFINITION_org_xmlvm_iphone_CGPDFPage __TIB_org_xmlvm_iphone_CGPDFPage = {
    0, // classInitializationBegan
    0, // classInitialized
    -1, // initializerThreadId
    __INIT_org_xmlvm_iphone_CGPDFPage, // classInitializer
    "org.xmlvm.iphone.CGPDFPage", // className
    "org.xmlvm.iphone", // package
    JAVA_NULL, // enclosingClassName
    JAVA_NULL, // enclosingMethodName
    JAVA_NULL, // signature
    (__TIB_DEFINITION_TEMPLATE*) &__TIB_org_xmlvm_iphone_CFType, // extends
    sizeof(org_xmlvm_iphone_CGPDFPage), // sizeInstance
    XMLVM_TYPE_CLASS};

JAVA_OBJECT __CLASS_org_xmlvm_iphone_CGPDFPage;
JAVA_OBJECT __CLASS_org_xmlvm_iphone_CGPDFPage_1ARRAY;
JAVA_OBJECT __CLASS_org_xmlvm_iphone_CGPDFPage_2ARRAY;
JAVA_OBJECT __CLASS_org_xmlvm_iphone_CGPDFPage_3ARRAY;
//XMLVM_BEGIN_IMPLEMENTATION

void org_xmlvm_iphone_CGPDFPage_INTERNAL_CONSTRUCTOR(JAVA_OBJECT me, CFTypeRef wrappedCFTypeRef)
{
    org_xmlvm_iphone_CFType_INTERNAL_CONSTRUCTOR(me, wrappedCFTypeRef);
}

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
    JAVA_OBJECT obj = __NEW_org_xmlvm_iphone_CGPDFPage();
    java_lang_reflect_Constructor* c = (java_lang_reflect_Constructor*) constructor;
    org_xmlvm_runtime_XMLVMArray* args = (org_xmlvm_runtime_XMLVMArray*) arguments;
    JAVA_ARRAY_OBJECT* argsArray = (JAVA_ARRAY_OBJECT*) args->fields.org_xmlvm_runtime_XMLVMArray.array_;
    switch (c->fields.java_lang_reflect_Constructor.slot_) {
    case 0:
        org_xmlvm_iphone_CGPDFPage___INIT___(obj);
        break;
    default:
        XMLVM_INTERNAL_ERROR();
        break;
    }
    return obj;
}

static JAVA_OBJECT* __method0_arg_types[] = {
};

static JAVA_OBJECT* __method1_arg_types[] = {
};

static JAVA_OBJECT* __method2_arg_types[] = {
};

static JAVA_OBJECT* __method3_arg_types[] = {
};

static JAVA_OBJECT* __method4_arg_types[] = {
};

static JAVA_OBJECT* __method5_arg_types[] = {
    &__CLASS_int,
};

static JAVA_OBJECT* __method6_arg_types[] = {
};

static JAVA_OBJECT* __method7_arg_types[] = {
    &__CLASS_int,
    &__CLASS_org_xmlvm_iphone_CGRect,
    &__CLASS_int,
    &__CLASS_boolean,
};

static XMLVM_METHOD_REFLECTION_DATA __method_reflection_data[] = {
    {"getTypeID",
    &__method0_arg_types[0],
    sizeof(__method0_arg_types) / sizeof(JAVA_OBJECT*),
    JAVA_NULL,
    0,
    0,
    "",
    JAVA_NULL,
    JAVA_NULL},
    {"retain",
    &__method1_arg_types[0],
    sizeof(__method1_arg_types) / sizeof(JAVA_OBJECT*),
    JAVA_NULL,
    0,
    0,
    "",
    JAVA_NULL,
    JAVA_NULL},
    {"release",
    &__method2_arg_types[0],
    sizeof(__method2_arg_types) / sizeof(JAVA_OBJECT*),
    JAVA_NULL,
    0,
    0,
    "",
    JAVA_NULL,
    JAVA_NULL},
    {"getDocument",
    &__method3_arg_types[0],
    sizeof(__method3_arg_types) / sizeof(JAVA_OBJECT*),
    JAVA_NULL,
    0,
    0,
    "",
    JAVA_NULL,
    JAVA_NULL},
    {"getPageNumber",
    &__method4_arg_types[0],
    sizeof(__method4_arg_types) / sizeof(JAVA_OBJECT*),
    JAVA_NULL,
    0,
    0,
    "",
    JAVA_NULL,
    JAVA_NULL},
    {"getBoxRect",
    &__method5_arg_types[0],
    sizeof(__method5_arg_types) / sizeof(JAVA_OBJECT*),
    JAVA_NULL,
    0,
    0,
    "",
    JAVA_NULL,
    JAVA_NULL},
    {"getRotationAngle",
    &__method6_arg_types[0],
    sizeof(__method6_arg_types) / sizeof(JAVA_OBJECT*),
    JAVA_NULL,
    0,
    0,
    "",
    JAVA_NULL,
    JAVA_NULL},
    {"getDrawingTransform",
    &__method7_arg_types[0],
    sizeof(__method7_arg_types) / sizeof(JAVA_OBJECT*),
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
        org_xmlvm_iphone_CGPDFPage_getTypeID__();
        break;
    case 1:
        org_xmlvm_iphone_CGPDFPage_retain__(receiver);
        break;
    case 2:
        org_xmlvm_iphone_CGPDFPage_release__(receiver);
        break;
    case 3:
        org_xmlvm_iphone_CGPDFPage_getDocument__(receiver);
        break;
    case 4:
        org_xmlvm_iphone_CGPDFPage_getPageNumber__(receiver);
        break;
    case 5:
        org_xmlvm_iphone_CGPDFPage_getBoxRect___int(receiver, ((java_lang_Integer*) argsArray[0])->fields.java_lang_Integer.value_);
        break;
    case 6:
        org_xmlvm_iphone_CGPDFPage_getRotationAngle__(receiver);
        break;
    case 7:
        org_xmlvm_iphone_CGPDFPage_getDrawingTransform___int_org_xmlvm_iphone_CGRect_int_boolean(receiver, ((java_lang_Integer*) argsArray[0])->fields.java_lang_Integer.value_, argsArray[1], ((java_lang_Integer*) argsArray[2])->fields.java_lang_Integer.value_, ((java_lang_Boolean*) argsArray[3])->fields.java_lang_Boolean.value_);
        break;
    default:
        XMLVM_INTERNAL_ERROR();
        break;
    }
    return result;
}

void __INIT_org_xmlvm_iphone_CGPDFPage()
{
    staticInitializerLock(&__TIB_org_xmlvm_iphone_CGPDFPage);

    // While the static initializer mutex is locked, locally store the value of
    // whether class initialization began or not
    int initBegan = __TIB_org_xmlvm_iphone_CGPDFPage.classInitializationBegan;

    // Whether or not class initialization had already began, it has begun now
    __TIB_org_xmlvm_iphone_CGPDFPage.classInitializationBegan = 1;

    staticInitializerUnlock(&__TIB_org_xmlvm_iphone_CGPDFPage);

    JAVA_LONG curThreadId = (JAVA_LONG)pthread_self();
    if (initBegan) {
        if (__TIB_org_xmlvm_iphone_CGPDFPage.initializerThreadId != curThreadId) {
            // Busy wait until the other thread finishes initializing this class
            while (!__TIB_org_xmlvm_iphone_CGPDFPage.classInitialized) {
                // do nothing
            }
        }
    } else {
        __TIB_org_xmlvm_iphone_CGPDFPage.initializerThreadId = curThreadId;
        __INIT_IMPL_org_xmlvm_iphone_CGPDFPage();
    }
}

void __INIT_IMPL_org_xmlvm_iphone_CGPDFPage()
{
    // Initialize base class if necessary
    if (!__TIB_org_xmlvm_iphone_CFType.classInitialized) __INIT_org_xmlvm_iphone_CFType();
    __TIB_org_xmlvm_iphone_CGPDFPage.newInstanceFunc = __NEW_INSTANCE_org_xmlvm_iphone_CGPDFPage;
    // Copy vtable from base class
    XMLVM_MEMCPY(__TIB_org_xmlvm_iphone_CGPDFPage.vtable, __TIB_org_xmlvm_iphone_CFType.vtable, sizeof(__TIB_org_xmlvm_iphone_CFType.vtable));
    // Initialize vtable for this class
    __TIB_org_xmlvm_iphone_CGPDFPage.vtable[7] = (VTABLE_PTR) &org_xmlvm_iphone_CGPDFPage_retain__;
    __TIB_org_xmlvm_iphone_CGPDFPage.vtable[6] = (VTABLE_PTR) &org_xmlvm_iphone_CGPDFPage_release__;
    // Initialize interface information
    __TIB_org_xmlvm_iphone_CGPDFPage.numImplementedInterfaces = 0;
    __TIB_org_xmlvm_iphone_CGPDFPage.implementedInterfaces = (__TIB_DEFINITION_TEMPLATE* (*)[1]) XMLVM_MALLOC(sizeof(__TIB_DEFINITION_TEMPLATE*) * 0);

    // Initialize interfaces if necessary and assign tib to implementedInterfaces

    __TIB_org_xmlvm_iphone_CGPDFPage.declaredFields = &__field_reflection_data[0];
    __TIB_org_xmlvm_iphone_CGPDFPage.numDeclaredFields = sizeof(__field_reflection_data) / sizeof(XMLVM_FIELD_REFLECTION_DATA);
    __TIB_org_xmlvm_iphone_CGPDFPage.constructorDispatcherFunc = constructor_dispatcher;
    __TIB_org_xmlvm_iphone_CGPDFPage.declaredConstructors = &__constructor_reflection_data[0];
    __TIB_org_xmlvm_iphone_CGPDFPage.numDeclaredConstructors = sizeof(__constructor_reflection_data) / sizeof(XMLVM_CONSTRUCTOR_REFLECTION_DATA);
    __TIB_org_xmlvm_iphone_CGPDFPage.methodDispatcherFunc = method_dispatcher;
    __TIB_org_xmlvm_iphone_CGPDFPage.declaredMethods = &__method_reflection_data[0];
    __TIB_org_xmlvm_iphone_CGPDFPage.numDeclaredMethods = sizeof(__method_reflection_data) / sizeof(XMLVM_METHOD_REFLECTION_DATA);
    __CLASS_org_xmlvm_iphone_CGPDFPage = XMLVM_CREATE_CLASS_OBJECT(&__TIB_org_xmlvm_iphone_CGPDFPage);
    __TIB_org_xmlvm_iphone_CGPDFPage.clazz = __CLASS_org_xmlvm_iphone_CGPDFPage;
    __TIB_org_xmlvm_iphone_CGPDFPage.baseType = JAVA_NULL;
    __CLASS_org_xmlvm_iphone_CGPDFPage_1ARRAY = XMLVM_CREATE_ARRAY_CLASS_OBJECT(__CLASS_org_xmlvm_iphone_CGPDFPage);
    __CLASS_org_xmlvm_iphone_CGPDFPage_2ARRAY = XMLVM_CREATE_ARRAY_CLASS_OBJECT(__CLASS_org_xmlvm_iphone_CGPDFPage_1ARRAY);
    __CLASS_org_xmlvm_iphone_CGPDFPage_3ARRAY = XMLVM_CREATE_ARRAY_CLASS_OBJECT(__CLASS_org_xmlvm_iphone_CGPDFPage_2ARRAY);
    //XMLVM_BEGIN_WRAPPER[__INIT_org_xmlvm_iphone_CGPDFPage]
    //XMLVM_END_WRAPPER

    __TIB_org_xmlvm_iphone_CGPDFPage.classInitialized = 1;
}

void __DELETE_org_xmlvm_iphone_CGPDFPage(void* me, void* client_data)
{
    //XMLVM_BEGIN_WRAPPER[__DELETE_org_xmlvm_iphone_CGPDFPage]
    //XMLVM_END_WRAPPER
}

void __INIT_INSTANCE_MEMBERS_org_xmlvm_iphone_CGPDFPage(JAVA_OBJECT me, int derivedClassWillRegisterFinalizer)
{
    __INIT_INSTANCE_MEMBERS_org_xmlvm_iphone_CFType(me, 0 || derivedClassWillRegisterFinalizer);
    //XMLVM_BEGIN_WRAPPER[__INIT_INSTANCE_MEMBERS_org_xmlvm_iphone_CGPDFPage]
    //XMLVM_END_WRAPPER
}

JAVA_OBJECT __NEW_org_xmlvm_iphone_CGPDFPage()
{
    if (!__TIB_org_xmlvm_iphone_CGPDFPage.classInitialized) __INIT_org_xmlvm_iphone_CGPDFPage();
    org_xmlvm_iphone_CGPDFPage* me = (org_xmlvm_iphone_CGPDFPage*) XMLVM_MALLOC(sizeof(org_xmlvm_iphone_CGPDFPage));
    me->tib = &__TIB_org_xmlvm_iphone_CGPDFPage;
    __INIT_INSTANCE_MEMBERS_org_xmlvm_iphone_CGPDFPage(me, 0);
    //XMLVM_BEGIN_WRAPPER[__NEW_org_xmlvm_iphone_CGPDFPage]
    //XMLVM_END_WRAPPER
    return me;
}

JAVA_OBJECT __NEW_INSTANCE_org_xmlvm_iphone_CGPDFPage()
{
    JAVA_OBJECT me = JAVA_NULL;
    me = __NEW_org_xmlvm_iphone_CGPDFPage();
    org_xmlvm_iphone_CGPDFPage___INIT___(me);
    return me;
}

JAVA_LONG org_xmlvm_iphone_CGPDFPage_getTypeID__()
{
    if (!__TIB_org_xmlvm_iphone_CGPDFPage.classInitialized) __INIT_org_xmlvm_iphone_CGPDFPage();
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_CGPDFPage_getTypeID__]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

void org_xmlvm_iphone_CGPDFPage___INIT___(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_CGPDFPage___INIT___]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

JAVA_OBJECT org_xmlvm_iphone_CGPDFPage_retain__(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_CGPDFPage_retain__]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

void org_xmlvm_iphone_CGPDFPage_release__(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_CGPDFPage_release__]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

JAVA_OBJECT org_xmlvm_iphone_CGPDFPage_getDocument__(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_CGPDFPage_getDocument__]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

JAVA_INT org_xmlvm_iphone_CGPDFPage_getPageNumber__(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_CGPDFPage_getPageNumber__]
    XMLVM_VAR_CFTHIZ;
    return CGPDFPageGetPageNumber(thiz);
    //XMLVM_END_WRAPPER
}

JAVA_OBJECT org_xmlvm_iphone_CGPDFPage_getBoxRect___int(JAVA_OBJECT me, JAVA_INT n1)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_CGPDFPage_getBoxRect___int]
    XMLVM_VAR_CFTHIZ;
    return fromCGRect(CGPDFPageGetBoxRect(thiz, n1));
    //XMLVM_END_WRAPPER
}

JAVA_INT org_xmlvm_iphone_CGPDFPage_getRotationAngle__(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_CGPDFPage_getRotationAngle__]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

JAVA_OBJECT org_xmlvm_iphone_CGPDFPage_getDrawingTransform___int_org_xmlvm_iphone_CGRect_int_boolean(JAVA_OBJECT me, JAVA_INT n1, JAVA_OBJECT n2, JAVA_INT n3, JAVA_BOOLEAN n4)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_CGPDFPage_getDrawingTransform___int_org_xmlvm_iphone_CGRect_int_boolean]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

