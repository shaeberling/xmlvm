#ifndef __ORG_XMLVM_IPHONE_CGIMAGE__
#define __ORG_XMLVM_IPHONE_CGIMAGE__

#include "xmlvm.h"
#include "org_xmlvm_iphone_NSObject.h"

// Circular references:
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_NSObject
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_NSObject
XMLVM_FORWARD_DECL(org_xmlvm_iphone_NSObject)
#endif
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_CGSize
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_CGSize
XMLVM_FORWARD_DECL(org_xmlvm_iphone_CGSize)
#endif
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_CGImage
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_CGImage
XMLVM_FORWARD_DECL(org_xmlvm_iphone_CGImage)
#endif
// Class declarations for org.xmlvm.iphone.CGImage
XMLVM_DEFINE_CLASS(org_xmlvm_iphone_CGImage, 15)

//XMLVM_BEGIN_DECLARATIONS
#define __ADDITIONAL_INSTANCE_FIELDS_org_xmlvm_iphone_CGImage
//XMLVM_END_DECLARATIONS

#define __INSTANCE_FIELDS_org_xmlvm_iphone_CGImage \
    __INSTANCE_FIELDS_org_xmlvm_iphone_NSObject; \
    struct { \
        __ADDITIONAL_INSTANCE_FIELDS_org_xmlvm_iphone_CGImage \
    } org_xmlvm_iphone_CGImage

struct org_xmlvm_iphone_CGImage {
    __TIB_DEFINITION_org_xmlvm_iphone_CGImage* tib;
    struct {
        __INSTANCE_FIELDS_org_xmlvm_iphone_CGImage;
    } fields;
};
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_CGImage
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_CGImage
typedef struct org_xmlvm_iphone_CGImage org_xmlvm_iphone_CGImage;
#endif

#define XMLVM_VTABLE_SIZE_org_xmlvm_iphone_CGImage 15
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_CGImage_getSize__ 14

void __INIT_org_xmlvm_iphone_CGImage();
JAVA_OBJECT __NEW_org_xmlvm_iphone_CGImage();
JAVA_OBJECT __NEW_INSTANCE_org_xmlvm_iphone_CGImage();
JAVA_OBJECT org_xmlvm_iphone_CGImage_createWithImageInRect___org_xmlvm_iphone_CGImage_org_xmlvm_iphone_CGRect(JAVA_OBJECT n1, JAVA_OBJECT n2);
JAVA_OBJECT org_xmlvm_iphone_CGImage_createPNGFromDataProvider___org_xmlvm_iphone_CGDataProvider(JAVA_OBJECT n1);
// Vtable index: 14
JAVA_OBJECT org_xmlvm_iphone_CGImage_getSize__(JAVA_OBJECT me);

#endif
