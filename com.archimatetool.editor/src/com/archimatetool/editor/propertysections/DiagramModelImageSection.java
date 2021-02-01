/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelImage;



/**
 * Property Section for a Diagram Model Image
 * 
 * @author Phillip Beauvoir
 */
public class DiagramModelImageSection extends ImageChooserSection {
    
    private PropertySectionTextControl fTextDocumentation;

    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof IDiagramModelImage;
        }

        @Override
        public Class<?> getAdaptableType() {
            return IDiagramModelImage.class;
        }
    }

    @Override
    protected void createControls(Composite parent) {
        createImageButton(parent);
        fTextDocumentation = createDocumentationControl(parent, Messages.AbstractNameDocumentationSection_1);
        
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }

    @Override
    protected void update() {
        super.update();
        refreshDocumentationField();
    }
    
    protected void refreshDocumentationField() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        fTextDocumentation.refresh(getFirstSelectedObject());
        fTextDocumentation.setEditable(!isLocked(getFirstSelectedObject()));
    }
    
    @Override
    protected void notifyChanged(Notification msg) {
        super.notifyChanged(msg);
        
        if(msg.getNotifier() == getFirstSelectedObject()) {
            Object feature = msg.getFeature();
            
            if(feature == IArchimatePackage.Literals.DOCUMENTABLE__DOCUMENTATION ||
                    feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                refreshDocumentationField();
            }
        }
    }

    @Override
    public boolean shouldUseExtraSpace() {
        return true;
    }

}
