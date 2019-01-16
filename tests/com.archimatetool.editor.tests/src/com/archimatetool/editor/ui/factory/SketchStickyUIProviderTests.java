/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.editor.diagram.sketch.editparts.StickyEditPart;
import com.archimatetool.editor.ui.factory.sketch.SketchStickyUIProvider;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ITextAlignment;

import junit.framework.JUnit4TestAdapter;

public class SketchStickyUIProviderTests extends AbstractGraphicalObjectUIProviderTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(SketchStickyUIProviderTests.class);
    }
    
    @Before
    public void runOnceBeforeAllTests() {
        provider = new SketchStickyUIProvider();
        expectedClass = IArchimatePackage.eINSTANCE.getSketchModelSticky();
    }
    
    @Override
    public void testCreateEditPart() {
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof StickyEditPart);
    }
    
    @Override
    @Test
    public void testGetDefaultSize() {
        assertEquals(new Dimension(135, 70), getProvider().getDefaultSize());
    }

    @Override
    @Test
    public void testGetDefaultTextAlignment() {
        assertEquals(ITextAlignment.TEXT_ALIGNMENT_LEFT, getProvider().getDefaultTextAlignment());
    }
}
