/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.actions;

import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;

import uk.ac.bolton.archimate.editor.model.IModelExporter;
import uk.ac.bolton.archimate.model.IArchimateModel;

/**
 * Export Model Action used by extension plugins
 * 
 * @author Phillip Beauvoir
 */
public class ExportModelAction extends AbstractModelSelectionAction {
    
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
    
    @Override
    protected void updateState() {
        setEnabled(getActiveArchimateModel() != null);
    }
}