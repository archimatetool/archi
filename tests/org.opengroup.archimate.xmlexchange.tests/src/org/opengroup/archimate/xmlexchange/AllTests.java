/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package org.opengroup.archimate.xmlexchange;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
    XMLExchangeUtilsTests.class,
    XMLModelExporterTests.class,
    XMLModelImporterTests.class,
    XMLValidatorTests.class
})

public class AllTests {
}