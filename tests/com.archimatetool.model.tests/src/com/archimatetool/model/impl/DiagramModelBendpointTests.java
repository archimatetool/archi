/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelBendpoint;


public class DiagramModelBendpointTests {
    
    private IDiagramModelBendpoint bp;
    

    @BeforeEach
    public void runBeforeEachDiagramModelArchimateObjectTest() {
        bp = IArchimateFactory.eINSTANCE.createDiagramModelBendpoint();
    }


    @Test
    public void testDefaultValues() {
        assertNotNull(bp);
        assertEquals(0, bp.getStartX());
        assertEquals(0, bp.getStartY());
        assertEquals(0, bp.getEndX());
        assertEquals(0, bp.getEndY());
    }

    @Test
    public void testSetValues() {
        assertNotNull(bp);
        
        bp.setStartX(1);
        bp.setStartY(2);
        bp.setEndX(3);
        bp.setEndY(4);
        
        assertEquals(1, bp.getStartX());
        assertEquals(2, bp.getStartY());
        assertEquals(3, bp.getEndX());
        assertEquals(4, bp.getEndY());
    }

}
