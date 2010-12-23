#ifndef __ORG_XMLVM_IPHONE_SKPAYMENTTRANSACTIONSTATE__
#define __ORG_XMLVM_IPHONE_SKPAYMENTTRANSACTIONSTATE__

#include "xmlvm.h"
#include "java_lang_Object.h"

// Circular references:
#ifndef XMLVM_FORWARD_DECL_java_lang_Object
#define XMLVM_FORWARD_DECL_java_lang_Object
XMLVM_FORWARD_DECL(java_lang_Object)
#endif
// Class declarations for org.xmlvm.iphone.SKPaymentTransactionState
XMLVM_DEFINE_CLASS(org_xmlvm_iphone_SKPaymentTransactionState, 11)

//XMLVM_BEGIN_FIELDS
#define __ADDITIONAL_INSTANCE_FIELDS_org_xmlvm_iphone_SKPaymentTransactionState
//XMLVM_END_FIELDS

#define __INSTANCE_FIELDS_org_xmlvm_iphone_SKPaymentTransactionState \
    __INSTANCE_FIELDS_java_lang_Object; \
    struct { \
        __ADDITIONAL_INSTANCE_FIELDS_org_xmlvm_iphone_SKPaymentTransactionState \
    } org_xmlvm_iphone_SKPaymentTransactionState

struct org_xmlvm_iphone_SKPaymentTransactionState {
    __TIB_DEFINITION_org_xmlvm_iphone_SKPaymentTransactionState* tib;
    struct {
        __INSTANCE_FIELDS_org_xmlvm_iphone_SKPaymentTransactionState;
    } fields;
};
#ifndef XMLVM_FORWARD_DECL_org_xmlvm_iphone_SKPaymentTransactionState
#define XMLVM_FORWARD_DECL_org_xmlvm_iphone_SKPaymentTransactionState
typedef struct org_xmlvm_iphone_SKPaymentTransactionState org_xmlvm_iphone_SKPaymentTransactionState;
#endif

#define XMLVM_VTABLE_SIZE_org_xmlvm_iphone_SKPaymentTransactionState 11

void __INIT_org_xmlvm_iphone_SKPaymentTransactionState();
JAVA_OBJECT __NEW_org_xmlvm_iphone_SKPaymentTransactionState();
JAVA_OBJECT __NEW_INSTANCE_org_xmlvm_iphone_SKPaymentTransactionState();
JAVA_INT org_xmlvm_iphone_SKPaymentTransactionState_GET_Purchasing();
void org_xmlvm_iphone_SKPaymentTransactionState_PUT_Purchasing(JAVA_INT v);
JAVA_INT org_xmlvm_iphone_SKPaymentTransactionState_GET_Purchased();
void org_xmlvm_iphone_SKPaymentTransactionState_PUT_Purchased(JAVA_INT v);
JAVA_INT org_xmlvm_iphone_SKPaymentTransactionState_GET_Failed();
void org_xmlvm_iphone_SKPaymentTransactionState_PUT_Failed(JAVA_INT v);
JAVA_INT org_xmlvm_iphone_SKPaymentTransactionState_GET_Restored();
void org_xmlvm_iphone_SKPaymentTransactionState_PUT_Restored(JAVA_INT v);
void org_xmlvm_iphone_SKPaymentTransactionState___INIT___(JAVA_OBJECT me);

#endif
