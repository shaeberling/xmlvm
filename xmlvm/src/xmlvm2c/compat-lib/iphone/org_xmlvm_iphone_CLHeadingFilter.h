#ifndef __ORG_XMLVM_IPHONE_CLHEADINGFILTER__
#define __ORG_XMLVM_IPHONE_CLHEADINGFILTER__

#include "xmlvm.h"
#include "java_lang_Object.h"

// Circular references:
#ifndef XMLVM_FORWARD_DECL_java_lang_Object
#define XMLVM_FORWARD_DECL_java_lang_Object
XMLVM_FORWARD_DECL(java_lang_Object)
#endif
// Class declarations for org.xmlvm.iphone.CLHeadingFilter
XMLVM_DEFINE_CLASS(org_xmlvm_iphone_CLHeadingFilter, 11)

//XMLVM_BEGIN_MEMBERS
#define __ADDITIONAL_INSTANCE_MEMBERS_org_xmlvm_iphone_CLHeadingFilter
//XMLVM_END_MEMBERS

#define __INSTANCE_MEMBERS_org_xmlvm_iphone_CLHeadingFilter \
    __INSTANCE_MEMBERS_java_lang_Object; \
    struct { \
        __ADDITIONAL_INSTANCE_MEMBERS_org_xmlvm_iphone_CLHeadingFilter \
    } org_xmlvm_iphone_CLHeadingFilter

struct org_xmlvm_iphone_CLHeadingFilter {
    __CLASS_DEFINITION_org_xmlvm_iphone_CLHeadingFilter* __class;
    __INSTANCE_MEMBERS_org_xmlvm_iphone_CLHeadingFilter;
};
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_CLHeadingFilter
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_CLHeadingFilter
typedef struct org_xmlvm_iphone_CLHeadingFilter org_xmlvm_iphone_CLHeadingFilter;
#endif

#define XMLVM_VTABLE_SIZE_org_xmlvm_iphone_CLHeadingFilter 11

void __INIT_org_xmlvm_iphone_CLHeadingFilter();
JAVA_OBJECT __NEW_org_xmlvm_iphone_CLHeadingFilter();
JAVA_OBJECT __NEW_INSTANCE_org_xmlvm_iphone_CLHeadingFilter();
JAVA_DOUBLE org_xmlvm_iphone_CLHeadingFilter_GET_None();
void org_xmlvm_iphone_CLHeadingFilter_PUT_None(JAVA_DOUBLE v);

#endif