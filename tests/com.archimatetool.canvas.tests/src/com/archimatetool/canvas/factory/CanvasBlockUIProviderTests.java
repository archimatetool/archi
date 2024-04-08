/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.swt.graphics.Color;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.archimatetool.canvas.editparts.CanvasBlockEditPart;
import com.archimatetool.canvas.model.ICanvasPackage;
import com.archimatetool.editor.ui.factory.AbstractGraphicalObjectUIProviderTests;
import com.archimatetool.editor.ui.factory.IGraphicalObjectUIProvider;
import com.archimatetool.editor.ui.factory.IObjectUIProvider;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ITextAlignment;

public class CanvasBlockUIProviderTests extends AbstractGraphicalObjectUIProviderTests {
    
    static Stream<Arguments> getParams() {
        return Stream.of(
                getParam(new CanvasBlockUIProvider(), ICanvasPackage.eINSTANCE.getCanvasModelBlock())
        );
    }

    @Override
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testCreateEditPart(IObjectUIProvider provider) {
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof CanvasBlockEditPart);
    }
    
    @Override
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetDefaultSize(IGraphicalObjectUIProvider provider) {
        assertEquals(new Dimension(200, 200), provider.getDefaultSize());
    }

    @Override
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetDefaultColor(IGraphicalObjectUIProvider provider) {
        Color color = provider.getDefaultColor();
        assertEquals(ColorConstants.white, color);
    }
    
    @Override
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testShouldExposeFeature(IObjectUIProvider provider) {
        assertFalse(provider.shouldExposeFeature(IArchimatePackage.Literals.LINE_OBJECT__LINE_COLOR.getName()));
        assertTrue(provider.shouldExposeFeature((String)null));
    }
    
    @Override
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetDefaultTextAlignment(IGraphicalObjectUIProvider provider) {
        assertEquals(ITextAlignment.TEXT_ALIGNMENT_LEFT, provider.getDefaultTextAlignment());
    }
}
