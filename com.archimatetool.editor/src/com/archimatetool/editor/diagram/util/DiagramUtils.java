/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.util;

import org.eclipse.draw2d.FreeformFigure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImagePrintFigureOperation;
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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
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
        shell.setLayout(new FillLayout());
        
        GraphicalViewer viewer = createViewer(model, shell);
        ModelReferencedImage image = createModelReferencedImage(viewer, scale, margin);
        shell.dispose();
        
        return image;
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
        
        // Use Image Operation
        // TODO: Use ImageGraphics to set scale
        if(FigureUtils.isAutoScaleEnabled()) {
            Shell shell = new Shell();
            try {
                ImagePrintDiagramOperation op = new ImagePrintDiagramOperation(shell, figure, scale, bounds);
                Image image = op.run();
                return new ModelReferencedImage(image, bounds);   
            }
            finally {
                shell.dispose();
            }
        }
        
        // Use manual method
        Image image = new Image(Display.getDefault(), (int)(bounds.width * scale), (int)(bounds.height * scale));
        GC gc = new GC(image);
        SWTGraphics graphics = new ImageGraphics(gc); // Use ImageGraphics so we can get actual scale
        
        if(scale != 1) {
            graphics.scale(scale);
        }
        
        // Compensate for negative co-ordinates
        graphics.translate(bounds.x * -1, bounds.y * -1);

        // Paint onto graphics
        figure.paint(graphics);
        
        // Dispose
        gc.dispose();
        graphics.dispose();
        
        return new ModelReferencedImage(image, bounds);
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
    
    /**
     * ImagePrintDiagramOperation
     * TODO: Use ImageGraphics to set scale or get scale some other way
     */
    private static class ImagePrintDiagramOperation extends ImagePrintFigureOperation {
        private final Rectangle bounds;
        private final double scale;

        public ImagePrintDiagramOperation(Control control, IFigure printSource, double scale, Rectangle bounds) {
            super(control, printSource);
            this.scale = scale;
            this.bounds = bounds.getCopy(); // Important to use a copy in case bounds is changed elsewhere
        }

        @Override
        protected ImageData getImageDataAtZoom(int zoom) {
            // Zoom can be < 100 see https://github.com/eclipse-gef/gef-classic/issues/1146
            if(zoom < 100) {
                return null;
            }
            return super.getImageDataAtZoom(zoom);
        }
        
        @Override
        protected Dimension getImageSize() {
            // Scale the size (width and height) of the bounds
            return bounds.getSize().scale(scale);
        }

        @Override
        protected void preparePrintSource(Graphics graphics) {
            // Scale
            graphics.scale(scale);
            // Compensate for negative co-ordinates
            graphics.translate(bounds.x * -1, bounds.y * -1);
        }
    }
}
