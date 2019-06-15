/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.commands.CommandStack;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.archimatetool.editor.TestSupport;
import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.editor.views.tree.TreeModelViewer;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelReference;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.util.ArchimateModelUtils;
import com.archimatetool.testingtools.ArchimateTestModel;

import junit.framework.JUnit4TestAdapter;



/**
 * It uses Mockito's mock objects
 */
@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("nls")
public class DeleteCommandHandlerTests {
    
    static File TEST_MODEL_FILE = new File(TestSupport.getTestDataFolder(), "models/testDeleteHandler.archimate");
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DeleteCommandHandlerTests.class);
    }
    
    private ArchimateTestModel tm;
    private IArchimateModel model;
    
    @Mock
    private TreeModelViewer treeModelViewer;
    
    @Before
    public void runOnceBeforeEachTest() throws IOException {
        tm = new ArchimateTestModel(TEST_MODEL_FILE);
        model = tm.loadModelWithCommandStack();
    }

    @Test
    public void testCanDeleteAllElements() {
        // Can delete all ArchiMate elements
        for(EClass eClass : ArchimateModelUtils.getAllArchimateClasses()) {
            assertTrue(DeleteCommandHandler.canDelete(IArchimateFactory.eINSTANCE.create(eClass)));
        }
    }
    
    @Test
    public void testCanDeleteAllRelations() {
        // Can delete all ArchiMate relations
        for(EClass eClass : ArchimateModelUtils.getRelationsClasses()) {
            assertTrue(DeleteCommandHandler.canDelete(IArchimateFactory.eINSTANCE.create(eClass)));
        }
    }

    @Test
    public void testCanDeleteCertainFolders() {
        IFolder folder = IArchimateFactory.eINSTANCE.createFolder();
        
        // Can only delete derived and user folders
        for(FolderType type : FolderType.values()) {
            folder.setType(type);
            
            if(type == FolderType.USER) { // Can delete these
                assertTrue(DeleteCommandHandler.canDelete(folder));
            }
            else {
                assertFalse(DeleteCommandHandler.canDelete(folder));
            }
        }
    }
    
    @Test
    public void testHasDiagramReferences() {
        // One is and one isn't in a diagram
        Object[] elements = new Object[] {
                ArchimateModelUtils.getObjectByID(model, "31a27739"),
                ArchimateModelUtils.getObjectByID(model, "d856464a")
        };
        
        DeleteCommandHandler commandHandler = new DeleteCommandHandler(treeModelViewer, elements);
        assertTrue(commandHandler.hasDiagramReferences());
        
        // This element isn't in a diagram
        elements = new Object[] {
                ArchimateModelUtils.getObjectByID(model, "d856464a")
        };
        
        commandHandler = new DeleteCommandHandler(treeModelViewer, elements);
        assertFalse(commandHandler.hasDiagramReferences());
    }
    
    @Test
    public void testDelete_CannotDeleteMainFolders() {
        // Cannot delete the main folders
        assertEquals(8,  model.getFolders().size());
        
        DeleteCommandHandler commandHandler = new DeleteCommandHandler(treeModelViewer, model.getFolders().toArray());
        commandHandler.delete();
        
        assertEquals(8,  model.getFolders().size());
    }
    
    @Test
    public void testDelete_Elements() {
        String[] elementIDs = new String[] {
                "31a27739",
                "8ab84e91",
                "46d0abd5",
                "8ecabfc2"
        };
        
        List<IArchimateElement> elements = new ArrayList<IArchimateElement>();
        for(String id : elementIDs) {
            elements.add((IArchimateElement)ArchimateModelUtils.getObjectByID(model, id));
        }
        
        // Ensure these elements are referenced in diagrams
        for(IArchimateElement element : elements) {
            assertTrue(DiagramModelUtils.isArchimateConceptReferencedInDiagrams(element));
        }
        
        // Delete them
        DeleteCommandHandler commandHandler = new DeleteCommandHandler(treeModelViewer, elements.toArray());
        commandHandler.delete();
        
        // Test that they are all gone in the model and in the referenced diagrams
        for(IArchimateElement element : elements) {
            assertFalse(DiagramModelUtils.isArchimateConceptReferencedInDiagrams(element));
            assertNull(ArchimateModelUtils.getObjectByID(model, element.getId()));
        }
        
        // And just for good measure, we'll undo it
        CommandStack commandStack = (CommandStack)model.getAdapter(CommandStack.class);
        commandStack.undo();
        
        // And find them all back again!
        for(IArchimateElement element : elements) {
            assertTrue(DiagramModelUtils.isArchimateConceptReferencedInDiagrams(element));
            assertNotNull(ArchimateModelUtils.getObjectByID(model, element.getId()));
        }
    }
    
    @Test
    public void testDelete_Folder_Deleted_SubElements() {
        // Sub-folder with elements
        Object[] folder = new Object[] {
                ArchimateModelUtils.getObjectByID(model, "0bd99416")
        };
        
        assertTrue(folder[0] instanceof IFolder);
        
        // sub-Elements that should be zapped
        String[] elementIDs = new String[] {
                "e836c6be", // element
                "b5742e18", // element
                "d1247cf1"  // element
        };
        
        List<IArchimateElement> elements = new ArrayList<IArchimateElement>();
        for(String id : elementIDs) {
            elements.add((IArchimateElement)ArchimateModelUtils.getObjectByID(model, id));
        }
        
        // Delete top folder
        DeleteCommandHandler commandHandler = new DeleteCommandHandler(treeModelViewer, folder);
        commandHandler.delete();
        
        // Is top folder deleted?
        assertNull(ArchimateModelUtils.getObjectByID(model, "0bd99416"));
        
        // Is sub folder deleted?
        assertNull(ArchimateModelUtils.getObjectByID(model, "02460ff1"));

        // Are elements deleted?
        for(IArchimateElement element : elements) {
            assertNull(ArchimateModelUtils.getObjectByID(model, element.getId()));
            assertFalse(DiagramModelUtils.isArchimateConceptReferencedInDiagrams(element));
        }
    }
    
    @Test
    public void testDelete_Elements_Deleted_Attached_Relations() {
        // Element 1
        IArchimateElement businessActor = (IArchimateElement)ArchimateModelUtils.getObjectByID(model, "31a27739");
        assertNotNull(businessActor);
        
        // Element 2
        IArchimateElement businessRole = (IArchimateElement)ArchimateModelUtils.getObjectByID(model, "8ab84e91");
        assertNotNull(businessRole);
        
        // Connecting relationship
        IArchimateRelationship relationship = (IArchimateRelationship)ArchimateModelUtils.getObjectByID(model, "3bede7f0");
        assertNotNull(relationship);
        
        // Relationship is there on a diagram
        assertTrue(DiagramModelUtils.isArchimateConceptReferencedInDiagrams(relationship));
        
        // Zap
        DeleteCommandHandler commandHandler = new DeleteCommandHandler(treeModelViewer, new Object[] { businessActor, businessRole } );
        commandHandler.delete();
        
        // All gone
        assertNull(ArchimateModelUtils.getObjectByID(model, relationship.getId()));
        assertFalse(DiagramModelUtils.isArchimateConceptReferencedInDiagrams(relationship));
    }
    
    @Test
    public void testDelete_Relations_Deleted_Diagram_Connections() {
        // Connecting relationship
        IArchimateRelationship relationship = (IArchimateRelationship)ArchimateModelUtils.getObjectByID(model, "3bede7f0");
        assertNotNull(relationship);
        
        // Relationship is there on a diagram
        assertTrue(DiagramModelUtils.isArchimateConceptReferencedInDiagrams(relationship));
        
        // Zap
        DeleteCommandHandler commandHandler = new DeleteCommandHandler(treeModelViewer, new Object[] { relationship } );
        commandHandler.delete();
        
        // All gone
        assertNull(ArchimateModelUtils.getObjectByID(model, relationship.getId()));
        assertFalse(DiagramModelUtils.isArchimateConceptReferencedInDiagrams(relationship));
    }

    @Test
    public void testDelete_DiagramModel_Deleted_Diagram_Reference() {
        IDiagramModel dm2 = model.getDiagramModels().get(1);
        
        // Reference to dm2 in dm1
        IDiagramModelReference dmRef = (IDiagramModelReference)ArchimateModelUtils.getObjectByID(model, "99a52921");
        assertNotNull(dmRef);
        
        // Zap
        DeleteCommandHandler commandHandler = new DeleteCommandHandler(treeModelViewer, new Object[] { dm2 } );
        commandHandler.delete();
        
        // All gone
        assertNull(ArchimateModelUtils.getObjectByID(model, dm2.getId()));
        assertNull(ArchimateModelUtils.getObjectByID(model, dmRef.getId()));
    }

    @Test
    public void testDelete_More_Than_One_Model() throws IOException {
        // Use another instance of the same model
        ArchimateTestModel tm2 = new ArchimateTestModel(TEST_MODEL_FILE);
        IArchimateModel model2 = tm2.loadModelWithCommandStack();
        
        // Elements
        String[] elementIDs = new String[] {
                "31a27739",
                "8ab84e91",
                "46d0abd5",
                "8ecabfc2"
        };
        
        // Select them from both models
        List<IArchimateElement> elements1 = new ArrayList<IArchimateElement>();
        for(String id : elementIDs) {
            elements1.add((IArchimateElement)ArchimateModelUtils.getObjectByID(model, id));
        }
        
        List<IArchimateElement> elements2 = new ArrayList<IArchimateElement>();
        for(String id : elementIDs) {
            elements2.add((IArchimateElement)ArchimateModelUtils.getObjectByID(model2, id));
        }
        
        List<IArchimateElement> allElements = new ArrayList<IArchimateElement>(elements1);
        allElements.addAll(elements2);
        assertEquals(8, allElements.size());
        
        // Zap
        DeleteCommandHandler commandHandler = new DeleteCommandHandler(treeModelViewer, allElements.toArray() );
        commandHandler.delete();
        
        // Test that they are all gone in the models and in the referenced diagrams
        for(IArchimateElement element : elements1) {
            assertFalse(DiagramModelUtils.isArchimateConceptReferencedInDiagrams(element));
            assertNull(ArchimateModelUtils.getObjectByID(model, element.getId()));
        }
        
        for(IArchimateElement element : elements2) {
            assertFalse(DiagramModelUtils.isArchimateConceptReferencedInDiagrams(element));
            assertNull(ArchimateModelUtils.getObjectByID(model2, element.getId()));
        }
        
        // And just for good measure, we'll undo it...once for each model
        CommandStack commandStack1 = (CommandStack)model.getAdapter(CommandStack.class);
        commandStack1.undo();
        
        CommandStack commandStack2 = (CommandStack)model2.getAdapter(CommandStack.class);
        commandStack2.undo();
        
        // And find them all back again!
        for(IArchimateElement element : elements1) {
            assertTrue(DiagramModelUtils.isArchimateConceptReferencedInDiagrams(element));
            assertNotNull(ArchimateModelUtils.getObjectByID(model, element.getId()));
        }

        for(IArchimateElement element : elements2) {
            assertTrue(DiagramModelUtils.isArchimateConceptReferencedInDiagrams(element));
            assertNotNull(ArchimateModelUtils.getObjectByID(model2, element.getId()));
        }
    }
}
