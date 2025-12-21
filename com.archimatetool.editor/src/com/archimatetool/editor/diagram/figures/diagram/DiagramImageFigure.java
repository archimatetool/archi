/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.diagram;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigure;
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.ImageFactory;
import com.archimatetool.model.IDiagramModelImage;
import com.archimatetool.model.IDiagramModelObject;


/**
 * Diagram Image Figure
 * 
 * @author Phillip Beauvoir
 */
public class DiagramImageFigure extends AbstractDiagramModelObjectFigure {
    
    private Image fImage;
    private Dimension fOriginalImageSize, fCurrentImageSize;
    
    // This is way faster than Draw2D re-drawing the original image at scale
    boolean useScaledImage = ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.USE_SCALED_IMAGES);
    
    public DiagramImageFigure(IDiagramModelImage diagramModelImage) {
        super(diagramModelImage);
    }
    
    @Override
    public IDiagramModelImage getDiagramModelObject() {
        return (IDiagramModelImage)super.getDiagramModelObject();
    }
    
    @Override
    protected void setUI() {
        setLayoutManager(new GridLayout());
        setOpaque(true);
        setImage();
    }
    
    /**
     * Update the image with new path
     */
    public void updateImage() {
        setImage();
        revalidate();
        repaint();
    }
    
    /**
     * @return The Border Color to use. If null, do not draw a border.
     */
    public Color getBorderColor() {
        return getCachedValue("borderColor", key -> { //$NON-NLS-1$
            // Null is allowed
            return ColorFactory.get(getDiagramModelObject().getBorderColor());
        });
    }

    @Override
    public Dimension getDefaultSize() {
        return fOriginalImageSize == null ? super.getDefaultSize() : fOriginalImageSize;
    }
    
    @Override
    protected void paintFigure(Graphics graphics) {
        graphics.pushState();
        
        graphics.setAntialias(SWT.ON);
        graphics.setInterpolation(SWT.HIGH);
        
        graphics.setAlpha(getDiagramModelObject().getAlpha());
        
        Rectangle rect = getBounds().getCopy();
        
        // Reduce width and height by 1 pixel
        rect.resize(-1, -1);
        
        boolean drawBorder = getBorderColor() != null && getLineStyle() != IDiagramModelObject.LINE_STYLE_NONE;
        
        if(drawBorder) {
            // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
            setLineWidth(graphics, rect);
            setLineStyle(graphics);
        }
        
        if(fImage != null) {
            // Faster but no transparency
            if(useScaledImage) {
                rescaleImage();
                graphics.pushState();
                graphics.clipRect(rect); // Need to do this
                graphics.drawImage(fImage, rect.x, rect.y);
                graphics.popState();
            }
            // This is slower
            else {
                // Safety width and height checks
                int w1 = Math.max(0, fImage.getBounds().width);
                int h1 = Math.max(0, fImage.getBounds().height);
                int w2 = Math.max(0, rect.width);
                int h2 = Math.max(0, rect.height);
                
                graphics.drawImage(fImage, 0, 0, w1, h1, rect.x, rect.y, w2, h2);
            }
        }
        else {
            graphics.setBackgroundColor(ColorConstants.white);
            graphics.fillRectangle(rect);
            Image image = IArchiImages.ImageFactory.getImage(IArchiImages.ICON_LANDSCAPE);
            graphics.drawImage(image, rect.x + (rect.width / 2) - 7, rect.y + (rect.height / 2) - 7);
        }
        
        // Border
        if(drawBorder) {
            graphics.setAlpha(getDiagramModelObject().getLineAlpha());
            graphics.setForegroundColor(getBorderColor());
            graphics.drawRectangle(rect.x, rect.y, rect.width, rect.height);
        }
        
        graphics.popState();
    }
    
    /**
     * Set the image and store it
     */
    protected void setImage() {
        disposeImage();
        
        fImage = getOriginalImage();
        
        if(fImage != null) {
            fOriginalImageSize = new Dimension(fImage);
            fCurrentImageSize = new Dimension(fOriginalImageSize);
        }
        else {
            fOriginalImageSize = null;
        }
    }
    
    protected Image getOriginalImage() {
        Image image = null;
        
        String imagePath = getDiagramModelObject().getImagePath();
        
        if(imagePath != null) {
            IArchiveManager archiveManager = (IArchiveManager)getDiagramModelObject().getAdapter(IArchiveManager.class);
            try {
                image = archiveManager.createImage(imagePath);
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }
        
        return image;
    }
    
    /**
     * Use a re-usable rescaled image if drawing an image to scale in paintFigure(Graphics) is too slow
     */
    protected void rescaleImage() {
        int width = getBounds().width;
        int height = getBounds().height;
        
        if(width <= 0 && height <= 0) { // safety check
            return;
        }
        
        // If the image bounds are different to those in the current image, rescale the image
        if(width != fCurrentImageSize.width || height != fCurrentImageSize.height) {
            disposeImage();
            
            Image originalImage = getOriginalImage();
            
            // Use original image
            if(width == originalImage.getBounds().width && height == originalImage.getBounds().height) {
                fImage = originalImage;
            }
            // Scaled image
            else {
                fImage = ImageFactory.getScaledImage(originalImage, width, height);
                originalImage.dispose();
            }

            fCurrentImageSize = new Dimension(fImage);
        }
    }
    
    protected void disposeImage() {
        if(fImage != null && !fImage.isDisposed()) {
            fImage.dispose();
            fImage = null;
        }
    }
    
    @Override
    public IFigure getTextControl() {
        return null;
    }
    
    @Override
    public void dispose() {
        super.dispose();
        disposeImage();
    }
}
