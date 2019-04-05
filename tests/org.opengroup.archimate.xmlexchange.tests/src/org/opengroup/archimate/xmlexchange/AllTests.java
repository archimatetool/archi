/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package org.opengroup.archimate.xmlexchange;


import junit.framework.TestSuite;

@SuppressWarnings("nls")
public class AllTests {

    public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("org.opengroup.archimate.xmlexchange");

        suite.addTest(XMLExchangeUtilsTests.suite());
		suite.addTest(XMLModelExporterTests.suite());
        suite.addTest(XMLModelImporterTests.suite());
        suite.addTest(XMLValidatorTests.suite());
		
        return suite;
	}

}