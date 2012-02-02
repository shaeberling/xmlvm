#ifndef __ORG_XMLVM_IPHONE_MPMOVIEPLAYERVIEWCONTROLLER__
#define __ORG_XMLVM_IPHONE_MPMOVIEPLAYERVIEWCONTROLLER__

#include "xmlvm.h"

// Preprocessor constants for interfaces:
// Implemented interfaces:
// Super Class:
#include "org_xmlvm_iphone_UIViewController.h"

// Circular references:
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_MPMoviePlayerController
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_MPMoviePlayerController
XMLVM_FORWARD_DECL(org_xmlvm_iphone_MPMoviePlayerController)
#endif
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_NSURL
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_NSURL
XMLVM_FORWARD_DECL(org_xmlvm_iphone_NSURL)
#endif
// Class declarations for org.xmlvm.iphone.MPMoviePlayerViewController
XMLVM_DEFINE_CLASS(org_xmlvm_iphone_MPMoviePlayerViewController, 12, XMLVM_ITABLE_SIZE_org_xmlvm_iphone_MPMoviePlayerViewController)

extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_MPMoviePlayerViewController;
extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_MPMoviePlayerViewController_1ARRAY;
extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_MPMoviePlayerViewController_2ARRAY;
extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_MPMoviePlayerViewController_3ARRAY;
//XMLVM_BEGIN_DECLARATIONS
#define __ADDITIONAL_INSTANCE_FIELDS_org_xmlvm_iphone_MPMoviePlayerViewController
//XMLVM_END_DECLARATIONS

#define __INSTANCE_FIELDS_org_xmlvm_iphone_MPMoviePlayerViewController \
    __INSTANCE_FIELDS_org_xmlvm_iphone_UIViewController; \
    struct { \
        __ADDITIONAL_INSTANCE_FIELDS_org_xmlvm_iphone_MPMoviePlayerViewController \
    } org_xmlvm_iphone_MPMoviePlayerViewController

struct org_xmlvm_iphone_MPMoviePlayerViewController {
    __TIB_DEFINITION_org_xmlvm_iphone_MPMoviePlayerViewController* tib;
    struct {
        __INSTANCE_FIELDS_org_xmlvm_iphone_MPMoviePlayerViewController;
    } fields;
};
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_MPMoviePlayerViewController
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_MPMoviePlayerViewController
typedef struct org_xmlvm_iphone_MPMoviePlayerViewController org_xmlvm_iphone_MPMoviePlayerViewController;
#endif

#define XMLVM_VTABLE_SIZE_org_xmlvm_iphone_MPMoviePlayerViewController 12

void __INIT_org_xmlvm_iphone_MPMoviePlayerViewController();
void __INIT_IMPL_org_xmlvm_iphone_MPMoviePlayerViewController();
void __DELETE_org_xmlvm_iphone_MPMoviePlayerViewController(void* me, void* client_data);
void __INIT_INSTANCE_MEMBERS_org_xmlvm_iphone_MPMoviePlayerViewController(JAVA_OBJECT me, int derivedClassWillRegisterFinalizer);
JAVA_OBJECT __NEW_org_xmlvm_iphone_MPMoviePlayerViewController();
JAVA_OBJECT __NEW_INSTANCE_org_xmlvm_iphone_MPMoviePlayerViewController();
void org_xmlvm_iphone_MPMoviePlayerViewController___INIT____org_xmlvm_iphone_NSURL(JAVA_OBJECT me, JAVA_OBJECT n1);
JAVA_OBJECT org_xmlvm_iphone_MPMoviePlayerViewController_getMoviePlayer__(JAVA_OBJECT me);

// Define a Macro for the method declarations of the Obj-C wrapper class so that subclass wrappers may easily include these too
#define XMLVM_OBJC_OVERRIDE_CLASS_DECLARATIONS_org_xmlvm_iphone_MPMoviePlayerViewController \
XMLVM_OBJC_OVERRIDE_CLASS_DECLARATIONS_org_xmlvm_iphone_UIViewController \

// Define a Macro for the entire contents of the Obj-C wrapper class
#define XMLVM_OBJC_OVERRIDE_CLASS_DEFINITIONS_org_xmlvm_iphone_MPMoviePlayerViewController \
XMLVM_OBJC_OVERRIDE_CLASS_DEFINITIONS_org_xmlvm_iphone_UIViewController \


#endif
