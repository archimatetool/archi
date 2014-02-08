/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import junit.framework.JUnit4TestAdapter;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.archimatetool.TestSupport;
import com.archimatetool.model.util.ArchimateResourceFactory;
import com.archimatetool.model.util.IDAdapter;



@SuppressWarnings("nls")
public class ArchimateModelTests {
    
    /**
     * This is required in order to run JUnit 4 tests with the old JUnit runner
     * 
     * @return
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ArchimateModelTests.class);
    }
    
    IArchimateModel model;
    
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
        model = IArchimateFactory.eINSTANCE.createArchimateModel();
    }
    
    @After
    public void runAfterEachTest() {
    }
    
    // ---------------------------------------------------------------------------------------------
    // public void addDefaultFolders();
    // ---------------------------------------------------------------------------------------------

    @Test
    public void testDefaultFoldersIsEmpty() {
        EList<IFolder> list = model.getFolders();
        
        // No folders by default
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testSetDefaults_IsCorrect() {
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
    public void testSetDefaults_MoreThanOnce() {
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
    public void testDefaultFolderForElementIsCorrect() {
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
    public void testGetFolderIsCorrectFolderType() {
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
    public void testGetDefaultDiagramModel() {
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
    public void testGetDiagramModels() {
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
        assertTrue(model.eAdapters().get(0) instanceof IDAdapter);
    }
    
    @Test
    public void testIDAddedToModel() {
        String id = model.getId();
        assertNull(model.getId());
        
        model.setDefaults();
        id = model.getId();
        assertNotNull(id);
        assertEquals(8, id.length());
    }

    @Test
    public void testIDAddedToChildElement() {
        IArchimateElement element = IArchimateFactory.eINSTANCE.createApplicationService();
        assertNull(element.getId());
        
        model.getDefaultFolderForElement(element).getElements().add(element);
        String id = element.getId();
        assertNotNull(id);
        assertEquals(8, id.length());
    }

    // ---------------------------------------------------------------------------------------------
    // Metadata
    // ---------------------------------------------------------------------------------------------
    
    @Test
    public void testMetadataCreated() throws Exception {
        String key ="some_key", value = "some_value";
        
        // Metadata exists
        IMetadata metadata = model.getMetadata();        
        assertNotNull(metadata);
        
        // Add a metadata entry as a property key/value pair
        IProperty property = IArchimateFactory.eINSTANCE.createProperty();
        property.setKey(key);
        property.setValue(value);
        metadata.getEntries().add(property);

        // Check entry is correct
        EList<IProperty> entries = metadata.getEntries();
        assertEquals(1, entries.size());
        assertEquals(property, entries.get(0));
        assertEquals(entries.get(0).getKey(), key);
        assertEquals(entries.get(0).getValue(), value);
        
        // Save to file
        File file = saveModel(model);
        assertTrue(file.exists());
        
        // Load it in again
        IArchimateModel model2 = loadModel(file);
        
        // Check it persisted
        entries = model2.getMetadata().getEntries();
        assertEquals(1, entries.size());
        IProperty property2 = entries.get(0);
        assertEquals(property2.getKey(), key);
        assertEquals(property2.getValue(), value);
    }
    
    // ---------------------------------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------------------------------

    private File saveModel(IArchimateModel model) throws IOException {
        File file = TestSupport.getTempFile(".archimate");
        
        ResourceSet resourceSet = ArchimateResourceFactory.createResourceSet();
        Resource resource = resourceSet.createResource(URI.createFileURI(file.getAbsolutePath()));
        resource.getContents().add(model);
        resource.save(null);

        return file;
    }
    
    private IArchimateModel loadModel(File file) throws IOException {
        ResourceSet resourceSet = ArchimateResourceFactory.createResourceSet();
        Resource resource = resourceSet.createResource(URI.createFileURI(file.getAbsolutePath()));
        resource.load(null);
        return (IArchimateModel)resource.getContents().get(0);
    }
}
