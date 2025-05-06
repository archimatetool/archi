/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package org.opengroup.archimate.xmlexchange;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SelectClasses({
    XMLExchangeUtilsTests.class,
    XMLModelExporterTests.class,
    XMLModelImporterTests.class
})
@SuiteDisplayName("All XML Exchange Tests")
public class AllTests {
}