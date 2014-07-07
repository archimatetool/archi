/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports;


import junit.framework.TestSuite;

import com.archimatetool.jasperreports.data.ArchimateModelDataSourceTests;
import com.archimatetool.jasperreports.data.ElementsDataSourceTests;
import com.archimatetool.jasperreports.data.FieldDataFactoryTests;
import com.archimatetool.jasperreports.data.PropertiesModelDataSourceTests;
import com.archimatetool.jasperreports.data.ViewChildrenDataSourceTests;
import com.archimatetool.jasperreports.data.ViewModelDataSourceTests;

@SuppressWarnings("nls")
public class AllTests {

    public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("com.archimatetool.jasperreports");

		suite.addTest(JasperReportsExporterTests.suite());
		suite.addTest(ArchimateModelDataSourceTests.suite());
        suite.addTest(ElementsDataSourceTests.suite());
        suite.addTest(FieldDataFactoryTests.suite());
        suite.addTest(PropertiesModelDataSourceTests.suite());
        suite.addTest(ViewChildrenDataSourceTests.suite());
        suite.addTest(ViewModelDataSourceTests.suite());
		
        return suite;
	}

}