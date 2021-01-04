/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.reports.html;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;

import com.archimatetool.editor.Logger;
import com.archimatetool.editor.actions.AbstractModelSelectionHandler;
import com.archimatetool.model.IArchimateModel;



/**
 * Command Action Handler for Preview HTML Report
 * 
 * @author Phillip Beauvoir
 */
public class PreviewHTMLReportHandler extends AbstractModelSelectionHandler {
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IArchimateModel model = getActiveArchimateModel();
        if(model != null) {
            try {
                HTMLReportExporter exporter = new HTMLReportExporter(model);
                exporter.preview();
            }
            catch(Exception ex) {
                Logger.log(IStatus.ERROR, "Error saving HTML Report", ex); //$NON-NLS-1$
                MessageDialog.openError(workbenchWindow.getShell(),
                        Messages.HTMLReportAction_0,
                        (ex.getMessage() == null ? ex.toString() : ex.getMessage()) );
            }
        }

        return null;
    }
        
}
