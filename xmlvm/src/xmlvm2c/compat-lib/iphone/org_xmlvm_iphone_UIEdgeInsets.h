#ifndef __ORG_XMLVM_IPHONE_UIEDGEINSETS__
#define __ORG_XMLVM_IPHONE_UIEDGEINSETS__

#include "xmlvm.h"
#include "org_xmlvm_iphone_NSObject.h"

// Circular references:
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_NSObject
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_NSObject
XMLVM_FORWARD_DECL(org_xmlvm_iphone_NSObject)
#endif
// Class declarations for org.xmlvm.iphone.UIEdgeInsets
XMLVM_DEFINE_CLASS(org_xmlvm_iphone_UIEdgeInsets, 14)

//XMLVM_BEGIN_MEMBERS
#define __ADDITIONAL_INSTANCE_MEMBERS_org_xmlvm_iphone_UIEdgeInsets
//XMLVM_END_MEMBERS

#define __INSTANCE_MEMBERS_org_xmlvm_iphone_UIEdgeInsets \
    __INSTANCE_MEMBERS_org_xmlvm_iphone_NSObject; \
    struct { \
        JAVA_FLOAT top_; \
        JAVA_FLOAT left_; \
        JAVA_FLOAT bottom_; \
        JAVA_FLOAT right_; \
        __ADDITIONAL_INSTANCE_MEMBERS_org_xmlvm_iphone_UIEdgeInsets \
    } org_xmlvm_iphone_UIEdgeInsets

struct org_xmlvm_iphone_UIEdgeInsets {
    __CLASS_DEFINITION_org_xmlvm_iphone_UIEdgeInsets* __class;
    __INSTANCE_MEMBERS_org_xmlvm_iphone_UIEdgeInsets;
};
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_UIEdgeInsets
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_UIEdgeInsets
typedef struct org_xmlvm_iphone_UIEdgeInsets org_xmlvm_iphone_UIEdgeInsets;
#endif

#define XMLVM_VTABLE_SIZE_org_xmlvm_iphone_UIEdgeInsets 14

void __INIT_org_xmlvm_iphone_UIEdgeInsets();
JAVA_OBJECT __NEW_org_xmlvm_iphone_UIEdgeInsets();
JAVA_OBJECT __NEW_INSTANCE_org_xmlvm_iphone_UIEdgeInsets();
void org_xmlvm_iphone_UIEdgeInsets___INIT___(JAVA_OBJECT me);
void org_xmlvm_iphone_UIEdgeInsets___INIT____float_float_float_float(JAVA_OBJECT me, JAVA_FLOAT n1, JAVA_FLOAT n2, JAVA_FLOAT n3, JAVA_FLOAT n4);

#endif
