/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.viewpoints;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.emf.ecore.EClass;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.util.ArchimateModelUtils;


@SuppressWarnings("nls")
public class ViewpointTests {

    private Viewpoint vp;
    
    @Before
    public void runBeforeEachTest() {
        vp = new Viewpoint("id", "name");
    }

    @Test
    public void testGetID() {
        assertEquals("id", vp.getID());
    }
    
    @Test
    public void testGetName() {
        assertEquals("name", vp.getName());
    }
    
    @Test
    public void testIsAllowedConcept_All_True_ByDefault() {
        assertTrue(vp.isAllowedConcept(IArchimatePackage.eINSTANCE.getJunction()));
        assertTrue(vp.isAllowedConcept(IArchimatePackage.eINSTANCE.getGrouping()));
        
        for(EClass eClass : ArchimateModelUtils.getAllArchimateClasses()) {
            assertTrue(vp.isAllowedConcept(eClass));
        }
        
        for(EClass eClass : ArchimateModelUtils.getRelationsClasses()) {
            assertTrue(vp.isAllowedConcept(eClass));
        }
    }
    
    @Test
    public void testIsAllowedElement_True_When_Added_And_False_Otherwise() {
        // Add an element
        vp.addEClass(IArchimatePackage.eINSTANCE.getBusinessActor());
        
        // Should be allowed
        assertTrue(vp.isAllowedConcept(IArchimatePackage.eINSTANCE.getBusinessActor()));
        
        // Default concepts allowed too
        assertTrue(vp.isAllowedConcept(IArchimatePackage.eINSTANCE.getJunction()));
        assertTrue(vp.isAllowedConcept(IArchimatePackage.eINSTANCE.getGrouping()));
        
        // But no other elements
        for(EClass eClass : ArchimateModelUtils.getAllArchimateClasses()) {
            if(eClass != IArchimatePackage.eINSTANCE.getBusinessActor() && eClass != IArchimatePackage.eINSTANCE.getGrouping()) {
                assertFalse(vp.isAllowedConcept(eClass));
            }
        }

        // And relations are allowed
        for(EClass eClass : ArchimateModelUtils.getRelationsClasses()) {
            assertTrue(vp.isAllowedConcept(eClass));
        }
    }
    
    @Test
    public void testIsAllowedRelations_True_When_Added_And_False_Otherwise() {
        // Add a relationship
        vp.addEClass(IArchimatePackage.eINSTANCE.getAccessRelationship());
        
        // Should be allowed
        assertTrue(vp.isAllowedConcept(IArchimatePackage.eINSTANCE.getAccessRelationship()));
        
        // But no other relations
        for(EClass eClass : ArchimateModelUtils.getRelationsClasses()) {
            if(eClass != IArchimatePackage.eINSTANCE.getAccessRelationship()) {
                assertFalse(vp.isAllowedConcept(eClass));
            }
        }
        
        // And all other elements
        for(EClass eClass : ArchimateModelUtils.getAllArchimateClasses()) {
            assertTrue(vp.isAllowedConcept(eClass));
        }
    }
} 
