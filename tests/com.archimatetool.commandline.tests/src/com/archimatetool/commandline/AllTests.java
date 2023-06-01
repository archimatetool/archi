/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.commandline;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.archimatetool.commandline.providers.CreateEmptyModelProviderTests;
import com.archimatetool.commandline.providers.LoadModelFromFileProviderTests;

@RunWith(Suite.class)

@Suite.SuiteClasses({
    CreateEmptyModelProviderTests.class,
    CommandLineStateTests.class,
    LoadModelFromFileProviderTests.class
})

public class AllTests {
}