/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.sketch;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;


/**
 * Layer to hold a tiled background image
 * 
 * @author Phillip Beauvoir
 */
public class BackgroundImageLayer extends FreeformLayer {
    public static final String NAME = "BackgroundImageLayer"; //$NON-NLS-1$
    
    private Image fBackgroundImage;
    private int fImageWidth;
    private int fImageHeight;
    
    public BackgroundImageLayer() {
    }
    
    public BackgroundImageLayer(Image image) {
        setImage(image);
    }
    
    public void setImage(Image image) {
        fBackgroundImage = image;
        if(fBackgroundImage != null) {
            fImageWidth = fBackgroundImage.getBounds().width;
            fImageHeight = fBackgroundImage.getBounds().height;
        }
        repaint();
    }
    
    @Override
    public void paintFigure(Graphics graphics) {
        if(fBackgroundImage != null) {
            Rectangle bounds = getBounds();
            for(int x = bounds.x; x < bounds.width; x += fImageWidth) {
                for(int y = bounds.y; y < bounds.height; y += fImageHeight) {
                    graphics.drawImage(fBackgroundImage, x, y);
                }
            }
        }
    }
}
