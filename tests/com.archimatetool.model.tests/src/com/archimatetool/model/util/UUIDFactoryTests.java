/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IBusinessActor;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelGroup;

import junit.framework.JUnit4TestAdapter;


public class UUIDFactoryTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(UUIDFactoryTests.class);
    }
    
    
    @Test
    public void createID_Concept() {
        String id = UUIDFactory.createID(IArchimateFactory.eINSTANCE.createBusinessActor());
        assertEquals(36, id.length());
    }
    
    @Test
    public void createID_Folder() {
        String id = UUIDFactory.createID(IArchimateFactory.eINSTANCE.createFolder());
        assertEquals(36, id.length());
    }

    @Test
    public void createID_Model() {
        String id = UUIDFactory.createID(IArchimateFactory.eINSTANCE.createArchimateModel());
        assertEquals(36, id.length());
    }
    
    @Test
    public void generateNewID() {
        IBusinessActor concept = IArchimateFactory.eINSTANCE.createBusinessActor();
        String id = concept.getId();
        UUIDFactory.generateNewID(concept);
        assertNotEquals(id, concept.getId());
    }
    
    @Test
    public void generateNewIDs() {
        IDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        IDiagramModelGroup group1 = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        dm.getChildren().add(group1);
        IDiagramModelGroup group2 = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        group1.getChildren().add(group2);
        
        IDiagramModelConnection connection = IArchimateFactory.eINSTANCE.createDiagramModelConnection();
        connection.connect(group1, group2);
        
        String id1 = dm.getId();
        String id2 = group1.getId();
        String id3 = group2.getId();
        String id4 = connection.getId();
        
        UUIDFactory.generateNewIDs(dm);
        
        assertNotEquals(id1, dm.getId());
        assertNotEquals(id2, group1.getId());
        assertNotEquals(id3, group2.getId());
        assertNotEquals(id4, connection.getId());
    }
    
} 
