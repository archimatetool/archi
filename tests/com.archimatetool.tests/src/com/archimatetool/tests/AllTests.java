/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.tests;

import junit.framework.TestSuite;

@SuppressWarnings("nls")
public class AllTests {

    public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("All JUnit Tests");
		
		suite.addTest(com.archimatetool.canvas.AllTests.suite());
		suite.addTest(com.archimatetool.commandline.AllTests.suite());
		suite.addTest(com.archimatetool.csv.AllTests.suite());
		suite.addTest(com.archimatetool.editor.AllTests.suite());
		suite.addTest(com.archimatetool.export.svg.AllTests.suite());
		suite.addTest(com.archimatetool.hammer.AllTests.suite());
		suite.addTest(com.archimatetool.help.AllTests.suite());
		suite.addTest(com.archimatetool.jasperreports.AllTests.suite());
		suite.addTest(com.archimatetool.jdom.AllTests.suite());
		suite.addTest(com.archimatetool.model.AllTests.suite());
		suite.addTest(com.archimatetool.modelimporter.AllTests.suite());
		suite.addTest(com.archimatetool.reports.AllTests.suite());
		suite.addTest(com.archimatetool.zest.AllTests.suite());
		suite.addTest(org.opengroup.archimate.xmlexchange.AllTests.suite());

		return suite;
	}
}