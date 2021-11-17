/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.reports.html;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.archimatetool.editor.Logger;
import com.archimatetool.model.IArchimateModel;



/**
 * Command Action Handler for HTML Report
 * 
 * @author Phillip Beauvoir
 */
public class HTMLReportHandler extends AbstractHandler {
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchPart part = HandlerUtil.getActivePart(event);
        IArchimateModel model = part != null ? part.getAdapter(IArchimateModel.class) : null;

        if(model != null) {
            try {
                HTMLReportExporter exporter = new HTMLReportExporter(model);
                exporter.export();
            }
            catch(Exception ex) {
                Logger.log(IStatus.ERROR, "Error saving HTML Report", ex); //$NON-NLS-1$
                MessageDialog.openError(HandlerUtil.getActiveShell(event),
                        Messages.HTMLReportAction_0,
                        (ex.getMessage() == null ? ex.toString() : ex.getMessage()) );
            }
        }

        return null;
    }
        
}
