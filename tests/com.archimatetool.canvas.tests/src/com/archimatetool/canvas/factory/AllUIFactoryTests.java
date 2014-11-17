/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.factory;

import junit.framework.TestSuite;

@SuppressWarnings("nls")
public class AllUIFactoryTests {

    public static junit.framework.Test suite() {
        TestSuite suite = new TestSuite("com.archimatetool.canvas.factory");
        
        suite.addTest(CanvasBlockUIProviderTests.suite());
        suite.addTest(CanvasImageUIProviderTests.suite());
        suite.addTest(CanvasLineConnectionUIProviderTests.suite());
        suite.addTest(CanvasModelUIProviderTests.suite());
        suite.addTest(CanvasStickyUIProviderTests.suite());

        return suite;
    }

}