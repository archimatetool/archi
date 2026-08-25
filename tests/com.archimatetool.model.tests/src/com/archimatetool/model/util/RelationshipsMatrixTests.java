/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.junit.jupiter.api.Test;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.util.RelationshipsMatrix.TargetRelations;


@SuppressWarnings("nls")
public class RelationshipsMatrixTests {

    private RelationshipsMatrix matrix = RelationshipsMatrix.INSTANCE;
    
    @Test
    void getRelationshipsKeyMapIsUnmodifiable() {
        Map<Character, EClass> map = matrix.getRelationsKeyMap();
        assertThrows(UnsupportedOperationException.class, () -> {
            map.put('o', IArchimatePackage.eINSTANCE.getAssociationRelationship());
        });
    }
    
    @Test
    void getRelationKeys() {
        assertEquals("cfgostv", matrix.getRelationKeys(IArchimatePackage.eINSTANCE.getBusinessActor(),
                            IArchimatePackage.eINSTANCE.getBusinessActor()));
        
        assertEquals("acfginorstv", matrix.getRelationKeys(IArchimatePackage.eINSTANCE.getBusinessActor(),
                IArchimatePackage.eINSTANCE.getGrouping()));
    }

    @Test
    void isValidRelationshipStart_Element() {
        IArchimateElement sourceElement = IArchimateFactory.eINSTANCE.createJunction();
        IArchimateRelationship relationship = IArchimateFactory.eINSTANCE.createFlowRelationship();
        
        assertTrue(matrix.isValidRelationshipStart(sourceElement.eClass(), relationship.eClass()));
        
        relationship = IArchimateFactory.eINSTANCE.createTriggeringRelationship();
        assertTrue(matrix.isValidRelationshipStart(sourceElement.eClass(), relationship.eClass()));
        
        relationship = IArchimateFactory.eINSTANCE.createSpecializationRelationship();
        assertTrue(matrix.isValidRelationshipStart(sourceElement.eClass(), relationship.eClass()));

        sourceElement = IArchimateFactory.eINSTANCE.createSystemSoftware();
        relationship = IArchimateFactory.eINSTANCE.createFlowRelationship();
        assertTrue(matrix.isValidRelationshipStart(sourceElement.eClass(), relationship.eClass()));
        
        relationship = IArchimateFactory.eINSTANCE.createAccessRelationship();
        assertTrue(matrix.isValidRelationshipStart(sourceElement.eClass(), relationship.eClass()));
    }

    @Test
    void isValidRelationshipStart_Relationship() {
        for(EClass eClassSource : ArchimateModelUtils.getRelationsClasses()) {
            assertTrue(matrix.isValidRelationshipStart(eClassSource, IArchimatePackage.eINSTANCE.getAssociationRelationship()));
        }
        
        for(EClass eClassSource : ArchimateModelUtils.getRelationsClasses()) {
            assertFalse(matrix.isValidRelationshipStart(eClassSource, IArchimatePackage.eINSTANCE.getInfluenceRelationship()));
        }
    }

    @Test
    void isValidRelationship_ElementToElement() {
        EClass sourceClass = IArchimatePackage.eINSTANCE.getJunction();
        EClass targetClass = IArchimatePackage.eINSTANCE.getJunction();
        EClass relationship = IArchimatePackage.eINSTANCE.getFlowRelationship();
        assertTrue(matrix.isValidRelationship(sourceClass, targetClass, relationship));

        relationship = IArchimatePackage.eINSTANCE.getTriggeringRelationship();
        assertTrue(matrix.isValidRelationship(sourceClass, targetClass, relationship));
        
        relationship = IArchimatePackage.eINSTANCE.getSpecializationRelationship();
        assertTrue(matrix.isValidRelationship(sourceClass, targetClass, relationship));
        
        sourceClass = IArchimatePackage.eINSTANCE.getSystemSoftware();
        targetClass = IArchimatePackage.eINSTANCE.getSystemSoftware();
        relationship = IArchimatePackage.eINSTANCE.getFlowRelationship();
        assertTrue(matrix.isValidRelationship(sourceClass, targetClass, relationship));
        
        relationship = IArchimatePackage.eINSTANCE.getAccessRelationship();
        assertFalse(matrix.isValidRelationship(sourceClass, targetClass, relationship));
        
        sourceClass = IArchimatePackage.eINSTANCE.getValue();
        targetClass = IArchimatePackage.eINSTANCE.getJunction();
        relationship = IArchimatePackage.eINSTANCE.getSpecializationRelationship();
        assertTrue(matrix.isValidRelationship(sourceClass, targetClass, relationship));
        
        relationship = IArchimatePackage.eINSTANCE.getSpecializationRelationship();
        assertTrue(matrix.isValidRelationship(sourceClass, targetClass, relationship));
    }
    
    @Test
    void isValidRelationship_RelationshipToAnother() {
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
    
    @Test
    void getTargetRelations() {
        // BusinessActor -> BusinessRole
        TargetRelations relations = matrix.getTargetRelations(IArchimatePackage.eINSTANCE.getBusinessActor(),
                                                              IArchimatePackage.eINSTANCE.getBusinessRole());
        assertNotNull(relations);
        assertEquals(5, relations.relationships().size());
        
        // BusinessActor -> Any Relationship should return ArchimateRelationship super class
        relations = matrix.getTargetRelations(IArchimatePackage.eINSTANCE.getBusinessActor(),
                                          IArchimatePackage.eINSTANCE.getAggregationRelationship());
        assertNotNull(relations);
        assertEquals(1, relations.relationships().size());
        
        // Any Relationship -> BusinessActor should work
        relations = matrix.getTargetRelations(IArchimatePackage.eINSTANCE.getAggregationRelationship(),
                                          IArchimatePackage.eINSTANCE.getBusinessActor());
        assertNotNull(relations);
        assertEquals(1, relations.relationships().size());
    }
    
    @Test
    void getRelationshipDerivation() {
        // TODO: add proper tests when the relationships.xml has uppercase key letters for direct relationships
        RelationshipDerivation state = matrix.getRelationshipDerivation(IArchimatePackage.eINSTANCE.getBusinessActor(),
                                                                        IArchimatePackage.eINSTANCE.getBusinessActor(),
                                                                        IArchimatePackage.eINSTANCE.getFlowRelationship());
        assertEquals(RelationshipDerivation.DERIVED, state); // will always return derived for now
        
        state = matrix.getRelationshipDerivation(IArchimatePackage.eINSTANCE.getBusinessActor(),
                                                 IArchimatePackage.eINSTANCE.getBusinessActor(),
                                                 IArchimatePackage.eINSTANCE.getAccessRelationship());
        assertEquals(RelationshipDerivation.INVALID, state); // AccessRelationship not present
    }
    
    @Test
    void getRelationshipsByDerivation_Derived() {
        // TODO: add proper tests when the relationships.xml has uppercase key letters for direct relationships
        Set<EClass> result = matrix.getRelationshipsByDerivation(IArchimatePackage.eINSTANCE.getBusinessActor(),
                                                     IArchimatePackage.eINSTANCE.getBusinessActor(),
                                                     true);
        assertEquals(7, result.size());
        
        // Unmodifiable
        assertThrows(UnsupportedOperationException.class, () -> {
            result.add(IArchimatePackage.eINSTANCE.getFlowRelationship());
        });
    }
    
    @Test
    void getRelationshipsByDerivation_Direct() {
        // TODO: add proper tests when the relationships.xml has uppercase key letters for direct relationships
        Set<EClass> result = matrix.getRelationshipsByDerivation(IArchimatePackage.eINSTANCE.getBusinessActor(),
                                                     IArchimatePackage.eINSTANCE.getBusinessActor(),
                                                     false);
        assertEquals(0, result.size());
        
        // Unmodifiable
        assertThrows(UnsupportedOperationException.class, () -> {
            result.add(IArchimatePackage.eINSTANCE.getFlowRelationship());
        });
    }
} 
