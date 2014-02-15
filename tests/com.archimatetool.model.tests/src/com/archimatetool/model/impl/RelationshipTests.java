/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IRelationship;


public abstract class RelationshipTests extends ArchimateElementTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(RelationshipTests.class);
    }
    
    protected IArchimateElement source;
    protected IArchimateElement target;
    
    protected IRelationship relationship;
    
    @Before
    public void runBeforeEachRelationshipTest() {
        relationship = (IRelationship)element;
        source = IArchimateFactory.eINSTANCE.createBusinessActor();
        target = IArchimateFactory.eINSTANCE.createBusinessProcess();
    }
    
    
    @Test
    public void testGetSource() {
        assertNull(relationship.getSource());
        relationship.setSource(source);
        assertSame(source, relationship.getSource());
    }
        
    @Test
    public void testGetTarget() {
        assertNull(relationship.getTarget());
        relationship.setTarget(target);
        assertSame(target, relationship.getTarget());
    }
 
}
