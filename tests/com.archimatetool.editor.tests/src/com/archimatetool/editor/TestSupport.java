/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import java.io.File;
import java.io.IOException;

import com.archimatetool.tests.TestUtils;


/**
 * Testing Support
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class TestSupport {
    
    private static File testFolder;
    
    public static File TEST_MODEL_FILE_ARCHISURANCE = new File(getTestDataFolder(), "models/Archisurance.archimate");
    public static File TEST_MODEL_FILE_1 = new File(getTestDataFolder(), "models/testmodel1.archimate");
    public static File TEST_MODEL_FILE_ZIPPED = new File(getTestDataFolder(), "models/model_zipped.archimate");

    public static File getTestDataFolder() {
        if(testFolder == null) {
            testFolder = TestUtils.getLocalBundleFolder("com.archimatetool.editor.tests", "testdata");
        }
        return testFolder;
    }
    
    /**
     * Will compare a given source Folder with a target Folder and compare that the files
     * therein are all there and are the same sizes
     * @param srcFolder
     * @param targetFolder
     * @throws IOException
     */
    public static void checkSourceAndTargetFolderSame(File srcFolder, File targetFolder) throws IOException {
        File[] srcFiles = srcFolder.listFiles();
        for(int i = 0; i < srcFiles.length; i++) {
            File srcFile = srcFiles[i];
            if(srcFile.isDirectory()) {
                File subFolder = new File(targetFolder, srcFile.getName());
                checkSourceAndTargetFolderSame(srcFile, subFolder);
            }
            else {
                File targetFile = new File(targetFolder, srcFile.getName());
                checkSourceAndTargetFileSame(srcFile, targetFile);
            }
        }
    }
    
    /**
     * Will compare a given source File with a target File and compare that the file is there
     * and is the same size as the source File
     * @param srcFile
     * @param targetFile
     * @throws IOException
     */
    public static void checkSourceAndTargetFileSame(File srcFile, File targetFile) throws IOException {
        if(!srcFile.exists()) {
            throw new IOException("Source File doesn't exist: " + targetFile);
        }
        if(!targetFile.exists()) {
            throw new IOException("Target File doesn't exist: " + targetFile);
        }
        if(targetFile.length() != srcFile.length()) {
            throw new IOException("Files don't compare in size: " + srcFile + " and " + targetFile);
        }
    }
}
