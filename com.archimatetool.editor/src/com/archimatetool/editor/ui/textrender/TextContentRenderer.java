/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.textrender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.ITextContent;

/**
 * TextContent renderer
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class TextContentRenderer extends AbstractTextRenderer {
    
    private static final Pattern CONTENT_PATTERN = Pattern.compile("\\$(connection:(?:source|target))?\\{content\\}");

    @Override
    public String render(IArchimateModelObject object, String text) {
        Matcher matcher = CONTENT_PATTERN.matcher(text);
        
        while(matcher.find()) {
            String prefix = matcher.group(1);
            String replacement = "";
            
            IArchimateModelObject refObject = getObjectFromPrefix(object, prefix);
            if(refObject instanceof ITextContent) {
                replacement = ((ITextContent)refObject).getContent();
            }
            
            text = text.replace(matcher.group(), replacement);
        }
        
        return text;
    }
}