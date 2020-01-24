/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.modelimporter;


import junit.framework.TestSuite;

@SuppressWarnings("nls")
public class AllTests {

    public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("com.archimatetool.modelimporter");

        suite.addTest(ModelImporterTests.suite());
		
        return suite;
	}

}