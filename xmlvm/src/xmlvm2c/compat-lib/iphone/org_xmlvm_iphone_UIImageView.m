#include "xmlvm.h"
#include "org_xmlvm_iphone_CGRect.h"
#include "org_xmlvm_iphone_UIImage.h"

#include "org_xmlvm_iphone_UIImageView.h"

#define XMLVM_CURRENT_CLASS_NAME UIImageView
#define XMLVM_CURRENT_PKG_CLASS_NAME org_xmlvm_iphone_UIImageView

__TIB_DEFINITION_org_xmlvm_iphone_UIImageView __TIB_org_xmlvm_iphone_UIImageView = {
    0, // classInitializationBegan
    0, // classInitialized
    -1, // initializerThreadId
    __INIT_org_xmlvm_iphone_UIImageView, // classInitializer
    "org.xmlvm.iphone.UIImageView", // className
    "org.xmlvm.iphone", // package
    JAVA_NULL, // enclosingClassName
    JAVA_NULL, // enclosingMethodName
    JAVA_NULL, // signature
    (__TIB_DEFINITION_TEMPLATE*) &__TIB_org_xmlvm_iphone_UIView, // extends
    sizeof(org_xmlvm_iphone_UIImageView), // sizeInstance
    XMLVM_TYPE_CLASS};

JAVA_OBJECT __CLASS_org_xmlvm_iphone_UIImageView;
JAVA_OBJECT __CLASS_org_xmlvm_iphone_UIImageView_1ARRAY;
JAVA_OBJECT __CLASS_org_xmlvm_iphone_UIImageView_2ARRAY;
JAVA_OBJECT __CLASS_org_xmlvm_iphone_UIImageView_3ARRAY;
//XMLVM_BEGIN_IMPLEMENTATION

#import <UIKit/UIImageView.h>

void org_xmlvm_iphone_UIImageView_INTERNAL_CONSTRUCTOR(JAVA_OBJECT me, NSObject* wrappedObj)
{
    org_xmlvm_iphone_UIView_INTERNAL_CONSTRUCTOR(me, wrappedObj);
}

//XMLVM_END_IMPLEMENTATION


#include "xmlvm-reflection.h"

static XMLVM_FIELD_REFLECTION_DATA __field_reflection_data[] = {
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
    "()V",
    JAVA_NULL,
    JAVA_NULL},
    {&__constructor1_arg_types[0],
    sizeof(__constructor1_arg_types) / sizeof(JAVA_OBJECT*),
    JAVA_NULL,
    0,
    0,
    "(Lorg/xmlvm/iphone/CGRect;)V",
    JAVA_NULL,
    JAVA_NULL},
};

static JAVA_OBJECT constructor_dispatcher(JAVA_OBJECT constructor, JAVA_OBJECT arguments)
{
    JAVA_OBJECT obj = __NEW_org_xmlvm_iphone_UIImageView();
    java_lang_reflect_Constructor* c = (java_lang_reflect_Constructor*) constructor;
    org_xmlvm_runtime_XMLVMArray* args = (org_xmlvm_runtime_XMLVMArray*) arguments;
    JAVA_ARRAY_OBJECT* argsArray = (JAVA_ARRAY_OBJECT*) args->fields.org_xmlvm_runtime_XMLVMArray.array_;
    switch (c->fields.java_lang_reflect_Constructor.slot_) {
    case 0:
        org_xmlvm_iphone_UIImageView___INIT___(obj);
        break;
    case 1:
        org_xmlvm_iphone_UIImageView___INIT____org_xmlvm_iphone_CGRect(obj, argsArray[0]);
        break;
    default:
        XMLVM_INTERNAL_ERROR();
        break;
    }
    return obj;
}

static JAVA_OBJECT* __method0_arg_types[] = {
    &__CLASS_org_xmlvm_iphone_UIImage,
};

static JAVA_OBJECT* __method1_arg_types[] = {
};

static XMLVM_METHOD_REFLECTION_DATA __method_reflection_data[] = {
    {"setImage",
    &__method0_arg_types[0],
    sizeof(__method0_arg_types) / sizeof(JAVA_OBJECT*),
    JAVA_NULL,
    0,
    0,
    "(Lorg/xmlvm/iphone/UIImage;)V",
    JAVA_NULL,
    JAVA_NULL},
    {"getImage",
    &__method1_arg_types[0],
    sizeof(__method1_arg_types) / sizeof(JAVA_OBJECT*),
    JAVA_NULL,
    0,
    0,
    "()Lorg/xmlvm/iphone/UIImage;",
    JAVA_NULL,
    JAVA_NULL},
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
    case 0:
        org_xmlvm_iphone_UIImageView_setImage___org_xmlvm_iphone_UIImage(receiver, argsArray[0]);
        break;
    case 1:
        result = (JAVA_OBJECT) org_xmlvm_iphone_UIImageView_getImage__(receiver);
        break;
    default:
        XMLVM_INTERNAL_ERROR();
        break;
    }
    return result;
}

void __INIT_org_xmlvm_iphone_UIImageView()
{
    staticInitializerLock(&__TIB_org_xmlvm_iphone_UIImageView);

    // While the static initializer mutex is locked, locally store the value of
    // whether class initialization began or not
    int initBegan = __TIB_org_xmlvm_iphone_UIImageView.classInitializationBegan;

    // Whether or not class initialization had already began, it has begun now
    __TIB_org_xmlvm_iphone_UIImageView.classInitializationBegan = 1;

    staticInitializerUnlock(&__TIB_org_xmlvm_iphone_UIImageView);

    JAVA_LONG curThreadId = (JAVA_LONG)pthread_self();
    if (initBegan) {
        if (__TIB_org_xmlvm_iphone_UIImageView.initializerThreadId != curThreadId) {
            // Busy wait until the other thread finishes initializing this class
            while (!__TIB_org_xmlvm_iphone_UIImageView.classInitialized) {
                // do nothing
            }
        }
    } else {
        __TIB_org_xmlvm_iphone_UIImageView.initializerThreadId = curThreadId;
        __INIT_IMPL_org_xmlvm_iphone_UIImageView();
    }
}

void __INIT_IMPL_org_xmlvm_iphone_UIImageView()
{
    // Initialize base class if necessary
    if (!__TIB_org_xmlvm_iphone_UIView.classInitialized) __INIT_org_xmlvm_iphone_UIView();
    __TIB_org_xmlvm_iphone_UIImageView.newInstanceFunc = __NEW_INSTANCE_org_xmlvm_iphone_UIImageView;
    // Copy vtable from base class
    XMLVM_MEMCPY(__TIB_org_xmlvm_iphone_UIImageView.vtable, __TIB_org_xmlvm_iphone_UIView.vtable, sizeof(__TIB_org_xmlvm_iphone_UIView.vtable));
    // Initialize vtable for this class
    // Initialize interface information
    __TIB_org_xmlvm_iphone_UIImageView.numImplementedInterfaces = 0;
    __TIB_org_xmlvm_iphone_UIImageView.implementedInterfaces = (__TIB_DEFINITION_TEMPLATE* (*)[1]) XMLVM_MALLOC(sizeof(__TIB_DEFINITION_TEMPLATE*) * 0);

    // Initialize interfaces if necessary and assign tib to implementedInterfaces

    __TIB_org_xmlvm_iphone_UIImageView.declaredFields = &__field_reflection_data[0];
    __TIB_org_xmlvm_iphone_UIImageView.numDeclaredFields = sizeof(__field_reflection_data) / sizeof(XMLVM_FIELD_REFLECTION_DATA);
    __TIB_org_xmlvm_iphone_UIImageView.constructorDispatcherFunc = constructor_dispatcher;
    __TIB_org_xmlvm_iphone_UIImageView.declaredConstructors = &__constructor_reflection_data[0];
    __TIB_org_xmlvm_iphone_UIImageView.numDeclaredConstructors = sizeof(__constructor_reflection_data) / sizeof(XMLVM_CONSTRUCTOR_REFLECTION_DATA);
    __TIB_org_xmlvm_iphone_UIImageView.methodDispatcherFunc = method_dispatcher;
    __TIB_org_xmlvm_iphone_UIImageView.declaredMethods = &__method_reflection_data[0];
    __TIB_org_xmlvm_iphone_UIImageView.numDeclaredMethods = sizeof(__method_reflection_data) / sizeof(XMLVM_METHOD_REFLECTION_DATA);
    __CLASS_org_xmlvm_iphone_UIImageView = XMLVM_CREATE_CLASS_OBJECT(&__TIB_org_xmlvm_iphone_UIImageView);
    __TIB_org_xmlvm_iphone_UIImageView.clazz = __CLASS_org_xmlvm_iphone_UIImageView;
    __TIB_org_xmlvm_iphone_UIImageView.baseType = JAVA_NULL;
    __CLASS_org_xmlvm_iphone_UIImageView_1ARRAY = XMLVM_CREATE_ARRAY_CLASS_OBJECT(__CLASS_org_xmlvm_iphone_UIImageView);
    __CLASS_org_xmlvm_iphone_UIImageView_2ARRAY = XMLVM_CREATE_ARRAY_CLASS_OBJECT(__CLASS_org_xmlvm_iphone_UIImageView_1ARRAY);
    __CLASS_org_xmlvm_iphone_UIImageView_3ARRAY = XMLVM_CREATE_ARRAY_CLASS_OBJECT(__CLASS_org_xmlvm_iphone_UIImageView_2ARRAY);
    //XMLVM_BEGIN_WRAPPER[__INIT_org_xmlvm_iphone_UIImageView]
    //XMLVM_END_WRAPPER

    __TIB_org_xmlvm_iphone_UIImageView.classInitialized = 1;
}

void __DELETE_org_xmlvm_iphone_UIImageView(void* me, void* client_data)
{
    //XMLVM_BEGIN_WRAPPER[__DELETE_org_xmlvm_iphone_UIImageView]
    __DELETE_org_xmlvm_iphone_UIView(me, client_data);
    //XMLVM_END_WRAPPER
}

void __INIT_INSTANCE_MEMBERS_org_xmlvm_iphone_UIImageView(JAVA_OBJECT me, int derivedClassWillRegisterFinalizer)
{
    __INIT_INSTANCE_MEMBERS_org_xmlvm_iphone_UIView(me, 0 || derivedClassWillRegisterFinalizer);
    //XMLVM_BEGIN_WRAPPER[__INIT_INSTANCE_MEMBERS_org_xmlvm_iphone_UIImageView]
    //XMLVM_END_WRAPPER
}

JAVA_OBJECT __NEW_org_xmlvm_iphone_UIImageView()
{
    if (!__TIB_org_xmlvm_iphone_UIImageView.classInitialized) __INIT_org_xmlvm_iphone_UIImageView();
    org_xmlvm_iphone_UIImageView* me = (org_xmlvm_iphone_UIImageView*) XMLVM_MALLOC(sizeof(org_xmlvm_iphone_UIImageView));
    me->tib = &__TIB_org_xmlvm_iphone_UIImageView;
    __INIT_INSTANCE_MEMBERS_org_xmlvm_iphone_UIImageView(me, 0);
    //XMLVM_BEGIN_WRAPPER[__NEW_org_xmlvm_iphone_UIImageView]
    //XMLVM_END_WRAPPER
    return me;
}

JAVA_OBJECT __NEW_INSTANCE_org_xmlvm_iphone_UIImageView()
{
    JAVA_OBJECT me = JAVA_NULL;
    me = __NEW_org_xmlvm_iphone_UIImageView();
    org_xmlvm_iphone_UIImageView___INIT___(me);
    return me;
}

void org_xmlvm_iphone_UIImageView___INIT___(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_UIImageView___INIT___]
    UIImageView* obj = [[UIImageView alloc] init];
    org_xmlvm_iphone_UIImageView_INTERNAL_CONSTRUCTOR(me, obj);
    //XMLVM_END_WRAPPER
}

void org_xmlvm_iphone_UIImageView___INIT____org_xmlvm_iphone_CGRect(JAVA_OBJECT me, JAVA_OBJECT n1)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_UIImageView___INIT____org_xmlvm_iphone_CGRect]
    CGRect rect = toCGRect(n1);
    UIImageView* obj = [[UIImageView alloc] init];
    [obj setFrame:rect];
	[obj setContentMode:UIViewContentModeCenter];
    org_xmlvm_iphone_UIImageView_INTERNAL_CONSTRUCTOR(me, obj);
    //XMLVM_END_WRAPPER
}

void org_xmlvm_iphone_UIImageView_setImage___org_xmlvm_iphone_UIImage(JAVA_OBJECT me, JAVA_OBJECT n1)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_UIImageView_setImage___org_xmlvm_iphone_UIImage]
    XMLVM_VAR_THIZ;
    XMLVM_VAR_IOS(UIImage, image, n1);
    
    jthiz->fields.org_xmlvm_iphone_UIImageView.image = jimage;
    [thiz setImage:image];
    //XMLVM_END_WRAPPER
}

JAVA_OBJECT org_xmlvm_iphone_UIImageView_getImage__(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_UIImageView_getImage__]
    org_xmlvm_iphone_UIImageView* thiz = me;
    return thiz->fields.org_xmlvm_iphone_UIImageView.image;
    //XMLVM_END_WRAPPER
}

