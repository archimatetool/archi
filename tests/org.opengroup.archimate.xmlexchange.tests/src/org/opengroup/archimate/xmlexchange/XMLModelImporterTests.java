/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package org.opengroup.archimate.xmlexchange;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.archimatetool.editor.model.ModelChecker;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IFolder;

/**
 * XML Model Importer Tests
 * 
 * @author Phillip Beauvoir
 */
public class XMLModelImporterTests {
    
    private XMLModelImporter importer;
    
    @BeforeEach
    public void runOnceBeforeEachTest() {
        importer = new XMLModelImporter();
    }

    @Test
    public void testArchimateModelExists() throws Exception {
        IArchimateModel model = importer.createArchiMateModel(TestSupport.XML_FILE1);
        
        assertNotNull(model);
        
        // Check Model
        ModelChecker checker = new ModelChecker(model);
        checker.checkAll();
        
        // Model has default folders
        assertFalse(model.getFolders().isEmpty());
        
        // Model has name
        assertNotNull(model.getName());
    }
    
    @Test
    public void testArchimateModelHasCorrectElementsAndRelations() throws Exception {
        IArchimateModel model = importer.createArchiMateModel(TestSupport.XML_FILE1);
        
        IFolder businessFolder = model.getFolder(FolderType.BUSINESS);
        IFolder relationsFolder = model.getFolder(FolderType.RELATIONS);
        
        assertEquals(2, businessFolder.getElements().size());
        assertEquals(1, relationsFolder.getElements().size());
        
        IArchimateElement element1 = (IArchimateElement)businessFolder.getElements().get(0);
        IArchimateElement element2 = (IArchimateElement)businessFolder.getElements().get(1);
        
        assertEquals(IArchimatePackage.eINSTANCE.getBusinessRole(), element1.eClass());
        assertEquals(IArchimatePackage.eINSTANCE.getBusinessProcess(), element2.eClass());
        
        IArchimateRelationship relation = (IArchimateRelationship)relationsFolder.getElements().get(0);
        assertEquals(IArchimatePackage.eINSTANCE.getAssignmentRelationship(), relation.eClass());
        assertEquals(element1, relation.getSource());
        assertEquals(element2, relation.getTarget());
    }
    
    @Test
    public void testImportValid() throws Exception {
        // Export to XML
        File outputFile = XMLModelExporterTests.export(TestSupport.TEST_MODEL_FILE_ARCHISURANCE);
        
        // Import from XML
        IArchimateModel model = importer.createArchiMateModel(outputFile);
        
        // Check Model
        ModelChecker checker = new ModelChecker(model);
        checker.checkAll();
    }
}
