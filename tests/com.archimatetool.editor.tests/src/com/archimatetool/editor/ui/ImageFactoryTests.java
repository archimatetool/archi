/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.junit.Test;

import junit.framework.JUnit4TestAdapter;



public class ImageFactoryTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ImageFactoryTests.class);
    }
    
    @Test
    public void getAutoScaledImage() {
        Image image = new Image(Display.getDefault(), 30, 40);
        Image image2 = ImageFactory.getAutoScaledImage(image);
        assertTrue(image.isDisposed());
        assertSame(Display.getDefault(), image2.getDevice());
        image2.dispose();
    }
    
    @Test
    public void getScaledImage_Width_Height() {
        Image image1 = new Image(Display.getDefault(), 30, 40);
        Rectangle bounds = image1.getBounds();
        assertEquals(30, bounds.width);
        assertEquals(40, bounds.height);
        
        Image image2 = ImageFactory.getScaledImage(image1, 90, 67);
        Rectangle bounds2 = image2.getBounds();
        assertEquals(90, bounds2.width);
        assertEquals(67, bounds2.height);
        
        image1.dispose();
        image2.dispose();
    }
    
    @Test
    public void getScaledImageSize() {
        Image image = new Image(Display.getDefault(), 100, 200);
        
        Rectangle bounds = ImageFactory.getScaledImageSize(image, 60);
        assertEquals(30, bounds.width);
        assertEquals(60, bounds.height);
        
        image.dispose();
    }

}
