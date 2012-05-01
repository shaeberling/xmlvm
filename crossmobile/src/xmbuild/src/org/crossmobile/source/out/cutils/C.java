package org.crossmobile.source.out.cutils;

public class C {

    public static final String T                       = "    ";
    public static final String TT                      = T + T;
    public static final String TTT                     = T + TT;
    public static final String N                       = "\n";
    public static final String NT                      = N + T;
    public static final String NTT                     = N + TT;
    public static final String NTTT                    = N + TTT;

    public static final String BEGIN_DECL              = N + "//XMLVM_BEGIN_DECLARATIONS";
    public static final String END_DECL                = "//XMLVM_END_DECLARATIONS";

    public static final String BEGIN_WRAPPER           = N + "//XMLVM_BEGIN_WRAPPER";
    public static final String END_WRAPPER             = "//XMLVM_END_WRAPPER";
    public static final String NOT_IMPLEMENTED         = N + "XMLVM_NOT_IMPLEMENTED();";
    public static final String BEGIN_IMPL              = N + "//XMLVM_BEGIN_IMPLEMENTATION";
    public static final String END_IMPL                = "//XMLVM_END_IMPLEMENTATION";

    public static final String XMLVM_VAR_THIZ          = NT + "XMLVM_VAR_THIZ;";
    public static final String AUTORELEASEPOOL_ALLOC   = NT
                                                               + "NSAutoreleasePool* p = [[NSAutoreleasePool alloc] init];"
                                                               + N;
    public static final String AUTORELEASEPOOL_RELEASE = T + "[ p release];" + N;
    public static final String XMLVM_VAR_CFTHIZ        = NT + "XMLVM_VAR_CFTHIZ;";
   // public static final String XMLVM_VAR_IOS_REF = NT + "XMLVM_VAR_IOS_REF";
}
