/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.editparts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.gef.EditPart;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.canvas.model.ICanvasFactory;
import com.archimatetool.canvas.model.ICanvasModel;
import com.archimatetool.canvas.model.ICanvasModelBlock;
import com.archimatetool.canvas.model.ICanvasModelSticky;
import com.archimatetool.editor.Logger;
import com.archimatetool.editor.diagram.editparts.DiagramConnectionEditPart;
import com.archimatetool.editor.diagram.editparts.diagram.DiagramImageEditPart;
import com.archimatetool.editor.diagram.editparts.diagram.EmptyEditPart;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelImage;
import com.archimatetool.model.IDiagramModelReference;

import junit.framework.JUnit4TestAdapter;


public class CanvasModelEditPartFactoryTests {
    
    private CanvasModelEditPartFactory editPartFactory;
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(CanvasModelEditPartFactoryTests.class);
    }
    
    @Before
    public void runOnceBeforeEachTest() {
        editPartFactory = new CanvasModelEditPartFactory();
    }

    @Test
    public void testNull() {
        assertNull(editPartFactory.createEditPart(null, null));
    }

    @Test
    public void testEmptyEditPart() {
        Logger.enabled = false;
        assertTrue(editPartFactory.createEditPart(null, new Object()) instanceof EmptyEditPart);
    }
    
    @Test
    public void testCanvasDiagramPart() {
        ICanvasModel cm = ICanvasFactory.eINSTANCE.createCanvasModel();
        EditPart editPart = editPartFactory.createEditPart(null, cm);
        assertTrue(editPart instanceof CanvasDiagramPart);
        assertEquals(cm, editPart.getModel());
    }

    @Test
    public void testCanvasBlockActorEditPart() {
        ICanvasModelBlock block = ICanvasFactory.eINSTANCE.createCanvasModelBlock();
        EditPart editPart = editPartFactory.createEditPart(null, block);
        assertTrue(editPart instanceof CanvasBlockEditPart);
        assertEquals(block, editPart.getModel());
    }

    @Test
    public void testCanvasStickyEditPart() {
        ICanvasModelSticky sticky = ICanvasFactory.eINSTANCE.createCanvasModelSticky();
        EditPart editPart = editPartFactory.createEditPart(null, sticky);
        assertTrue(editPart instanceof CanvasStickyEditPart);
        assertEquals(sticky, editPart.getModel());
    }

    @Test
    public void testDiagramImageEditPart() {
        IDiagramModelImage image = IArchimateFactory.eINSTANCE.createDiagramModelImage();
        EditPart editPart = editPartFactory.createEditPart(null, image);
        assertTrue(editPart instanceof DiagramImageEditPart);
        assertEquals(image, editPart.getModel());
    }

    @Test
    public void testSketchDiagramModelReferenceEditPart() {
        IDiagramModelReference ref = IArchimateFactory.eINSTANCE.createDiagramModelReference();
        EditPart editPart = editPartFactory.createEditPart(null, ref);
        assertTrue(editPart instanceof CanvasDiagramModelReferenceEditPart);
        assertEquals(ref, editPart.getModel());
    }

    @Test
    public void testCanvasConnectionEditPart() {
        IDiagramModelConnection conn = ICanvasFactory.eINSTANCE.createCanvasModelConnection();
        
        EditPart editPart = editPartFactory.createEditPart(null, conn);
        assertTrue(editPart instanceof DiagramConnectionEditPart);
        assertEquals(conn, editPart.getModel());
    }
}
