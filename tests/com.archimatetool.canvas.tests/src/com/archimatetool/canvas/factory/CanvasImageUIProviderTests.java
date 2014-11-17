/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.factory;

import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.eclipse.gef.EditPart;
import org.junit.Before;

import com.archimatetool.canvas.model.ICanvasPackage;
import com.archimatetool.editor.diagram.editparts.diagram.DiagramImageEditPart;
import com.archimatetool.editor.ui.factory.DiagramModelImageUIProviderTests;

public class CanvasImageUIProviderTests extends DiagramModelImageUIProviderTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(CanvasImageUIProviderTests.class);
    }
    
    @Override
    @Before
    public void runOnceBeforeAllTests() {
        provider = new CanvasImageUIProvider();
        expectedClass = ICanvasPackage.eINSTANCE.getCanvasModelImage();
    }
    
    @Override
    public void testCreateEditPart() {
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof DiagramImageEditPart);
    }
}
