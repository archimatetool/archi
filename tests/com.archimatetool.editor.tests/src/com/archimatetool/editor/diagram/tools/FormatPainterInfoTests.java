/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.tools;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.swt.graphics.RGB;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.testingtools.ArchimateTestModel;
import com.archimatetool.tests.TestUtils;

@SuppressWarnings("nls")
public class FormatPainterInfoTests {
    
    private static FormatPainterInfo info = FormatPainterInfo.INSTANCE;
    
    @BeforeEach
    public void runBeforeEachTest() {
        info.reset();
    }
    
    @AfterAll
    public static void runAfterAll() {
        info.dispose();
    }

    // ---------------------------------------------------------------------------------------------
    // Tests
    // ---------------------------------------------------------------------------------------------

    @Test
    public void testInitialValues() throws Exception {
        assertNull(info.getSourceComponent());
        assertNull(info.getCursorColor());
        assertSame(info.getCursor(), TestUtils.getPrivateField(info, "defaultCursor"));
    }

    @Test
    public void testUpdateWithSourceComponent() throws Exception {
        // Set FormatPainterInfo to Source component
        IDiagramModelArchimateObject sourceComponent =
                ArchimateTestModel.createDiagramModelArchimateObject(IArchimateFactory.eINSTANCE.createBusinessActor());
        info.updateWithSourceComponent(sourceComponent);
        
        assertNotNull(info.getSourceComponent());
        // FormatPaintInfo will make a copy of the source component
        assertNotEquals(sourceComponent, info.getSourceComponent());
        
        // Colored cursor
        assertSame(info.getCursor(), TestUtils.getPrivateField(info, "coloredCursor"));
        
        // Cursor color
        assertEquals(new RGB(255, 255, 181), info.getCursorColor());
    }
    
    @Test
    public void testHasSourceComponent() {
        assertFalse(info.hasSourceComponent());

        IDiagramModelArchimateObject sourceComponent =
                ArchimateTestModel.createDiagramModelArchimateObject(IArchimateFactory.eINSTANCE.createBusinessActor());
        info.updateWithSourceComponent(sourceComponent);
        
        assertTrue(info.hasSourceComponent());
    }

    @Test
    public void testReset() throws Exception {
        IDiagramModelArchimateObject sourceComponent =
                ArchimateTestModel.createDiagramModelArchimateObject(IArchimateFactory.eINSTANCE.createBusinessActor());
        info.updateWithSourceComponent(sourceComponent);
        assertNotNull(info.getSourceComponent());
        assertEquals(new RGB(255, 255, 181), info.getCursorColor());
        assertSame(info.getCursor(), TestUtils.getPrivateField(info, "coloredCursor"));
        
        info.reset();
        assertNull(info.getSourceComponent());
        assertNull(info.getCursorColor());
        assertSame(info.getCursor(), TestUtils.getPrivateField(info, "defaultCursor"));
    }
}