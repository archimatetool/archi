/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.archimatetool.editor.Logger;
import com.archimatetool.editor.model.IModelImporter;


/**
 * Import Model Action used by extension plugins
 * 
 * @author Phillip Beauvoir
 */
public class ImportModelAction extends Action implements IWorkbenchAction {
    
    private IWorkbenchWindow workbenchWindow;

    public ImportModelAction(IWorkbenchWindow window, String id, String label) {
        super(label);
        workbenchWindow = window;
        setId(id);
    }
    
    @Override
    public void run() {
        try {
            Object instance = ActionUtil.createExtensionPointInstance(ActionUtil.EXTENSIONPOINT_IMPORT_HANDLER, getId());
            if(instance instanceof IModelImporter importer) {
                importer.doImport();
            }
        }
        catch(IOException | CoreException ex) {
            Logger.logError("Error on Import", ex); //$NON-NLS-1$
            MessageDialog.openError(workbenchWindow.getShell(), Messages.ImportModelAction_0, ex.getMessage());
        }
    }
    
    @Override
    public void dispose() {
        workbenchWindow = null;
    }
}