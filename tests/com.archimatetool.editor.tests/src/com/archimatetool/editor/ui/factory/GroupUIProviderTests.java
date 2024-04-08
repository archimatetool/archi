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
import org.eclipse.swt.graphics.Color;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.archimatetool.editor.diagram.editparts.diagram.GroupEditPart;
import com.archimatetool.editor.ui.factory.diagram.GroupUIProvider;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ITextAlignment;

public class GroupUIProviderTests extends AbstractGraphicalObjectUIProviderTests {
    
    static Stream<Arguments> getParams() {
        return Stream.of(
                getParam(new GroupUIProvider(), IArchimatePackage.eINSTANCE.getDiagramModelGroup())
        );
    }

    @Override
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testCreateEditPart(IObjectUIProvider provider) {
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof GroupEditPart);
    }
    
    @Override
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetDefaultColor(IGraphicalObjectUIProvider provider) {
        assertEquals(new Color(210, 215, 215), provider.getDefaultColor());
    }
    
    @Override
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetDefaultSize(IGraphicalObjectUIProvider provider) {
        assertEquals(new Dimension(400, 140), provider.getDefaultSize());
    }

    @Override
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testShouldExposeFeature(IObjectUIProvider provider) {
        super.testShouldExposeFeature(provider);
        assertTrue(provider.shouldExposeFeature(IArchimatePackage.Literals.TEXT_ALIGNMENT__TEXT_ALIGNMENT.getName()));
    }

    @Override
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testGetDefaultTextAlignment(IGraphicalObjectUIProvider provider) {
        assertEquals(ITextAlignment.TEXT_ALIGNMENT_LEFT, provider.getDefaultTextAlignment());
    }

}
