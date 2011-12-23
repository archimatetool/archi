/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.canvas.propertysections;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.IFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import uk.ac.bolton.archimate.canvas.model.ICanvasPackage;
import uk.ac.bolton.archimate.canvas.model.INotesContent;
import uk.ac.bolton.archimate.editor.model.commands.EObjectFeatureCommand;
import uk.ac.bolton.archimate.editor.propertysections.AbstractArchimatePropertySection;
import uk.ac.bolton.archimate.editor.propertysections.ITabbedLayoutConstants;
import uk.ac.bolton.archimate.editor.propertysections.PropertySectionTextControl;
import uk.ac.bolton.archimate.editor.ui.components.StyledTextControl;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.ILockable;


/**
 * Property Section for Notes
 * 
 * @author Phillip Beauvoir
 */
public class NotesSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "uk.ac.bolton.archimate.help.elementPropertySection";

    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter implements IFilter {
        @Override
        public boolean select(Object object) {
            return (object instanceof EditPart) && ((EditPart)object).getModel() instanceof INotesContent;
        }
    }

    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Model event
            if(feature == ICanvasPackage.Literals.NOTES_CONTENT__NOTES 
                    || feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                refreshControls();
            }
        }
    };
    
    private INotesContent fNotesContent;
    
    private PropertySectionTextControl fTextNotesControl;
    
    @Override
    protected void createControls(Composite parent) {
        createCLabel(parent, "Notes:", ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.TOP);
        
        StyledTextControl styledTextControl = createStyledTextControl(parent, SWT.NONE);
        
        fTextNotesControl = new PropertySectionTextControl(styledTextControl.getControl(), ICanvasPackage.Literals.NOTES_CONTENT__NOTES) {
            @Override
            protected void textChanged(String oldText, String newText) {
                if(isAlive()) {
                    fIsExecutingCommand = true;
                    getCommandStack().execute(new EObjectFeatureCommand("Notes", fNotesContent,
                                                ICanvasPackage.Literals.NOTES_CONTENT__NOTES, newText));
                    fIsExecutingCommand = false;
                }
            }
        };
        fTextNotesControl.setHint("Add some notes here");
        
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(fTextNotesControl.getTextControl(), HELP_ID);
    }

    @Override
    protected void setElement(Object element) {
        if(element instanceof INotesContent) {
            fNotesContent = (INotesContent)element;
        }
        else if(element instanceof EditPart && ((EditPart)element).getModel() instanceof INotesContent) {
            fNotesContent = (INotesContent)((EditPart)element).getModel();
        }

        if(fNotesContent == null) {
            throw new RuntimeException("Notes Content was null");
        }
        
        refreshControls();
    }
    
    protected void refreshControls() {
        if(fIsExecutingCommand) {
            return; 
        }
        fTextNotesControl.refresh(fNotesContent);
        
        boolean enabled = fNotesContent instanceof ILockable ? !((ILockable)fNotesContent).isLocked() : true;
        fTextNotesControl.getTextControl().setEnabled(enabled);
    }
    
    @Override
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }

    @Override
    protected EObject getEObject() {
        return fNotesContent;
    }
    
    @Override
    public boolean shouldUseExtraSpace() {
        return true;
    }
}
