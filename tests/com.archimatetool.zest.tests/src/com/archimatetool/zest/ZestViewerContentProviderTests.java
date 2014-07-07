/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.zest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import junit.framework.JUnit4TestAdapter;

import org.junit.BeforeClass;
import org.junit.Test;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IRelationship;
import com.archimatetool.testingtools.ArchimateTestModel;
import com.archimatetool.tests.TestData;


@SuppressWarnings("nls")
public class ZestViewerContentProviderTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ZestViewerContentProviderTests.class);
    }
    
    private static ArchimateTestModel tm;
    private static ZestViewerContentProvider provider;

    @BeforeClass
    public static void runOnceBeforeAllTests() throws IOException {
        // Load ArchiMate model
        tm = new ArchimateTestModel(TestData.TEST_MODEL_FILE_ARCHISURANCE);
        tm.loadModel();
        provider = new ZestViewerContentProvider();
    }
    
    @Test
    public void testSetDepth() {
        assertEquals(0, provider.getDepth());
        provider.setDepth(2);
        assertEquals(2, provider.getDepth());
    }
    
    @Test
    public void testGetElements_Element() {
        IArchimateElement inputElement = (IArchimateElement)tm.getObjectByID("521");
        Object[] elements = provider.getElements(inputElement);
        assertEquals(17, elements.length);
        for(Object object : elements) {
            assertTrue(object instanceof IRelationship);
        }
    }

    @Test
    public void testGetElements_Relationship() {
        IRelationship inputElement = (IRelationship)tm.getObjectByID("460");
        Object[] elements = provider.getElements(inputElement);
        assertEquals(inputElement, elements[0]);
    }

    @Test
    public void testGetSource_Element() {
        IArchimateElement inputElement = (IArchimateElement)tm.getObjectByID("521");
        Object source = provider.getSource(inputElement);
        assertNull(source);
    }
    
    @Test
    public void testGetSource_Relationship() {
        IRelationship inputElement = (IRelationship)tm.getObjectByID("460");
        IArchimateElement expected = (IArchimateElement)tm.getObjectByID("409");
        Object source = provider.getSource(inputElement);
        assertEquals(expected, source);
    }

    @Test
    public void testGetDestination_Element() {
        IArchimateElement inputElement = (IArchimateElement)tm.getObjectByID("521");
        Object destination = provider.getDestination(inputElement);
        assertNull(destination);
    }
    
    @Test
    public void testGetDestination_Relationship() {
        IRelationship inputElement = (IRelationship)tm.getObjectByID("460");
        IArchimateElement expected = (IArchimateElement)tm.getObjectByID("289");
        Object destination = provider.getDestination(inputElement);
        assertEquals(expected, destination);
    }
    
}
