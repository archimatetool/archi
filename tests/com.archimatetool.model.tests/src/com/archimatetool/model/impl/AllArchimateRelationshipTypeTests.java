/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.archimatetool.model.IAccessRelationship;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IAssociationRelationship;
import com.archimatetool.model.IInfluenceRelationship;
import com.archimatetool.model.util.ArchimateModelUtils;

@SuppressWarnings("nls")
@RunWith(Parameterized.class)
public class AllArchimateRelationshipTypeTests extends ArchimateConceptTests {
    
    @Parameters
    public static Collection<EClass[]> eObjects() {
        List<EClass[]> list = new ArrayList<>();
        
        for(EClass eClass : ArchimateModelUtils.getRelationsClasses()) {
            list.add(new EClass[] { eClass });
        }
        
        return list;
    }
    
    private IArchimateRelationship relationship;
    
    private IArchimateElement sourceElement;
    private IArchimateElement targetElement;
    
    private IArchimateRelationship sourceRelationship;
    private IArchimateRelationship targetRelationship;

    public AllArchimateRelationshipTypeTests(EClass eClass) {
        super(eClass);
    }
    
    @Override
    @Before
    public void runBeforeEachTest() {
        super.runBeforeEachTest();
        
        relationship = (IArchimateRelationship)getConcept();
        
        sourceElement = IArchimateFactory.eINSTANCE.createBusinessActor();
        targetElement = IArchimateFactory.eINSTANCE.createBusinessProcess();
        
        sourceRelationship = IArchimateFactory.eINSTANCE.createAssociationRelationship();
        targetRelationship = IArchimateFactory.eINSTANCE.createAssociationRelationship();
    }
    
    // Access Relationship Type
    @Test
    public void testAccess_Interface_Type() {
        // Only IAccessRelationship type
        Assume.assumeTrue(relationship instanceof IAccessRelationship);

        IAccessRelationship aRelationship = (IAccessRelationship)relationship;
        assertEquals(IAccessRelationship.WRITE_ACCESS, aRelationship.getAccessType());
        aRelationship.setAccessType(IAccessRelationship.READ_ACCESS);
        assertEquals(IAccessRelationship.READ_ACCESS, aRelationship.getAccessType());
    }
    
    // Influence Relationship Strength
    @Test
    public void testInfluence_Strength() {
        // Only IInfluenceRelationship type
        Assume.assumeTrue(relationship instanceof IInfluenceRelationship);

        IInfluenceRelationship aRelationship = (IInfluenceRelationship)relationship;
        assertEquals("", aRelationship.getStrength());
        aRelationship.setStrength("++");
        assertEquals("++", aRelationship.getStrength());
    }

    // Association Relationship Directed
    @Test
    public void testAssociation_Directed() {
        // Only IAssociationRelationship type
        Assume.assumeTrue(relationship instanceof IAssociationRelationship);

        IAssociationRelationship aRelationship = (IAssociationRelationship)relationship;
        assertFalse(aRelationship.isDirected());
        aRelationship.setDirected(true);
        assertTrue(aRelationship.isDirected());
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
