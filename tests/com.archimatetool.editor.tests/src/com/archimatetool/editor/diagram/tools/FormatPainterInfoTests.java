/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.tools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.swt.graphics.RGB;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.archimatetool.editor.diagram.tools.FormatPainterInfo.PaintFormat;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.testingtools.ArchimateTestModel;

public class FormatPainterInfoTests {
    
    private FormatPainterInfo info;
    
    @BeforeEach
    public void runBeforeEachTest() {
        info = new FormatPainterInfo();
    }
    
    @AfterEach
    public void runAfterEachTest() {
        info.dispose();
    }

    // ---------------------------------------------------------------------------------------------
    // Tests
    // ---------------------------------------------------------------------------------------------

    @Test
    public void testGetPaintFormat() {
        // Should be null initially
        PaintFormat pf = info.getPaintFormat();
        assertNull(pf);
    }

    @Test
    public void testUpdatePaintFormat() {
        // Set FormatPainterInfo to Source component
        IDiagramModelArchimateObject sourceComponent =
                ArchimateTestModel.createDiagramModelArchimateObject(IArchimateFactory.eINSTANCE.createBusinessActor());
        info.updatePaintFormat(sourceComponent);
        PaintFormat pf = info.getPaintFormat();
        assertNotNull(pf);
        assertEquals(sourceComponent, pf.getSourceComponent());
        
        // Check cursor
        assertEquals(new RGB(255, 255, 181), pf.getCursorColor());
    }
    
    @Test
    public void testIsFat() {
        assertFalse(info.isFat());

        IDiagramModelArchimateObject sourceComponent =
                ArchimateTestModel.createDiagramModelArchimateObject(IArchimateFactory.eINSTANCE.createBusinessActor());
        info.updatePaintFormat(sourceComponent);
        assertTrue(info.isFat());
    }

    @Test
    public void testReset() {
        PaintFormat pf = info.getPaintFormat();
        assertNull(pf);

        IDiagramModelArchimateObject sourceComponent =
                ArchimateTestModel.createDiagramModelArchimateObject(IArchimateFactory.eINSTANCE.createBusinessActor());
        info.updatePaintFormat(sourceComponent);
        pf = info.getPaintFormat();
        assertNotNull(pf);
        
        info.reset();
        pf = info.getPaintFormat();
        assertNull(pf);
    }
}