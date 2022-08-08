/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.swt.widgets.Composite;

import com.archimatetool.model.IArchimateConcept;



/**
 * Property Section for a Label Renderer for a Concept
 * 
 * @author Phillip Beauvoir
 */
public class LabelRendererSection2 extends LabelRendererSection {
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof IArchimateConcept;
        }

        @Override
        public Class<?> getAdaptableType() {
            return IArchimateConcept.class;
        }
    }

    @Override
    protected void createControls(Composite parent) {
        super.createControls(parent);
        fLabel.setText("Global Expression:");
    }

    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
}
