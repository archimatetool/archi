/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.junit.Test;

import com.archimatetool.editor.TestSupport;
import com.archimatetool.editor.diagram.actions.CopySnapshot.BidiHashtable;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelReference;
import com.archimatetool.model.IIdentifier;
import com.archimatetool.testingtools.ArchimateTestModel;
import com.archimatetool.tests.TestData;

import junit.framework.JUnit4TestAdapter;

@SuppressWarnings("nls")
public class CopySnapshotTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(CopySnapshotTests.class);
    }
    
    private ArchimateTestModel tm;
    private IArchimateModel model;
    private IDiagramModel sourceDiagramModel;
    private IDiagramModel targetDiagramModel;
    
    // ---------------------------------------------------------------------------------------------
    // Tests
    // ---------------------------------------------------------------------------------------------

    @Test
    public void testBidiHashtable() {
        BidiHashtable<String, String> table = new BidiHashtable<String, String>();
        table.put("key1", "value1");
        table.put("key2", "value2");
        
        assertEquals("key1", table.getKey("value1"));
        assertEquals("key2", table.getKey("value2"));
    }

    @Test
    public void testCanPasteToDiagram() throws IOException {
        loadTestModel1();
        
        List<IDiagramModelComponent> selected = new ArrayList<IDiagramModelComponent>();
        
        // Empty selection
        CopySnapshot snapshot = new CopySnapshot(selected);
        assertFalse(snapshot.canPasteToDiagram(targetDiagramModel));
        
        // Select some
        selected.addAll(sourceDiagramModel.getChildren());
        
        snapshot = new CopySnapshot(selected);
        assertTrue(snapshot.canPasteToDiagram(targetDiagramModel));

        // Different diagram model types
        IDiagramModel sketchModel = IArchimateFactory.eINSTANCE.createSketchModel();
        assertFalse(snapshot.canPasteToDiagram(sketchModel));
    }
        
    @Test
    public void testCanPasteToDiagram_DifferentModelDiagramReference() throws IOException {
        loadTestModel1();
        
        // Test can't paste IDiagramModelReference to another Archimate model

        // Source model
        IDiagramModelReference reference = IArchimateFactory.eINSTANCE.createDiagramModelReference();
        reference.setReferencedModel(model.getDiagramModels().get(0));
        sourceDiagramModel.getChildren().add(reference);
        
        // Target model
        IArchimateModel model2 = IArchimateFactory.eINSTANCE.createArchimateModel();
        IArchimateDiagramModel targetDiagramModel2 = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        model2.getDefaultFolderForObject(targetDiagramModel2).getElements().add(targetDiagramModel2);
        
        List<IDiagramModelComponent> selected = new ArrayList<IDiagramModelComponent>();
        selected.add(reference);

        CopySnapshot snapshot = new CopySnapshot(selected);
        assertFalse(snapshot.canPasteToDiagram(targetDiagramModel2));
        
        // Should be OK if other objects added
        selected.addAll(sourceDiagramModel.getChildren());
        snapshot = new CopySnapshot(selected);
        assertTrue(snapshot.canPasteToDiagram(targetDiagramModel2));
    }

    @Test
    public void testGetPasteCommand() throws IOException {
        loadTestModel1();
        
        List<IDiagramModelComponent> selected = new ArrayList<IDiagramModelComponent>();
        selected.addAll(getAllDiagramComponents(sourceDiagramModel));
        
        CopySnapshot snapshot = new CopySnapshot(selected);
        assertNotNull(snapshot);
        
        // Should be null
        Command cmd = snapshot.getPasteCommand(null, null, null, false);
        assertNull(cmd);

        // Real one
        cmd = snapshot.getPasteCommand(targetDiagramModel, mock(GraphicalViewer.class), null, false);
        assertTrue(cmd.canExecute());

        cmd.execute();

        // Same number of objects pasted
        assertEquals(countAllObjects(sourceDiagramModel), countAllObjects(targetDiagramModel));

        // Same number of connections pasted
        assertEquals(countAllConnections(sourceDiagramModel), countAllConnections(targetDiagramModel));
    }
    
    @Test
    public void testNestedConnectionIsCopied() throws IOException {
        loadTestModel1();
        
        // Create parent object
        IDiagramModelArchimateObject dmoParent =
                ArchimateTestModel.createDiagramModelArchimateObject(IArchimateFactory.eINSTANCE.createBusinessActor());
        dmoParent.setBounds(0, 0, 200, 200);
        sourceDiagramModel.getChildren().add(dmoParent);
        
        // Create child object
        IDiagramModelArchimateObject dmoChild =
                ArchimateTestModel.createDiagramModelArchimateObject(IArchimateFactory.eINSTANCE.createBusinessRole());
        dmoChild.setBounds(0, 0, 100, 100);
        dmoParent.getChildren().add(dmoChild);
        
        // Create relationship
        IArchimateRelationship relationship = IArchimateFactory.eINSTANCE.createAssignmentRelationship();
        relationship.setSource(dmoParent.getArchimateElement());
        relationship.setTarget(dmoChild.getArchimateElement());
        
        // Test that an explicit connection is copied
        
        // Create connection
        IDiagramModelArchimateConnection connection =
                ArchimateTestModel.createDiagramModelArchimateConnection(relationship);
        connection.connect(dmoParent, dmoChild);
        
        List<IDiagramModelComponent> selected = new ArrayList<IDiagramModelComponent>();
        selected.add(dmoParent);
        
        CopySnapshot snapshot = new CopySnapshot(selected);
        Command cmd = snapshot.getPasteCommand(targetDiagramModel, null, null, false);
        assertNotNull(cmd);
        cmd.execute();
        assertEquals(1, countAllConnections(targetDiagramModel));
    }
    
    @Test
    public void testDiagramCopyContainsAllConnectionsAndObjects() throws IOException {
        loadTestModel2();
        
        List<IDiagramModelComponent> selected = new ArrayList<IDiagramModelComponent>();
        
        // Select all
        selected.addAll(getAllDiagramComponents(sourceDiagramModel));
        
        CopySnapshot snapshot = new CopySnapshot(selected);
        assertTrue(snapshot.canPasteToDiagram(targetDiagramModel));

        Command cmd = snapshot.getPasteCommand(targetDiagramModel, mock(GraphicalViewer.class), null, false);
        assertTrue(cmd.canExecute());
        cmd.execute();
        
        // Same number of objects pasted
        assertEquals(8, countAllObjects(sourceDiagramModel));
        assertEquals(countAllObjects(sourceDiagramModel), countAllObjects(targetDiagramModel));

        // Same number of connections pasted
        assertEquals(11, countAllConnections(sourceDiagramModel));
        assertEquals(countAllConnections(sourceDiagramModel), countAllConnections(targetDiagramModel));
    }
    
    @Test
    public void testCopiedObjectsHaveIdentifiersAndParentsWhenPastedToSameDiagramModel() throws IOException {
        loadTestModel1();
        testCopiedObjectsHaveIdentifiersAndParents(sourceDiagramModel);
    }
    
    @Test
    public void testCopiedObjectsHaveIdentifiersAndParentsWhenPastedToNewDiagramModel() throws IOException {
        loadTestModel1();
        testCopiedObjectsHaveIdentifiersAndParents(targetDiagramModel);
    }

    @Test
    public void testCopiedObjectsHaveIdentifiersAndParentsWhenPastedToNewArchimateModel() throws IOException {
        loadTestModel1();
        
        ArchimateTestModel tm = new ArchimateTestModel();
        IArchimateModel newModel = tm.createNewModel();
        targetDiagramModel = newModel.getDefaultDiagramModel();
        
        testCopiedObjectsHaveIdentifiersAndParents(targetDiagramModel);
    }

    private void testCopiedObjectsHaveIdentifiersAndParents(IDiagramModel newTargetDiagramModel) {
        List<IDiagramModelComponent> selected = new ArrayList<IDiagramModelComponent>();
        selected.addAll(getAllDiagramComponents(sourceDiagramModel));
        
        CopySnapshot snapshot = new CopySnapshot(selected);
        Command cmd = snapshot.getPasteCommand(newTargetDiagramModel, mock(GraphicalViewer.class), null, false);
        cmd.execute();
        
        for(TreeIterator<EObject> iter = newTargetDiagramModel.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            
            assertNotNull(eObject.eContainer());
            
            if(eObject instanceof IIdentifier) {
                assertNotNull(((IIdentifier)eObject).getId());
            }
            if(eObject instanceof IDiagramModelArchimateComponent) {
                assertNotNull(((IDiagramModelArchimateComponent)eObject).getArchimateConcept());
                assertNotNull(((IDiagramModelArchimateComponent)eObject).getArchimateConcept().eContainer());
            }
        }
    }


    // ---------------------------------------------------------------------------------------------
    // Support
    // ---------------------------------------------------------------------------------------------

    private void loadTestModel1() throws IOException {
        tm = new ArchimateTestModel(TestData.TEST_MODEL_FILE_ARCHISURANCE);
        model = tm.loadModelWithCommandStack();
        sourceDiagramModel = model.getDiagramModels().get(1);
        targetDiagramModel = tm.addNewArchimateDiagramModel();
    }
    
    private void loadTestModel2() throws IOException {
        tm = new ArchimateTestModel(new File(TestSupport.getTestDataFolder(), "models/testCopySnapshot.archimate"));
        model = tm.loadModelWithCommandStack();
        sourceDiagramModel = model.getDiagramModels().get(0);
        targetDiagramModel = tm.addNewArchimateDiagramModel();
    }
    
    private List<IDiagramModelComponent> getAllDiagramComponents(IDiagramModel dm) {
        List<IDiagramModelComponent> list = new ArrayList<IDiagramModelComponent>();
        
        for(Iterator<EObject> iter = dm.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            if(eObject instanceof IDiagramModelComponent) {
                list.add((IDiagramModelComponent)eObject);
            }
        }
        
        return list;
    }

    private int countAllObjects(IDiagramModel dm) {
        return countAllThings(dm, IArchimatePackage.eINSTANCE.getDiagramModelObject());
    }
        
    private int countAllConnections(IDiagramModel dm) {
        return countAllThings(dm, IArchimatePackage.eINSTANCE.getDiagramModelConnection());
    }

    private int countAllThings(IDiagramModel dm, EClass type) {
        int count = 0;
        
        for(Iterator<EObject> iter = dm.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            if(type.isSuperTypeOf(eObject.eClass())) {
                count++;
            }
        }
        
        return count;
    }

}