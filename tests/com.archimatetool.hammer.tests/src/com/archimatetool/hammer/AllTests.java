/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

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

@RunWith(Suite.class)

@Suite.SuiteClasses({
    // validation
    ValidatorTests.class,
    // validation.checkers
    DuplicateElementCheckerTests.class,
    EmptyViewsCheckerTests.class,
    InvalidRelationsCheckerTests.class,
    JunctionsCheckerTests.class,
    NestedElementsCheckerTests.class,
    UnusedElementsCheckerTests.class,
    UnusedRelationsCheckerTests.class,
    ViewpointCheckerTests.class,
    // validation.issues
    AdviceCategoryTests.class,
    AdviceTypeTests.class,
    ErrorsCategoryTests.class,
    ErrorTypeTests.class,
    OKTypeTests.class,
    WarningsCategoryTests.class,
    WarningTypeTests.class
})

public class AllTests {
}