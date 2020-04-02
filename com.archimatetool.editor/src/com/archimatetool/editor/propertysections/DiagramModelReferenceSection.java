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
import com.archimatetool.model.IDiagramModelReference;



/**
 * Property Section for a Diagram Model Reference
 * 
 * @author Phillip Beauvoir
 */
public class DiagramModelReferenceSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$

    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof IDiagramModelReference;
        }

        @Override
        public Class<?> getAdaptableType() {
            return IDiagramModelReference.class;
        }
        
        @Override
        public Object adaptObject(Object object) {
            // Return the referenced diagram model
            Object adapted = super.adaptObject(object);
            return adapted == null ? null : ((IDiagramModelReference)adapted).getReferencedModel();
        }
    }

    private PropertySectionTextControl fTextName;
    
    @Override
    protected void createControls(Composite parent) {
        fTextName = createNameControl(parent, Messages.DiagramModelReferenceSection_0);

        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    @Override
    protected void notifyChanged(Notification msg) {
        if(msg.getNotifier() == getFirstSelectedObject()) {
            Object feature = msg.getFeature();
            
            if(feature == IArchimatePackage.Literals.NAMEABLE__NAME) {
                update();
                updatePropertiesLabel(); // Update Main label
            }
        }
    }

    @Override
    protected void update() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        fTextName.refresh(getFirstSelectedObject());
    }
    
    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
}
