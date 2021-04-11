/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;

import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IProfile;

import junit.framework.JUnit4TestAdapter;



/**
 * ArchimateModelUtils Tests
 *
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class ArchimateModelUtilsTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ArchimateModelUtilsTests.class);
    }
    
    /*
     * NOTE - Tests for ArchimateModelUtils.isValidRelationshipStart() and ArchimateModelUtils.isValidRelationship() are in ViewpointTests
     */
    
    @Test
    public void testHasDirectRelationship() {
        IArchimateConcept sourceConcept = IArchimateFactory.eINSTANCE.createBusinessActor();
        IArchimateConcept someTarget = IArchimateFactory.eINSTANCE.createBusinessRole();

        IArchimateRelationship relationship = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        
        assertFalse(ArchimateModelUtils.hasDirectRelationship(sourceConcept, relationship));
        assertFalse(ArchimateModelUtils.hasDirectRelationship(relationship, sourceConcept));
        
        relationship.connect(sourceConcept, someTarget);
        
        assertTrue(ArchimateModelUtils.hasDirectRelationship(sourceConcept, relationship));
        assertTrue(ArchimateModelUtils.hasDirectRelationship(relationship, sourceConcept));
        
        relationship.connect(someTarget, sourceConcept);
        
        assertTrue(ArchimateModelUtils.hasDirectRelationship(sourceConcept, relationship));
        assertTrue(ArchimateModelUtils.hasDirectRelationship(relationship, sourceConcept));
    }
    
    @Test
    public void testGetValidRelationships() {
        EClass sourceClass = IArchimatePackage.eINSTANCE.getBusinessActor();
        EClass targetClass = IArchimatePackage.eINSTANCE.getBusinessRole();

        EClass[] classes = ArchimateModelUtils.getValidRelationships(sourceClass, targetClass);
        assertEquals(5, classes.length);
        
        // The order of these is set in ArchimateModelUtils.getRelationsClasses()
        assertEquals(IArchimatePackage.eINSTANCE.getAssignmentRelationship(), classes[0]);
        assertEquals(IArchimatePackage.eINSTANCE.getServingRelationship(), classes[1]);
        assertEquals(IArchimatePackage.eINSTANCE.getTriggeringRelationship(), classes[2]);
        assertEquals(IArchimatePackage.eINSTANCE.getFlowRelationship(), classes[3]);
        assertEquals(IArchimatePackage.eINSTANCE.getAssociationRelationship(), classes[4]);
        
        // How much more can we test this...?
    }
    
    @Test
    public void testGetAllRelationshipsForConcept_NotNull() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();

        IArchimateElement element1 = IArchimateFactory.eINSTANCE.createBusinessActor();
        model.getDefaultFolderForObject(element1).getElements().add(element1);
        
        IArchimateElement element2 = IArchimateFactory.eINSTANCE.createBusinessRole();
        model.getDefaultFolderForObject(element2).getElements().add(element2);
        
        assertNotNull(ArchimateModelUtils.getAllRelationshipsForConcept(element1));
        assertNotNull(ArchimateModelUtils.getAllRelationshipsForConcept(element2));
    }

    @Test
    public void testGetAllRelationshipsForConcept_NoRelations() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();

        IArchimateElement element1 = IArchimateFactory.eINSTANCE.createBusinessActor();
        model.getDefaultFolderForObject(element1).getElements().add(element1);
        
        IArchimateElement element2 = IArchimateFactory.eINSTANCE.createBusinessRole();
        model.getDefaultFolderForObject(element2).getElements().add(element2);
        
        assertTrue(ArchimateModelUtils.getAllRelationshipsForConcept(element1).isEmpty());
        assertTrue(ArchimateModelUtils.getAllRelationshipsForConcept(element2).isEmpty());
    }

    @Test
    public void testGetAllRelationshipsForConcept() {
        IArchimateElement element1 = IArchimateFactory.eINSTANCE.createBusinessActor();
        IArchimateElement element2 = IArchimateFactory.eINSTANCE.createBusinessRole();
        
        IArchimateRelationship relation1 = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        relation1.setSource(element1);
        relation1.setTarget(element2);
        
        assertEquals(1, ArchimateModelUtils.getAllRelationshipsForConcept(element1).size());
        assertEquals(1, ArchimateModelUtils.getAllRelationshipsForConcept(element2).size());

        IArchimateRelationship relation2 = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        relation2.setSource(element1);
        relation2.setTarget(element2);
       
        assertEquals(2, ArchimateModelUtils.getAllRelationshipsForConcept(element1).size());
        assertEquals(2, ArchimateModelUtils.getAllRelationshipsForConcept(element2).size());
    }
    
    @Test
    public void testGetObjectByID() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        
        EObject element = ArchimateModelUtils.getObjectByID(model, null);
        assertNull(element);
        
        IArchimateElement newElement1 = IArchimateFactory.eINSTANCE.createApplicationFunction();
        model.getDefaultFolderForObject(newElement1).getElements().add(newElement1);
        IArchimateElement newElement2 = IArchimateFactory.eINSTANCE.createBusinessActor();
        model.getDefaultFolderForObject(newElement2).getElements().add(newElement2);
        
        element = ArchimateModelUtils.getObjectByID(model, newElement1.getId());
        assertSame(newElement1, element);
        
        element = ArchimateModelUtils.getObjectByID(model, newElement2.getId());
        assertSame(newElement2, element);
    }
    
    @Test
    public void testGetStrategyClasses() {
        EClass[] classes = ArchimateModelUtils.getStrategyClasses();
        assertEquals(4, classes.length);
        
        for(EClass eClass : classes) {
            assertTrue(IArchimatePackage.eINSTANCE.getStrategyElement().isSuperTypeOf(eClass));
        }
    }
    
    @Test
    public void testGetBusinessClasses() {
        EClass[] classes = ArchimateModelUtils.getBusinessClasses();
        assertEquals(13, classes.length);
        
        for(EClass eClass : classes) {
            assertTrue(IArchimatePackage.eINSTANCE.getBusinessElement().isSuperTypeOf(eClass));
        }
    }
    
    @Test
    public void testGetApplicationClasses() {
        EClass[] classes = ArchimateModelUtils.getApplicationClasses();
        assertEquals(9, classes.length);
        
        for(EClass eClass : classes) {
            assertTrue(IArchimatePackage.eINSTANCE.getApplicationElement().isSuperTypeOf(eClass));
        }
    }

    @Test
    public void testGetTechnologyClasses() {
        EClass[] classes = ArchimateModelUtils.getTechnologyClasses();
        assertEquals(13, classes.length);
        
        for(EClass eClass : classes) {
            assertTrue(IArchimatePackage.eINSTANCE.getTechnologyElement().isSuperTypeOf(eClass));
        }
    }
    
    @Test
    public void testGetPhysicalClasses() {
        EClass[] classes = ArchimateModelUtils.getPhysicalClasses();
        assertEquals(4, classes.length);
        
        for(EClass eClass : classes) {
            assertTrue(IArchimatePackage.eINSTANCE.getPhysicalElement().isSuperTypeOf(eClass));
        }
    }
    
    @Test
    public void testGetMotivationClasses() {
        EClass[] classes = ArchimateModelUtils.getMotivationClasses();
        assertEquals(10, classes.length);
        
        for(EClass eClass : classes) {
            assertTrue(IArchimatePackage.eINSTANCE.getMotivationElement().isSuperTypeOf(eClass));
        }
    }

    @Test
    public void testGetImplementationMigrationClasses() {
        EClass[] classes = ArchimateModelUtils.getImplementationMigrationClasses();
        assertEquals(5, classes.length);
        
        for(EClass eClass : classes) {
            assertTrue(IArchimatePackage.eINSTANCE.getImplementationMigrationElement().isSuperTypeOf(eClass));
        }
    }
    
    @Test
    public void testGetOtherClasses() {
        EClass[] classes = ArchimateModelUtils.getOtherClasses();
        assertEquals(2, classes.length);

        for(EClass eClass : classes) {
            assertTrue(IArchimatePackage.eINSTANCE.getArchimateElement().isSuperTypeOf(eClass));
        }
    }

    @Test
    public void testGetRelationsClasses() {
        EClass[] classes = ArchimateModelUtils.getRelationsClasses();
        assertEquals(11, classes.length);
        
        for(EClass eClass : classes) {
            assertTrue(IArchimatePackage.eINSTANCE.getArchimateRelationship().isSuperTypeOf(eClass));
        }
    }
    
    @Test
    public void testGetConnectorClasses() {
        EClass[] classes = ArchimateModelUtils.getConnectorClasses();
        assertEquals(1, classes.length);
    }
    
    @Test
    public void testGetAllArchimateClasses() {
        EClass[] classes = ArchimateModelUtils.getAllArchimateClasses();
        assertEquals(60, classes.length);
        
        for(EClass eClass : classes) {
            assertTrue(IArchimatePackage.eINSTANCE.getArchimateElement().isSuperTypeOf(eClass));
        }
    }
    
    @Test
    public void hasProfileByNameAndType_Model() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();
        
        String name1 = "profile";
        String name2 = "profile2";
        String conceptType1 = IArchimatePackage.eINSTANCE.getBusinessActor().getName();
        String conceptType2 = IArchimatePackage.eINSTANCE.getBusinessRole().getName();
        
        assertFalse(ArchimateModelUtils.hasProfileByNameAndType(model, name1, conceptType1));
        
        IProfile profile = IArchimateFactory.eINSTANCE.createProfile();
        profile.setName(name1);
        profile.setConceptType(conceptType1);
        model.getProfiles().add(profile);
        
        assertTrue(ArchimateModelUtils.hasProfileByNameAndType(model, name1, conceptType1));
        
        profile.setConceptType(conceptType2);
        assertFalse(ArchimateModelUtils.hasProfileByNameAndType(model, name1, conceptType1));
        
        profile.setName(name2);
        profile.setConceptType(conceptType1);
        assertFalse(ArchimateModelUtils.hasProfileByNameAndType(model, name1, conceptType1));
    }
} 
