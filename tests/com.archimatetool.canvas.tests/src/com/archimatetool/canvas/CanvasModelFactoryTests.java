/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.junit.BeforeClass;
import org.junit.Test;

import com.archimatetool.canvas.model.ICanvasModelBlock;
import com.archimatetool.canvas.model.ICanvasModelConnection;
import com.archimatetool.canvas.model.ICanvasModelImage;
import com.archimatetool.canvas.model.ICanvasModelSticky;
import com.archimatetool.canvas.model.ICanvasPackage;
import com.archimatetool.editor.diagram.ArchimateDiagramEditor;
import com.archimatetool.editor.diagram.ICreationFactory;
import com.archimatetool.editor.diagram.sketch.SketchEditor;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.tests.TestUtils;


@SuppressWarnings("nls")
public class CanvasModelFactoryTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(CanvasModelFactoryTests.class);
    }
    @BeforeClass
    public static void runOnceBeforeAllTests() {
        // Need this
        TestUtils.ensureDefaultDisplay();
    }
    
    @Test
    public void testIsUsedFor() {
        ICreationFactory factory = new CanvasModelFactory(null);
        assertTrue(factory.isUsedFor(new CanvasEditor()));
        assertFalse(factory.isUsedFor(new SketchEditor()));
        assertFalse(factory.isUsedFor(new ArchimateDiagramEditor()));
    }
    
    @Test
    public void testGetNewObjectCanvasSticky() {
        ICreationFactory factory = new CanvasModelFactory(ICanvasPackage.eINSTANCE.getCanvasModelSticky(), ColorFactory.COLOR_APPLICATION);
        ICanvasModelSticky sticky = (ICanvasModelSticky)factory.getNewObject();
        assertEquals(ColorFactory.convertColorToString(ColorFactory.COLOR_APPLICATION), sticky.getFillColor());
        assertEquals("#C0C0C0", sticky.getBorderColor());
    }

    @Test
    public void testGetNewObjectCanvasBlock() {
        ICreationFactory factory = new CanvasModelFactory(ICanvasPackage.eINSTANCE.getCanvasModelBlock());
        ICanvasModelBlock block = (ICanvasModelBlock)factory.getNewObject();
        assertEquals("#000000", block.getBorderColor());
    }

    @Test
    public void testGetNewObjectCanvasImage() {
        ICreationFactory factory = new CanvasModelFactory(ICanvasPackage.eINSTANCE.getCanvasModelImage());
        ICanvasModelImage image = (ICanvasModelImage)factory.getNewObject();
        assertEquals("#000000", image.getBorderColor());
    }

    @Test
    public void testGetNewObjectConnection() {
        ICreationFactory factory = new CanvasModelFactory(ICanvasPackage.eINSTANCE.getCanvasModelConnection(), 2);
        ICanvasModelConnection connection = (ICanvasModelConnection)factory.getNewObject();
        assertEquals("", connection.getName());
        assertEquals(2, connection.getType());
    }
}
