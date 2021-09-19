/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.sketch;

import junit.framework.TestSuite;

import com.archimatetool.editor.diagram.sketch.editparts.SketchEditPartFactoryTests;

@SuppressWarnings("nls")
public class AllSketchTests {

    public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("com.archimatetool.editor.diagram.sketch");
		
		suite.addTest(SketchEditPartFactoryTests.suite());
		suite.addTest(SketchModelFactoryTests.suite());

        return suite;
	}

}