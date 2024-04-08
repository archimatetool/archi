/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.utils;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SelectClasses({
    FileUtilsTests.class,
    HTMLUtilsTests.class,
    StringUtilsTests.class,
    ZipUtilsTests.class
})
@SuiteDisplayName("All Utils Tests")
public class AllUtilsTests {
}