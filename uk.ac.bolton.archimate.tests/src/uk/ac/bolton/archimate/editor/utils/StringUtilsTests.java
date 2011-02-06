/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.utils;

import static org.junit.Assert.*;

import java.util.UUID;

import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * JUnit tests for JUnit 4
 *
 * @author Phillip Beauvoir
 */
public class StringUtilsTests {

    /**
     * This is required in order to run JUnit 4 tests with the old JUnit runner
     * 
     * @return
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(StringUtilsTests.class);
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
        String string = "This is a test String";
        String result = StringUtils.stripTags(string);
        assertEquals("String should be the same", string, result);
        
        String string2 = "<bold>This is a <i>test String</bold>";
        result = StringUtils.stripTags(string2);
        assertEquals("String should be the same", string, result);
    }
    
    // ---------------------------------------------------------------------------------------------

    /**
     * safeString()
     */
    @Test
    public void safeStringNull() {
        String string = null;
        String result = StringUtils.safeString(string);
        assertEquals("String should be blank", "", result);
    }

    @Test
    public void safeStringNotNull() {
        String string = "Hello World";
        String result = StringUtils.safeString(string);
        assertEquals("String should be same", string, result);
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * isSet()
     */
    @Test
    public void isSetNull() {
        String string = null;
        boolean result = StringUtils.isSet(string);
        assertFalse("Should be false", result);
    }

    @Test
    public void isSetEmptyString() {
        String string = "";
        boolean result = StringUtils.isSet(string);
        assertFalse("Should be false", result);
    }

    @Test
    public void isSetString() {
        String string = "Hello";
        boolean result = StringUtils.isSet(string);
        assertTrue("Should be true", result);
    }

    // ---------------------------------------------------------------------------------------------
    
    /**
     * isSetAfterTrim(String)
     */
    @Test
    public void isSetAfterTrim1() {
        String s = "   ";
        boolean result = StringUtils.isSetAfterTrim(s);
        assertFalse("Should be false", result);
    }
    
    @Test
    public void isSetAfterTrim2() {
        String s = null;
        boolean result = StringUtils.isSetAfterTrim(s);
        assertFalse("Should be false", result);
    }
    
    @Test
    public void isSetAfterTrim3() {
        String s = " \n\n  ";
        boolean result = StringUtils.isSetAfterTrim(s);
        assertFalse("Should be false", result);
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * getStringCharacters(String, int)
     */
    @Test
    public void getStringCharacters1() {
        String s = "*";
        String result = StringUtils.getStringCharacters(s, 5);
        assertEquals("String wrong", "*****", result);
    }
    
    @Test
    public void getStringCharacters2() {
        String s = "*";
        String result = StringUtils.getStringCharacters(s, 1);
        assertEquals("String wrong", "*", result);
    }

    @Test
    public void getStringCharacters3() {
        String s = "*";
        String result = StringUtils.getStringCharacters(s, 0);
        assertEquals("String wrong", "", result);
    }
    
    @Test
    public void getStringCharacters4() {
        String s = "*";
        String result = StringUtils.getStringCharacters(s, -1);
        assertEquals("String wrong", "", result);
    }

    // ---------------------------------------------------------------------------------------------
    
    /**
     * isValidID(String)
     */
    @Test
    public void isValidID1() {
        String s = "Hello*";
        boolean result = StringUtils.isValidID(s);
        assertFalse("Should be false", result);
    }
    
    @Test
    public void isValidID2() {
        String s = "-hello-world-x";
        boolean result = StringUtils.isValidID(s);
        assertFalse("Should be false", result);
    }
    
    @Test
    public void isValidIDFromUUID() {
        String s = "id-" + UUID.randomUUID().toString(); //Must have non-numeric start
        boolean result = StringUtils.isValidID(s);
        assertTrue(s + "  Should be valid ID", result);
    }
}
