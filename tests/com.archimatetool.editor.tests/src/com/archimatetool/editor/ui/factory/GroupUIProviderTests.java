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
import org.eclipse.swt.graphics.Color;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.editor.diagram.editparts.diagram.GroupEditPart;
import com.archimatetool.editor.ui.factory.diagram.GroupUIProvider;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ITextAlignment;

import junit.framework.JUnit4TestAdapter;

public class GroupUIProviderTests extends AbstractGraphicalObjectUIProviderTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(GroupUIProviderTests.class);
    }
    
    @Before
    public void runOnceBeforeAllTests() {
        provider = new GroupUIProvider();
        expectedClass = IArchimatePackage.eINSTANCE.getDiagramModelGroup();
    }
    
    @Override
    public void testCreateEditPart() {
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof GroupEditPart);
    }
    
    @Override
    @Test
    public void testGetDefaultColor() {
        assertEquals(new Color(210, 215, 215), getProvider().getDefaultColor());
    }
    
    @Override
    @Test
    public void testGetDefaultSize() {
        assertEquals(new Dimension(400, 140), getProvider().getDefaultSize());
    }

    @Override
    public void testShouldExposeFeature() {
        super.testShouldExposeFeature();
        assertTrue(provider.shouldExposeFeature(IArchimatePackage.Literals.TEXT_ALIGNMENT__TEXT_ALIGNMENT.getName()));
    }

    @Override
    @Test
    public void testGetDefaultTextAlignment() {
        assertEquals(ITextAlignment.TEXT_ALIGNMENT_LEFT, getProvider().getDefaultTextAlignment());
    }

}
