/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.utils;

import junit.framework.TestSuite;

@SuppressWarnings("nls")
public class AllUtilsTests {

    public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("com.archimatetool.editor.utils");
		
        // utils
        suite.addTest(FileUtilsTests.suite());
        suite.addTest(HTMLUtilsTests.suite());
        suite.addTest(StringUtilsTests.suite());
        suite.addTest(ZipUtilsTests.suite());

        return suite;
	}

}