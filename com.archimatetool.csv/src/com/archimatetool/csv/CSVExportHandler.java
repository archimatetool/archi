package com.archimatetool.csv;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.archimatetool.csv.export.CSVExportProvider;
import com.archimatetool.model.IArchimateModel;



/**
 * Command Action Handler for Export
 * 
 * @author Phillip Beauvoir
 */
public class CSVExportHandler extends AbstractHandler {
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchPart part = HandlerUtil.getActivePart(event);
        IArchimateModel model = part != null ? part.getAdapter(IArchimateModel.class) : null;
        
        if(model != null) {
            new CSVExportProvider().export(HandlerUtil.getActiveWorkbenchWindow(event), model);
        }

        return null;
    }
}
