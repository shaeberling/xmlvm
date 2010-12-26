#ifndef __ORG_XMLVM_IPHONE_NSBUNDLE__
#define __ORG_XMLVM_IPHONE_NSBUNDLE__

#include "xmlvm.h"
#include "org_xmlvm_iphone_NSObject.h"

// Circular references:
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_NSObject
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_NSObject
XMLVM_FORWARD_DECL(org_xmlvm_iphone_NSObject)
#endif
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_NSBundle
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_NSBundle
XMLVM_FORWARD_DECL(org_xmlvm_iphone_NSBundle)
#endif
#ifndef XMLVM_FORWARD_DECL_java_lang_String
#define XMLVM_FORWARD_DECL_java_lang_String
XMLVM_FORWARD_DECL(java_lang_String)
#endif
// Class declarations for org.xmlvm.iphone.NSBundle
XMLVM_DEFINE_CLASS(org_xmlvm_iphone_NSBundle, 17)

//XMLVM_BEGIN_DECLARATIONS
#define __ADDITIONAL_INSTANCE_FIELDS_org_xmlvm_iphone_NSBundle
//XMLVM_END_DECLARATIONS

#define __INSTANCE_FIELDS_org_xmlvm_iphone_NSBundle \
    __INSTANCE_FIELDS_org_xmlvm_iphone_NSObject; \
    struct { \
        __ADDITIONAL_INSTANCE_FIELDS_org_xmlvm_iphone_NSBundle \
    } org_xmlvm_iphone_NSBundle

struct org_xmlvm_iphone_NSBundle {
    __TIB_DEFINITION_org_xmlvm_iphone_NSBundle* tib;
    struct {
        __INSTANCE_FIELDS_org_xmlvm_iphone_NSBundle;
    } fields;
};
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_NSBundle
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_NSBundle
typedef struct org_xmlvm_iphone_NSBundle org_xmlvm_iphone_NSBundle;
#endif

#define XMLVM_VTABLE_SIZE_org_xmlvm_iphone_NSBundle 17
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_NSBundle_pathForResource___java_lang_String_java_lang_String_java_lang_String 14
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_NSBundle_pathForResource___java_lang_String_java_lang_String 15
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_NSBundle_bundlePath__ 16

void __INIT_org_xmlvm_iphone_NSBundle();
JAVA_OBJECT __NEW_org_xmlvm_iphone_NSBundle();
JAVA_OBJECT __NEW_INSTANCE_org_xmlvm_iphone_NSBundle();
JAVA_OBJECT org_xmlvm_iphone_NSBundle_mainBundle__();
// Vtable index: 14
JAVA_OBJECT org_xmlvm_iphone_NSBundle_pathForResource___java_lang_String_java_lang_String_java_lang_String(JAVA_OBJECT me, JAVA_OBJECT n1, JAVA_OBJECT n2, JAVA_OBJECT n3);
// Vtable index: 15
JAVA_OBJECT org_xmlvm_iphone_NSBundle_pathForResource___java_lang_String_java_lang_String(JAVA_OBJECT me, JAVA_OBJECT n1, JAVA_OBJECT n2);
// Vtable index: 16
JAVA_OBJECT org_xmlvm_iphone_NSBundle_bundlePath__(JAVA_OBJECT me);
void org_xmlvm_iphone_NSBundle___CLINIT_();

#endif
