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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.crossmobile.source.guru.Reporter;
import org.crossmobile.source.parser.BlockType;
import org.crossmobile.source.parser.Stream;

public class CAny implements Serializable {
    private static final long serialVersionUID = 1L;

    public final String name;
    private List<String> definition = new ArrayList<String>(2);

    public CAny(String name) {
        if (name.equals("final"))
            name = "finalValue";
        if (name.equals("interface"))
            name = "interfaceValue";
        this.name = name;
    }

    public void addDefinition(String definition) {
        this.definition.add(definition);
    }

    public void appendDefinitions(CAny other) {
        definition.addAll(other.definition);
    }

    public Iterable<String> getDefinitions() {
        return definition;
    }

    public static void parse(CLibrary lib, String data) {
        parse(lib, new Stream(data), null);
    }

    public static void parse(CLibrary lib, Stream s, CObject strct) {
        CObject lastObject = null;
        s.consumeSpaces();
        BlockType type;
        boolean istypedef;
        boolean isRequired;
        boolean isProtocol;

        while ((type = s.peekBlockType()) != BlockType.EOF) {
            String z = s.peekBlock();
            istypedef = false;
            switch (type) {
                case OPTIONAL:
                case REQUIRED:
                    if (strct != null)
                        throw new RuntimeException("Illegal context: object inside struct");
                    isRequired = type == BlockType.REQUIRED;
                    s.consumeBlock();
                    break;
                case PROTOCOLSTART:
                case OBJECTSTART:
                    if (strct != null)
                        throw new RuntimeException("Illegal context: object inside struct");
                    isProtocol = type == BlockType.PROTOCOLSTART;
                    s.consumeBlock();
                    lastObject = CObject.parse(lib, isProtocol, s);
                    isRequired = isProtocol;    // by default, protocol selectors are required. If it is a class, (not a protocol) it is not required (it has default implementation)
                    break;
                case PROPERTY:
                    if (strct != null)
                        throw new RuntimeException("Illegal context: object inside struct");
                    if (lastObject == null)
                        throw new NullPointerException("Enclosing object not found!");
                    s.consumeBlock();
                    CProperty.parse(lastObject, s);
                    break;
                case SELECTOR:
                    if (strct != null)
                        throw new RuntimeException("Illegal context: object inside struct");
                    if (lastObject == null)
                        throw new NullPointerException("Enclosing object not found!");
                    CSelector.parse(lastObject, s);
                    break;
                case OBJECTEND:
                    if (strct != null)
                        throw new RuntimeException("Illegal context: object inside struct");
                    Reporter.setObject(null);
                    lastObject = null;
                    s.consumeBlock();
                    break;
                case TYPEDEFFUNCTION:
                    istypedef = true;
                case FUNCTION:
                    if (!CType.isFunctionPointer(s.peekBlock(), "typedef"))
                        CFunction.create(lib, s.peekBlock());
                    s.consumeBlock();
                    break;
                case TYPEDEFENUM:
                    istypedef = true;
                case ENUM:
                    if (strct != null)
                        CStruct.foundEnum(lib, strct, istypedef, s.consumeBlock());
                    else
                        CEnum.create(lib, istypedef, s.consumeBlock());
                    break;
                case TYPEDEFSTRUCT:
                    istypedef = true;
                case STRUCT:
                    if (strct != null)
                        CStruct.foundStruct(lib, strct, istypedef, s.consumeBlock());
                    else
                        CStruct.create(lib, istypedef, s.consumeBlock(), false);
                    break;
                case TYPEDEFEXTERNAL:
                    istypedef = true;
                case EXTERNAL:
                    if (strct != null)
                        CStruct.foundArg(lib, strct, istypedef, s.consumeBlock());
                    else
                        CArgument.create(lib, lib, istypedef, s.consumeBlock());
                    break;
                case CLOSEBRACKET:
                    s.consumeChars(1);
                    break;
                default:
                    Reporter.addUnknownItem(lastObject, s.consumeBlock());
            }
        }
    }
}
