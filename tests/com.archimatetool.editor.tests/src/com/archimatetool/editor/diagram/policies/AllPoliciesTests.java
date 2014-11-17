/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.policies;

import junit.framework.TestSuite;

import com.archimatetool.editor.diagram.policies.snaptogrid.ExtendedConnectionBendpointTrackerTests;

@SuppressWarnings("nls")
public class AllPoliciesTests {

    public static junit.framework.Test suite() {
		TestSuite suite = new TestSuite("com.archimatetool.editor.diagram.policies");
		
        // policies
        suite.addTest(ArchimateDiagramConnectionPolicyTests.suite());

        // policies.snaptogrid
        suite.addTest(ExtendedConnectionBendpointTrackerTests.suite());
        
        return suite;
	}

}