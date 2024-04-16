/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.Stream;

import org.eclipse.gef.EditPart;
import org.junit.jupiter.params.provider.Arguments;

import com.archimatetool.editor.ParamsTest;
import com.archimatetool.editor.diagram.sketch.editparts.SketchDiagramPart;
import com.archimatetool.editor.ui.factory.sketch.SketchModelUIProvider;
import com.archimatetool.model.IArchimatePackage;

public class SketchModelUIProviderTests extends AbstractObjectUIProviderTests {
    
    static Stream<Arguments> getParams() {
        return Stream.of(
                getParam(new SketchModelUIProvider(), IArchimatePackage.eINSTANCE.getSketchModel())
        );
    }

    @Override
    @ParamsTest
    public void testCreateEditPart(IObjectUIProvider provider) {
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof SketchDiagramPart);
    }
}
