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

package org.crossmobile.source.out.cutils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.crossmobile.source.ctype.ObjCSelector;
import org.crossmobile.source.ctype.ObjCSelector.Parameter;
import org.crossmobile.source.ctype.ObjCSelector.Variable;

public class ObjCSelectorUtil {

    private static final Pattern OBJ_C_SELECTOR_PATTERN;
    private static final Pattern OBJ_C_PARAMETER_PATTERN;
    private static final int MIN_SELECTOR_PARTS = 5;
    static {
        String regexBegin = "^";
        // Regex for e.g. "(NSObject *)", "(int)" or "(id <QLPreviewItem>)", having 3 groups: the type, the type inside the "<>" and either the "*" or an empty string
        String variableTypeRegex = "\\(\\s*([^\\s\\*\\)]*)\\s*<?(\\w*)>?\\s*(\\*?)\\s*\\)";
        // Regex for a parameter, having 5 groups: selector param name, the three values from "variableTypeRegex", and the param name (not critical)
        String parameterRegex = "\\s*(\\w*)\\s*:\\s*" + variableTypeRegex + "\\s*(\\w+)";

        // $1 = The "+" or "-" for static or non-static
        // $2 = return type excluding the "*"
        // $3 = anything inside the "<>" from the return type
        // $4 = the "*" or empty string from the return type
        // $5 = selector leading name
        String regex1 = "\\s*([\\+-])\\s*" + variableTypeRegex + "\\s*(\\w+)";
        // $6 and up = selector parameter values
        String regex2 = "((" + parameterRegex + ")*)";
        String regexEnd = ".*$";

        OBJ_C_SELECTOR_PATTERN = Pattern.compile(regexBegin + regex1 + regex2 + regexEnd);
        OBJ_C_PARAMETER_PATTERN = Pattern.compile(parameterRegex);
    }

    /**
     * Convert a String full Obj-C selector definition into an instance.
     * 
     * An example full Obj-C selector definition is
     * "- (NSObject *)myMethod:(NSObject *)p1 andValue:(int)p2;"
     * 
     * @param selectorDefinition the full Obj-C selector definition
     * @return the selector definition as an instance or null if it couldn't be created
     */
    public static ObjCSelector toObjCSelector(String selectorDefinition) {
        ObjCSelector result = null;
        List<String> selectorParts = breakupObjCSelector(selectorDefinition);
        if (selectorParts != null && selectorParts.size() >= MIN_SELECTOR_PARTS) {
            Iterator<String> iter = selectorParts.iterator();
            boolean staticSelector = "+".equals(iter.next());
            Variable returnType = toObjCSelectorVariable(iter);
            String leadingName = iter.next();

            List<Parameter> parameters = new ArrayList<Parameter>();
            while (iter.hasNext()) {
                String selectorParamName = iter.next();
                Variable type = toObjCSelectorVariable(iter);
                String trivialName = iter.next();
                parameters.add(new Parameter(selectorParamName, type, trivialName));
            }
            result = new ObjCSelector(staticSelector, returnType, leadingName, parameters);
        }

        return result;
    }

    private static Variable toObjCSelectorVariable(Iterator<String> iter) {
        String type = iter.next();
        iter.next(); // throw out this value
        boolean pointer = "*".equals(iter.next());
        return new Variable(type, pointer);
    }

    /**
     * Break up a full Obj-C selector definition into String parts.
     *
     * An example full Obj-C selector definition is
     * "- (NSObject *)myMethod:(NSObject *)p1 andValue:(int)p2;"
     *
     * @param selectorDefinition the full Obj-C selector definition
     * @return the individual parts of the Obj-C selector
     */
    private static List<String> breakupObjCSelector(String selectorDefinition) {
        List<String> values = new ArrayList<String>();

        Matcher m1 = OBJ_C_SELECTOR_PATTERN.matcher(selectorDefinition);
        while (m1.find()) {
            // Include the first selector parts described in the static initializer
            append(values, m1, 1, MIN_SELECTOR_PARTS);

            // Include the parameters as described in the static initializer
            String parameterContents = m1.group(MIN_SELECTOR_PARTS + 1);
            if (parameterContents != null) {
                Matcher m2 = OBJ_C_PARAMETER_PATTERN.matcher(parameterContents);
                while (m2.find()) {
                    append(values, m2, 1, m2.groupCount());
                }
            }
        }

        return values;
    }

    private static void append(List<String> values, Matcher m, int startIndex, int endIndexInclusive) {
        for (int i = startIndex; i <= endIndexInclusive; i++) {
            values.add(m.group(i));
        }
    }

    public static void main(String[] args) {
        String[][] originalAndExpected = new String[][] {
                new String[]{
                        "- (void)netServiceWillPublish:(NSNetService *)sender;"
                        ,"- (void)netServiceWillPublish:(NSNetService *)sender"
                }
                ,new String[]{
                        "+ (void) netService:(NSNetService *)sender didNotPublish:(NSDictionary *)errorDict"
                        ,"+ (void)netService:(NSNetService *)sender didNotPublish:(NSDictionary *)errorDict"
                }
                ,new String[]{
                        "-(NSObject *) Hello2You4:(id) yo"
                        ,"- (NSObject *)Hello2You4:(id)yo"
                }
                ,new String[]{
                        " + (id)What2Say: (int)yo "
                        ,"+ (id)What2Say:(int)yo"
                }
                ,new String[]{
                        "- (void)netServiceWillPublish:(NSNetService *)sender"
                        ,"- (void)netServiceWillPublish:(NSNetService *)sender"
                }
                ,new String[]{
                        "- (NSObject*)myNoParamMethod"
                        ,"- (NSObject *)myNoParamMethod"
                }
                ,new String[]{
                        "- (id)mySecondMethod:(int)firstParam andString:(NSString*)str"
                        ,"- (id)mySecondMethod:(int)firstParam andString:(NSString *)str"
                }
                ,new String[]{
                        "- (id)mySecondMethod:(int)firstParam :(NSString*)str"
                        ,"- (id)mySecondMethod:(int)firstParam :(NSString *)str"
                }
                ,new String[]{
                        " - ( NSObject * ) spacesEverywhere : ( NSObject * ) firstParam andString : ( NSString * ) str ; "
                        ,"- (NSObject *)spacesEverywhere:(NSObject *)firstParam andString:(NSString *)str"
                }
                ,new String[]{
                        "- (id <QLPreviewItem>)previewController:(QLPreviewController *)controller previewItemAtIndex:(NSInteger)index;"
                        ,"- (id)previewController:(QLPreviewController *)controller previewItemAtIndex:(NSInteger)index"
                }
        };
        for (String[] origAndExpected : originalAndExpected) {
            List<String> selectorParts = breakupObjCSelector(origAndExpected[0]);
            for (String part : selectorParts) {
                System.out.print(part + "|");
            }
            System.out.println();
        }

        System.out.println("-------------------------");
        for (String[] origAndExpected : originalAndExpected) {
            ObjCSelector sel = toObjCSelector(origAndExpected[0]);
            String output = sel.toObjCFormat();
            System.out.println(output);
            if (!output.equals(origAndExpected[1])) {
                throw new RuntimeException("Output \"" + output + "\" does not match expected for \"" + origAndExpected[1] + "\"");
            }
        }
    }

}
