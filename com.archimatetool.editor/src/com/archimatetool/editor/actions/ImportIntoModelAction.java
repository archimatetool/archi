/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import com.archimatetool.editor.model.ISelectedModelImporter;
import com.archimatetool.model.IArchimateModel;


/**
 * Inport Into Model Action used by extension plugins
 * 
 * @author Phillip Beauvoir
 */
public class ImportIntoModelAction extends AbstractModelAction {
    
    private ISelectedModelImporter fImporter;

    public ImportIntoModelAction(IWorkbenchWindow window, String id, String label, ISelectedModelImporter importer) {
        super(label, window);
        setId(id);
        fImporter = importer;
    }
    
    @Override
    public void run() {
        IArchimateModel model = getActiveArchimateModel();
        if(model != null && fImporter != null) {
            try {
                fImporter.doImport(model);
            }
            catch(IOException ex) {
                MessageDialog.openError(workbenchWindow.getShell(), Messages.ImportIntoModelAction_0, ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
}