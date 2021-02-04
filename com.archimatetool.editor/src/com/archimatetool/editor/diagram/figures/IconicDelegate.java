/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;

import com.archimatetool.editor.Logger;
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.ui.ImageFactory;
import com.archimatetool.model.IIconic;


/**
 * Delegate class to handle drawing of iconic types
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class IconicDelegate {
    
    /**
     * Setting max image size to this value means that no scaling is done
     */
    public static final int MAX_IMAGESIZE = -1;
    
    private IIconic fIconic;
    private Image fImage;
    private int fMaxImageSize = MAX_IMAGESIZE;
    
    private int topOffset = 0;
    private int bottomOffset = 0;
    private int leftOffset = 0;
    private int rightOffset = 0;
    
    public IconicDelegate(IIconic owner) {
        this(owner, MAX_IMAGESIZE);
    }
    
    public IconicDelegate(IIconic owner, int maxSize) {
        fIconic = owner;
        fMaxImageSize = maxSize;
        updateImage();
    }
    
    /**
     * Update the image with a new scaled image
     */
    public void updateImage() {
        disposeImage();
        
        if(fIconic.getImagePath() != null) {
            IArchiveManager archiveManager = (IArchiveManager)fIconic.getAdapter(IArchiveManager.class);

            try {
                fImage = archiveManager.createImage(fIconic.getImagePath());
            }
            catch(Exception ex) {
                ex.printStackTrace();
                Logger.logError("Could not create image!", ex);
            }
        }
    }
    
    public Image getImage() {
        return fImage;
    }
    
    /**
     * Set offset value to use when icon is positioned on the top
     * The y position will be moved down by val amount
     */
    public void setTopOffset(int val) {
        topOffset = val;
    }
    
    /**
     * Set offset value to use when icon is positioned on the bottom
     * The y position will be moved down by val amount (use negative value to move up)
     */
    public void setBottomOffset(int val) {
        bottomOffset = val;
    }
    
    /**
     * Set offset value to use when icon is positioned on the left
     * The x position will be moved to the right by val amount
     */
    public void setLeftOffset(int val) {
        leftOffset = val;
    }
    
    /**
     * Set offset value to use when icon is positioned on the right
     * The x position will be moved to the right by val amount (use negative value to move left)
     */
    public void setRightOffset(int val) {
        rightOffset = val;
    }
    
    /**
     * Set offset values to use
     */
    public void setOffsets(int top, int right, int bottom, int left) {
        topOffset = top;
        rightOffset = right;
        bottomOffset = bottom;
        leftOffset = left;
    }

    /**
     * Draw the icon
     */
    public void drawIcon(Graphics graphics, org.eclipse.draw2d.geometry.Rectangle bounds) {
        if(fImage != null) {
            Rectangle imageBounds = fImage.getBounds();
            
            // New Image size, possibly scaled
            Rectangle newSize = getImageSize(imageBounds);
            
            int width = newSize.width;
            int height = newSize.height;
            
            int x = bounds.x;
            int y = bounds.y;
            
            switch(fIconic.getImagePosition()) {
                case IIconic.ICON_POSITION_TOP_LEFT:
                    x += leftOffset;
                    y += topOffset;
                    break;
                case IIconic.ICON_POSITION_TOP_CENTRE:
                    x = bounds.x + ((bounds.width - width) / 2);
                    y += topOffset;
                    break;
                case IIconic.ICON_POSITION_TOP_RIGHT:
                    x = (bounds.x + bounds.width) - width + rightOffset;
                    y += topOffset;
                    break;

                case IIconic.ICON_POSITION_MIDDLE_LEFT:
                    x += leftOffset;
                    y = bounds.y + ((bounds.height - height) / 2);
                    break;
                case IIconic.ICON_POSITION_MIDDLE_CENTRE:
                    x = bounds.x + ((bounds.width - width) / 2);
                    y = bounds.y + ((bounds.height - height) / 2);
                    break;
                case IIconic.ICON_POSITION_MIDDLE_RIGHT:
                    x = (bounds.x + bounds.width) - width + rightOffset;
                    y = bounds.y + ((bounds.height - height) / 2);
                    break;

                case IIconic.ICON_POSITION_BOTTOM_LEFT:
                    x += leftOffset;
                    y = (bounds.y + bounds.height) - height + bottomOffset;
                    break;
                case IIconic.ICON_POSITION_BOTTOM_CENTRE:
                    x = bounds.x + ((bounds.width - width) / 2);
                    y = (bounds.y + bounds.height) - height + bottomOffset;
                    break;
                case IIconic.ICON_POSITION_BOTTOM_RIGHT:
                    x = (bounds.x + bounds.width) - width + rightOffset;
                    y = (bounds.y + bounds.height) - height + bottomOffset;
                    break;

                default:
                    break;
            }
            
            graphics.pushState();
            
            graphics.setAntialias(SWT.ON);
            graphics.setInterpolation(SWT.HIGH);
            graphics.setClip(bounds); // At zoom levels > 100 the image can be painted beyond the bounds
            
            // Ensure image is drawn in full alpha
            graphics.setAlpha(255);
            
            // Original size
            if(fMaxImageSize == MAX_IMAGESIZE) {
                graphics.drawImage(fImage, x, y);
            }
            // Scaled size
            else {
                graphics.drawImage(fImage, 0, 0, imageBounds.width, imageBounds.height, x, y, width, height);
            }
            
            graphics.popState();
        }
    }
    
    /**
     * @return the possibly scaled image size or original image size if not scaled
     */
    private Rectangle getImageSize(Rectangle imageBounds) {
        // Use image bounds
        if(fMaxImageSize == MAX_IMAGESIZE) {
            return imageBounds;
        }

        return ImageFactory.getScaledImageSize(fImage, fMaxImageSize);
    }
    
    public void dispose() {
        disposeImage();
        fIconic = null;
    }
    
    private void disposeImage() {
        if(fImage != null && !fImage.isDisposed()) {
            fImage.dispose();
            fImage = null;
        }
    }
}
