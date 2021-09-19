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
import org.eclipse.swt.widgets.Shell;

import com.archimatetool.editor.diagram.util.DiagramUtils;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.templates.model.TemplateManager;



/**
 * Template Utils
 * 
 * @author Phillip Beauvoir
 */
public class TemplateUtils {
    
    /**
     * Create and add a thumbnail preview image of diagramModel to label, scaled.
     * The image will will need to be disposed.
     * @param label
     */
    public static void createThumbnailPreviewImage(IDiagramModel diagramModel, Label label) {
        // Dispose of old image
        if(label.getImage() != null) { 
            label.getImage().dispose();
            label.setImage(null);
        }

        Shell shell = new Shell();
        GraphicalViewer diagramViewer = DiagramUtils.createViewer(diagramModel, shell);
        
        final int margin = 5;
        Rectangle bounds = DiagramUtils.getDiagramExtents(diagramViewer);
        bounds.expand(margin * 2, margin * 2);
        double ratio = Math.min(1, Math.min((double)label.getBounds().width / bounds.width,
                (double)label.getBounds().height / bounds.height));
        
        Image image = DiagramUtils.createImage(diagramViewer, ratio, margin);
        label.setImage(image);
        shell.dispose();
    }
    
    /**
     * Create a thumbnail image to add to the template
     * @param dm
     * @return
     */
    public static Image createThumbnailImage(IDiagramModel diagramModel) {
        Shell shell = new Shell();
        GraphicalViewer diagramViewer = DiagramUtils.createViewer(diagramModel, shell);
        
        int padding = 5;
        Rectangle bounds = DiagramUtils.getDiagramExtents(diagramViewer);
        bounds.expand(padding * 2, padding * 2);
        double ratio = Math.min(1, Math.min((double)TemplateManager.THUMBNAIL_WIDTH / bounds.width,
                (double)TemplateManager.THUMBNAIL_HEIGHT / bounds.height));
        
        Image image = DiagramUtils.createImage(diagramViewer, ratio, padding);
        shell.dispose();

        // Draw a border
        GC gc = new GC(image);
        gc.setForeground(new Color(64, 64, 64));
        gc.drawRectangle(0, 0, image.getBounds().width - 1, image.getBounds().height - 1);
        gc.dispose();

        return image;
    }
}
