/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer;


import com.archimatetool.hammer.validation.ValidatorTests;
import com.archimatetool.hammer.validation.checkers.DuplicateElementCheckerTests;
import com.archimatetool.hammer.validation.checkers.EmptyViewsCheckerTests;
import com.archimatetool.hammer.validation.checkers.InvalidRelationsCheckerTests;
import com.archimatetool.hammer.validation.checkers.JunctionsCheckerTests;
import com.archimatetool.hammer.validation.checkers.NestedElementsCheckerTests;
import com.archimatetool.hammer.validation.checkers.UnusedElementsCheckerTests;
import com.archimatetool.hammer.validation.checkers.UnusedRelationsCheckerTests;
import com.archimatetool.hammer.validation.checkers.ViewpointCheckerTests;
import com.archimatetool.hammer.validation.issues.AdviceCategoryTests;
import com.archimatetool.hammer.validation.issues.AdviceTypeTests;
import com.archimatetool.hammer.validation.issues.ErrorTypeTests;
import com.archimatetool.hammer.validation.issues.ErrorsCategoryTests;
import com.archimatetool.hammer.validation.issues.OKTypeTests;
import com.archimatetool.hammer.validation.issues.WarningTypeTests;
import com.archimatetool.hammer.validation.issues.WarningsCategoryTests;

import junit.framework.TestSuite;


@SuppressWarnings("nls")
public class AllTests {

    public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("com.archimatetool.hammer");

		// validation
		suite.addTest(ValidatorTests.suite());
		
        // validation.checkers
		suite.addTest(DuplicateElementCheckerTests.suite());
        suite.addTest(EmptyViewsCheckerTests.suite());
        suite.addTest(InvalidRelationsCheckerTests.suite());
        suite.addTest(JunctionsCheckerTests.suite());
        suite.addTest(NestedElementsCheckerTests.suite());
        suite.addTest(UnusedElementsCheckerTests.suite());
        suite.addTest(UnusedRelationsCheckerTests.suite());
        suite.addTest(ViewpointCheckerTests.suite());
		
		// validation.issues
        suite.addTest(AdviceCategoryTests.suite());
        suite.addTest(AdviceTypeTests.suite());
        suite.addTest(ErrorsCategoryTests.suite());
        suite.addTest(ErrorTypeTests.suite());
        suite.addTest(OKTypeTests.suite());
		suite.addTest(WarningsCategoryTests.suite());
        suite.addTest(WarningTypeTests.suite());
		
        return suite;
	}

}