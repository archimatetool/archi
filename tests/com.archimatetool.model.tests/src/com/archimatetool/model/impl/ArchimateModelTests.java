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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.util.IModelContentListener;

import junit.framework.JUnit4TestAdapter;


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
    public void addModelContentListener() {
        model.setDefaults();
        
        assertTrue(model.eAdapters().isEmpty());

        assertFalse(model.addModelContentListener(null));
        
        IModelContentListener listener = (notification) ->  {
            assertEquals(model.getFolder(FolderType.DIAGRAMS), notification.getNotifier());
        };
        
        assertTrue(model.addModelContentListener(listener));
        assertFalse(model.addModelContentListener(listener)); // do it twice
        
        assertEquals(1, model.eAdapters().size()); // should be only one
        
        model.getFolder(FolderType.DIAGRAMS).getElements().add(IArchimateFactory.eINSTANCE.createArchimateDiagramModel());
        
        // Disposed
        model.dispose();
        assertFalse(model.addModelContentListener(listener));
    }
    
    @Test
    public void removeModelContentListener() {
        assertFalse(model.removeModelContentListener(null));
        
        IModelContentListener listener = (notification) ->  {};
        
        assertFalse(model.removeModelContentListener(listener));
        
        model.addModelContentListener(listener);
        
        assertTrue(model.removeModelContentListener(listener));
        assertFalse(model.removeModelContentListener(listener));

        // Disposed
        model.dispose();
        assertFalse(model.removeModelContentListener(listener));
    }

    @Test
    public void addDefaultFolders() {
        // No folders by default
        EList<IFolder> list = model.getFolders();
        assertEquals(0, list.size());
        
        model.addDefaultFolders();
        list = model.getFolders();
        
        // Correct number of folders
        assertEquals(9, list.size());
        
        // Types
        assertEquals(FolderType.STRATEGY, list.get(0).getType());
        assertEquals(FolderType.BUSINESS, list.get(1).getType());
        assertEquals(FolderType.APPLICATION, list.get(2).getType());
        assertEquals(FolderType.TECHNOLOGY, list.get(3).getType());
        assertEquals(FolderType.MOTIVATION, list.get(4).getType());
        assertEquals(FolderType.IMPLEMENTATION_MIGRATION, list.get(5).getType());
        assertEquals(FolderType.OTHER, list.get(6).getType());
        assertEquals(FolderType.RELATIONS, list.get(7).getType());
        assertEquals(FolderType.DIAGRAMS, list.get(8).getType());
        
        // Test can't do it twice
        model.addDefaultFolders();
        list = model.getFolders();
        assertEquals(9, list.size());
    }
    
    @Test
    public void getDefaultFolderForObject() {
        EObject element = IArchimateFactory.eINSTANCE.createResource();
        IFolder folder = model.getDefaultFolderForObject(element);
        assertNotNull(folder);
        assertEquals(FolderType.STRATEGY, folder.getType());
        
        element = IArchimateFactory.eINSTANCE.createBusinessEvent();
        folder = model.getDefaultFolderForObject(element);
        assertNotNull(folder);
        assertEquals(FolderType.BUSINESS, folder.getType());
        
        element = IArchimateFactory.eINSTANCE.createApplicationComponent();
        folder = model.getDefaultFolderForObject(element);
        assertNotNull(folder);
        assertEquals(FolderType.APPLICATION, folder.getType());
        
        element = IArchimateFactory.eINSTANCE.createNode();
        folder = model.getDefaultFolderForObject(element);
        assertNotNull(folder);
        assertEquals(FolderType.TECHNOLOGY, folder.getType());
        
        element = IArchimateFactory.eINSTANCE.createEquipment();
        folder = model.getDefaultFolderForObject(element);
        assertNotNull(folder);
        assertEquals(FolderType.TECHNOLOGY, folder.getType());

        element = IArchimateFactory.eINSTANCE.createJunction();
        folder = model.getDefaultFolderForObject(element);
        assertNotNull(folder);
        assertEquals(FolderType.OTHER, folder.getType());
        
        element = IArchimateFactory.eINSTANCE.createAssignmentRelationship();
        folder = model.getDefaultFolderForObject(element);
        assertNotNull(folder);
        assertEquals(FolderType.RELATIONS, folder.getType());
        
        element = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        folder = model.getDefaultFolderForObject(element);
        assertNotNull(folder);
        assertEquals(FolderType.DIAGRAMS, folder.getType());
        
        element = IArchimateFactory.eINSTANCE.createSketchModel();
        folder = model.getDefaultFolderForObject(element);
        assertNotNull(folder);
        assertEquals(FolderType.DIAGRAMS, folder.getType());
    }
    
    @Test
    public void getDefaultFolderForWrongElement() {
        // Null
        IFolder folder = model.getDefaultFolderForObject(null);
        assertNull(folder);
        
        // Folder
        EObject element = IArchimateFactory.eINSTANCE.createFolder();
        folder = model.getDefaultFolderForObject(element);
        assertNull(folder);
        
        element = IArchimateFactory.eINSTANCE.createSketchModelActor();
        folder = model.getDefaultFolderForObject(element);
        assertNull(folder);

        element = IArchimateFactory.eINSTANCE.createSketchModelSticky();
        folder = model.getDefaultFolderForObject(element);
        assertNull(folder);
    }
    
    @Test
    public void getFolder() {
        assertNull(model.getFolder(FolderType.STRATEGY));
        assertNull(model.getFolder(FolderType.BUSINESS));
        assertNull(model.getFolder(FolderType.APPLICATION));
        assertNull(model.getFolder(FolderType.TECHNOLOGY));
        assertNull(model.getFolder(FolderType.OTHER));
        assertNull(model.getFolder(FolderType.RELATIONS));
        assertNull(model.getFolder(FolderType.DIAGRAMS));
        
        model.setDefaults();
        
        assertEquals(FolderType.STRATEGY, model.getFolder(FolderType.STRATEGY).getType());
        assertEquals(FolderType.BUSINESS, model.getFolder(FolderType.BUSINESS).getType());
        assertEquals(FolderType.APPLICATION, model.getFolder(FolderType.APPLICATION).getType());
        assertEquals(FolderType.TECHNOLOGY, model.getFolder(FolderType.TECHNOLOGY).getType());
        assertEquals(FolderType.OTHER, model.getFolder(FolderType.OTHER).getType());
        assertEquals(FolderType.RELATIONS, model.getFolder(FolderType.RELATIONS).getType());
        assertEquals(FolderType.DIAGRAMS, model.getFolder(FolderType.DIAGRAMS).getType());
    }
    
    @Test
    public void getAdapter() {
        CommonTests.testGetAdapter(model);
    }

    @Test
    public void getDefaultDiagramModel() {
        assertNull(model.getDefaultDiagramModel());
        
        IDiagramModel dm1 = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        model.getDefaultFolderForObject(dm1).getElements().add(dm1);
        IDiagramModel dm2 = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        model.getDefaultFolderForObject(dm2).getElements().add(dm2);
        
        assertSame(dm1, model.getDefaultDiagramModel());
    }

    @Test
    public void getDiagramModels() {
        model.addDefaultFolders();
        
        EList<IDiagramModel> list = model.getDiagramModels();
        assertNotNull(list);
        assertTrue(list.isEmpty());
        
        IFolder folder1 = model.getFolder(FolderType.DIAGRAMS);
        
        IDiagramModel dm1 = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        folder1.getElements().add(dm1);
        IDiagramModel dm2 = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        folder1.getElements().add(dm2);
        
        list = model.getDiagramModels();
        assertEquals(2, list.size());
        
        IFolder folder2 = IArchimateFactory.eINSTANCE.createFolder();
        folder1.getFolders().add(folder2);
        
        IDiagramModel dm3 = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        folder2.getElements().add(dm3);
        
        IFolder folder3 = IArchimateFactory.eINSTANCE.createFolder();
        folder2.getFolders().add(folder3);
        
        IDiagramModel dm4 = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        folder3.getElements().add(dm4);
        
        list = model.getDiagramModels();
        assertEquals(4, list.size());
    }
    
    @Test
    public void getName() {
        CommonTests.testGetName(model);
    }

    @Test
    public void getIDNotNull() {
        assertNotNull(model.getId());
    }
        
    @Test
    public void getArchimateModelSame() {
        assertSame(model, model.getArchimateModel());
    }

    @Test
    public void getProperties() {
        CommonTests.testProperties(model);
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void getMetadata() {
        assertEquals(null, model.getMetadata());
        model.setMetadata(IArchimateFactory.eINSTANCE.createMetadata());
        assertNotNull(model.getMetadata());
    }

    @Test
    public void getPurpose() {
        assertEquals("", model.getPurpose());
        model.setPurpose("name");
        assertEquals("name", model.getPurpose());
    }

    @Test
    public void getFoldersIsEmpty() {
        assertTrue(model.getFolders().isEmpty());
    }

    @Test
    public void getFile() {
        assertNull(model.getFile());
        File file = new File("");
        model.setFile(file);
        assertSame(file, model.getFile());
    }

    @Test
    public void getVersion() {
        assertEquals("", model.getVersion());
        model.setVersion("1.2");
        assertEquals("1.2", model.getVersion());
    }
    
    @Test
    public void setDefaults() {
        model.setDefaults();
        assertNotNull(model.getId());
        assertEquals(9, model.getFolders().size());
    }
}
