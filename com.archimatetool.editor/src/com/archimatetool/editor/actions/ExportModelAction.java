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
import com.archimatetool.editor.model.IModelExporter;
import com.archimatetool.model.IArchimateModel;


/**
 * Legacy Export Model Action used by extension plugins
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("deprecation")
public class ExportModelAction extends AbstractModelAction {
    
    public ExportModelAction(IWorkbenchWindow window, String id, String label) {
        super(label, window);
        setId(id);
    }
    
    @Override
    public void run() {
        IArchimateModel model = getActiveArchimateModel();
        if(model != null) {
            try {
                if(createExtensionPointInstance(getId()) instanceof IModelExporter exporter) {
                    exporter.export(model);
                }
            }
            catch(IOException | CoreException ex) {
                Logger.error("Error on Export", ex); //$NON-NLS-1$
                MessageDialog.openError(workbenchWindow.getShell(), Messages.ExportModelAction_0, ex.getMessage());
            }
        }
    }
    
    /**
     * Dynamically create an instance of IModelExporter
     */
    private Object createExtensionPointInstance(String extensionId) throws CoreException {
        for(IConfigurationElement configurationElement : Platform.getExtensionRegistry().getConfigurationElementsFor("com.archimatetool.editor.exportHandler")) { //$NON-NLS-1$
            if(Objects.equals(configurationElement.getAttribute("id"), extensionId)) { //$NON-NLS-1$
                return configurationElement.createExecutableExtension("class"); //$NON-NLS-1$
            }
        }
        
        return null;
    }

}