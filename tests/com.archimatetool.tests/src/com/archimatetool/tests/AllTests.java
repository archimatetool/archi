/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.tests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SelectClasses(names={
    "com.archimatetool.canvas.AllTests",
    "com.archimatetool.commandline.AllTests",
    "com.archimatetool.csv.AllTests",
    "com.archimatetool.editor.AllTests",
    "com.archimatetool.export.svg.AllTests",
    "com.archimatetool.hammer.AllTests",
    "com.archimatetool.help.AllTests",
    "com.archimatetool.jasperreports.AllTests",
    "com.archimatetool.jdom.AllTests",
    "com.archimatetool.model.AllTests",
    "com.archimatetool.modelimporter.AllTests",
    "com.archimatetool.reports.AllTests",
    "com.archimatetool.zest.AllTests",
    "org.opengroup.archimate.xmlexchange.AllTests"
})
@SuiteDisplayName("All Archi Tests")
public class AllTests {
}