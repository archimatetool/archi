/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import junit.framework.TestSuite;

@SuppressWarnings("nls")
public class AllCommandsTests {

    public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("com.archimatetool.editor.diagram.commands");
		
        // commands
        suite.addTest(CreateDiagramArchimateConnectionWithDialogCommandTests.suite());

        return suite;
	}

}