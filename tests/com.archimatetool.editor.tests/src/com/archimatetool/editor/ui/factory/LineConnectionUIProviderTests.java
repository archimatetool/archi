/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.editor.ui.factory.diagram.DiagramConnectionUIProvider;
import com.archimatetool.model.IArchimatePackage;

import junit.framework.JUnit4TestAdapter;

public class LineConnectionUIProviderTests extends AbstractGraphicalObjectUIProviderTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(LineConnectionUIProviderTests.class);
    }
    
    @Before
    public void runOnceBeforeAllTests() {
        provider = new DiagramConnectionUIProvider();
        expectedClass = IArchimatePackage.eINSTANCE.getDiagramModelConnection();
    }
    
    @Override
    public void testCreateEditPart() {
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof AbstractConnectionEditPart);
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

    @Override
    @Test
    public void testGetDefaultSize() {
        assertEquals(new Dimension(-1, -1), getProvider().getDefaultSize());
    }
}
