/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IRelationship;


@SuppressWarnings("nls")
public class DiagramModelArchimateConnectionTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DiagramModelArchimateConnectionTests.class);
    }
    
    private IRelationship relationship;
    private IDiagramModelArchimateObject source, target;
    private IDiagramModelArchimateConnection connection;
    
    // ---------------------------------------------------------------------------------------------
    // BEFORE AND AFTER METHODS GO HERE 
    // ---------------------------------------------------------------------------------------------
    
    @Before
    public void runBeforeEachOfTheseTests() {
        source = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        source.setArchimateElement(IArchimateFactory.eINSTANCE.createBusinessActor());
        target = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        target.setArchimateElement(IArchimateFactory.eINSTANCE.createBusinessRole());

        relationship = IArchimateFactory.eINSTANCE.createRealisationRelationship();
        connection = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        connection.setRelationship(relationship);
    }
    
    // ---------------------------------------------------------------------------------------------
    // Tests
    // ---------------------------------------------------------------------------------------------
    
    @Test
    public void testGetName() {
        CommonTests.testGetName(connection);
        
        // Set relationship name directly
        relationship.setName("another");
        assertEquals("another", connection.getName());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConnect_WrongTypes() {
        connection.connect(IArchimateFactory.eINSTANCE.createDiagramModelGroup(), IArchimateFactory.eINSTANCE.createDiagramModelGroup());
    }

    @Test
    public void testConnect() {
        connection.connect(source, target);
        assertSame(source, connection.getSource());
        assertSame(target, connection.getTarget());
    }

    @Test
    public void testReconnect() {
        connection.connect(source, target);
        connection.disconnect();
        connection.reconnect();
        
        assertSame(relationship, connection.getRelationship());
        assertSame(relationship.getSource(), connection.getRelationship().getSource());
        assertSame(relationship.getTarget(), connection.getRelationship().getTarget());
    }
    
    @Test
    public void testGetRelationship() {
        assertSame(relationship, connection.getRelationship());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testAddRelationshipToModel_AlreadyHasParent() {
        IFolder parent = IArchimateFactory.eINSTANCE.createFolder();
        parent.getElements().add(connection.getRelationship());
        
        connection.addRelationshipToModel(null);
    }
    
    @Test
    public void testAdd_Remove_RelationshipToModel() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        IArchimateDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        model.getDefaultFolderForElement(dm).getElements().add(dm);
        dm.getChildren().add(source);
        dm.getChildren().add(target);
        
        connection.connect(source, target);
        
        // Passing null uses a default folder in the model
        IFolder expectedFolder = model.getDefaultFolderForElement(connection.getRelationship());
        connection.addRelationshipToModel(null);
        assertSame(expectedFolder, connection.getRelationship().eContainer());
        
        connection.removeRelationshipFromModel();
        assertNull(connection.getRelationship().eContainer());
        
        expectedFolder = IArchimateFactory.eINSTANCE.createFolder();
        connection.addRelationshipToModel(expectedFolder);
        assertSame(expectedFolder, connection.getRelationship().eContainer());
    }
    
    @Test
    public void testGetCopy() {
        connection.setName("name");
        
        IDiagramModelArchimateConnection copy = (IDiagramModelArchimateConnection)connection.getCopy();
        assertNotSame(copy, connection);
        assertEquals("name", connection.getName());
        
        assertNotNull(copy.getRelationship());
        assertNotSame(copy.getRelationship(), connection.getRelationship());
    }

}
