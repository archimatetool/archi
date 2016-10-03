/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.validation.issues;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.archimatetool.help.hints.IHelpHintProvider;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;

import junit.framework.JUnit4TestAdapter;


@SuppressWarnings("nls")
public abstract class AbstractIssueTypeTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(AbstractIssueTypeTests.class);
    }
    
    protected AbstractIssueType issueType;
    
    @Test
    public void testGetName() {
        assertNotNull(issueType.getName());
        issueType.setName("aname");
        assertEquals("aname", issueType.getName());
    }
    
    @Test
    public void testGetDescription() {
        assertNotNull(issueType.getDescription());
        issueType.setDescription("adesc");
        assertEquals("adesc", issueType.getDescription());
    }
    
    @Test
    public void testGetExplanation() {
        assertNotNull(issueType.getExplanation());
        issueType.setExplanation("exp");
        assertEquals("exp", issueType.getExplanation());
    }

    @Test
    public void testGetObject() {
        assertNull(issueType.getObject());
        Object anObject = new Object();
        issueType.setObject(anObject);
        assertSame(anObject, issueType.getObject());
    }

    @Test
    public void testGetImage() {
        assertNotNull(issueType.getImage());
    }
    
    @Test
    public void testGetHelpHintContent() {
        assertEquals(issueType.getExplanation(), issueType.getHelpHintContent());
    }
    
    @Test
    public void testGetHelpHintTitle() {
        assertEquals(issueType.getName(), issueType.getHelpHintTitle());
    }
    
    @Test
    public void testGetAdapter_IHelpHintProvider_Same() {
        assertSame(issueType, issueType.getAdapter(IHelpHintProvider.class));
    }
    
    @Test
    public void testGetAdapter_ObjectIsNull() {
        assertNull(issueType.getAdapter(Object.class));
    }

    @Test
    public void testGetAdapter_ObjectIsInstance() {
        IArchimateElement element = IArchimateFactory.eINSTANCE.createBusinessActor();
        issueType.setObject(element);
        assertSame(element, issueType.getAdapter(element.getClass()));
    }
}
