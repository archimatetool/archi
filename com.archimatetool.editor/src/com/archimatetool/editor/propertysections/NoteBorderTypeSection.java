/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import com.archimatetool.model.IDiagramModelNote;


/**
 * Property Section for a Border Type
 * 
 * @author Phillip Beauvoir
 */
public class NoteBorderTypeSection extends BorderTypeSection {
    
    private static final String[] noteComboItems = {
            Messages.NoteBorderTypeSection_0,
            Messages.NoteBorderTypeSection_1,
            Messages.NoteBorderTypeSection_2
    };
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof IDiagramModelNote;
        }

        @Override
        public Class<?> getAdaptableType() {
            return IDiagramModelNote.class;
        }
    }

    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
    
    @Override
    protected String[] getComboItems() {
        return noteComboItems;
    }
    
}
