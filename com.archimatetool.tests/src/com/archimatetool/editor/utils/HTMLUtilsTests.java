/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.utils;

import static org.junit.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.archimatetool.editor.utils.HTMLUtils;


/**
 * JUnit tests for JUnit 4
 *
 * @author Phillip Beauvoir
 */
public class HTMLUtilsTests {

    /**
     * This is required in order to run JUnit 4 tests with the old JUnit runner
     * 
     * @return
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(HTMLUtilsTests.class);
    }
    
    // ---------------------------------------------------------------------------------------------
    // BEFORE AND AFTER METHODS GO HERE 
    // ---------------------------------------------------------------------------------------------
    
    @BeforeClass
    public static void runOnceBeforeAllTests() {
    }
    
    @AfterClass
    public static void runOnceAfterAllTests() {
    }
    
    @Before
    public void runBeforeEachTest() {
    }
    
    @After
    public void runAfterEachTest() {
    }

    // ---------------------------------------------------------------------------------------------
    // TESTS GO HERE 
    // ---------------------------------------------------------------------------------------------

    /**
     * stripTags()
     */
    @Test
    public void stripTags_String() {
        String string = "This is a test String"; //$NON-NLS-1$
        String result = HTMLUtils.stripTags(string);
        assertEquals("String should be the same", string, result); //$NON-NLS-1$
        
        String string2 = "<bold>This is a <i>test String</bold>"; //$NON-NLS-1$
        result = HTMLUtils.stripTags(string2);
        assertEquals("String should be the same", string, result); //$NON-NLS-1$
    }
    
}
