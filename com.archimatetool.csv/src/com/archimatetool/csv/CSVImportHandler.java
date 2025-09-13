package com.archimatetool.csv;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.archimatetool.csv.importer.CSVImportProvider;
import com.archimatetool.model.IArchimateModel;



/**
 * Command Action Handler for Import
 * 
 * @author Phillip Beauvoir
 */
public class CSVImportHandler extends AbstractHandler {
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchPart part = HandlerUtil.getActivePart(event);
        IArchimateModel model = part != null ? part.getAdapter(IArchimateModel.class) : null;
        
        if(model != null) {
            new CSVImportProvider().doImport(HandlerUtil.getActiveWorkbenchWindow(event), model);
        }

        return null;
    }
}
