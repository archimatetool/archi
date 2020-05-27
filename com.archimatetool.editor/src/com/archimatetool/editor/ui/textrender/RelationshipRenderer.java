/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.textrender;

import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IInfluenceRelationship;

/**
 * Relationship specialty renderer
 * 
 * @author Phillip Beauvoir
 */
public class RelationshipRenderer extends AbstractTextRenderer {
    
    // Influence Strength
    private static final String STRENGTH = "${strength}"; //$NON-NLS-1$

    @Override
    public String render(IArchimateModelObject object, String text) {
        object = getActualObject(object);
        
        if(object instanceof IInfluenceRelationship) {
            return text.replace(STRENGTH, ((IInfluenceRelationship)object).getStrength());
        }
        
        return text;
    }
}