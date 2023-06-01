/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import static org.junit.Assert.assertTrue;

import org.eclipse.gef.EditPart;
import org.junit.Before;

import com.archimatetool.editor.diagram.editparts.ArchimateDiagramPart;
import com.archimatetool.editor.ui.factory.diagram.ArchimateDiagramModelUIProvider;
import com.archimatetool.model.IArchimatePackage;

public class ArchimateDiagramModelUIProviderTests extends AbstractObjectUIProviderTests {
    
    @Before
    public void runOnceBeforeAllTests() {
        provider = new ArchimateDiagramModelUIProvider();
        expectedClass = IArchimatePackage.eINSTANCE.getArchimateDiagramModel();
    }
    
    @Override
    public void testCreateEditPart() {
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof ArchimateDiagramPart);
    }
}
