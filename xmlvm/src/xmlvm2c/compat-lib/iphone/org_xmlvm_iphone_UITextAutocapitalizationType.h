#ifndef __ORG_XMLVM_IPHONE_UITEXTAUTOCAPITALIZATIONTYPE__
#define __ORG_XMLVM_IPHONE_UITEXTAUTOCAPITALIZATIONTYPE__

#include "xmlvm.h"

// Preprocessor constants for interfaces:
// Implemented interfaces:
// Super Class:
#include "java_lang_Object.h"

// Circular references:
// Class declarations for org.xmlvm.iphone.UITextAutocapitalizationType
XMLVM_DEFINE_CLASS(org_xmlvm_iphone_UITextAutocapitalizationType, 6, XMLVM_ITABLE_SIZE_org_xmlvm_iphone_UITextAutocapitalizationType)

extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_UITextAutocapitalizationType;
extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_UITextAutocapitalizationType_1ARRAY;
extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_UITextAutocapitalizationType_2ARRAY;
extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_UITextAutocapitalizationType_3ARRAY;
//XMLVM_BEGIN_DECLARATIONS
#define __ADDITIONAL_INSTANCE_FIELDS_org_xmlvm_iphone_UITextAutocapitalizationType
//XMLVM_END_DECLARATIONS

#define __INSTANCE_FIELDS_org_xmlvm_iphone_UITextAutocapitalizationType \
    __INSTANCE_FIELDS_java_lang_Object; \
    struct { \
        __ADDITIONAL_INSTANCE_FIELDS_org_xmlvm_iphone_UITextAutocapitalizationType \
    } org_xmlvm_iphone_UITextAutocapitalizationType

struct org_xmlvm_iphone_UITextAutocapitalizationType {
    __TIB_DEFINITION_org_xmlvm_iphone_UITextAutocapitalizationType* tib;
    struct {
        __INSTANCE_FIELDS_org_xmlvm_iphone_UITextAutocapitalizationType;
    } fields;
};
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_UITextAutocapitalizationType
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_UITextAutocapitalizationType
typedef struct org_xmlvm_iphone_UITextAutocapitalizationType org_xmlvm_iphone_UITextAutocapitalizationType;
#endif

#define XMLVM_VTABLE_SIZE_org_xmlvm_iphone_UITextAutocapitalizationType 6

void __INIT_org_xmlvm_iphone_UITextAutocapitalizationType();
void __INIT_IMPL_org_xmlvm_iphone_UITextAutocapitalizationType();
void __DELETE_org_xmlvm_iphone_UITextAutocapitalizationType(void* me, void* client_data);
void __INIT_INSTANCE_MEMBERS_org_xmlvm_iphone_UITextAutocapitalizationType(JAVA_OBJECT me, int derivedClassWillRegisterFinalizer);
JAVA_OBJECT __NEW_org_xmlvm_iphone_UITextAutocapitalizationType();
JAVA_OBJECT __NEW_INSTANCE_org_xmlvm_iphone_UITextAutocapitalizationType();
JAVA_INT org_xmlvm_iphone_UITextAutocapitalizationType_GET_None();
void org_xmlvm_iphone_UITextAutocapitalizationType_PUT_None(JAVA_INT v);
JAVA_INT org_xmlvm_iphone_UITextAutocapitalizationType_GET_Words();
void org_xmlvm_iphone_UITextAutocapitalizationType_PUT_Words(JAVA_INT v);
JAVA_INT org_xmlvm_iphone_UITextAutocapitalizationType_GET_Sentences();
void org_xmlvm_iphone_UITextAutocapitalizationType_PUT_Sentences(JAVA_INT v);
JAVA_INT org_xmlvm_iphone_UITextAutocapitalizationType_GET_AllCharacters();
void org_xmlvm_iphone_UITextAutocapitalizationType_PUT_AllCharacters(JAVA_INT v);

// Define a Macro for the method declarations of the Obj-C wrapper class so that subclass wrappers may easily include these too
#define XMLVM_OBJC_OVERRIDE_CLASS_DECLARATIONS_org_xmlvm_iphone_UITextAutocapitalizationType \

// Define a Macro for the entire contents of the Obj-C wrapper class
#define XMLVM_OBJC_OVERRIDE_CLASS_DEFINITIONS_org_xmlvm_iphone_UITextAutocapitalizationType \


#endif
