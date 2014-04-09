/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import junit.framework.TestSuite;

import com.archimatetool.editor.actions.AllActionsTests;
import com.archimatetool.editor.diagram.AllDiagramTests;
import com.archimatetool.editor.model.AllModelTests;
import com.archimatetool.editor.ui.AllUITests;
import com.archimatetool.editor.utils.AllUtilsTests;
import com.archimatetool.editor.views.AllViewsTests;

@SuppressWarnings("nls")
public class AllTests {

    public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("com.archimatetool.editor");
		
		// actions
		suite.addTest(AllActionsTests.suite());
		
        // diagram
        suite.addTest(AllDiagramTests.suite());

		// model
        suite.addTest(AllModelTests.suite());
		
        // ui
        suite.addTest(AllUITests.suite());

        // utils
        suite.addTest(AllUtilsTests.suite());

        // views
        suite.addTest(AllViewsTests.suite());

        return suite;
	}

}