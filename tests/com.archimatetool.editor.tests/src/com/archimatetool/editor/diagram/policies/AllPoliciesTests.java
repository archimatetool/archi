/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.policies;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.archimatetool.editor.diagram.policies.snaptogrid.ExtendedConnectionBendpointTrackerTests;

@RunWith(Suite.class)

@Suite.SuiteClasses({
    ArchimateDiagramConnectionPolicyTests.class,
    ExtendedConnectionBendpointTrackerTests.class
})

public class AllPoliciesTests {
}