/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.viewpoints;

import static org.junit.Assert.*;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.junit.Test;

import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.util.ArchimateModelUtils;

import junit.framework.JUnit4TestAdapter;


@SuppressWarnings("nls")
public class ViewpointManagerTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ViewpointManagerTests.class);
    }
    
    @Test
    public void testExists() {
        assertNotNull(ViewpointManager.INSTANCE);
    }
    
    @Test
    public void testNoneViewpoint() {
        assertNotNull(ViewpointManager.NONE_VIEWPOINT);
        
        for(EClass eClass : ArchimateModelUtils.getAllArchimateClasses()) {
            assertTrue(ViewpointManager.NONE_VIEWPOINT.isAllowedConcept(eClass));
        }
        
        for(EClass eClass : ArchimateModelUtils.getRelationsClasses()) {
            assertTrue(ViewpointManager.NONE_VIEWPOINT.isAllowedConcept(eClass));
        }
        
        assertTrue(ViewpointManager.NONE_VIEWPOINT.isAllowedConcept(IArchimatePackage.eINSTANCE.getJunction()));
    }
    
    @Test
    public void testGetAllViewpoints() {
        List<IViewpoint> list = ViewpointManager.INSTANCE.getAllViewpoints();
        
        // None is first in the list
        assertEquals(ViewpointManager.NONE_VIEWPOINT, list.get(0));
    }
    
    @Test
    public void testGetViewpoint_ReturnsNoneViewpoint() {
        IViewpoint vp = ViewpointManager.INSTANCE.getViewpoint(null);
        assertSame(ViewpointManager.NONE_VIEWPOINT, vp);
        
        vp = ViewpointManager.INSTANCE.getViewpoint("");
        assertSame(ViewpointManager.NONE_VIEWPOINT, vp);
        
        vp = ViewpointManager.INSTANCE.getViewpoint("some_bogus");
        assertSame(ViewpointManager.NONE_VIEWPOINT, vp);
    }

    @Test
    public void testGetViewpoint_ReturnsViewpoint() {
        IViewpoint vp = ViewpointManager.INSTANCE.getViewpoint("organization");
        
        assertNotNull(vp);
        assertEquals("organization", vp.getID());
        assertEquals("Organization", vp.getName());
    }

    @Test
    public void testIsAllowedDiagramModelComponent() {
        IArchimateDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        dm.setViewpoint("organization");
        
        IDiagramModelArchimateObject dmo1 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo1.setArchimateElement(IArchimateFactory.eINSTANCE.createBusinessActor());
        dm.getChildren().add(dmo1);
        
        assertTrue(ViewpointManager.INSTANCE.isAllowedDiagramModelComponent(dmo1));
        
        IDiagramModelArchimateObject dmo2 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo2.setArchimateElement(IArchimateFactory.eINSTANCE.createNode());
        dm.getChildren().add(dmo2);
        
        assertFalse(ViewpointManager.INSTANCE.isAllowedDiagramModelComponent(dmo2));
        
        IDiagramModelArchimateConnection conn = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        conn.setArchimateRelationship(IArchimateFactory.eINSTANCE.createAssociationRelationship());
        conn.connect(dmo1, dmo2);
        
        assertFalse(ViewpointManager.INSTANCE.isAllowedDiagramModelComponent(conn));
        
        IDiagramModelArchimateObject dmo3 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo3.setArchimateElement(IArchimateFactory.eINSTANCE.createBusinessRole());
        dm.getChildren().add(dmo3);
        conn.connect(dmo1, dmo3);
        
        assertTrue(ViewpointManager.INSTANCE.isAllowedDiagramModelComponent(conn));
    }
    
    @Test
    public void testIsAllowedConceptForDiagramModel() {
        IArchimateDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        dm.setViewpoint("organization");
        
        assertTrue(ViewpointManager.INSTANCE.isAllowedConceptForDiagramModel(dm, IArchimatePackage.eINSTANCE.getBusinessActor()));
        assertFalse(ViewpointManager.INSTANCE.isAllowedConceptForDiagramModel(dm, IArchimatePackage.eINSTANCE.getNode()));
    }
} 
