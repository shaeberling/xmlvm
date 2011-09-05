#ifndef __ORG_XMLVM_IPHONE_UICONTROLCONTENTHORIZONTALALIGNMENT__
#define __ORG_XMLVM_IPHONE_UICONTROLCONTENTHORIZONTALALIGNMENT__

#include "xmlvm.h"
#include "java_lang_Object.h"

// Circular references:
#ifndef XMLVM_FORWARD_DECL_java_lang_Object
#define XMLVM_FORWARD_DECL_java_lang_Object
XMLVM_FORWARD_DECL(java_lang_Object)
#endif
// Class declarations for org.xmlvm.iphone.UIControlContentHorizontalAlignment
XMLVM_DEFINE_CLASS(org_xmlvm_iphone_UIControlContentHorizontalAlignment, 6, XMLVM_ITABLE_SIZE_org_xmlvm_iphone_UIControlContentHorizontalAlignment)

extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_UIControlContentHorizontalAlignment;
extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_UIControlContentHorizontalAlignment_1ARRAY;
extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_UIControlContentHorizontalAlignment_2ARRAY;
extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_UIControlContentHorizontalAlignment_3ARRAY;
//XMLVM_BEGIN_DECLARATIONS
#define __ADDITIONAL_INSTANCE_FIELDS_org_xmlvm_iphone_UIControlContentHorizontalAlignment
//XMLVM_END_DECLARATIONS

#define __INSTANCE_FIELDS_org_xmlvm_iphone_UIControlContentHorizontalAlignment \
    __INSTANCE_FIELDS_java_lang_Object; \
    struct { \
        __ADDITIONAL_INSTANCE_FIELDS_org_xmlvm_iphone_UIControlContentHorizontalAlignment \
    } org_xmlvm_iphone_UIControlContentHorizontalAlignment

struct org_xmlvm_iphone_UIControlContentHorizontalAlignment {
    __TIB_DEFINITION_org_xmlvm_iphone_UIControlContentHorizontalAlignment* tib;
    struct {
        __INSTANCE_FIELDS_org_xmlvm_iphone_UIControlContentHorizontalAlignment;
    } fields;
};
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_UIControlContentHorizontalAlignment
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_UIControlContentHorizontalAlignment
typedef struct org_xmlvm_iphone_UIControlContentHorizontalAlignment org_xmlvm_iphone_UIControlContentHorizontalAlignment;
#endif

#define XMLVM_VTABLE_SIZE_org_xmlvm_iphone_UIControlContentHorizontalAlignment 6

void __INIT_org_xmlvm_iphone_UIControlContentHorizontalAlignment();
void __INIT_IMPL_org_xmlvm_iphone_UIControlContentHorizontalAlignment();
void __DELETE_org_xmlvm_iphone_UIControlContentHorizontalAlignment(void* me, void* client_data);
void __INIT_INSTANCE_MEMBERS_org_xmlvm_iphone_UIControlContentHorizontalAlignment(JAVA_OBJECT me, int derivedClassWillRegisterFinalizer);
JAVA_OBJECT __NEW_org_xmlvm_iphone_UIControlContentHorizontalAlignment();
JAVA_OBJECT __NEW_INSTANCE_org_xmlvm_iphone_UIControlContentHorizontalAlignment();
JAVA_INT org_xmlvm_iphone_UIControlContentHorizontalAlignment_GET_Center();
void org_xmlvm_iphone_UIControlContentHorizontalAlignment_PUT_Center(JAVA_INT v);
JAVA_INT org_xmlvm_iphone_UIControlContentHorizontalAlignment_GET_Left();
void org_xmlvm_iphone_UIControlContentHorizontalAlignment_PUT_Left(JAVA_INT v);
JAVA_INT org_xmlvm_iphone_UIControlContentHorizontalAlignment_GET_Right();
void org_xmlvm_iphone_UIControlContentHorizontalAlignment_PUT_Right(JAVA_INT v);
JAVA_INT org_xmlvm_iphone_UIControlContentHorizontalAlignment_GET_Fill();
void org_xmlvm_iphone_UIControlContentHorizontalAlignment_PUT_Fill(JAVA_INT v);

#endif
