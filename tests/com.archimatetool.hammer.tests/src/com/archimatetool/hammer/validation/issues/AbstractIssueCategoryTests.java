/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.validation.issues;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.archimatetool.tests.TestUtils;


@SuppressWarnings("nls")
public abstract class AbstractIssueCategoryTests {
    
    @BeforeAll
    public static void runOnceBeforeAllTests() {
        // AbstractUIPlugin#createImageRegistry expects to see a non null Display.getCurrent()
        TestUtils.ensureDefaultDisplay();
    }
    
    protected AbstractIssueCategory issueCategory;
    
    @Test
    public void testGetName() {
        assertNotNull(issueCategory.getName());
        issueCategory.setName("name");
        assertEquals("name", issueCategory.getName());
    }
    
    @Test
    public void testGetIssues() {
        assertNotNull(issueCategory.getIssues());
    }
    
    @Test
    public void testGetImage() {
        assertNotNull(issueCategory.getImage());
    }
}
