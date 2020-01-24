/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.modelimporter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IFolder;
import com.archimatetool.modelimporter.ModelImporter;
import com.archimatetool.testingtools.ArchimateTestModel;

import junit.framework.JUnit4TestAdapter;


/**
 * Importer Tests
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class ModelImporterTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ModelImporterTests.class);
    }
    
    @Test
    public void importModel_Initialised() {
        ModelImporter importer = new ModelImporter();
        
        assertFalse(importer.shouldUpdate());
        assertFalse(importer.shouldUpdateAll());
        assertNull(importer.getImportedModel());
        assertNull(importer.getTargetModel());
    }

    @Test
    public void doImport_Update() throws Exception {
        ModelImporter importer = new ModelImporter();
        importer.setUpdate(true);
        
        ArchimateTestModel tm = new ArchimateTestModel();
        IArchimateModel model = tm.createNewModel();
        model.setName("My Model");
        model.setPurpose("My Purpose");
        
        importer.doImport(TestData.TEST_MODEL_FILE, model);
        
        // Model root should be the same
        assertEquals("My Model", model.getName());
        assertEquals("My Purpose", model.getPurpose());
        assertEquals(0, model.getProperties().size());
        
        // Top-level folder should be the same
        IFolder businessFolder = model.getFolder(FolderType.BUSINESS);
        assertEquals("", businessFolder.getDocumentation());
        assertEquals(0, businessFolder.getProperties().size());
        
        // Concept in top folder
        IArchimateElement element1 = (IArchimateElement)businessFolder.getElements().get(0);
        assertEquals("BR2", element1.getName());
        assertEquals("BR2 Documentation", element1.getDocumentation());
        assertEquals(1, element1.getProperties().size());
        
        // Sub-folder should be updated
        IFolder businessSubFolder = businessFolder.getFolders().get(0);
        assertEquals("Folder1", businessSubFolder.getName());
        assertEquals("Folder1 Documentation", businessSubFolder.getDocumentation());
        assertEquals(1, businessSubFolder.getProperties().size());
        assertEquals(2, businessSubFolder.getElements().size());
        
        // Concepts in sub-folder
        IArchimateElement element2 = (IArchimateElement)businessSubFolder.getElements().get(0);
        assertEquals("BA1", element2.getName());
        assertEquals("BA1 Documentation", element2.getDocumentation());
        assertEquals(1, element2.getProperties().size());
        
        IArchimateElement element3 = (IArchimateElement)businessSubFolder.getElements().get(1);
        assertEquals("BR1", element3.getName());
        assertEquals("BR1 Documentation", element3.getDocumentation());
        assertEquals(1, element3.getProperties().size());
        
        // Relationship
        IFolder relationsFolder = model.getFolder(FolderType.RELATIONS);
        
        IArchimateRelationship relation1 = (IArchimateRelationship)relationsFolder.getElements().get(0);
        assertEquals("Rel1", relation1.getName());
        assertEquals("Rel1 Documentation", relation1.getDocumentation());
        assertEquals(1, relation1.getProperties().size());
        
        // Views
        IFolder viewsFolder = model.getFolder(FolderType.DIAGRAMS);
        assertEquals(3, viewsFolder.getElements().size());
    }
    
    @Test
    public void doImport_UpdateAll() throws Exception {
        ModelImporter importer = new ModelImporter();
        importer.setUpdate(true);
        importer.setUpdateAll(true);
        
        ArchimateTestModel tm = new ArchimateTestModel();
        IArchimateModel model = tm.createNewModel();
        model.setName("My Model");
        model.setPurpose("My Purpose");
        
        importer.doImport(TestData.TEST_MODEL_FILE, model);
        
        // Model root should be updated
        assertEquals("Test Model", model.getName());
        assertEquals("Test Purpose", model.getPurpose());
        assertEquals(1, model.getProperties().size());
        
        // Top-level folder should be updated
        IFolder businessFolder = model.getFolder(FolderType.BUSINESS);
        assertEquals("Business Documentation", businessFolder.getDocumentation());
        assertEquals(1, businessFolder.getProperties().size());
    }
    
    @Test
    public void doImport_NoUpdate() throws Exception {
        ModelImporter importer = new ModelImporter();
        
        ArchimateTestModel tm = new ArchimateTestModel(TestData.TEST_MODEL_FILE);
        IArchimateModel model = tm.loadModelWithCommandStack();
        model.setName("My Model");
        model.setPurpose("My Purpose");
        
        // Top-level folder should be the same
        IFolder businessFolder = model.getFolder(FolderType.BUSINESS);
        businessFolder.setDocumentation("My Documentation");

        // Concept in top folder
        IArchimateElement element1 = (IArchimateElement)businessFolder.getElements().get(0);
        element1.setName("My BR2");
        element1.setDocumentation("My Documentation");
        
        // Sub-folder should be updated
        IFolder businessSubFolder = businessFolder.getFolders().get(0);
        businessSubFolder.setName("My Folder");
        businessSubFolder.setDocumentation("My Documentation");
        
        // Concepts in sub-folder
        IArchimateElement element2 = (IArchimateElement)businessSubFolder.getElements().get(0);
        element2.setName("My BA1");
        element2.setDocumentation("My Documentation");
        
        IArchimateElement element3 = (IArchimateElement)businessSubFolder.getElements().get(1);
        element3.setName("My BR1");
        element3.setDocumentation("My Documentation");
        
        // Relationship
        IFolder relationsFolder = model.getFolder(FolderType.RELATIONS);
        
        IArchimateRelationship relation1 = (IArchimateRelationship)relationsFolder.getElements().get(0);
        relation1.setName("My Rel1");
        relation1.setDocumentation("My Documentation");
        
        // Do import
        importer.doImport(TestData.TEST_MODEL_FILE, model);
        
        // Model root should be the same
        assertEquals("My Model", model.getName());
        assertEquals("My Purpose", model.getPurpose());
        assertEquals(1, model.getProperties().size());
        
        // Business Folder
        assertEquals("My Documentation", businessFolder.getDocumentation());
        assertEquals(1, businessFolder.getProperties().size());

        // Element 1
        assertEquals("My BR2", element1.getName());
        assertEquals("My Documentation", element1.getDocumentation());
        assertEquals(1, element1.getProperties().size());

        // Sub folder
        assertEquals("My Folder", businessSubFolder.getName());
        assertEquals("My Documentation", businessSubFolder.getDocumentation());
        assertEquals(1, businessSubFolder.getProperties().size());
        assertEquals(2, businessSubFolder.getElements().size());
        
        // Concepts in sub-folder
        assertEquals("My BA1", element2.getName());
        assertEquals("My Documentation", element2.getDocumentation());
        assertEquals(1, element2.getProperties().size());
        
        assertEquals("My BR1", element3.getName());
        assertEquals("My Documentation", element3.getDocumentation());
        assertEquals(1, element3.getProperties().size());
        
        // Relationship
        assertEquals("My Rel1", relation1.getName());
        assertEquals("My Documentation", relation1.getDocumentation());
        assertEquals(1, relation1.getProperties().size());
        
        // Views
        IFolder viewsFolder = model.getFolder(FolderType.DIAGRAMS);
        assertEquals(2, viewsFolder.getElements().size());
    }

}
