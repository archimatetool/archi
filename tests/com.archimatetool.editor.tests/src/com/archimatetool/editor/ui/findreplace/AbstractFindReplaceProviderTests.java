/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.findreplace;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;


@SuppressWarnings("nls")
public abstract class AbstractFindReplaceProviderTests {
    
    protected abstract AbstractFindReplaceProvider getProvider();
    
    protected AbstractFindReplaceProvider provider;
    
    @Before
    public void runOnceBeforeAbstractEachTest() {
        provider = getProvider();
    }
    
    @Test
    public void testCanFind() {
        assertFalse(provider.canFind(""));
        assertFalse(provider.canFind(null));
        assertTrue(provider.canFind("Hello"));
    }
    
    @Test
    public void testCanFindAll() {
        assertFalse(provider.canFindAll(""));
        assertFalse(provider.canFindAll(null));
        assertTrue(provider.canFindAll("Hello"));
    }

    @Test
    public void testCanReplace() {
        assertFalse(provider.canReplace("", null));
        assertFalse(provider.canReplace(null, ""));
        assertFalse(provider.canReplace("Hello", null));
        assertTrue(provider.canReplace("Hello", ""));
        assertTrue(provider.canReplace("Hello", "There"));
    }
    
    @Test
    public void testCanReplaceAll() {
        assertFalse(provider.canReplaceAll("", null));
        assertFalse(provider.canReplaceAll(null, ""));
        assertFalse(provider.canReplaceAll("Hello", null));
        assertTrue(provider.canReplaceAll("Hello", ""));
        assertTrue(provider.canReplaceAll("Hello", "There"));
    }

    @Test
    public void testIsAll() {
        provider.setParameter(IFindReplaceProvider.PARAM_ALL, false);
        assertFalse(provider.isAll());
        provider.setParameter(IFindReplaceProvider.PARAM_ALL, true);
        assertTrue(provider.isAll());
    }

    @Test
    public void testIsCaseSensitive() {
        provider.setParameter(IFindReplaceProvider.PARAM_CASE_SENSITIVE, false);
        assertFalse(provider.isCaseSensitive());
        provider.setParameter(IFindReplaceProvider.PARAM_CASE_SENSITIVE, true);
        assertTrue(provider.isCaseSensitive());
    }

    @Test
    public void testIsForward() {
        provider.setParameter(IFindReplaceProvider.PARAM_FORWARD, false);
        assertFalse(provider.isForward());
        provider.setParameter(IFindReplaceProvider.PARAM_FORWARD, true);
        assertTrue(provider.isForward());
    }
    
    @Test
    public void testGetNewName() {
        String oldName = "Hello World";

        assertEquals(oldName, provider.getNewName(oldName, "z", "zzz"));
        assertEquals("Hellz Wzrld", provider.getNewName(oldName, "o", "z"));
        assertEquals("Hellz Wzrld", provider.getNewName(oldName, "O", "z"));
        
        provider.setParameter(IFindReplaceProvider.PARAM_CASE_SENSITIVE, true);
        assertEquals(oldName, provider.getNewName(oldName, "O", "z"));
        
        // Test regex characters don't mess things up
        oldName = "Hello (World \b *. \\";
        assertEquals(oldName, provider.getNewName(oldName, "O", "z"));
        provider.setParameter(IFindReplaceProvider.PARAM_CASE_SENSITIVE, false);
        assertEquals("Hellz (Wzrld \b *. \\", provider.getNewName(oldName, "O", "z"));
        
        // Test classic use case
        assertEquals("Business Actor", provider.getNewName("Business Actor (copy)", " (copy)", ""));
    }
    
    @Test
    public void testGetSearchStringPattern() {
        String searchString = "Hello ( C \\ *. World";
        
        assertEquals("(?i:(?s).*\\Q" + searchString + "\\E.*)", provider.getSearchStringPattern(searchString));
        
        provider.setParameter(IFindReplaceProvider.PARAM_WHOLE_WORD, true);
        assertEquals("(?i:(?s).*\\b\\Q" + searchString + "\\E\\b.*)", provider.getSearchStringPattern(searchString));
        
        provider.setParameter(IFindReplaceProvider.PARAM_CASE_SENSITIVE, true);
        assertEquals("(?s).*\\b\\Q" + searchString + "\\E\\b.*", provider.getSearchStringPattern(searchString));
    }
}
