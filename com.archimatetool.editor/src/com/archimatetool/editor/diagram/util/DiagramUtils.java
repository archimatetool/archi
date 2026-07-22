/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.util;

import org.eclipse.draw2d.FreeformFigure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editparts.FreeformGraphicalRootEditPart;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.ui.parts.GraphicalViewerImpl;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.internal.DPIUtil;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import com.archimatetool.editor.diagram.DiagramEditorFactoryExtensionHandler;
import com.archimatetool.editor.diagram.IDiagramEditorFactory;
import com.archimatetool.editor.diagram.editparts.ArchimateDiagramEditPartFactory;
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.diagram.sketch.editparts.SketchEditPartFactory;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.ISketchModel;




/**
 * Diagram Utils
 * 
 * @author Phillip Beauvoir
 */
public final class DiagramUtils {
    
    /**
     * Create a simple GraphicalViewer for the diagram model.
     * @param model The diagram model
     * @param parent the parent Composite (usually a Shell) on which to create the GraphicalViewer.
     * @return The Graphical Viewer which is actually a GraphicalViewerImpl
     */
    public static GraphicalViewerImpl createViewer(IDiagramModel model, Composite parent) {
        EditPartFactory editPartFactory = null;
        
        if(model instanceof IArchimateDiagramModel) {
            editPartFactory = new ArchimateDiagramEditPartFactory();
        }
        else if(model instanceof ISketchModel) {
            editPartFactory = new SketchEditPartFactory();
        }
        else {
            // Extensions
            IDiagramEditorFactory factory = DiagramEditorFactoryExtensionHandler.INSTANCE.getFactory(model);
            if(factory != null) {
                editPartFactory = factory.createEditPartFactory();
            }
        }
        
        if(editPartFactory == null) {
            throw new RuntimeException("Unsupported model type"); //$NON-NLS-1$
        }
        
        GraphicalViewerImpl viewer = new GraphicalViewerImpl();
        viewer.createControl(parent);
        viewer.setEditPartFactory(editPartFactory);
        
        RootEditPart rootPart = new FreeformGraphicalRootEditPart();
        viewer.setRootEditPart(rootPart);
        
        viewer.setContents(model);
        viewer.flush();
        
        return viewer;
    }
    
    /**
     * @param model The model to create the image from
     * @param scale The scale to use. 1 is full size.
     * @param margin amount of white space margin to apply around the image
     * @return A Scaled Image from the given Diagram Model
     *         Clients must dispose of the Image when done.
     *         If model has no children a blank image of 100x100 is returned
     */
    public static Image createImage(IDiagramModel model, double scale, int margin) {
        return createModelReferencedImage(model, scale, margin).getImage();
    }
    
    /**
     * @param model The model to create the image from
     * @param scale The scale to use. 1 is full size.
     * @param margin amount of white space margin to apply around the image
     * @return ModelReferencedImage wrapper class containing a Scaled Image from the given Diagram Model and offset bounds
     *         Clients must dispose of the Image when done.
     *         If model has no children a blank image of 100x100 is returned
     */
    public static ModelReferencedImage createModelReferencedImage(IDiagramModel model, double scale, int margin) {
        Shell shell = new Shell();
        try {
            GraphicalViewer viewer = createViewer(model, shell);
            return createModelReferencedImage(viewer, scale, margin);
        }
        finally {
            shell.dispose();
        }
    }

    /**
     * @param graphicalViewer The GraphicalViewer to create the image from
     * @param scale The scale to use. 1 is full size. Max of 4 is allowed.
     * @param margin amount of white space margin to apply around the image
     * @return A Scaled Image from the given GraphicalViewer trimming off whitespace
     *         Clients must dispose of the Image when done.
     *         If graphicalViewer has no children a blank image of 100x100 is returned
     */
    public static Image createImage(GraphicalViewer graphicalViewer, double scale, int margin) {
        return createModelReferencedImage(graphicalViewer, scale, margin).getImage();
    }
    
    private static ModelReferencedImage createModelReferencedImage(GraphicalViewer graphicalViewer, double scale, int margin) {
        LayerManager layerManager = (LayerManager)graphicalViewer.getEditPartRegistry().get(LayerManager.ID);
        IFigure rootFigure = layerManager.getLayer(LayerConstants.PRINTABLE_LAYERS);
        return createModelReferencedImage(rootFigure, scale, margin);
    }
    
    /**
     * @param figure The Figure to create the image from
     * @param scale The scale to use. 1 is full size. Max of 5 is allowed.
     * @param margin amount of white space margin to apply around the image
     * @return A Scaled Image from the given GraphicalViewer trimming off whitespace
     *         Clients must dispose of the Image when done.
     *         If figure has no children a blank image of 100x100 is returned
     */
    public static Image createImage(IFigure figure, double scale, int margin) {
        return createModelReferencedImage(figure, scale, margin).getImage();
    }

    private static ModelReferencedImage createModelReferencedImage(IFigure figure, double scale, int margin) {
        scale = Math.clamp(scale, 0.1, 5);
        
        Rectangle bounds = getMinimumBounds(figure);
        if(bounds == null) {
            bounds = new Rectangle(0, 0, 100, 100); // At least a minimum
        }
        else {
            bounds.expand(margin / scale, margin / scale);
        }
        
        // On Windows use the scaled method else  just create image with scale
        Image image = FigureUtils.isAutoScaleEnabled() ? createAutoScaledImage(figure, bounds, scale) : createImageAtScale(figure, bounds, 1.0, scale);
        return new ModelReferencedImage(image, bounds);   
    }
    
    /**
     * Create a scaled image on Windows depending on device zoom
     * Comes from https://github.com/eclipse-gef/gef-classic/discussions/1151
     */
    @SuppressWarnings("restriction")
    private static Image createAutoScaledImage(IFigure figure, Rectangle bounds, double imageScale) {
        int deviceZoom = DPIUtil.getDeviceZoom();
        double displayScale = deviceZoom / 100.0;
        
        // Create the image at scale
        Image imageZoom = createImageAtScale(figure, bounds, displayScale, imageScale);

        // If device zoom is 100% no need for any more processing
        if(deviceZoom == 100) {
            return imageZoom;
        }

        // Image is stored at 100% zoom, but is scaled by the monitor zoom
        ImageData imageDataZoom = imageZoom.getImageData(100);
        Image tmp = new Image(null, imageDataZoom);
        imageZoom.dispose();

        int width100 = imageDataZoom.width * 100 / deviceZoom;
        int height100 = imageDataZoom.height * 100 / deviceZoom;
        Image image = new Image(null, width100, height100);

        GC gc = new GC(image);
        image.getImageData(deviceZoom);
        gc.drawImage(tmp, 0, 0, width100, height100);
        gc.dispose();

        tmp.dispose();
        
        return image;
    }

    /**
     * Create a scaled image
     * Parts from https://github.com/eclipse-gef/gef-classic/discussions/1151
     */
    private static Image createImageAtScale(IFigure figure, Rectangle bounds, double displayScale, double imageScale) {
        Dimension size = bounds.getSize();
        
        // Scale size by display scale * image scale
        size.scale(displayScale * imageScale);

        Image image = new Image(null, size.width, size.height);

        GC gc = new GC(image);
        
        // Force image to be drawn as if at 100% zoom
        // This is the magic that stops text clipping
        if(FigureUtils.isAutoScaleEnabled()) {
            image.getImageData(100);
        }

        SWTGraphics graphics = new ImageGraphics(gc);
        
        // Scale by display and image scale
        graphics.scale(displayScale * imageScale);
        
        // Compensate for negative co-ordinates
        graphics.translate(bounds.x * -1, bounds.y * -1);
        
        // Paint figure
        figure.paint(graphics);

        graphics.dispose();
        gc.dispose();
        
        return image;
    }

    /**
     * Return the extents of the diagram by extending from the left-topmost child to the right-bottom-most child.
     * If there are no children in the diagram a minimal size of 100x100 is returned.
     */
    public static Rectangle getDiagramExtents(GraphicalViewer graphicalViewer) {
        LayerManager layerManager = (LayerManager)graphicalViewer.getEditPartRegistry().get(LayerManager.ID);
        IFigure rootFigure = layerManager.getLayer(LayerConstants.PRINTABLE_LAYERS);
        Rectangle r = getMinimumBounds(rootFigure);
        return r == null ? new Rectangle(0, 0, 100, 100) : r;
    }
    
    /**
     * @param figure
     * @return The minimum bounds for a figure or null if there are no children
     */
    public static Rectangle getMinimumBounds(IFigure figure) {
        // Simple Figure
        if(!(figure instanceof FreeformFigure)) {
            return figure.getBounds();
        }
        
        Rectangle minimumBounds = null;
        
        for(IFigure child : figure.getChildren()) {
            Rectangle bounds;
            if(child instanceof FreeformFigure) {
                bounds = getMinimumBounds(child);
            }
            else {
                bounds = child.getBounds();
            }
            
            if(bounds != null) {
                if(minimumBounds == null) {
                    minimumBounds = new Rectangle(bounds);
                }
                else {
                    minimumBounds.union(bounds);
                }
            }
        }
        
        return minimumBounds;
    }
}
