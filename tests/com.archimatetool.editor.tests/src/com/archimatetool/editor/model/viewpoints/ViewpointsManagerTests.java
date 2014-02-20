/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.viewpoints;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.junit.BeforeClass;
import org.junit.Test;

import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;


public class ViewpointsManagerTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ViewpointsManagerTests.class);
    }
    
    private static ViewpointsManager vpm;
    
    @BeforeClass
    public static void runOnceBeforeAllTests() {
        vpm = ViewpointsManager.INSTANCE;
    }
    
    
    @Test
    public void testGetAllViewpoints() {
        assertEquals(26, vpm.getAllViewpoints().size());
    }

    @Test
    public void testGetViewpoint() {
        assertEquals(vpm.getAllViewpoints().get(0), vpm.getViewpoint(-1));
        assertEquals(vpm.getAllViewpoints().get(0), vpm.getViewpoint(1000));
    }
    
    
    @Test
    public void testIsAllowedType_IDiagramModelComponent() {
        IArchimateDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        dm.setViewpoint(IViewpoint.ACTOR_COOPERATION_VIEWPOINT);
        
        IDiagramModelArchimateObject dmo1 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo1.setArchimateElement(IArchimateFactory.eINSTANCE.createBusinessActor());
        dm.getChildren().add(dmo1);
        
        assertTrue(vpm.isAllowedType(dmo1));
        
        IDiagramModelArchimateObject dmo2 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo2.setArchimateElement(IArchimateFactory.eINSTANCE.createNode());
        dm.getChildren().add(dmo2);
        
        assertFalse(vpm.isAllowedType(dmo2));
        
        IDiagramModelArchimateConnection conn = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        conn.setRelationship(IArchimateFactory.eINSTANCE.createAssociationRelationship());
        conn.connect(dmo1, dmo2);
        
        assertFalse(vpm.isAllowedType(conn));
        
        IDiagramModelArchimateObject dmo3 = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo3.setArchimateElement(IArchimateFactory.eINSTANCE.createBusinessRole());
        dm.getChildren().add(dmo3);
        conn.connect(dmo1, dmo3);
        
        assertTrue(vpm.isAllowedType(conn));
    }

}
