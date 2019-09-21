/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.swt.graphics.Color;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.canvas.editparts.CanvasStickyEditPart;
import com.archimatetool.canvas.model.ICanvasPackage;
import com.archimatetool.editor.ui.factory.AbstractGraphicalObjectUIProviderTests;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ITextPosition;

import junit.framework.JUnit4TestAdapter;

public class CanvasStickyUIProviderTests extends AbstractGraphicalObjectUIProviderTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(CanvasStickyUIProviderTests.class);
    }
    
    @Before
    public void runOnceBeforeAllTests() {
        provider = new CanvasStickyUIProvider();
        expectedClass = ICanvasPackage.eINSTANCE.getCanvasModelSticky();
    }
    
    @Override
    public void testCreateEditPart() {
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof CanvasStickyEditPart);
    }
    
    @Override
    @Test
    public void testGetDefaultSize() {
        assertEquals(new Dimension(75, 63), getProvider().getDefaultSize());
    }

    @Override
    public void testGetDefaultColor() {
        Color color = getProvider().getDefaultColor();
        assertEquals(ColorConstants.white, color);
    }
    
    @Override
    public void testShouldExposeFeature() {
        assertFalse(provider.shouldExposeFeature(IArchimatePackage.Literals.LINE_OBJECT__LINE_COLOR.getName()));
        assertTrue(provider.shouldExposeFeature((String)null));
    }
    
    @Override
    @Test
    public void testGetDefaultTextPosition() {
        assertEquals(ITextPosition.TEXT_POSITION_CENTRE, getProvider().getDefaultTextPosition());
    }
}
