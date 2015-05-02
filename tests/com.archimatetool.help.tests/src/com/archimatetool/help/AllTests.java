/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.help;

import junit.framework.TestSuite;

import com.archimatetool.help.cheatsheets.CreateMapViewCheatSheetActionTests;

@SuppressWarnings("nls")
public class AllTests {

    public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("com.archimatetool.help");

		// impl
        suite.addTest(CreateMapViewCheatSheetActionTests.suite());

        return suite;
	}

}