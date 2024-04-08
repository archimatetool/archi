/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.policies;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

import com.archimatetool.editor.diagram.policies.snaptogrid.ExtendedConnectionBendpointTrackerTests;

@Suite
@SelectClasses({
    ArchimateDiagramConnectionPolicyTests.class,
    ExtendedConnectionBendpointTrackerTests.class
})
@SuiteDisplayName("All Policies Tests")
public class AllPoliciesTests {
}