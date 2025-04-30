/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.archimatetool.editor.model.impl.EditorModelManager;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IAssignmentRelationship;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IProfile;
import com.archimatetool.testingtools.ArchimateTestModel;
import com.archimatetool.tests.TestData;

@SuppressWarnings("nls")
public class ModelCheckerTests {
    
    private ArchimateTestModel tm;
    private IArchimateModel model;
    private ModelChecker modelChecker;
    
    @BeforeEach
    public void runBeforeEachTest() {
        tm = new ArchimateTestModel();
        model = tm.createNewModel();
        modelChecker = new ModelChecker(model);
    }
    
    @Test
    public void checkAll() throws Exception {
        File file = TestData.TEST_MODEL_FILE_ARCHISURANCE;
        IArchimateModel model = new EditorModelManager().load(file);
        assertNotNull(model);
        
        ModelChecker modelChecker = new ModelChecker(model);
        assertTrue(modelChecker.checkAll());
    }
    
    @Test
    public void checkModel() throws Exception {
        model.setId(null); // Check model has ID
        assertFalse(modelChecker.checkAll());
        assertEquals(1, modelChecker.getErrorMessages().size());
    }

    @Test
    public void checkFolderStructure() {
        List<String> messages = modelChecker.checkFolderStructure();
        assertTrue(messages.isEmpty());
        
        model.getFolders().remove(0);
        messages = modelChecker.checkFolderStructure();
        assertEquals(1, messages.size());
        assertEquals("Strategy Folder Missing", messages.get(0));
        
        model.getFolders().remove(0);
        messages = modelChecker.checkFolderStructure();
        assertEquals(2, messages.size());
        assertEquals("Business Folder Missing", messages.get(1));
    }
    
    @Test
    public void checkHasIdentifier() {
        IArchimateConcept concept = IArchimateFactory.eINSTANCE.createBusinessObject();
        concept.setName("Test");
        
        List<String> messages = modelChecker.checkHasIdentifier(concept);
        assertTrue(messages.isEmpty());
        
        concept.setId(null);
        messages = modelChecker.checkHasIdentifier(concept);
        assertEquals(1, messages.size());
        assertEquals("No identifier set on 'Test'", messages.get(0));
    }

    @Test
    public void checkObjectInCorrectFolder_Element() {
        IArchimateConcept concept = IArchimateFactory.eINSTANCE.createBusinessActor();
        IFolder subFolder = IArchimateFactory.eINSTANCE.createFolder();
        subFolder.getElements().add(concept);
        model.getFolder(FolderType.APPLICATION).getElements().add(subFolder);
        
        List<String> messages = modelChecker.checkObjectInCorrectFolder(concept);
        assertEquals(1, messages.size());
        assertTrue(messages.get(0).startsWith("Object is in wrong folder type"));
    }
    
    @Test
    public void checkObjectInCorrectFolder_Relationship() {
        IArchimateConcept concept = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        IFolder subFolder = IArchimateFactory.eINSTANCE.createFolder();
        subFolder.getElements().add(concept);
        model.getFolder(FolderType.APPLICATION).getElements().add(subFolder);
        
        List<String> messages = modelChecker.checkObjectInCorrectFolder(concept);
        assertEquals(1, messages.size());
        assertTrue(messages.get(0).startsWith("Object is in wrong folder type"));
    }
    
    @Test
    public void checkObjectInCorrectFolder_DiagramModel() {
        IDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        IFolder subFolder = IArchimateFactory.eINSTANCE.createFolder();
        subFolder.getElements().add(dm);
        model.getFolder(FolderType.APPLICATION).getElements().add(subFolder);
        
        List<String> messages = modelChecker.checkObjectInCorrectFolder(dm);
        assertEquals(1, messages.size());
        assertTrue(messages.get(0).startsWith("Object is in wrong folder type"));
    }

    @Test
    public void checkRelationship() {
        IArchimateElement src = (IArchimateElement)tm.createModelElementAndAddToModel(IArchimatePackage.eINSTANCE.getBusinessActor());
        IArchimateElement tgt = (IArchimateElement)tm.createModelElementAndAddToModel(IArchimatePackage.eINSTANCE.getBusinessActor());
        IArchimateRelationship relation = (IArchimateRelationship)tm.createModelElementAndAddToModel(IArchimatePackage.eINSTANCE.getAssociationRelationship());
        relation.setSource(src);
        relation.setTarget(tgt);

        List<String> messages = modelChecker.checkRelationship(relation);
        assertTrue(messages.isEmpty());
        
        relation.setSource(null);
        relation.setTarget(null);
        
        messages = modelChecker.checkRelationship(relation);
        assertEquals(2, messages.size());
        assertTrue(messages.get(0).startsWith("Relationship has missing referenced source element"));
        assertTrue(messages.get(1).startsWith("Relationship has missing referenced target element"));
        
        relation.setSource(src);
        relation.setTarget(tgt);
        model.getFolder(FolderType.BUSINESS).getElements().remove(src);
        model.getFolder(FolderType.BUSINESS).getElements().remove(tgt);
        
        messages = modelChecker.checkRelationship(relation);
        assertEquals(2, messages.size());
        assertTrue(messages.get(0).startsWith("Relationship has orphaned source element"));
        assertTrue(messages.get(1).startsWith("Relationship has orphaned target element"));
    }

    @Test
    public void checkDiagramModelArchimateObject() {
        model.getDefaultDiagramModel().setName("dm");
        
        IArchimateElement element = IArchimateFactory.eINSTANCE.createBusinessActor();
        model.getFolder(FolderType.BUSINESS).getElements().add(element);
        IDiagramModelArchimateObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo.setArchimateElement(element);
        model.getDefaultDiagramModel().getChildren().add(dmo);
        
        List<String> messages = modelChecker.checkDiagramModelArchimateObject(dmo);
        assertTrue(messages.isEmpty());
        
        dmo.setArchimateElement(null);
        messages = modelChecker.checkDiagramModelArchimateObject(dmo);
        assertEquals(1, messages.size());
        assertTrue(messages.get(0).startsWith("Diagram Element has missing referenced ArchiMate element in 'dm'"));

        dmo.setArchimateElement(element);
        model.getFolder(FolderType.BUSINESS).getElements().remove(element);
        
        messages = modelChecker.checkDiagramModelArchimateObject(dmo);
        assertEquals(1, messages.size());
        assertTrue(messages.get(0).startsWith("Diagram Element has orphaned ArchiMate element in 'dm'"));
    }
    
    @Test
    public void checkDiagramModelArchimateConnection() {
        model.getDefaultDiagramModel().setName("dm");
        
        IArchimateElement actor = IArchimateFactory.eINSTANCE.createBusinessActor();
        IDiagramModelArchimateObject dmo1 = tm.createDiagramModelArchimateObjectAndAddToModel(actor);
        model.getDefaultDiagramModel().getChildren().add(dmo1);
        
        IArchimateElement role = IArchimateFactory.eINSTANCE.createBusinessRole();
        IDiagramModelArchimateObject dmo2 = tm.createDiagramModelArchimateObjectAndAddToModel(role);
        model.getDefaultDiagramModel().getChildren().add(dmo2);
        
        IAssignmentRelationship relation = IArchimateFactory.eINSTANCE.createAssignmentRelationship();
        relation.setSource(actor);
        relation.setTarget(role);
        IDiagramModelArchimateConnection dmc1 = tm.createDiagramModelArchimateConnectionAndAddToModel(relation);
        dmc1.connect(dmo1, dmo2);
        
        List<String> messages = modelChecker.checkDiagramModelArchimateConnection(dmc1);
        assertTrue(messages.isEmpty());
        
        model.getFolder(FolderType.RELATIONS).getElements().remove(relation);
        model.getFolder(FolderType.BUSINESS).getElements().remove(actor);
        model.getFolder(FolderType.BUSINESS).getElements().remove(role);
        
        messages = modelChecker.checkDiagramModelArchimateConnection(dmc1);
        assertEquals(3, messages.size());
        assertTrue(messages.get(0).startsWith("Diagram Connection has orphaned ArchiMate relation in 'dm'"));
        assertTrue(messages.get(1).startsWith("Diagram Connection has orphaned ArchiMate source element in 'dm'"));
        assertTrue(messages.get(2).startsWith("Diagram Connection has orphaned ArchiMate target element in 'dm'"));
    }
    
    @Test
    public void checkFolderContainsCorrectObjects() {
        IFolder folder = IArchimateFactory.eINSTANCE.createFolder();
        folder.getElements().add(IArchimateFactory.eINSTANCE.createBusinessObject());
        folder.getElements().add(IArchimateFactory.eINSTANCE.createAccessRelationship());
        folder.getElements().add(IArchimateFactory.eINSTANCE.createArchimateDiagramModel());
        
        List<String> messages = modelChecker.checkFolderContainsCorrectObjects(folder);
        assertTrue(messages.isEmpty());
        
        IFolder object = IArchimateFactory.eINSTANCE.createFolder();
        folder.getElements().add(object);
        
        messages = modelChecker.checkFolderContainsCorrectObjects(folder);
        assertEquals(1, messages.size());
        assertEquals("Folder contains wrong child object (Folder: " + folder.getId() + " Object: " + object.getId() + ")", messages.get(0));
    }
    
    @Test
    public void checkProfiles() {
        IArchimateElement element = IArchimateFactory.eINSTANCE.createBusinessActor();
        model.getFolder(FolderType.BUSINESS).getElements().add(element);
        
        IProfile profile = IArchimateFactory.eINSTANCE.createProfile();
        profile.setName("Specialization");
        profile.setConceptType(element.eClass().getName());
        
        model.getProfiles().add(profile);
        element.getProfiles().add(profile);
        
        List<String> messages = modelChecker.checkProfiles(element);
        assertTrue(messages.isEmpty());
        
        // Remove from model
        model.getProfiles().remove(profile);
        
        messages = modelChecker.checkProfiles(element);
        assertEquals(1, messages.size());
        assertTrue(messages.get(0).startsWith("Profile reference is orphaned"));
        
        // Wrong concept type
        profile.setConceptType(IArchimatePackage.eINSTANCE.getArtifact().getName());
        messages = modelChecker.checkProfiles(element);
        assertEquals(2, messages.size());
        assertTrue(messages.get(1).startsWith("Profile has wrong concept type"));
    }
    
    @Test
    public void checkObject() {
        List<String> messages = modelChecker.checkObject(IArchimateFactory.eINSTANCE.createBusinessActor());
        assertTrue(messages.isEmpty());
        
        ModelChecker extendedChecker = new ModelChecker(model) {
            @Override
            protected List<String> checkObject(EObject eObject) {
                return List.of("New Error");
            }
        };
        
        assertFalse(extendedChecker.checkAll());
        assertTrue(extendedChecker.getErrorMessages().get(0).equals("New Error"));
    }
}