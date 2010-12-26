#ifndef __ORG_XMLVM_IPHONE_MPMOVIEPLAYERCONTROLLER__
#define __ORG_XMLVM_IPHONE_MPMOVIEPLAYERCONTROLLER__

#include "xmlvm.h"
#include "org_xmlvm_iphone_NSObject.h"

// Circular references:
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_NSObject
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_NSObject
XMLVM_FORWARD_DECL(org_xmlvm_iphone_NSObject)
#endif
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_MPMoviePlayerController
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_MPMoviePlayerController
XMLVM_FORWARD_DECL(org_xmlvm_iphone_MPMoviePlayerController)
#endif
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_NSURL
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_NSURL
XMLVM_FORWARD_DECL(org_xmlvm_iphone_NSURL)
#endif
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_UIColor
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_UIColor
XMLVM_FORWARD_DECL(org_xmlvm_iphone_UIColor)
#endif
// Class declarations for org.xmlvm.iphone.MPMoviePlayerController
XMLVM_DEFINE_CLASS(org_xmlvm_iphone_MPMoviePlayerController, 25)

//XMLVM_BEGIN_DECLARATIONS
#define __ADDITIONAL_INSTANCE_FIELDS_org_xmlvm_iphone_MPMoviePlayerController
//XMLVM_END_DECLARATIONS

#define __INSTANCE_FIELDS_org_xmlvm_iphone_MPMoviePlayerController \
    __INSTANCE_FIELDS_org_xmlvm_iphone_NSObject; \
    struct { \
        __ADDITIONAL_INSTANCE_FIELDS_org_xmlvm_iphone_MPMoviePlayerController \
    } org_xmlvm_iphone_MPMoviePlayerController

struct org_xmlvm_iphone_MPMoviePlayerController {
    __TIB_DEFINITION_org_xmlvm_iphone_MPMoviePlayerController* tib;
    struct {
        __INSTANCE_FIELDS_org_xmlvm_iphone_MPMoviePlayerController;
    } fields;
};
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_MPMoviePlayerController
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_MPMoviePlayerController
typedef struct org_xmlvm_iphone_MPMoviePlayerController org_xmlvm_iphone_MPMoviePlayerController;
#endif

#define XMLVM_VTABLE_SIZE_org_xmlvm_iphone_MPMoviePlayerController 25
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_MPMoviePlayerController_play__ 14
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_MPMoviePlayerController_stop__ 15
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_MPMoviePlayerController_getContentURL__ 16
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_MPMoviePlayerController_getBackgroundColor__ 17
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_MPMoviePlayerController_setBackgroundColor___org_xmlvm_iphone_UIColor 18
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_MPMoviePlayerController_getInitialPlaybackTime__ 19
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_MPMoviePlayerController_setInitialPlaybackTime___double 20
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_MPMoviePlayerController_getScalingMode__ 21
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_MPMoviePlayerController_setScalingMode___int 22
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_MPMoviePlayerController_getMovieControlMode__ 23
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_MPMoviePlayerController_setMovieControlMode___int 24

void __INIT_org_xmlvm_iphone_MPMoviePlayerController();
JAVA_OBJECT __NEW_org_xmlvm_iphone_MPMoviePlayerController();
JAVA_OBJECT __NEW_INSTANCE_org_xmlvm_iphone_MPMoviePlayerController();
JAVA_OBJECT org_xmlvm_iphone_MPMoviePlayerController_initWithContentURL___org_xmlvm_iphone_NSURL(JAVA_OBJECT n1);
// Vtable index: 14
void org_xmlvm_iphone_MPMoviePlayerController_play__(JAVA_OBJECT me);
// Vtable index: 15
void org_xmlvm_iphone_MPMoviePlayerController_stop__(JAVA_OBJECT me);
// Vtable index: 16
JAVA_OBJECT org_xmlvm_iphone_MPMoviePlayerController_getContentURL__(JAVA_OBJECT me);
// Vtable index: 17
JAVA_OBJECT org_xmlvm_iphone_MPMoviePlayerController_getBackgroundColor__(JAVA_OBJECT me);
// Vtable index: 18
void org_xmlvm_iphone_MPMoviePlayerController_setBackgroundColor___org_xmlvm_iphone_UIColor(JAVA_OBJECT me, JAVA_OBJECT n1);
// Vtable index: 19
JAVA_DOUBLE org_xmlvm_iphone_MPMoviePlayerController_getInitialPlaybackTime__(JAVA_OBJECT me);
// Vtable index: 20
void org_xmlvm_iphone_MPMoviePlayerController_setInitialPlaybackTime___double(JAVA_OBJECT me, JAVA_DOUBLE n1);
// Vtable index: 21
JAVA_INT org_xmlvm_iphone_MPMoviePlayerController_getScalingMode__(JAVA_OBJECT me);
// Vtable index: 22
void org_xmlvm_iphone_MPMoviePlayerController_setScalingMode___int(JAVA_OBJECT me, JAVA_INT n1);
// Vtable index: 23
JAVA_INT org_xmlvm_iphone_MPMoviePlayerController_getMovieControlMode__(JAVA_OBJECT me);
// Vtable index: 24
void org_xmlvm_iphone_MPMoviePlayerController_setMovieControlMode___int(JAVA_OBJECT me, JAVA_INT n1);

#endif
