/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.sketch;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.editor.ui.IArchimateImages;


/**
 * Layer to hold Background Image
 * 
 * @author Phillip Beauvoir
 */
public class BackgroundImageLayer extends FreeformLayer {
    public static final String NAME = "BackgroundImageLayer"; //$NON-NLS-1$
    
    private Image fBackgroundImage = IArchimateImages.ImageFactory.getImage(IArchimateImages.BROWN_PAPER);
    private int width = fBackgroundImage.getBounds().width;
    private int height = fBackgroundImage.getBounds().height;
    
    @Override
    public void paintFigure(Graphics graphics) {
        Rectangle bounds = getBounds();
        for(int x = bounds.x; x < bounds.width; x += width) {
            for(int y = bounds.y; y < bounds.height; y += height) {
                graphics.drawImage(fBackgroundImage, x, y);
            }
        }
    }
}
