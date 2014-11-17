/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas;

import junit.framework.TestSuite;

import com.archimatetool.canvas.editparts.CanvasModelEditPartFactoryTests;
import com.archimatetool.canvas.factory.AllUIFactoryTests;

@SuppressWarnings("nls")
public class AllTests {

    public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("com.archimatetool.canvas");
		
		// editparts
		suite.addTest(CanvasModelEditPartFactoryTests.suite());
		
		// factory
		suite.addTest(AllUIFactoryTests.suite());
		
		suite.addTest(CanvasModelFactoryTests.suite());
		
        return suite;
	}

}