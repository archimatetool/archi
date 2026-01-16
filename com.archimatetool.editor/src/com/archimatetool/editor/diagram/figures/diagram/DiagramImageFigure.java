/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.diagram;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.Logger;
import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigure;
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IDiagramModelImage;
import com.archimatetool.model.IDiagramModelObject;


/**
 * Diagram Image Figure
 * 
 * @author Phillip Beauvoir
 */
public class DiagramImageFigure extends AbstractDiagramModelObjectFigure {
    
    private Image fImage;
    
    public DiagramImageFigure(IDiagramModelImage diagramModelImage) {
        super(diagramModelImage);
    }
    
    @Override
    public IDiagramModelImage getDiagramModelObject() {
        return (IDiagramModelImage)super.getDiagramModelObject();
    }
    
    @Override
    protected void setUI() {
        setImage();
    }
    
    /**
     * Update the image with new path
     */
    public void updateImage() {
        setImage();
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
        return fImage == null ? super.getDefaultSize() : new Dimension(fImage);
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
            // Safety width and height checks
            int w1 = Math.max(0, fImage.getBounds().width);
            int h1 = Math.max(0, fImage.getBounds().height);
            int w2 = Math.max(0, rect.width);
            int h2 = Math.max(0, rect.height);
            graphics.drawImage(fImage, 0, 0, w1, h1, rect.x, rect.y, w2, h2);
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
     * Set and create a new image (can be null)
     */
    protected void setImage() {
        fImage = createImage();
    }
    
    /**
     * @return image or null
     */
    protected Image getImage() {
        return fImage;
    }
    
    /**
     * If imagePath is not null create the image disposing of old one first, else return null
     */
    protected Image createImage() {
        // Dispose old one first
        disposeImage();
        
        String imagePath = getDiagramModelObject().getImagePath();
        
        if(imagePath != null) {
            try {
                IArchiveManager archiveManager = (IArchiveManager)getDiagramModelObject().getAdapter(IArchiveManager.class);
                return archiveManager.createImage(imagePath);
            }
            catch(Exception ex) {
                Logger.error("Failed to create image: " + imagePath, ex); //$NON-NLS-1$
            }
        }
        
        return null;
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
