/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.zest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.viewpoints.IViewpoint;
import com.archimatetool.model.viewpoints.ViewpointManager;
import com.archimatetool.testingtools.ArchimateTestModel;
import com.archimatetool.tests.TestData;


@SuppressWarnings("nls")
public class ZestViewerContentProviderTests {
    
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
    public void testSetDirection() {
        provider.setDirection(ZestViewerContentProvider.DIR_IN);
        assertEquals(ZestViewerContentProvider.DIR_IN, provider.getDirection());
        
        provider.setDirection(ZestViewerContentProvider.DIR_BOTH);
        assertEquals(ZestViewerContentProvider.DIR_BOTH, provider.getDirection());
        
        provider.setDirection(-1);
        assertEquals(ZestViewerContentProvider.DIR_BOTH, provider.getDirection());
    }

    @Test
    public void testSetViewpointFilter() {
        // Default VP
        IViewpoint defaultViewpoint = ViewpointManager.NONE_VIEWPOINT;
        assertTrue(provider.getViewpointFilter() == defaultViewpoint);
        
        provider.setViewpointFilter(ViewpointManager.INSTANCE.getAllViewpoints().get(1));
        assertSame(provider.getViewpointFilter(), ViewpointManager.INSTANCE.getAllViewpoints().get(1));
        
        // Back to default
        provider.setViewpointFilter(defaultViewpoint);
    }

    @Test
    public void testAddElementFilter() {
        // Default is "All" so empty list
        assertTrue(provider.getElementFilters().isEmpty());

        // Add some
        provider.addElementFilter(IArchimatePackage.eINSTANCE.getBusinessActor());
        provider.addElementFilter(IArchimatePackage.eINSTANCE.getBusinessFunction());
        assertEquals(2, provider.getElementFilters().size());
        assertTrue(provider.getElementFilters().contains((IArchimatePackage.eINSTANCE.getBusinessActor())));
        assertTrue(provider.getElementFilters().contains((IArchimatePackage.eINSTANCE.getBusinessFunction())));
        
        // Back to default
        provider.addElementFilter(null);
        assertTrue(provider.getElementFilters().isEmpty());
    }

    @Test
    public void testAddRelationshipFilter() {
        // Default is "All" so empty list
        assertTrue(provider.getRelationshipFilters().isEmpty());
        
        // Add some
        provider.addRelationshipFilter(IArchimatePackage.eINSTANCE.getCompositionRelationship());
        provider.addRelationshipFilter(IArchimatePackage.eINSTANCE.getAssignmentRelationship());
        assertEquals(2, provider.getRelationshipFilters().size());
        assertTrue(provider.getRelationshipFilters().contains((IArchimatePackage.eINSTANCE.getCompositionRelationship())));
        assertTrue(provider.getRelationshipFilters().contains((IArchimatePackage.eINSTANCE.getAssignmentRelationship())));
        
        // Back to default
        provider.addRelationshipFilter(null);
        assertTrue(provider.getRelationshipFilters().isEmpty());
    }

    @Test
    public void testGetElements_Element() {
        IArchimateElement inputElement = (IArchimateElement)tm.getObjectByID("521");
        Object[] elements = provider.getElements(inputElement);
        assertEquals(17, elements.length);
        for(Object object : elements) {
            assertTrue(object instanceof IArchimateRelationship);
        }
    }

    @Test
    public void testGetElements_Relationship() {
        IArchimateRelationship inputElement = (IArchimateRelationship)tm.getObjectByID("460");
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
        IArchimateRelationship inputElement = (IArchimateRelationship)tm.getObjectByID("460");
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
        IArchimateRelationship inputElement = (IArchimateRelationship)tm.getObjectByID("460");
        IArchimateElement expected = (IArchimateElement)tm.getObjectByID("289");
        Object destination = provider.getDestination(inputElement);
        assertEquals(expected, destination);
    }
    
}
