/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.propertysections;

import java.io.File;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import uk.ac.bolton.archimate.editor.model.commands.EObjectFeatureCommand;
import uk.ac.bolton.archimate.editor.ui.components.StyledTextControl;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IArchimatePackage;


/**
 * Property Section for an Archimate Model
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateModelSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "uk.ac.bolton.archimate.help.archimateModelSection"; //$NON-NLS-1$

    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Model Name event (Undo/Redo and here!)
            if(feature == IArchimatePackage.Literals.NAMEABLE__NAME) {
                refreshNameField();
                fPage.labelProviderChanged(null); // Update Main label
            }
            // Model File
            else if(feature == IArchimatePackage.Literals.ARCHIMATE_MODEL__FILE) {
                refreshFileField();
            }
            // Model Purpose event (Undo/Redo and here!)
            else if(feature == IArchimatePackage.Literals.ARCHIMATE_MODEL__PURPOSE) {
                refreshPurposeField();
            }
        }
    };
    
    private IArchimateModel fModel;
    
    private PropertySectionTextControl fTextName;
    private Text fTextFile;
    private PropertySectionTextControl fTextPurpose;
    
    @Override
    protected void createControls(Composite parent) {
        fTextName = createNameControl(parent, Messages.ArchimateModelSection_0);
        createFileControl(parent);
        createPurposeControl(parent);
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    private void createFileControl(Composite parent) {
        // Label
        getWidgetFactory().createCLabel(parent, Messages.ArchimateModelSection_1);

        // Text
        fTextFile = createSingleTextControl(parent, SWT.READ_ONLY);
        fTextFile.setEnabled(false);
    }
    
    private void createPurposeControl(Composite parent) {
        // Label
        createCLabel(parent, Messages.ArchimateModelSection_2, STANDARD_LABEL_WIDTH, SWT.TOP);

        // Text
        StyledTextControl styledTextControl = createStyledTextControl(parent, SWT.NONE);
        
        fTextPurpose = new PropertySectionTextControl(styledTextControl.getControl(), IArchimatePackage.Literals.ARCHIMATE_MODEL__PURPOSE) {
            @Override
            protected void textChanged(String oldText, String newText) {
                if(isAlive()) {
                    fIsExecutingCommand = true;
                    getCommandStack().execute(new EObjectFeatureCommand(Messages.ArchimateModelSection_3, fModel,
                                                    IArchimatePackage.Literals.ARCHIMATE_MODEL__PURPOSE, newText));
                    fIsExecutingCommand = false;
                }
            }
        };
        fTextPurpose.setHint(Messages.ArchimateModelSection_4);
    }

    @Override
    protected void setElement(Object element) {
        if(element instanceof IArchimateModel) {
            fModel = (IArchimateModel)element;
        }
        else {
            System.err.println("Section wants to display for " + element); //$NON-NLS-1$
        }
        
        refreshControls();
    }
    
    protected void refreshControls() {
        refreshNameField();
        refreshFileField();
        refreshPurposeField();
    }
    
    protected void refreshNameField() {
        if(fIsExecutingCommand) {
            return; 
        }
        fTextName.refresh(fModel);
    }
    
    protected void refreshFileField() {
        File file = fModel.getFile();
        if(file != null) {
            fTextFile.setText(file.getAbsolutePath());
        }
        else{
            fTextFile.setText(Messages.ArchimateModelSection_5);
        }
    }
    
    protected void refreshPurposeField() {
        if(fIsExecutingCommand) {
            return; 
        }
        fTextPurpose.refresh(fModel);
    }

    @Override
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }

    @Override
    protected EObject getEObject() {
        return fModel;
    }
    
    @Override
    public boolean shouldUseExtraSpace() {
        return true;
    }
}
