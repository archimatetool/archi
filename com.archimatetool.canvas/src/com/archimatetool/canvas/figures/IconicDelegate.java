/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.canvas.model.IIconic;
import com.archimatetool.editor.Logger;
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.ui.ImageFactory;


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
                return;
            }
            
            // If the image bounds is bigger than the set max size then create a scaled image
            if(fImage != null && fMaxImageSize != MAX_IMAGESIZE && 
                    (fImage.getBounds().width > fMaxImageSize || fImage.getBounds().height > fMaxImageSize)) {
                Image image = ImageFactory.getScaledImage(fImage, fMaxImageSize);
                fImage.dispose();
                fImage = image;
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
     * Draw the icon
     */
    public void drawIcon(Graphics graphics, Rectangle bounds) {
        if(fImage != null) {
            org.eclipse.swt.graphics.Rectangle imageBounds = fImage.getBounds();
            int width = imageBounds.width;
            int height = imageBounds.height;
            
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
            
            graphics.setAntialias(SWT.ON);
            graphics.setInterpolation(SWT.HIGH);
            graphics.drawImage(fImage, x, y);
        }
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
