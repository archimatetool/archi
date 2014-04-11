/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.csv;


import junit.framework.TestSuite;

import com.archimatetool.csv.export.CSVExporterTests;
import com.archimatetool.csv.importer.CSVImporterTests;

@SuppressWarnings("nls")
public class AllTests {

    public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("com.archimatetool.csv");

		suite.addTest(CSVExporterTests.suite());
        suite.addTest(CSVImporterTests.suite());
		
        return suite;
	}

}