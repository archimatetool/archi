/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.propertysections;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import uk.ac.bolton.archimate.editor.diagram.editparts.diagram.GroupEditPart;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModelGroup;


/**
 * Property Section for a Group
 * 
 * @author Phillip Beauvoir
 */
public class GroupSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "uk.ac.bolton.archimate.help.elementPropertySection";
    
    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Element Name event (Undo/Redo and here)
            if(feature == IArchimatePackage.Literals.NAMEABLE__NAME) {
                refreshNameField();
                fPage.labelProviderChanged(null); // Update Main label
            }
            // Element Documentation event (Undo/Redo and here)
            else if(feature == IArchimatePackage.Literals.DOCUMENTABLE__DOCUMENTATION) {
                refreshDocumentationField();
            }
        }
    };
    
    private IDiagramModelGroup fDiagramModelGroup;
    
    private PropertySectionTextControl fTextName;
    private PropertySectionTextControl fTextDocumentation;
    
    @Override
    protected void createControls(Composite parent) {
        fTextName = createNameControl(parent, "Add a name for this group here");
        fTextDocumentation = createDocumentationControl(parent, "Add documentation relating to this group here");
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    @Override
    protected void setElement(Object element) {
        if(element instanceof GroupEditPart) {
            fDiagramModelGroup = (IDiagramModelGroup)((EditPart)element).getModel();
            if(fDiagramModelGroup == null) {
                throw new RuntimeException("Diagram Group was null");
            }
        }
        else {
            throw new RuntimeException("Should have been a Group Edit Part");
        }
    }
    
    @Override
    public void refresh() {
        // Populate fields...
        refreshNameField();
        refreshDocumentationField();
    }
    
    protected void refreshNameField() {
        fTextName.refresh(fDiagramModelGroup);
    }
    
    protected void refreshDocumentationField() {
        fTextDocumentation.refresh(fDiagramModelGroup);
    }

    @Override
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }

    @Override
    protected EObject getEObject() {
        return fDiagramModelGroup;
    }
    
    @Override
    public boolean shouldUseExtraSpace() {
        return true;
    }

}
