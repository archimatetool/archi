/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate;

import java.io.File;
import java.io.IOException;


/**
 * Testing Utils
 * 
 * @author Phillip Beauvoir
 */
public class Testing {

    /**
     * Temporary folder
     */
    public static final String TESTS_TEMP_FOLDER = "uk.ac.bolton.tests.tmp"; //$NON-NLS-1$
    
    /**
     * Test Folder in Temp dir
     */
    public static File TMP_FOLDER = new File(System.getProperty("java.io.tmpdir"), TESTS_TEMP_FOLDER); //$NON-NLS-1$
    
    public static File TESTDATA_FOLDER = new File("testdata"); //$NON-NLS-1$

    
    /**
     * @return a Temporary Folder for testing.  Will be deleted on exit (if empty).
     */
    public static File getTempFolder(String folderName) {
        File tmp = new File(TMP_FOLDER, folderName);
        tmp.mkdirs();
        return tmp;
    }
    
    /**
     * @return a Temporary File handle for testing.  Will be deleted on exit.
     */
    public static File getTempFile(String extension) throws IOException {
        File tmp = File.createTempFile("~test", extension, getMainTestFolder()); //$NON-NLS-1$
        tmp.deleteOnExit();
        return tmp;
    }
    
    /**
     * @return The Temporary Testing Main Folder for testing.
     * It's best that Test routines that create folders delete this folder on tearDown()
     */
    public static File getMainTestFolder() {
        TMP_FOLDER.mkdirs();
        return TMP_FOLDER;
    }
    

}
