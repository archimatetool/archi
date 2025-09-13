/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import com.archimatetool.editor.Logger;
import com.archimatetool.editor.model.ISelectedModelImporter;
import com.archimatetool.model.IArchimateModel;


/**
 * Inport Into Model Action used by extension plugins
 * 
 * @author Phillip Beauvoir
 */
public class ImportIntoModelAction extends AbstractModelAction {
    
    public ImportIntoModelAction(IWorkbenchWindow window, String id, String label) {
        super(label, window);
        setId(id);
        setActionDefinitionId(id);
        
        // Register this with the handler service
        ActionUtil.registerCommandHandler(window, this, ActionUtil.createCommandName(label, Messages.ImportIntoModelAction_1));
    }
    
    @Override
    public void run() {
        IArchimateModel model = getActiveArchimateModel();
        if(model != null) {
            try {
                Object instance = ActionUtil.createExtensionPointInstance(ActionUtil.EXTENSIONPOINT_IMPORT_HANDLER, getId());
                if(instance instanceof ISelectedModelImporter importer) {
                    importer.doImport(model);
                }
            }
            catch(IOException | CoreException ex) {
                Logger.logError("Error on Export", ex); //$NON-NLS-1$
                MessageDialog.openError(workbenchWindow.getShell(), Messages.ImportIntoModelAction_0, ex.getMessage());
            }
        }
    }
}