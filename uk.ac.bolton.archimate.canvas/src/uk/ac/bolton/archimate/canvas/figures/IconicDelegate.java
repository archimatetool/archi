/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.canvas.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.canvas.model.IIconic;
import uk.ac.bolton.archimate.editor.model.IArchiveManager;
import uk.ac.bolton.archimate.editor.model.ICachedImage;
import uk.ac.bolton.archimate.editor.ui.ImageFactory;

/**
 * Delegate class to handle drawing of iconic types
 * 
 * @author Phillip Beauvoir
 */
public class IconicDelegate {
    
    private IIconic fIconic;
    private Image fImage;
    private ICachedImage fCachedImage;
    
    public IconicDelegate(IIconic owner) {
        fIconic = owner;
    }
    
    public void updateImage() {
        disposeLocalImage();
        releaseCachedImage();
        
        if(fIconic.getImagePath() != null) {
            IArchiveManager archiveManager = (IArchiveManager)fIconic.getAdapter(IArchiveManager.class);
            ICachedImage cachedImage = archiveManager.getImage(fIconic.getImagePath());
            if(cachedImage != null) {
                // If the cached image bounds is bigger than the maximum displayed image here then create a scaled image
                if(cachedImage.getImage().getBounds().width > IIconic.MAX_IMAGE_SIZE &&
                                cachedImage.getImage().getBounds().height > IIconic.MAX_IMAGE_SIZE) {
                    fImage = ImageFactory.getScaledImage(cachedImage.getImage(), IIconic.MAX_IMAGE_SIZE);
                    cachedImage.release();
                }
                // Else use the cached image here
                else {
                    fCachedImage = cachedImage;
                    fImage = cachedImage.getImage();
                }
            }
        }
    }
    
    public void drawIcon(Graphics graphics, Rectangle bounds) {
        if(fImage != null) {
            int width = fImage.getBounds().width;
            int height = fImage.getBounds().height;
            
            int x = bounds.x;
            int y = bounds.y;
            
            switch(fIconic.getImagePosition()) {
                case IIconic.ICON_POSITION_TOP_LEFT:
                    x += 1;
                    y += 1;
                    break;
                case IIconic.ICON_POSITION_TOP_CENTRE:
                    x = bounds.x + ((bounds.width - width) / 2);
                    y += 1;
                    break;
                case IIconic.ICON_POSITION_TOP_RIGHT:
                    x = (bounds.x + bounds.width) - width - 1;
                    y += 1;
                    break;

                case IIconic.ICON_POSITION_MIDDLE_LEFT:
                    x += 1;
                    y = bounds.y + ((bounds.height - height) / 2);
                    break;
                case IIconic.ICON_POSITION_MIDDLE_CENTRE:
                    x = bounds.x + ((bounds.width - width) / 2);
                    y = bounds.y + ((bounds.height - height) / 2);
                    break;
                case IIconic.ICON_POSITION_MIDDLE_RIGHT:
                    x = (bounds.x + bounds.width) - width - 1;
                    y = bounds.y + ((bounds.height - height) / 2);
                    break;

                case IIconic.ICON_POSITION_BOTTOM_LEFT:
                    x += 1;
                    y = (bounds.y + bounds.height) - height - 1;
                    break;
                case IIconic.ICON_POSITION_BOTTOM_CENTRE:
                    x = bounds.x + ((bounds.width - width) / 2);
                    y = (bounds.y + bounds.height) - height - 1;
                    break;
                case IIconic.ICON_POSITION_BOTTOM_RIGHT:
                    x = (bounds.x + bounds.width) - width - 1;
                    y = (bounds.y + bounds.height) - height - 1;
                    break;

                default:
                    break;
            }
            
            graphics.drawImage(fImage, x, y);
        }
    }
    
    public void dispose() {
        disposeLocalImage();
        releaseCachedImage();
    }
    
    /**
     * Release the cached image
     */
    private void releaseCachedImage() {
        if(fCachedImage != null) {
            fCachedImage.release();
            fCachedImage = null;
        }
    }

    /**
     * Dispose the local image if it is not disposed and also not equal to the cached image
     */
    private void disposeLocalImage() {
        if(fImage != null && !fImage.isDisposed()) {
            if(fCachedImage != null && fImage != fCachedImage.getImage()) {
                fImage.dispose();
                fImage = null;
            }
        }
    }
}
