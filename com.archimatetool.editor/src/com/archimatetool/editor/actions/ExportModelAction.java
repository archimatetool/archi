/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import com.archimatetool.editor.model.IModelExporter;
import com.archimatetool.model.IArchimateModel;


/**
 * Export Model Action used by extension plugins
 * 
 * @author Phillip Beauvoir
 */
public class ExportModelAction extends AbstractModelAction {
    
    private IModelExporter fExporter;

    public ExportModelAction(IWorkbenchWindow window, String id, String label, IModelExporter exporter) {
        super(label, window);
        setId(id);
        fExporter = exporter;
    }
    
    @Override
    public void run() {
        IArchimateModel model = getActiveArchimateModel();
        if(model != null && fExporter != null) {
            try {
                fExporter.export(model);
            }
            catch(IOException ex) {
                MessageDialog.openError(workbenchWindow.getShell(), Messages.ExportModelAction_0, ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}