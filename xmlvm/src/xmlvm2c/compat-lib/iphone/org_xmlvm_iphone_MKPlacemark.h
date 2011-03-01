#ifndef __ORG_XMLVM_IPHONE_MKPLACEMARK__
#define __ORG_XMLVM_IPHONE_MKPLACEMARK__

#include "xmlvm.h"
#include "org_xmlvm_iphone_MKAnnotation.h"

// Circular references:
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_MKAnnotation
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_MKAnnotation
XMLVM_FORWARD_DECL(org_xmlvm_iphone_MKAnnotation)
#endif
#ifndef XMLVM_FORWARD_DECL_java_util_Map
#define XMLVM_FORWARD_DECL_java_util_Map
XMLVM_FORWARD_DECL(java_util_Map)
#endif
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_CLLocationCoordinate2D
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_CLLocationCoordinate2D
XMLVM_FORWARD_DECL(org_xmlvm_iphone_CLLocationCoordinate2D)
#endif
// Class declarations for org.xmlvm.iphone.MKPlacemark
XMLVM_DEFINE_CLASS(org_xmlvm_iphone_MKPlacemark, 28)

extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_MKPlacemark;
extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_MKPlacemark_ARRAYTYPE;

//XMLVM_BEGIN_DECLARATIONS
#define __ADDITIONAL_INSTANCE_FIELDS_org_xmlvm_iphone_MKPlacemark
//XMLVM_END_DECLARATIONS

#define __INSTANCE_FIELDS_org_xmlvm_iphone_MKPlacemark \
    __INSTANCE_FIELDS_org_xmlvm_iphone_MKAnnotation; \
    struct { \
        __ADDITIONAL_INSTANCE_FIELDS_org_xmlvm_iphone_MKPlacemark \
    } org_xmlvm_iphone_MKPlacemark

struct org_xmlvm_iphone_MKPlacemark {
    __TIB_DEFINITION_org_xmlvm_iphone_MKPlacemark* tib;
    struct {
        __INSTANCE_FIELDS_org_xmlvm_iphone_MKPlacemark;
    } fields;
};
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_MKPlacemark
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_MKPlacemark
typedef struct org_xmlvm_iphone_MKPlacemark org_xmlvm_iphone_MKPlacemark;
#endif

#define XMLVM_VTABLE_SIZE_org_xmlvm_iphone_MKPlacemark 28
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_MKPlacemark_getAddressDictionary__ 18
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_MKPlacemark_getAdministrativeArea__ 19
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_MKPlacemark_getCountry__ 20
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_MKPlacemark_getCountryCode__ 21
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_MKPlacemark_getLocality__ 22
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_MKPlacemark_getPostalCode__ 23
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_MKPlacemark_getSubAdministrativeArea__ 24
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_MKPlacemark_getSubLocality__ 25
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_MKPlacemark_getSubThoroughfare__ 26
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_MKPlacemark_getThoroughfare__ 27
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_MKPlacemark_getCoordinate__ 14
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_MKPlacemark_title__ 16
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_MKPlacemark_subtitle__ 17

void __INIT_org_xmlvm_iphone_MKPlacemark();
void __DELETE_org_xmlvm_iphone_MKPlacemark(void* me, void* client_data);
JAVA_OBJECT __NEW_org_xmlvm_iphone_MKPlacemark();
JAVA_OBJECT __NEW_INSTANCE_org_xmlvm_iphone_MKPlacemark();
void org_xmlvm_iphone_MKPlacemark___INIT____org_xmlvm_iphone_CLLocationCoordinate2D_java_util_Map(JAVA_OBJECT me, JAVA_OBJECT n1, JAVA_OBJECT n2);
// Vtable index: 18
JAVA_OBJECT org_xmlvm_iphone_MKPlacemark_getAddressDictionary__(JAVA_OBJECT me);
// Vtable index: 19
JAVA_OBJECT org_xmlvm_iphone_MKPlacemark_getAdministrativeArea__(JAVA_OBJECT me);
// Vtable index: 20
JAVA_OBJECT org_xmlvm_iphone_MKPlacemark_getCountry__(JAVA_OBJECT me);
// Vtable index: 21
JAVA_OBJECT org_xmlvm_iphone_MKPlacemark_getCountryCode__(JAVA_OBJECT me);
// Vtable index: 22
JAVA_OBJECT org_xmlvm_iphone_MKPlacemark_getLocality__(JAVA_OBJECT me);
// Vtable index: 23
JAVA_OBJECT org_xmlvm_iphone_MKPlacemark_getPostalCode__(JAVA_OBJECT me);
// Vtable index: 24
JAVA_OBJECT org_xmlvm_iphone_MKPlacemark_getSubAdministrativeArea__(JAVA_OBJECT me);
// Vtable index: 25
JAVA_OBJECT org_xmlvm_iphone_MKPlacemark_getSubLocality__(JAVA_OBJECT me);
// Vtable index: 26
JAVA_OBJECT org_xmlvm_iphone_MKPlacemark_getSubThoroughfare__(JAVA_OBJECT me);
// Vtable index: 27
JAVA_OBJECT org_xmlvm_iphone_MKPlacemark_getThoroughfare__(JAVA_OBJECT me);
// Vtable index: 14
JAVA_OBJECT org_xmlvm_iphone_MKPlacemark_getCoordinate__(JAVA_OBJECT me);
// Vtable index: 16
JAVA_OBJECT org_xmlvm_iphone_MKPlacemark_title__(JAVA_OBJECT me);
// Vtable index: 17
JAVA_OBJECT org_xmlvm_iphone_MKPlacemark_subtitle__(JAVA_OBJECT me);

#endif
