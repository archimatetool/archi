/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IDiagramModelReference;



/**
 * Property Section for a Diagram Model Reference
 * 
 * @author Phillip Beauvoir
 */
public class DiagramModelReferenceSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$

    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        protected boolean isRequiredType(Object object) {
            return object instanceof IDiagramModelReference;
        }

        @Override
        protected Class<?> getAdaptableType() {
            return IDiagramModelObject.class;
        }
    }

    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Model Name event (Undo/Redo and here)
            if(feature == IArchimatePackage.Literals.NAMEABLE__NAME) {
                refreshNameField();
                fPage.labelProviderChanged(null); // Update Main label
            }
        }
    };
    
    private IDiagramModel fDiagramModel;
    
    private PropertySectionTextControl fTextName;
    
    @Override
    protected void createControls(Composite parent) {
        fTextName = createNameControl(parent, Messages.DiagramModelSection_0);

        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    @Override
    protected void setElement(Object element) {
        IDiagramModelReference ref = (IDiagramModelReference)new Filter().adaptObject(element);
        if(ref == null) {
            System.err.println(getClass() + " failed to get element for " + element); //$NON-NLS-1$
        }
        else {
            fDiagramModel = ref.getReferencedModel();
        }

        refreshControls();
    }
    
    protected void refreshControls() {
        refreshNameField();
    }
    
    protected void refreshNameField() {
        if(fIsExecutingCommand) {
            return; 
        }
        fTextName.refresh(fDiagramModel);
    }
    
    @Override
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }

    @Override
    protected EObject getEObject() {
        return fDiagramModel;
    }
}
