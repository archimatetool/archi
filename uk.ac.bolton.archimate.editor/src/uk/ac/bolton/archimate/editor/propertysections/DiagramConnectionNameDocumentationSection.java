/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.propertysections;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IFilter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import uk.ac.bolton.archimate.editor.diagram.editparts.connections.IArchimateConnectionEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.connections.IDiagramConnectionEditPart;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;
import uk.ac.bolton.archimate.model.ILockable;


/**
 * Property Section for the name & documentation of a non-Archimate diagram connection
 * 
 * @author Phillip Beauvoir
 */
public class DiagramConnectionNameDocumentationSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "uk.ac.bolton.archimate.help.elementPropertySection";
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter implements IFilter {
        @Override
        public boolean select(Object object) {
            return (object instanceof IDiagramConnectionEditPart)
                        && !(object instanceof IArchimateConnectionEditPart); // Not Archimate connections
        }
    }

    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Name event (Undo/Redo and here)
            if(feature == IArchimatePackage.Literals.NAMEABLE__NAME) {
                refreshNameField();
                fPage.labelProviderChanged(null); // Update Main label
            }
            // Documentation event (Undo/Redo and here)
            else if(feature == IArchimatePackage.Literals.DOCUMENTABLE__DOCUMENTATION) {
                refreshDocumentationField();
            }
            else if(feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                refreshControls();
            }
        }
    };
    
    private IDiagramModelConnection fConnection;

    private PropertySectionTextControl fTextName;
    private PropertySectionTextControl fTextDocumentation;
    
    @Override
    protected void createControls(Composite parent) {
        fTextName = createNameControl(parent, "Add a name for this connection here");
        fTextDocumentation = createDocumentationControl(parent, "Add documentation relating to this connection here");

        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }

    @Override
    protected void setElement(Object element) {
        if(element instanceof IAdaptable) {
            fConnection = (IDiagramModelConnection)((IAdaptable)element).getAdapter(IDiagramModelConnection.class);
        }
        else {
            throw new RuntimeException("Should have been an Edit Part");
        }
        
        refreshControls();
    }
    
    protected void refreshControls() {
        refreshNameField();
        refreshDocumentationField();
    }
    
    protected void refreshNameField() {
        if(fIsExecutingCommand) {
            return; 
        }
        fTextName.refresh(fConnection);
        
        boolean enabled = fConnection instanceof ILockable ? !((ILockable)fConnection).isLocked() : true;
        fTextName.getTextControl().setEnabled(enabled);
    }
    
    protected void refreshDocumentationField() {
        if(fIsExecutingCommand) {
            return; 
        }
        fTextDocumentation.refresh(fConnection);

        boolean enabled = fConnection instanceof ILockable ? !((ILockable)fConnection).isLocked() : true;
        fTextDocumentation.getTextControl().setEnabled(enabled);
    }
    
    @Override
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }
    
    @Override
    protected EObject getEObject() {
        return fConnection;
    }
    
    @Override
    public boolean shouldUseExtraSpace() {
        return true;
    }
}
