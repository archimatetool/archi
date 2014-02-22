/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.graphics.Image;
import org.junit.Test;

import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;



public class FigureChooserTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(FigureChooserTests.class);
    }

    @Test
    public void testHasAlternateFigure() {
        for(EClass eClass : FigureChooser.FIGURE_CLASSES) {
            assertTrue(FigureChooser.hasAlternateFigure((IArchimateElement)IArchimateFactory.eINSTANCE.create(eClass)));
        }
    }

    @Test
    public void testGetFigurePreviewImagesForClass() {
        for(EClass eClass : FigureChooser.FIGURE_CLASSES) {
            Image[] images = FigureChooser.getFigurePreviewImagesForClass(eClass);
            assertNotNull(images[0]);
            assertNotNull(images[1]);
            assertFalse(images[0].isDisposed());
            assertFalse(images[1].isDisposed());
        }
    }
    
    @Test
    public void testGetDefaultFigureTypeForNewDiagramElement() {
        for(EClass eClass : FigureChooser.FIGURE_CLASSES) {
            int type = FigureChooser.getDefaultFigureTypeForNewDiagramElement((IArchimateElement)IArchimateFactory.eINSTANCE.create(eClass));
            assertEquals(0, type);
        }
        
        Preferences.STORE.setValue(IPreferenceConstants.BUSINESS_INTERFACE_FIGURE, 1);
        int type = FigureChooser.getDefaultFigureTypeForNewDiagramElement(IArchimateFactory.eINSTANCE.createBusinessInterface());
        assertEquals(1, type);
    }
    
}
