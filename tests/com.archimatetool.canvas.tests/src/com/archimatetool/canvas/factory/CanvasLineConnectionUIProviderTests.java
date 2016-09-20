/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.EditPart;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.canvas.model.ICanvasPackage;
import com.archimatetool.editor.diagram.editparts.DiagramConnectionEditPart;
import com.archimatetool.editor.ui.factory.AbstractGraphicalObjectUIProviderTests;

import junit.framework.JUnit4TestAdapter;

public class CanvasLineConnectionUIProviderTests extends AbstractGraphicalObjectUIProviderTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(CanvasLineConnectionUIProviderTests.class);
    }
    
    @Before
    public void runOnceBeforeAllTests() {
        provider = new CanvasConnectionUIProvider();
        expectedClass = ICanvasPackage.eINSTANCE.getCanvasModelConnection();
    }
    
    @Override
    public void testCreateEditPart() {
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof DiagramConnectionEditPart);
    }
    
    @Override
    @Test
    public void testGetDefaultColor() {
        assertEquals(ColorConstants.black, getProvider().getDefaultColor());
    }
    
    @Override
    public void testGetDefaultLineColor() {
        assertEquals(ColorConstants.black, getProvider().getDefaultLineColor());
    }


}
