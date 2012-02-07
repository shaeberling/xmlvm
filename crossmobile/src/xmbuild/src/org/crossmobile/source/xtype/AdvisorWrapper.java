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
    public static boolean needsReplacer(String classname) {
        return (Advisor.getSpecialClasses().containsKey(classname) && Advisor.getSpecialClasses()
                .get(classname).isReplace());
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
    public static boolean needsAccumulator(String classname) {
        return (Advisor.getSpecialClasses().containsKey(classname) && Advisor.getSpecialClasses()
                .get(classname).isRetain());

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
     * In some cases, extra code needs to be injected which exists outside the
     * objective-c API. This information is provided via the advice. This method
     * checks, if any code has to be injected for a particular class.
     * 
     * @param objectName
     *            - name of the class
     * @return true if code has to be injected; false otherwise.
     */
    public static boolean classHasInjectedCode(String objectName) {
        XObject obj = null;
        if ((obj = getSpecialClass(objectName)) != null) {
            return obj.hasInjectedCode();
        }
        return false;
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
    public static List<XInjectedMethod> getInjectedMethods(String objectName) {
        return getSpecialClass(objectName).getInjectedMethods();
    }
}
