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

package org.crossmobile.source.ctype;

import java.util.ArrayList;
import java.util.List;

public class ObjCSelector {
    private boolean staticSelector;
    private Variable returnType;
    private String leadingName;
    private List<Parameter> parameters;

    /**
     * Initialize the instance. See the "getter" methods for further descriptions.
     *
     * @param staticSelector true if the selector is static, else false
     * @param returnType the selector return type
     * @param leadingName the leading name for the selector
     * @param parameters the list of selector parameters or null to default to an empty list
     */
    public ObjCSelector(boolean staticSelector, Variable returnType, String leadingName,
            List<Parameter> parameters) {
        this.staticSelector = staticSelector;
        this.returnType = returnType;
        this.leadingName = leadingName;
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return "ObjCSelector[" + toObjCFormat() + "]";
    }

    public String toObjCFormat() {
        StringBuilder sb = new StringBuilder();
        sb.append(staticSelector ? "+ (" : "- (");
        sb.append(returnType.toObjCFormat());
        sb.append(")");
        sb.append(leadingName);
        int i = 0;
        for (Parameter p : getParameters()) {
            if (i++ != 0) {
                sb.append(" ");
            }
            sb.append(p.toObjCFormat());
        }
        return sb.toString();
    }

    public static class Variable {
        private String type;
        private boolean pointer;
        private boolean requiresConversion;

        /**
         * @param type
         *            the variable type, such as "NSObject", "int" or "id"
         * @param pointer
         *            true if the is variable a pointer. Specifically, does it
         *            have a "*"? Note that "id" types are an exception, which
         *            will be false.
         */
        public Variable(String type, boolean pointer, boolean requiresConversion) {
            this.type = type;
            this.pointer = pointer;
            this.requiresConversion = requiresConversion;
        }

        @Override
        public String toString() {
            return "ObjCSelector.Variable[" + toObjCFormat() + "]";
        }

        public String toObjCFormat() {
            return type + (pointer ? " *" : "");
        }

        /**
         * @return the variable type, such as "NSObject", "int" or "id"
         */
        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }

        /**
         * @return true if the is variable a pointer. Specifically, does it have
         *         a "*"? Note that "id" types are an exception, which will be
         *         false.
         */
        public boolean isPointer() {
            return pointer;
        }
        public void setPointer(boolean pointer) {
            this.pointer = pointer;
        }
        
        public boolean requiresConversion(){
            return requiresConversion;
        }
    }

    public static class Parameter {
        private String selectorParamName;
        private Variable type;
        private String trivialName;

        /**
         * @param selectorParamName
         *            the name of the parameter, which contributes to the
         *            uniqueness of the selector name, or null if there is no
         *            selector parameter name for this parameter.
         * @param type
         *            the type of parameter
         * @param trivialName
         *            the unimportant/replaceable parameter name, which does not
         *            contribute to the uniqueness of the selector name.
         */
        public Parameter(String selectorParamName, Variable type, String trivialName) {
            this.selectorParamName = selectorParamName;
            this.type = type;
            this.trivialName = trivialName;
        }

        @Override
        public String toString() {
            return "ObjCSelector.Parameter[" + toObjCFormat() + "]";
        }

        public String toObjCFormat() {
            boolean hasSelectorParamName = (selectorParamName == null
                    || selectorParamName.trim().equals(""));
            return (hasSelectorParamName ? "" : selectorParamName) + ":(" + type.toObjCFormat()
                    + ")" + trivialName;
        }

        /**
         * @return the name of the parameter, which contributes to the
         *         uniqueness of the selector name, or null if there is no
         *         selector parameter name for this parameter.
         */
        public String getSelectorParamName() {
            return selectorParamName;
        }
        public void setSelectorParamName(String selectorParamName) {
            this.selectorParamName = selectorParamName;
        }

        /**
         * @return the type of parameter
         */
        public Variable getType() {
            return type;
        }
        public void setType(Variable type) {
            this.type = type;
        }

        /**
         * @return the unimportant/replaceable parameter name, which does not
         *         contribute to the uniqueness of the selector name.
         */
        public String getTrivialName() {
            return trivialName;
        }
        public void setTrivialName(String trivialName) {
            this.trivialName = trivialName;
        }
    }

    /**
     * @return true if the selector is static, else false
     */
    public boolean isStaticSelector() {
        return staticSelector;
    }
    public void setStaticSelector(boolean staticSelector) {
        this.staticSelector = staticSelector;
    }

    /**
     * @return the selector return type
     */
    public Variable getReturnType() {
        return returnType;
    }
    public void setReturnType(Variable returnType) {
        this.returnType = returnType;
    }

    /**
     * Get the leading name of the selector.
     *
     * For example, the leading name is "myMethod" for either:
     * "- (NSObject *)myMethod:(NSObject *)p1 andValue:(int)p2;"
     * OR
     * "- (id)myMethod;"
     *
     * @return the leading name for the selector
     */
    public String getLeadingName() {
        return leadingName;
    }
    public void setLeadingName(String leadingName) {
        this.leadingName = leadingName;
    }

    /**
     * @return the non-null list of selector parameters, where the first
     *         parameter never has a selector parameter name. That is the
     *         responsibility of the leading name.
     */
    public List<Parameter> getParameters() {
        if (parameters == null) {
            parameters = new ArrayList<Parameter>();
        }
        return parameters;
    }
    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }
}
