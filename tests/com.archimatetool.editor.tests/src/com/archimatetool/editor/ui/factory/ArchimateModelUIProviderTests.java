/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import static org.junit.Assert.assertNull;

import org.junit.Before;

import com.archimatetool.editor.ui.factory.model.ArchimateModelUIProvider;
import com.archimatetool.model.IArchimatePackage;

public class ArchimateModelUIProviderTests extends AbstractObjectUIProviderTests {
    
    @Before
    public void runOnceBeforeAllTests() {
        provider = new ArchimateModelUIProvider();
        expectedClass = IArchimatePackage.eINSTANCE.getArchimateModel();
    }
    
    @Override
    public void testCreateEditPart() {
        assertNull(provider.createEditPart());
    }
}
