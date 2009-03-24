<?xml version="1.0"?>


<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs = "http://www.w3.org/2001/XMLSchema"
                xmlns:vm ="http://xmlvm.org"
                xmlns:jvm="http://xmlvm.org/jvm"
                version="2.0">

<xsl:param name="pass">emitHeader</xsl:param>
<xsl:param name="header">default.h</xsl:param>

<xsl:output method="text" indent="no"/>

<xsl:template match="vm:xmlvm">
  <xsl:text>
// Automatically generated by xmlvm2obj. Do not edit!

</xsl:text>  




  <xsl:choose>
    <xsl:when test="$pass = 'emitHeader'">
      <xsl:text>
	</xsl:text>
      <xsl:call-template name="emitInterfaces"/>
    </xsl:when>
    <xsl:otherwise>
      <xsl:text>
#import "</xsl:text>
      <xsl:value-of select="$header"/>
      <xsl:text>"

</xsl:text>
      <xsl:call-template name="emitImplementation"/>
      
	  <xsl:choose>
	  <xsl:when test="vm:class/@extends = 'android.app.Activity'">
        <xsl:call-template name="emitMainMethodForAndroid2Iphone"/>
      </xsl:when>
      
      
      <xsl:otherwise>
      
      <xsl:if test="vm:class/vm:method/@name = 'main'">
        <xsl:call-template name="emitMainMethod"/>
      </xsl:if>
      </xsl:otherwise>
      
      </xsl:choose>
      
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>



<xsl:template name="emitMainMethodForAndroid2Iphone">
  <xsl:text>
int main(int argc, char* argv[])
{
  	NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init];
    </xsl:text>
    <xsl:variable name="cl" as="node()" select="vm:class[@extends = 'android.app.Activity']"/>
    <xsl:value-of select="vm:fixname($cl/@package)"/>
    <xsl:text>_</xsl:text>
    <xsl:value-of select="$cl/@name"/>
    
    <xsl:text> *activity = [[[ </xsl:text>
    
    <xsl:value-of select="vm:fixname($cl/@package)"/>
    <xsl:text>_</xsl:text>
    <xsl:value-of select="$cl/@name"/>
    
    <xsl:text> alloc] init] autorelease];
    [activity __init_</xsl:text>
    <xsl:value-of select="vm:fixname($cl/@package)"/>
    <xsl:text>_</xsl:text>
    <xsl:value-of select="$cl/@name"/>
    <xsl:text>];
    [android_app_ActivityWrapper setActivity___android_app_Activity: activity];
    UIApplicationMain(
     argc, 
     argv,
     @"android_app_ActivityWrapper",
     @"android_app_ActivityWrapper");
	[pool release];
	return 0;					 
  }
  
  
  
</xsl:text>
</xsl:template>

<xsl:template name="emitMainMethod">
  <xsl:text>
  
      
  int main(int argc, char* argv[])
{
  	NSAutoreleasePool *pool = [[NSAutoreleasePool alloc] init];
    UIApplicationMain(
     argc, 
     argv,
     @"</xsl:text>
    <xsl:variable name="cl" as="node()" select="vm:class/vm:method[@name = 'main']/.."/>
    <xsl:value-of select="vm:fixname($cl/@package)"/>
    <xsl:text>_</xsl:text>
    <xsl:value-of select="$cl/@name"/>
    <xsl:text>",
     @"</xsl:text>
    <xsl:variable name="cl" as="node()" select="vm:class/vm:method[@name = 'main']/.."/>
    <xsl:value-of select="vm:fixname($cl/@package)"/>
    <xsl:text>_</xsl:text>
    <xsl:value-of select="$cl/@name"/>
    <xsl:text>");
	 
	[pool release];
	return 0;						 
  
  }
  
  
  
</xsl:text>
</xsl:template>

<xsl:function name="vm:fixname">
  <xsl:param  name="a"/>
  <xsl:value-of  select="replace(replace($a,'\$', '_'),'\.','_')"/>
</xsl:function>
  
  
  <xsl:template name="emitInterfaces">
  <xsl:for-each select="vm:class">
    <xsl:text>
@interface </xsl:text>
    <xsl:value-of select="vm:fixname(@package)"/>
    <xsl:text>_</xsl:text>
    <xsl:value-of select="vm:fixname(@name)"/>
    <xsl:text> : </xsl:text>
    <xsl:value-of select="vm:fixname(@extends)"/>
    <xsl:text> {
</xsl:text>
    <!-- Emit declarations for all non-static fields -->
    <xsl:for-each select="vm:field[not(@isStatic = 'true')]">
      <xsl:text>@public </xsl:text>
      <xsl:call-template name="emitType">
        <xsl:with-param name="type" select="@type"/>
      </xsl:call-template>
      <xsl:text> </xsl:text>
      <xsl:value-of select="@name"/>
      <xsl:text>;
</xsl:text>
    </xsl:for-each>
    <xsl:text>
}
</xsl:text>
    <!-- Emit declarations for getter and setter methods (as class methods) for all static fields -->
    <xsl:for-each select="vm:field[@isStatic = 'true']">
      <!-- Emit getter -->
      <xsl:text>+ (</xsl:text>
      <xsl:call-template name="emitType">
        <xsl:with-param name="type" select="@type"/>
      </xsl:call-template>
      <xsl:text>) _GET_STATIC_</xsl:text>
      <xsl:value-of select="vm:fixname(@name)"/>
      <xsl:text>;
</xsl:text>
      <!-- Emit setter -->
      <xsl:text>+ (void) _PUT_STATIC_</xsl:text>
      <xsl:value-of select="vm:fixname(@name)"/>
      <xsl:text>: (</xsl:text>
      <xsl:call-template name="emitType">
        <xsl:with-param name="type" select="@type"/>
      </xsl:call-template>
      <xsl:text>) v</xsl:text>
      <xsl:text>;
</xsl:text>
    </xsl:for-each>
    <!-- Emit declarations for all methods -->
    <xsl:for-each select="vm:method">
      <xsl:call-template name="emitMethodSignature"/>
  <xsl:text>;
</xsl:text>
    </xsl:for-each>
      
    <xsl:text>
@end

</xsl:text>
  </xsl:for-each>
  
</xsl:template>


<xsl:template name="emitImplementation">
  <xsl:for-each select="vm:class">
    <!-- Emit global variable definition for all static fields -->
    <xsl:for-each select="vm:field[@isStatic = 'true']">
      <xsl:call-template name="emitType">
        <xsl:with-param name="type" select="@type"/>
      </xsl:call-template>
      <xsl:text> _STATIC_</xsl:text>
      <xsl:value-of select="vm:fixname(../@package)"/>
      <xsl:text>_</xsl:text>
      <xsl:value-of select="vm:fixname(../@name)"/>
      <xsl:text>_</xsl:text>
      <xsl:value-of select="vm:fixname(@name)"/>
      <xsl:if test="@value">
        <xsl:text> = </xsl:text>
        <!-- TODO String values need to be surrounded by quotes and escaped properly. -->
        <xsl:if test="@type = 'java.lang.String'">
          <xsl:text>@</xsl:text>
        </xsl:if>
        <xsl:value-of select="@value"/>
      </xsl:if>
      <xsl:text>;
</xsl:text>
    </xsl:for-each>

    <xsl:text>
@implementation </xsl:text>
    <xsl:value-of select="vm:fixname(@package)"/>
    <xsl:text>_</xsl:text>
    <xsl:value-of select="vm:fixname(@name)"/>
    <xsl:text>;

</xsl:text>
    <xsl:for-each select="vm:field[@isStatic = 'true']">
      <!-- Emit getter -->
      <xsl:variable name="field">
        <xsl:text>_STATIC_</xsl:text>
        <xsl:value-of select="vm:fixname(../@package)"/>
        <xsl:text>_</xsl:text>
        <xsl:value-of select="vm:fixname(../@name)"/>
        <xsl:text>_</xsl:text>
        <xsl:value-of select="vm:fixname(@name)"/>
      </xsl:variable>
      <xsl:text>+ (</xsl:text>
      <xsl:call-template name="emitType">
        <xsl:with-param name="type" select="@type"/>
      </xsl:call-template>
      <xsl:text>) _GET_STATIC_</xsl:text>
      <xsl:value-of select="vm:fixname(@name)"/>
      <xsl:text>
{
    return </xsl:text>
      <xsl:value-of select="$field"/>
      <xsl:text>;
}

</xsl:text>
      <!-- Emit setter -->
      <xsl:text>+ (void) _PUT_STATIC_</xsl:text>
      <xsl:value-of select="vm:fixname(@name)"/>
      <xsl:text>: (</xsl:text>
      <xsl:call-template name="emitType">
        <xsl:with-param name="type" select="@type"/>
      </xsl:call-template>
      <xsl:text>) v
{
    </xsl:text>
    <xsl:if test="vm:isObjectRef(@type)">
        <xsl:text>[v retain];
    [</xsl:text>
        <xsl:value-of select="$field"/>
        <xsl:text> release];
    </xsl:text>
    </xsl:if>
      <xsl:value-of select="$field"/>
      <xsl:text> = v;
}

</xsl:text>
    </xsl:for-each>
    
    <xsl:for-each select="vm:method">
      <xsl:call-template name="emitMethodSignature"/>
  <xsl:text>
</xsl:text>
      <xsl:apply-templates/>
    </xsl:for-each>
      
    <xsl:text>
@end

</xsl:text>
  </xsl:for-each>
  
</xsl:template>


<xsl:template match="vm:signature">
  <!-- Do nothing -->
</xsl:template>


<xsl:template match="vm:code">
<xsl:text>{
    XMLVMElem _stack[</xsl:text>
  <xsl:value-of select="../@stack"/>
  <xsl:text>];
    XMLVMElem _locals[</xsl:text>
  <xsl:value-of select="../@locals"/>
  <xsl:text>];
    int _sp = 0;
    XMLVMElem _op1, _op2, _op3;
    int _i;
    for (_i = 0; _i &lt;</xsl:text>
  <xsl:value-of select="../@locals"/>
  <xsl:text>; _i++) _locals[_i].o = nil;
    NSAutoreleasePool* _pool = [[NSAutoreleasePool alloc] init];
</xsl:text>
  <xsl:call-template name="initLocals"/>
  <xsl:apply-templates/>
  <xsl:text>}


</xsl:text>
</xsl:template>


<xsl:template match="jvm:var">
  <!-- Do nothing -->
</xsl:template>


<xsl:template match="jvm:label">
  <xsl:text>    label</xsl:text>
  <xsl:value-of select="@id"/>
  <xsl:text>:;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:aload">
  <xsl:text>    _stack[_sp++].o = _locals[</xsl:text>
  <xsl:value-of select="@index"/>
  <xsl:text>].o;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:astore">
  <xsl:text>    _op1.o = _stack[--_sp].o;
    [_op1.o retain];
    [_locals[</xsl:text>
  <xsl:value-of select="@index"/>
  <xsl:text>].o release];
    _locals[</xsl:text>
  <xsl:value-of select="@index"/>
  <xsl:text>].o = _op1.o;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:return">
  <xsl:text>    [_pool release];
    return;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:ireturn">
  <xsl:text>    _op1.i = _stack[--_sp].i;
    [_pool release];
    return _op1.i;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:freturn">
  <xsl:text>    _op1.f = _stack[--_sp].f;
    [_pool release];
    return _op1.f;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:areturn">
  <xsl:text>    _op1.o = _stack[--_sp].o;
    [_op1.o retain];
    [_pool release];
    return _op1.o;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:ldc|jvm:ldc_w|jvm:ldc2_w">
  <xsl:text>    _stack[_sp++]</xsl:text>
  <xsl:choose>
    <xsl:when test="@type = 'float'">
      <xsl:text>.f = </xsl:text>
      <xsl:value-of select="@value"/>
    </xsl:when>
    <xsl:when test="@type = 'double'">
      <xsl:text>.d = </xsl:text>
      <xsl:value-of select="@value"/>
    </xsl:when>
    <xsl:when test="@type = 'int'">
      <xsl:text>.i = </xsl:text>
      <xsl:value-of select="@value"/>
    </xsl:when>
    <xsl:when test="@type = 'long'">
      <xsl:text>.l = </xsl:text>
      <xsl:value-of select="@value"/>
    </xsl:when>
    <xsl:when test="@type = 'java.lang.String'">
      <xsl:text>.o = @"</xsl:text>
      <xsl:value-of select="@value"/>
      <xsl:text>"</xsl:text>
    </xsl:when>
    <!-- Quick hack so we catch other missing @type -->
    <xsl:when test="@type = 'org.xmlvm.test.iphone.ihelloworld.HelloWorld'">
      <xsl:text>.o = [org_xmlvm_test_iphone_ihelloworld_HelloWorld class]</xsl:text>
    </xsl:when>
    <!-- Quick hack so we catch other missing @type -->
    <xsl:when test="@type = 'org.xmlvm.test.iphone.ifireworks.Main'">
      <xsl:text>.o = [org_xmlvm_test_iphone_ifireworks_Main class]</xsl:text>
    </xsl:when>
    <!-- Quick hack so we catch other missing @type -->
    <xsl:when test="@type = 'org.xmlvm.test.iphone.Android'">
      <xsl:text>.o = [org_xmlvm_test_iphone_Android class]</xsl:text>
    </xsl:when>
    <!-- Quick hack so we catch other missing @type -->
    <xsl:when test="@type = 'org.xmlvm.test.iphone.todo.Main'">
      <xsl:text>.o = [org_xmlvm_test_iphone_todo_Main class]</xsl:text>
    </xsl:when>
    <!-- Quick hack so we catch other missing @type -->
    <xsl:when test="@type = 'org.xmlvm.iphone.internal.RemoteUI'">
      <xsl:text>.o = [org_xmlvm_iphone_internal_RemoteUI class]</xsl:text>
    </xsl:when>
    <!-- Quick hack so we catch other missing @type -->
    <xsl:when test="@type = 'org.xmlvm.iphone.remote.Main'">
      <xsl:text>.o = [org_xmlvm_iphone_remote_Main class]</xsl:text>
    </xsl:when>
    <!-- Quick hack so we catch other missing @type -->
    <xsl:when test="@type = 'org.xmlvm.iphone.iremote.Main'">
      <xsl:text>.o = [org_xmlvm_iphone_iremote_Main class]</xsl:text>
    </xsl:when>
    <xsl:otherwise>
      <xsl:message select="'Unknown type in jvm:ldc'"/>
      <xsl:message select="@type"/>
    </xsl:otherwise>
  </xsl:choose>
  <xsl:text>;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:getstatic">
  <xsl:variable name="typedAccess">
    <xsl:call-template name="emitTypedAccess">
      <xsl:with-param name="type" select="@type"/>
    </xsl:call-template>
  </xsl:variable>
  <xsl:text>    _op1</xsl:text>
  <xsl:value-of select="$typedAccess"/>
  <xsl:text> = [</xsl:text>
  <xsl:value-of select="vm:fixname(@class-type)"/>
  <xsl:text> _GET_STATIC_</xsl:text>
  <xsl:value-of select="@field"/>
  <xsl:text>];
    _stack[_sp++]</xsl:text>
  <xsl:value-of select="$typedAccess"/>
  <xsl:text> = _op1</xsl:text>
  <xsl:value-of select="$typedAccess"/>
  <xsl:text>;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:putstatic">
  <xsl:variable name="typedAccess">
    <xsl:call-template name="emitTypedAccess">
      <xsl:with-param name="type" select="@type"/>
    </xsl:call-template>
  </xsl:variable>
  <xsl:text>    _op1</xsl:text>
  <xsl:value-of select="$typedAccess"/>
  <xsl:text> = _stack[--_sp]</xsl:text>
  <xsl:value-of select="$typedAccess"/>
  <xsl:text>;
    [</xsl:text>
  <xsl:value-of select="vm:fixname(@class-type)"/>
  <xsl:text> _PUT_STATIC_</xsl:text>
  <xsl:value-of select="@field"/>
  <xsl:text>: _op1</xsl:text>
  <xsl:value-of select="$typedAccess"/>
  <xsl:text>];
</xsl:text>
</xsl:template>


<xsl:template match="jvm:aconst_null">
  <xsl:text>    _stack[_sp++].o = nil;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:iconst">
  <xsl:text>    _stack[_sp++].i = </xsl:text>
  <xsl:value-of select="@value"/>
  <xsl:text>;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:fconst">
  <xsl:text>    _stack[_sp++].f = </xsl:text>
  <xsl:value-of select="@value"/>
  <xsl:text>;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:dconst">
  <xsl:text>    _stack[_sp++].d = </xsl:text>
  <xsl:value-of select="@value"/>
  <xsl:text>;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:istore">
  <xsl:text>    _op1.i = _stack[--_sp].i;
    _locals[</xsl:text>
  <xsl:value-of select="@index"/>
  <xsl:text>].i = _op1.i;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:dstore">
  <xsl:text>    _op1.d = _stack[--_sp].d;
    _locals[</xsl:text>
  <xsl:value-of select="@index"/>
  <xsl:text>].d = _op1.d;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:fstore">
  <xsl:text>    _op1.f = _stack[--_sp].f;
    _locals[</xsl:text>
  <xsl:value-of select="@index"/>
  <xsl:text>].f = _op1.f;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:fneg">
  <xsl:text>    _op1.f = _stack[--_sp].f;
    _stack[_sp++].f = -_op1.f;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:iload">
  <xsl:text>    _op1.i = _locals[</xsl:text>
  <xsl:value-of select="@index"/>
  <xsl:text>].i;
    _stack[_sp++].i = _op1.i;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:lload">
  <xsl:text>    _op1.l = _locals[</xsl:text>
  <xsl:value-of select="@index"/>
  <xsl:text>].l;
  _stack[_sp++].l = _op1.l;
</xsl:text>
</xsl:template>


  <xsl:template match="jvm:dload">
  <xsl:text>    _op1.d = _locals[</xsl:text>
  <xsl:value-of select="@index"/>
  <xsl:text>].d;
    _stack[_sp++].d = _op1.d;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:fload">
  <xsl:text>    _op1.f = _locals[</xsl:text>
  <xsl:value-of select="@index"/>
  <xsl:text>].f;
    _stack[_sp++].f = _op1.f;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:iinc">
  <xsl:text>    _locals[</xsl:text>
  <xsl:value-of select="@index"/>
  <xsl:text>].i += </xsl:text>
  <xsl:value-of select="@incr"/>
  <xsl:text>;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:iand">
  <xsl:text>    _op2.i = _stack[--_sp].i;
    _op1.i = _stack[--_sp].i;
    _stack[_sp++].i = _op1.i &amp; _op2.i;</xsl:text>
</xsl:template>


<xsl:template match="jvm:iadd">
  <xsl:text>    _op2.i = _stack[--_sp].i;
    _op1.i = _stack[--_sp].i;
    _stack[_sp++].i = _op1.i + _op2.i;</xsl:text>
</xsl:template>


<xsl:template match="jvm:fadd">
  <xsl:text>    _op2.f = _stack[--_sp].f;
    _op1.f = _stack[--_sp].f;
    _stack[_sp++].f = _op1.f + _op2.f;</xsl:text>
</xsl:template>


<xsl:template match="jvm:isub">
  <xsl:text>    _op2.i = _stack[--_sp].i;
    _op1.i = _stack[--_sp].i;
    _stack[_sp++].i = _op1.i - _op2.i;</xsl:text>
</xsl:template>


<xsl:template match="jvm:fsub">
  <xsl:text>    _op2.f = _stack[--_sp].f;
    _op1.f = _stack[--_sp].f;
    _stack[_sp++].f = _op1.f - _op2.f;</xsl:text>
</xsl:template>


<xsl:template match="jvm:imul">
  <xsl:text>    _op2.i = _stack[--_sp].i;
    _op1.i = _stack[--_sp].i;
    _stack[_sp++].i = _op1.i * _op2.i;</xsl:text>
</xsl:template>


<xsl:template match="jvm:fmul">
  <xsl:text>    _op2.f = _stack[--_sp].f;
    _op1.f = _stack[--_sp].f;
    _stack[_sp++].f = _op1.f * _op2.f;</xsl:text>
</xsl:template>


<xsl:template match="jvm:dmul">
  <xsl:text>    _op2.d = _stack[--_sp].d;
    _op1.d = _stack[--_sp].d;
    _stack[_sp++].d = _op1.d * _op2.d;</xsl:text>
</xsl:template>


<xsl:template match="jvm:idiv">
  <xsl:text>    _op2.i = _stack[--_sp].i;
  _op1.i = _stack[--_sp].i;
  _stack[_sp++].i = _op1.i / _op2.i;</xsl:text>
</xsl:template>

<xsl:template match="jvm:fdiv">
  <xsl:text>    _op2.f = _stack[--_sp].f;
  _op1.f = _stack[--_sp].f;
  _stack[_sp++].f = _op1.f / _op2.f;</xsl:text>
</xsl:template>

<xsl:template match="jvm:ddiv">
  <xsl:text>    _op2.d = _stack[--_sp].d;
    _op1.d = _stack[--_sp].d;
    _stack[_sp++].d = _op1.d / _op2.d;</xsl:text>
</xsl:template>


<xsl:template match="jvm:d2i">
  <xsl:text>    _op1.d = _stack[--_sp].d;
    _stack[_sp++].i = (int) _op1.d;</xsl:text>
</xsl:template>

<xsl:template match="jvm:irem">
  <xsl:text>    
  _op2.i = _stack[--_sp].i; // Integer remainder of 
  _op1.i = _stack[--_sp].i;
  _stack[_sp++].i = _op1.i % _op2.i;</xsl:text>
</xsl:template>



  <xsl:template match="jvm:f2i">
  <xsl:text>    _op1.f = _stack[--_sp].f;
  _stack[_sp++].i = (int) _op1.f;</xsl:text>
</xsl:template>

  <xsl:template match="jvm:d2f">
  <xsl:text>    _op1.d = _stack[--_sp].d;
    _stack[_sp++].f = (float) _op1.d;</xsl:text>
</xsl:template>


<xsl:template match="jvm:f2d">
  <xsl:text>    _op1.f = _stack[--_sp].f;
    _stack[_sp++].d = (double) _op1.f;</xsl:text>
</xsl:template>


<xsl:template match="jvm:i2f">
  <xsl:text>    _op1.i = _stack[--_sp].i;
    _stack[_sp++].f = (float) _op1.i;</xsl:text>
</xsl:template>
  
<xsl:template match="jvm:l2f">
  <xsl:text>    _op1.l = _stack[--_sp].l;
  _stack[_sp++].f = (float) _op1.l;</xsl:text>
</xsl:template>


<xsl:template match="jvm:i2d">
  <xsl:text>    _op1.i = _stack[--_sp].i;
  _stack[_sp++].d = (double) _op1.i;</xsl:text>
</xsl:template>

<xsl:template match="jvm:putfield">
  <xsl:variable name="m">
    <xsl:call-template name="emitTypedAccess">
      <xsl:with-param name="type" select="@type"/>
    </xsl:call-template>
  </xsl:variable>
  <xsl:text>    _op1</xsl:text>
  <xsl:value-of select="$m"/>
  <xsl:text> = _stack[--_sp]</xsl:text>
  <xsl:value-of select="$m"/>
  <xsl:text>;
    </xsl:text>
  <xsl:if test="vm:isObjectRef(@type)">
    <xsl:text>[_op1.o retain];
    </xsl:text>
  </xsl:if>
  <xsl:text>_op2.o = _stack[--_sp].o;
    </xsl:text>
  <xsl:if test="vm:isObjectRef(@type)">
    <xsl:text>[((</xsl:text>
    <xsl:call-template name="emitType">
      <xsl:with-param name="type" select="@class-type"/>
    </xsl:call-template>
    <xsl:text>) _op2.o)-&gt;</xsl:text>
    <xsl:value-of select="@field"/>
    <xsl:text> release];
    </xsl:text>
  </xsl:if>
  <xsl:text>((</xsl:text>
  <xsl:call-template name="emitType">
    <xsl:with-param name="type" select="@class-type"/>
  </xsl:call-template>
  <xsl:text>) _op2.o)-&gt;</xsl:text>
  <xsl:value-of select="@field"/>
  <xsl:text> = _op1</xsl:text>
  <xsl:value-of select="$m"/>
  <xsl:text>;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:getfield">
  <xsl:variable name="m">
    <xsl:call-template name="emitTypedAccess">
      <xsl:with-param name="type" select="@type"/>
    </xsl:call-template>
  </xsl:variable>
  <xsl:text>    _op1.o = _stack[--_sp].o;
    _op2</xsl:text>
  <xsl:value-of select="$m"/>
  <xsl:text> = ((</xsl:text>
  <xsl:call-template name="emitType">
    <xsl:with-param name="type" select="@class-type"/>
  </xsl:call-template>
  <xsl:text>) _op1.o)-&gt;</xsl:text>
  <xsl:value-of select="@field"/>
  <xsl:text>;
    _stack[_sp++]</xsl:text>
  <xsl:value-of select="$m"/>
  <xsl:text> = _op2</xsl:text>
  <xsl:value-of select="$m"/>
  <xsl:text>;
</xsl:text>
</xsl:template>

<xsl:template match="jvm:pop">
<xsl:text>
_sp--;
</xsl:text>
</xsl:template>

<xsl:template match="jvm:dup">
  <xsl:text>    _op1 = _stack[_sp - 1];
    _stack[_sp++] = _op1;
</xsl:text>
</xsl:template>


<!-- dup_x1 -->
<xsl:template match="jvm:dup_x1">
  <xsl:text>    _op1 = _stack[--_sp];
    _op2 = _stack[--_sp];
    _stack[_sp++] = _op1;
    _stack[_sp++] = _op2;
    _stack[_sp++] = _op1;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:new">
  <xsl:text>    _stack[_sp++].o = [[[</xsl:text>
  <xsl:value-of select="vm:fixname(@type)"/>
  <xsl:text> alloc] init] autorelease];
</xsl:text>
</xsl:template>


<xsl:template match="jvm:goto">
  <xsl:text>    goto label</xsl:text>
  <xsl:value-of select="@label"/>
  <xsl:text>;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:lookupswitch">
  <xsl:text>    _op1.i = _stack[--_sp].i;
    switch (_op1.i) {</xsl:text>
  <xsl:for-each select="jvm:case">
    <xsl:text>
        case </xsl:text>
    <xsl:value-of select="@key"/>
    <xsl:text>: goto label</xsl:text>
    <xsl:value-of select="@label"/>
    <xsl:text>;</xsl:text>
  </xsl:for-each>
  <xsl:if test="jvm:default">
    <xsl:text>
        default: goto label</xsl:text>
    <xsl:value-of select="jvm:default/@label"/>
    <xsl:text>;</xsl:text>
  </xsl:if>
  <xsl:text>
    }
</xsl:text>
</xsl:template>


<xsl:template match="jvm:bipush|jvm:sipush">
  <xsl:text>    _stack[_sp++].i = </xsl:text>
  <xsl:value-of select="@value"/>
  <xsl:text>;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:ifeq">
  <xsl:text>    _op1.i = _stack[--_sp].i;
    if (_op1.i == 0) goto label</xsl:text>
  <xsl:value-of select="@label"/>
  <xsl:text>;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:ifne">
  <xsl:text>    _op1.i = _stack[--_sp].i;
    if (_op1.i != 0) goto label</xsl:text>
  <xsl:value-of select="@label"/>
  <xsl:text>;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:iflt">
  <xsl:text>    _op1.i = _stack[--_sp].i;
    if (_op1.i &lt; 0) goto label</xsl:text>
  <xsl:value-of select="@label"/>
  <xsl:text>;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:ifle">
  <xsl:text>    _op1.i = _stack[--_sp].i;
    if (_op1.i &lt;= 0) goto label</xsl:text>
  <xsl:value-of select="@label"/>
  <xsl:text>;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:ifgt">
  <xsl:text>    _op1.i = _stack[--_sp].i;
    if (_op1.i &gt; 0) goto label</xsl:text>
  <xsl:value-of select="@label"/>
  <xsl:text>;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:ifge">
  <xsl:text>    _op1.i = _stack[--_sp].i;
    if (_op1.i &gt;= 0) goto label</xsl:text>
  <xsl:value-of select="@label"/>
  <xsl:text>;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:ifnull">
  <xsl:text>    _op1.o = _stack[--_sp].o;
    if (_op1.o == nil) goto label</xsl:text>
  <xsl:value-of select="@label"/>
  <xsl:text>;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:ifnonnull">
  <xsl:text>    _op1.o = _stack[--_sp].o;
    if (_op1.o != nil) goto label</xsl:text>
  <xsl:value-of select="@label"/>
  <xsl:text>;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:if_icmplt">
  <xsl:text>    _op2.i = _stack[--_sp].i;
    _op1.i = _stack[--_sp].i;
    if (_op1.i &lt; _op2.i) goto label</xsl:text>
  <xsl:value-of select="@label"/>
  <xsl:text>;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:if_icmpge">
  <xsl:text>    _op2.i = _stack[--_sp].i;
    _op1.i = _stack[--_sp].i;
    if (_op1.i >= _op2.i) goto label</xsl:text>
  <xsl:value-of select="@label"/>
  <xsl:text>;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:if_icmple">
  <xsl:text>    _op2.i = _stack[--_sp].i;
    _op1.i = _stack[--_sp].i;
    if (_op1.i &lt;= _op2.i) goto label</xsl:text>
  <xsl:value-of select="@label"/>
  <xsl:text>;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:if_icmpne">
  <xsl:text>    _op2.i = _stack[--_sp].i;
    _op1.i = _stack[--_sp].i;
    if (_op1.i != _op2.i) goto label</xsl:text>
  <xsl:value-of select="@label"/>
  <xsl:text>;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:if_icmpeq">
  <xsl:text>    _op2.i = _stack[--_sp].i;
    _op1.i = _stack[--_sp].i;
    if (_op1.i == _op2.i) goto label</xsl:text>
  <xsl:value-of select="@label"/>
  <xsl:text>;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:dcmpg">
  <xsl:text>    _op2.f = (float) _stack[--_sp].d;
    _op1.f = (float) _stack[--_sp].d;
    _op3.i = 1;
    if (_op1.f &gt; _op2.f)
      _op3.i = 1;
    else if (_op1.f == _op2.f)
      _op3.i = 0;
    else if (_op1.f &lt; _op2.f)
      _op3.i = -1;
    _stack[_sp++].i = _op3.i;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:fcmpg">
  <xsl:text>    _op2.f = _stack[--_sp].f;
    _op1.f = _stack[--_sp].f;
    _op3.i = 1;
    if (_op1.f &gt; _op2.f)
      _op3.i = 1;
    else if (_op1.f == _op2.f)
      _op3.i = 0;
    else if (_op1.f &lt; _op2.f)
      _op3.i = -1;
    _stack[_sp++].i = _op3.i;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:fcmpl">
  <xsl:text>    _op2.f = _stack[--_sp].f;
    _op1.f = _stack[--_sp].f;
    _op3.i = -1;
    if (_op1.f &gt; _op2.f)
      _op3.i = 1;
    else if (_op1.f == _op2.f)
      _op3.i = 0;
    else if (_op1.f &lt; _op2.f)
      _op3.i = -1;
    _stack[_sp++].i = _op3.i;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:invokestatic">
  <xsl:text>    _sp -= </xsl:text>
  <xsl:value-of select="count(vm:signature/vm:parameter)"/>
  <xsl:text>;
    </xsl:text>
  <xsl:variable name="returnTypedAccess">
    <xsl:call-template name="emitTypedAccess">
      <xsl:with-param name="type" select="vm:signature/vm:return/@type"/>
    </xsl:call-template>
  </xsl:variable>
  <xsl:if test="vm:signature/vm:return/@type != 'void'">
    <xsl:text>_op1</xsl:text>
    <xsl:value-of select="$returnTypedAccess"/>
    <xsl:text> = </xsl:text>
  </xsl:if>
  <xsl:text>[</xsl:text>
  <xsl:value-of select="vm:fixname(@class-type)"/>
  <xsl:text> </xsl:text>
  <xsl:value-of select="@method"/>
  <xsl:call-template name="appendSignature"/>
  <xsl:for-each select="vm:signature/vm:parameter">
    <xsl:text>:_stack[_sp + </xsl:text>
    <xsl:value-of select="position() - 1"/>
    <xsl:text>]</xsl:text>
    <xsl:call-template name="emitTypedAccess">
      <xsl:with-param name="type" select="@type"/>
    </xsl:call-template>
  </xsl:for-each>
  <xsl:text>];
</xsl:text>
  <xsl:if test="vm:signature/vm:return/@type != 'void'">
    <xsl:text>    _stack[_sp++]</xsl:text>
    <xsl:value-of select="$returnTypedAccess"/>
    <xsl:text> = _op1</xsl:text>
    <xsl:value-of select="$returnTypedAccess"/>
    <xsl:text>;
</xsl:text>
  </xsl:if>
</xsl:template>



<xsl:template match="jvm:invokevirtual|jvm:invokeinterface|jvm:invokespecial">
  <xsl:text>    _sp -= </xsl:text>
  <xsl:value-of select="count(vm:signature/vm:parameter) + 1"/>
  <xsl:text>;
    </xsl:text>
  <xsl:variable name="returnTypedAccess">
    <xsl:call-template name="emitTypedAccess">
      <xsl:with-param name="type" select="vm:signature/vm:return/@type"/>
    </xsl:call-template>
  </xsl:variable>
  <xsl:if test="vm:signature/vm:return/@type != 'void'">
    <xsl:text>_op1</xsl:text>
    <xsl:value-of select="$returnTypedAccess"/>
    <xsl:text> = </xsl:text>
  </xsl:if>
  <xsl:text>[((</xsl:text>
  
  <xsl:variable name="baseClass">
    <xsl:value-of select="../../../@extends"/>
  </xsl:variable>
  <xsl:choose>
 	<xsl:when test="name() = 'jvm:invokevirtual' or compare(@class-type,$baseClass) != 0">
		  <xsl:call-template name="emitType">
		    <xsl:with-param name="type" select="@class-type"/>
  			</xsl:call-template>
  		<xsl:text>) _stack[_sp].o) </xsl:text>
  	</xsl:when>
    <xsl:otherwise>
	  <xsl:text>super)) </xsl:text>
    </xsl:otherwise>
  </xsl:choose>

  <xsl:call-template name="emitMethodName">
    <xsl:with-param name="name" select="@method"/>
    <xsl:with-param name="class-type" select="@class-type"/>
  </xsl:call-template>
  <xsl:call-template name="appendSignature"/>
  <xsl:for-each select="vm:signature/vm:parameter">
    <xsl:text>:_stack[_sp + </xsl:text>
    <xsl:value-of select="position()"/>
    <xsl:text>]</xsl:text>
    <xsl:call-template name="emitTypedAccess">
      <xsl:with-param name="type" select="@type"/>
    </xsl:call-template>
  </xsl:for-each>
  <xsl:text>];
</xsl:text>
  <xsl:if test="vm:signature/vm:return/@type != 'void'">
    <xsl:text>    _stack[_sp++]</xsl:text>
    <xsl:value-of select="$returnTypedAccess"/>
    <xsl:text> = _op1</xsl:text>
    <xsl:value-of select="$returnTypedAccess"/>
    <xsl:text>;
</xsl:text>
  </xsl:if>
</xsl:template>


<xsl:template match="jvm:newarray|jvm:anewarray">
  <xsl:text>    _op1.i = _stack[--_sp].i;
    _stack[_sp].o = [NSMutableArray arrayWithCapacity: _op1.i];
    for (_op2.i = 0; _op2.i &lt; _op1.i; _op2.i++)
        [_stack[_sp].o addObject: [[NSObject alloc] init]];
    _sp++;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:arraylength">
  <xsl:text>    _op1.i = [_stack[--_sp].o count];
    _stack[_sp++].i = _op1.i;
</xsl:text>
</xsl:template>


<xsl:template match="jvm:aaload">
  <xsl:text>    _op1.i = _stack[--_sp].i;
    _op2.o = _stack[--_sp].o;
    _stack[_sp++].o = [_op2.o objectAtIndex: _op1.i];
</xsl:text>
</xsl:template>


<xsl:template match="jvm:caload">
  <xsl:text>    _op1.i = _stack[--_sp].i;
    _op2.o = _stack[--_sp].o;
    _stack[_sp++].i = [[_op2.o objectAtIndex: _op1.i] intValue];
</xsl:text>
</xsl:template>


<xsl:template match="jvm:faload">
  <xsl:text>    _op1.i = _stack[--_sp].i;
    _op2.o = _stack[--_sp].o;
    _stack[_sp++].f = [[_op2.o objectAtIndex: _op1.i] floatValue];
</xsl:text>
</xsl:template>


<xsl:template match="jvm:aastore">
  <xsl:text>    _op1.o = _stack[--_sp].o;
    _op2.i = _stack[--_sp].i;
    _op3.o = _stack[--_sp].o;
    [_op3.o replaceObjectAtIndex: _op2.i withObject: _op1.o];
</xsl:text>
</xsl:template>


<xsl:template match="jvm:castore">
  <xsl:text>    _op1.i = _stack[--_sp].i;
    _op2.i = _stack[--_sp].i;
    _op3.o = _stack[--_sp].o;
    [_op3.o replaceObjectAtIndex: _op2.i withObject: [NSNumber numberWithInt: _op1.i]];
</xsl:text>
</xsl:template>


<xsl:template match="jvm:fastore">
  <xsl:text>    _op1.f = _stack[--_sp].f;
    _op2.i = _stack[--_sp].i;
    _op3.o = _stack[--_sp].o;
    [_op3.o replaceObjectAtIndex: _op2.i withObject: [NSNumber numberWithFloat: _op1.f]];
</xsl:text>
</xsl:template>


<xsl:template match="jvm:checkcast">
  <!-- TODO should do a runtime type check -->
</xsl:template>


<xsl:template name="emitType">
  <xsl:param name="type"/>
  <xsl:choose>
    <xsl:when test="$type = 'void'">
      <xsl:text>void</xsl:text>
    </xsl:when>
    <xsl:when test="$type = 'int'">
      <xsl:text>int</xsl:text>
    </xsl:when>
    <xsl:when test="$type = 'long'">
      <xsl:text>long</xsl:text>
    </xsl:when>
    <xsl:when test="$type = 'float'">
      <xsl:text>float</xsl:text>
    </xsl:when>
    <xsl:when test="$type = 'double'">
      <xsl:text>double</xsl:text>
    </xsl:when>
    <xsl:when test="$type = 'boolean'">
      <xsl:text>int</xsl:text>
    </xsl:when>
    <xsl:when test="ends-with($type, '[]')">
      <xsl:text>NSMutableArray*</xsl:text>
    </xsl:when>
    <xsl:otherwise>
      <xsl:value-of select="vm:fixname($type)"/>
      <xsl:text>*</xsl:text>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>


<xsl:template name="emitMethodName">
  <xsl:param name="name"/>
  <xsl:param name="class-type"/>
  <xsl:choose>
    <xsl:when test="$name = '&lt;init&gt;'">
      <xsl:text>__init_</xsl:text>
      <xsl:value-of select="vm:fixname($class-type)"/>
    </xsl:when>
    <xsl:when test="$name = '&lt;clinit&gt;'">
      <xsl:text>initialize</xsl:text>
    </xsl:when>
    <xsl:otherwise>
      <xsl:value-of select="$name"/>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>


<xsl:template name="emitMethodSignature">
  <xsl:value-of select="if (@isStatic = 'true') then '+' else '-'"/>
  <xsl:text> (</xsl:text>
  <xsl:call-template name="emitType">
    <xsl:with-param name="type" select="vm:signature/vm:return/@type"/>
  </xsl:call-template>
  <xsl:text>) </xsl:text>
  <xsl:call-template name="emitMethodName">
    <xsl:with-param name="name" select="@name"/>
    <xsl:with-param name="class-type" select="concat(../@package, '.', ../@name)"/>
  </xsl:call-template>
  <xsl:call-template name="appendSignature"/>
  <xsl:for-each select="vm:signature/vm:parameter">
    <xsl:text> :(</xsl:text>
    <xsl:call-template name="emitType">
      <xsl:with-param name="type" select="@type"/>
    </xsl:call-template>
    <xsl:text>)n</xsl:text>
    <xsl:value-of select="position()"/>
  </xsl:for-each>
</xsl:template>


<xsl:template name="appendSignature">
  <xsl:choose>
    <xsl:when test="count(vm:signature/vm:parameter) != 0">
      <xsl:text>__</xsl:text>
      <xsl:for-each select="vm:signature/vm:parameter">
        <xsl:text>_</xsl:text>
        <xsl:value-of select="replace(vm:fixname(@type), '\[\]', '_ARRAYTYPE')"/>
      </xsl:for-each>
    </xsl:when>
  </xsl:choose>
</xsl:template>

    
<!--
   initLocals
   ==========
   This function is called from the template for <code>. Its task is
   to initialize the local variables. This basically means that the
   actual parameters have to be copied to _locals[i]. If the method
   is not static, 'this' will be copied to _locals[0].
-->

<xsl:template name="initLocals">
    <xsl:for-each select="jvm:var">
		<xsl:choose>    
      		<xsl:when test="@name = 'this'">
      			<xsl:text>    _locals[</xsl:text>
    			<xsl:value-of select="@id" />
    			<xsl:text>].o = self;
</xsl:text>
     		</xsl:when>
     		<xsl:otherwise>
     			<xsl:if test="(position()-count(../jvm:var[@name='this'])) &lt;= count(../../vm:signature/vm:parameter)" >
     			  <xsl:text>    _locals[</xsl:text>
     	  		  <xsl:value-of select="@id" />
     	  		  <xsl:text>]</xsl:text>
     	  		  <xsl:call-template name="emitTypedAccess">
     	  		    <xsl:with-param name="type" select="@type"/>
     	  		  </xsl:call-template>
     	  		  <xsl:text> = n</xsl:text>
     	  		  <xsl:value-of select="(position()-count(../jvm:var[@name='this']))" />
     	  		  <xsl:text>;
</xsl:text>
     			</xsl:if>
     		</xsl:otherwise>
     	</xsl:choose>
    </xsl:for-each>
</xsl:template>


<xsl:template name="emitTypedAccess">
  <xsl:param name="type"/>
  
  <xsl:text>.</xsl:text>
  <xsl:choose>
    <xsl:when test="$type = 'int' or $type = 'boolean'">
      <xsl:text>i</xsl:text>
    </xsl:when>
    <xsl:when test="$type = 'float'">
      <xsl:text>f</xsl:text>
    </xsl:when>
    <xsl:when test="$type = 'double'">
      <xsl:text>d</xsl:text>
    </xsl:when>
    <xsl:when test="$type = 'long'">
      <xsl:text>l</xsl:text>
    </xsl:when>
    <xsl:otherwise>
      <xsl:text>o</xsl:text>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>


<xsl:function name="vm:isObjectRef" as="xs:boolean">
  <xsl:param name="type" as="xs:string"/>
  
  <xsl:value-of select="not($type='int' or $type='float' or $type='long' or $type='double' or
                            $type='char' or $type='boolean')"/>
</xsl:function>


<!--
   Default template. If the XMLVM file should contain an instruction
   that is not handled by this stylesheet, this default template
   will make sure we notice it by writing a special error function
   to the output stream.
-->
<xsl:template match="*">
  <xsl:text>      //ERROR("</xsl:text>
  <xsl:value-of select="name()"/>
    <xsl:text>");
</xsl:text>
  <xsl:message select="."/>
</xsl:template>


</xsl:stylesheet>
