/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
    com.archimatetool.canvas.AllTests.class,
    com.archimatetool.commandline.AllTests.class,
    com.archimatetool.csv.AllTests.class,
    com.archimatetool.editor.AllTests.class,
    com.archimatetool.export.svg.AllTests.class,
    com.archimatetool.hammer.AllTests.class,
    com.archimatetool.help.AllTests.class,
    com.archimatetool.jasperreports.AllTests.class,
    com.archimatetool.jdom.AllTests.class,
    com.archimatetool.model.AllTests.class,
    com.archimatetool.modelimporter.AllTests.class,
    com.archimatetool.reports.AllTests.class,
    com.archimatetool.zest.AllTests.class,
    org.opengroup.archimate.xmlexchange.AllTests.class
})

public class AllTests {
}