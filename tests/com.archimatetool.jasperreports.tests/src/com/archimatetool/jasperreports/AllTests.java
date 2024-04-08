/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports;


import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

import com.archimatetool.jasperreports.data.ArchimateModelDataSourceTests;
import com.archimatetool.jasperreports.data.ElementsDataSourceTests;
import com.archimatetool.jasperreports.data.FieldDataFactoryTests;
import com.archimatetool.jasperreports.data.PropertiesModelDataSourceTests;
import com.archimatetool.jasperreports.data.ViewChildrenDataSourceTests;
import com.archimatetool.jasperreports.data.ViewModelDataSourceTests;

@Suite
@SelectClasses({
    JasperReportsExporterTests.class,
    ArchimateModelDataSourceTests.class,
    ElementsDataSourceTests.class,
    FieldDataFactoryTests.class,
    PropertiesModelDataSourceTests.class,
    ViewChildrenDataSourceTests.class,
    ViewModelDataSourceTests.class,
})
@SuiteDisplayName("All Jasper Reports Tests")
public class AllTests {
}