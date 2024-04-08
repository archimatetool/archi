/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelReference;


@SuppressWarnings("nls")
public class DiagramModelReferenceTests extends DiagramModelObjectTests {
    
    private IDiagramModelReference ref;
    private IDiagramModel dm;
    
    @Override
    protected IDiagramModelReference getComponent() {
        ref = IArchimateFactory.eINSTANCE.createDiagramModelReference();
        return ref;
    }

    @BeforeEach
    public void runBeforeEachTest() {
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
        ref.setReferencedModel(dm);
        super.testGetName();
        assertEquals("name", ref.getReferencedModel().getName());
    }

}
