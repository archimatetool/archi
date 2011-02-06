/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.actions;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;

import uk.ac.bolton.archimate.editor.templates.wizard.SaveModelAsTemplateWizard;
import uk.ac.bolton.archimate.editor.ui.ExtendedWizardDialog;
import uk.ac.bolton.archimate.model.IArchimateModel;

/**
 * Save As Template Action
 * 
 * @author Phillip Beauvoir
 */
public class SaveAsTemplateAction extends AbstractModelSelectionAction {
    public SaveAsTemplateAction(IWorkbenchWindow window) {
        super("Save As &Template...", window);
    }
    
    @Override
    public void run() {
        IArchimateModel model = getActiveArchimateModel();
        if(model != null) {
            WizardDialog dialog = new ExtendedWizardDialog(workbenchWindow.getShell(),
                                  new SaveModelAsTemplateWizard(model),
                                  "SaveModelAsTemplateWizard");
            dialog.open();
        }
    }
    
    @Override
    protected void updateState() {
        setEnabled(getActiveArchimateModel() != null);
    }
}