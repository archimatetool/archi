/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.sketch.editparts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.gef.EditPart;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.editor.Logger;
import com.archimatetool.editor.diagram.editparts.DiagramConnectionEditPart;
import com.archimatetool.editor.diagram.editparts.diagram.EmptyEditPart;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.model.IDiagramModelReference;
import com.archimatetool.model.ISketchModel;
import com.archimatetool.model.ISketchModelActor;
import com.archimatetool.model.ISketchModelSticky;

import junit.framework.JUnit4TestAdapter;


public class SketchEditPartFactoryTests {
    
    private SketchEditPartFactory editPartFactory;
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(SketchEditPartFactoryTests.class);
    }
    
    @Before
    public void runOnceBeforeEachTest() {
        editPartFactory = new SketchEditPartFactory();
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
    public void testSketchDiagramPart() {
        ISketchModel sm = IArchimateFactory.eINSTANCE.createSketchModel();
        EditPart editPart = editPartFactory.createEditPart(null, sm);
        assertTrue(editPart instanceof SketchDiagramPart);
        assertEquals(sm, editPart.getModel());
    }

    @Test
    public void testSketchGroupEditPart() {
        IDiagramModelGroup group = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        EditPart editPart = editPartFactory.createEditPart(null, group);
        assertTrue(editPart instanceof SketchGroupEditPart);
        assertEquals(group, editPart.getModel());
    }
 
    @Test
    public void testSketchActorEditPart() {
        ISketchModelActor actor = IArchimateFactory.eINSTANCE.createSketchModelActor();
        EditPart editPart = editPartFactory.createEditPart(null, actor);
        assertTrue(editPart instanceof SketchActorEditPart);
        assertEquals(actor, editPart.getModel());
    }

    @Test
    public void testSketchStickyEditPart() {
        ISketchModelSticky sticky = IArchimateFactory.eINSTANCE.createSketchModelSticky();
        EditPart editPart = editPartFactory.createEditPart(null, sticky);
        assertTrue(editPart instanceof StickyEditPart);
        assertEquals(sticky, editPart.getModel());
    }

    @Test
    public void testSketchDiagramModelReferenceEditPart() {
        IDiagramModelReference ref = IArchimateFactory.eINSTANCE.createDiagramModelReference();
        EditPart editPart = editPartFactory.createEditPart(null, ref);
        assertTrue(editPart instanceof SketchDiagramModelReferenceEditPart);
        assertEquals(ref, editPart.getModel());
    }

    @Test
    public void testLineConnectionEditPart() {
        IDiagramModelConnection conn = IArchimateFactory.eINSTANCE.createDiagramModelConnection();
        
        EditPart editPart = editPartFactory.createEditPart(null, conn);
        assertTrue(editPart instanceof DiagramConnectionEditPart);
        assertEquals(conn, editPart.getModel());
    }
}
