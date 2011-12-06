/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.templates.impl;

import org.eclipse.jface.wizard.WizardDialog;

import uk.ac.bolton.archimate.editor.actions.AbstractModelSelectionDelegateAction;
import uk.ac.bolton.archimate.editor.ui.components.ExtendedWizardDialog;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.templates.impl.wizard.SaveArchimateModelAsTemplateWizard;


/**
 * Save As Template Action
 * 
 * @author Phillip Beauvoir
 */
public class SaveAsTemplateAction extends AbstractModelSelectionDelegateAction {
    
    @Override
    public void run() {
        IArchimateModel model = getActiveArchimateModel();
        if(model != null) {
            WizardDialog dialog = new ExtendedWizardDialog(workbenchWindow.getShell(),
                    new SaveArchimateModelAsTemplateWizard(model),
                    "SaveModelAsTemplateWizard");
            dialog.open();
        }
    }
    
}
