/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.archimatetool.editor.ui.components.DuplicateNameHandler;
import com.archimatetool.model.IArchimateConcept;



/**
 * Property Section for an Archimate Concept
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateConceptSection extends AbstractNameDocumentationSection {
    
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
        
        // Add a DuplicateNameHandler on focus gained in name control
        fTextName.getTextControl().addFocusListener(new FocusListener() {
            private DuplicateNameHandler duplicateNameHandler;
            
            @Override
            public void focusGained(FocusEvent e) {
                if(duplicateNameHandler != null) {
                    duplicateNameHandler.dispose();
                }
                
                if(isAlive(getFirstSelectedObject()) && getFirstSelectedObject() instanceof IArchimateConcept concept) {
                    duplicateNameHandler = new DuplicateNameHandler((Text)fTextName.getTextControl(), concept);
                }
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if(duplicateNameHandler != null) {
                    duplicateNameHandler.dispose();
                    duplicateNameHandler = null;
                }
            }
        });
    }
    
    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
}
