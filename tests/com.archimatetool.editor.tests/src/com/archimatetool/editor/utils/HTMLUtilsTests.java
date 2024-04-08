/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * JUnit tests
 *
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class HTMLUtilsTests {

    // ---------------------------------------------------------------------------------------------
    // BEFORE AND AFTER METHODS GO HERE 
    // ---------------------------------------------------------------------------------------------
    
    @BeforeAll
    public static void runOnceBeforeAllTests() {
    }
    
    @AfterAll
    public static void runOnceAfterAllTests() {
    }
    
    @BeforeEach
    public void runBeforeEachTest() {
    }
    
    @AfterEach
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
        String string = "This is a test String";
        String result = HTMLUtils.stripTags(string);
        assertEquals(string, result, "String should be the same");
        
        String string2 = "<bold>This is a <i>test String</bold>";
        result = HTMLUtils.stripTags(string2);
        assertEquals(string, result, "String should be the same");
    }
    
}
