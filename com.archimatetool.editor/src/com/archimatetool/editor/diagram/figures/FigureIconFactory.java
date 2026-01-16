/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.ui.IIconDelegate;
import com.archimatetool.editor.ui.IIconDelegateProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;

/**
 * Factory for drawing figure icons
 * 
 * @author Phillip Beauvoir
 */
public class FigureIconFactory {
    
    /**
     * Draw a figure icon
     * 
     * TODO: all icons need to be drawn with a common origin point so callers can provide one Point
     * 
     * @param eClass The EClass for the figure
     * @param graphics The graphics to draw on
     * @param foregroundColor
     * @param backgroundColor
     * @param pt The start point to draw the icon
     */
    public static void drawIcon(EClass eClass, Graphics graphics, Color foregroundColor, Color backgroundColor, Point pt) {
        // Get IIconDelegate from provider if there is one
        if(ObjectUIFactory.INSTANCE.getProviderForClass(eClass) instanceof IIconDelegateProvider provider) {
            IIconDelegate delegate = provider.getIconDelegate();
            if(delegate != null) {
                delegate.drawIcon(graphics, foregroundColor, backgroundColor, pt);
            }
        }
    }
    
    /**
     * Create an ImageDescriptor for an EClass figure icon
     * The image data size is 16x16 or 32x32 depending on zoom.
     * 
     * @param eClass The EClass for the figure
     * @param foregroundColor
     * @param backgroundColor
     * @param pt The start point to draw the icon
     */
    public static ImageDescriptor getImageDescriptorFromFigureIcon(EClass eClass, Color foregroundColor, Color backgroundColor, Point pt) {
        return new ImageDescriptor() {
            // Set background to this color so we can make it transparent
            private static final Color transparentColor = new Color(255, 255, 254);
            
            @Override
            public ImageData getImageData(int zoom) {
                // Blank icon image for background and size
                Image img = new Image(Display.getCurrent(), 16, 16);

                GC gc = new GC(img);

                // Set background to this color so we can make it transparent
                gc.setBackground(transparentColor);
                gc.fillRectangle(0, 0, 16, 16);

                SWTGraphics graphics = new SWTGraphics(gc);

                drawIcon(eClass, graphics, foregroundColor, backgroundColor, pt);

                graphics.dispose();
                gc.dispose();

                ImageData data = img.getImageData(zoom);

                // Set transparent pixel to background color
                data.transparentPixel = data.palette.getPixel(transparentColor.getRGB());

                img.dispose();

                return data;
            }
        };
    }
}
