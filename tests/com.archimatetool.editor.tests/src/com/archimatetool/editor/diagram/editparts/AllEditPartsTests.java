/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts;

import junit.framework.TestSuite;

@SuppressWarnings("nls")
public class AllEditPartsTests {

    public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("com.archimatetool.editor.diagram.editparts");
		
        // editparts
        suite.addTest(ArchimateDiagramEditPartFactoryTests.suite());

        return suite;
	}

}