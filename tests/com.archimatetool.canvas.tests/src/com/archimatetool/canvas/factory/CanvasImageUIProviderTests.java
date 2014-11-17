/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.swt.graphics.Color;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.canvas.model.ICanvasPackage;
import com.archimatetool.editor.diagram.editparts.diagram.DiagramImageEditPart;
import com.archimatetool.editor.ui.factory.AbstractElementUIProviderTests;

public class CanvasImageUIProviderTests extends AbstractElementUIProviderTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(CanvasImageUIProviderTests.class);
    }
    
    @Before
    public void runOnceBeforeAllTests() {
        provider = new CanvasImageUIProvider();
        expectedClass = ICanvasPackage.eINSTANCE.getCanvasModelImage();
    }
    
    @Override
    public void testCreateEditPart() {
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof DiagramImageEditPart);
    }
    
    @Override
    @Test
    public void testGetDefaultSize() {
        assertEquals(new Dimension(200, 150), provider.getDefaultSize());
    }

    @Override
    public void testGetDefaultColor() {
        Color color = provider.getDefaultColor();
        assertEquals(ColorConstants.white, color);
    }
}
