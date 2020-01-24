/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.tests;

import java.lang.reflect.Method;

import junit.framework.Test;
import junit.framework.TestSuite;

@SuppressWarnings("nls")
public class AllTests {

    public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("All JUnit Tests");
		
        suite.addTest(getTest("com.archimatetool.canvas.AllTests"));
        suite.addTest(getTest("com.archimatetool.commandline.AllTests"));
        suite.addTest(getTest("com.archimatetool.csv.AllTests"));
        suite.addTest(getTest("com.archimatetool.editor.AllTests"));
        suite.addTest(getTest("com.archimatetool.export.svg.AllTests"));
        suite.addTest(getTest("com.archimatetool.hammer.AllTests"));
        suite.addTest(getTest("com.archimatetool.help.AllTests"));
        suite.addTest(getTest("com.archimatetool.jasperreports.AllTests"));
        suite.addTest(getTest("com.archimatetool.jdom.AllTests"));
		suite.addTest(getTest("com.archimatetool.model.AllTests"));
        suite.addTest(getTest("com.archimatetool.modelimporter.AllTests"));
        suite.addTest(getTest("com.archimatetool.reports.AllTests"));
        suite.addTest(getTest("com.archimatetool.zest.AllTests"));
        suite.addTest(getTest("org.opengroup.archimate.xmlexchange.AllTests"));

		return suite;
	}

    /**
     * Use reflection to get classes in test fragments
     * See http://rcpquickstart.wordpress.com/2007/06/20/unit-testing-plug-ins-with-fragments/
     */
	private static Test getTest(String suiteClassName) {
		try {
			Class<?> clazz = Class.forName(suiteClassName);
			Method suiteMethod = clazz.getMethod("suite", new Class[0]);
			return (Test) suiteMethod.invoke(null, new Object[0]);
		} catch (Exception e) {
			throw new RuntimeException("Error", e);
		}
	}
}