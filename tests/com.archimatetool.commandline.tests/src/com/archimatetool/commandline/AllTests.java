/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.commandline;


import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

import com.archimatetool.commandline.providers.CreateEmptyModelProviderTests;
import com.archimatetool.commandline.providers.LoadModelFromFileProviderTests;

@Suite
@SelectClasses({
    CreateEmptyModelProviderTests.class,
    CommandLineStateTests.class,
    LoadModelFromFileProviderTests.class
})
@SuiteDisplayName("All Command Line Tests")
public class AllTests {
}