/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package org.opengroup.archimate.xmlexchange;

import java.io.File;

import com.archimatetool.tests.TestUtils;


/**
 * Testing Support
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class TestSupport {
    
    public static File TESTDATA_FOLDER = TestUtils.getLocalBundleFolder("org.opengroup.archimate.xmlexchange.tests", "testdata");
    public static File TEST_MODEL_FILE_ARCHISURANCE = new File(TESTDATA_FOLDER, "Archisurance.archimate");
    public static File XML_FILE1 = new File(TESTDATA_FOLDER, "Sample1.xml");
    
}
