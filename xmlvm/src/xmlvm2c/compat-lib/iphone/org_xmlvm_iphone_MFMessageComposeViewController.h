#ifndef __ORG_XMLVM_IPHONE_MFMESSAGECOMPOSEVIEWCONTROLLER__
#define __ORG_XMLVM_IPHONE_MFMESSAGECOMPOSEVIEWCONTROLLER__

#include "xmlvm.h"
#include "org_xmlvm_iphone_UINavigationController.h"

// Circular references:
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_MFMessageComposeViewControllerDelegate
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_MFMessageComposeViewControllerDelegate
XMLVM_FORWARD_DECL(org_xmlvm_iphone_MFMessageComposeViewControllerDelegate)
#endif
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_UINavigationController
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_UINavigationController
XMLVM_FORWARD_DECL(org_xmlvm_iphone_UINavigationController)
#endif
#ifndef XMLVM_FORWARD_DECL_java_util_ArrayList
#define XMLVM_FORWARD_DECL_java_util_ArrayList
XMLVM_FORWARD_DECL(java_util_ArrayList)
#endif
#ifndef XMLVM_FORWARD_DECL_java_lang_String
#define XMLVM_FORWARD_DECL_java_lang_String
XMLVM_FORWARD_DECL(java_lang_String)
#endif
// Class declarations for org.xmlvm.iphone.MFMessageComposeViewController
XMLVM_DEFINE_CLASS(org_xmlvm_iphone_MFMessageComposeViewController, 14, XMLVM_ITABLE_SIZE_org_xmlvm_iphone_MFMessageComposeViewController)

extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_MFMessageComposeViewController;
extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_MFMessageComposeViewController_1ARRAY;
extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_MFMessageComposeViewController_2ARRAY;
extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_MFMessageComposeViewController_3ARRAY;
//XMLVM_BEGIN_DECLARATIONS
#define __ADDITIONAL_INSTANCE_FIELDS_org_xmlvm_iphone_MFMessageComposeViewController
//XMLVM_END_DECLARATIONS

#define __INSTANCE_FIELDS_org_xmlvm_iphone_MFMessageComposeViewController \
    __INSTANCE_FIELDS_org_xmlvm_iphone_UINavigationController; \
    struct { \
        __ADDITIONAL_INSTANCE_FIELDS_org_xmlvm_iphone_MFMessageComposeViewController \
    } org_xmlvm_iphone_MFMessageComposeViewController

struct org_xmlvm_iphone_MFMessageComposeViewController {
    __TIB_DEFINITION_org_xmlvm_iphone_MFMessageComposeViewController* tib;
    struct {
        __INSTANCE_FIELDS_org_xmlvm_iphone_MFMessageComposeViewController;
    } fields;
};
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_MFMessageComposeViewController
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_MFMessageComposeViewController
typedef struct org_xmlvm_iphone_MFMessageComposeViewController org_xmlvm_iphone_MFMessageComposeViewController;
#endif

#define XMLVM_VTABLE_SIZE_org_xmlvm_iphone_MFMessageComposeViewController 14

void __INIT_org_xmlvm_iphone_MFMessageComposeViewController();
void __INIT_IMPL_org_xmlvm_iphone_MFMessageComposeViewController();
void __DELETE_org_xmlvm_iphone_MFMessageComposeViewController(void* me, void* client_data);
void __INIT_INSTANCE_MEMBERS_org_xmlvm_iphone_MFMessageComposeViewController(JAVA_OBJECT me, int derivedClassWillRegisterFinalizer);
JAVA_OBJECT __NEW_org_xmlvm_iphone_MFMessageComposeViewController();
JAVA_OBJECT __NEW_INSTANCE_org_xmlvm_iphone_MFMessageComposeViewController();
void org_xmlvm_iphone_MFMessageComposeViewController___INIT___(JAVA_OBJECT me);
JAVA_BOOLEAN org_xmlvm_iphone_MFMessageComposeViewController_canSendText__();
JAVA_OBJECT org_xmlvm_iphone_MFMessageComposeViewController_getBody__(JAVA_OBJECT me);
void org_xmlvm_iphone_MFMessageComposeViewController_setBody___java_lang_String(JAVA_OBJECT me, JAVA_OBJECT n1);
JAVA_OBJECT org_xmlvm_iphone_MFMessageComposeViewController_getRecipients__(JAVA_OBJECT me);
void org_xmlvm_iphone_MFMessageComposeViewController_setRecipients___java_util_ArrayList(JAVA_OBJECT me, JAVA_OBJECT n1);
JAVA_OBJECT org_xmlvm_iphone_MFMessageComposeViewController_getMessageComposeDelegate__(JAVA_OBJECT me);
void org_xmlvm_iphone_MFMessageComposeViewController_setMessageComposeDelegate___org_xmlvm_iphone_MFMessageComposeViewControllerDelegate(JAVA_OBJECT me, JAVA_OBJECT n1);

#endif
