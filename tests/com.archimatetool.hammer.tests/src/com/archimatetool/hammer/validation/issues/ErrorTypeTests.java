/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.validation.issues;


import org.junit.Before;


@SuppressWarnings("nls")
public class ErrorTypeTests extends AbstractIssueTypeTests {
    
    @Before
    public void runOnceBeforeEachTest() {
        issueType = new ErrorType("error", "description", "explanation", null);
    }
    
}
