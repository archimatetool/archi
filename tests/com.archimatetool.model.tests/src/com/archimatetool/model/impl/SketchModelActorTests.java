/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.Test;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.ISketchModelActor;


public class SketchModelActorTests extends DiagramModelObjectTests {
    
    private ISketchModelActor actor;
    
    @Override
    protected IDiagramModelComponent getComponent() {
        actor = IArchimateFactory.eINSTANCE.createSketchModelActor();
        return actor;
    }


    @Override
    @Test
    public void testGetCopy() {
        super.testGetCopy();
        
        actor.getProperties().add(IArchimateFactory.eINSTANCE.createProperty());
        
        ISketchModelActor copy = (ISketchModelActor)actor.getCopy();
        
        assertNotSame(actor, copy);
        
        assertNotSame(actor.getProperties(), copy.getProperties());
        assertEquals(actor.getProperties().size(), copy.getProperties().size());
    }

    @Test
    public void testGetDocumentation() {
        CommonTests.testGetDocumentation(actor);
    }

    @Test
    public void testGetProperties() {
        CommonTests.testProperties(actor);
    }
}
