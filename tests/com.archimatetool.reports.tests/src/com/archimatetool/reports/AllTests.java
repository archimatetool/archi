/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.reports;


import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

import com.archimatetool.reports.html.HTMLReportExporterTests;

@Suite
@SelectClasses({
    HTMLReportExporterTests.class
})
@SuiteDisplayName("All Reports Tests")
public class AllTests {
}