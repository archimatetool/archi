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

import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.ui.ImageFactory;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IIconic;
import com.archimatetool.model.IProfile;


/**
 * Delegate class to handle drawing of iconic types
 * 
 * @author Phillip Beauvoir
 */
public class IconicDelegate {
    
    /**
     * Setting max image size to this value means that no scaling is done
     */
    public static final int MAX_IMAGESIZE = -1;
    
    private IIconic fIconic;
    private Image fImage;
    private int fMaxImageSize = MAX_IMAGESIZE;
    
    /**
     * Store current image path to optimise
     */
    private String fImagePath;
    
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
    }
    
    /**
     * Update the image
     */
    public void updateImage() {
        String imagePath = getImagePath();
        
        // If the same image path then do nothing
        if(fImagePath != null && fImagePath.equals(imagePath)) {
            return;
        }
        
        // Store image path
        fImagePath = imagePath;
        
        disposeImage();
        
        if(imagePath != null) {
            IArchiveManager archiveManager = (IArchiveManager)fIconic.getAdapter(IArchiveManager.class);
            if(archiveManager != null) { // fIconic object can be orphaned at this point when importing another model
                fImage = archiveManager.createImage(imagePath);
            }
        }
    }
    
    public Image getImage() {
        return fImage;
    }
    
    public boolean hasImage() {
        return fImage != null;
    }
    
    private String getImagePath() {
        // If this is an ArchiMate object and has a Profile with image then return the Profile Image path...
        if(fIconic instanceof IDiagramModelArchimateObject) {
            IDiagramModelArchimateObject dmo = (IDiagramModelArchimateObject)fIconic;
            // If set to show profile image...
            if(dmo.useProfileImage()) {
                // If there is a profile...
                IProfile profile = dmo.getArchimateElement().getPrimaryProfile();
                return profile != null ? profile.getImagePath() : null;
            }
        }
        
        // Return object's image path...
        return fIconic != null ? fIconic.getImagePath() : null;
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
     * Draw the icon image in the full figure bounds
     * @param graphics Graphics context
     * @param drawArea The area to draw the image in
     */
    public void drawIcon(Graphics graphics, org.eclipse.draw2d.geometry.Rectangle drawArea) {
        drawIcon(graphics, drawArea, drawArea);
    }
    
    /**
     * Draw the icon image in a given area of the figure and specify full figure bounds in case of Fill draw
     * @param graphics Graphics context
     * @param figureBounds The bounds of the Figure
     * @param drawArea The area to draw the image in (may be the same as figureBounds)
     */
    public void drawIcon(Graphics graphics, org.eclipse.draw2d.geometry.Rectangle figureBounds, org.eclipse.draw2d.geometry.Rectangle drawArea) {
        if(fImage == null || fIconic == null) {
            return;
        }
        
        Rectangle imageBounds = fImage.getBounds();
        
        // New Image size, possibly scaled
        Rectangle newSize = getImageSize(imageBounds);
        
        int width = newSize.width;
        int height = newSize.height;
        
        int x = drawArea.x;
        int y = drawArea.y;
        
        switch(fIconic.getImagePosition()) {
            case IIconic.ICON_POSITION_TOP_LEFT:
                x += leftOffset;
                y += topOffset;
                break;
            case IIconic.ICON_POSITION_TOP_CENTRE:
                x = drawArea.x + ((drawArea.width - width) / 2);
                y += topOffset;
                break;
            case IIconic.ICON_POSITION_TOP_RIGHT:
                x = (drawArea.x + drawArea.width) - width + rightOffset;
                y += topOffset;
                break;

            case IIconic.ICON_POSITION_MIDDLE_LEFT:
                x += leftOffset;
                y = drawArea.y + ((drawArea.height - height) / 2);
                break;
            case IIconic.ICON_POSITION_MIDDLE_CENTRE:
                x = drawArea.x + ((drawArea.width - width) / 2);
                y = drawArea.y + ((drawArea.height - height) / 2);
                break;
            case IIconic.ICON_POSITION_MIDDLE_RIGHT:
                x = (drawArea.x + drawArea.width) - width + rightOffset;
                y = drawArea.y + ((drawArea.height - height) / 2);
                break;

            case IIconic.ICON_POSITION_BOTTOM_LEFT:
                x += leftOffset;
                y = (drawArea.y + drawArea.height) - height + bottomOffset;
                break;
            case IIconic.ICON_POSITION_BOTTOM_CENTRE:
                x = drawArea.x + ((drawArea.width - width) / 2);
                y = (drawArea.y + drawArea.height) - height + bottomOffset;
                break;
            case IIconic.ICON_POSITION_BOTTOM_RIGHT:
                x = (drawArea.x + drawArea.width) - width + rightOffset;
                y = (drawArea.y + drawArea.height) - height + bottomOffset;
                break;

            default:
                break;
        }
        
        graphics.pushState();
        
        graphics.setAntialias(SWT.ON);
        graphics.setInterpolation(SWT.HIGH);
        graphics.setClip(figureBounds); // Don't paint the image beyond the figure's bounds
        
        // Ensure image is drawn in full alpha
        graphics.setAlpha(255);
        
        // Fill
        if(fIconic.getImagePosition() == IIconic.ICON_POSITION_FILL) {
            // Fill
            // graphics.drawImage(fImage, 0, 0, imageBounds.width, imageBounds.height, figureBounds.x, figureBounds.y, figureBounds.width, figureBounds.height);
            
            // Cover Fill (algorithm from JB the maths wizard)
            float imageRatio  = (float) imageBounds.width / imageBounds.height;
            float figureRatio = (float) figureBounds.width / figureBounds.height;
            int newWidth  = (int) (imageRatio < figureRatio ? figureBounds.width : figureBounds.height * imageRatio);
            int newHeight = (int) (imageRatio < figureRatio ? figureBounds.width / imageRatio : figureBounds.height);                
                            
            // Image top-left corner
            x = figureBounds.x - (newWidth / 2) + (figureBounds.width / 2);
            y = figureBounds.y - (newHeight / 2) + (figureBounds.height / 2);
            
            graphics.drawImage(fImage, 0, 0, imageBounds.width, imageBounds.height, x, y, newWidth, newHeight);
        }
        // Full image size
        else if(fMaxImageSize == MAX_IMAGESIZE) {
            graphics.drawImage(fImage, x, y);
        }
        // Scaled image size
        else {
            graphics.drawImage(fImage, 0, 0, imageBounds.width, imageBounds.height, x, y, width, height);
        }
        
        graphics.popState();
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
