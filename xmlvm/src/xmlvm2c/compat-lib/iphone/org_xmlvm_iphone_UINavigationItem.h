#ifndef __ORG_XMLVM_IPHONE_UINAVIGATIONITEM__
#define __ORG_XMLVM_IPHONE_UINAVIGATIONITEM__

#include "xmlvm.h"

// Preprocessor constants for interfaces:
// Implemented interfaces:
// Super Class:
#include "org_xmlvm_iphone_NSObject.h"

// Circular references:
#ifndef XMLVM_FORWARD_DECL_java_lang_String
#define XMLVM_FORWARD_DECL_java_lang_String
XMLVM_FORWARD_DECL(java_lang_String)
#endif
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_UIBarButtonItem
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_UIBarButtonItem
XMLVM_FORWARD_DECL(org_xmlvm_iphone_UIBarButtonItem)
#endif
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_UINavigationBar
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_UINavigationBar
XMLVM_FORWARD_DECL(org_xmlvm_iphone_UINavigationBar)
#endif
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_UIView
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_UIView
XMLVM_FORWARD_DECL(org_xmlvm_iphone_UIView)
#endif
// Class declarations for org.xmlvm.iphone.UINavigationItem
XMLVM_DEFINE_CLASS(org_xmlvm_iphone_UINavigationItem, 7, XMLVM_ITABLE_SIZE_org_xmlvm_iphone_UINavigationItem)

extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_UINavigationItem;
extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_UINavigationItem_1ARRAY;
extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_UINavigationItem_2ARRAY;
extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_UINavigationItem_3ARRAY;
//XMLVM_BEGIN_DECLARATIONS
#define __ADDITIONAL_INSTANCE_FIELDS_org_xmlvm_iphone_UINavigationItem
//XMLVM_END_DECLARATIONS

#define __INSTANCE_FIELDS_org_xmlvm_iphone_UINavigationItem \
    __INSTANCE_FIELDS_org_xmlvm_iphone_NSObject; \
    struct { \
        __ADDITIONAL_INSTANCE_FIELDS_org_xmlvm_iphone_UINavigationItem \
    } org_xmlvm_iphone_UINavigationItem

struct org_xmlvm_iphone_UINavigationItem {
    __TIB_DEFINITION_org_xmlvm_iphone_UINavigationItem* tib;
    struct {
        __INSTANCE_FIELDS_org_xmlvm_iphone_UINavigationItem;
    } fields;
};
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_UINavigationItem
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_UINavigationItem
typedef struct org_xmlvm_iphone_UINavigationItem org_xmlvm_iphone_UINavigationItem;
#endif

#define XMLVM_VTABLE_SIZE_org_xmlvm_iphone_UINavigationItem 7

void __INIT_org_xmlvm_iphone_UINavigationItem();
void __INIT_IMPL_org_xmlvm_iphone_UINavigationItem();
void __DELETE_org_xmlvm_iphone_UINavigationItem(void* me, void* client_data);
void __INIT_INSTANCE_MEMBERS_org_xmlvm_iphone_UINavigationItem(JAVA_OBJECT me, int derivedClassWillRegisterFinalizer);
JAVA_OBJECT __NEW_org_xmlvm_iphone_UINavigationItem();
JAVA_OBJECT __NEW_INSTANCE_org_xmlvm_iphone_UINavigationItem();
void org_xmlvm_iphone_UINavigationItem___INIT____java_lang_String(JAVA_OBJECT me, JAVA_OBJECT n1);
void org_xmlvm_iphone_UINavigationItem_setToolbar___org_xmlvm_iphone_UINavigationBar(JAVA_OBJECT me, JAVA_OBJECT n1);
JAVA_OBJECT org_xmlvm_iphone_UINavigationItem_getBackBarButtonItem__(JAVA_OBJECT me);
void org_xmlvm_iphone_UINavigationItem_setBackBarButtonItem___org_xmlvm_iphone_UIBarButtonItem(JAVA_OBJECT me, JAVA_OBJECT n1);
JAVA_BOOLEAN org_xmlvm_iphone_UINavigationItem_hidesBackButton__(JAVA_OBJECT me);
void org_xmlvm_iphone_UINavigationItem_setHidesBackButton___boolean(JAVA_OBJECT me, JAVA_BOOLEAN n1);
void org_xmlvm_iphone_UINavigationItem_setHidesBackButton___boolean_boolean(JAVA_OBJECT me, JAVA_BOOLEAN n1, JAVA_BOOLEAN n2);
JAVA_OBJECT org_xmlvm_iphone_UINavigationItem_getLeftBarButtonItem__(JAVA_OBJECT me);
void org_xmlvm_iphone_UINavigationItem_setLeftBarButtonItem___org_xmlvm_iphone_UIBarButtonItem(JAVA_OBJECT me, JAVA_OBJECT n1);
void org_xmlvm_iphone_UINavigationItem_setLeftBarButtonItem___org_xmlvm_iphone_UIBarButtonItem_boolean(JAVA_OBJECT me, JAVA_OBJECT n1, JAVA_BOOLEAN n2);
JAVA_OBJECT org_xmlvm_iphone_UINavigationItem_getPrompt__(JAVA_OBJECT me);
void org_xmlvm_iphone_UINavigationItem_setPrompt___java_lang_String(JAVA_OBJECT me, JAVA_OBJECT n1);
JAVA_OBJECT org_xmlvm_iphone_UINavigationItem_getRightBarButtonItem__(JAVA_OBJECT me);
void org_xmlvm_iphone_UINavigationItem_setRightBarButtonItem___org_xmlvm_iphone_UIBarButtonItem(JAVA_OBJECT me, JAVA_OBJECT n1);
void org_xmlvm_iphone_UINavigationItem_setRightBarButtonItem___org_xmlvm_iphone_UIBarButtonItem_boolean(JAVA_OBJECT me, JAVA_OBJECT n1, JAVA_BOOLEAN n2);
JAVA_OBJECT org_xmlvm_iphone_UINavigationItem_getTitle__(JAVA_OBJECT me);
void org_xmlvm_iphone_UINavigationItem_setTitle___java_lang_String(JAVA_OBJECT me, JAVA_OBJECT n1);
JAVA_OBJECT org_xmlvm_iphone_UINavigationItem_getTitleView__(JAVA_OBJECT me);
void org_xmlvm_iphone_UINavigationItem_setTitleView___org_xmlvm_iphone_UIView(JAVA_OBJECT me, JAVA_OBJECT n1);

// Define a Macro for the method declarations of the Obj-C wrapper class so that subclass wrappers may easily include these too
#define XMLVM_OBJC_OVERRIDE_CLASS_DECLARATIONS_org_xmlvm_iphone_UINavigationItem \
XMLVM_OBJC_OVERRIDE_CLASS_DECLARATIONS_org_xmlvm_iphone_NSObject \

// Define a Macro for the entire contents of the Obj-C wrapper class
#define XMLVM_OBJC_OVERRIDE_CLASS_DEFINITIONS_org_xmlvm_iphone_UINavigationItem \
XMLVM_OBJC_OVERRIDE_CLASS_DEFINITIONS_org_xmlvm_iphone_NSObject \


#endif
