/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.model.importer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import uk.ac.bolton.archimate.editor.model.IModelImporter;


/**
 * Determine type of file to import
 * 
 * @author Phillip Beauvoir
 */
public class ImportManager {
    
    public static String BIZZDESIGN_ARCHITECT_2 = "BiZZdesignArchitect2";
    public static String BIZZDESIGN_ARCHITECT_3 = "BiZZdesignArchitect2";
    
    public static String BIZZDESIGN_ARCHITECT_EXT = ".xma";
    
    /**
     * Get a Model Importer for a given file
     * 
     * @param file The file to import
     * @return A Model Importer for the given file or null if there isn't one.
     * @throws IOException
     */
    public static IModelImporter getImporter(File file) throws IOException {
        if(file == null || !file.exists() || !file.canRead()) {
            throw new IOException("Cannot read file");
        }
        
        IModelImporter importer = null;
        
        if(file.getName().toLowerCase().endsWith(BIZZDESIGN_ARCHITECT_EXT)) {
            importer = getBiZZdesignImporter(file);
        }
        
        return importer;
    }
    
    /**
     * BiZZdesign Importer
     */
    private static IModelImporter getBiZZdesignImporter(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        char[] buf = new char[256];
        reader.read(buf);
        String s = new String(buf);
        reader.close();
        
        if(s.contains("MM_Document version=\"1.0\"")) {
            return new BiZZdesign2Importer(file);
        }
        
        return null;
    }
}
