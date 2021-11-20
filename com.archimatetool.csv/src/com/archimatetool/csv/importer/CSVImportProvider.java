/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.csv.importer;

import java.io.File;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

import com.archimatetool.csv.CSVConstants;
import com.archimatetool.csv.CSVParseException;
import com.archimatetool.editor.model.ISelectedModelImporter;
import com.archimatetool.model.IArchimateModel;



/**
 * Import CSV Provider
 * 
 * @author Phillip Beauvoir
 */
public class CSVImportProvider implements ISelectedModelImporter, CSVConstants {
    
    @Override
    public void doImport(IArchimateModel model) throws IOException {
        File file = askOpenFile();
        if(file == null) {
            return;
        }
        
        // Check file is valid
        if(!CSVImporter.isElementsFileName(file) && !CSVImporter.isRelationsFileName(file) && !CSVImporter.isPropertiesFileName(file)) {
            throw new IOException(Messages.CSVImportProvider_0);
        }
        
        // Import
        try {
            CSVImporter importer =  new CSVImporter(model);
            importer.doImport(file);
        }
        catch(CSVParseException ex) {
            throw new IOException(ex.getMessage());
        }
    }

    /**
     * User should select elements file of format "xxx-elements.csv"
     * The "xxx-" prefix is optional
     */
    File askOpenFile() {
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
        dialog.setFilterExtensions(new String[] { "*.csv", "*.txt", "*.*" } ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        String path = dialog.open();
        return path != null ? new File(path) : null;
    }

}
