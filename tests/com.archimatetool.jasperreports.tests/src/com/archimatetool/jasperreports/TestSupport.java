/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports;

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
    
    public static File STANDARD_REPORT_MAIN_FILE = new File(getReportsFolder(), "Standard Report/main.jrxml");
    public static File CUSTOM_REPORT_MAIN_FILE = new File(getReportsFolder(), "Customizable Report/main.jrxml");

    public static File getReportsFolder() {
        if(testFolder == null) {
            testFolder = TestUtils.getLocalBundleFolder("com.archimatetool.jasperreports", "reports");
        }
        return testFolder;
    }
}
