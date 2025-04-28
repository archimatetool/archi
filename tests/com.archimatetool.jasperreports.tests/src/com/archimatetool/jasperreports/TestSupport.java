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
    
    private static File REPORTS_FOLDER = TestUtils.getLocalBundleFolder("com.archimatetool.jasperreports", "reports");
    public static File CUSTOM_REPORT_MAIN_FILE = new File(REPORTS_FOLDER, "Customizable Report/main.jrxml");
    
}
