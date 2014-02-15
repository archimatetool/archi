/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.ISketchModelActor;


public class SketchModelActorTests extends DiagramModelObjectTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(SketchModelActorTests.class);
    }
    
    private ISketchModelActor actor;
    
    @Override
    protected IDiagramModelComponent getComponent() {
        ISketchModelActor actor = IArchimateFactory.eINSTANCE.createSketchModelActor();
        return actor;
    }

    @Before
    public void runBeforeEachDiagramModelArchimateObjectTest() {
        actor = (ISketchModelActor)getComponent();
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
