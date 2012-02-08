/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
     * safeString()
     */
    @Test
    public void safeStringNull() {
        String string = null;
        String result = StringUtils.safeString(string);
        assertEquals("String should be blank", "", result); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Test
    public void safeStringNotNull() {
        String string = "Hello World"; //$NON-NLS-1$
        String result = StringUtils.safeString(string);
        assertEquals("String should be same", string, result); //$NON-NLS-1$
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * isSet()
     */
    @Test
    public void isSetNull() {
        String string = null;
        boolean result = StringUtils.isSet(string);
        assertFalse("Should be false", result); //$NON-NLS-1$
    }

    @Test
    public void isSetEmptyString() {
        String string = ""; //$NON-NLS-1$
        boolean result = StringUtils.isSet(string);
        assertFalse("Should be false", result); //$NON-NLS-1$
    }

    @Test
    public void isSetString() {
        String string = "Hello"; //$NON-NLS-1$
        boolean result = StringUtils.isSet(string);
        assertTrue("Should be true", result); //$NON-NLS-1$
    }

    // ---------------------------------------------------------------------------------------------
    
    /**
     * isSetAfterTrim(String)
     */
    @Test
    public void isSetAfterTrim1() {
        String s = "   "; //$NON-NLS-1$
        boolean result = StringUtils.isSetAfterTrim(s);
        assertFalse("Should be false", result); //$NON-NLS-1$
    }
    
    @Test
    public void isSetAfterTrim2() {
        String s = null;
        boolean result = StringUtils.isSetAfterTrim(s);
        assertFalse("Should be false", result); //$NON-NLS-1$
    }
    
    @Test
    public void isSetAfterTrim3() {
        String s = " \n\n  "; //$NON-NLS-1$
        boolean result = StringUtils.isSetAfterTrim(s);
        assertFalse("Should be false", result); //$NON-NLS-1$
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * getStringCharacters(String, int)
     */
    @Test
    public void getStringCharacters1() {
        String s = "*"; //$NON-NLS-1$
        String result = StringUtils.getStringCharacters(s, 5);
        assertEquals("String wrong", "*****", result); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    @Test
    public void getStringCharacters2() {
        String s = "*"; //$NON-NLS-1$
        String result = StringUtils.getStringCharacters(s, 1);
        assertEquals("String wrong", "*", result); //$NON-NLS-1$ //$NON-NLS-2$
    }

    @Test
    public void getStringCharacters3() {
        String s = "*"; //$NON-NLS-1$
        String result = StringUtils.getStringCharacters(s, 0);
        assertEquals("String wrong", "", result); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    @Test
    public void getStringCharacters4() {
        String s = "*"; //$NON-NLS-1$
        String result = StringUtils.getStringCharacters(s, -1);
        assertEquals("String wrong", "", result); //$NON-NLS-1$ //$NON-NLS-2$
    }

    // ---------------------------------------------------------------------------------------------
    
    /**
     * isValidID(String)
     */
    @Test
    public void isValidID1() {
        String s = "Hello*"; //$NON-NLS-1$
        boolean result = StringUtils.isValidID(s);
        assertFalse("Should be false", result); //$NON-NLS-1$
    }
    
    @Test
    public void isValidID2() {
        String s = "-hello-world-x"; //$NON-NLS-1$
        boolean result = StringUtils.isValidID(s);
        assertFalse("Should be false", result); //$NON-NLS-1$
    }
    
    @Test
    public void isValidIDFromUUID() {
        String s = "id-" + UUID.randomUUID().toString();  //$NON-NLS-1$ //Must have non-numeric start
        boolean result = StringUtils.isValidID(s);
        assertTrue(s + "  Should be valid ID", result); //$NON-NLS-1$
    }
}
