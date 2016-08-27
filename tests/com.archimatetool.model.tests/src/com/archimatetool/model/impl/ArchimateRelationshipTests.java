/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateRelationship;

import junit.framework.JUnit4TestAdapter;


public abstract class ArchimateRelationshipTests extends ArchimateConceptTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ArchimateRelationshipTests.class);
    }
    
    protected IArchimateElement sourceElement;
    protected IArchimateElement targetElement;
    
    protected IArchimateRelationship sourceRelationship;
    protected IArchimateRelationship targetRelationship;

    protected IArchimateRelationship relationship;
    
    @Before
    public void runBeforeEachRelationshipTest() {
        relationship = (IArchimateRelationship)concept;
        
        sourceElement = IArchimateFactory.eINSTANCE.createBusinessActor();
        targetElement = IArchimateFactory.eINSTANCE.createBusinessProcess();
        
        sourceRelationship = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        targetRelationship = IArchimateFactory.eINSTANCE.createAssociationRelationship();
    }
    
    
    @Test
    public void testGetSetSourceElement() {
        assertNull(relationship.getSource());
        relationship.setSource(sourceElement);
        assertSame(sourceElement, relationship.getSource());
    }
        
    @Test
    public void testGetSetTargetElement() {
        assertNull(relationship.getTarget());
        relationship.setTarget(targetElement);
        assertSame(targetElement, relationship.getTarget());
    }
 
    @Test
    public void testGetSetSourceRelationship() {
        assertNull(relationship.getSource());
        relationship.setSource(sourceRelationship);
        assertSame(sourceRelationship, relationship.getSource());
    }
        
    @Test
    public void testGetSetTargetRelationship() {
        assertNull(relationship.getTarget());
        relationship.setTarget(targetRelationship);
        assertSame(targetRelationship, relationship.getTarget());
    }
    
    @Test
    public void testSetSource_AlsoSetsSourceAndRemovesRelationships() {
        relationship.setSource(sourceRelationship);
        assertSame(relationship, sourceRelationship.getSourceRelationships().get(0));

        relationship.setSource(sourceElement);
        assertTrue(sourceRelationship.getSourceRelationships().isEmpty());
        assertSame(relationship, sourceElement.getSourceRelationships().get(0));
    }
    
    @Test
    public void testSetSource_AlsoSetsSourceRelationships() {
        sourceRelationship.setSource(relationship);
        targetRelationship.setSource(relationship);
        
        assertSame(sourceRelationship, relationship.getSourceRelationships().get(0));
        assertSame(targetRelationship, relationship.getSourceRelationships().get(1));
    }

    @Test
    public void testSetTarget_AlsoSetsTargetAndRemovesRelationships() {
        relationship.setTarget(targetRelationship);
        assertSame(relationship, targetRelationship.getTargetRelationships().get(0));

        relationship.setTarget(targetElement);
        assertTrue(targetRelationship.getTargetRelationships().isEmpty());
        assertSame(relationship, targetElement.getTargetRelationships().get(0));
    }
    
    @Test
    public void testSetTarget_AlsoSetsTargetRelationships() {
        sourceRelationship.setTarget(relationship);
        targetRelationship.setTarget(relationship);
        
        assertSame(sourceRelationship, relationship.getTargetRelationships().get(0));
        assertSame(targetRelationship, relationship.getTargetRelationships().get(1));
    }
    
    @Test
    public void testConnect() {
        assertNull(relationship.getSource());
        assertNull(relationship.getTarget());
        
        relationship.connect(sourceElement, targetElement);
        
        assertSame(sourceElement, relationship.getSource());
        assertSame(targetElement, relationship.getTarget());
    }
    
    @Test
    public void testDisconnect() {
        relationship.connect(sourceElement, targetElement);
        assertSame(sourceElement, relationship.getSource());
        assertSame(targetElement, relationship.getTarget());
        
        relationship.disconnect();
        assertSame(sourceElement, relationship.getSource());
        assertSame(targetElement, relationship.getTarget());
        assertTrue(sourceElement.getSourceRelationships().isEmpty());
        assertTrue(targetElement.getTargetRelationships().isEmpty());
    }

    @Test
    public void testReconnect() {
        relationship.connect(sourceElement, targetElement);
        relationship.disconnect();
        relationship.reconnect();
        
        assertSame(sourceElement, relationship.getSource());
        assertSame(targetElement, relationship.getTarget());
        assertSame(relationship, sourceElement.getSourceRelationships().get(0));
        assertSame(relationship, targetElement.getTargetRelationships().get(0));
    }
    
    @Test
    public void testGetCopy_SourceAndTargetComponentsAreNull() {
        relationship.connect(sourceElement, targetElement);
        IArchimateRelationship copy = (IArchimateRelationship)relationship.getCopy();
        assertNull(copy.getSource());
        assertNull(copy.getTarget());
    }
}
