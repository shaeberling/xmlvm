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
import org.crossmobile.source.guru.Advisor;

public class CConstructor extends CSelector {

    private CEnum overloadenum;
    private boolean isOverloaded = false;

    public CConstructor(List<CArgument> arguments, List<String> nameparts) {
        super("", false, arguments, nameparts);
    }

    public boolean isOverloaded() {
        return isOverloaded;
    }

    public CEnum getEnum() {
        return overloadenum;
    }

    public void updateEnum(String signature) {
        overloadenum = Advisor.constructorOverload(signature);
        if (overloadenum != null) {
            isOverloaded = true;
            if (overloadenum.name.equals(""))  // No name means we simply ignore these constructor enumerations
                overloadenum = null;
            else if (overloadenum.resetsArgNames()) {
                List<CArgument> cargs = getArguments();
                List<CArgument> args = new ArrayList<CArgument>(cargs);
                cargs.clear();
                for (int i = 0; i < args.size(); i++)
                    cargs.add(new CArgument(args.get(i).type, "arg" + (i + 1)));
            }
        }
    }
}
