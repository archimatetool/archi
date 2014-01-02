/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.bolton.archimate.model.util.IDAdapter;


public class ArchimateModelTests {
    
    /**
     * This is required in order to run JUnit 4 tests with the old JUnit runner
     * 
     * @return
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ArchimateModelTests.class);
    }
    
    // ---------------------------------------------------------------------------------------------
    // BEFORE AND AFTER METHODS GO HERE 
    // ---------------------------------------------------------------------------------------------
    
    @BeforeClass
    public static void runOnceBeforeAllTests() {
    }
    
    @AfterClass
    public static void runOnceAfterAllTests() {
    }
    
    @Before
    public void runBeforeEachTest() {
    }
    
    @After
    public void runAfterEachTest() {
    }
    
    // ---------------------------------------------------------------------------------------------
    // public void addDefaultFolders();
    // ---------------------------------------------------------------------------------------------

    @Test
    public void addDefaultFolders_Empty() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        
        EList<IFolder> list = model.getFolders();
        
        // No folders by default
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void setDefaults_Folders_Populated() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();
        
        EList<IFolder> list = model.getFolders();
        
        // Correct number of folders
        assertNotNull(list);
        assertEquals(8, list.size());
        
        // Types
        assertEquals(FolderType.BUSINESS, list.get(0).getType());
        assertEquals(FolderType.APPLICATION, list.get(1).getType());
        assertEquals(FolderType.TECHNOLOGY, list.get(2).getType());
        assertEquals(FolderType.MOTIVATION, list.get(3).getType());
        assertEquals(FolderType.IMPLEMENTATION_MIGRATION, list.get(4).getType());
        assertEquals(FolderType.CONNECTORS, list.get(5).getType());
        assertEquals(FolderType.RELATIONS, list.get(6).getType());
        assertEquals(FolderType.DIAGRAMS, list.get(7).getType());
    }
    
    @Test
    public void setDefaults_Folders_MoreThanOnce() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();
        // Add Again
        model.setDefaults();
        
        EList<IFolder> list = model.getFolders();
        
        // Correct number of folders
        assertNotNull(list);
        assertEquals(8, list.size());
    }

    // ---------------------------------------------------------------------------------------------
    // public IFolder getDefaultFolderForElement(EObject element);
    // ---------------------------------------------------------------------------------------------

    @Test
    public void getDefaultFolderForElement() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        
        EObject element = IArchimateFactory.eINSTANCE.createBusinessEvent();
        IFolder folder = model.getDefaultFolderForElement(element);
        assertNotNull(folder);
        assertEquals(FolderType.BUSINESS, folder.getType());
        
        element = IArchimateFactory.eINSTANCE.createApplicationComponent();
        folder = model.getDefaultFolderForElement(element);
        assertNotNull(folder);
        assertEquals(FolderType.APPLICATION, folder.getType());
        
        element = IArchimateFactory.eINSTANCE.createNode();
        folder = model.getDefaultFolderForElement(element);
        assertNotNull(folder);
        assertEquals(FolderType.TECHNOLOGY, folder.getType());
        
        element = IArchimateFactory.eINSTANCE.createAndJunction();
        folder = model.getDefaultFolderForElement(element);
        assertNotNull(folder);
        assertEquals(FolderType.CONNECTORS, folder.getType());
        
        element = IArchimateFactory.eINSTANCE.createAssignmentRelationship();
        folder = model.getDefaultFolderForElement(element);
        assertNotNull(folder);
        assertEquals(FolderType.RELATIONS, folder.getType());
        
        element = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        folder = model.getDefaultFolderForElement(element);
        assertNotNull(folder);
        assertEquals(FolderType.DIAGRAMS, folder.getType());
    }
    
    // ---------------------------------------------------------------------------------------------
    // public IFolder getFolder(FolderType type);
    // ---------------------------------------------------------------------------------------------

    @Test
    public void getFolder() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        
        IFolder folder = model.getFolder(FolderType.BUSINESS);
        assertNull(folder);
        
        folder = model.getFolder(FolderType.DIAGRAMS);
        assertNull(folder);
        
        model.setDefaults();
        
        folder = model.getFolder(FolderType.BUSINESS);
        assertEquals(FolderType.BUSINESS, folder.getType());
        
        folder = model.getFolder(FolderType.DIAGRAMS);
        assertEquals(FolderType.DIAGRAMS, folder.getType());
    }
    
    // ---------------------------------------------------------------------------------------------
    // public IDiagramModel getDefaultDiagramModel();
    // ---------------------------------------------------------------------------------------------

    @Test
    public void getDefaultDiagramModel() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        
        IDiagramModel dm = model.getDefaultDiagramModel();
        assertNull(dm);
        
        IDiagramModel newDiagramModel1 = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        model.getDefaultFolderForElement(newDiagramModel1).getElements().add(newDiagramModel1);
        IDiagramModel newDiagramModel2 = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        model.getDefaultFolderForElement(newDiagramModel2).getElements().add(newDiagramModel2);
        
        dm = model.getDefaultDiagramModel();
        assertEquals(newDiagramModel1, dm);
    }

    // ---------------------------------------------------------------------------------------------
    // public EList<IDiagramModel> getDiagramModels();
    // ---------------------------------------------------------------------------------------------

    @Test
    public void getDiagramModels() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        
        EList<IDiagramModel> list = model.getDiagramModels();
        assertNotNull(list);
        assertTrue(list.isEmpty());
        
        IDiagramModel newDiagramModel1 = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        model.getDefaultFolderForElement(newDiagramModel1).getElements().add(newDiagramModel1);
        IDiagramModel newDiagramModel2 = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        model.getDefaultFolderForElement(newDiagramModel2).getElements().add(newDiagramModel2);
        
        list = model.getDiagramModels();
        assertEquals(2, list.size());
    }
    
    // ---------------------------------------------------------------------------------------------
    // ID Adapter
    // ---------------------------------------------------------------------------------------------
   
    @Test
    public void testIDAdapterAddedToArchimateModel() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        assertTrue(model.eAdapters().get(0) instanceof IDAdapter);
    }
    
    @Test
    public void testIDAddedToModel() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        String id = model.getId();
        assertNull(model.getId());
        
        model.setDefaults();
        id = model.getId();
        assertNotNull(id);
        assertEquals(8, id.length());
    }

    @Test
    public void testIDAddedToChildElement() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        
        IArchimateElement element = IArchimateFactory.eINSTANCE.createApplicationService();
        assertNull(element.getId());
        
        model.getDefaultFolderForElement(element).getElements().add(element);
        String id = element.getId();
        assertNotNull(id);
        assertEquals(8, id.length());
    }

}
