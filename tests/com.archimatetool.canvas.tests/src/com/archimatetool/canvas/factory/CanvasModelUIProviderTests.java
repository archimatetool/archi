/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.factory;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.eclipse.gef.EditPart;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.archimatetool.canvas.editparts.CanvasDiagramPart;
import com.archimatetool.canvas.model.ICanvasPackage;
import com.archimatetool.editor.ui.factory.AbstractObjectUIProviderTests;
import com.archimatetool.editor.ui.factory.IObjectUIProvider;

public class CanvasModelUIProviderTests extends AbstractObjectUIProviderTests {
    
    static Stream<Arguments> getParams() {
        return Stream.of(
                getParam(new CanvasModelUIProvider(), ICanvasPackage.eINSTANCE.getCanvasModel())
        );
    }

    @Override
    @ParameterizedTest
    @MethodSource(PARAMS_METHOD)
    public void testCreateEditPart(IObjectUIProvider provider) {
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof CanvasDiagramPart);
    }
}
