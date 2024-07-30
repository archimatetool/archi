/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.archimatetool.model.IAccessRelationship;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IFolder;


@SuppressWarnings("nls")
public class DiagramModelArchimateConnectionTests extends DiagramModelConnectionTests {
    
    private IArchimateRelationship relationship;
    private IDiagramModelArchimateObject source, target;
    private IDiagramModelArchimateConnection connection;
    
    
    @BeforeEach
    public void runBeforeEachTest() {
        source = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        source.setArchimateElement(IArchimateFactory.eINSTANCE.createBusinessActor());
        target = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        target.setArchimateElement(IArchimateFactory.eINSTANCE.createBusinessRole());

        relationship = IArchimateFactory.eINSTANCE.createRealizationRelationship();
        connection = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        connection.setArchimateRelationship(relationship);
    }
    
    
    @Override
    @Test
    public void testGetName() {
        super.testGetName();
        
        // Set relationship name directly
        relationship.setName("another");
        assertEquals("another", connection.getName());
    }

    @Test
    public void testConnect_WrongTypes() {
        assertThrows(IllegalArgumentException.class, () -> {
            connection.connect(IArchimateFactory.eINSTANCE.createDiagramModelGroup(), IArchimateFactory.eINSTANCE.createDiagramModelGroup());
        });
    }

    @Override
    @Test
    public void testConnect() {
        super.testConnect();
        
        assertSame(relationship, connection.getArchimateRelationship());
        assertSame(relationship.getSource(), connection.getArchimateRelationship().getSource());
        assertSame(relationship.getTarget(), connection.getArchimateRelationship().getTarget());
    }

    @Override
    @Test
    public void testReconnect() {
        super.testReconnect();
        
        assertSame(relationship, connection.getArchimateRelationship());
        assertSame(relationship.getSource(), connection.getArchimateRelationship().getSource());
        assertSame(relationship.getTarget(), connection.getArchimateRelationship().getTarget());
    }
    
    @Test
    public void testGetArchimateRelationship() {
        assertSame(relationship, connection.getArchimateRelationship());
    }
    
    @Test
    public void testSetArchimateRelationshipCanBeNull() {
        connection.setArchimateRelationship(null);
        assertNull(connection.getArchimateRelationship());
    }

    @Test
    public void testGetArchimateConcept() {
        assertSame(relationship, connection.getArchimateConcept());
    }
    
    @Test
    public void testSetArchimateConcept() {
        IAccessRelationship r = IArchimateFactory.eINSTANCE.createAccessRelationship();
        connection.setArchimateConcept(r);
        assertSame(r, connection.getArchimateConcept());
        assertSame(r, connection.getArchimateRelationship());
    }
    
    @Test
    public void testSetArchimateConceptCanBeNull() {
        connection.setArchimateConcept(null);
        assertNull(connection.getArchimateConcept());
        assertNull(connection.getArchimateRelationship());
    }
    
    @Test
    public void testSetArchimateConceptThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            connection.setArchimateConcept(IArchimateFactory.eINSTANCE.createBusinessActor());
        });
    }

    @Test
    public void testAddArchimateRelationshipToModel_AlreadyHasParent() {
        IFolder parent = IArchimateFactory.eINSTANCE.createFolder();
        parent.getElements().add(connection.getArchimateRelationship());
        
        assertThrows(IllegalArgumentException.class, () -> {
            connection.addArchimateConceptToModel(null);
        });
    }
    
    @Test
    public void testAdd_Remove_ArchimateRelationshipToModel() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        IArchimateDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        model.getDefaultFolderForObject(dm).getElements().add(dm);
        dm.getChildren().add(source);
        dm.getChildren().add(target);
        
        connection.connect(source, target);
        
        // Passing null uses a default folder in the model
        IFolder expectedFolder = model.getDefaultFolderForObject(connection.getArchimateRelationship());
        connection.addArchimateConceptToModel(null);
        assertSame(expectedFolder, connection.getArchimateRelationship().eContainer());
        
        connection.removeArchimateConceptFromModel();
        assertNull(connection.getArchimateRelationship().eContainer());
        
        expectedFolder = IArchimateFactory.eINSTANCE.createFolder();
        connection.addArchimateConceptToModel(expectedFolder);
        assertSame(expectedFolder, connection.getArchimateRelationship().eContainer());
    }
    
    @Override
    @Test
    public void testGetCopy() {
        super.testGetCopy();
        
        connection.setName("name");
        
        IDiagramModelArchimateConnection copy = (IDiagramModelArchimateConnection)connection.getCopy();
        assertNotSame(copy, connection);
        assertEquals("name", connection.getName());
        
        assertNotNull(copy.getArchimateRelationship());
        assertNotSame(copy.getArchimateRelationship(), connection.getArchimateRelationship());
        assertNull(copy.getArchimateRelationship().getSource());
        assertNull(copy.getArchimateRelationship().getTarget());
    }

}
