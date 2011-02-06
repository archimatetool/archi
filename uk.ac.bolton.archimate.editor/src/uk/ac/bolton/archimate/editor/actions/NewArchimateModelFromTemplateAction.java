/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import uk.ac.bolton.archimate.editor.templates.wizard.NewArchimateModelFromTemplateWizard;
import uk.ac.bolton.archimate.editor.ui.ExtendedWizardDialog;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;

/**
 * New ArchiMate Model From Template Action
 * 
 * @author Phillip Beauvoir
 */
public class NewArchimateModelFromTemplateAction
extends Action
implements IWorkbenchAction
{
    private IWorkbenchWindow fWindow;
    
    public NewArchimateModelFromTemplateAction(IWorkbenchWindow window) {
        fWindow = window;
        setImageDescriptor(IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_NEW_FILE_16));
        setText("Model From &Template...");
        setToolTipText("Model From Template");
        setId("uk.ac.bolton.archimate.editor.action.newModelFromTemplate");
    }
    
    @Override
    public void run() {
        WizardDialog dialog = new ExtendedWizardDialog(fWindow.getShell(),
                              new NewArchimateModelFromTemplateWizard(),
                              "NewArchimateModelFromTemplateWizard");
        dialog.open();
    }

    public void dispose() {
    }
}