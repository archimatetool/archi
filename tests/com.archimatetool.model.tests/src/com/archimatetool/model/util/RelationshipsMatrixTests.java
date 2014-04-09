/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import junit.framework.JUnit4TestAdapter;

import org.eclipse.emf.ecore.EClass;
import org.junit.Test;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.util.RelationshipsMatrix.TargetMatrix;



public class RelationshipsMatrixTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(RelationshipsMatrixTests.class);
    }
    
    private RelationshipsMatrix matrix = RelationshipsMatrix.INSTANCE;
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetRelationshipsMatrixIsUnmodifiable() {
        Map<EClass, List<TargetMatrix>> map = matrix.getRelationshipsMatrix();
        map.put(IArchimatePackage.eINSTANCE.getAndJunction(), null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetRelationshipsValueMapIsUnmodifiable() {
        Map<EClass, Character> map = matrix.getRelationshipsValueMap();
        map.put(IArchimatePackage.eINSTANCE.getAndJunction(), null);
    }

    @Test
    public void testIsValidRelationshipStart() {
        IArchimateElement sourceElement = IArchimateFactory.eINSTANCE.createAndJunction();
        IRelationship relationship = IArchimateFactory.eINSTANCE.createFlowRelationship();
        
        assertTrue(matrix.isValidRelationshipStart(sourceElement, relationship.eClass()));
        assertTrue(matrix.isValidRelationshipStart(sourceElement, relationship.eClass()));
        
        relationship = IArchimateFactory.eINSTANCE.createTriggeringRelationship();
        assertTrue(matrix.isValidRelationshipStart(sourceElement, relationship.eClass()));
        
        relationship = IArchimateFactory.eINSTANCE.createUsedByRelationship();
        assertFalse(matrix.isValidRelationshipStart(sourceElement, relationship.eClass()));

        sourceElement = IArchimateFactory.eINSTANCE.createSystemSoftware();
        relationship = IArchimateFactory.eINSTANCE.createFlowRelationship();
        assertTrue(matrix.isValidRelationshipStart(sourceElement, relationship.eClass()));
        relationship = IArchimateFactory.eINSTANCE.createAccessRelationship();
        assertTrue(matrix.isValidRelationshipStart(sourceElement, relationship.eClass()));
    }

    @Test
    public void testIsValidRelationship() {
        EClass sourceClass = IArchimatePackage.eINSTANCE.getAndJunction();
        EClass targetClass = IArchimatePackage.eINSTANCE.getAndJunction();
        EClass relationship = IArchimatePackage.eINSTANCE.getFlowRelationship();
        assertTrue(matrix.isValidRelationship(sourceClass, targetClass, relationship));

        relationship = IArchimatePackage.eINSTANCE.getTriggeringRelationship();
        assertTrue(matrix.isValidRelationship(sourceClass, targetClass, relationship));
        
        relationship = IArchimatePackage.eINSTANCE.getUsedByRelationship();
        assertFalse(matrix.isValidRelationship(sourceClass, targetClass, relationship));
        
        sourceClass = IArchimatePackage.eINSTANCE.getSystemSoftware();
        targetClass = IArchimatePackage.eINSTANCE.getSystemSoftware();
        relationship = IArchimatePackage.eINSTANCE.getFlowRelationship();
        assertTrue(matrix.isValidRelationship(sourceClass, targetClass, relationship));
        
        relationship = IArchimatePackage.eINSTANCE.getAccessRelationship();
        assertFalse(matrix.isValidRelationship(sourceClass, targetClass, relationship));
        
        sourceClass = IArchimatePackage.eINSTANCE.getValue();
        targetClass = IArchimatePackage.eINSTANCE.getOrJunction();
        relationship = IArchimatePackage.eINSTANCE.getFlowRelationship();
        assertFalse(matrix.isValidRelationship(sourceClass, targetClass, relationship));
        
        relationship = IArchimatePackage.eINSTANCE.getAccessRelationship();
        assertFalse(matrix.isValidRelationship(sourceClass, targetClass, relationship));
    }
    
    
} 
