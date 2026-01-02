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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import com.archimatetool.editor.Logger;
import com.archimatetool.editor.model.ISelectedModelImporter;
import com.archimatetool.model.IArchimateModel;


/**
 * Legacy Import Into Model Action used by extension plugins
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("deprecation")
public class ImportIntoModelAction extends AbstractModelAction {
    
    public ImportIntoModelAction(IWorkbenchWindow window, String id, String label) {
        super(label, window);
        setId(id);
    }
    
    @Override
    public void run() {
        IArchimateModel model = getActiveArchimateModel();
        if(model != null) {
            try {
                if(createExtensionPointInstance(getId()) instanceof ISelectedModelImporter importer) {
                    importer.doImport(model);
                }
            }
            catch(IOException | CoreException ex) {
                Logger.error("Error on Export", ex); //$NON-NLS-1$
                MessageDialog.openError(workbenchWindow.getShell(), Messages.ImportIntoModelAction_0, ex.getMessage());
            }
        }
    }
    
    /**
     * Dynamically create an instance of ISelectedModelImporter
     */
    private Object createExtensionPointInstance(String extensionId) throws CoreException {
        for(IConfigurationElement configurationElement : Platform.getExtensionRegistry().getConfigurationElementsFor("com.archimatetool.editor.importHandler")) { //$NON-NLS-1$
            if(Objects.equals(configurationElement.getAttribute("id"), extensionId)) { //$NON-NLS-1$
                return configurationElement.createExecutableExtension("class"); //$NON-NLS-1$
            }
        }
        
        return null;
    }
}