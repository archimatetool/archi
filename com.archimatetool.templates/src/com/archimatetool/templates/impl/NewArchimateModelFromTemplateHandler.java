/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.templates.impl;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;
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
        WizardDialog dialog = new ExtendedWizardDialog(HandlerUtil.getActiveShell(event),
                new NewArchimateModelFromTemplateWizard(),
                "NewArchimateModelFromTemplateWizard"); //$NON-NLS-1$
        dialog.open();

        return null;
    }

}
