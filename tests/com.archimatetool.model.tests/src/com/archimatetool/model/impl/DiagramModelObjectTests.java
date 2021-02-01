/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IBounds;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ITextAlignment;


@SuppressWarnings("nls")
public abstract class DiagramModelObjectTests extends DiagramModelComponentTests {
    
    private IDiagramModelObject object;
    
    @Before
    public void runBeforeEachDiagramModelObjectTest() {
        object = (IDiagramModelObject)component;
    }

    @Test
    public void testGetID() {
        assertNotNull(component.getId());
    }
    
    @Test
    public void testGetDiagramModel() {
        assertNull(object.getDiagramModel());
        
        dm.getChildren().add(object);
        assertSame(dm, object.getDiagramModel());
    }

    @Test
    public void testGetArchimateModel() {
        assertNull(object.getArchimateModel());
        
        IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
        model.getDefaultFolderForObject(dm).getElements().add(dm);
        dm.getChildren().add(object);
        
        assertSame(model, object.getArchimateModel());
    }

    @Test
    public void testGetBounds() {
        assertNull(object.getBounds());
        IBounds bounds = IArchimateFactory.eINSTANCE.createBounds();
        object.setBounds(bounds);
        assertSame(bounds, object.getBounds());
    }
    
    @Test
    public void testGetSourceConnections() {
        assertTrue(object.getSourceConnections().isEmpty());
        
        IDiagramModelConnection conn = IArchimateFactory.eINSTANCE.createDiagramModelConnection();
        object.getSourceConnections().add(conn);
        assertSame(conn, object.getSourceConnections().get(0));
    }
    
    @Test
    public void testGetTargetConnections() {
        assertTrue(object.getTargetConnections().isEmpty());
        
        IDiagramModelConnection conn = IArchimateFactory.eINSTANCE.createDiagramModelConnection();
        object.getTargetConnections().add(conn);
        assertSame(conn, object.getTargetConnections().get(0));
    }
    
    @Test
    public void testGetFillColor() {
        assertNull(object.getFillColor());
        object.setFillColor("#ffffff");
        assertEquals("#ffffff", object.getFillColor());
    }

    @Test
    public void testGetFont() {
        assertNull(object.getFont());
        object.setFont("Arial");
        assertEquals("Arial", object.getFont());
    }
    
    @Test
    public void testGetFontColor() {
        assertNull(object.getFontColor());
        object.setFontColor("#ffffff");
        assertEquals("#ffffff", object.getFontColor());
    }
    
    @Test
    public void testGetAlpha() {
        assertEquals(255, object.getAlpha());
        object.setAlpha(100);
        assertEquals(100, object.getAlpha());
    }
    
    @Test
    public void testGetLineAlpha() {
        assertEquals(255, object.getLineAlpha());
        object.setLineAlpha(100);
        assertEquals(100, object.getLineAlpha());
    }
    
    @Test
    public void testGradient() {
        assertEquals(IDiagramModelObject.GRADIENT_NONE, object.getGradient());
        object.setGradient(2);
        assertEquals(2, object.getGradient());
    }
    
    @Test
    public void testIconVisible() {
        assertTrue(object.isIconVisible());
        object.setIconVisible(false);
        assertFalse(object.isIconVisible());
    }
    
    @Test
    public void testGetDefaultTextAlignment() {
        assertEquals(ITextAlignment.TEXT_ALIGNMENT_CENTER, object.getTextAlignment());
    }
    
    @Test
    public void testSetTextAlignment() {
        object.setTextAlignment(ITextAlignment.TEXT_ALIGNMENT_RIGHT);
        assertEquals(ITextAlignment.TEXT_ALIGNMENT_RIGHT, object.getTextAlignment());
    }
    
    @Test
    public void testGetLineWidth() {
        assertEquals(1, object.getLineWidth());
        object.setLineWidth(2);
        assertEquals(2, object.getLineWidth());
    }
    
    @Test
    public void testGetLineColor() {
        assertNull(object.getLineColor());
        object.setLineColor("#ffffff");
        assertEquals("#ffffff", object.getLineColor());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testAddConnection_Null() {
        object.addConnection(null);
    }
    
    @Test
    public void testAddConnection() {
        IDiagramModelConnection conn = IArchimateFactory.eINSTANCE.createDiagramModelConnection();
        
        // Should not be added if connection source or target not set
        object.addConnection(conn);
        assertTrue(object.getSourceConnections().isEmpty());
        assertTrue(object.getTargetConnections().isEmpty());
        
        // Now should be OK
        conn.connect(object, object);
        object.addConnection(conn);
        
        assertTrue(object.getSourceConnections().contains(conn));
        assertTrue(object.getTargetConnections().contains(conn));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testRemoveConnection_Null() {
        object.removeConnection(null);
    }

    @Test
    public void testRemoveConnection() {
        IDiagramModelConnection conn = IArchimateFactory.eINSTANCE.createDiagramModelConnection();
        conn.connect(object, object);

        object.addConnection(conn);
        assertTrue(object.getSourceConnections().contains(conn));
        assertTrue(object.getTargetConnections().contains(conn));
        
        // Try to remove bogus connection
        IDiagramModelConnection conn2 = IArchimateFactory.eINSTANCE.createDiagramModelConnection();
        conn2.connect(IArchimateFactory.eINSTANCE.createDiagramModelNote(), IArchimateFactory.eINSTANCE.createDiagramModelNote());
        object.removeConnection(conn2);
        assertTrue(object.getSourceConnections().contains(conn));
        assertTrue(object.getTargetConnections().contains(conn));

        // Now do it properly
        object.removeConnection(conn);
        assertTrue(object.getSourceConnections().isEmpty());
        assertTrue(object.getTargetConnections().isEmpty());
    }

    @Override
    @Test
    public void testGetCopy() {
        super.testGetCopy();
        
        IDiagramModelConnection conn = IArchimateFactory.eINSTANCE.createDiagramModelConnection();
        conn.connect(object, object);
        object.addConnection(conn);
        assertTrue(object.getSourceConnections().contains(conn));
        assertTrue(object.getTargetConnections().contains(conn));
        
        IBounds bounds = IArchimateFactory.eINSTANCE.createBounds(2, 4, 6, 8);
        object.setBounds(bounds);
        
        IDiagramModelObject copy = (IDiagramModelObject)object.getCopy();
        assertNotSame(object, copy);
        
        assertTrue(copy.getSourceConnections().isEmpty());
        assertTrue(copy.getTargetConnections().isEmpty());
        
        assertNotSame(bounds, copy.getBounds());
        assertEquals(bounds.getX(), copy.getBounds().getX());
        assertEquals(bounds.getY(), copy.getBounds().getY());
        assertEquals(bounds.getWidth(), copy.getBounds().getWidth());
        assertEquals(bounds.getHeight(), copy.getBounds().getHeight());
    }

}
