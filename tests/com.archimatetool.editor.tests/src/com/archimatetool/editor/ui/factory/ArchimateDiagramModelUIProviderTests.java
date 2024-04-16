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
import com.archimatetool.editor.diagram.editparts.ArchimateDiagramPart;
import com.archimatetool.editor.ui.factory.diagram.ArchimateDiagramModelUIProvider;
import com.archimatetool.model.IArchimatePackage;

public class ArchimateDiagramModelUIProviderTests extends AbstractObjectUIProviderTests {
    
    static Stream<Arguments> getParams() {
        return Stream.of(
                getParam(new ArchimateDiagramModelUIProvider(), IArchimatePackage.eINSTANCE.getArchimateDiagramModel())
        );
    }

    @Override
    @ParamsTest
    public void testCreateEditPart(IObjectUIProvider provider) {
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof ArchimateDiagramPart);
    }
}
