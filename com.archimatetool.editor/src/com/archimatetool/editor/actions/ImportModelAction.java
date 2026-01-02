/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import java.io.IOException;
import java.util.Objects;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import com.archimatetool.editor.Logger;
import com.archimatetool.editor.model.IModelImporter;


/**
 * Legacy Import Model Action used by extension plugins
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("deprecation")
public class ImportModelAction extends Action
implements IWorkbenchAction {
    
    private IWorkbenchWindow workbenchWindow;

    public ImportModelAction(IWorkbenchWindow window, String id, String label) {
        super(label);
        workbenchWindow = window;
        setId(id);
    }
    
    @Override
    public void run() {
        try {
            if(createExtensionPointInstance(getId()) instanceof IModelImporter importer) {
                importer.doImport();
            }
        }
        catch(IOException | CoreException ex) {
            Logger.error("Error on Import", ex); //$NON-NLS-1$
            MessageDialog.openError(workbenchWindow.getShell(), Messages.ImportModelAction_0, ex.getMessage());
        }
    }
    
    /**
     * Dynamically create an instance of IModelImporter
     */
    private Object createExtensionPointInstance(String extensionId) throws CoreException {
        for(IConfigurationElement configurationElement : Platform.getExtensionRegistry().getConfigurationElementsFor("com.archimatetool.editor.importHandler")) { //$NON-NLS-1$
            if(Objects.equals(configurationElement.getAttribute("id"), extensionId)) { //$NON-NLS-1$
                return configurationElement.createExecutableExtension("class"); //$NON-NLS-1$
            }
        }
        
        return null;
    }
    
    @Override
    public void dispose() {
        workbenchWindow = null;
    }
}