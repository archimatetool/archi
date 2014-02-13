/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.eclipse.gef.commands.Command;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.editor.TestSupport;
import com.archimatetool.editor.diagram.IDiagramModelEditor;
import com.archimatetool.editor.diagram.actions.CopySnapshot.BidiHashtable;
import com.archimatetool.editor.model.impl.EditorModelManager;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IDiagramModelReference;

@SuppressWarnings("nls")
public class CopySnapshotTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(CopySnapshotTests.class);
    }
    
    IArchimateModel model;
    IDiagramModel sourceDiagramModel;
    IDiagramModel targetDiagramModel;
    
    @Before
    public void runBeforeEachTest() {
        model = new EditorModelManager().openModel(TestSupport.TEST_MODEL_FILE_ARCHISURANCE);
        sourceDiagramModel = model.getDiagramModels().get(1);
        
        targetDiagramModel = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        model.getDefaultFolderForElement(targetDiagramModel).getElements().add(targetDiagramModel);
    }

    @After
    public void runAfterEachTest() {
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
        
        IDiagramModelEditor editor = EditorManager.openDiagramEditor(targetDiagramModel);
        
        // Should be null
        Command cmd = snapshot.getPasteCommand(null, null, null);
        assertNull(cmd);

        // Real one
        cmd = snapshot.getPasteCommand(targetDiagramModel, editor.getGraphicalViewer(), null);
        assertTrue(cmd.canExecute());

        //CommandStack stack = (CommandStack)model.getAdapter(CommandStack.class);
        //stack.execute(cmd);
        cmd.execute();

        // Same number of objects pasted
        assertEquals(countObjects(selectedObjects), countObjects(targetDiagramModel.getChildren()));

        // Same number of connections pasted
        assertEquals(countConnections(selectedObjects), countConnections(targetDiagramModel.getChildren()));
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