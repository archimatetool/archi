package com.archimatetool.zest;

import java.io.File;

import com.archimatetool.tests.TestUtils;


/**
 * Testing Support
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class TestSupport {
    
    private static File testFolder;
    
    public static File TEST_MODEL_FILE_ARCHISURANCE = new File(getTestDataFolder(), "Archisurance.archimate");

    public static File getTestDataFolder() {
        if(testFolder == null) {
            testFolder = TestUtils.getLocalBundleFolder("com.archimatetool.zest.tests", "testdata");
        }
        return testFolder;
    }

}
