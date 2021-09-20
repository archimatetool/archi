/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.validation.issues;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;

import com.archimatetool.tests.TestUtils;

import junit.framework.JUnit4TestAdapter;


@SuppressWarnings("nls")
public abstract class AbstractIssueCategoryTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(AbstractIssueCategoryTests.class);
    }
    
    @BeforeClass
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
