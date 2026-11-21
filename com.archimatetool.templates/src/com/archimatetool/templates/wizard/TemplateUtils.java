/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.templates.wizard;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Label;

import com.archimatetool.editor.diagram.util.DiagramImageFactory;
import com.archimatetool.editor.diagram.util.DiagramImageFactory.Option;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.templates.model.TemplateManager;



/**
 * Template Utils
 * 
 * @author Phillip Beauvoir
 */
public class TemplateUtils {
    
    private static final int IMAGE_MARGIN = 5;
    
    /**
     * Create and add a thumbnail preview image of diagramModel to label, scaled.
     * The image will will need to be disposed.
     */
    public static void createThumbnailPreviewImage(IDiagramModel diagramModel, Label label) {
        // Dispose of old image
        if(label.getImage() != null) { 
            label.getImage().dispose();
            label.setImage(null);
        }

        try(DiagramImageFactory imageFactory = DiagramImageFactory.withShell()) {
            GraphicalViewer graphicalViewer = imageFactory.createViewer(diagramModel, Option.NO_UPSCALE);
            org.eclipse.swt.graphics.Rectangle labelBounds = label.getBounds();
            double ratio = getImageRatio(graphicalViewer, labelBounds.width, labelBounds.height);
            Image image = imageFactory.createImage(graphicalViewer, ratio, IMAGE_MARGIN, Option.NO_UPSCALE);
            label.setImage(image);
        }
    }
    
    /**
     * Create a thumbnail image to add to the template
     * The image will will need to be disposed.
     */
    public static Image createThumbnailImage(IDiagramModel diagramModel) {
        try(DiagramImageFactory imageFactory = DiagramImageFactory.withShell()) {
            GraphicalViewer graphicalViewer = imageFactory.createViewer(diagramModel, Option.NO_UPSCALE);
            double ratio = getImageRatio(graphicalViewer, TemplateManager.THUMBNAIL_WIDTH, TemplateManager.THUMBNAIL_HEIGHT);
            Image image = imageFactory.createImage(graphicalViewer, ratio, IMAGE_MARGIN, Option.NO_UPSCALE);

            // Draw a border
            GC gc = new GC(image);
            gc.setForeground(new Color(64, 64, 64));
            gc.drawRectangle(0, 0, image.getBounds().width - 1, image.getBounds().height - 1);
            gc.dispose();

            return image;
        }
    }
    
    /**
     * Get the ratio for the image given a width and height
     */
    private static double getImageRatio(GraphicalViewer graphicalViewer, int width, int height) {
        Rectangle bounds = DiagramImageFactory.getDiagramExtents(graphicalViewer);
        bounds.expand(IMAGE_MARGIN * 2, IMAGE_MARGIN * 2); // take into account margin
        return Math.min(1, Math.min((double)width / bounds.width, (double)height / bounds.height));
    }
}
