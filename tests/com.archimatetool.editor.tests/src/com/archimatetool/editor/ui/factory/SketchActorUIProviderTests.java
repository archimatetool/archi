/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.swt.graphics.Color;
import org.junit.jupiter.params.provider.Arguments;

import com.archimatetool.editor.ParamsTest;
import com.archimatetool.editor.diagram.sketch.editparts.SketchActorEditPart;
import com.archimatetool.editor.ui.factory.sketch.SketchActorUIProvider;
import com.archimatetool.model.IArchimatePackage;

public class SketchActorUIProviderTests extends AbstractGraphicalObjectUIProviderTests {
    
    static Stream<Arguments> getParams() {
        return Stream.of(
                getParam(new SketchActorUIProvider(), IArchimatePackage.eINSTANCE.getSketchModelActor())
        );
    }

    @Override
    @ParamsTest
    public void testCreateEditPart(IObjectUIProvider provider) {
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof SketchActorEditPart);
    }
    
    @Override
    @ParamsTest
    public void testGetDefaultSize(IGraphicalObjectUIProvider provider) {
        assertEquals(new Dimension(75, 100), provider.getDefaultSize());
    }

    @Override
    @ParamsTest
    public void testGetDefaultColor(IGraphicalObjectUIProvider provider) {
        Color color = provider.getDefaultColor();
        assertEquals(ColorConstants.black, color);
    }
    
    @Override
    @ParamsTest
    public void testShouldExposeFeature(IObjectUIProvider provider) {
        super.testShouldExposeFeature(provider);
        assertFalse(provider.shouldExposeFeature(IArchimatePackage.Literals.TEXT_ALIGNMENT__TEXT_ALIGNMENT.getName()));
        assertFalse(provider.shouldExposeFeature(IArchimatePackage.Literals.LINE_OBJECT__LINE_COLOR.getName()));
    }

}
