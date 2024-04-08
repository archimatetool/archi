/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.csv;


import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

import com.archimatetool.csv.export.CSVExporterTests;
import com.archimatetool.csv.importer.CSVImporterTests;

@Suite
@SelectClasses({
    CSVExporterTests.class,
    CSVImporterTests.class
})
@SuiteDisplayName("All CSV Tests")
public class AllTests {
}