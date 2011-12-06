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
import org.eclipse.jface.viewers.IFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import uk.ac.bolton.archimate.editor.model.commands.EObjectFeatureCommand;
import uk.ac.bolton.archimate.editor.ui.components.StyledTextControl;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.ILockable;
import uk.ac.bolton.archimate.model.ITextContent;


/**
 * Property Section for a Text Content
 * 
 * @author Phillip Beauvoir
 */
public class TextContentSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "uk.ac.bolton.archimate.help.elementPropertySection";

    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter implements IFilter {
        @Override
        public boolean select(Object object) {
            return (object instanceof ITextContent) ||
                    ((object instanceof EditPart) && ((EditPart)object).getModel() instanceof ITextContent);
        }
    }

    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Model Name event (Undo/Redo and here!)
            if(feature == IArchimatePackage.Literals.TEXT_CONTENT__CONTENT ||
                    feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                refreshControls();
            }
        }
    };
    
    private ITextContent fTextContent;
    
    private PropertySectionTextControl fTextContentControl;
    
    @Override
    protected void createControls(Composite parent) {
        createCLabel(parent, "Content:", ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.TOP);
        
        StyledTextControl styledTextControl = createStyledTextControl(parent, SWT.NONE);
        
        fTextContentControl = new PropertySectionTextControl(styledTextControl.getControl(), IArchimatePackage.Literals.TEXT_CONTENT__CONTENT) {
            @Override
            protected void textChanged(String oldText, String newText) {
                if(isAlive()) {
                    fIsExecutingCommand = true;
                    getCommandStack().execute(new EObjectFeatureCommand("Content", fTextContent,
                                                IArchimatePackage.Literals.TEXT_CONTENT__CONTENT, newText));
                    fIsExecutingCommand = false;
                }
            }
        };
        fTextContentControl.setHint("Add some content text here");
        
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(fTextContentControl.getTextControl(), HELP_ID);
    }

    @Override
    protected void setElement(Object element) {
        if(element instanceof ITextContent) {
            fTextContent = (ITextContent)element;
        }
        else if(element instanceof EditPart && ((EditPart)element).getModel() instanceof ITextContent) {
            fTextContent = (ITextContent)((EditPart)element).getModel();
        }

        if(fTextContent == null) {
            throw new RuntimeException("Text Content was null");
        }
        
        refreshControls();
    }
    
    protected void refreshControls() {
        if(fIsExecutingCommand) {
            return; 
        }
        fTextContentControl.refresh(fTextContent);
        
        boolean enabled = fTextContent instanceof ILockable ? !((ILockable)fTextContent).isLocked() : true;
        fTextContentControl.getTextControl().setEnabled(enabled);
    }
    
    @Override
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }

    @Override
    protected EObject getEObject() {
        return fTextContent;
    }
    
    @Override
    public boolean shouldUseExtraSpace() {
        return true;
    }
}
