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

package org.crossmobile.source.ctype;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.crossmobile.source.guru.Advisor;
import org.crossmobile.source.guru.Reporter;
import org.crossmobile.source.utils.StringUtils;
import org.crossmobile.source.parser.Stream;

public abstract class CSelector extends CAnyMethod {

    private final List<CArgument> arguments;
    protected final List<String> nameParts;

    public static CSelector create(CObject parent, boolean isStatic, CType returnType, List<String> methodParts, List<CArgument> args) {
        String methodName = methodParts.get(0);
        boolean constructor = methodName.startsWith("init") && !methodName.equals("initialize");
        if (constructor)
            methodName = "";

        String signature = getSignature(parent.getName(), methodName, args);

        returnType = fixGenericsConflict(returnType, args, signature);
        fixArgumentsIDConflict(parent, args, signature);
        if (constructor)
            return new CConstructor(args, methodParts);
        else {
            returnType = fixReturnIDConflict(parent, isStatic, returnType, methodName, signature);
            return new CMethod(methodName, parent.isProtocol(), args, methodParts, isStatic, returnType);
        }
    }

    private static CType fixGenericsConflict(CType returnType, List<CArgument> arg, String signature) {

        return returnType;
    }

    private static CType fixReturnIDConflict(CObject parent, boolean isStatic, CType returnType, String methodName, String signature) {
        // Check if the return value is ID, so that it needs to be properly reported            
        if (returnType.isID()) {
            // Unknown method, first consult conflict solver
            String newtype = Advisor.returnID(signature);
            if (newtype != null)
                returnType = new CType(newtype);
            else {
                // Check if this is a convenience selector
                String simplename = parent.getName().toLowerCase().substring(2);
                if (simplename.startsWith("mutable"))
                    simplename = simplename.substring(7);
                if (isStatic && methodName.toLowerCase().startsWith(simplename))    // convenient method
                    returnType = new CType(parent.getName());
                else // Use generics as a last resort 
                if (parent.getGenericsCount() > 0)
                    returnType = new CType("A");
                else {
                    returnType = new CType("Object");
                    Reporter.UNKNOWN_ID.report("return", signature);
                }
            }
        }
        return returnType;
    }

    private static void fixArgumentsIDConflict(CObject parent, List<CArgument> args, String signature) {
        int conflicts = 0;
        for (CArgument arg : args)
            if (arg.type.isID())
                conflicts++;
        if (conflicts == 0)
            return;

        List<String> newtypes = Advisor.argumentID(signature);
        if (newtypes != null) {
            if (newtypes.size() != conflicts)
                Reporter.MISMATCH_ID_RESOLVER.report("want=" + conflicts + " provided=" + newtypes.size(), signature);
            int loc = 0;
            for (CArgument arg : args)
                if (arg.type.isID())
                    arg.type = new CType(newtypes.get(loc++)); // Might throw array index out of bounds, if ID arguments and 
        } else if (parent.getGenericsCount() > 1)  // Use generics as a last resort
            for (CArgument arg : args)
                if (arg.type.isID())
                    arg.type = new CType("A");
                else {
                    arg.type = new CType("Object");
                    Reporter.UNKNOWN_ID.report("given argument", signature);
                }
    }

    public CSelector(String name, boolean isAbstract, List<CArgument> arguments, List<String> nameParts) {
        super(name, isAbstract);
        this.arguments = arguments;
        this.nameParts = nameParts;
    }

    public final String getSignature(String objectname) {
        return getSignature(objectname, name, arguments);
    }

    private static String getSignature(String parent, String name, List<CArgument> arguments) {
        StringBuilder out = new StringBuilder(parent);
        out.append(":").append(name).append(":");
        for (CArgument arg : arguments)
            out.append(arg.type);
        return out.toString();
    }

    public List<CArgument> getArguments() {
        return arguments;
    }

    public static void parse(CObject parent, Stream s) {
        String fullText = s.consumeBlock();

        boolean isStatic = fullText.charAt(0) == '+';
        String body = fullText.substring(1, fullText.length() - 1).trim();

        String rtrn = "id";
        if (body.startsWith("(")) {
            int param = StringUtils.matchFromStart(body, '(', ')');
            rtrn = body.substring(1, param);
            body = body.substring(param + 1);
        }
        if (CType.isFunctionPointer(rtrn, "selector return"))
            rtrn = CType.FUNCPOINT;
        CType returnType = new CType(rtrn);

        List<CArgument> args = new ArrayList<CArgument>();
        List<String> methodParts = new ArrayList<String>();
        if (!body.contains(":"))
            // No arguments, only method name
            methodParts.add(body);
        else {
            // At least one argument: parsing
            StringTokenizer tk = new StringTokenizer(body, ":");
            methodParts.add(tk.nextToken().trim());  // Add first token as method name
            while (tk.hasMoreTokens()) {
                ArgumentResult res = CArgument.getSelectorArgument(tk.nextToken().trim());
                args.add(res.argument);
                methodParts.add(res.selectorPart);
            }
            methodParts.remove(methodParts.size() - 1); // Selectors end with argument definition, not argument name
        }
        CSelector sel = CSelector.create(parent, isStatic, returnType, methodParts, args);
        sel.addDefinition(fullText);
        parent.addSelector(sel);
    }

    public static class ArgumentResult {

        private String selectorPart;
        private CArgument argument;

        public ArgumentResult(CArgument argument, String selectorPart) {
            this.selectorPart = selectorPart;
            this.argument = argument;
        }
    }
}
