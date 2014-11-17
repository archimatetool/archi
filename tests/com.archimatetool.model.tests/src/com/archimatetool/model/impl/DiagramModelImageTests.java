/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelImage;


@SuppressWarnings("nls")
public class DiagramModelImageTests extends DiagramModelObjectTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DiagramModelImageTests.class);
    }
    
    private IDiagramModelImage image;
    
    @Override
    protected IDiagramModelComponent getComponent() {
        image = IArchimateFactory.eINSTANCE.createDiagramModelImage();
        return image;
    }


    @Test
    public void testGetBorderColor() {
        assertEquals(null, image.getBorderColor());
        image.setBorderColor("#ffffff");
        assertEquals("#ffffff", image.getBorderColor());
    }

    @Test
    public void testGetImagePath() {
        assertEquals(null, image.getImagePath());
        image.setImagePath("/somepath");
        assertEquals("/somepath", image.getImagePath());
    }

    @Override
    public void testShouldShouldExposeFeature() {
        assertTrue(component.shouldExposeFeature(IArchimatePackage.Literals.BORDER_OBJECT__BORDER_COLOR));
        assertFalse(component.shouldExposeFeature(null));
    }
}
