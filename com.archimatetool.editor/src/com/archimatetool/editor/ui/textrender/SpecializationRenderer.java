/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.textrender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IProfile;
import com.archimatetool.model.IProfiles;

/**
 * Specialization renderer
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class SpecializationRenderer extends AbstractTextRenderer {
    
    private static final Pattern NAME_PATTERN = Pattern.compile("\\$" + allPrefixesGroup + "\\{specialization\\}");

    @Override
    public String render(IArchimateModelObject object, String text) {
        Matcher matcher = NAME_PATTERN.matcher(text);
        
        while(matcher.find()) {
            String prefix = matcher.group(1);
            String replacement = "";
            
            IArchimateModelObject refObject = getObjectFromPrefix(object, prefix);
            if(refObject instanceof IProfiles) {
                IProfile profile = ((IProfiles)refObject).getPrimaryProfile();
                if(profile != null) {
                    replacement = profile.getName();
                }
            }
            
            text = text.replace(matcher.group(), replacement);
        }
        
        return text;
    }
}