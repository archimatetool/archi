/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.util;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.ScaledGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editparts.FreeformGraphicalRootEditPart;
import org.eclipse.gef.ui.parts.GraphicalViewerImpl;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import uk.ac.bolton.archimate.editor.diagram.DiagramEditorFactoryExtensionHandler;
import uk.ac.bolton.archimate.editor.diagram.IDiagramEditorFactory;
import uk.ac.bolton.archimate.editor.diagram.editparts.ArchimateDiagramEditPartFactory;
import uk.ac.bolton.archimate.editor.diagram.sketch.editparts.SketchEditPartFactory;
import uk.ac.bolton.archimate.model.IArchimateDiagramModel;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.ISketchModel;



/**
 * Diagram Utils
 * 
 * @author Phillip Beauvoir
 */
public final class DiagramUtils {
    
    /**
     * Create a GraphicalViewerImpl to show the model. The Viewer has no Scroll Bars
     * @param model
     * @return A Graphical Viewer
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
            throw new RuntimeException("Unsupported model type");
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
     * @param model
     * @return An Image from the given Diagram Model
     *         Clients must dispose of the Image when done.
     */
    public static Image createImage(IDiagramModel model) {
        return createScaledImage(model, 1);
    }
    
    /**
     * @param model
     * @return A Scaled Image from the given Diagram Model
     *         Clients must dispose of the Image when done.
     */
    public static Image createScaledImage(IDiagramModel model, double scale) {
        Shell shell = new Shell();
        shell.setLayout(new FillLayout());
        
        GraphicalViewer viewer = createViewer(model, shell);
        Image image = createScaledImage(viewer, scale);
        shell.dispose();
        
        return image;
    }

    /**
     * @param diagramViewer
     * @return An Image from the given GraphicalViewer trimming off whitespace
     *         Clients must dispose of the Image when done.
     */
    public static Image createImage(GraphicalViewer diagramViewer) {
        return createScaledImage(diagramViewer, 1);
    }
    
    /**
     * @param diagramViewer
     * @param scale
     * @return A Scaled Image from the given GraphicalViewer trimming off whitespace
     *         Clients must dispose of the Image when done.
     */
    public static Image createScaledImage(GraphicalViewer diagramViewer, double scale) {
        if(scale <= 0) {
            scale = 1;
        }
        if(scale > 4) {
            scale = 4;
        }
        
        Rectangle rectangle = getDiagramExtents(diagramViewer);
        
        Image image = new Image(Display.getDefault(), (int)(rectangle.width * scale), (int)(rectangle.height * scale) );
        GC gc = new GC(image);
        SWTGraphics swtGraphics = new SWTGraphics(gc);
        Graphics graphics = swtGraphics;
        
        IFigure figure = ((FreeformGraphicalRootEditPart)diagramViewer.getRootEditPart()).getLayer(LayerConstants.PRINTABLE_LAYERS);
        
        // If scaled, then scale now
        if(scale != 1) {
            graphics = new ScaledGraphics(swtGraphics);
            graphics.scale(scale);
        }
        
        // Compensate for negative co-ordinates
        graphics.translate(rectangle.x * -1, rectangle.y * -1);

        // Paint onto graphics
        figure.paint(graphics);
        
        // Dispose
        gc.dispose();
        graphics.dispose();
        if(swtGraphics != graphics) {
            swtGraphics.dispose();
        }
        
        return image;
    }
    
    /**
     * Return the actual extents of the diagram by trimming the whitespace off of the diagram.
     * If there are no children in the diagram a size of 100x100 is returned.
     */
    public static Rectangle getDiagramExtents(GraphicalViewer diagramViewer) {
        FreeformGraphicalRootEditPart rootEditPart = (FreeformGraphicalRootEditPart)diagramViewer.getRootEditPart();
        
        // No Children
        if(!hasChildFigures(rootEditPart)) {
            return new Rectangle(100, 100, 100, 100);
        }
        
        IFigure figure = rootEditPart.getLayer(LayerConstants.PRINTABLE_LAYERS);
        Rectangle extents = figure.getBounds().getCopy(); // make sure to get a copy
        
        // Calculate the minimum extents of the primary layer
        IFigure primaryLayer = rootEditPart.getLayer(LayerConstants.PRIMARY_LAYER);
        Rectangle rect = new Rectangle(primaryLayer.getBounds().width, primaryLayer.getBounds().height,
                primaryLayer.getBounds().x, primaryLayer.getBounds().y);
        
        for(Object child : primaryLayer.getChildren()) {
            if(child instanceof FreeformLayer) {
                for(Object o : ((Figure)child).getChildren()) {
                    getDiagramExtents((IFigure)o, rect);
                }
            }
        }
        
        // Calculate the minimum extents of the connection layer
        IFigure connectionLayer = rootEditPart.getLayer(LayerConstants.CONNECTION_LAYER);
        Rectangle rect2 = new Rectangle(connectionLayer.getBounds().width, connectionLayer.getBounds().height,
                connectionLayer.getBounds().x, connectionLayer.getBounds().y);
        
        for(Object child : connectionLayer.getChildren()) {
            if(child instanceof IFigure) {
                getDiagramExtents((IFigure)child, rect2);
            }
        }
        
        // Take the largest area from the primary and connections layers
        rect.x = Math.min(rect.x, rect2.x);
        rect.y = Math.min(rect.y, rect2.y);
        rect.width = Math.max(rect.width, rect2.width);
        rect.height = Math.max(rect.height, rect2.height);
        
        // Take the smallest area from the original diagram extents and the primary and connections layers
        extents.x = Math.max(extents.x, rect.x);
        extents.y = Math.max(extents.y, rect.y);
        extents.width = Math.min(extents.width, rect.width - extents.x);
        extents.height = Math.min(extents.height, rect.height - extents.y);
        
        // Border
        int BORDER_WIDTH = 10;
        extents.x -= BORDER_WIDTH;
        extents.y -= BORDER_WIDTH;
        extents.width += BORDER_WIDTH * 2;
        extents.height += BORDER_WIDTH * 2;
        
        return extents;
    }

    private static void getDiagramExtents(IFigure figure, Rectangle rect) {
        int x = figure.getBounds().x + figure.getBounds().width;
        if(x > rect.width) {
            rect.width = x;
        }

        if(figure.getBounds().x < rect.x) {
            rect.x = figure.getBounds().x;
        }

        int y = figure.getBounds().y + figure.getBounds().height;
        if(y > rect.height) {
            rect.height = y;
        }

        if(figure.getBounds().y < rect.y) {
            rect.y = figure.getBounds().y;
        }
    }

    private static boolean hasChildFigures(FreeformGraphicalRootEditPart rootEditPart) {
        IFigure layer = rootEditPart.getLayer(LayerConstants.PRIMARY_LAYER);
        
        for(Object child : layer.getChildren()) {
            if(child instanceof FreeformLayer) {
                return !((FreeformLayer)child).getChildren().isEmpty();
            }
        }
        
        return false;
    }
    
}
