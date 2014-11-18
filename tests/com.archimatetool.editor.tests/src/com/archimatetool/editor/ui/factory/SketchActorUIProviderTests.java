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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.swt.graphics.Color;
import org.junit.Before;
import org.junit.Test;

import com.archimatetool.editor.diagram.sketch.editparts.SketchActorEditPart;
import com.archimatetool.editor.ui.factory.sketch.SketchActorUIProvider;
import com.archimatetool.model.IArchimatePackage;

public class SketchActorUIProviderTests extends AbstractElementUIProviderTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(SketchActorUIProviderTests.class);
    }
    
    @Before
    public void runOnceBeforeAllTests() {
        provider = new SketchActorUIProvider();
        expectedClass = IArchimatePackage.eINSTANCE.getSketchModelActor();
    }
    
    @Override
    public void testCreateEditPart() {
        EditPart editPart = provider.createEditPart();
        assertTrue(editPart instanceof SketchActorEditPart);
    }
    
    @Override
    @Test
    public void testGetDefaultSize() {
        assertEquals(new Dimension(75, 100), provider.getDefaultSize());
    }

    @Override
    public void testGetDefaultColor() {
        Color color = provider.getDefaultColor();
        assertEquals(ColorConstants.black, color);
    }
    
    @Override
    public void testShouldExposeFeature() {
        super.testShouldExposeFeature();
        EObject instance = expectedClass.getEPackage().getEFactoryInstance().create(expectedClass);
        assertFalse(provider.shouldExposeFeature(instance, IArchimatePackage.Literals.TEXT_ALIGNMENT__TEXT_ALIGNMENT));
        assertFalse(provider.shouldExposeFeature(instance, IArchimatePackage.Literals.LINE_OBJECT__LINE_COLOR));
    }

}
