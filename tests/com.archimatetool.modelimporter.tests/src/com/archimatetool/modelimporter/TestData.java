/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.modelimporter;

import java.io.File;

import com.archimatetool.tests.TestUtils;



/**
 * Testing Data
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class TestData {

    private static File testFolder;
    
    public static File TEST_MODEL_FILE = new File(getTestDataFolder(), "test.archimate");

    public static File getTestDataFolder() {
        if(testFolder == null) {
            testFolder = TestUtils.getLocalBundleFolder("com.archimatetool.modelimporter.tests", "testdata");
        }
        return testFolder;
    }

}
