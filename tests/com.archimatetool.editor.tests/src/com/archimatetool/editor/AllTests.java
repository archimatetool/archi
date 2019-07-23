/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import com.archimatetool.editor.actions.AllActionsTests;
import com.archimatetool.editor.diagram.AllDiagramTests;
import com.archimatetool.editor.model.AllModelTests;
import com.archimatetool.editor.p2.AllP2Tests;
import com.archimatetool.editor.propertysections.AllPropertySectionsTests;
import com.archimatetool.editor.ui.AllUITests;
import com.archimatetool.editor.utils.AllUtilsTests;
import com.archimatetool.editor.views.AllViewsTests;

import junit.framework.TestSuite;

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
        
        // propertysections
        suite.addTest(AllPropertySectionsTests.suite());
		
        // ui
        suite.addTest(AllUITests.suite());

        // utils
        suite.addTest(AllUtilsTests.suite());

        // views
        suite.addTest(AllViewsTests.suite());
        
        // p2
        suite.addTest(AllP2Tests.suite());

        return suite;
	}

}