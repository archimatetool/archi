/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import junit.framework.TestSuite;

@SuppressWarnings("nls")
public class AllUITests {

    public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("com.archimatetool.editor.ui");
		
        // ui
        suite.addTest(FigureChooserTests.suite());

        return suite;
	}

}