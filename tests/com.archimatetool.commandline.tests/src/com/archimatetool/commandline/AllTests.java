/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.commandline;


import com.archimatetool.commandline.providers.CreateEmptyModelProviderTests;
import com.archimatetool.commandline.providers.LoadModelFromFileProviderTests;

import junit.framework.TestSuite;

@SuppressWarnings("nls")
public class AllTests {

    public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("com.archimatetool.commandline");

        suite.addTest(CreateEmptyModelProviderTests.suite());
		suite.addTest(CommandLineStateTests.suite());
        suite.addTest(LoadModelFromFileProviderTests.suite());
		
        return suite;
	}

}