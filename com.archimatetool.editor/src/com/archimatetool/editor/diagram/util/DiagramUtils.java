/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.util;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.GraphicalViewerImpl;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import com.archimatetool.model.IDiagramModel;


/**
 * Diagram Utils
 * 
 * @author Phillip Beauvoir
 */
@Deprecated(since = "5.12")
public final class DiagramUtils {
    
    /**
     * Create a simple GraphicalViewer for the diagram model.
     * @param model The diagram model
     * @param parent the parent Composite (usually a Shell) on which to create the GraphicalViewer.
     * @return The Graphical Viewer which is actually a GraphicalViewerImpl
     * @deprecated Use {@link DiagramImageFactory}
     */
    @Deprecated(since = "5.12")
    public static GraphicalViewerImpl createViewer(IDiagramModel model, Composite parent) {
        try(DiagramImageFactory imageFactory = DiagramImageFactory.noShell()) {
            return (GraphicalViewerImpl)imageFactory.createViewer(model, parent);
        }
    }
    
    /**
     * @param model The model to create the image from
     * @param scale The scale to use. 1 is full size.
     * @param margin amount of white space margin to apply around the image
     * @return A Scaled Image from the given Diagram Model
     *         Clients must dispose of the Image when done.
     *         If model has no children a blank image of 100x100 is returned
     * @deprecated Use {@link DiagramImageFactory}
     */
    @Deprecated(since = "5.12")
    public static Image createImage(IDiagramModel model, double scale, int margin) {
        return createModelReferencedImage(model, scale, margin).getImage();
    }
    
    /**
     * @param graphicalViewer The GraphicalViewer to create the image from
     * @param scale The scale to use. 1 is full size. Max of 4 is allowed.
     * @param margin amount of white space margin to apply around the image
     * @return A Scaled Image from the given GraphicalViewer trimming off whitespace
     *         Clients must dispose of the Image when done.
     *         If graphicalViewer has no children a blank image of 100x100 is returned
     * @deprecated Use {@link DiagramImageFactory}
     */
    @Deprecated(since = "5.12")
    public static Image createImage(GraphicalViewer graphicalViewer, double scale, int margin) {
        try(DiagramImageFactory imageFactory = DiagramImageFactory.withShell()) {
            return imageFactory.createImage(graphicalViewer, scale, margin);
        }
    }
    
    /**
     * @param figure The Figure to create the image from
     * @param scale The scale to use. 1 is full size. Max of 5 is allowed.
     * @param margin amount of white space margin to apply around the image
     * @return A Scaled Image from the given IFigure trimming off whitespace
     *         Clients must dispose of the Image when done.
     *         If figure has no children a blank image of 100x100 is returned
     * @deprecated Use {@link DiagramImageFactory}
     */
    @Deprecated(since = "5.12")
    public static Image createImage(IFigure figure, double scale, int margin) {
        try(DiagramImageFactory imageFactory = DiagramImageFactory.withShell()) {
            return imageFactory.createImage(figure, scale, margin);
        }
    }

    /**
     * @param model The model to create the image from
     * @param scale The scale to use. 1 is full size.
     * @param margin amount of white space margin to apply around the image
     * @return ModelReferencedImage wrapper class containing a Scaled Image from the given Diagram Model and offset bounds
     *         Clients must dispose of the Image when done.
     *         If model has no children a blank image of 100x100 is returned
     * @deprecated Use {@link DiagramImageFactory}
     */
    @Deprecated(since = "5.12")
    public static ModelReferencedImage createModelReferencedImage(IDiagramModel model, double scale, int margin) {
        try(DiagramImageFactory imageFactory = DiagramImageFactory.withShell()) {
            DiagramImageInfo imageInfo = imageFactory.createDiagramImageInfo(model, scale, margin);
            return new ModelReferencedImage(imageInfo.image(), imageInfo.bounds());
        }
    }

    /**
     * Return the extents of the diagram by extending from the left-topmost child to the right-bottom-most child.
     * If there are no children in the diagram a minimal size of 100x100 is returned.
     * @deprecated Use {@link DiagramImageFactory}
     */
    @Deprecated(since = "5.12")
    public static Rectangle getDiagramExtents(GraphicalViewer graphicalViewer) {
        return DiagramImageFactory.getDiagramExtents(graphicalViewer);
    }
    
    /**
     * @param figure
     * @return The minimum bounds for a figure or null if there are no children
     * @deprecated Use {@link DiagramImageFactory}
     */
    @Deprecated(since = "5.12")
    public static Rectangle getMinimumBounds(IFigure figure) {
        return DiagramImageFactory.getMinimumBounds(figure);
    }
}
