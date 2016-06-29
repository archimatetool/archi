/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.reports.html;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

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
            HTMLReportExporter exporter = new HTMLReportExporter(model);
            exporter.preview();
        }

        return null;
    }
        
}
