/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.EditPart;
import org.junit.jupiter.params.provider.Arguments;

import com.archimatetool.canvas.model.ICanvasPackage;
import com.archimatetool.editor.ParamsTest;
import com.archimatetool.editor.diagram.editparts.DiagramConnectionEditPart;
import com.archimatetool.editor.ui.factory.AbstractGraphicalObjectUIProviderTests;
import com.archimatetool.editor.ui.factory.IGraphicalObjectUIProvider;
import com.archimatetool.editor.ui.factory.IObjectUIProvider;

public class CanvasLineConnectionUIProviderTests extends AbstractGraphicalObjectUIProviderTests {
    
    static Stream<Arguments> getParams() {
        return Stream.of(
                getParam(new CanvasConnectionUIProvider(), ICanvasPackage.eINSTANCE.getCanvasModelConnection())
        );
    }

    @Override
    @ParamsTest
    public void testCreateEditPart(IObjectUIProvider provider) {
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof DiagramConnectionEditPart);
    }
    
    @Override
    @ParamsTest
    public void testGetDefaultColor(IGraphicalObjectUIProvider provider) {
        assertEquals(ColorConstants.black, provider.getDefaultColor());
    }
    
    @Override
    @ParamsTest
    public void testGetDefaultLineColor(IGraphicalObjectUIProvider provider) {
        assertEquals(ColorConstants.black, provider.getDefaultLineColor());
    }

}
