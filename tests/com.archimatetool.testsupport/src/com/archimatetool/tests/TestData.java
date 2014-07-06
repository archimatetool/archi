/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.tests;

import java.io.File;



/**
 * Testing Data
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class TestData {

    private static File testFolder;
    
    public static File TEST_MODEL_FILE_ARCHISURANCE = new File(getTestDataFolder(), "models/Archisurance.archimate");

    public static File getTestDataFolder() {
        if(testFolder == null) {
            testFolder = TestUtils.getLocalBundleFolder("com.archimatetool.testsupport", "testdata");
        }
        return testFolder;
    }

}
