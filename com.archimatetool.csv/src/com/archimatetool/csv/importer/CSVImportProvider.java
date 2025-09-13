/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.csv.importer;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchWindow;

import com.archimatetool.csv.CSVConstants;
import com.archimatetool.csv.CSVParseException;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.modelimporter.ImportException;



/**
 * Import CSV Provider
 * 
 * @author Phillip Beauvoir
 */
public class CSVImportProvider implements CSVConstants {
    
    public void doImport(IWorkbenchWindow window, IArchimateModel model) {
        File file = askOpenFile(window);
        if(file == null) {
            return;
        }
        
        // Check file is valid
        if(!CSVImporter.isElementsFileName(file) && !CSVImporter.isRelationsFileName(file) && !CSVImporter.isPropertiesFileName(file)) {
            MessageDialog.openError(window.getShell(), Messages.CSVImportProvider_1, Messages.CSVImportProvider_0);
            return;
        }
        
        // Import
        try {
            CSVImporter importer =  new CSVImporter(model);
            importer.doImport(file);
        }
        catch(CSVParseException | ImportException | IOException ex) {
            MessageDialog.openError(window.getShell(), Messages.CSVImportProvider_1, ex.getMessage());
        }
    }

    /**
     * User should select elements file of format "xxx-elements.csv"
     * The "xxx-" prefix is optional
     */
    File askOpenFile(IWorkbenchWindow window) {
        FileDialog dialog = new FileDialog(window.getShell(), SWT.OPEN);
        dialog.setFilterExtensions(new String[] { "*.csv", "*.txt", "*.*" } ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        String path = dialog.open();
        return path != null ? new File(path) : null;
    }

}
