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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.crossmobile.source.ctype.CArgument;
import org.crossmobile.source.ctype.CConstructor;
import org.crossmobile.source.ctype.CFunction;
import org.crossmobile.source.ctype.CLibrary;
import org.crossmobile.source.ctype.CMethod;
import org.crossmobile.source.ctype.CObject;
import org.crossmobile.source.ctype.CSelector;

public class Oracle {

    /**
     * Break name into parts, required to group a procedural item to an obj-c object
     * @param data The full name of the procedural item
     * @return the name in parts
     */
    public static List<String> canonical(String data) {
        List<String> res = new ArrayList<String>();
        if (data == null || data.length() == 0)
            return res;

        int from = 0;
        int to = 0;
        boolean wasInLower = false;
        char c;
        data = nameBeautifier(data);
        while (to < data.length()) {
            c = data.charAt(to);
            if (c >= 'A' && c <= 'Z') {
                if (wasInLower) {
                    wasInLower = false;
                    res.add(data.substring(from, to));
                    from = to;
                }
            } else if (c >= 'a' && c <= 'z' && !wasInLower)
                wasInLower = true;
            to++;
        }
        if (from < to)
            res.add(data.substring(from, to));
        return res;
    }

    public static void positionObjects(CLibrary lib) {
        SortedSet<CObject> objects = new TreeSet<CObject>(new Comparator<CObject>() {

            @Override
            public int compare(CObject o1, CObject o2) {
                return -o1.name.compareTo(o2.name);
            }
        });
        for (CObject item : lib.getObjects())
            objects.add(item);

        Set<CFunction> found = new HashSet<CFunction>();
        Set<CFunction> missing = new HashSet<CFunction>();
        boolean still_missing;
        for (CFunction f : lib.getFunctions()) {
            still_missing = true;
            for (CObject o : objects)
                if (f.name.startsWith(o.name)) {
                    String fname = f.name.substring(o.name.length());
                    String lfname = fname.toLowerCase();
                    fname = Character.toLowerCase(fname.charAt(0)) + fname.substring(1);
                    CSelector s;
                    if (lfname.equals("make") || fname.equals("create") || fname.equals("new"))
                        s = new CConstructor(f.getParameters(), canonical(fname));
                    else {
                        List<CArgument> args = f.getParameters();
                        boolean isStatic = true;
                        if (!args.isEmpty() && Oracle.nameBeautifier(args.get(0).getType().getProcessedName()).equals(o.name)) {
                            isStatic = false;
                            args.remove(0);
                        }
                        s = new CMethod(fname, false, args, canonical(fname), isStatic, f.getResult());
                    }
                    s.addDefinition(f.definition);
                    o.addSelector(s);
                    still_missing = false;
                    found.add(f);
                    break;
                }
            if (still_missing)
                missing.add(f);
        }
        for (CFunction f : missing) {
            CSelector s = new CMethod(f.name, false, f.getParameters(), canonical(f.name), true, f.getResult());
            s.addDefinition(f.definition);
            lib.getObject(f.framework).addSelector(s);
        }
    }

    public static String nameBeautifier(String name) {
        if (name.isEmpty())
            return name;

        String orig = name;
        Map<String, String> newnames = Advisor.getNameChanges();
        for (String replace : newnames.keySet())
            if (name.startsWith(replace)) {
                name = newnames.get(replace) + name.substring(replace.length());
                continue;
            }
        char t = name.charAt(0);
        if (!(t >= 'A' && t <= 'Z') && !Advisor.isNativeType(name))
            Reporter.GROUPING_ERROR.report("unsupported character", orig);
        if (name.endsWith("Ref"))
            name = name.substring(0, name.length() - 3);
        if (name.isEmpty())
            throw new NullPointerException("Name is empty (original is " + orig + ")");
        return name;
    }

    /**
     * Find unique names for selectors with multiple arguments
     * @param methods list of selectors, with their unique names
     * @param isInterface use this parameter to hint the automatic method resolver that this is a delegate and the first part might be copied over and over
     * @return  list of normalized names
     */
    public static List<String> findUniqueNames(List<List<String>> methods, List<Boolean> isStatic, boolean isDelegate) {
        int size = methods.size();
        int stillActive = size;
        List<String> res = new ArrayList<String>(size);

        boolean[] pending = new boolean[size];  // Set to false, when a selector has been fixed
        List<Boolean> using = new ArrayList<Boolean>();  // For every level deep, note if we should take into account this level or not
        for (int i = 0; i < size; i++) {
            pending[i] = true;
            res.add(null);    // pre-set places, in order to use "set" instead of "add"
        }

        int depth = 0;
        int firstMissing;
        int secondMissing;
        Set<String> alreadyFound = new HashSet<String>();
        Map<String, Integer> unique = new HashMap<String, Integer>();
        String key;
        while (stillActive > 0) {

            // Check if a selector does not have any more items. This check is required since it might have the *same* section name with others, but no more other sections
            firstMissing = -1;
            secondMissing = -1;
            for (int i = 0; i < size; i++)  // For every selector...
                if (pending[i])  // ... which we still take into account ...
                    if (methods.get(i).size() == depth) // ... check if it just finished
                        if (firstMissing == -1)  //This is the first one
                            firstMissing = i;
                        else // Second found
                        if (isStatic.get(i).equals(isStatic.get(firstMissing)) || secondMissing >= 0) {  // one is static, the other not, OR third found
                            pending[i] = false; // skip it
                            stillActive--;
                            Reporter.GROUPING_ERROR.report("same signature", methods.get(i).toString());
                        } else {
                            secondMissing = i;
                            int whichIsStatic = isStatic.get(firstMissing) ? firstMissing : secondMissing;
                            methods.get(whichIsStatic).set(depth - 1, methods.get(whichIsStatic).get(depth - 1) + "Static");
                        }
            if (firstMissing >= 0) {  // Found item with slector that has finished
                using.set(using.size() - 1, Boolean.TRUE);    // Should use it!!!
                res.set(firstMissing, getName(methods.get(firstMissing), using, isDelegate)); // Found at least one 
                stillActive--;
                pending[firstMissing] = false;
                if (secondMissing >= 0) {  // Found item with slector that has finished
                    res.set(secondMissing, getName(methods.get(secondMissing), using, isDelegate)); // Found a second one 
                    stillActive--;
                    pending[secondMissing] = false;
                }
            }

            if (stillActive == 0)
                break;
            if (stillActive == 1) {   // Only one waiting, get it's name and we're done
                using.add(Boolean.TRUE);    // Add one more level, to distinguish with selector that has just finished
                for (int i = 0; i < size; i++)  // find it
                    if (pending[i]) {
                        stillActive--;
                        res.set(i, getName(methods.get(i), using, isDelegate));
                        break;
                    }
            } else {
                alreadyFound.clear();
                unique.clear();
                for (int i = 0; i < size; i++)  // Find unique keys
                    if (pending[i]) {
                        key = methods.get(i).get(depth);
                        if (alreadyFound.contains(key))
                            unique.remove(key); // it is not unique any more
                        else {
                            alreadyFound.add(key);  // remember it for future usage
                            unique.put(key, i);     // possibly it's unique
                        }
                    }
                if (unique.size() > 0) {    // Found at least one unique item
                    using.add(Boolean.TRUE);    // ... so take this part into account
                    for (int i : unique.values()) { // set unique items and remove them from list
                        res.set(i, getName(methods.get(i), using, isDelegate));
                        pending[i] = false;
                    }
                    stillActive -= unique.size();   // active items are reduces by the number of unique items found
                } else
                    using.add(Boolean.FALSE);       // No unique found, remove it from list
            }
            depth++;
        }
        return res;
    }

    private static String getName(List<String> selector, List<Boolean> using, boolean isDelegate) {
        StringBuilder out = new StringBuilder();
        String part;
        for (int i = 0; i < using.size(); i++)  // Probably we won't need the whole selector name
            if ((i == 0 && !isDelegate) || using.get(i)) { // ignore parts that are the same
                part = selector.get(i);
                if (out.length() != 0) // capitalize if not the first part
                    part = part.substring(0, 1).toUpperCase() + part.substring(1);
                else
                    part = part.substring(0, 1).toLowerCase() + part.substring(1);
                out.append(part);
            }
        return out.toString();
    }
}
