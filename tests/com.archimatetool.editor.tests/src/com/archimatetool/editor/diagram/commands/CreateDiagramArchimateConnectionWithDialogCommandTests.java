/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.eclipse.gef.requests.CreateConnectionRequest;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.archimatetool.editor.diagram.ArchimateDiagramModelFactory;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IAssignmentRelationship;
import com.archimatetool.model.IConnectable;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.testingtools.ArchimateTestModel;

import junit.framework.JUnit4TestAdapter;


public class CreateDiagramArchimateConnectionWithDialogCommandTests {
    
    private CreateDiagramArchimateConnectionWithDialogCommand cmd;
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(CreateDiagramArchimateConnectionWithDialogCommandTests.class);
    }
    
    @BeforeClass
    public static void runOnceBeforeAllTests() {
    }
    
    @Before
    public void runOnceBeforeEachTest() {
        CreateConnectionRequest request = new CreateConnectionRequest();
        request.setFactory(new ArchimateDiagramModelFactory(IArchimatePackage.eINSTANCE.getAssignmentRelationship()));
        cmd = new CreateDiagramArchimateConnectionWithDialogCommand(request);
    }

    @Test
    public void testCreationOfConnectionAndRelationship() {
        ArchimateTestModel tm = new ArchimateTestModel();
        IArchimateModel model = tm.createNewModel();
        
        IDiagramModelArchimateObject dmo1 = tm.createDiagramModelArchimateObjectAndAddToModel(IArchimateFactory.eINSTANCE.createBusinessActor());
        IDiagramModelArchimateObject dmo2 = tm.createDiagramModelArchimateObjectAndAddToModel(IArchimateFactory.eINSTANCE.createBusinessRole());
        
        model.getDefaultDiagramModel().getChildren().add(dmo1);
        model.getDefaultDiagramModel().getChildren().add(dmo2);
        
        cmd.setSource(dmo1);
        cmd.setTarget(dmo2);
        
        cmd.execute();
        
        IDiagramModelConnection connection = cmd.fConnection;
        assertTrue(connection instanceof IDiagramModelArchimateConnection);
        assertSame(dmo1, connection.getSource());
        assertSame(dmo2, connection.getTarget());
        
        IArchimateRelationship relationship = ((IDiagramModelArchimateConnection)connection).getArchimateRelationship();
        assertTrue(relationship instanceof IAssignmentRelationship);
        assertNotNull(relationship.eContainer());
        
        cmd.undo();
        
        assertNull(relationship.eContainer());
    }

    @Test
    public void testSwapSourceAndTargetElements() {
        IConnectable oldsource = cmd.fSource;
        IConnectable oldtarget = cmd.fTarget;
        
        cmd.swapSourceAndTargetConcepts();
        
        assertSame(oldsource, cmd.fTarget);
        assertSame(oldtarget, cmd.fSource);
    }

    @Test
    public void testGetExistingRelationshipOfType() {
        ArchimateTestModel tm = new ArchimateTestModel();
        tm.createSimpleModel();
        
        IArchimateElement source = (IArchimateElement)tm.createModelElementAndAddToModel(IArchimatePackage.eINSTANCE.getBusinessActor());
        IArchimateElement target = (IArchimateElement)tm.createModelElementAndAddToModel(IArchimatePackage.eINSTANCE.getBusinessActor());
        
        IArchimateRelationship relation1 = (IArchimateRelationship)tm.createModelElementAndAddToModel(IArchimatePackage.eINSTANCE.getAssignmentRelationship());
        IArchimateRelationship relation2 = (IArchimateRelationship)tm.createModelElementAndAddToModel(IArchimatePackage.eINSTANCE.getFlowRelationship());
        
        assertTrue(cmd.getExistingRelationshipsOfType(IArchimatePackage.eINSTANCE.getAssignmentRelationship(),
                source, target).isEmpty());
        
        relation1.setSource(source);
        relation1.setTarget(target);
        
        relation2.setSource(source);
        relation2.setTarget(target);
        
        assertEquals(relation1, cmd.getExistingRelationshipsOfType(IArchimatePackage.eINSTANCE.getAssignmentRelationship(),
                source, target).get(0));
        
        assertEquals(relation2, cmd.getExistingRelationshipsOfType(IArchimatePackage.eINSTANCE.getFlowRelationship(),
                source, target).get(0));
    }
}
