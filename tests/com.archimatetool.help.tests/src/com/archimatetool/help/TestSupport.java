/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.help;

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
            testFolder = TestUtils.getLocalBundleFolder("com.archimatetool.help.tests", "testdata");
        }
        return testFolder;
    }
}
