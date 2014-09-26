/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import junit.framework.TestSuite;

@SuppressWarnings("nls")
public class AllPropertySectionsTests {

    public static junit.framework.Test suite() {
        TestSuite suite = new TestSuite("com.archimatetool.editor.propertysections");

        suite.addTest(PropertiesLabelProviderTests.suite());
        
        return suite;
    }

}