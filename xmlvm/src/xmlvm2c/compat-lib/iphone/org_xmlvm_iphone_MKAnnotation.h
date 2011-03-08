#ifndef __ORG_XMLVM_IPHONE_MKANNOTATION__
#define __ORG_XMLVM_IPHONE_MKANNOTATION__

#include "xmlvm.h"
#include "org_xmlvm_iphone_NSObject.h"

// Circular references:
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_NSObject
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_NSObject
XMLVM_FORWARD_DECL(org_xmlvm_iphone_NSObject)
#endif
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_CLLocationCoordinate2D
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_CLLocationCoordinate2D
XMLVM_FORWARD_DECL(org_xmlvm_iphone_CLLocationCoordinate2D)
#endif
// Class declarations for org.xmlvm.iphone.MKAnnotation
XMLVM_DEFINE_CLASS(org_xmlvm_iphone_MKAnnotation, 18)

extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_MKAnnotation;
extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_MKAnnotation_ARRAYTYPE;

//XMLVM_BEGIN_DECLARATIONS
#define __ADDITIONAL_INSTANCE_FIELDS_org_xmlvm_iphone_MKAnnotation
//XMLVM_END_DECLARATIONS

#define __INSTANCE_FIELDS_org_xmlvm_iphone_MKAnnotation \
    __INSTANCE_FIELDS_org_xmlvm_iphone_NSObject; \
    struct { \
        __ADDITIONAL_INSTANCE_FIELDS_org_xmlvm_iphone_MKAnnotation \
    } org_xmlvm_iphone_MKAnnotation

struct org_xmlvm_iphone_MKAnnotation {
    __TIB_DEFINITION_org_xmlvm_iphone_MKAnnotation* tib;
    struct {
        __INSTANCE_FIELDS_org_xmlvm_iphone_MKAnnotation;
    } fields;
};
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_MKAnnotation
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_MKAnnotation
typedef struct org_xmlvm_iphone_MKAnnotation org_xmlvm_iphone_MKAnnotation;
#endif

#define XMLVM_VTABLE_SIZE_org_xmlvm_iphone_MKAnnotation 18
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_MKAnnotation_getCoordinate__ 14
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_MKAnnotation_setCoordinate___org_xmlvm_iphone_CLLocationCoordinate2D 15
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_MKAnnotation_title__ 16
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_MKAnnotation_subtitle__ 17

void __INIT_org_xmlvm_iphone_MKAnnotation();
void __DELETE_org_xmlvm_iphone_MKAnnotation(void* me, void* client_data);
JAVA_OBJECT __NEW_org_xmlvm_iphone_MKAnnotation();
JAVA_OBJECT __NEW_INSTANCE_org_xmlvm_iphone_MKAnnotation();
void org_xmlvm_iphone_MKAnnotation___INIT___(JAVA_OBJECT me);
// Vtable index: 14
JAVA_OBJECT org_xmlvm_iphone_MKAnnotation_getCoordinate__(JAVA_OBJECT me);
// Vtable index: 15
void org_xmlvm_iphone_MKAnnotation_setCoordinate___org_xmlvm_iphone_CLLocationCoordinate2D(JAVA_OBJECT me, JAVA_OBJECT n1);
// Vtable index: 16
JAVA_OBJECT org_xmlvm_iphone_MKAnnotation_title__(JAVA_OBJECT me);
// Vtable index: 17
JAVA_OBJECT org_xmlvm_iphone_MKAnnotation_subtitle__(JAVA_OBJECT me);

#endif