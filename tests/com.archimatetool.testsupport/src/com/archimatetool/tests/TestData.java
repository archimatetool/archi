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

    public static File TESTDATA_FOLDER = TestUtils.getLocalBundleFolder("com.archimatetool.testsupport", "testdata");;
    public static File TEST_MODEL_FILE_ARCHISURANCE = new File(TESTDATA_FOLDER, "models/Archisurance.archimate");

}
