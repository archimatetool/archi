/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.eclipse.swt.graphics.RGB;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.editor.diagram.tools.FormatPainterInfo.PaintFormat;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.testingtools.ArchimateTestModel;

public class FormatPainterInfoTests {
    
    private FormatPainterInfo info;
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(FormatPainterInfoTests.class);
    }
    
    @Before
    public void runBeforeEachTest() {
        info = new FormatPainterInfo();
    }
    
    @After
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