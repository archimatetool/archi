/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.editor.diagram.editparts.diagram.GroupEditPart;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.factory.diagram.GroupUIProvider;
import com.archimatetool.model.IArchimatePackage;

public class GroupUIProviderTests extends AbstractElementUIProviderTests {
    
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
        assertEquals(ColorFactory.get(210, 215, 215), provider.getDefaultColor());
    }
    
    @Override
    @Test
    public void testGetDefaultSize() {
        assertEquals(new Dimension(400, 140), provider.getDefaultSize());
    }

}
