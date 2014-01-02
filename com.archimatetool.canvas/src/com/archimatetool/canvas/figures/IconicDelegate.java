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
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.ui.ImageFactory;


/**
 * Delegate class to handle drawing of iconic types
 * 
 * @author Phillip Beauvoir
 */
public class IconicDelegate {
    
    private IIconic fIconic;
    private Image fImage;
    
    public IconicDelegate(IIconic owner) {
        fIconic = owner;
    }
    
    public void updateImage() {
        disposeImage();
        
        if(fIconic.getImagePath() != null) {
            IArchiveManager archiveManager = (IArchiveManager)fIconic.getAdapter(IArchiveManager.class);

            Image image = null;
            try {
                image = archiveManager.createImage(fIconic.getImagePath());
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
            
            if(image != null) {
                // If the image bounds is bigger than the maximum displayed image here then create a scaled image
                if(image.getBounds().width > IIconic.MAX_IMAGE_SIZE || image.getBounds().height > IIconic.MAX_IMAGE_SIZE) {
                    fImage = ImageFactory.getScaledImage(image, IIconic.MAX_IMAGE_SIZE);
                    image.dispose();
                }
                // Else use original
                else {
                    fImage = image;
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
