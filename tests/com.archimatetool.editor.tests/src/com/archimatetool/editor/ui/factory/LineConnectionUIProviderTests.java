/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.junit.jupiter.params.provider.Arguments;

import com.archimatetool.editor.ParamsTest;
import com.archimatetool.editor.ui.factory.diagram.DiagramConnectionUIProvider;
import com.archimatetool.model.IArchimatePackage;

public class LineConnectionUIProviderTests extends AbstractGraphicalObjectUIProviderTests {
    
    static Stream<Arguments> getParams() {
        return Stream.of(
                getParam(new DiagramConnectionUIProvider(), IArchimatePackage.eINSTANCE.getDiagramModelConnection())
        );
    }

    @Override
    @ParamsTest
    public void testCreateEditPart(IObjectUIProvider provider) {
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof AbstractConnectionEditPart);
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

    @Override
    @ParamsTest
    public void testGetDefaultSize(IGraphicalObjectUIProvider provider) {
        assertEquals(new Dimension(-1, -1), provider.getDefaultSize());
    }
}
