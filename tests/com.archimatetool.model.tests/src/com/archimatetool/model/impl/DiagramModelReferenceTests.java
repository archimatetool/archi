/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelReference;


@SuppressWarnings("nls")
public class DiagramModelReferenceTests extends DiagramModelObjectTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DiagramModelReferenceTests.class);
    }
    
    private IDiagramModelReference ref;
    private IDiagramModel dm;
    
    @Override
    protected IDiagramModelComponent getComponent() {
        IDiagramModelReference ref = IArchimateFactory.eINSTANCE.createDiagramModelReference();
        return ref;
    }

    @Before
    public void runBeforeEachDiagramModelArchimateObjectTest() {
        ref = (IDiagramModelReference)getComponent();
        dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
    }


    @Test
    public void testReferencedModel() {
        assertNull(ref.getReferencedModel());
        ref.setReferencedModel(dm);
        assertSame(dm, ref.getReferencedModel());
    }

    @Override
    @Test
    public void testGetName() {
        assertEquals("", ref.getName());
        dm.setName("aName");
        ref.setReferencedModel(dm);
        assertEquals("aName", ref.getName());
    }

}
