/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.archimatetool.jasperreports.data.ArchimateModelDataSourceTests;
import com.archimatetool.jasperreports.data.ElementsDataSourceTests;
import com.archimatetool.jasperreports.data.FieldDataFactoryTests;
import com.archimatetool.jasperreports.data.PropertiesModelDataSourceTests;
import com.archimatetool.jasperreports.data.ViewChildrenDataSourceTests;
import com.archimatetool.jasperreports.data.ViewModelDataSourceTests;

@RunWith(Suite.class)

@Suite.SuiteClasses({
    JasperReportsExporterTests.class,
    ArchimateModelDataSourceTests.class,
    ElementsDataSourceTests.class,
    FieldDataFactoryTests.class,
    PropertiesModelDataSourceTests.class,
    ViewChildrenDataSourceTests.class,
    ViewModelDataSourceTests.class,
})

public class AllTests {
}