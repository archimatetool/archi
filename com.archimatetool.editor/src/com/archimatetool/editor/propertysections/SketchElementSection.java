/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ISketchModelActor;



/**
 * Property Section for a Sketch Element
 * 
 * @author Phillip Beauvoir
 */
public class SketchElementSection extends AbstractNameDocumentationSection {
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof ISketchModelActor;
        }

        @Override
        public Class<?> getAdaptableType() {
            return IDiagramModelObject.class;
        }
    }

    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
}
