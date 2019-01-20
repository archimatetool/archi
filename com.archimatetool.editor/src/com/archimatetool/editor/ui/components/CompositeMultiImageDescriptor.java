/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.components;

import org.eclipse.jface.resource.CompositeImageDescriptor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;

/**
 * Composite Image Descriptor
 * 
 * @author Phillip Beauvoir
 */
public class CompositeMultiImageDescriptor extends CompositeImageDescriptor {
    
    private ImageDescriptor[] fOverlays;
    private Point fSize;
    
    public CompositeMultiImageDescriptor(ImageDescriptor[] overlays) {
        fOverlays = overlays;
    }
    
    public CompositeMultiImageDescriptor(ImageDescriptor[] overlays, Point size) {
        fSize = size;
        fOverlays = overlays;
    }

    @Override
    protected void drawCompositeImage(int width, int height) {
        for(ImageDescriptor overlay : fOverlays) {
            if(overlay != null) {
                drawImage(zoom -> overlay.getImageData(100), 0, 0); // Not sure how this works
            }
        }
    }

    @Override
    protected Point getSize() {
        // Find the largest size of all the images
        if(fSize == null) {
            fSize = new Point(1, 1);
            for(ImageDescriptor overlay : fOverlays) {
                if(overlay != null) {
                    ImageData id = overlay.getImageData(100);
                    fSize.x = Math.max(fSize.x, id.width);
                    fSize.y = Math.max(fSize.y, id.height);
                }
            }
        }
        return fSize;
    }
}