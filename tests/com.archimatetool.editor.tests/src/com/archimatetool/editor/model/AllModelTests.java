/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model;

import com.archimatetool.editor.model.commands.CommandsTests;
import com.archimatetool.editor.model.compatibility.ModelCompatibilityTests;
import com.archimatetool.editor.model.compatibility.handlers.ArchiMate2To3HandlerTests;
import com.archimatetool.editor.model.compatibility.handlers.FixDefaultSizesHandlerTests;
import com.archimatetool.editor.model.compatibility.handlers.OutlineOpacityHandlerTests;
import com.archimatetool.editor.model.impl.ArchiveManagerTests;
import com.archimatetool.editor.model.impl.EditorModelManagerTests;

import junit.framework.TestSuite;

@SuppressWarnings("nls")
public class AllModelTests {

    public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("com.archimatetool.editor.model");
		
		// model
        suite.addTest(DiagramModelUtilsTests.suite());
        suite.addTest(DiagramModelUtilsNestedRelationsTests.suite());
        suite.addTest(IArchiveManagerTests.suite());
        suite.addTest(ModelCheckerTests.suite());

        // model.commands
        suite.addTest(CommandsTests.suite());

        // model.compatibility
        suite.addTest(ModelCompatibilityTests.suite());
        
        // model.compatibility.handlers
        suite.addTest(ArchiMate2To3HandlerTests.suite());
        suite.addTest(FixDefaultSizesHandlerTests.suite());
        suite.addTest(OutlineOpacityHandlerTests.suite());

        // model.impl
        suite.addTest(ArchiveManagerTests.suite());
		suite.addTest(EditorModelManagerTests.suite());
		
		return suite;
	}

}