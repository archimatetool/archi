/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.util;

import java.util.Objects;

import org.eclipse.draw2d.FreeformFigure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editparts.FreeformGraphicalRootEditPart;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.ui.parts.GraphicalViewerImpl;
import org.eclipse.swt.graphics.AutoscalingMode;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.DiagramEditorFactoryExtensionHandler;
import com.archimatetool.editor.diagram.IDiagramEditorFactory;
import com.archimatetool.editor.diagram.editparts.ArchimateDiagramEditPartFactory;
import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.sketch.editparts.SketchEditPartFactory;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.DPIUtils;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.ISketchModel;

/**
 * DiagramImageFactory
 * 
 * @author Phillip Beauvoir
 * @since 5.12.0
 */
@SuppressWarnings("nls")
public class DiagramImageFactory implements AutoCloseable {
    
    /**
     * Options for upscaling images on Windows
     * {@link #UPSCALE} use {@link ImagePrintDiagramOperation}
     * {@link #NO_UPSCALE} don't use {@link ImagePrintDiagramOperation}
     */
    public enum Option {
        UPSCALE,
        NO_UPSCALE
    }
    
    /**
     * Optional Shell
     */
    protected Shell shell;
    
    private DiagramImageFactory(Shell shell) {
        this.shell = shell;
    }
    
    /**
     * @return a new DiagramImageFactory with Shell 
     */
    public static DiagramImageFactory withShell() {
        return new DiagramImageFactory(DPIUtils.createShellForImage());
    }
    
    /**
     * @return a new DiagramImageFactory with no Shell 
     */
    public static DiagramImageFactory noShell() {
        return new DiagramImageFactory(null);
    }
    
    /**
     * Create a simple GraphicalViewer for the diagram model.
     * @param model The diagram model
     * @return The Graphical Viewer which is actually a GraphicalViewerImpl
     */
    public GraphicalViewer createViewer(IDiagramModel model, Option... options) {
        return createViewer(model, shell, options);
    }

    /**
     * Create a simple GraphicalViewer for the diagram model.
     * @param model The diagram model
     * @param parent the parent Composite (usually a Shell) on which to create the GraphicalViewer.
     * @return The Graphical Viewer which is actually a GraphicalViewerImpl
     */
    public GraphicalViewer createViewer(IDiagramModel model, Composite parent, Option... options) {
        Objects.requireNonNull(parent, "Parent must not be null");

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
        Control control = viewer.createControl(parent);
        if(!doUpscale(options)) {
            // Stops text clipping on Windows but text height will be increased
            // No effect on Mac and Linux or Windows at 100% scaling
            control.setAutoscalingMode(AutoscalingMode.ENABLED);
        }
        
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
    public Image createImage(IDiagramModel model, double scale, int margin, Option... options) {
        return createDiagramImageInfo(model, scale, margin, options).image();
    }

    /**
     * @param graphicalViewer The GraphicalViewer to create the image from
     * @param scale The scale to use. 1 is full size. Max of 4 is allowed.
     * @param margin amount of white space margin to apply around the image
     * @return A Scaled Image from the given GraphicalViewer trimming off whitespace
     *         Clients must dispose of the Image when done.
     *         If graphicalViewer has no children a blank image of 100x100 is returned
     */
    public Image createImage(GraphicalViewer graphicalViewer, double scale, int margin, Option... options) {
        return createDiagramImageInfo(graphicalViewer, scale, margin, options).image();
    }

    /**
     * @param figure The Figure to create the image from
     * @param scale The scale to use. 1 is full size. Max of 5 is allowed.
     * @param margin amount of white space margin to apply around the image
     * @return A Scaled Image from the given IFigure trimming off whitespace
     *         Clients must dispose of the Image when done.
     *         If figure has no children a blank image of 100x100 is returned
     */
    public Image createImage(IFigure figure, double scale, int margin, Option... options) {
        return createDiagramImageInfo(figure, scale, margin, options).image();
    }
    
    /**
     * Create a DiagramImageInfo from the IDiagramModel
     */
    public DiagramImageInfo createDiagramImageInfo(IDiagramModel model, double scale, int margin, Option... options) {
        GraphicalViewer viewer = createViewer(model, options);
        return createDiagramImageInfo(viewer, scale, margin, options);
    }
    
    /**
     * Create a DiagramImageInfo from the GraphicalViewer's root figure
     */
    protected DiagramImageInfo createDiagramImageInfo(GraphicalViewer graphicalViewer, double scale, int margin, Option... options) {
        IFigure rootFigure = getPrintableLayer(graphicalViewer);
        return createDiagramImageInfo(rootFigure, scale, margin, options);
    }
    
    /**
     * Create a DiagramImageInfo containing the diagram image and its bounds
     */
    protected DiagramImageInfo createDiagramImageInfo(IFigure figure, double scale, int margin, Option... options) {
        scale = Math.clamp(scale, 0.1, 5);
        
        // Get Figure Bounds
        Rectangle bounds = getFigureBounds(figure, scale, margin);
        
        // Set the figure scale for AbstractDiagramModelObjectFigure children
        setFigureScale(figure, scale);

        // Use Image Operation if preference or option is set
        if(doUpscale(options) && shell != null) {
            return new DiagramImageInfo(createImageUpscaled(shell, figure, scale, bounds), bounds);
        }
        
        // Else do basic Image generation
        return new DiagramImageInfo(createImage(figure, scale, bounds), bounds);
    }

    /**
     * Get Figure Bounds with scale and margin
     */
    protected Rectangle getFigureBounds(IFigure figure, double scale, int margin) {
        Rectangle bounds = getMinimumBounds(figure);
        
        if(bounds == null) {
            bounds = new Rectangle(0, 0, 100, 100); // At least a minimum
        }
        else {
            bounds.expand(margin / scale, margin / scale);
        }
        
        return bounds;
    }

    /**
     * Create Image using standard Image methods
     */
    protected Image createImage(IFigure figure, double scale, Rectangle bounds) {
        // Use normal graphics method
        Image image = new Image(Display.getDefault(), (int)(bounds.width * scale), (int)(bounds.height * scale));
        GC gc = new GC(image);
        SWTGraphics graphics = new SWTGraphics(gc);
        
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
        
        return image;
    }
    
    /**
     * Create Image using upscaling and ImagePrintFigureOperation
     */
    protected Image createImageUpscaled(Shell shell, IFigure figure, double scale, Rectangle bounds) {
        ImagePrintFigureOperation op = new ImagePrintDiagramOperation(shell, figure, scale, bounds);
        return op.run();
    }

    /**
     * For each AbstractDiagramModelObjectFigure child set imagescale to the scale value
     * so that AbstractDiagramModelObjectFigure#applyLineWidthOffset can use it
     */
    protected void setFigureScale(IFigure figure, double scale) {
        if(figure instanceof AbstractDiagramModelObjectFigure dmoFigure) {
            dmoFigure.imageScale = scale;
        }
        
        figure.getChildren().forEach(child -> setFigureScale(child, scale));
    }
    
    /**
     * @return The zoom level to use for Image.getImageData(int zoom) based on the Shell here.
     * If Shell is null delegate to DPIUtils.getDeviceZoom();
     */
    public int getImageDeviceZoom() {
        return ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.SCALE_IMAGE_EXPORT) ? getDeviceZoom() : 100;
    }
    
    /**
     * @return The zoom level of the current Shell
     */
    protected int getDeviceZoom() {
        if(shell != null && !shell.isDisposed()) {
            return shell.getZoom();
        }
        
        return DPIUtils.getDeviceZoom();
    }
    
    /**
     * Return true if options contains Option.UPSCALE or preference set
     */
    protected boolean doUpscale(Option... options) {
        if(!DPIUtils.isDraw2dAutoScaleEnabled() || getDeviceZoom() == 100) {
            return false;
        }
        
        if(options != null) {
            for(Option option : options) {
                if(option == Option.NO_UPSCALE) {
                    return false;
                }
                if(option == Option.UPSCALE) {
                    return true;
                }
            }
        }
        
        return ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.UPSCALE_IMAGE_EXPORT);
    }
    
    @Override
    public void close() {
        if(shell != null) {
            shell.dispose();
            shell = null;
        }
    }
    
    /**
     * Get the printable layer figure from the GraphicalViewer
     */
    public static IFigure getPrintableLayer(GraphicalViewer graphicalViewer) {
        LayerManager layerManager = (LayerManager)graphicalViewer.getEditPartForModel(LayerManager.ID);
        return layerManager.getLayer(LayerConstants.PRINTABLE_LAYERS);
    }
    
    /**
     * Return the extents of the diagram by extending from the left-topmost child to the right-bottom-most child.
     * If there are no children in the diagram a minimal size of 100x100 is returned.
     */
    public static Rectangle getDiagramExtents(GraphicalViewer graphicalViewer) {
        IFigure printableLayer = getPrintableLayer(graphicalViewer);
        Rectangle bounds = getMinimumBounds(printableLayer);
        return bounds == null ? new Rectangle(0, 0, 100, 100) : bounds;
    }
    
    /**
     * @param figure
     * @return The minimum bounds for a figure or null if there are no children
     */
    public static Rectangle getMinimumBounds(IFigure figure) {
        // Simple Figure
        if(!(figure instanceof FreeformFigure)) {
            return figure.getBounds().getCopy();
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