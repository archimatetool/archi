/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.eclipse.gef.EditPart;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.archimatetool.editor.Logger;
import com.archimatetool.editor.diagram.editparts.business.BusinessActorEditPart;
import com.archimatetool.editor.diagram.editparts.connections.AccessConnectionEditPart;
import com.archimatetool.editor.diagram.editparts.diagram.EmptyEditPart;
import com.archimatetool.editor.diagram.editparts.diagram.GroupEditPart;
import com.archimatetool.editor.diagram.editparts.diagram.LineConnectionEditPart;
import com.archimatetool.editor.diagram.editparts.diagram.NoteEditPart;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelGroup;
import com.archimatetool.model.IDiagramModelNote;
import com.archimatetool.model.IRelationship;
import com.archimatetool.tests.TestUtils;


public class ArchimateDiagramEditPartFactoryTests {
    
    private ArchimateDiagramEditPartFactory editPartFactory;
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ArchimateDiagramEditPartFactoryTests.class);
    }
    
    @BeforeClass
    public static void runOnceBeforeAllTests() {
        // Need this
        TestUtils.ensureDefaultDisplay();
    }
    
    @Before
    public void runOnceBeforeEachTest() {
        editPartFactory = new ArchimateDiagramEditPartFactory();
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
    public void testArchimateDiagramPart() {
        IArchimateDiagramModel dm = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
        EditPart editPart = editPartFactory.createEditPart(null, dm);
        assertTrue(editPart instanceof ArchimateDiagramPart);
        assertEquals(dm, editPart.getModel());
    }

    @Test
    public void testArchimateObjectEditPart() {
        IArchimateElement element = IArchimateFactory.eINSTANCE.createBusinessActor();
        IDiagramModelArchimateObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
        dmo.setArchimateElement(element);
        
        EditPart editPart = editPartFactory.createEditPart(null, dmo);
        assertTrue(editPart instanceof BusinessActorEditPart);
        assertEquals(dmo, editPart.getModel());
    }
    
    @Test
    public void testArchimateConnectionEditPart() {
        IRelationship relation = IArchimateFactory.eINSTANCE.createAccessRelationship();
        IDiagramModelArchimateConnection conn = IArchimateFactory.eINSTANCE.createDiagramModelArchimateConnection();
        conn.setRelationship(relation);
        
        EditPart editPart = editPartFactory.createEditPart(null, conn);
        assertTrue(editPart instanceof AccessConnectionEditPart);
        assertEquals(conn, editPart.getModel());
    }

    @Test
    public void testLineConnectionEditPart() {
        IDiagramModelConnection conn = IArchimateFactory.eINSTANCE.createDiagramModelConnection();
        
        EditPart editPart = editPartFactory.createEditPart(null, conn);
        assertTrue(editPart instanceof LineConnectionEditPart);
        assertEquals(conn, editPart.getModel());
    }

    @Test
    public void testGroupEditPart() {
        IDiagramModelGroup group = IArchimateFactory.eINSTANCE.createDiagramModelGroup();
        EditPart editPart = editPartFactory.createEditPart(null, group);
        assertTrue(editPart instanceof GroupEditPart);
        assertEquals(group, editPart.getModel());
    }
    
    @Test
    public void testNoteEditPart() {
        IDiagramModelNote note = IArchimateFactory.eINSTANCE.createDiagramModelNote();
        EditPart editPart = editPartFactory.createEditPart(null, note);
        assertTrue(editPart instanceof NoteEditPart);
        assertEquals(note, editPart.getModel());
    }
}
