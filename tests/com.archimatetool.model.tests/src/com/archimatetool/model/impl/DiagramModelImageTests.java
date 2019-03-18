/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import static org.junit.Assert.assertEquals;
import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import com.archimatetool.model.IArchimateFactory;
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
    
    @Test
    public void testGetProperties() {
        CommonTests.testProperties(image);
    }

    @Test
    public void testGetDocumentation() {
        CommonTests.testGetDocumentation(image);
    }
}
