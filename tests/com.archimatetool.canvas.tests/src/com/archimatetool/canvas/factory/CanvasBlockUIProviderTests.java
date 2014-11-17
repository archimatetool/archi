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

import com.archimatetool.canvas.editparts.CanvasBlockEditPart;
import com.archimatetool.canvas.model.ICanvasPackage;
import com.archimatetool.editor.ui.factory.AbstractElementUIProviderTests;

public class CanvasBlockUIProviderTests extends AbstractElementUIProviderTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(CanvasBlockUIProviderTests.class);
    }
    
    @Before
    public void runOnceBeforeAllTests() {
        provider = new CanvasBlockUIProvider();
        expectedClass = ICanvasPackage.eINSTANCE.getCanvasModelBlock();
    }
    
    @Override
    public void testCreateEditPart() {
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof CanvasBlockEditPart);
    }
    
    @Override
    @Test
    public void testGetDefaultSize() {
        assertEquals(new Dimension(200, 200), provider.getDefaultSize());
    }

    @Override
    public void testGetDefaultColor() {
        Color color = provider.getDefaultColor();
        assertEquals(ColorConstants.white, color);
    }
}
