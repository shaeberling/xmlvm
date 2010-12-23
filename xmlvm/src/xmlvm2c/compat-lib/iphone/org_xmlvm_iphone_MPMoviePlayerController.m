#include "org_xmlvm_iphone_NSURL.h"
#include "org_xmlvm_iphone_UIColor.h"

#include "org_xmlvm_iphone_MPMoviePlayerController.h"

__TIB_DEFINITION_org_xmlvm_iphone_MPMoviePlayerController __TIB_org_xmlvm_iphone_MPMoviePlayerController = {
    0, // classInitialized
    "org.xmlvm.iphone.MPMoviePlayerController", // className
    (__TIB_DEFINITION_TEMPLATE*) &__TIB_org_xmlvm_iphone_NSObject, // extends
};

//XMLVM_BEGIN_IMPLEMENTATION
//XMLVM_END_IMPLEMENTATION


void __INIT_org_xmlvm_iphone_MPMoviePlayerController()
{
    __TIB_org_xmlvm_iphone_MPMoviePlayerController.classInitialized = 1;
    // Initialize base class if necessary
    if (!__TIB_org_xmlvm_iphone_NSObject.classInitialized) __INIT_org_xmlvm_iphone_NSObject();
    __TIB_org_xmlvm_iphone_MPMoviePlayerController.newInstanceFunc = __NEW_INSTANCE_org_xmlvm_iphone_MPMoviePlayerController;
    // Copy vtable from base class
    XMLVM_MEMCPY(__TIB_org_xmlvm_iphone_MPMoviePlayerController.vtable, __TIB_org_xmlvm_iphone_NSObject.vtable, sizeof(__TIB_org_xmlvm_iphone_NSObject.vtable));
    // Initialize vtable for this class
    __TIB_org_xmlvm_iphone_MPMoviePlayerController.vtable[14] = (VTABLE_PTR) &org_xmlvm_iphone_MPMoviePlayerController_play__;
    __TIB_org_xmlvm_iphone_MPMoviePlayerController.vtable[15] = (VTABLE_PTR) &org_xmlvm_iphone_MPMoviePlayerController_stop__;
    __TIB_org_xmlvm_iphone_MPMoviePlayerController.vtable[16] = (VTABLE_PTR) &org_xmlvm_iphone_MPMoviePlayerController_getContentURL__;
    __TIB_org_xmlvm_iphone_MPMoviePlayerController.vtable[17] = (VTABLE_PTR) &org_xmlvm_iphone_MPMoviePlayerController_getBackgroundColor__;
    __TIB_org_xmlvm_iphone_MPMoviePlayerController.vtable[18] = (VTABLE_PTR) &org_xmlvm_iphone_MPMoviePlayerController_setBackgroundColor___org_xmlvm_iphone_UIColor;
    __TIB_org_xmlvm_iphone_MPMoviePlayerController.vtable[19] = (VTABLE_PTR) &org_xmlvm_iphone_MPMoviePlayerController_getInitialPlaybackTime__;
    __TIB_org_xmlvm_iphone_MPMoviePlayerController.vtable[20] = (VTABLE_PTR) &org_xmlvm_iphone_MPMoviePlayerController_setInitialPlaybackTime___double;
    __TIB_org_xmlvm_iphone_MPMoviePlayerController.vtable[21] = (VTABLE_PTR) &org_xmlvm_iphone_MPMoviePlayerController_getScalingMode__;
    __TIB_org_xmlvm_iphone_MPMoviePlayerController.vtable[22] = (VTABLE_PTR) &org_xmlvm_iphone_MPMoviePlayerController_setScalingMode___int;
    __TIB_org_xmlvm_iphone_MPMoviePlayerController.vtable[23] = (VTABLE_PTR) &org_xmlvm_iphone_MPMoviePlayerController_getMovieControlMode__;
    __TIB_org_xmlvm_iphone_MPMoviePlayerController.vtable[24] = (VTABLE_PTR) &org_xmlvm_iphone_MPMoviePlayerController_setMovieControlMode___int;
    // Initialize vtable for implementing interfaces
    __TIB_org_xmlvm_iphone_MPMoviePlayerController.numImplementedInterfaces = 0;
    __TIB_org_xmlvm_iphone_MPMoviePlayerController.implementedInterfaces = (__TIB_DEFINITION_TEMPLATE* (*)[1]) XMLVM_MALLOC(sizeof(__TIB_DEFINITION_TEMPLATE*) * 0);


    //XMLVM_BEGIN_WRAPPER[__INIT_org_xmlvm_iphone_MPMoviePlayerController]
    //XMLVM_END_WRAPPER
}

void __DELETE_org_xmlvm_iphone_MPMoviePlayerController(void* me, void* client_data)
{
    //XMLVM_BEGIN_WRAPPER[__DELETE_org_xmlvm_iphone_MPMoviePlayerController]
    //XMLVM_END_WRAPPER
}

JAVA_OBJECT __NEW_org_xmlvm_iphone_MPMoviePlayerController()
{
    if (!__TIB_org_xmlvm_iphone_MPMoviePlayerController.classInitialized) __INIT_org_xmlvm_iphone_MPMoviePlayerController();
    org_xmlvm_iphone_MPMoviePlayerController* me = (org_xmlvm_iphone_MPMoviePlayerController*) XMLVM_MALLOC(sizeof(org_xmlvm_iphone_MPMoviePlayerController));
    me->tib = &__TIB_org_xmlvm_iphone_MPMoviePlayerController;
    //XMLVM_BEGIN_WRAPPER[__NEW_org_xmlvm_iphone_MPMoviePlayerController]
    //XMLVM_END_WRAPPER
    return me;
}

JAVA_OBJECT __NEW_INSTANCE_org_xmlvm_iphone_MPMoviePlayerController()
{
    JAVA_OBJECT me = JAVA_NULL;
    return me;
}

JAVA_OBJECT org_xmlvm_iphone_MPMoviePlayerController_initWithContentURL___org_xmlvm_iphone_NSURL(JAVA_OBJECT n1)
{
    if (!__TIB_org_xmlvm_iphone_MPMoviePlayerController.classInitialized) __INIT_org_xmlvm_iphone_MPMoviePlayerController();
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_MPMoviePlayerController_initWithContentURL___org_xmlvm_iphone_NSURL]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

void org_xmlvm_iphone_MPMoviePlayerController_play__(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_MPMoviePlayerController_play__]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

void org_xmlvm_iphone_MPMoviePlayerController_stop__(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_MPMoviePlayerController_stop__]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

JAVA_OBJECT org_xmlvm_iphone_MPMoviePlayerController_getContentURL__(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_MPMoviePlayerController_getContentURL__]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

JAVA_OBJECT org_xmlvm_iphone_MPMoviePlayerController_getBackgroundColor__(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_MPMoviePlayerController_getBackgroundColor__]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

void org_xmlvm_iphone_MPMoviePlayerController_setBackgroundColor___org_xmlvm_iphone_UIColor(JAVA_OBJECT me, JAVA_OBJECT n1)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_MPMoviePlayerController_setBackgroundColor___org_xmlvm_iphone_UIColor]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

JAVA_DOUBLE org_xmlvm_iphone_MPMoviePlayerController_getInitialPlaybackTime__(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_MPMoviePlayerController_getInitialPlaybackTime__]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

void org_xmlvm_iphone_MPMoviePlayerController_setInitialPlaybackTime___double(JAVA_OBJECT me, JAVA_DOUBLE n1)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_MPMoviePlayerController_setInitialPlaybackTime___double]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

JAVA_INT org_xmlvm_iphone_MPMoviePlayerController_getScalingMode__(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_MPMoviePlayerController_getScalingMode__]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

void org_xmlvm_iphone_MPMoviePlayerController_setScalingMode___int(JAVA_OBJECT me, JAVA_INT n1)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_MPMoviePlayerController_setScalingMode___int]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

JAVA_INT org_xmlvm_iphone_MPMoviePlayerController_getMovieControlMode__(JAVA_OBJECT me)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_MPMoviePlayerController_getMovieControlMode__]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

void org_xmlvm_iphone_MPMoviePlayerController_setMovieControlMode___int(JAVA_OBJECT me, JAVA_INT n1)
{
    //XMLVM_BEGIN_WRAPPER[org_xmlvm_iphone_MPMoviePlayerController_setMovieControlMode___int]
    XMLVM_NOT_IMPLEMENTED();
    //XMLVM_END_WRAPPER
}

