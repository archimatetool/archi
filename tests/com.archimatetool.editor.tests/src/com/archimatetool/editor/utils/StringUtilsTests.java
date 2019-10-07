/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;


/**
 * StringUtilsTests
 */
@SuppressWarnings("nls")
public class StringUtilsTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(StringUtilsTests.class);
    }
    
    @Test
    public void safeStringNull() {
        String string = null;
        String result = StringUtils.safeString(string);
        assertEquals("", result);
    }

    @Test
    public void safeStringNotNull() {
        String string = "Hello World";
        String result = StringUtils.safeString(string);
        assertEquals(string, result);
    }

    // ---------------------------------------------------------------------------------------------

    @Test
    public void isSetNull() {
        String string = null;
        boolean result = StringUtils.isSet(string);
        assertFalse(result);
    }

    @Test
    public void isSetEmptyString() {
        String string = "";
        boolean result = StringUtils.isSet(string);
        assertFalse(result);
    }

    @Test
    public void isSetString() {
        String string = "Hello";
        boolean result = StringUtils.isSet(string);
        assertTrue(result);
    }

    // ---------------------------------------------------------------------------------------------
    
    @Test
    public void isSetAfterTrim1() {
        String s = "   ";
        boolean result = StringUtils.isSetAfterTrim(s);
        assertFalse(result);
    }
    
    @Test
    public void isSetAfterTrim2() {
        String s = null;
        boolean result = StringUtils.isSetAfterTrim(s);
        assertFalse(result);
    }
    
    @Test
    public void isSetAfterTrim3() {
        String s = " \n\n  ";
        boolean result = StringUtils.isSetAfterTrim(s);
        assertFalse(result);
    }

    // ---------------------------------------------------------------------------------------------

    @Test
    public void getStringCharacters1() {
        String s = "*";
        String result = StringUtils.getStringCharacters(s, 5);
        assertEquals("*****", result);
    }
    
    @Test
    public void getStringCharacters2() {
        String s = "*";
        String result = StringUtils.getStringCharacters(s, 1);
        assertEquals("*", result);
    }

    @Test
    public void getStringCharacters3() {
        String s = "*";
        String result = StringUtils.getStringCharacters(s, 0);
        assertEquals("", result);
    }
    
    @Test
    public void getStringCharacters4() {
        String s = "*";
        String result = StringUtils.getStringCharacters(s, -1);
        assertEquals("", result);
    }

    // ---------------------------------------------------------------------------------------------
    
    @Test
    public void isValidID1() {
        String s = "Hello*";
        boolean result = StringUtils.isValidID(s);
        assertFalse(result);
    }
    
    @Test
    public void isValidID2() {
        String s = "-hello-world-x";
        boolean result = StringUtils.isValidID(s);
        assertFalse(result);
    }
    
    @Test
    public void isValidIDFromUUID() {
        String s = "id-" + UUID.randomUUID().toString(); //Must have non-numeric start
        boolean result = StringUtils.isValidID(s);
        assertTrue(result);
    }
    
    // ---------------------------------------------------------------------------------------------
    
    @Test
    public void testEscapeAmpersandsInText() {
        assertEquals("He&&llo", StringUtils.escapeAmpersandsInText("He&llo"));
        assertEquals("He&&&&llo", StringUtils.escapeAmpersandsInText("He&&llo"));
    }
    
    @Test
    public void testReplaceNewLineCharacters() {
        assertEquals("Hello World", StringUtils.replaceNewLineCharacters("Hello\r\nWorld", " "));
        assertEquals("Hello World", StringUtils.replaceNewLineCharacters("Hello\rWorld", " "));
        assertEquals("Hello World", StringUtils.replaceNewLineCharacters("Hello\nWorld", " "));
        
        assertEquals("HelloWorld", StringUtils.replaceNewLineCharacters("Hello\r\nWorld", ""));
        assertEquals("HelloWorld", StringUtils.replaceNewLineCharacters("Hello\rWorld", ""));
        assertEquals("HelloWorld", StringUtils.replaceNewLineCharacters("Hello\nWorld", ""));
    }

    @Test
    public void testNormaliseNewLineCharacters() {
        assertEquals("Hello World", StringUtils.normaliseNewLineCharacters("Hello\r\n\r\n\r\nWorld"));
    }

    @Test
    public void testCompareVersionNumbers() {
        assertEquals(-1, StringUtils.compareVersionNumbers("1", "2"));
        assertEquals(0, StringUtils.compareVersionNumbers("1", "1"));
        assertEquals(1, StringUtils.compareVersionNumbers("2", "1"));

        assertEquals(-1, StringUtils.compareVersionNumbers("1.1", "1.2"));
        assertEquals(0, StringUtils.compareVersionNumbers("1.1", "1.1"));
        assertEquals(1, StringUtils.compareVersionNumbers("1.2", "1.1"));
        
        assertEquals(-1, StringUtils.compareVersionNumbers("1.1.1", "1.1.2"));
        assertEquals(0, StringUtils.compareVersionNumbers("1.1.1", "1.1.1"));
        assertEquals(1, StringUtils.compareVersionNumbers("1.1.2", "1.1.1"));
        
        assertEquals(-1, StringUtils.compareVersionNumbers("2.6.1", "10.0.0"));
        assertEquals(0, StringUtils.compareVersionNumbers("10.0.0", "10.0.0"));
        assertEquals(1, StringUtils.compareVersionNumbers("10.0.0", "2.6.1"));

        assertEquals(-1, StringUtils.compareVersionNumbers("1.1.1", "10.10.10"));
        assertEquals(0, StringUtils.compareVersionNumbers("10.10.10", "10.10.10"));
        assertEquals(1, StringUtils.compareVersionNumbers("10.10.10", "1.1.1"));
    }

    @Test
    public void testVersionNumberAsInt() {
        assertEquals(0, StringUtils.versionNumberAsInt(null));
        assertEquals(0, StringUtils.versionNumberAsInt(""));
        assertEquals(1, StringUtils.versionNumberAsInt("1"));
        assertEquals(1 << 16, StringUtils.versionNumberAsInt("1.0"));
        assertEquals((1 << 16) + (1 << 8), StringUtils.versionNumberAsInt("1.1"));
        assertEquals((8 << 16) + (2 << 8) + 16, StringUtils.versionNumberAsInt("8.2.16"));
    }
}
