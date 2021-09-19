/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import static org.junit.Assert.assertEquals;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Color;
import org.junit.Test;

import com.archimatetool.model.ITextAlignment;
import com.archimatetool.model.ITextPosition;

import junit.framework.JUnit4TestAdapter;



public abstract class AbstractGraphicalObjectUIProviderTests extends AbstractObjectUIProviderTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(AbstractGraphicalObjectUIProviderTests.class);
    }
    
    protected IGraphicalObjectUIProvider getProvider() {
        return (IGraphicalObjectUIProvider)provider;
    }
    
    @Test
    public void testGetDefaultColor() {
        Color color = getProvider().getDefaultColor();
        assertEquals(ColorConstants.white, color);
    }

    @Test
    public void testGetDefaultLineColor() {
        Color color = getProvider().getDefaultLineColor();
        assertEquals(new Color(92, 92, 92), color);
    }

    @Test
    public void testGetDefaultSize() {
        assertEquals(new Dimension(-1, -1), getProvider().getDefaultSize());
    }
    
    @Test
    public void testGetDefaultTextAlignment() {
        assertEquals(ITextAlignment.TEXT_ALIGNMENT_CENTER, getProvider().getDefaultTextAlignment());
    }

    @Test
    public void testGetDefaultTextPosition() {
        assertEquals(ITextPosition.TEXT_POSITION_TOP, getProvider().getDefaultTextPosition());
    }

}
