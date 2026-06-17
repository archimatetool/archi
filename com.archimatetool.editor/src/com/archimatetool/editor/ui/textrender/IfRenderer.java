/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.textrender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.archimatetool.model.IArchimateModelObject;

/**
 * If/Nvl Label Renderer
 * 
 * @author Jean-Baptiste Sarrodie
 */
@SuppressWarnings("nls")
public class IfRenderer extends AbstractTextRenderer {

    private static final String startOfExpression = "\\$" + allPrefixesGroup + "\\{";
    private static final String notStartOfExpression = "(?!" + startOfExpression + ")";
    private static final String acceptedChar = "(?:[^:\\}\\\\]|\\\\:|\\\\\\\\)";
    private static final String textWithoutExpressions = "(?:\\\\:|" + notStartOfExpression + acceptedChar + ")*";
    
    private static final Pattern IF_THEN_ELSE_PATTERN = Pattern.compile("\\$\\{if:(?<IF>" + textWithoutExpressions + "):(?<THEN>"
            + textWithoutExpressions + "):(?<ELSE>" + textWithoutExpressions + ")\\}");
    private static final Pattern IF_THEN_PATTERN = Pattern.compile("\\$\\{if:(?<IF>" + textWithoutExpressions + "):(?<THEN>" + textWithoutExpressions + ")\\}");
    private static final Pattern NVL_PATTERN = Pattern.compile("\\$\\{nvl:(?<COND>" + textWithoutExpressions + "):(?<ALT>" + textWithoutExpressions + ")\\}");

    @Override
    public String render(IArchimateModelObject object, String text) {
        // First checking with String.contains() to optimize a bit and use
        // Matcher only if needed
        if(text.contains("{if:")) {
            text = renderIfThenElse(text);
            text = renderIfThen(text);
        }

        if(text.contains("{nvl:")) {
            text = renderNvl(text);
        }

        return text;
    }

    private String renderIfThen(String text) {
        Matcher matcher = IF_THEN_PATTERN.matcher(text);

        while(matcher.find()) {
            String ifCondition = matcher.group("IF");
            String ifThen = matcher.group("THEN");
            String s = "";

            s = ifCondition.isBlank() ? "" : ifThen;
            s = s == null ? "" : s;
            text = text.replace(matcher.group(), s);
        }

        return text;
    }

    private String renderIfThenElse(String text) {
        Matcher matcher = IF_THEN_ELSE_PATTERN.matcher(text);

        while(matcher.find()) {
            String ifCondition = matcher.group("IF");
            String ifThen = matcher.group("THEN");
            String ifElse = matcher.group("ELSE");
            String s = "";

            s = ifCondition.isBlank() ? ifElse : ifThen;
            s = s == null ? "" : s;
            text = text.replace(matcher.group(), s);
        }

        return text;
    }

    private String renderNvl(String text) {
        Matcher matcher = NVL_PATTERN.matcher(text);

        while(matcher.find()) {
            String nvlCondition = matcher.group("COND");
            String nvlAlternate = matcher.group("ALT");
            String s = "";

            s = nvlCondition.isBlank() ? nvlAlternate : nvlCondition;
            s = s == null ? "" : s;
            text = text.replace(matcher.group(), s);
        }

        return text;
    }
}
