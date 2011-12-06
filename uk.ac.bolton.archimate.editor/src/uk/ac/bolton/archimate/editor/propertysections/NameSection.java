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
import uk.ac.bolton.archimate.model.ILockable;
import uk.ac.bolton.archimate.model.INameable;


/**
 * Property Section for a Name element
 * 
 * @author Phillip Beauvoir
 */
public class NameSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "uk.ac.bolton.archimate.help.elementPropertySection";

    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Model Name event (Undo/Redo and here)
            if(feature == IArchimatePackage.Literals.NAMEABLE__NAME ||
                    feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                refreshNameField();
                fPage.labelProviderChanged(null); // Update Main label
            }
        }
    };
    
    private INameable fNameable;
    
    private PropertySectionTextControl fTextName;
    
    @Override
    protected void createControls(Composite parent) {
        fTextName = createNameControl(parent, "Add a name here");

        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    @Override
    protected void setElement(Object element) {
        if(element instanceof EditPart && ((EditPart)element).getModel() instanceof INameable) {
            fNameable = (INameable)((EditPart)element).getModel();
        }

        if(fNameable == null) {
            throw new RuntimeException("Object was null");
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
        fTextName.refresh(fNameable);
        
        boolean enabled = fNameable instanceof ILockable ? !((ILockable)fNameable).isLocked() : true;
        fTextName.getTextControl().setEnabled(enabled);
    }
    
    @Override
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }

    @Override
    protected EObject getEObject() {
        return fNameable;
    }
}
