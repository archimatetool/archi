/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model;

import junit.framework.TestSuite;

import com.archimatetool.editor.model.commands.CommandsTests;
import com.archimatetool.editor.model.impl.ArchiveManagerTests;
import com.archimatetool.editor.model.impl.ByteArrayStorageTests;
import com.archimatetool.editor.model.impl.EditorModelManagerTests;

@SuppressWarnings("nls")
public class AllModelTests {

    public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("com.archimatetool.editor.model");
		
		// model
        suite.addTest(DiagramModelUtilsTests.suite());
        suite.addTest(IArchiveManagerTests.suite());

        // model.commands
        suite.addTest(CommandsTests.suite());

        // model.impl
        suite.addTest(ArchiveManagerTests.suite());
        suite.addTest(ByteArrayStorageTests.suite());
		suite.addTest(EditorModelManagerTests.suite());
		
        return suite;
	}

}