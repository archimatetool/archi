/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Color;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.archimatetool.model.ITextAlignment;
import com.archimatetool.model.ITextPosition;



public abstract class AbstractGraphicalObjectUIProviderTests extends AbstractObjectUIProviderTests {

    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetDefaultColor(IGraphicalObjectUIProvider provider) {
        Color color = provider.getDefaultColor();
        assertEquals(ColorConstants.white, color);
    }

    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetDefaultLineColor(IGraphicalObjectUIProvider provider) {
        Color color = provider.getDefaultLineColor();
        assertEquals(new Color(92, 92, 92), color);
    }

    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetDefaultSize(IGraphicalObjectUIProvider provider) {
        assertEquals(new Dimension(-1, -1), provider.getDefaultSize());
    }
    
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetDefaultTextAlignment(IGraphicalObjectUIProvider provider) {
        assertEquals(ITextAlignment.TEXT_ALIGNMENT_CENTER, provider.getDefaultTextAlignment());
    }

    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetDefaultTextPosition(IGraphicalObjectUIProvider provider) {
        assertEquals(ITextPosition.TEXT_POSITION_TOP, provider.getDefaultTextPosition());
    }

}
