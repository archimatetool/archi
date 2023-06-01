/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.reports;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.archimatetool.reports.html.HTMLReportExporterTests;

@RunWith(Suite.class)

@Suite.SuiteClasses({
    HTMLReportExporterTests.class
})

public class AllTests {
}