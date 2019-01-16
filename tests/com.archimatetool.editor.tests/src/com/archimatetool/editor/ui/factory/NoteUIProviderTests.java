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

import com.archimatetool.editor.diagram.editparts.diagram.NoteEditPart;
import com.archimatetool.editor.ui.factory.diagram.NoteUIProvider;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ITextAlignment;

import junit.framework.JUnit4TestAdapter;

public class NoteUIProviderTests extends AbstractGraphicalObjectUIProviderTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(NoteUIProviderTests.class);
    }
    
    @Before
    public void runOnceBeforeAllTests() {
        provider = new NoteUIProvider();
        expectedClass = IArchimatePackage.eINSTANCE.getDiagramModelNote();
    }
    
    @Override
    public void testCreateEditPart() {
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof NoteEditPart);
    }
    
    @Override
    @Test
    public void testGetDefaultSize() {
        assertEquals(new Dimension(185, 80), getProvider().getDefaultSize());
    }

    @Override
    @Test
    public void testGetDefaultTextAlignment() {
        assertEquals(ITextAlignment.TEXT_ALIGNMENT_LEFT, getProvider().getDefaultTextAlignment());
    }
}
