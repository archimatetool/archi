/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.help;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

import com.archimatetool.help.cheatsheets.CreateMapViewCheatSheetActionTests;

@Suite
@SelectClasses({
    CreateMapViewCheatSheetActionTests.class
})
@SuiteDisplayName("All Help Tests")
public class AllTests {
}