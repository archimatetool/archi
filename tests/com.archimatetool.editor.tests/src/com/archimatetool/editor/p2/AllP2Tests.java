/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.p2;

import junit.framework.TestSuite;

@SuppressWarnings("nls")
public class AllP2Tests {

    public static junit.framework.Test suite() {
        TestSuite suite = new TestSuite("com.archimatetool.editor.p2");

        suite.addTest(DropinsPluginHandlerTests.suite());
        
        return suite;
    }

}