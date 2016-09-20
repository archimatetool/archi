/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.editor.diagram.editparts.diagram.DiagramImageEditPart;
import com.archimatetool.editor.ui.factory.diagram.DiagramImageUIProvider;
import com.archimatetool.model.IArchimatePackage;

public class DiagramModelImageUIProviderTests extends AbstractGraphicalObjectUIProviderTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DiagramModelImageUIProviderTests.class);
    }
    
    @Before
    public void runOnceBeforeAllTests() {
        provider = new DiagramImageUIProvider();
        expectedClass = IArchimatePackage.eINSTANCE.getDiagramModelImage();
    }
    
    @Override
    public void testCreateEditPart() {
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof DiagramImageEditPart);
    }
    
    @Override
    @Test
    public void testGetDefaultSize() {
        assertEquals(new Dimension(200, 150), getProvider().getDefaultSize());
    }

    @Override
    public void testShouldExposeFeature() {
        EObject instance = createInstanceForExpectedClass();
        provider.setInstance(instance);
        
        assertTrue(provider.shouldExposeFeature(IArchimatePackage.Literals.BORDER_OBJECT__BORDER_COLOR));
        assertFalse(provider.shouldExposeFeature(null));
    }
}
