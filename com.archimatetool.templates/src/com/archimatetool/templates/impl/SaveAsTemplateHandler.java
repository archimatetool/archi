/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.templates.impl;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;

import com.archimatetool.editor.actions.AbstractModelSelectionHandler;
import com.archimatetool.editor.ui.components.ExtendedWizardDialog;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.templates.impl.wizard.SaveArchimateModelAsTemplateWizard;



/**
 * Command Action Handler for Save As Template
 * 
 * @author Phillip Beauvoir
 */
public class SaveAsTemplateHandler extends AbstractModelSelectionHandler {
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IArchimateModel model = getActiveArchimateModel();
        if(model != null) {
            WizardDialog dialog = new ExtendedWizardDialog(workbenchWindow.getShell(),
                    new SaveArchimateModelAsTemplateWizard(model),
                    "SaveModelAsTemplateWizard"); //$NON-NLS-1$
            dialog.open();
        }

        return null;
    }
    
}
