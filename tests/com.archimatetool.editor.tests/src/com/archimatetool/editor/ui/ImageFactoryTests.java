/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.junit.jupiter.api.Test;


public class ImageFactoryTests {

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
        Image image = new Image(Display.getDefault(), 790, 600);
        assertEquals(new Rectangle(0, 0, 60, 46), ImageFactory.getScaledImageSize(image, 60));
        image.dispose();
        
        image = new Image(Display.getDefault(), 800, 600);
        assertEquals(new Rectangle(0, 0, 40, 30), ImageFactory.getScaledImageSize(image, 40));
        image.dispose();
    }

    @Test
    public void getScaledSize() {
        assertEquals(new Rectangle(0, 0, 0, 0), ImageFactory.getScaledSize(800, -1, -1));
        assertEquals(new Rectangle(0, 0, 400, 300), ImageFactory.getScaledSize(800, 600, 400));
        assertEquals(new Rectangle(0, 0, 40, 30), ImageFactory.getScaledSize(800, 600, 40));
        assertEquals(new Rectangle(0, 0, 60, 46), ImageFactory.getScaledSize(790, 600, 60));
        assertEquals(new Rectangle(0, 0, 20, 20), ImageFactory.getScaledSize(20, 20, 60));
        assertEquals(new Rectangle(0, 0, 60, 60), ImageFactory.getScaledSize(60, 60, 60));
        assertEquals(new Rectangle(0, 0, 60, 60), ImageFactory.getScaledSize(61, 61, 60));
    }
}
