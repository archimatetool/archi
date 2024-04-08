/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.archimatetool.editor.diagram.sketch.editparts.StickyEditPart;
import com.archimatetool.editor.ui.factory.sketch.SketchStickyUIProvider;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ITextAlignment;

public class SketchStickyUIProviderTests extends AbstractGraphicalObjectUIProviderTests {
    
    static Stream<Arguments> getParams() {
        return Stream.of(
                getParam(new SketchStickyUIProvider(), IArchimatePackage.eINSTANCE.getSketchModelSticky())
        );
    }

    @Override
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testCreateEditPart(IObjectUIProvider provider) {
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof StickyEditPart);
    }
    
    @Override
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetDefaultSize(IGraphicalObjectUIProvider provider) {
        assertEquals(new Dimension(135, 70), provider.getDefaultSize());
    }

    @Override
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetDefaultTextAlignment(IGraphicalObjectUIProvider provider) {
        assertEquals(ITextAlignment.TEXT_ALIGNMENT_LEFT, provider.getDefaultTextAlignment());
    }
}
