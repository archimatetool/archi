/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.editor.diagram.editparts.diagram.DiagramModelReferenceEditPart;
import com.archimatetool.editor.ui.factory.diagram.DiagramModelReferenceUIProvider;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelReference;

import junit.framework.JUnit4TestAdapter;

public class DiagramModelReferenceUIProviderTests extends AbstractGraphicalObjectUIProviderTests {
    
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
        EditPart editPart = getProvider().createEditPart();
        assertTrue(editPart instanceof DiagramModelReferenceEditPart);
    }
    
    @Override
    @Test
    public void testGetDefaultColor() {
        Color color = getProvider().getDefaultColor();
        assertEquals(new Color(220, 235, 235), color);
    }
    
    @Override
    @Test
    public void testGetDefaultSize() {
        assertEquals(new Dimension(120, 55), getProvider().getDefaultSize());
    }

    @Override
    public void testGetImageInstance() {
        IDiagramModelReference ref = (IDiagramModelReference)IArchimateFactory.eINSTANCE.create(expectedClass);
        ref.setReferencedModel(IArchimateFactory.eINSTANCE.createArchimateDiagramModel());
        ((AbstractObjectUIProvider)provider).setInstance(ref);
        
        Image image = provider.getImage();
        assertNotNull(image);
        
        ref.setReferencedModel(IArchimateFactory.eINSTANCE.createSketchModel());
        image = provider.getImage();
        assertNotNull(image);
    }
    
    @Test
    public void testHasIcon() {
        assertTrue(getProvider().hasIcon());
    }

}
