/* Copyright (c) 2002-2011 by XMLVM.org
 *
 * Project Info:  http://www.xmlvm.org
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 */

package org.crossmobile.source.xtype;

import java.util.List;

import org.crossmobile.source.guru.Advisor;

/**
 * This class acts as an interface for the data that comes from the advisor.
 * 
 */
public class AdvisorMediator {

    /**
     * Checks if a class needs an extra argument in the header file - i.e.,
     * ADDITIONAL_INSTANCE_FIELDS. A C-reference to the delegate is kept to tell
     * the garbage collector about the association
     * 
     * @param classname
     *            - name of the class
     * @return true if a c-reference is needed for any of the arguments in the
     *         class; false otherwise.
     */
    public static boolean classHasReplacePolicy(String objectName) {
        return (Advisor.getSpecialClasses().containsKey(objectName) && Advisor.getSpecialClasses()
                .get(objectName).hasReplacePolicy());
    }

    /**
     * Returns the instance of the entry for a particular class if there is an
     * advice existing for this class in advisor.xml
     * 
     * @param name
     *            - name of the class
     * @return instance of the entry for a class if an entry exists in
     *         advisor.xml for the classname
     */
    public static XObject getSpecialClass(String name) {
        return Advisor.getSpecialClasses().get(name);
    }

    /**
     * Checks if an accumulative array needs to be created for a given class
     * 
     * @param classname
     *            - name of the class
     * @return true if accumulative array is required; false otherwise.
     */
    public static boolean classHasRetainPolicy(String objectName) {
        return (Advisor.getSpecialClasses().containsKey(objectName) && Advisor.getSpecialClasses()
                .get(objectName).hasRetainPolicy());

    }

    /**
     * In case of some convenience methods, obj-C puts the objects to
     * autorelease pool automatically. And if NSAutoreleasePool does not exist,
     * we get an error message. For this, autoReleaePool has to be created
     * explicitly.
     * 
     * @param sel
     *            - selector name which requires the autorelease pool to be
     *            created
     * @param objectName
     *            - the name of the class of which the selector is part of
     * @return true if autoReleasePool required; false otherwise
     */
    public static boolean needsAutoReleasePool(String sel, String objectName) {

        XObject obj = null;
        if ((obj = getSpecialClass(objectName)) != null) {
            XMethod m = null;
            if ((m = obj.getMethodInstance(sel)) != null && m.isAutoReleasePoolRequired())
                return true;
        }
        return false;
    }

    /**
     * In some cases, extra code needs to be injected at method level. This
     * information is provided via the advice. This method checks, if any code
     * has to be injected for a particular method.
     * 
     * @param selName
     *            - name of the selector
     * @param objName
     *            - name of the class
     * @return true if code has to be injected; false otherwise.
     */
    public static boolean selectorHasCodeInjection(String selName, String objName) {
        XObject obj = null;
        return ((obj = getSpecialClass(objName)) != null) ? obj.selectorHasInjectedCode(selName)
                : false;
    }

    /**
     * In some cases, extra code needs to be injected which exists outside the
     * objective-c API. This information is provided via the advice. This method
     * checks, if any code has to be injected for a particular class.
     * 
     * @param objectName
     *            - name of the class
     * @return true if code has to be injected; false otherwise.
     */
    public static boolean classHasExternallyInjectedCode(String objectName) {
        XObject obj = null;
        return ((obj = getSpecialClass(objectName)) != null) ? obj.hasExternallyInjectedCode()
                : false;
    }

    /**
     * Returns a list of code injections for a particular selector
     * 
     * @param objectName
     *            - name of the class
     * @return List of instances of XInjectedMethod class which contain the
     *         information for code injection
     */
    public static List<XCode> getInjectedCodeForSelector(String selName, String objectName) {
        return getSpecialClass(objectName).getInjectedCodeForSelector(selName);
    }

    /**
     * Returns a list of methods that need to be injected into a particular
     * class
     * 
     * @param objectName
     *            - name of the class
     * @return List of instances of XInjectedMethod class which contain the
     *         information for code injection
     */
    public static List<XInjectedMethod> getExternallyInjectedCode(String objectName) {
        return getSpecialClass(objectName).getExternallyInjectedMethods();
    }

    /**
     * There are some referenced classes that need to be specified via
     * XMLVMSkeletonOnly annotation's 'references' argument. This list is
     * specified using the advisor at the class level.
     * 
     * @param name
     *            - Name of the object
     * @return - List of classes being referenced if they exist; null otherwise.
     */
    public static List<String> getReferencesForObject(String name) {
        XObject obj = null;
        return ((obj = getSpecialClass(name)) != null) ? obj.getReferences() : null;
    }

    /**
     * Some methods of interfaces are mandatory and the developer has to
     * implement these in the applications. The methods which are mandatory are
     * left as abstract in the interface implementations (under
     * org.xmlvm.ios.adapter.*). The advice for this is specified via the
     * advisor.
     * 
     * @param selName
     *            - name of the selector
     * @param objectName
     *            - name of the class
     * @return - true if the method is mandatory
     */
    public static boolean methodIsMandatoryForObject(String selName, String objectName) {
        XObject obj = null;
        return ((obj = getSpecialClass(objectName)) != null) ? obj.selectorIsMandatory(selName)
                : false;
    }

    /**
     * If the class has mandatory methods, then the class should remain
     * 'abstract'. This method specifies if the class has mandatory methods or
     * not.
     * 
     * @param objectName
     *            - name of the class
     * @return - true if the method is mandatory
     */
    public static boolean objectHasMandatoryMethods(String objectName) {
        XObject obj = null;
        return ((obj = getSpecialClass(objectName)) != null) ? obj.hasMandatoryMethods() : false;
    }

    /**
     * The implementation for interfaces are given default return values as 0,
     * null false. But if this value has to be overridden by any specific
     * values, then it is specified using special advice.
     * 
     * @param selName
     *            - name of the selector
     * @param objectName
     *            - class name
     * @return - return the specified return value, null otherwise
     */
    public static String getDefaultReturnValue(String selName, String objectName) {
        XObject obj = null;
        return ((obj = getSpecialClass(objectName)) != null) ? obj
                .getDefaultReturnValueForSelector(selName) : null;
    }

    /**
     * There are methods(Eg:UIView.drawRect) which are not part of protocols but
     * can be overridden by developer. In such cases, we need wrappers to bridge
     * the Obj-C and generated C code.
     * 
     * @param selName
     *            - name of the selector
     * @param objectName
     *            - class name
     */
    public static boolean isDelegateMethod(String selName, String objectName) {
        XObject obj = null;
        return ((obj = getSpecialClass(objectName)) != null) ? obj.methodIsDelegate(selName)
                : false;
    }

    /**
     * There are methods(Eg:UIView.drawRect) which are not part of protocols but
     * can be overridden by developer. In such cases, we need wrappers to bridge
     * the Obj-C and generated C code.
     * 
     * @return
     */
    public static boolean classHasDelegateMethods(String objectName) {
        XObject obj = null;
        return ((obj = getSpecialClass(objectName)) != null) ? obj.hasDelegateMethods() : false;
    }

    /**
     * The Opaque types are special exceptions which cannot be derived from the
     * header files and he information is provided using the advice.
     * 
     * @param objectName
     *            - class name
     * @return - true if object derives from CFType (as specified in advisor),
     *         false otherwise
     */
    public static String getOpaqueBaseType(String objectName) {
        XObject obj = null;
        return ((obj = getSpecialClass(objectName)) != null) ? obj.getOpaqueBaseType() : null;
    }

    /**
     * Some classes like NSObject and CFType have their Internal constructors
     * defined in xmlvm-ios.h and need not be generated automatically since
     * these are special cases. This is specified via the advisor.
     * 
     * @param objectName
     *            - class name
     * @return - false if object does not need Internal constructor; true
     *         otherwise.
     */
    public static boolean needsInternalConstructor(String objectName) {
        XObject obj = null;
        return ((obj = getSpecialClass(objectName)) != null) ? !obj.noInternalConstructor() : true;
    }

    /**
     * There are Core Foundation Opaque types that derive from CFType and this
     * information needs to be provided using the advisor.
     * 
     * @param objectName
     *            - class name
     * @return - true if it is derived from CFType; false otherwise
     */
    public static boolean isCFOpaqueType(String objectName) {
        String opaqueType;
        return ((opaqueType = getOpaqueBaseType(objectName)) != null && opaqueType.equals("CFType")) ? true
                : false;
    }

    /**
     * In case a property has a specific type (for setters) or a selector has a
     * specific set of arguments such information is provided via the advisor to
     * have a strongly typed API.
     * 
     * @param name
     *            - name of the property/ selector
     * @param objectName
     *            - name of the class
     * @param isProperty
     *            - true if it is a property
     * @return - true if it has specific types defined in advisor, false
     *         otherwise.
     */
    public static boolean hasSpecialArgumentsDefined(String name, String objectName,
            boolean isProperty) {
        XObject obj = null;
        if (isProperty)
            return (obj = getSpecialClass(objectName)) != null ? (obj.getPropertyType(name) != null ? true
                    : false)
                    : false;
        else
            return ((obj = getSpecialClass(objectName)) != null) ? obj
                    .selectorHasArgumentsDefined(name) : false;
    }

    /**
     * In case a property has a specific type (for getters) or a selector has a
     * specific return type such information is provided via the advisor to have
     * a strongly typed API.
     * 
     * @param name
     *            - name of the property/ selector
     * @param objectName
     *            - name of the class
     * @param isProperty
     *            - true if it is a property
     * @return - true if it has specific return type defined in advisor, false
     *         otherwise.
     */
    public static boolean hasSpecialReturnType(String name, String objectName, boolean isProperty) {
        XObject obj = null;
        if (isProperty)
            return (obj = getSpecialClass(objectName)) != null ? (obj.getPropertyType(name) != null ? true
                    : false)
                    : false;
        else
            return ((obj = getSpecialClass(objectName)) != null) ? obj
                    .selectorHasReturnTypeDefined(name) : false;
    }

    /**
     * In case a selector has a specific return type such information is
     * provided via the advisor to have a strongly typed API.This method returns
     * the return type specified for a selector in the advise.
     * 
     * @param selName
     *            - name of the selector
     * @param objectName
     *            - name of the class
     * @return - return type specified in the advisor
     */
    public static String getSelectorReturnType(String selName, String objectName) {
        return getSpecialClass(objectName).getMethodInstance(selName).getReturnType();
    }

    /**
     * In case a selector's actual arguments obtained by parsing the objective C
     * APIs have to be replaced with new set of arguments( Eg:
     * UIControl.addTarget) it needs to be specified via the advisor. The advise
     * should contain the entire set of arguments for the method in order. This
     * method returns the list of arguments specified in the advisor for a
     * selector.
     * 
     * @param selName
     *            - name of the selector
     * @param objectName
     *            - name of the class
     * @return - list of arguments for a selector
     */
    public static List<XArg> getArgumentsForMethod(String selName, String objectName) {
        return getSpecialClass(objectName).getMethodInstance(selName).getArgList();
    }

    /**
     * In case a property has specific type (EG: List<UIViewController>) such
     * information is provided via the advisor to have a strongly typed API.
     * 
     * @param property
     *            - name of the property
     * @return - type of the property specified in the advisor
     */
    public static String getPropertyType(String property, String objectName) {
        XObject obj = null;
        return ((obj = getSpecialClass(objectName)) != null) ? obj.getPropertyType(property) : null;
    }

    /**
     * Some system APIs are applicable only for MAC OS and not applicable to
     * iPhone. Including these APIs in generation of C back-end will result in
     * compile errors. Currently this is specified using advice. A better
     * solution is to find this while parsing the headers.
     * 
     * @param selName
     *            - Name of the selector
     * @param objectName
     *            - name of the class
     * @return - true if not applicable to iphone; false otherwise
     */
    public static boolean methodIsIgnore(String selName, String objectName) {
        XObject obj = null;
        XMethod meth = null;
        return (obj = getSpecialClass(objectName)) != null ? ((meth = obj
                .getMethodInstance(selName)) != null ? meth.isIgnore() : false) : false;
    }

    /**
     * There are instances when code has to be injected at class level between
     * // XMLVM_BEGIN_IMPLEMENTATION and //XMLVM_END_IMPLEMENTATION. This is
     * specified using the advisor.
     * 
     * @param objectName
     *            - Name of the class
     * @return true if there is code to be injected at class level; false
     *         otherwise
     */
    public static boolean objectHasGlobalCodeInjection(String objectName) {
        XObject obj = null;
        return (obj = getSpecialClass(objectName)) != null ? obj.hasGlobalCodeInjection() : false;
    }

    /**
     * This method is used to return code that has to be injected at class level
     * between // XMLVM_BEGIN_IMPLEMENTATION and //XMLVM_END_IMPLEMENTATION.
     * 
     * @param objectName
     *            - Name of the class
     * @return return the list of code snippet for injection
     */
    public static List<XCode> getInjectedCodeForObject(String objectName) {
        return getSpecialClass(objectName).getGlobalCodeToInject();
    }

}
