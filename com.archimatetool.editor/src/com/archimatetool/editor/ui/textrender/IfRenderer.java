package com.archimatetool.editor.ui.textrender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.archimatetool.model.IArchimateModelObject;

public class IfRenderer extends AbstractTextRenderer {
	
    private static final String startOfExpression = "\\$" + allPrefixesGroup + "\\{";
    private static final String notStartOfExpression = "(?!" + startOfExpression + ")";
    private static final String acceptedChar = "[^:\\}]";
    private static final String textWithoutExpressions = "(?:" + notStartOfExpression + acceptedChar + ")*";
    
    private static final Pattern IF_THEN_ELSE_PATTERN = Pattern.compile("\\$\\{if:(?<IF>" + textWithoutExpressions + "):(?<THEN>" + textWithoutExpressions +"):(?<ELSE>" + textWithoutExpressions + ")\\}");
    private static final Pattern IF_THEN_PATTERN = Pattern.compile("\\$\\{if:(?<IF>" + textWithoutExpressions + "):(?<THEN>" + textWithoutExpressions +")\\}");
    private static final Pattern NVL_PATTERN = Pattern.compile("\\$\\{nvl:(?<COND>" + textWithoutExpressions + "):(?<ALT>" + textWithoutExpressions + ")\\}");
    
	@Override
    public String render(IArchimateModelObject object, String text) {
		// First checking with String.contains() to optimize a bit and use Matcher only if needed
		if(text.contains("{if:")) {
	        text = renderIfThenElse(object, text);
	        text = renderIfThen(object, text);
		}
		if(text.contains("{nvl:")) {
			text = renderNvl(object, text);
		}

        return text;
    }
	
	public String renderIfThen(IArchimateModelObject object, String text) {
        Matcher matcher = IF_THEN_PATTERN.matcher(text);
        while(matcher.find()) {
        	String ifCondition = matcher.group("IF");
            String ifThen = matcher.group("THEN");
            String s = "";

            s = (!ifCondition.isBlank())? ifThen : "";
            s = (s != null)? s : "";
            text = text.replace(matcher.group(), s);
        }

        return text;
	}
	
	public String renderIfThenElse(IArchimateModelObject object, String text) {
        Matcher matcher = IF_THEN_ELSE_PATTERN.matcher(text);
        while(matcher.find()) {
        	String ifCondition = matcher.group("IF");
            String ifThen = matcher.group("THEN");
            String ifElse = matcher.group("ELSE");
            String s = "";

            s = (!ifCondition.isBlank())? ifThen : ifElse;
            s = (s != null)? s : "";
            text = text.replace(matcher.group(), s);
        }

        return text;
	}
	
	public String renderNvl(IArchimateModelObject object, String text) {
        Matcher matcher = NVL_PATTERN.matcher(text);
        while(matcher.find()) {
        	String nvlCondition = matcher.group("COND");
        	String nvlAlternate = matcher.group("ALT");
            String s = "";

            s = (!nvlCondition.isBlank())? nvlCondition : nvlAlternate;
            s = (s != null)? s : "";
            text = text.replace(matcher.group(), s);
        }

        return text;
	}

}
