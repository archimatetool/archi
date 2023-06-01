/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.csv;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.archimatetool.csv.export.CSVExporterTests;
import com.archimatetool.csv.importer.CSVImporterTests;

@RunWith(Suite.class)

@Suite.SuiteClasses({
    CSVExporterTests.class,
    CSVImporterTests.class
})

public class AllTests {
}