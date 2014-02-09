/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import junit.framework.TestSuite;

import com.archimatetool.model.impl.ArchimateModelTests;
import com.archimatetool.model.impl.MetadataTests;
import com.archimatetool.model.util.ArchimateModelUtilsTests;
import com.archimatetool.model.util.DerivedRelationsUtilsTests;

@SuppressWarnings("nls")
public class AllTests {

    public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("com.archimatetool.model");

		// impl
		suite.addTest(ArchimateModelTests.suite());
		suite.addTest(MetadataTests.suite());
		
        // util
        suite.addTest(ArchimateModelUtilsTests.suite());
        suite.addTest(DerivedRelationsUtilsTests.suite());

        return suite;
	}

}