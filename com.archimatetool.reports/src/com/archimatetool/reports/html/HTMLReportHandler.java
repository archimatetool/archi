/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.reports.html;

import java.io.IOException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;

import com.archimatetool.editor.actions.AbstractModelSelectionHandler;
import com.archimatetool.model.IArchimateModel;



/**
 * Command Action Handler for HTML Report
 * 
 * @author Phillip Beauvoir
 */
public class HTMLReportHandler extends AbstractModelSelectionHandler {
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IArchimateModel model = getActiveArchimateModel();
        if(model != null) {
            try {
                HTMLReportExporter exporter = new HTMLReportExporter(model);
                exporter.export();
            }
            catch(IOException ex) {
                MessageDialog.openError(workbenchWindow.getShell(), Messages.HTMLReportAction_0, ex.getMessage());
                ex.printStackTrace();
            }
        }

        return null;
    }
        
}
