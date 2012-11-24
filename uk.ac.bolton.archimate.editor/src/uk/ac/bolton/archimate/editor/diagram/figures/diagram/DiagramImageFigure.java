/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.diagram.figures.diagram;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.editor.diagram.figures.AbstractDiagramModelObjectFigure;
import uk.ac.bolton.archimate.editor.model.IArchiveManager;
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
    
    private String fImagePath;
    private Image fImage;
    
    private Color fBorderColor;
    
    /**
     * Global Image cache of original images to improve rescaling speed
     */
    private static Map<String, Image> imageCache = new HashMap<String, Image>();
    private static List<String> imageCacheTally = new ArrayList<String>();

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
        disposeImage();
        releaseOriginalImage();
        
        setImage();
        
        revalidate();
        repaint();
    }
    
    public void refreshVisuals() {
        // Border Color
        setBorderColor();
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
        Image originalImage = getOriginalImage();
        if(originalImage != null) {
            return new Dimension(originalImage.getBounds().width, originalImage.getBounds().height);
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
        graphics.setInterpolation(SWT.HIGH);
        
        if(fImagePath != null && fImage != null) {
            rescaleImage();
            graphics.drawImage(fImage, bounds.x, bounds.y);
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
    
    /**
     * Set the image and store it
     */
    private void setImage() {
        fImagePath = getDiagramModelObject().getImagePath();
        if(fImagePath != null) {
            Image originalImage = imageCache.get(fImagePath);
            
            if(originalImage == null) {
                IArchiveManager archiveManager = (IArchiveManager)getDiagramModelObject().getAdapter(IArchiveManager.class);
                try {
                    originalImage = archiveManager.createImage(fImagePath);
                    imageCache.put(fImagePath, originalImage);
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                    return;
                }
            }
            
            retainOriginalImage();

            fImage = originalImage;
        }
    }
    
    /**
     * Use a re-usable rescaled image because drawing an image to scale in paintFigure(Graphics) is too slow
     */
    private void rescaleImage() {
        int width = bounds.width;
        int height = bounds.height;
        
        if(width <= 0 && height <= 0) { // safety check
            return;
        }
        
        // If the image bounds are different to those in the current image, rescale the image
        if(width != fImage.getBounds().width || height != fImage.getBounds().height) {
            disposeImage();
            
            Image originalImage = getOriginalImage();
            
            // Use original image
            if(width == originalImage.getBounds().width && height == originalImage.getBounds().height) {
                fImage = originalImage;
            }
            else {
                fImage = ImageFactory.getScaledImage(originalImage, width, height);
            }
        }
    }
    
    private Image getOriginalImage() {
        return fImagePath == null ? null : imageCache.get(fImagePath);
    }

    private void retainOriginalImage() {
        if(fImagePath != null) {
            imageCacheTally.add(fImagePath);
        }
    }

    private void releaseOriginalImage() {
        if(fImagePath != null) {
            imageCacheTally.remove(fImagePath);
            if(!imageCacheTally.contains(fImagePath)) {
                Image image = imageCache.get(fImagePath);
                if(image != null) {
                    image.dispose();
                    imageCache.remove(fImagePath);
                }
            }
        }
    }
    
    private void disposeImage() {
        if(fImage != getOriginalImage() && fImage != null && !fImage.isDisposed()) {
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
        releaseOriginalImage();
        fBorderColor = null;
    }
}
