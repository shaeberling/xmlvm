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

package org.crossmobile.source.parser;

import org.crossmobile.source.utils.StringUtils;

public final class Stream {

    private final String buffer;
    private final int size;
    private int location;
    private BlockType nextype = null;
    private int blockLimit = -1;

    public Stream(String buffer) {
        this.buffer = buffer;
        this.size = buffer.length();
    }

    public BlockType peekBlockType() {
        findBlock();
        return nextype;
    }

    public String peekBlock() {
        findBlock();
        return buffer.substring(location, blockLimit);
    }

    public String consumeBlock() {
        String res = peekBlock();
        location = blockLimit;
        unsetBlock();
        return res;
    }

    public char peekChar() {
        consumeSpaces();
        return buffer.charAt(location);
    }

    public String peekChars(int howmany) {
        consumeSpaces();
        if (howmany < 0)
            howmany = 0;
        if (howmany + location > size)
            howmany = size - location;
        return buffer.substring(location, location + howmany);
    }

    public String consumeBalanced(char open, char close) {
        consumeSpaces();
        int newloc = StringUtils.matchBalanced(buffer, location, open, close, 1);
        String res = "";
        if (newloc >= 0) {
            newloc++;
            res = buffer.substring(location, newloc);
            location = newloc;
        }
        unsetBlock();
        return res;
    }

    public String consumeID() {
        consumeSpaces();
        int from = location;
        char c;
        while (location < size && ((c = buffer.charAt(location)) == '_' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')))
            location++;
        String token = buffer.substring(from, location);
        unsetBlock();
        return token;
    }

    public String consumeChars(int howmany) {
        consumeSpaces();
        if (howmany < 0)
            howmany = 0;
        if (howmany + location > size)
            howmany = size - location;
        String part = buffer.substring(location, location + howmany);
        location += howmany;
        unsetBlock();
        return part;
    }

    public void consumeSpaces() {
        char c;
        while (location < size && ((c = buffer.charAt(location)) == ' ' || c == '\n' || c == '\t' || c == '\r' || c == '\f'))
            location++;
    }

    private void unsetBlock() {
        nextype = null;
        blockLimit = -1;
    }

    private void findBlock() {
        if (nextype != null)
            return;

        consumeSpaces();
        if (location >= size) {
            nextype = BlockType.EOF;
            blockLimit = size;
            return;
        }
        char c;
        blockLimit = location;
        if (buffer.charAt(location) == '@') // @-based block
            while (blockLimit < size && ((c = buffer.charAt(blockLimit)) == '@' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_'))
                blockLimit++;
        else
            blockLimit = StringUtils.searchForMatchingBlock(buffer, location, ';');
        c = buffer.charAt(location);
        if (c == '-' || c == '+')
            nextype = BlockType.SELECTOR;
        else if (buffer.regionMatches(location, "@optional", 0, 9))
            nextype = BlockType.OPTIONAL;
        else if (buffer.regionMatches(location, "@required", 0, 9))
            nextype = BlockType.REQUIRED;
        else if (buffer.regionMatches(location, "@interface", 0, 10))
            nextype = BlockType.OBJECTSTART;
        else if (buffer.regionMatches(location, "@protocol", 0, 9))
            nextype = BlockType.PROTOCOLSTART;
        else if (buffer.regionMatches(location, "@property", 0, 9))
            nextype = BlockType.PROPERTY;
        else if (buffer.regionMatches(location, "@end", 0, 4))
            nextype = BlockType.OBJECTEND;
        else if (buffer.regionMatches(location, "{", 0, 1))
            nextype = BlockType.OPENBRACKET;
        else if (buffer.regionMatches(location, "}", 0, 1))
            nextype = BlockType.CLOSEBRACKET;
        else {
            String block = buffer.substring(location, blockLimit).trim();
            if (block.startsWith("const"))
                block = block.substring(5).trim();
            boolean typedef = block.startsWith("typedef");
            if (typedef)
                block = block.substring(7).trim();
            if (block.startsWith("const"))
                block = block.substring(5).trim();
            
            if (block.startsWith("enum"))
                nextype = typedef ? BlockType.TYPEDEFENUM : BlockType.ENUM;
            else if (block.startsWith("struct"))
                nextype = typedef ? BlockType.TYPEDEFSTRUCT : BlockType.STRUCT;
            else if (block.startsWith("union"))
                nextype = typedef ? BlockType.TYPEDEFUNION : BlockType.UNION;
            else if (block.startsWith("namespace"))
                nextype = BlockType.NAMESPACE;
            else if (block.startsWith("class"))
                nextype = BlockType.UNKNOWN;
            else {
                block = block.endsWith("}") ? block.substring(0, StringUtils.matchFromEnd(block, '{', '}')) + ";" : block;
                block = block.trim();
                if (block.endsWith(";") && block.substring(0, block.length() - 1).trim().endsWith(")"))
                    nextype = typedef ? BlockType.TYPEDEFFUNCTION : BlockType.FUNCTION;
                else if (block.endsWith(";") && StringUtils.findLastWord(block.substring(0, block.length() - 1).trim()) >= 0)
                    nextype = typedef ? BlockType.TYPEDEFEXTERNAL : BlockType.EXTERNAL;
                else
                    nextype = BlockType.UNKNOWN;
            }
        }
    }
}
