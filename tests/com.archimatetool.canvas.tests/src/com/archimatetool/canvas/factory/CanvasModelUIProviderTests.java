/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.factory;

import static org.junit.Assert.assertTrue;

import org.eclipse.gef.EditPart;
import org.junit.Before;

import com.archimatetool.canvas.editparts.CanvasDiagramPart;
import com.archimatetool.canvas.model.ICanvasPackage;
import com.archimatetool.editor.ui.factory.AbstractObjectUIProviderTests;

public class CanvasModelUIProviderTests extends AbstractObjectUIProviderTests {
    
    @Before
    public void runOnceBeforeAllTests() {
        provider = new CanvasModelUIProvider();
        expectedClass = ICanvasPackage.eINSTANCE.getCanvasModel();
    }
    
    @Override
    public void testCreateEditPart() {
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof CanvasDiagramPart);
    }
}
