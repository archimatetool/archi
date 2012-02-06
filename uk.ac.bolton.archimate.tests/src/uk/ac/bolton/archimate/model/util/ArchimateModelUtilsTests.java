/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.model.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.eclipse.emf.ecore.EObject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IArchimateFactory;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IRelationship;


/**
 * ArchimateModelUtils Tests
 *
 * @author Phillip Beauvoir
 */
public class ArchimateModelUtilsTests {

    /**
     * This is required in order to run JUnit 4 tests with the old JUnit runner
     * 
     * @return
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ArchimateModelUtilsTests.class);
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
    // isValidRelationshipStart
    // ---------------------------------------------------------------------------------------------

    @Test
    public void isValidRelationshipStart() {
        IArchimateElement sourceElement = IArchimateFactory.eINSTANCE.createAndJunction();
        IRelationship relationship = IArchimateFactory.eINSTANCE.createFlowRelationship();
        assertTrue(ArchimateModelUtils.isValidRelationshipStart(sourceElement, relationship.eClass()));
        assertTrue(ArchimateModelUtils.isValidRelationshipStart(sourceElement, relationship.eClass()));
        relationship = IArchimateFactory.eINSTANCE.createTriggeringRelationship();
        assertTrue(ArchimateModelUtils.isValidRelationshipStart(sourceElement, relationship.eClass()));
        relationship = IArchimateFactory.eINSTANCE.createUsedByRelationship();
        assertFalse(ArchimateModelUtils.isValidRelationshipStart(sourceElement, relationship.eClass()));

        sourceElement = IArchimateFactory.eINSTANCE.createSystemSoftware();
        relationship = IArchimateFactory.eINSTANCE.createFlowRelationship();
        assertTrue(ArchimateModelUtils.isValidRelationshipStart(sourceElement, relationship.eClass()));
        relationship = IArchimateFactory.eINSTANCE.createAccessRelationship();
        assertTrue(ArchimateModelUtils.isValidRelationshipStart(sourceElement, relationship.eClass()));
    }
    
    // ---------------------------------------------------------------------------------------------
    // isValidRelationship
    // ---------------------------------------------------------------------------------------------

    @Test
    public void isValidRelationship() {
        IArchimateElement sourceElement = IArchimateFactory.eINSTANCE.createAndJunction();
        IArchimateElement targetElement = IArchimateFactory.eINSTANCE.createAndJunction();
        IRelationship relationship = IArchimateFactory.eINSTANCE.createFlowRelationship();
        assertTrue(ArchimateModelUtils.isValidRelationship(sourceElement, targetElement, relationship.eClass()));
        relationship = IArchimateFactory.eINSTANCE.createTriggeringRelationship();
        assertTrue(ArchimateModelUtils.isValidRelationship(sourceElement, targetElement, relationship.eClass()));
        relationship = IArchimateFactory.eINSTANCE.createUsedByRelationship();
        assertFalse(ArchimateModelUtils.isValidRelationship(sourceElement, targetElement, relationship.eClass()));
        
        sourceElement = IArchimateFactory.eINSTANCE.createSystemSoftware();
        targetElement = IArchimateFactory.eINSTANCE.createSystemSoftware();
        relationship = IArchimateFactory.eINSTANCE.createFlowRelationship();
        assertTrue(ArchimateModelUtils.isValidRelationship(sourceElement, targetElement, relationship.eClass()));
        relationship = IArchimateFactory.eINSTANCE.createAccessRelationship();
        assertFalse(ArchimateModelUtils.isValidRelationship(sourceElement, targetElement, relationship.eClass()));
        
        sourceElement = IArchimateFactory.eINSTANCE.createValue();
        targetElement = IArchimateFactory.eINSTANCE.createOrJunction();
        relationship = IArchimateFactory.eINSTANCE.createFlowRelationship();
        assertFalse(ArchimateModelUtils.isValidRelationship(sourceElement, targetElement, relationship.eClass()));
        relationship = IArchimateFactory.eINSTANCE.createAccessRelationship();
        assertFalse(ArchimateModelUtils.isValidRelationship(sourceElement, targetElement, relationship.eClass()));
    }
    
    
    // ---------------------------------------------------------------------------------------------
    // public static List<IRelationship> getRelationships(IArchimateElement element);
    // ---------------------------------------------------------------------------------------------

    @Test
    public void getRelationships_NotNull() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();

        IArchimateElement element1 = IArchimateFactory.eINSTANCE.createBusinessActor();
        model.getDefaultFolderForElement(element1).getElements().add(element1);
        
        IArchimateElement element2 = IArchimateFactory.eINSTANCE.createBusinessRole();
        model.getDefaultFolderForElement(element2).getElements().add(element2);
        
        assertNotNull(ArchimateModelUtils.getRelationships(element1));
        assertNotNull(ArchimateModelUtils.getRelationships(element2));
    }

    @Test
    public void getRelationships_NoRelations() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();

        IArchimateElement element1 = IArchimateFactory.eINSTANCE.createBusinessActor();
        model.getDefaultFolderForElement(element1).getElements().add(element1);
        
        IArchimateElement element2 = IArchimateFactory.eINSTANCE.createBusinessRole();
        model.getDefaultFolderForElement(element2).getElements().add(element2);
        
        assertTrue(ArchimateModelUtils.getRelationships(element1).isEmpty());
        assertTrue(ArchimateModelUtils.getRelationships(element2).isEmpty());
    }

    @Test
    public void getRelationships() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();

        IArchimateElement element1 = IArchimateFactory.eINSTANCE.createBusinessActor();
        model.getDefaultFolderForElement(element1).getElements().add(element1);
        
        IArchimateElement element2 = IArchimateFactory.eINSTANCE.createBusinessRole();
        model.getDefaultFolderForElement(element2).getElements().add(element2);
        
        IRelationship relation1 = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        relation1.setSource(element1);
        relation1.setTarget(element2);
        model.getDefaultFolderForElement(relation1).getElements().add(relation1);
        
        assertEquals(1, ArchimateModelUtils.getRelationships(element1).size());
        assertEquals(1, ArchimateModelUtils.getRelationships(element2).size());

        IRelationship relation2 = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        relation2.setSource(element1);
        relation2.setTarget(element2);
        model.getDefaultFolderForElement(relation2).getElements().add(relation2);
       
        assertEquals(2, ArchimateModelUtils.getRelationships(element1).size());
        assertEquals(2, ArchimateModelUtils.getRelationships(element2).size());
    }
    
    @Test
    public void getSourceRelationships() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();

        IArchimateElement element1 = IArchimateFactory.eINSTANCE.createBusinessActor();
        model.getDefaultFolderForElement(element1).getElements().add(element1);
        
        IArchimateElement element2 = IArchimateFactory.eINSTANCE.createBusinessRole();
        model.getDefaultFolderForElement(element2).getElements().add(element2);
        
        IRelationship relation1 = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        relation1.setSource(element1);
        relation1.setTarget(element2);
        model.getDefaultFolderForElement(relation1).getElements().add(relation1);
        
        assertEquals(1, ArchimateModelUtils.getSourceRelationships(element1).size());
        assertEquals(0, ArchimateModelUtils.getSourceRelationships(element2).size());
        assertEquals(relation1, ArchimateModelUtils.getSourceRelationships(element1).get(0));

        IRelationship relation2 = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        relation2.setSource(element1);
        relation2.setTarget(element2);
        model.getDefaultFolderForElement(relation2).getElements().add(relation2);
       
        assertEquals(2, ArchimateModelUtils.getSourceRelationships(element1).size());
        assertEquals(0, ArchimateModelUtils.getSourceRelationships(element2).size());
    }
    
    @Test
    public void getTargetRelationships() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.setDefaults();

        IArchimateElement element1 = IArchimateFactory.eINSTANCE.createBusinessActor();
        model.getDefaultFolderForElement(element1).getElements().add(element1);
        
        IArchimateElement element2 = IArchimateFactory.eINSTANCE.createBusinessRole();
        model.getDefaultFolderForElement(element2).getElements().add(element2);
        
        IRelationship relation1 = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        relation1.setSource(element1);
        relation1.setTarget(element2);
        model.getDefaultFolderForElement(relation1).getElements().add(relation1);
        
        assertEquals(0, ArchimateModelUtils.getTargetRelationships(element1).size());
        assertEquals(1, ArchimateModelUtils.getTargetRelationships(element2).size());
        assertEquals(relation1, ArchimateModelUtils.getTargetRelationships(element2).get(0));

        IRelationship relation2 = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        relation2.setSource(element1);
        relation2.setTarget(element2);
        model.getDefaultFolderForElement(relation2).getElements().add(relation2);
       
        assertEquals(0, ArchimateModelUtils.getTargetRelationships(element1).size());
        assertEquals(2, ArchimateModelUtils.getTargetRelationships(element2).size());
    }
    
    
    // ---------------------------------------------------------------------------------------------
    // public EObject getObjectByID(IArchimateModel model, String id);
    // ---------------------------------------------------------------------------------------------

    @Test
    public void getObjectByID() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        
        EObject element = ArchimateModelUtils.getObjectByID(model, null);
        assertNull(element);
        
        IArchimateElement newElement1 = IArchimateFactory.eINSTANCE.createApplicationFunction();
        model.getDefaultFolderForElement(newElement1).getElements().add(newElement1);
        IArchimateElement newElement2 = IArchimateFactory.eINSTANCE.createBusinessActivity();
        model.getDefaultFolderForElement(newElement2).getElements().add(newElement2);
        
        element = ArchimateModelUtils.getObjectByID(model, newElement1.getId());
        assertEquals(newElement1, element);
        
        element = ArchimateModelUtils.getObjectByID(model, newElement2.getId());
        assertEquals(newElement2, element);
    }
    
} 
