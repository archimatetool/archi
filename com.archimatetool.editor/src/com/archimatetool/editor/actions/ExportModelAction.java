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
import com.archimatetool.editor.model.IModelExporter;
import com.archimatetool.model.IArchimateModel;


/**
 * Export Model Action used by extension plugins
 * 
 * @author Phillip Beauvoir
 */
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
                Object instance = ActionUtil.createExtensionPointInstance(ActionUtil.EXTENSIONPOINT_EXPORT_HANDLER, getId());
                if(instance instanceof IModelExporter exporter) {
                    exporter.export(model);
                }
            }
            catch(IOException | CoreException ex) {
                Logger.logError("Error on Export", ex); //$NON-NLS-1$
                MessageDialog.openError(workbenchWindow.getShell(), Messages.ExportModelAction_0, ex.getMessage());
            }
        }
    }
}