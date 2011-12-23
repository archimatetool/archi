/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures.diagram;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.editor.diagram.figures.AbstractDiagramModelObjectFigure;
import uk.ac.bolton.archimate.editor.diagram.figures.ToolTipFigure;
import uk.ac.bolton.archimate.editor.model.IArchiveManager;
import uk.ac.bolton.archimate.editor.model.ICachedImage;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.editor.ui.ImageFactory;
import uk.ac.bolton.archimate.model.IDiagramModelImage;

/**
 * Diagram Image Figure
 * 
 * @author Phillip Beauvoir
 */
public class DiagramImageFigure extends AbstractDiagramModelObjectFigure {
    
    public static Dimension DEFAULT_SIZE = new Dimension(200, 150);
    
    private ICachedImage fCachedImage;
    private Image fImage;
    private Color fBorderColor;
    
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
    
    public void refreshVisuals() {
        // Border Color
        setBorderColor();
    }

    public void setImage() {
        disposeLocalImage();
        releaseCachedImage();

        if(getDiagramModelObject().getImagePath() != null) {
            IArchiveManager archiveManager = (IArchiveManager)getDiagramModelObject().getAdapter(IArchiveManager.class);
            fCachedImage = archiveManager.getImage(getDiagramModelObject().getImagePath());
            if(fCachedImage != null) {
                fImage = fCachedImage.getImage();
            }
        }
        
        revalidate();
        repaint();
    }
    
    protected void setBorderColor() {
        String val = getDiagramModelObject().getBorderColor();
        Color c = ColorFactory.get(val);
        if(c != fBorderColor) {
            fBorderColor = c;
            repaint();
        }
    }
    
    /**
     * @return The Border Color to use. If null, do not draw a border.
     */
    public Color getBorderColor() {
        return fBorderColor;
    }

    public Dimension getDefaultSize() {
        if(fCachedImage != null) {
            return new Dimension(fCachedImage.getImage().getBounds().width,
                    fCachedImage.getImage().getBounds().height);
        }
        return DEFAULT_SIZE;
    }
    
    @Override
    public Dimension getPreferredSize(int wHint, int hHint) {
        return getDefaultSize();
    }

    @Override
    protected void paintFigure(Graphics graphics) {
        graphics.setAntialias(SWT.ON);
        
        if(fCachedImage != null) {
            rescaleImage();
            graphics.drawImage(fImage, bounds.x + 0, bounds.y + 0);
        }
        else {
            super.paintFigure(graphics);
            Image image = IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_LANDSCAPE_16);
            graphics.drawImage(image, bounds.x + (bounds.width / 2) - 7, bounds.y + (bounds.height / 2) - 7);
        }
        
        // Border
        if(getBorderColor() != null) {
            graphics.setForegroundColor(getBorderColor());
            graphics.drawRectangle(new Rectangle(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1));
        }
    }
    
    private void rescaleImage() {
        int width = bounds.width;
        int height = bounds.height;
        
        if(width <= 0 && height <= 0) { // safety check
            return;
        }
        
        // If the bounds width and height matches that of the cached image width and height, then use that
        Image image = fCachedImage.getImage();
        if(image != null && width == image.getBounds().width && height == image.getBounds().height) {
            disposeLocalImage();
            fImage = image;
        }
        // Else if we have an image already and the bounds are different, rescale the image
        else if(fImage != null && width != fImage.getBounds().width || height != fImage.getBounds().height) {
            disposeLocalImage();
            fImage = ImageFactory.getScaledImage(fCachedImage.getImage(), width, height);
        }
    }
    
    @Override
    public IFigure getTextControl() {
        return null;
    }
    
    @Override
    public IFigure getToolTip() {
        ToolTipFigure toolTipFigure = (ToolTipFigure)super.getToolTip();
        
        if(toolTipFigure != null) {
            toolTipFigure.setText("Image");
        }
        
        return toolTipFigure;
    }

    @Override
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
