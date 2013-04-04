/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.templates.impl;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;

import uk.ac.bolton.archimate.editor.actions.AbstractModelSelectionHandler;
import uk.ac.bolton.archimate.editor.ui.components.ExtendedWizardDialog;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.templates.impl.wizard.SaveArchimateModelAsTemplateWizard;


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
