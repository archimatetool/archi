/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.modelimporter;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.archimatetool.editor.Logger;
import com.archimatetool.editor.model.ModelChecker;
import com.archimatetool.editor.ui.components.ExtendedWizardDialog;
import com.archimatetool.model.IArchimateModel;


/**
 * Command Action Handler for Import
 * 
 * @author Phillip Beauvoir
 */
public class ImportModelHandler extends AbstractHandler {
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchPart part = HandlerUtil.getActivePart(event);
        IArchimateModel model = part != null ? part.getAdapter(IArchimateModel.class) : null;
        
        if(model != null) {
            IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
            
            try {
                doImport(window, model);
            }
            catch(Exception ex) {
                Logger.logError("Error on Export", ex); //$NON-NLS-1$
                MessageDialog.openError(window.getShell(), Messages.ImportModelHandler_1, ex.getMessage());
            }
        }
        
        return null;
    }
    
    private void doImport(IWorkbenchWindow window, IArchimateModel targetModel) throws Exception {
        ImportModelWizard wizard = new ImportModelWizard();
        
        WizardDialog dialog = new ExtendedWizardDialog(window.getShell(),
                wizard,
                "ImportModelWizard") { //$NON-NLS-1$

            @Override
            protected void createButtonsForButtonBar(Composite parent) {
                super.createButtonsForButtonBar(parent); // Change "Finish" to "Import"
                Button b = getButton(IDialogConstants.FINISH_ID);
                b.setText(Messages.ImportModelHandler_0);
            }
        };
        
        if(dialog.open() == Window.OK) {
            File importedFile = wizard.getFile();
            if(importedFile == null) {
                return;
            }
            
            ModelImporter importer = new ModelImporter();
            
            importer.setUpdate(wizard.shouldUpdate());
            importer.setUpdateAll(wizard.shouldUpdateAll());
            
            Exception[] exception = new Exception[1];

            BusyIndicator.showWhile(Display.getCurrent(), () -> {
                try {
                    importer.doImport(importedFile, targetModel);
                }
                catch(Exception ex) {
                    exception[0] = ex;
                }
            });
            
            if(exception[0] != null) {
                throw exception[0];
            }
            
            // Run the Model checker now
            ModelChecker checker = new ModelChecker(targetModel);
            
            // Show Model Checker Errors
            if(!checker.checkAll()) {
                checker.showErrorDialog(window.getShell());
            }
            // Show Status Dialog
            else if(wizard.shouldShowStatusDialog()) {
                StatusDialog statusDialog = new StatusDialog(window.getShell(), importer.getStatusMessages());
                statusDialog.open();
            }
        }
    }
}
