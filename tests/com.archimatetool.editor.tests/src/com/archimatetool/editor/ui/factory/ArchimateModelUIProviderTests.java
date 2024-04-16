/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

import com.archimatetool.editor.ParamsTest;
import com.archimatetool.editor.ui.factory.model.ArchimateModelUIProvider;
import com.archimatetool.model.IArchimatePackage;

public class ArchimateModelUIProviderTests extends AbstractObjectUIProviderTests {
    
    static Stream<Arguments> getParams() {
        return Stream.of(
                getParam(new ArchimateModelUIProvider(), IArchimatePackage.eINSTANCE.getArchimateModel())
        );
    }

    @Override
    @ParamsTest
    public void testCreateEditPart(IObjectUIProvider provider) {
        assertNull(provider.createEditPart());
    }
}
