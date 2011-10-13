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

package org.crossmobile.source.utils;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

    public static int findFirstWord(String data) {
        if (data == null || data.length() == 0)
            return -1;
        char c = data.charAt(0);
        if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_'))
            return -1;
        int p = 1;
        for (; p < data.length(); p++) {
            c = data.charAt(p);
            if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_'))
                return p;
        }
        return p;
    }

    public static int findLastWord(String data) {
        if (data == null || data.length() == 0)
            return -1;
        char c;
        for (int p = data.length() - 1; p >= 0; p--) {
            c = data.charAt(p);
            if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_')) {
                p++;
                if (p < data.length()) {
                    c = data.charAt(p);
                    while (c >= '0' && c <= '9') {
                        p++;
                        if (p >= data.length())
                            break;
                        c = data.charAt(p);
                    }
                }
                if (p >= data.length())
                    p = -1;
                return p;
            }
        }
        return 0;
    }

    public static List<String> divideBySection(String data) {
        List<String> parts = new ArrayList<String>();
        int loc = 0;
        char c;
        int reference = 0;
        StringBuilder b = new StringBuilder();
        String part;
        boolean foundBlock = false;

        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        boolean inQuotes = false;

        while (loc < data.length()) {
            c = data.charAt(loc++);
            if (reference == 0 && (foundBlock || c == ';')) {
                part = b.toString().trim();
                if (part.length() > 0 && !part.equals("}"))
                    parts.add(part);
                b = new StringBuilder();
            } else {
                switch (c) {
                    case '\'':
                        if (inSingleQuote) {
                            inSingleQuote = false;
                            inQuotes = false;
                        } else if (!inDoubleQuote) {
                            inSingleQuote = true;
                            inQuotes = true;
                        }
                        break;
                    case '"':
                        if (inDoubleQuote) {
                            inDoubleQuote = false;
                            inQuotes = false;
                        } else if (!inSingleQuote) {
                            inDoubleQuote = true;
                            inQuotes = true;
                        }
                        break;
                    case '{':
                        if (!inQuotes)
                            foundBlock = true;
                    case '(':
                        if (!inQuotes)
                            reference++;
                        break;
                    case ')':
                    case '}':
                        if (!inQuotes)
                            reference--;
                        break;
                }
                b.append(c);
            }
        }
        part = b.toString().trim();
        if (part.length() > 0 && !part.equals("}"))
            parts.add(part);
        return parts;
    }

    // Start form beginning
    public static int matchFromStart(String data, char open, char close) {
        return matchBalanced(data, 0, open, close, 1);
    }

    // Start from end
    public static int matchFromEnd(String data, char open, char close) {
        return matchBalanced(data, 0, open, close, -1);
    }

    /**
     * Find the closing or opening character on a string
     * @param data The string to search into. The opening/closing character should
     * be either the beginning or the ending character either for forward (begin) 
     * or backward (end) searches. This is a feature, so that if the string is
     * not trim, it might break.
     * @param open opening character
     * @param location location to start searching
     * @param close closing character
     * @param step -1 or 1: either to look backwards or forwards
     * @return location of the matching character
     */
    public static int matchBalanced(String data, int location, char open, char close, int step) {
        int length = data.length();
        if (location >= length)
            return -1;
        int pos = step < 0 ? length - 1 : location;
        int reference = 0;
        char c;
        int start = pos;

        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        boolean inQuotes = false;

        while (pos >= location && pos < length) {
            c = data.charAt(pos);
            if (c == '\'')
                if (inSingleQuote) {
                    inSingleQuote = false;
                    inQuotes = false;
                } else if (!inDoubleQuote) {
                    inSingleQuote = true;
                    inQuotes = true;
                }
            if (c == '"')
                if (inDoubleQuote) {
                    inDoubleQuote = false;
                    inQuotes = false;
                } else if (!inSingleQuote) {
                    inDoubleQuote = true;
                    inQuotes = true;
                }
            if (!inQuotes && c == close)
                reference++;
            if (!inQuotes && c == open)
                reference--;
            if (reference == 0 && pos != start)
                return pos;
            pos += step;
        }
        return -1;
    }

    public static int searchForMatchingBlock(String data, int start, char blockend) {
        char c;
        int pos = start;
        int size = data.length();
        int reference = 0;
        boolean foundBlock = false;
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        boolean inQuotes = false;

        while (pos < size) {
            c = data.charAt(pos++);
            if (!inQuotes && reference == 0 && (c == blockend || foundBlock)) {
                // Might have an ID which needs to be taken into account
                if (c != blockend) {
                    int newpos = pos;
                    while (newpos < size && ((c = data.charAt(newpos)) == ' ' || c == '\n' || c == '\t' || c == '\r' || c == '\f'))
                        newpos++;
                    while (newpos < size && ((c = data.charAt(newpos)) == '_' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')))
                        newpos++;
                    while (newpos < size && ((c = data.charAt(newpos)) == ' ' || c == '\n' || c == '\t' || c == '\r' || c == '\f'))
                        newpos++;
                    pos = (newpos < size && data.charAt(newpos) == ';') ? newpos + 1 : pos;
                }
                break;
            } else
                switch (c) {
                    case '\'':
                        if (inSingleQuote) {
                            inSingleQuote = false;
                            inQuotes = false;
                        } else if (!inDoubleQuote) {
                            inSingleQuote = true;
                            inQuotes = true;
                        }
                        break;
                    case '"':
                        if (inDoubleQuote) {
                            inDoubleQuote = false;
                            inQuotes = false;
                        } else if (!inSingleQuote) {
                            inDoubleQuote = true;
                            inQuotes = true;
                        }
                        break;
                    case '{':
                        if (!inQuotes)
                            foundBlock = true;
                    case '(':
                        if (!inQuotes)
                            reference++;
                        break;
                    case ')':
                    case '}':
                        if (!inQuotes)
                            reference--;
                        break;
                }
        }
        return pos;
    }

    public static int count(String data, char what) {
        int reset = 0;
        for (int i = 0; i < data.length(); i++)
            if (data.charAt(i) == what)
                reset++;
        return reset;
    }
}
