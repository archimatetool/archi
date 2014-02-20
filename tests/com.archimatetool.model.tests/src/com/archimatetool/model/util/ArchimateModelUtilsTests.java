/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IRelationship;



/**
 * ArchimateModelUtils Tests
 *
 * @author Phillip Beauvoir
 */
public class ArchimateModelUtilsTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ArchimateModelUtilsTests.class);
    }
    
    /*
     * NOTE - Tests for ArchimateModelUtils.isValidRelationshipStart() and ArchimateModelUtils.isValidRelationship() are in RelationshipsMatrixTests
     */
    
    
    
    @Test
    public void testGetValidRelationships() {
        EClass sourceClass = IArchimatePackage.eINSTANCE.getBusinessActor();
        EClass targetClass = IArchimatePackage.eINSTANCE.getBusinessRole();

        EClass[] classes = ArchimateModelUtils.getValidRelationships(sourceClass, targetClass);
        assertEquals(5, classes.length);
        
        // The order of these is set in ArchimateModelUtils.getRelationsClasses()
        assertEquals(IArchimatePackage.eINSTANCE.getAssignmentRelationship(), classes[0]);
        assertEquals(IArchimatePackage.eINSTANCE.getTriggeringRelationship(), classes[1]);
        assertEquals(IArchimatePackage.eINSTANCE.getFlowRelationship(), classes[2]);
        assertEquals(IArchimatePackage.eINSTANCE.getUsedByRelationship(), classes[3]);
        assertEquals(IArchimatePackage.eINSTANCE.getAssociationRelationship(), classes[4]);
        
        // How much more can we test this...?
    }
    
    @Test
    public void testGetRelationships_NotNull() {
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
    public void testGetRelationships_NoRelations() {
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
    public void testGetRelationships() {
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
    public void testGetSourceRelationships() {
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
        assertSame(relation1, ArchimateModelUtils.getSourceRelationships(element1).get(0));

        IRelationship relation2 = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        relation2.setSource(element1);
        relation2.setTarget(element2);
        model.getDefaultFolderForElement(relation2).getElements().add(relation2);
       
        assertEquals(2, ArchimateModelUtils.getSourceRelationships(element1).size());
        assertEquals(0, ArchimateModelUtils.getSourceRelationships(element2).size());
    }
    
    @Test
    public void testGetTargetRelationships() {
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
        assertSame(relation1, ArchimateModelUtils.getTargetRelationships(element2).get(0));

        IRelationship relation2 = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        relation2.setSource(element1);
        relation2.setTarget(element2);
        model.getDefaultFolderForElement(relation2).getElements().add(relation2);
       
        assertEquals(0, ArchimateModelUtils.getTargetRelationships(element1).size());
        assertEquals(2, ArchimateModelUtils.getTargetRelationships(element2).size());
    }
    
    @Test
    public void testGetObjectByID() {
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        
        EObject element = ArchimateModelUtils.getObjectByID(model, null);
        assertNull(element);
        
        IArchimateElement newElement1 = IArchimateFactory.eINSTANCE.createApplicationFunction();
        model.getDefaultFolderForElement(newElement1).getElements().add(newElement1);
        IArchimateElement newElement2 = IArchimateFactory.eINSTANCE.createBusinessActivity();
        model.getDefaultFolderForElement(newElement2).getElements().add(newElement2);
        
        element = ArchimateModelUtils.getObjectByID(model, newElement1.getId());
        assertSame(newElement1, element);
        
        element = ArchimateModelUtils.getObjectByID(model, newElement2.getId());
        assertSame(newElement2, element);
    }
    
    @Test
    public void testGetBusinessClasses() {
        EClass[] classes = ArchimateModelUtils.getBusinessClasses();
        assertEquals(16, classes.length);
        
        for(EClass eClass : classes) {
            assertTrue(IArchimatePackage.eINSTANCE.getBusinessLayerElement().isSuperTypeOf(eClass));
        }
    }
    
    @Test
    public void testGetApplicationClasses() {
        EClass[] classes = ArchimateModelUtils.getApplicationClasses();
        assertEquals(7, classes.length);
        
        for(EClass eClass : classes) {
            assertTrue(IArchimatePackage.eINSTANCE.getApplicationLayerElement().isSuperTypeOf(eClass));
        }
    }

    @Test
    public void testGetTechnologyClasses() {
        EClass[] classes = ArchimateModelUtils.getTechnologyClasses();
        assertEquals(9, classes.length);
        
        for(EClass eClass : classes) {
            assertTrue(IArchimatePackage.eINSTANCE.getTechnologyLayerElement().isSuperTypeOf(eClass));
        }
    }
    
    @Test
    public void testGetMotivationClasses() {
        EClass[] classes = ArchimateModelUtils.getMotivationClasses();
        assertEquals(7, classes.length);
        
        for(EClass eClass : classes) {
            assertTrue(IArchimatePackage.eINSTANCE.getMotivationElement().isSuperTypeOf(eClass));
        }
    }

    @Test
    public void testGetImplementationMigrationClasses() {
        EClass[] classes = ArchimateModelUtils.getImplementationMigrationClasses();
        assertEquals(4, classes.length);
        
        for(EClass eClass : classes) {
            assertTrue(IArchimatePackage.eINSTANCE.getImplementationMigrationElement().isSuperTypeOf(eClass));
        }
    }
    
    @Test
    public void testGetRelationsClasses() {
        EClass[] classes = ArchimateModelUtils.getRelationsClasses();
        assertEquals(11, classes.length);
        
        for(EClass eClass : classes) {
            assertTrue(IArchimatePackage.eINSTANCE.getRelationship().isSuperTypeOf(eClass));
        }
    }
    
    @Test
    public void testGetConnectorClasses() {
        EClass[] classes = ArchimateModelUtils.getConnectorClasses();
        assertEquals(3, classes.length);
        
        for(EClass eClass : classes) {
            assertTrue(IArchimatePackage.eINSTANCE.getJunctionElement().isSuperTypeOf(eClass));
        }
    }
    
    @Test
    public void testGetAllArchimateClasses() {
        EClass[] classes = ArchimateModelUtils.getAllArchimateClasses();
        assertEquals(43, classes.length);
        
        for(EClass eClass : classes) {
            assertTrue(IArchimatePackage.eINSTANCE.getArchimateElement().isSuperTypeOf(eClass));
        }
    }
} 
