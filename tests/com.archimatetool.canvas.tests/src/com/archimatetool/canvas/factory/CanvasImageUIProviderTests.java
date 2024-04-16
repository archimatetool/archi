/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.factory;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.eclipse.gef.EditPart;
import org.junit.jupiter.params.provider.Arguments;

import com.archimatetool.canvas.model.ICanvasPackage;
import com.archimatetool.editor.ParamsTest;
import com.archimatetool.editor.diagram.editparts.diagram.DiagramImageEditPart;
import com.archimatetool.editor.ui.factory.DiagramModelImageUIProviderTests;
import com.archimatetool.editor.ui.factory.IObjectUIProvider;

public class CanvasImageUIProviderTests extends DiagramModelImageUIProviderTests {
    
    static Stream<Arguments> getParams() {
        return Stream.of(
                getParam(new CanvasImageUIProvider(), ICanvasPackage.eINSTANCE.getCanvasModelImage())
        );
    }

    @Override
    @ParamsTest
    public void testCreateEditPart(IObjectUIProvider provider) {
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof DiagramImageEditPart);
    }
}
