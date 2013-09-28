#ifndef __ORG_XMLVM_IPHONE_MKPINANNOTATIONVIEW__
#define __ORG_XMLVM_IPHONE_MKPINANNOTATIONVIEW__

#include "xmlvm.h"

// Preprocessor constants for interfaces:
// Implemented interfaces:
// Super Class:
#include "org_xmlvm_iphone_MKAnnotationView.h"

// Circular references:
#ifndef XMLVM_FORWARD_DECL_java_lang_String
#define XMLVM_FORWARD_DECL_java_lang_String
XMLVM_FORWARD_DECL(java_lang_String)
#endif
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_MKAnnotation
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_MKAnnotation
XMLVM_FORWARD_DECL(org_xmlvm_iphone_MKAnnotation)
#endif
// Class declarations for org.xmlvm.iphone.MKPinAnnotationView
XMLVM_DEFINE_CLASS(org_xmlvm_iphone_MKPinAnnotationView, 15, XMLVM_ITABLE_SIZE_org_xmlvm_iphone_MKPinAnnotationView)

extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_MKPinAnnotationView;
extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_MKPinAnnotationView_1ARRAY;
extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_MKPinAnnotationView_2ARRAY;
extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_MKPinAnnotationView_3ARRAY;
//XMLVM_BEGIN_DECLARATIONS
#define __ADDITIONAL_INSTANCE_FIELDS_org_xmlvm_iphone_MKPinAnnotationView
//XMLVM_END_DECLARATIONS

#define __INSTANCE_FIELDS_org_xmlvm_iphone_MKPinAnnotationView \
    __INSTANCE_FIELDS_org_xmlvm_iphone_MKAnnotationView; \
    struct { \
        __ADDITIONAL_INSTANCE_FIELDS_org_xmlvm_iphone_MKPinAnnotationView \
    } org_xmlvm_iphone_MKPinAnnotationView

struct org_xmlvm_iphone_MKPinAnnotationView {
    __TIB_DEFINITION_org_xmlvm_iphone_MKPinAnnotationView* tib;
    struct {
        __INSTANCE_FIELDS_org_xmlvm_iphone_MKPinAnnotationView;
    } fields;
};
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_MKPinAnnotationView
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_MKPinAnnotationView
typedef struct org_xmlvm_iphone_MKPinAnnotationView org_xmlvm_iphone_MKPinAnnotationView;
#endif

#define XMLVM_VTABLE_SIZE_org_xmlvm_iphone_MKPinAnnotationView 15

void __INIT_org_xmlvm_iphone_MKPinAnnotationView();
void __INIT_IMPL_org_xmlvm_iphone_MKPinAnnotationView();
void __DELETE_org_xmlvm_iphone_MKPinAnnotationView(void* me, void* client_data);
void __INIT_INSTANCE_MEMBERS_org_xmlvm_iphone_MKPinAnnotationView(JAVA_OBJECT me, int derivedClassWillRegisterFinalizer);
JAVA_OBJECT __NEW_org_xmlvm_iphone_MKPinAnnotationView();
JAVA_OBJECT __NEW_INSTANCE_org_xmlvm_iphone_MKPinAnnotationView();
void org_xmlvm_iphone_MKPinAnnotationView___INIT____org_xmlvm_iphone_MKAnnotation_java_lang_String(JAVA_OBJECT me, JAVA_OBJECT n1, JAVA_OBJECT n2);
JAVA_BOOLEAN org_xmlvm_iphone_MKPinAnnotationView_isAnimatesDrop__(JAVA_OBJECT me);
void org_xmlvm_iphone_MKPinAnnotationView_setAnimatesDrop___boolean(JAVA_OBJECT me, JAVA_BOOLEAN n1);
JAVA_INT org_xmlvm_iphone_MKPinAnnotationView_getPinColor__(JAVA_OBJECT me);
void org_xmlvm_iphone_MKPinAnnotationView_setPinColor___int(JAVA_OBJECT me, JAVA_INT n1);

// Define a Macro for the method declarations of the Obj-C wrapper class so that subclass wrappers may easily include these too
#define XMLVM_OBJC_OVERRIDE_CLASS_DECLARATIONS_org_xmlvm_iphone_MKPinAnnotationView \
XMLVM_OBJC_OVERRIDE_CLASS_DECLARATIONS_org_xmlvm_iphone_MKAnnotationView \

// Define a Macro for the entire contents of the Obj-C wrapper class
#define XMLVM_OBJC_OVERRIDE_CLASS_DEFINITIONS_org_xmlvm_iphone_MKPinAnnotationView \
XMLVM_OBJC_OVERRIDE_CLASS_DEFINITIONS_org_xmlvm_iphone_MKAnnotationView \


#endif
