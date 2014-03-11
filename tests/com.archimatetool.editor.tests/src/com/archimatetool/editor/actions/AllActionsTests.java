/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import junit.framework.TestSuite;

@SuppressWarnings("nls")
public class AllActionsTests {

    public static junit.framework.Test suite() {
        TestSuite suite = new TestSuite("com.archimatetool.editor.actions");

        suite.addTest(CheckForNewVersionActionTests.suite());
        suite.addTest(MRUMenuManagerTests.suite());

        return suite;
    }

}