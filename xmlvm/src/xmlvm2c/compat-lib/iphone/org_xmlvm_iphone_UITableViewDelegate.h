#ifndef __ORG_XMLVM_IPHONE_UITABLEVIEWDELEGATE__
#define __ORG_XMLVM_IPHONE_UITABLEVIEWDELEGATE__

#include "xmlvm.h"
#include "org_xmlvm_iphone_NSObject.h"

// Circular references:
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_NSObject
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_NSObject
XMLVM_FORWARD_DECL(org_xmlvm_iphone_NSObject)
#endif
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_UITableViewCell
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_UITableViewCell
XMLVM_FORWARD_DECL(org_xmlvm_iphone_UITableViewCell)
#endif
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_UITableView
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_UITableView
XMLVM_FORWARD_DECL(org_xmlvm_iphone_UITableView)
#endif
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_NSIndexPath
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_NSIndexPath
XMLVM_FORWARD_DECL(org_xmlvm_iphone_NSIndexPath)
#endif
// Class declarations for org.xmlvm.iphone.UITableViewDelegate
XMLVM_DEFINE_CLASS(org_xmlvm_iphone_UITableViewDelegate, 7, XMLVM_ITABLE_SIZE_org_xmlvm_iphone_UITableViewDelegate)

extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_UITableViewDelegate;
extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_UITableViewDelegate_1ARRAY;
extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_UITableViewDelegate_2ARRAY;
extern JAVA_OBJECT __CLASS_org_xmlvm_iphone_UITableViewDelegate_3ARRAY;
//XMLVM_BEGIN_DECLARATIONS

#import <UIKit/UIKit.h>

#define __ADDITIONAL_INSTANCE_FIELDS_org_xmlvm_iphone_UITableViewDelegate

@interface UITableViewDelegateWrapper : DelegateWrapper <UITableViewDelegate>
{
    @public JAVA_OBJECT delegate_;
}

- (id) initWithDelegate: (JAVA_OBJECT) d_;
- (CGFloat) tableView: (UITableView*) tableView heightForRowAtIndexPath: (NSIndexPath*) indexPath;
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath;
@end

//XMLVM_END_DECLARATIONS

#define __INSTANCE_FIELDS_org_xmlvm_iphone_UITableViewDelegate \
    __INSTANCE_FIELDS_org_xmlvm_iphone_NSObject; \
    struct { \
        __ADDITIONAL_INSTANCE_FIELDS_org_xmlvm_iphone_UITableViewDelegate \
    } org_xmlvm_iphone_UITableViewDelegate

struct org_xmlvm_iphone_UITableViewDelegate {
    __TIB_DEFINITION_org_xmlvm_iphone_UITableViewDelegate* tib;
    struct {
        __INSTANCE_FIELDS_org_xmlvm_iphone_UITableViewDelegate;
    } fields;
};
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_UITableViewDelegate
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_UITableViewDelegate
typedef struct org_xmlvm_iphone_UITableViewDelegate org_xmlvm_iphone_UITableViewDelegate;
#endif

#define XMLVM_VTABLE_SIZE_org_xmlvm_iphone_UITableViewDelegate 7

void __INIT_org_xmlvm_iphone_UITableViewDelegate();
void __INIT_IMPL_org_xmlvm_iphone_UITableViewDelegate();
void __DELETE_org_xmlvm_iphone_UITableViewDelegate(void* me, void* client_data);
void __INIT_INSTANCE_MEMBERS_org_xmlvm_iphone_UITableViewDelegate(JAVA_OBJECT me, int derivedClassWillRegisterFinalizer);
JAVA_OBJECT __NEW_org_xmlvm_iphone_UITableViewDelegate();
JAVA_OBJECT __NEW_INSTANCE_org_xmlvm_iphone_UITableViewDelegate();
void org_xmlvm_iphone_UITableViewDelegate___INIT___(JAVA_OBJECT me);
void org_xmlvm_iphone_UITableViewDelegate_willDisplayCellForRowAtIndexPath___org_xmlvm_iphone_UITableView_org_xmlvm_iphone_UITableViewCell_org_xmlvm_iphone_NSIndexPath(JAVA_OBJECT me, JAVA_OBJECT n1, JAVA_OBJECT n2, JAVA_OBJECT n3);
void org_xmlvm_iphone_UITableViewDelegate_accessoryButtonTappedForRowWithIndexPath___org_xmlvm_iphone_UITableView_org_xmlvm_iphone_NSIndexPath(JAVA_OBJECT me, JAVA_OBJECT n1, JAVA_OBJECT n2);
void org_xmlvm_iphone_UITableViewDelegate_didSelectRowAtIndexPath___org_xmlvm_iphone_UITableView_org_xmlvm_iphone_NSIndexPath(JAVA_OBJECT me, JAVA_OBJECT n1, JAVA_OBJECT n2);
void org_xmlvm_iphone_UITableViewDelegate_didDeselectRowAtIndexPath___org_xmlvm_iphone_UITableView_org_xmlvm_iphone_NSIndexPath(JAVA_OBJECT me, JAVA_OBJECT n1, JAVA_OBJECT n2);
void org_xmlvm_iphone_UITableViewDelegate_willBeginEditingRowAtIndexPath___org_xmlvm_iphone_UITableView_org_xmlvm_iphone_NSIndexPath(JAVA_OBJECT me, JAVA_OBJECT n1, JAVA_OBJECT n2);
void org_xmlvm_iphone_UITableViewDelegate_didEndEditingRowAtIndexPath___org_xmlvm_iphone_UITableView_org_xmlvm_iphone_NSIndexPath(JAVA_OBJECT me, JAVA_OBJECT n1, JAVA_OBJECT n2);
JAVA_BOOLEAN org_xmlvm_iphone_UITableViewDelegate_shouldIndentWhileEditingRowAtIndexPath___org_xmlvm_iphone_UITableView_org_xmlvm_iphone_NSIndexPath(JAVA_OBJECT me, JAVA_OBJECT n1, JAVA_OBJECT n2);
JAVA_INT org_xmlvm_iphone_UITableViewDelegate_editingStyleForRowAtIndexPath___org_xmlvm_iphone_UITableView_org_xmlvm_iphone_NSIndexPath(JAVA_OBJECT me, JAVA_OBJECT n1, JAVA_OBJECT n2);
JAVA_FLOAT org_xmlvm_iphone_UITableViewDelegate_heightForRowAtIndexPath___org_xmlvm_iphone_UITableView_org_xmlvm_iphone_NSIndexPath(JAVA_OBJECT me, JAVA_OBJECT n1, JAVA_OBJECT n2);
JAVA_OBJECT org_xmlvm_iphone_UITableViewDelegate_viewForHeaderInSection___org_xmlvm_iphone_UITableView_int(JAVA_OBJECT me, JAVA_OBJECT n1, JAVA_INT n2);
JAVA_OBJECT org_xmlvm_iphone_UITableViewDelegate_viewForFooterInSection___org_xmlvm_iphone_UITableView_int(JAVA_OBJECT me, JAVA_OBJECT n1, JAVA_INT n2);
JAVA_FLOAT org_xmlvm_iphone_UITableViewDelegate_heightForHeaderInSection___org_xmlvm_iphone_UITableView_int(JAVA_OBJECT me, JAVA_OBJECT n1, JAVA_INT n2);
JAVA_FLOAT org_xmlvm_iphone_UITableViewDelegate_heightForFooterInSection___org_xmlvm_iphone_UITableView_int(JAVA_OBJECT me, JAVA_OBJECT n1, JAVA_INT n2);
JAVA_OBJECT org_xmlvm_iphone_UITableViewDelegate_titleForDeleteConfirmationButtonForRowAtIndexPath___org_xmlvm_iphone_UITableView_org_xmlvm_iphone_NSIndexPath(JAVA_OBJECT me, JAVA_OBJECT n1, JAVA_OBJECT n2);

#endif
