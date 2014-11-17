/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.EditPart;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.canvas.editparts.CanvasLineConnectionEditPart;
import com.archimatetool.canvas.model.ICanvasPackage;
import com.archimatetool.editor.ui.factory.AbstractElementUIProviderTests;

public class CanvasLineConnectionUIProviderTests extends AbstractElementUIProviderTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(CanvasLineConnectionUIProviderTests.class);
    }
    
    @Before
    public void runOnceBeforeAllTests() {
        provider = new CanvasLineConnectionUIProvider();
        expectedClass = ICanvasPackage.eINSTANCE.getCanvasModelConnection();
    }
    
    @Override
    public void testCreateEditPart() {
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof CanvasLineConnectionEditPart);
    }
    
    @Override
    @Test
    public void testGetDefaultColor() {
        assertEquals(ColorConstants.black, provider.getDefaultColor());
    }
    
    @Override
    public void testGetDefaultLineColor() {
        assertEquals(ColorConstants.black, provider.getDefaultLineColor());
    }


}
