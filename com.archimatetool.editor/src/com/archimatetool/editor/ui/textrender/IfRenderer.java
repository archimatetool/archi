/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.textrender;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.archimatetool.model.IArchimateModelObject;

/**
 * If/Nvl Label Renderer – true conditional evaluation, supports nested expressions
 * and escape sequences. Uses a regex to locate the start of an expression, then
 * parses manually to correctly handle inner <code>${...}</code> blocks.
 *
 * @author Jean-Baptiste Sarrodie
 */
@SuppressWarnings("nls")
public class IfRenderer extends AbstractTextRenderer {

    // Matches the beginning of an if or nvl expression, allowing optional spaces
    private static final Pattern START_PATTERN = Pattern.compile("\\$\\{ *(if|nvl) *:");

    @Override
    public String render(IArchimateModelObject object, String text) {
        StringBuilder result = new StringBuilder();
        int lastEnd = 0;
        Matcher matcher = START_PATTERN.matcher(text);

        while (lastEnd < text.length()) {
            matcher.region(lastEnd, text.length());
            if (!matcher.find()) {
                break;
            }

            String keyword = matcher.group(1);
            int contentStart = matcher.end();

            // Find the matching closing brace, handling nesting and escapes
            int braceDepth = 0;
            int pos = contentStart;
            while (pos < text.length()) {
                char c = text.charAt(pos);
                // Handle escaped characters: \}, \{, and \:
                if (c == '\\' && pos + 1 < text.length()) {
                    char next = text.charAt(pos + 1);
                    if (next == '}' || next == '{' || next == ':') {
                        pos += 2;
                        continue;
                    }
                }
                if (c == '$' && pos + 1 < text.length() && text.charAt(pos + 1) == '{') {
                    braceDepth++;
                    pos += 2;
                    continue;
                }
                if (c == '}') {
                    if (braceDepth == 0) {
                        break; // Found the outer closing brace
                    }
                    braceDepth--;
                }
                pos++;
            }

            if (pos >= text.length()) {
                // Unclosed expression: stop processing and return the remaining text as-is
                result.append(text.substring(lastEnd));
                return result.toString();
            }

            // Append the literal text before the expression
            result.append(text, lastEnd, matcher.start());

            // Extract the inner content
            String inner = text.substring(contentStart, pos);

            // Split inner into parts using ':' not inside nested expressions and not escaped
            String[] parts = splitParts(inner);

            // Evaluate and replace
            String replacement = evaluate(object, keyword, parts);
            result.append(replacement != null ? replacement : "");

            lastEnd = pos + 1; // continue after the closing brace
        }

        result.append(text.substring(lastEnd));
        return result.toString();
    }

    /**
     * Splits the inner content by ':' that are not escaped and not inside a ${...} block.
     * Escaped characters (\:, \{, \}) are preserved. Each resulting part is trimmed
     * to remove optional spaces around separators.
     */
    private String[] splitParts(String s) {
        List<String> parts = new ArrayList<>();
        int start = 0;
        int braceDepth = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            // Handle escaped characters: \:, \}, \{
            if (c == '\\' && i + 1 < s.length()) {
                char next = s.charAt(i + 1);
                if (next == ':' || next == '{' || next == '}') {
                    i++; // skip the escaped character; the backslash stays in the part
                    continue;
                }
            }
            // Start of a nested expression
            if (c == '$' && i + 1 < s.length() && s.charAt(i + 1) == '{') {
                braceDepth++;
                i++; // skip the second character; the loop will also increment i
                continue;
            }
            if (c == '}') {
                braceDepth--;
                continue;
            }
            if (c == ':' && braceDepth == 0) {
                parts.add(s.substring(start, i).trim());   // ← TRIM HERE
                start = i + 1;
            }
        }
        parts.add(s.substring(start).trim());              // ← TRIM LAST PART
        return parts.toArray(new String[0]);
    }

    private String evaluate(IArchimateModelObject object, String keyword, String[] parts) {
        if ("if".equals(keyword)) {
            if (parts.length < 2) {
                return "";
            }
            String ifRaw   = parts[0];
            String thenRaw = parts[1];
            String elseRaw = parts.length >= 3 ? parts[2] : "";

            // Render each part using the full rendering pipeline
            String ifVal   = renderPart(object, ifRaw);
            String thenVal = renderPart(object, thenRaw);
            String elseVal = renderPart(object, elseRaw);

            boolean conditionTrue = ifVal != null && !ifVal.isBlank();
            return conditionTrue ? thenVal : elseVal;
        } else if ("nvl".equals(keyword)) {
            if (parts.length != 2) {
                return "";
            }
            String condRaw = parts[0];
            String altRaw  = parts[1];

            String condVal = renderPart(object, condRaw);
            String altVal  = renderPart(object, altRaw);

            boolean condBlank = condVal == null || condVal.isBlank();
            return condBlank ? altVal : condVal;
        }
        return "";
    }

    /**
     * Renders a sub‑expression through the entire text rendering chain.
     * Uses renderWithExpression to avoid interference with any format expression
     * that may be stored on the object itself.
     */
    private String renderPart(IArchimateModelObject object, String text) {
        return TextRenderer.getDefault().renderWithExpression(object, text);
    }
}
