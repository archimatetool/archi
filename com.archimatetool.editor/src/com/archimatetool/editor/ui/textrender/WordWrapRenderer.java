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
 * WordWrap Renderer
 * 
 * @author jbsarrodie
 */
@SuppressWarnings("nls")
public class WordWrapRenderer extends AbstractTextRenderer {
	
	private static final String LINEBREAK = "\n";
	
    private static final String startOfExpression = "\\$" + allPrefixesGroup + "\\{";
    private static final String notStartOfExpression = "(?!" + startOfExpression + ")";
    private static final String acceptedKeyChar = "[^\\}]";
    private static final String textToWrap = "(" + notStartOfExpression + acceptedKeyChar + ")+";
    
    private static final Pattern WORD_WRAP_PATTERN = Pattern.compile("\\$\\{wordwrap:([0-9]+):(" + textToWrap +")\\}");
    
	@Override
    public String render(IArchimateModelObject object, String text) {
        Matcher matcher = WORD_WRAP_PATTERN.matcher(text);

        while(matcher.find()) {
            int wrapLimit;
            String textToWrap = matcher.group(2);
            String s = "";

            try {
                wrapLimit = Integer.parseInt(matcher.group(1));
            }
            catch(NumberFormatException nfe) {
                // Should not be possible to have an exception here because Pattern only accept integer
                wrapLimit = 999999;
            }

            s = wrap(textToWrap, wrapLimit);
            text = text.replace(matcher.group(), s);
        }

        return text;
    }

	/*
	 * Based on a simple word wrapping function found on Stackoverflow (https://stackoverflow.com/a/45614206)
	 * Updated to use positive lookaround (http://www.regular-expressions.info/lookaround.html) to keep separators
	 * as part of each fragments so it is no more needed to add them again at the end (which was leading to
	 * unwanted behavior like missing/added spaces or added linebreaks).
	 */
	public static String wrap(String string, int lineLength) {
        StringBuilder b = new StringBuilder();
        
        for(String line : string.split("(?<=" + LINEBREAK + ")")) {
            b.append(wrapLine(line, lineLength));
        }
        
        return b.toString();
	}

	private static String wrapLine(String line, int lineLength) {
        if(line.length() == 0) {
            return "";
        }
        
        if(line.length() <= lineLength) {
            return line;
        }
        
        StringBuilder allLines = new StringBuilder();
        StringBuilder trimmedLine = new StringBuilder();
        
        for(String word : line.split("(?<= )")) {
            if(trimmedLine.length() + word.length() <= lineLength) {
                trimmedLine.append(word);
            } else if(trimmedLine.length() + word.trim().length() <= lineLength) {
                // Edge case: only the trimmed word can be added to the line
                trimmedLine.append(word.trim());
            }
            else {
                if(trimmedLine.length() > 0) {
                    allLines.append(trimmedLine).append(LINEBREAK);
                    trimmedLine = new StringBuilder();
                }
                trimmedLine.append(word);
            }
        }
        
        if(trimmedLine.length() > 0) {
            allLines.append(trimmedLine);
        }
        
        return allLines.toString();
	}
}
