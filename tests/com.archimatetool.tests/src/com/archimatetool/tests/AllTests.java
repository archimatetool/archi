/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.tests;

import org.junit.platform.suite.api.IncludeClassNamePatterns;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SelectPackages({
    "com.archimatetool.canvas",
    "com.archimatetool.commandline",
    "com.archimatetool.csv",
    "com.archimatetool.editor",
    "com.archimatetool.export.svg",
    "com.archimatetool.hammer",
    "com.archimatetool.help",
    "com.archimatetool.jasperreports",
    "com.archimatetool.jdom",
    "com.archimatetool.model",
    "com.archimatetool.modelimporter",
    "com.archimatetool.reports",
    "com.archimatetool.zest",
    "org.opengroup.archimate.xmlexchange"
})
@IncludeClassNamePatterns(".*AllTests")
@SuiteDisplayName("All Archi Tests")
public class AllTests {
}