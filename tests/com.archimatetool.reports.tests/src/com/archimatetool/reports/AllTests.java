/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.reports;


import com.archimatetool.reports.html.HTMLReportExporterTests;

import junit.framework.TestSuite;

@SuppressWarnings("nls")
public class AllTests {

    public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("com.archimatetool.reports");

		suite.addTest(HTMLReportExporterTests.suite());
		
        return suite;
	}

}