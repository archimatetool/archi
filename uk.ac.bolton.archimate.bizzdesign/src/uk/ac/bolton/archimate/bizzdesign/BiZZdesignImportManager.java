/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.bizzdesign;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

import uk.ac.bolton.archimate.editor.model.IModelImporter;


/**
 * Determine type of file to import
 * 
 * @author Phillip Beauvoir
 */
public class BiZZdesignImportManager implements IModelImporter {
    
    public static String BIZZDESIGN_ARCHITECT_EXT_WILDCARD = "*.xma";
    

    @Override
    public void doImport() throws IOException {
        File file = askOpenFile();
        if(file == null) {
            return;
        }
        
        IModelImporter importer = getImporter(file);
        if(importer != null) {
            importer.doImport();
        }
        else {
            throw new IOException("Cannot import. Unknown format.");
        }
    }

    /**
     * Get a Model Importer for a given file
     * 
     * @param file The file to import
     * @return A Model Importer for the given file or null if there isn't one.
     * @throws IOException
     */
    private IModelImporter getImporter(File file) throws IOException {
        if(file == null || !file.exists() || !file.canRead()) {
            throw new IOException("Cannot read file");
        }
        
        BufferedReader reader = new BufferedReader(new FileReader(file));
        char[] buf = new char[256];
        reader.read(buf);
        String s = new String(buf);
        reader.close();
        
        if(s.contains("MM_Document version=\"1.0\"")) {
            return new BiZZdesign2Importer(file);
        }
        
        if(s.contains("MM_Document version=\"2.0\"")) {
            //return new BiZZdesign3Importer(file);
        }
        
        return null;
    }
    
    private File askOpenFile() {
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
        dialog.setFilterExtensions(new String[] { BIZZDESIGN_ARCHITECT_EXT_WILDCARD, "*.*" } );
        String path = dialog.open();
        return path != null ? new File(path) : null;
    }

}
