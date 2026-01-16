package com.archimatetool.csv.importer;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.archimatetool.csv.CSVParseException;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.modelimporter.ImportException;



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
            doImport(HandlerUtil.getActiveWorkbenchWindow(event), model);
        }

        return null;
    }
    
    private void doImport(IWorkbenchWindow window, IArchimateModel model) {
        File file = askOpenFile(window);
        if(file == null) {
            return;
        }
        
        // Check file is valid
        if(!CSVImporter.isElementsFileName(file) && !CSVImporter.isRelationsFileName(file) && !CSVImporter.isPropertiesFileName(file)) {
            MessageDialog.openError(window.getShell(), Messages.CSVImportHandler_0, Messages.CSVImportHandler_1);
            return;
        }
        
        // Import
        try {
            CSVImporter importer =  new CSVImporter(model);
            importer.doImport(file);
        }
        catch(CSVParseException | ImportException | IOException ex) {
            MessageDialog.openError(window.getShell(), Messages.CSVImportHandler_0, ex.getMessage());
        }
    }

    /**
     * User should select elements file of format "xxx-elements.csv"
     * The "xxx-" prefix is optional
     */
    private File askOpenFile(IWorkbenchWindow window) {
        FileDialog dialog = new FileDialog(window.getShell(), SWT.OPEN);
        dialog.setFilterExtensions(new String[] { "*.csv", "*.txt", "*.*" } ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        String path = dialog.open();
        return path != null ? new File(path) : null;
    }
}
