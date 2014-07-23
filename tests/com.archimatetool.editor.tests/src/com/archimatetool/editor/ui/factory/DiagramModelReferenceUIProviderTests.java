/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.editor.diagram.editparts.diagram.DiagramModelReferenceEditPart;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.factory.diagram.DiagramModelReferenceUIProvider;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelReference;

public class DiagramModelReferenceUIProviderTests extends AbstractElementUIProviderTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DiagramModelReferenceUIProviderTests.class);
    }
    
    @Before
    public void runOnceBeforeAllTests() {
        provider = new DiagramModelReferenceUIProvider();
        expectedClass = IArchimatePackage.eINSTANCE.getDiagramModelReference();
    }
    
    @Override
    public void testCreateEditPart() {
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof DiagramModelReferenceEditPart);
    }
    
    @Override
    @Test
    public void testGetDefaultColor() {
        Color color = provider.getDefaultColor();
        assertEquals(ColorFactory.get(220, 235, 235), color);
    }
    
    @Override
    @Test
    public void testGetDefaultSize() {
        assertEquals(new Dimension(120, 55), provider.getDefaultSize());
    }

    @Override
    public void testGetImageInstance() {
        IDiagramModelReference instance = (IDiagramModelReference)IArchimateFactory.eINSTANCE.create(expectedClass);
        instance.setReferencedModel(IArchimateFactory.eINSTANCE.createArchimateDiagramModel());
        Image image = provider.getImage(instance);
        assertNotNull(image);
        
        instance.setReferencedModel(IArchimateFactory.eINSTANCE.createSketchModel());
        image = provider.getImage(instance);
        assertNotNull(image);
    }
}
