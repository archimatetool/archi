/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.viewpoints;

import static org.junit.Assert.*;

import org.eclipse.emf.ecore.EClass;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.util.ArchimateModelUtils;

import junit.framework.JUnit4TestAdapter;


@SuppressWarnings("nls")
public class ViewpointTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ViewpointTests.class);
    }
    
    Viewpoint vp;
    
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
    public void testIsAllowedConcept_True_When_Added_And_False_Otherwise() {
        // Add a concept
        vp.getClassList().add(IArchimatePackage.eINSTANCE.getBusinessActor());
        
        // Should be allowed
        assertTrue(vp.isAllowedConcept(IArchimatePackage.eINSTANCE.getBusinessActor()));
        
        // Default concepts allowed too
        assertTrue(vp.isAllowedConcept(IArchimatePackage.eINSTANCE.getJunction()));
        assertTrue(vp.isAllowedConcept(IArchimatePackage.eINSTANCE.getGrouping()));
        
        // But no others
        for(EClass eClass : ArchimateModelUtils.getAllArchimateClasses()) {
            if(eClass != IArchimatePackage.eINSTANCE.getBusinessActor() && eClass != IArchimatePackage.eINSTANCE.getGrouping()) {
                assertFalse(vp.isAllowedConcept(eClass));
            }
        }
    }
} 
