/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.emf.ecore.EClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.archimatetool.model.IAccessRelationship;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IAssociationRelationship;
import com.archimatetool.model.IInfluenceRelationship;
import com.archimatetool.model.util.ArchimateModelUtils;

@SuppressWarnings("nls")
public class AllArchimateRelationshipTypeTests extends ArchimateConceptTests {
    
    static Stream<Arguments> getParams() {
        List<Arguments> list = new ArrayList<>();
        
        for(EClass eClass : ArchimateModelUtils.getRelationsClasses()) {
            list.add(getParam(eClass));
        }
        
        return list.stream();
    }
    
    private IArchimateElement sourceElement;
    private IArchimateElement targetElement;
    
    private IArchimateRelationship sourceRelationship;
    private IArchimateRelationship targetRelationship;

    @BeforeEach
    public void runBeforeEachTest() {
        sourceElement = IArchimateFactory.eINSTANCE.createBusinessActor();
        targetElement = IArchimateFactory.eINSTANCE.createBusinessProcess();
        
        sourceRelationship = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        targetRelationship = IArchimateFactory.eINSTANCE.createAssociationRelationship();
    }
    
    // Access Relationship Type
    @Test
    public void testAccess_Interface_Type() {
        IAccessRelationship relationship = IArchimateFactory.eINSTANCE.createAccessRelationship();
        assertEquals(IAccessRelationship.WRITE_ACCESS, relationship.getAccessType());
        relationship.setAccessType(IAccessRelationship.READ_ACCESS);
        assertEquals(IAccessRelationship.READ_ACCESS, relationship.getAccessType());
    }
    
    // Influence Relationship Strength
    @Test
    public void testInfluence_Strength() {
        IInfluenceRelationship relationship = IArchimateFactory.eINSTANCE.createInfluenceRelationship();
        assertEquals("", relationship.getStrength());
        relationship.setStrength("++");
        assertEquals("++", relationship.getStrength());
    }

    // Association Relationship Directed
    @Test
    public void testAssociation_Directed() {
        IAssociationRelationship relationship = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        assertFalse(relationship.isDirected());
        relationship.setDirected(true);
        assertTrue(relationship.isDirected());
    }
    
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetSetSourceElement(IArchimateRelationship relationship) {
        assertNull(relationship.getSource());
        relationship.setSource(sourceElement);
        assertSame(sourceElement, relationship.getSource());
    }
        
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetSetTargetElement(IArchimateRelationship relationship) {
        assertNull(relationship.getTarget());
        relationship.setTarget(targetElement);
        assertSame(targetElement, relationship.getTarget());
    }
 
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetSetSourceRelationship(IArchimateRelationship relationship) {
        assertNull(relationship.getSource());
        relationship.setSource(sourceRelationship);
        assertSame(sourceRelationship, relationship.getSource());
    }
        
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetSetTargetRelationship(IArchimateRelationship relationship) {
        assertNull(relationship.getTarget());
        relationship.setTarget(targetRelationship);
        assertSame(targetRelationship, relationship.getTarget());
    }
    
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testSetSource_AlsoSetsSourceAndRemovesRelationships(IArchimateRelationship relationship) {
        relationship.setSource(sourceRelationship);
        assertSame(relationship, sourceRelationship.getSourceRelationships().get(0));

        relationship.setSource(sourceElement);
        assertTrue(sourceRelationship.getSourceRelationships().isEmpty());
        assertSame(relationship, sourceElement.getSourceRelationships().get(0));
    }
    
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testSetSource_AlsoSetsSourceRelationships(IArchimateRelationship relationship) {
        sourceRelationship.setSource(relationship);
        targetRelationship.setSource(relationship);
        
        assertSame(sourceRelationship, relationship.getSourceRelationships().get(0));
        assertSame(targetRelationship, relationship.getSourceRelationships().get(1));
    }

    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testSetTarget_AlsoSetsTargetAndRemovesRelationships(IArchimateRelationship relationship) {
        relationship.setTarget(targetRelationship);
        assertSame(relationship, targetRelationship.getTargetRelationships().get(0));

        relationship.setTarget(targetElement);
        assertTrue(targetRelationship.getTargetRelationships().isEmpty());
        assertSame(relationship, targetElement.getTargetRelationships().get(0));
    }
    
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testSetTarget_AlsoSetsTargetRelationships(IArchimateRelationship relationship) {
        sourceRelationship.setTarget(relationship);
        targetRelationship.setTarget(relationship);
        
        assertSame(sourceRelationship, relationship.getTargetRelationships().get(0));
        assertSame(targetRelationship, relationship.getTargetRelationships().get(1));
    }
    
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testConnect(IArchimateRelationship relationship) {
        assertNull(relationship.getSource());
        assertNull(relationship.getTarget());
        
        relationship.connect(sourceElement, targetElement);
        
        assertSame(sourceElement, relationship.getSource());
        assertSame(targetElement, relationship.getTarget());
    }
    
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testDisconnect(IArchimateRelationship relationship) {
        relationship.connect(sourceElement, targetElement);
        assertSame(sourceElement, relationship.getSource());
        assertSame(targetElement, relationship.getTarget());
        
        relationship.disconnect();
        assertSame(sourceElement, relationship.getSource());
        assertSame(targetElement, relationship.getTarget());
        assertTrue(sourceElement.getSourceRelationships().isEmpty());
        assertTrue(targetElement.getTargetRelationships().isEmpty());
    }

    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testReconnect(IArchimateRelationship relationship) {
        relationship.connect(sourceElement, targetElement);
        relationship.disconnect();
        relationship.reconnect();
        
        assertSame(sourceElement, relationship.getSource());
        assertSame(targetElement, relationship.getTarget());
        assertSame(relationship, sourceElement.getSourceRelationships().get(0));
        assertSame(relationship, targetElement.getTargetRelationships().get(0));
    }
    
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetCopy_SourceAndTargetComponentsAreNull(IArchimateRelationship relationship) {
        relationship.connect(sourceElement, targetElement);
        IArchimateRelationship copy = (IArchimateRelationship)relationship.getCopy();
        assertNull(copy.getSource());
        assertNull(copy.getTarget());
    }

}
