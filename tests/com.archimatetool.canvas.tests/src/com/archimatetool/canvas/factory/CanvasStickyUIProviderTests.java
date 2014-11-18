/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.swt.graphics.Color;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.canvas.editparts.CanvasStickyEditPart;
import com.archimatetool.canvas.model.ICanvasPackage;
import com.archimatetool.editor.ui.factory.AbstractElementUIProviderTests;
import com.archimatetool.model.IArchimatePackage;

public class CanvasStickyUIProviderTests extends AbstractElementUIProviderTests {
    
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
        assertEquals(new Dimension(75, 63), provider.getDefaultSize());
    }

    @Override
    public void testGetDefaultColor() {
        Color color = provider.getDefaultColor();
        assertEquals(ColorConstants.white, color);
    }
    
    @Override
    public void testShouldExposeFeature() {
        EObject instance = expectedClass.getEPackage().getEFactoryInstance().create(expectedClass);
        assertFalse(provider.shouldExposeFeature(instance, IArchimatePackage.Literals.LINE_OBJECT__LINE_COLOR));
        assertTrue(provider.shouldExposeFeature(instance, null));
    }
}
