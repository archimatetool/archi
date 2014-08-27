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

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.internal.Model;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.editor.diagram.actions.CopySnapshot.BidiHashtable;
import com.archimatetool.editor.diagram.commands.DiagramCommandFactory;
import com.archimatetool.editor.model.commands.DeleteElementCommand;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IDiagramModelReference;
import com.archimatetool.model.INameable;
import com.archimatetool.model.IRelationship;
import com.archimatetool.testingtools.ArchimateTestModel;
import com.archimatetool.tests.TestData;

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
        tm = new ArchimateTestModel(TestData.TEST_MODEL_FILE_ARCHISURANCE);
        model = tm.loadModelWithCommandStack();
        // Note: the source diagram model used, is the view "Layered View" from the ArchiShurance test mode.
        sourceDiagramModel = model.getDiagramModels().get(1);
        targetDiagramModel = tm.addNewArchimateDiagramModel();
    }
    
    @After
    public void runOnceAfterEachTest() {
        targetDiagramModel.getChildren().clear();
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
        
        // Should be null
        Command cmd = snapshot.getPasteCommand(null, null, null);
        assertNull(cmd);

        // Real one
        cmd = snapshot.getPasteCommand(targetDiagramModel, mock(GraphicalViewer.class), null);
        assertTrue(cmd.canExecute());

        cmd.execute();

        // Same number of objects pasted
        assertEquals(countObjects(selectedObjects), countObjects(targetDiagramModel.getChildren()));

        // Same number of connections pasted
        assertEquals(countConnections(selectedObjects), countConnections(targetDiagramModel.getChildren()));
    }


    
    // mbd: 2014-04-12
    // This test is because I need something to support refactoring the method  on CopySnapShot.
    // Unfortunately, the method "needsCopiedArchimateElements" is private.
    // In lack of knowledge of policy, I use reflection to get the private method. I know some people dislike this. 
    @Test
    public void testNeedsCopiedArchimateElements() {
    	// Reflection helper class. Construct, then use call to call.
    	// The call emulates the part of getPasteCommand that sets up a field on the CopySnapshot instance.
    	class NCAECaller {
    		Method method;
    		Field field;
    		CopySnapshot snapshot;
        	NCAECaller(CopySnapshot snapshot) {
                @SuppressWarnings("rawtypes")
				Class[] argClasses = {IDiagramModel.class};
        		try {
        			method = CopySnapshot.class.getDeclaredMethod("needsCopiedArchimateElements", argClasses);
        		} catch (NoSuchMethodException | SecurityException e) {
        			throw new AssertionError("Unable to access private method CopySnapshot.needsCopiedArchimateElements during test", e);
        		}
                method.setAccessible(true); 
                try {
                	field = CopySnapshot.class.getDeclaredField("fTargetArchimateModel");
				} catch (NoSuchFieldException | SecurityException e) {
					throw new AssertionError("Unable to access private field CopySnapshot.fTargetArchimateModel during test", e);
				}	
                field.setAccessible(true);
                this.snapshot = snapshot;
        	};
        	boolean needsCopiedArchimateElements(IDiagramModel diagramModel) {
                Object[] argObjects = {diagramModel};
                // Since the selection contains stuff, and the target does not, it should be OK.
                try {
                	// As part of the public getPasteCommand, fTargetArchimateModel is set, so we set it here
                	field.set(snapshot, diagramModel.getArchimateModel());
                	return (boolean) method.invoke(snapshot, argObjects);
				} catch (IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
        			throw new AssertionError("Call to private method CopySnapshot.needsCopiedArchimateElements failed during test", e);
				}
        	}
        };

        // Check some assumptions about the model before trying to test on it.
        assertEquals("sourceDiagramModel does not point at Layered View, or it changed name", "Layered View", sourceDiagramModel.getName() );
        assertEquals("number of elements on view have changed", 7, sourceDiagramModel.getChildren().size());

        // TODO: Note, technically, a test is missing with a partial overlap of elements; that is, the selection contains a single element already present in the target diagram.
        
        // No need to copy, if objects taken from source view, and copied to new empty diagram (in the same archimate model)
        {
            // Set up a selection with objects from the source diagram model.
        	List<IDiagramModelObject> selectedObjects = new ArrayList<IDiagramModelObject>();
            selectedObjects.addAll(sourceDiagramModel.getChildren());
            
            // Create a snapshot based on the objects in the selection.
            CopySnapshot snapshot = new CopySnapshot(selectedObjects);
            assertNotNull(snapshot);

            // Set up helper for the reflection stuff.
            NCAECaller caller = new NCAECaller(snapshot);
            assertFalse("No need to copy selection, when copying to emty diagram", caller.needsCopiedArchimateElements(targetDiagramModel));
        }

        // Same test as above, but using a single element from another diagram and paste it into target diagram => no copy needed
        {
        	// Find a single element from the source and copy it into the view
        	// 4193 is a firewall element in a diagram different from the Layered View.
        	 EObject element = tm.getObjectByID("4193");
             assertNotNull(element);
             assertTrue( "This must be a diagram element", element instanceof IDiagramModelObject );
        	
             // Set up a selection with objects from the source diagram model.
             List<IDiagramModelObject> selectedObjects = new ArrayList<IDiagramModelObject>();
             selectedObjects.add((IDiagramModelObject) element);
             
             // Create a snapshot based on the objects in the selection.
             CopySnapshot snapshot = new CopySnapshot(selectedObjects);
             assertNotNull(snapshot);

             // Set up helper for the reflection stuff.
             NCAECaller caller = new NCAECaller(snapshot);
             assertFalse("No need to copy element, if element not present in target diagram", caller.needsCopiedArchimateElements(targetDiagramModel));
        }
                
        // Must copy, if an element has been deleted
        {
        	// Find a single element from the source and copy it into the view
        	// 4193 is a firewall element in a diagram different from the Layered View.
        	 EObject element = tm.getObjectByID("4193");
             assertNotNull(element);
             assertTrue( "This must be a diagram object", element instanceof IDiagramModelObject );
             assertTrue( "This must be an archimate diagram element", element instanceof IDiagramModelArchimateObject );
             
             IDiagramModelArchimateObject object = (IDiagramModelArchimateObject) element;
             assertTrue( "The underlying archimate element must be an INameable", object.getArchimateElement() instanceof INameable);
             
             // Set up a selection with objects from the source diagram model.
             List<IDiagramModelObject> selectedObjects = new ArrayList<IDiagramModelObject>();
             selectedObjects.add(object);
             
             // Create a snapshot based on the objects in the selection.
             CopySnapshot snapshot = new CopySnapshot(selectedObjects);
             assertNotNull(snapshot);

             // Delete the element from the diagram it came from. I hope.
             // TODO: I hope this does what I think it does... it probably leaves an inconsistent model
             // I had a look at the DeleteFromModelAction but it needed an IWorkbenchPart, and I really don't think I have one with me.
             Command cmd = new DeleteElementCommand(object.getArchimateElement());
             cmd.execute();
             object.setArchimateElement(null);
             assertTrue( "The archimate element should now be deleted", null == object.getArchimateElement());

             // Set up helper for the reflection stuff.
             NCAECaller caller = new NCAECaller(snapshot);
             // Since the selection contains an element that have been deleted from the mode, a copy is needed
             assertTrue("Must copy element, when element has been deleted in the model", caller.needsCopiedArchimateElements(targetDiagramModel));

             // Restore the model as it was.
             tm = new ArchimateTestModel(TestSupport.TEST_MODEL_FILE_ARCHISURANCE);
             try {
				model = tm.loadModelWithCommandStack();
			} catch (IOException e) {
				throw new AssertionError( "Unable to reload/reset the model file while testing", e);
			}
             // Note: the source model used, is the view "Layered View" from the ArchiShurance test mode.
             sourceDiagramModel = model.getDiagramModels().get(1);
             targetDiagramModel = tm.addNewArchimateDiagramModel();
        }

        // Must copy, if an element is present in target; copy from the model into it self
        {
        	// Find a single element from the source and copy it into the view
        	// 4104 is an element from the layered view.
        	 EObject element = tm.getObjectByID("4104");
             assertNotNull(element);
             assertTrue( "This must be a diagram element", element instanceof IDiagramModelObject );
             
             // Set up a selection with objects from the source diagram model.
             List<IDiagramModelObject> selectedObjects = new ArrayList<IDiagramModelObject>();
             selectedObjects.add((IDiagramModelObject) element);
             
             // Create a snapshot based on the objects in the selection.
             CopySnapshot snapshot = new CopySnapshot(selectedObjects);
             assertNotNull(snapshot);

             // Set up helper for the reflection stuff.
             NCAECaller caller = new NCAECaller(snapshot);
             assertTrue("Must copy if element present in diagram already", caller.needsCopiedArchimateElements(sourceDiagramModel));
        }

        // Must copy, if an relationship has been deleted
        {
            // Set up a selection with objects from the source diagram model.
        	List<IDiagramModelObject> selectedObjects = new ArrayList<IDiagramModelObject>();
            selectedObjects.addAll(sourceDiagramModel.getChildren());
            
            // Create a snapshot based on the objects in the selection.
            CopySnapshot snapshot = new CopySnapshot(selectedObjects);
            assertNotNull(snapshot);
        	
        	// Find a single relation from the source and delete it
        	// 4153 is a connection from the Layered View, that uses the relation in 1446 
        	 EObject element = tm.getObjectByID("4153");
             assertNotNull(element);
             assertTrue( "This must be a diagram object", element instanceof IDiagramModelConnection );
             assertTrue( "This must be an archimate diagram connection", element instanceof IDiagramModelArchimateConnection );
             
             IDiagramModelArchimateConnection connection = (IDiagramModelArchimateConnection) element;

             // TODO: This is probably not the way to delete the relation from the model
             assertTrue( "The underlying archimate connection must be an INameable", connection.getRelationship() instanceof INameable);
             assertEquals( "Got the right connection", "1446", connection.getRelationship().getId() );
             Command cmd = new DeleteElementCommand(connection.getRelationship());
             cmd.execute();
             connection.setRelationship(null);             
            
             // Set up helper for the reflection stuff.
             NCAECaller caller = new NCAECaller(snapshot);
             assertTrue("Must copy when connection has been deleted", caller.needsCopiedArchimateElements(targetDiagramModel));

             // Restore the model as it was.
             tm = new ArchimateTestModel(TestSupport.TEST_MODEL_FILE_ARCHISURANCE);
             try {
				model = tm.loadModelWithCommandStack();
			} catch (IOException e) {
				throw new AssertionError( "Unable to reload/reset the model file while testing", e);
			}
             // Note: the source model used, is the view "Layered View" from the ArchiShurance test mode.
             sourceDiagramModel = model.getDiagramModels().get(1);
             targetDiagramModel = tm.addNewArchimateDiagramModel();
        }

        // TODO: Must copy if an relationship is already present in target
        {
        	// I don't know how to write this test...
        }

        
    	// Must copy if same model, and it contains elements already present.
        // This is a copy of everything, so it should return true
        {
            // Set up a selection with objects from the source model.
        	List<IDiagramModelObject> selectedObjects = new ArrayList<IDiagramModelObject>();
            selectedObjects.addAll(sourceDiagramModel.getChildren());
            
            // Create a snapshot based on the objects in the selection.
            CopySnapshot snapshot = new CopySnapshot(selectedObjects);
            assertNotNull(snapshot);

            // Set up helper for the reflection stuff.
            NCAECaller caller = new NCAECaller(snapshot);
            assertTrue("Must copy if selection contains elements already present", (boolean) caller.needsCopiedArchimateElements(sourceDiagramModel));
        }

        // Must return copy if different ArchimateModesl....
        {
            // Load a new ArchimateTestModel
        	ArchimateTestModel tm2 = new ArchimateTestModel(TestSupport.TEST_MODEL_FILE_ARCHISURANCE);
        	IArchimateModel model2;
            try {
				 model2 = tm2.loadModelWithCommandStack();
			} catch (IOException e) {
				throw new AssertionError( "Unable to load an additional model file while testing", e);
			}
            targetDiagramModel = tm2.addNewArchimateDiagramModel();
            
            // Set up a selection with objects from the source model.
        	List<IDiagramModelObject> selectedObjects = new ArrayList<IDiagramModelObject>();
            selectedObjects.addAll(sourceDiagramModel.getChildren());
            
            // Create a snapshot based on the objects in the selection.
            CopySnapshot snapshot = new CopySnapshot(selectedObjects);
            assertNotNull(snapshot);

            // Set up helper for the reflection stuff.
            NCAECaller caller = new NCAECaller(snapshot);
            assertTrue("Must copy if archimate models are different", caller.needsCopiedArchimateElements(targetDiagramModel));
        }
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