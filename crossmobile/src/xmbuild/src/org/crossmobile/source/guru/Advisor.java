/* Copyright (c) 2011 by crossmobile.org
 *
 * CrossMobile is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 2.
 *
 * CrossMobile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CrossMobile; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

package org.crossmobile.source.guru;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.SAXParserFactory;
import org.crossmobile.source.ctype.CEnum;
import org.crossmobile.source.ctype.CLibrary;
import org.crossmobile.source.ctype.CType;
import org.crossmobile.source.xtype.XArg;
import org.crossmobile.source.xtype.XCode;
import org.crossmobile.source.xtype.XInjectedMethod;
import org.crossmobile.source.xtype.XMethod;
import org.crossmobile.source.xtype.XProperty;
import org.crossmobile.source.xtype.XObject;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Advisor extends DefaultHandler {

    private static final Advisor current = new Advisor();
    //
    private static final Map<String, CEnum> constructorOverload = new HashMap<String, CEnum>();
    private static final Map<String, List<String>> argumentID = new HashMap<String, List<String>>();
    private static final Map<String, String> returnID = new HashMap<String, String>();
    private static final Map<String, Integer> genericsSupport = new HashMap<String, Integer>();
    private static final Set<String> nativeTypes = new HashSet<String>();
    private static final Set<Tuple> typedefs = new HashSet<Tuple>();
    private static final List<Tuple> removables = new ArrayList<Tuple>();
    private static final Map<String, String> nameChanges = new HashMap<String, String>();
    private static final Map<String, String> methodCanonicals = new HashMap<String, String>();
    private static final Set<String> delegatePatterns = new HashSet<String>();
    private static final Set<String> definedObjects = new HashSet<String>();
    private static final List<String> ignoreList = new ArrayList<String>();
    private static final Map<String, String> dataTypeMapping = new HashMap<String, String>();
    private static final Map<String, XObject> classObject = new HashMap<String, XObject>();
    //
    private String argsig;
    private List<String> argids;
    private String consig;
    private String conname;
    private List<String> conids;
    private boolean conreset;
    private Map<String, String> conMaps;
    
    private String className;
    private String selectorName;
    private boolean isMandatory = false;
    private XArg xarg;
    private XMethod xmethod;
    private XObject xobject;
    private ArrayList<XMethod> methodList;
    private List<XArg> argList;
    private String requireAutoReleasePool;
    private ArrayList<XProperty> propertyList;
    private XProperty property;
    private List<String> aliasList;
    private List<XInjectedMethod> injectedMethodList;
    private XInjectedMethod injMethod;
    private String injectedMethodName;
    private String injectedMethodModifier;
    private String returnType;
    private String defaultRetunValue;
    private String language;
    private String mode;
    private StringBuilder injectedCode;
    private boolean currentElement = false;
    private List<String> referenceList;
    private boolean isDelegate = true;
    private String opaqueBaseType;
    private boolean noInternalConstructor = false;
    private boolean ignoreMethod = false;
    private List<XCode> globalCodeList;
    private List<XCode> selectorCodeList;
    XCode code;
    boolean isSelector = false;
    private boolean isInjectedMethod = false;
    private boolean isOverridden = false;
    
    //
    private static String lastfile;

    static {
        try {
            String defaultpath = "/" + Advisor.class.getPackage().getName().replace('.', '/') + "/advisor.xml";
            lastfile = "System Defaults";
            education(Advisor.class.getResourceAsStream(defaultpath));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static synchronized void educate(String filename) {
        try {
            lastfile = filename;
            education(new FileInputStream(filename));
        } catch (Exception ex) {
            Reporter.ADVISOR_LOADING_ERROR.report(ex.toString(), filename);
        }
    }

    private synchronized static void education(InputStream in) throws Exception {
        SAXParserFactory.newInstance().newSAXParser().parse(in, current);
        lastfile = null;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes at) throws SAXException {
        
        if (qName.equals("native"))
            nativeTypes.add(at.getValue("type"));
        else if (qName.equals("typedef"))
            typedefs.add(new Tuple(at.getValue("java"), at.getValue("c")));
        else if (qName.equals("genericsclass"))
            genericsSupport.put(at.getValue("class"), Integer.parseInt(at.getValue("items")));
        else if (qName.equals("returnid"))
            returnID.put(at.getValue("signature"), at.getValue("object"));
        else if (qName.equals("argumentid")) {
            argids = new ArrayList<String>();
            argsig = at.getValue("signature");
        } else if (qName.equals("argumentiditem"))
            argids.add(at.getValue("object"));
        else if (qName.equals("constructor")) {
            conids = new ArrayList<String>();
            conMaps = new HashMap<String, String>();
            consig = at.getValue("signature");
            conname = at.getValue("type");
            String resetnames = at.getValue("resetnames");
            conreset = resetnames == null ? false : resetnames.equals("true") || resetnames.equals("yes") || resetnames.equals("1") || resetnames.startsWith("enable");
        } else if (qName.equals("citem")){
            conids.add(at.getValue("value"));
            conMaps.put(at.getValue("value"), at.getValue("name"));
        }
        else if (qName.equals("replace")) {
            String replaceTo = at.getValue("with");
            if (replaceTo == null)
                replaceTo = "";
            removables.add(new Tuple(at.getValue("pattern"), replaceTo));
        } else if (qName.equals("namechange")) {
            String to = at.getValue("to");
            to = to == null ? "" : to;
            nameChanges.put(at.getValue("prefix"), to);
        } else if (qName.equals("method"))
            methodCanonicals.put(at.getValue("signature"), at.getValue("name"));
        else if (qName.equals("delegate"))
            delegatePatterns.add(at.getValue("pattern"));
        else if (qName.equals("object"))
            definedObjects.add(at.getValue("name"));
        else if (qName.equals("ignore"))
            ignoreList.add(at.getValue("name"));
        else if(qName.equals("type")){
            dataTypeMapping.put(at.getValue("name"), at.getValue("map"));        
        } else if(qName.equals("class")){    
            className = at.getValue("name");
            noInternalConstructor = isTrue(at.getValue("no-internal-constructor"));
            propertyList = new ArrayList<XProperty>();
            methodList = new ArrayList<XMethod>();
            injectedMethodList = new ArrayList<XInjectedMethod>();
        } else if(qName.equals("selector")){
            requireAutoReleasePool = at.getValue("autoReleasePool");
            selectorName = at.getValue("name");
            isMandatory = isTrue(at.getValue("mandatory"));
            isDelegate = isTrue(at.getValue("delegate"));
            ignoreMethod = isTrue(at.getValue("ignore"));
            isOverridden = isTrue(at.getValue("override"));
            argList = new ArrayList<XArg>(); 
            isSelector = true;
        } else if(qName.equals("arg")){
            int flag = -1;
            if(isTrue(at.getValue("retain")))
                flag = org.crossmobile.source.xtype.XObject.RETAIN;
            else if(isTrue(at.getValue("replace")))
                flag = org.crossmobile.source.xtype.XObject.REPLACE;
            else if(isTrue(at.getValue("release")))
                flag = org.crossmobile.source.xtype.XObject.RELEASE;
            xarg = new XArg(at.getValue("type"), flag);
            if(argList == null)
                argList = new ArrayList<XArg>();
            argList.add(xarg);
        } else if(qName.equals("property")){
            int flag = -1;
            if(isTrue(at.getValue("retain")))
                flag = org.crossmobile.source.xtype.XObject.RETAIN;
            else if(isTrue(at.getValue("replace")))
                flag = org.crossmobile.source.xtype.XObject.REPLACE;
            property = new XProperty(at.getValue("name"), at.getValue("type"), flag);
            propertyList.add(property);
            isDelegate = false;
        } else if(qName.equals("alias")) { 
            if(aliasList == null)
                aliasList = new ArrayList<String>();
            aliasList.add(at.getValue("name"));
        } else if(qName.equals("reference")) {
            if(referenceList == null)
                referenceList = new ArrayList<String>();
            referenceList.add(at.getValue("class"));
        }else if(qName.equals("injected-method")){
            injMethod = new XInjectedMethod();
            injectedMethodName = at.getValue("name");
            injectedMethodModifier = at.getValue("modifier");
            isOverridden = isTrue(at.getValue("override"));
            isInjectedMethod = true;
        } else if(qName.equals("return")) {
            returnType = at.getValue("type");
            defaultRetunValue = at.getValue("default-value");
        } else if(qName.equals("code")){
            injectedCode = new StringBuilder();
            language = at.getValue("language");
            mode = at.getValue("mode");
            currentElement  = true;
        } else if(qName.equals("opaque")) {
            opaqueBaseType = at.getValue("base-type");
        }
        
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {       
        if(currentElement){
            String cdata = new String(ch, start, length);
            injectedCode.append(cdata);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("argumentid")) {
            argumentID.put(argsig, argids);
            argsig = null;
            argids = null;
        } else if (qName.equals("constructor")) {
            if (conids.size() < 2 && !conname.equals(""))
                Reporter.ADVISOR_LOADING_ERROR.report(null, "node coverload needs at least 2 subnodes to be valid");
            constructorOverload.put(consig, new CEnum(conname, conids, "Advisor", lastfile, conreset, conMaps));
            consig = null;
            conname = null;
            conids = null;
        } else if(qName.equals("selector")){
            xmethod = new XMethod(selectorName, argList, requireAutoReleasePool, isMandatory, defaultRetunValue);
            xmethod.setDelegate(isDelegate);
            xmethod.setReturnType(returnType);
            xmethod.setIgnore(ignoreMethod);
            xmethod.setOverridden(isOverridden);
            methodList.add(xmethod);
            argList = null;
            isMandatory = isDelegate = ignoreMethod = isSelector = isOverridden = false;
            defaultRetunValue = null;
            returnType = null;
            xmethod.setInjectedCode(selectorCodeList);
            selectorCodeList = null;
        } else if(qName.equals("class")){
            xobject = new XObject(className, methodList, propertyList, aliasList, referenceList, injectedMethodList);
            xobject.setOpaqueBaseType(opaqueBaseType);
            xobject.setNoInternalConstructor(noInternalConstructor);
            xobject.setInjectedCode(globalCodeList);
            classObject.put(className, xobject);
            methodList = null;
            propertyList = null;
            aliasList = null;
            referenceList = null;
            injectedMethodList = null;
            opaqueBaseType = null;
            globalCodeList = null;
            noInternalConstructor = false;
        } else if(qName.equals("injected-method")){
            injMethod.setReturnType(returnType);
            injMethod.setName(injectedMethodName);
            injMethod.setModifier(injectedMethodModifier);
            injMethod.setArguments(argList);
            injMethod.setOverridden(isOverridden);
            injectedMethodList.add(injMethod);
            argList = null;
            returnType = null;
            injMethod = null;
            isInjectedMethod = isOverridden = false;
        } else if(qName.equals("code")){
            code = new XCode(injectedCode.toString(), language, mode);  
            if(isSelector) {
                if(selectorCodeList == null)
                    selectorCodeList = new ArrayList<XCode>();
                selectorCodeList.add(code);
            } else if(isInjectedMethod){
                if(injMethod == null)
                    injMethod = new XInjectedMethod();
                injMethod.addCode(code);
            } else {
                if(globalCodeList == null)
                    globalCodeList = new ArrayList<XCode>();
                globalCodeList.add(code);
            }
            injectedCode = null;
            currentElement = false;
        }
       
    }
    
    private boolean isTrue(String attribute){
        return (attribute!=null && attribute.equals("true")) ? true : false;
    }

    public static void addDefinedObjects(CLibrary lib) {
        for (String obj : definedObjects)
            lib.getObject(obj);
    }
    
    public static Set<String> getDefinedObjects(){
        return definedObjects;
    }

    public static CEnum constructorOverload(String signature) {
        return constructorOverload.get(signature);
    }
  
    public static String getDataTypeMapping(String dataType){
        if(dataTypeMapping.containsKey(dataType))
            return dataTypeMapping.get(dataType);
        else
            return null;
    }

    public static List<String> argumentID(String signature) {
        return argumentID.get(signature);
    }

    public static String returnID(String signature) {
        return returnID.get(signature);
    }

    public static int genericsSupport(String object) {
        Integer i = genericsSupport.get(object);
        return i == null ? 0 : i.intValue();
    }

    public static boolean isNativeType(String type) {
        return nativeTypes.contains(type);
    }

    public static String convertData(String data) {
        for (Tuple replace : removables)
            data = data.replaceAll(replace.name, replace.value);
        return data;
    }

    public static boolean isInIgnoreList(String name) {
        return ignoreList.contains(name);
    }
    
    public static Map<String, String> getNameChanges() {
        return nameChanges;
    }

    public static String getMethodCanonical(String signature) {
        return methodCanonicals.get(signature);
    }
    
    public static Set<String> getDelegatePatterns() {
        return delegatePatterns;
    }

    public static void addDefaultTypedefs() {
        for (Tuple item : typedefs)
            CType.registerTypedef(item.name, item.value);
    }

    private static class Tuple {

        private String name;
        private String value;

        public Tuple(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }
    
    public static Map<String, XObject> getSpecialClasses(){
        return classObject;
    }
}
