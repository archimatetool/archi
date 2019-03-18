/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;

import com.archimatetool.editor.actions.AbstractModelSelectionHandler;
import com.archimatetool.editor.ui.components.ExtendedWizardDialog;
import com.archimatetool.model.IArchimateModel;



/**
 * Command Action Handler for Jasper Reports
 * 
 * @author Phillip Beauvoir
 */
public class JasperReportsHandler extends AbstractModelSelectionHandler {
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IArchimateModel model = getActiveArchimateModel();
        if(model != null) {
            ExportJasperReportsWizard wizard = new ExportJasperReportsWizard(model);
            
            WizardDialog dialog = new ExtendedWizardDialog(workbenchWindow.getShell(),
                    wizard,
                    "ExportJasperReportsWizard"); //$NON-NLS-1$
            
            if(dialog.open() == Window.OK) {
                try {
                    wizard.runWithProgress();
                }
                catch(InvocationTargetException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return null;
    }
    
}
