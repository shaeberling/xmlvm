#ifndef __ORG_XMLVM_IPHONE_UISEGMENTEDCONTROL__
#define __ORG_XMLVM_IPHONE_UISEGMENTEDCONTROL__

#include "xmlvm.h"
#include "org_xmlvm_iphone_UIControl.h"

// Circular references:
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_UIImage
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_UIImage
XMLVM_FORWARD_DECL(org_xmlvm_iphone_UIImage)
#endif
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_UIEvent
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_UIEvent
XMLVM_FORWARD_DECL(org_xmlvm_iphone_UIEvent)
#endif
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_UIControl
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_UIControl
XMLVM_FORWARD_DECL(org_xmlvm_iphone_UIControl)
#endif
#ifndef XMLVM_FORWARD_DECL_java_util_ArrayList
#define XMLVM_FORWARD_DECL_java_util_ArrayList
XMLVM_FORWARD_DECL(java_util_ArrayList)
#endif
#ifndef XMLVM_FORWARD_DECL_java_util_Set
#define XMLVM_FORWARD_DECL_java_util_Set
XMLVM_FORWARD_DECL(java_util_Set)
#endif
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_CGRect
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_CGRect
XMLVM_FORWARD_DECL(org_xmlvm_iphone_CGRect)
#endif
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_UIColor
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_UIColor
XMLVM_FORWARD_DECL(org_xmlvm_iphone_UIColor)
#endif
#ifndef XMLVM_FORWARD_DECL_java_lang_String
#define XMLVM_FORWARD_DECL_java_lang_String
XMLVM_FORWARD_DECL(java_lang_String)
#endif
// Class declarations for org.xmlvm.iphone.UISegmentedControl
XMLVM_DEFINE_CLASS(org_xmlvm_iphone_UISegmentedControl, 86)

extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_UISegmentedControl;
extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_UISegmentedControl_ARRAYTYPE;

//XMLVM_BEGIN_DECLARATIONS
#define __ADDITIONAL_INSTANCE_FIELDS_org_xmlvm_iphone_UISegmentedControl
//XMLVM_END_DECLARATIONS

#define __INSTANCE_FIELDS_org_xmlvm_iphone_UISegmentedControl \
    __INSTANCE_FIELDS_org_xmlvm_iphone_UIControl; \
    struct { \
        __ADDITIONAL_INSTANCE_FIELDS_org_xmlvm_iphone_UISegmentedControl \
    } org_xmlvm_iphone_UISegmentedControl

struct org_xmlvm_iphone_UISegmentedControl {
    __TIB_DEFINITION_org_xmlvm_iphone_UISegmentedControl* tib;
    struct {
        __INSTANCE_FIELDS_org_xmlvm_iphone_UISegmentedControl;
    } fields;
};
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_UISegmentedControl
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_UISegmentedControl
typedef struct org_xmlvm_iphone_UISegmentedControl org_xmlvm_iphone_UISegmentedControl;
#endif

#define XMLVM_VTABLE_SIZE_org_xmlvm_iphone_UISegmentedControl 86
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_UISegmentedControl_setTitle___java_lang_String_int 71
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_UISegmentedControl_getTitleForSegmentAtIndex___int 72
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_UISegmentedControl_insertSegmentWithTitle___java_lang_String_int_boolean 73
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_UISegmentedControl_insertSegmentWithImage___org_xmlvm_iphone_UIImage_int_boolean 74
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_UISegmentedControl_numberOfSegments__ 75
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_UISegmentedControl_removeAllSegments__ 76
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_UISegmentedControl_removeSegmentAtIndex___int_boolean 77
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_UISegmentedControl_getSelectedSegmentIndex__ 78
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_UISegmentedControl_setSelectedSegmentIndex___int 79
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_UISegmentedControl_getSegmentedControlStyle__ 80
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_UISegmentedControl_setSegmentedControlStyle___int 81
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_UISegmentedControl_getTintColor__ 82
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_UISegmentedControl_setTintColor___org_xmlvm_iphone_UIColor 83
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_UISegmentedControl_isMomentary__ 84
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_UISegmentedControl_setMomentary___boolean 85
#define XMLVM_VTABLE_IDX_org_xmlvm_iphone_UISegmentedControl_touchesEnded___java_util_Set_org_xmlvm_iphone_UIEvent 16

void __INIT_org_xmlvm_iphone_UISegmentedControl();
JAVA_OBJECT __NEW_org_xmlvm_iphone_UISegmentedControl();
JAVA_OBJECT __NEW_INSTANCE_org_xmlvm_iphone_UISegmentedControl();
void org_xmlvm_iphone_UISegmentedControl___INIT___(JAVA_OBJECT me);
void org_xmlvm_iphone_UISegmentedControl___INIT____org_xmlvm_iphone_CGRect(JAVA_OBJECT me, JAVA_OBJECT n1);
void org_xmlvm_iphone_UISegmentedControl___INIT____java_util_ArrayList(JAVA_OBJECT me, JAVA_OBJECT n1);
// Vtable index: 71
void org_xmlvm_iphone_UISegmentedControl_setTitle___java_lang_String_int(JAVA_OBJECT me, JAVA_OBJECT n1, JAVA_INT n2);
// Vtable index: 72
JAVA_OBJECT org_xmlvm_iphone_UISegmentedControl_getTitleForSegmentAtIndex___int(JAVA_OBJECT me, JAVA_INT n1);
// Vtable index: 73
void org_xmlvm_iphone_UISegmentedControl_insertSegmentWithTitle___java_lang_String_int_boolean(JAVA_OBJECT me, JAVA_OBJECT n1, JAVA_INT n2, JAVA_BOOLEAN n3);
// Vtable index: 74
void org_xmlvm_iphone_UISegmentedControl_insertSegmentWithImage___org_xmlvm_iphone_UIImage_int_boolean(JAVA_OBJECT me, JAVA_OBJECT n1, JAVA_INT n2, JAVA_BOOLEAN n3);
// Vtable index: 75
JAVA_INT org_xmlvm_iphone_UISegmentedControl_numberOfSegments__(JAVA_OBJECT me);
// Vtable index: 76
void org_xmlvm_iphone_UISegmentedControl_removeAllSegments__(JAVA_OBJECT me);
// Vtable index: 77
void org_xmlvm_iphone_UISegmentedControl_removeSegmentAtIndex___int_boolean(JAVA_OBJECT me, JAVA_INT n1, JAVA_BOOLEAN n2);
// Vtable index: 78
JAVA_INT org_xmlvm_iphone_UISegmentedControl_getSelectedSegmentIndex__(JAVA_OBJECT me);
// Vtable index: 79
void org_xmlvm_iphone_UISegmentedControl_setSelectedSegmentIndex___int(JAVA_OBJECT me, JAVA_INT n1);
// Vtable index: 80
JAVA_INT org_xmlvm_iphone_UISegmentedControl_getSegmentedControlStyle__(JAVA_OBJECT me);
// Vtable index: 81
void org_xmlvm_iphone_UISegmentedControl_setSegmentedControlStyle___int(JAVA_OBJECT me, JAVA_INT n1);
// Vtable index: 82
JAVA_OBJECT org_xmlvm_iphone_UISegmentedControl_getTintColor__(JAVA_OBJECT me);
// Vtable index: 83
void org_xmlvm_iphone_UISegmentedControl_setTintColor___org_xmlvm_iphone_UIColor(JAVA_OBJECT me, JAVA_OBJECT n1);
// Vtable index: 84
JAVA_BOOLEAN org_xmlvm_iphone_UISegmentedControl_isMomentary__(JAVA_OBJECT me);
// Vtable index: 85
void org_xmlvm_iphone_UISegmentedControl_setMomentary___boolean(JAVA_OBJECT me, JAVA_BOOLEAN n1);
// Vtable index: 16
void org_xmlvm_iphone_UISegmentedControl_touchesEnded___java_util_Set_org_xmlvm_iphone_UIEvent(JAVA_OBJECT me, JAVA_OBJECT n1, JAVA_OBJECT n2);

#endif
