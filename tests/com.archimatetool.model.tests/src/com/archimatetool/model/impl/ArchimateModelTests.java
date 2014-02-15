/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;

import junit.framework.JUnit4TestAdapter;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IMetadata;
import com.archimatetool.model.util.IDAdapter;


@SuppressWarnings("nls")
public class ArchimateModelTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ArchimateModelTests.class);
    }
    
    private ArchimateModel model;
    
    // ---------------------------------------------------------------------------------------------
    // BEFORE AND AFTER METHODS GO HERE 
    // ---------------------------------------------------------------------------------------------
    
    @Before
    public void runBeforeEachTest() {
        model = (ArchimateModel)IArchimateFactory.eINSTANCE.createArchimateModel();
    }
    
    // ---------------------------------------------------------------------------------------------
    // TESTS
    // ---------------------------------------------------------------------------------------------
    
    @Test
    public void testAddDefaultFolders() {
        // No folders by default
        EList<IFolder> list = model.getFolders();
        assertEquals(0, list.size());
        
        model.addDefaultFolders();
        list = model.getFolders();
        
        // Correct number of folders
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
        
        // Test can't do it twice
        model.addDefaultFolders();
        list = model.getFolders();
        assertEquals(8, list.size());
    }
    
    @Test
    public void testAdd_Remove_DerivedRelationsFolder() {
        IFolder folder = model.addDerivedRelationsFolder();
        assertTrue(folder.getType() == FolderType.DERIVED);
        assertEquals(9, model.getFolders().size());
        assertEquals(7, model.getFolders().indexOf(folder));
        
        model.removeDerivedRelationsFolder();
        assertEquals(8, model.getFolders().size());
        assertFalse(model.getFolders().contains(folder));
    }
    
    @Test
    public void testGetDefaultFolderForElement() {
        model.addDerivedRelationsFolder();
        
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
        
        element = IArchimateFactory.eINSTANCE.createSketchModel();
        folder = model.getDefaultFolderForElement(element);
        assertNotNull(folder);
        assertEquals(FolderType.DIAGRAMS, folder.getType());
    }
    
    @Test
    public void testGetDefaultFolderForWrongElement() {
        // Null
        IFolder folder = model.getDefaultFolderForElement(null);
        assertNull(folder);
        
        // Folder
        EObject element = IArchimateFactory.eINSTANCE.createFolder();
        folder = model.getDefaultFolderForElement(element);
        assertNull(folder);
        
        element = IArchimateFactory.eINSTANCE.createSketchModelActor();
        folder = model.getDefaultFolderForElement(element);
        assertNull(folder);

        element = IArchimateFactory.eINSTANCE.createSketchModelSticky();
        folder = model.getDefaultFolderForElement(element);
        assertNull(folder);
    }
    
    @Test
    public void testGetFolder() {
        assertNull(model.getFolder(FolderType.BUSINESS));
        assertNull(model.getFolder(FolderType.APPLICATION));
        assertNull(model.getFolder(FolderType.TECHNOLOGY));
        assertNull(model.getFolder(FolderType.CONNECTORS));
        assertNull(model.getFolder(FolderType.RELATIONS));
        assertNull(model.getFolder(FolderType.DIAGRAMS));
        
        model.setDefaults();
        
        assertEquals(FolderType.BUSINESS, model.getFolder(FolderType.BUSINESS).getType());
        assertEquals(FolderType.APPLICATION, model.getFolder(FolderType.APPLICATION).getType());
        assertEquals(FolderType.TECHNOLOGY, model.getFolder(FolderType.TECHNOLOGY).getType());
        assertEquals(FolderType.CONNECTORS, model.getFolder(FolderType.CONNECTORS).getType());
        assertEquals(FolderType.RELATIONS, model.getFolder(FolderType.RELATIONS).getType());
        assertEquals(FolderType.DIAGRAMS, model.getFolder(FolderType.DIAGRAMS).getType());
    }
    
    @Test
    public void testGetAdapter() {
        CommonTests.testGetAdapter(model);
    }

    @Test
    public void testGetDefaultDiagramModel() {
        assertNull(model.getDefaultDiagramModel());
        
        IDiagramModel dm1 = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        model.getDefaultFolderForElement(dm1).getElements().add(dm1);
        IDiagramModel dm2 = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        model.getDefaultFolderForElement(dm2).getElements().add(dm2);
        
        assertSame(dm1, model.getDefaultDiagramModel());
    }

    @Test
    public void testGetDiagramModels() {
        EList<IDiagramModel> list = model.getDiagramModels();
        assertNotNull(list);
        assertTrue(list.isEmpty());
        
        IDiagramModel dm1 = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        model.getDefaultFolderForElement(dm1).getElements().add(dm1);
        IDiagramModel dm2 = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        model.getDefaultFolderForElement(dm2).getElements().add(dm2);
        
        list = model.getDiagramModels();
        assertEquals(2, list.size());
    }
    
    @Test
    public void testGetName() {
        CommonTests.testGetName(model);
    }

    @Test
    public void testGetID() {
        String id = model.getId();
        assertNull(id);
        
        model.setDefaults();
        id = model.getId();
        assertNotNull(id);
        assertEquals(8, id.length());
    }
        
    @Test
    public void testGetArchimateModel() {
        assertSame(model, model.getArchimateModel());
    }

    @Test
    public void testGetProperties() {
        CommonTests.testProperties(model);
    }
    
    @Test
    public void testGetMetadata() {
        IMetadata metadata = model.getMetadata();        
        assertNotNull(metadata);
        assertEquals(0, model.getMetadata().getEntries().size());
    }

    @Test
    public void testGetPurpose() {
        assertEquals(null, model.getPurpose());
        model.setPurpose("name");
        assertEquals("name", model.getPurpose());
    }

    @Test
    public void testGetFolders() {
        assertTrue(model.getFolders().isEmpty());
    }

    @Test
    public void testGetFile() {
        assertNull(model.getFile());
        File file = new File("");
        model.setFile(file);
        assertSame(file, model.getFile());
    }

    @Test
    public void testGetVersion() {
        assertEquals("", model.getVersion());
        model.setVersion("name");
        assertEquals("name", model.getVersion());
    }
    
    @Test
    public void testSetDefaults() {
        model.setDefaults();
        assertNotNull(model.getId());
        assertEquals(8, model.getFolders().size());
    }
    
    // ---------------------------------------------------------------------------------------------
    // ID Adapter
    // ---------------------------------------------------------------------------------------------
   
    @Test
    public void testIDAdapterAddedToArchimateModel() {
        assertTrue(model.eAdapters().get(0) instanceof IDAdapter);
    }
    
    @Test
    public void testIDAddedToElement() {
        IArchimateElement element = IArchimateFactory.eINSTANCE.createApplicationService();
        assertNull(element.getId());
        
        model.getDefaultFolderForElement(element).getElements().add(element);
        String id = element.getId();
        assertNotNull(id);
        assertEquals(8, id.length());
    }

}
