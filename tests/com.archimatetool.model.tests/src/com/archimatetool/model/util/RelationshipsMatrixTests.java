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

import org.eclipse.emf.ecore.EClass;
import org.junit.Test;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.util.RelationshipsMatrix.TargetMatrix;

import junit.framework.JUnit4TestAdapter;



public class RelationshipsMatrixTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(RelationshipsMatrixTests.class);
    }
    
    private RelationshipsMatrix matrix = RelationshipsMatrix.INSTANCE;
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetRelationshipsMatrixIsUnmodifiable() {
        Map<EClass, List<TargetMatrix>> map = matrix.getRelationshipsMatrix();
        map.put(IArchimatePackage.eINSTANCE.getJunction(), null);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testGetRelationshipsValueMapIsUnmodifiable() {
        Map<EClass, Character> map = matrix.getRelationshipsValueMap();
        map.put(IArchimatePackage.eINSTANCE.getJunction(), null);
    }

    @Test
    public void testIsValidRelationshipStart_Element() {
        IArchimateElement sourceElement = IArchimateFactory.eINSTANCE.createJunction();
        IArchimateRelationship relationship = IArchimateFactory.eINSTANCE.createFlowRelationship();
        
        assertTrue(matrix.isValidRelationshipStart(sourceElement.eClass(), relationship.eClass()));
        
        relationship = IArchimateFactory.eINSTANCE.createTriggeringRelationship();
        assertTrue(matrix.isValidRelationshipStart(sourceElement.eClass(), relationship.eClass()));
        
        relationship = IArchimateFactory.eINSTANCE.createSpecializationRelationship();
        assertFalse(matrix.isValidRelationshipStart(sourceElement.eClass(), relationship.eClass()));

        sourceElement = IArchimateFactory.eINSTANCE.createSystemSoftware();
        relationship = IArchimateFactory.eINSTANCE.createFlowRelationship();
        assertTrue(matrix.isValidRelationshipStart(sourceElement.eClass(), relationship.eClass()));
        
        relationship = IArchimateFactory.eINSTANCE.createAccessRelationship();
        assertTrue(matrix.isValidRelationshipStart(sourceElement.eClass(), relationship.eClass()));
    }

    @Test
    public void testIsValidRelationshipStart_Relationship() {
        for(EClass eClassSource : ArchimateModelUtils.getRelationsClasses()) {
            assertTrue(matrix.isValidRelationshipStart(eClassSource, IArchimatePackage.eINSTANCE.getAssociationRelationship()));
        }
        
        for(EClass eClassSource : ArchimateModelUtils.getRelationsClasses()) {
            assertFalse(matrix.isValidRelationshipStart(eClassSource, IArchimatePackage.eINSTANCE.getInfluenceRelationship()));
        }
    }

    @Test
    public void testIsValidRelationship_ElementToElement() {
        EClass sourceClass = IArchimatePackage.eINSTANCE.getJunction();
        EClass targetClass = IArchimatePackage.eINSTANCE.getJunction();
        EClass relationship = IArchimatePackage.eINSTANCE.getFlowRelationship();
        assertTrue(matrix.isValidRelationship(sourceClass, targetClass, relationship));

        relationship = IArchimatePackage.eINSTANCE.getTriggeringRelationship();
        assertTrue(matrix.isValidRelationship(sourceClass, targetClass, relationship));
        
        relationship = IArchimatePackage.eINSTANCE.getSpecializationRelationship();
        assertFalse(matrix.isValidRelationship(sourceClass, targetClass, relationship));
        
        sourceClass = IArchimatePackage.eINSTANCE.getSystemSoftware();
        targetClass = IArchimatePackage.eINSTANCE.getSystemSoftware();
        relationship = IArchimatePackage.eINSTANCE.getFlowRelationship();
        assertTrue(matrix.isValidRelationship(sourceClass, targetClass, relationship));
        
        relationship = IArchimatePackage.eINSTANCE.getAccessRelationship();
        assertFalse(matrix.isValidRelationship(sourceClass, targetClass, relationship));
        
        sourceClass = IArchimatePackage.eINSTANCE.getValue();
        targetClass = IArchimatePackage.eINSTANCE.getJunction();
        relationship = IArchimatePackage.eINSTANCE.getSpecializationRelationship();
        assertFalse(matrix.isValidRelationship(sourceClass, targetClass, relationship));
        
        relationship = IArchimatePackage.eINSTANCE.getSpecializationRelationship();
        assertFalse(matrix.isValidRelationship(sourceClass, targetClass, relationship));
    }
    
    
    @Test
    public void testIsValidRelationship_RelationshipToAnother() {
        EClass relationshipType = IArchimatePackage.eINSTANCE.getAssociationRelationship(); 
        
        EClass objectClass = IArchimatePackage.eINSTANCE.getBusinessActor();
        EClass relationClass = IArchimatePackage.eINSTANCE.getCompositionRelationship();
        
        // OK from object to relation
        assertTrue(matrix.isValidRelationship(objectClass, relationClass, relationshipType));

        // OK from relation to object
        assertTrue(matrix.isValidRelationship(relationClass, objectClass, relationshipType));

        // Not OK from relation -> relation
        assertFalse(matrix.isValidRelationship(relationClass, relationClass, relationshipType));
        
        // Not OK from relation -> Junction
        objectClass = IArchimatePackage.eINSTANCE.getJunction();
        assertFalse(matrix.isValidRelationship(relationClass, objectClass, relationshipType));
        
        // Not OK from Junction to relation
        assertFalse(matrix.isValidRelationship(objectClass, relationClass, relationshipType));
    }
} 
