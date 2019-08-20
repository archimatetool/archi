/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.templates.impl;

import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.handlers.HandlerUtil;

import com.archimatetool.editor.ui.components.ExtendedWizardDialog;
import com.archimatetool.templates.impl.wizard.NewArchimateModelFromTemplateWizard;



/**
 * Command Action Handler for new model from template
 * 
 * @author Phillip Beauvoir
 */
public class NewArchimateModelFromTemplateHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        NewArchimateModelFromTemplateWizard wizard = new NewArchimateModelFromTemplateWizard();
        
        WizardDialog dialog = new ExtendedWizardDialog(HandlerUtil.getActiveShell(event),
                wizard,
                "NewArchimateModelFromTemplateWizard"); //$NON-NLS-1$
        
        if(dialog.open() == Window.OK) {
            try {
                wizard.createNewModel();
            }
            catch(IOException ex) {
                ex.printStackTrace();
                MessageDialog.openError(Display.getCurrent().getActiveShell(),
                        Messages.NewArchimateModelFromTemplateHandler_0, ex.getMessage());
            }
        }

        return null;
    }

}
