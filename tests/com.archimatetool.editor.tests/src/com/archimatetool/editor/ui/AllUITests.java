/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import junit.framework.TestSuite;

import com.archimatetool.editor.ui.factory.AllUIFactoryTests;
import com.archimatetool.editor.ui.textrender.AllTextRenderTests;

@SuppressWarnings("nls")
public class AllUITests {

    public static junit.framework.Test suite() {
        TestSuite suite = new TestSuite("com.archimatetool.editor.ui");

        // ui
        suite.addTest(ArchiLabelProviderTests.suite());
        suite.addTest(ColorFactoryTests.suite());
        suite.addTest(FigureImagePreviewFactoryTests.suite());
        suite.addTest(FontFactoryTests.suite());
        
        // factory
        suite.addTest(AllUIFactoryTests.suite());
        
        // textrender
        suite.addTest(AllTextRenderTests.suite());

        return suite;
    }

}