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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.eclipse.gef.commands.Command;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.editor.ArchimateTestModel;
import com.archimatetool.editor.TestSupport;
import com.archimatetool.editor.diagram.IDiagramModelEditor;
import com.archimatetool.editor.diagram.actions.CopySnapshot.BidiHashtable;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IDiagramModelReference;
import com.archimatetool.model.IRelationship;
import com.archimatetool.tests.TestUtils;

@SuppressWarnings("nls")
public class CopySnapshotTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(CopySnapshotTests.class);
    }
    
    private ArchimateTestModel tm;
    private IArchimateModel model;
    private IDiagramModel sourceDiagramModel;
    private IDiagramModel targetDiagramModel;
    
    @Before
    public void runOnceBeforeEachTest() throws IOException {
        tm = new ArchimateTestModel(TestSupport.TEST_MODEL_FILE_ARCHISURANCE);
        model = tm.loadModelWithCommandStack();
        sourceDiagramModel = model.getDiagramModels().get(1);
        targetDiagramModel = tm.addNewArchimateDiagramModel();
    }
    
    @After
    public void runOnceAfterEachTest() {
        targetDiagramModel.getChildren().clear();
    }

    @AfterClass
    public static void runOnceAfterAllTests() {
        TestUtils.closeAllEditors();
    }

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
    public void testCanPasteToDiagram() {
        List<IDiagramModelObject> selectedObjects = new ArrayList<IDiagramModelObject>();
        
        // Empty selection
        CopySnapshot snapshot = new CopySnapshot(selectedObjects);
        assertFalse(snapshot.canPasteToDiagram(targetDiagramModel));
        
        // Select some
        selectedObjects.addAll(sourceDiagramModel.getChildren());
        
        snapshot = new CopySnapshot(selectedObjects);
        assertTrue(snapshot.canPasteToDiagram(targetDiagramModel));

        // Different diagram model types
        IDiagramModel sketchModel = IArchimateFactory.eINSTANCE.createSketchModel();
        assertFalse(snapshot.canPasteToDiagram(sketchModel));
    }
        
    @Test
    public void testCanPasteToDiagram_DifferentModelDiagramReference() {
        // Test can't paste IDiagramModelReference to another Archimate model

        // Source model
        IDiagramModelReference reference = IArchimateFactory.eINSTANCE.createDiagramModelReference();
        reference.setReferencedModel(model.getDiagramModels().get(0));
        sourceDiagramModel.getChildren().add(reference);
        
        // Target model
        IArchimateModel model2 = IArchimateFactory.eINSTANCE.createArchimateModel();
        IArchimateDiagramModel targetDiagramModel2 = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        model2.getDefaultFolderForElement(targetDiagramModel2).getElements().add(targetDiagramModel2);
        
        List<IDiagramModelObject> selectedObjects = new ArrayList<IDiagramModelObject>();
        selectedObjects.add(reference);

        CopySnapshot snapshot = new CopySnapshot(selectedObjects);
        assertFalse(snapshot.canPasteToDiagram(targetDiagramModel2));
        
        // Should be OK if other objects added
        selectedObjects.addAll(sourceDiagramModel.getChildren());
        snapshot = new CopySnapshot(selectedObjects);
        assertTrue(snapshot.canPasteToDiagram(targetDiagramModel2));
    }

    @Test
    public void testGetPasteCommand() {
        List<IDiagramModelObject> selectedObjects = new ArrayList<IDiagramModelObject>();
        selectedObjects.addAll(sourceDiagramModel.getChildren());
        
        CopySnapshot snapshot = new CopySnapshot(selectedObjects);
        assertNotNull(snapshot);
        
        IDiagramModelEditor editor = EditorManager.openDiagramEditor(targetDiagramModel);
        assertNotNull(editor);
        
        // Should be null
        Command cmd = snapshot.getPasteCommand(null, null, null);
        assertNull(cmd);

        // Real one
        cmd = snapshot.getPasteCommand(targetDiagramModel, editor.getGraphicalViewer(), null);
        assertTrue(cmd.canExecute());

        cmd.execute();

        // Same number of objects pasted
        assertEquals(countObjects(selectedObjects), countObjects(targetDiagramModel.getChildren()));

        // Same number of connections pasted
        assertEquals(countConnections(selectedObjects), countConnections(targetDiagramModel.getChildren()));
    }
    
    @Test
    public void testNestedConnectionIsCopied() {
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
        IRelationship relationship = IArchimateFactory.eINSTANCE.createAssignmentRelationship();
        relationship.setSource(dmoParent.getArchimateElement());
        relationship.setTarget(dmoChild.getArchimateElement());
        
        // Test that an explicit connection is copied
        
        // Create connection
        IDiagramModelArchimateConnection connection =
                ArchimateTestModel.createDiagramModelArchimateConnection(relationship);
        connection.connect(dmoParent, dmoChild);
        
        List<IDiagramModelObject> selectedObjects = new ArrayList<IDiagramModelObject>();
        selectedObjects.add(dmoParent);
        
        CopySnapshot snapshot = new CopySnapshot(selectedObjects);
        Command cmd = snapshot.getPasteCommand(targetDiagramModel, null, null);
        assertNotNull(cmd);
        cmd.execute();
        assertEquals(1, countConnections(targetDiagramModel.getChildren()));
        
        // @TODO - need to redo implicit connection logic
        // Now test that an implicit nested connection is copied
//        connection.disconnect();
//        targetDiagramModel.getChildren().clear();
//        snapshot = new CopySnapshot(selectedObjects);
//        cmd = snapshot.getPasteCommand(targetDiagramModel, null, null);
//        cmd.execute();
//        assertEquals(1, countConnections(targetDiagramModel.getChildren()));
    }
    
    /**
     * @param objects
     * @return count of connections and child objects connections
     */
    private int countConnections(List<IDiagramModelObject> objects) {
        int count = 0;
        
        for(IDiagramModelObject dmo : objects) {
            count += dmo.getTargetConnections().size();
            
            if(dmo instanceof IDiagramModelContainer) {
                count += countConnections(((IDiagramModelContainer)dmo).getChildren());
            }
        }
        
        return count;
    }

    /**
     * @param objects
     * @return count of objects and child objects
     */
    private int countObjects(List<IDiagramModelObject> objects) {
        int count = objects.size();
        
        for(IDiagramModelObject dmo : objects) {
            if(dmo instanceof IDiagramModelContainer) {
                count += countObjects(((IDiagramModelContainer)dmo).getChildren());
            }
        }
        
        return count;
    }
}