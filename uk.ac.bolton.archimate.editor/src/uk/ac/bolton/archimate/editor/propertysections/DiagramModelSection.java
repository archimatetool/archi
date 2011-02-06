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

import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModel;


/**
 * Property Section for a Diagram Model
 * 
 * @author Phillip Beauvoir
 */
public class DiagramModelSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "uk.ac.bolton.archimate.help.diagramModelSection";

    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Diagram Name event (Undo/Redo and here!)
            if(feature == IArchimatePackage.Literals.NAMEABLE__NAME) {
                refresh();
                fPage.labelProviderChanged(null); // Update Main label
            }
            // Documentation
            else if(feature == IArchimatePackage.Literals.DOCUMENTABLE__DOCUMENTATION) {
                refresh();
            }
        }
    };
    
    private IDiagramModel fDiagramModel;
    
    private PropertySectionTextControl fTextName;
    private PropertySectionTextControl fTextDocumentation;

    @Override
    protected void createControls(Composite parent) {
        fTextName = createNameControl(parent, "Add a name for this view here");
        fTextDocumentation = createDocumentationControl(parent, "Add documentation relating to this view here");
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }

    @Override
    protected void setElement(Object element) {
        if(element instanceof EditPart) {
            fDiagramModel = (IDiagramModel)((EditPart)element).getModel();
        }
        else if(element instanceof IDiagramModel) {
            fDiagramModel = (IDiagramModel)element;
        }
        else {
            System.err.println("Section wants to display for " + element);
        }
    }
    
    @Override
    public void refresh() {
        if(fDiagramModel == null) {
            return;
        }
        
        // Populate fields...
        fTextName.refresh(fDiagramModel);
        fTextDocumentation.refresh(fDiagramModel);
    }

    @Override
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }

    @Override
    protected EObject getEObject() {
        return fDiagramModel;
    }
    
    @Override
    public boolean shouldUseExtraSpace() {
        return true;
    }
}
