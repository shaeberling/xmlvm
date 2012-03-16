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
public class AdvisorWrapper {

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
    public static boolean needsReplacer(String objectName) {
        return (Advisor.getSpecialClasses().containsKey(objectName) && Advisor.getSpecialClasses()
                .get(objectName).isReplace());
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
    public static boolean needsAccumulator(String objectName) {
        return (Advisor.getSpecialClasses().containsKey(objectName) && Advisor.getSpecialClasses()
                .get(objectName).isRetain());

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
        if ((obj = getSpecialClass(objName)) != null) {
            return obj.selectorHasInjectedCode(selName);
        }
        return false;
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
        if ((obj = getSpecialClass(objectName)) != null) {
            return obj.hasExternallyInjectedCode();
        }
        return false;
    }

    /**
     * Returns a list of code injections for a particular selector
     * 
     * @param objectName
     *            - name of the class
     * @return List of instances of XInjectedMethod class which contain the
     *         information for code injection
     */
    public static XInjectedMethod getInjectedCodeForSelector(String selName, String objectName) {
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
        if ((obj = getSpecialClass(name)) != null) {
            return obj.getReferences();
        }
        return null;
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
        if ((obj = getSpecialClass(objectName)) != null) {
            return obj.selectorIsMandatory(selName);
        }
        return false;
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
        if ((obj = getSpecialClass(objectName)) != null) {
            return obj.hasMandatoryMethods();
        }
        return false;
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
        if ((obj = getSpecialClass(objectName)) != null) {
            return obj.getDefaultReturnValueForSelector(selName);
        }
        return null;
    }
}
