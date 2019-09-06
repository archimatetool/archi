/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.diagram;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigure;
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.ImageFactory;
import com.archimatetool.model.IDiagramModelImage;


/**
 * Diagram Image Figure
 * 
 * @author Phillip Beauvoir
 */
public class DiagramImageFigure extends AbstractDiagramModelObjectFigure {
    
    private Image fImage;
    private Dimension fOriginalImageSize, fCurrentImageSize;
    
    private Color fBorderColor;
    
    // This is way faster than Draw2D re-drawing the original image at scale
    private boolean useScaledImage = true;
    
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
    
    @Override
    public void refreshVisuals() {
        setBorderColor();
        repaint();
    }

    protected void setBorderColor() {
        String val = getDiagramModelObject().getBorderColor();
        fBorderColor = ColorFactory.get(val);
    }
    
    /**
     * @return The Border Color to use. If null, do not draw a border.
     */
    public Color getBorderColor() {
        return fBorderColor;
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
        
        if(fImage != null) {
            if(useScaledImage) {
                rescaleImage();
                graphics.drawImage(fImage, bounds.x, bounds.y);
            }
            // This is way too slow
            else {
                graphics.drawImage(fImage, 0, 0, fImage.getBounds().width, fImage.getBounds().height,
                        bounds.x, bounds.y, bounds.width, bounds.height);
            }
        }
        else {
            super.paintFigure(graphics);
            Image image = IArchiImages.ImageFactory.getImage(IArchiImages.ICON_LANDSCAPE);
            graphics.drawImage(image, bounds.x + (bounds.width / 2) - 7, bounds.y + (bounds.height / 2) - 7);
        }
        
        // Border
        if(getBorderColor() != null) {
            graphics.setAlpha(getDiagramModelObject().getLineAlpha());
            graphics.setForegroundColor(getBorderColor());
            graphics.drawRectangle(new Rectangle(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1));
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
     * Use a re-usable rescaled image because drawing an image to scale in paintFigure(Graphics) is too slow
     */
    protected void rescaleImage() {
        int width = bounds.width;
        int height = bounds.height;
        
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
        disposeImage();
        fBorderColor = null;
    }
}
