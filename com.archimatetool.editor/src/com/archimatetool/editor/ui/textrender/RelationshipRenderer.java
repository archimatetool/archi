/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.textrender;

import java.util.Arrays;
import java.util.List;

import com.archimatetool.model.IAccessRelationship;
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

    // Access type
    private static final String ACCESS_TYPE = "${accessType}"; //$NON-NLS-1$
    
    List<String> ACCESS_TYPES_LIST = Arrays.asList(new String[] {
            Messages.RelationshipRenderer_0,
            Messages.RelationshipRenderer_1,
            Messages.RelationshipRenderer_2,
            Messages.RelationshipRenderer_3
    });


    @Override
    public String render(IArchimateModelObject object, String text) {
        object = getActualObject(object);
        
        if(object instanceof IInfluenceRelationship) {
            return text.replace(STRENGTH, ((IInfluenceRelationship)object).getStrength());
        }
        
        if(object instanceof IAccessRelationship) {
            return text.replace(ACCESS_TYPE, ACCESS_TYPES_LIST.get(((IAccessRelationship)object).getAccessType()));
        }
        
        return text;
    }
}