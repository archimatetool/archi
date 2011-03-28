/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures.business;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.handles.HandleBounds;

import uk.ac.bolton.archimate.editor.diagram.figures.AbstractTextFlowFigure;
import uk.ac.bolton.archimate.editor.diagram.figures.EllipseFigureDelegate;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;

/**
 * Alternative Figure for a Business Interface
 * 
 * @author Phillip Beauvoir
 */
public class BusinessInterfaceFigure2
extends AbstractTextFlowFigure implements HandleBounds {
    
    protected static Dimension DEFAULT_SIZE = new Dimension(60, 60);
    
    public BusinessInterfaceFigure2(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
        
        // Use an Ellipse Figure Delegate to Draw
        EllipseFigureDelegate figureDelegate = new EllipseFigureDelegate(this);
        setFigureDelegate(figureDelegate);
    }
    
    @Override
    public Dimension getDefaultSize() {
        return DEFAULT_SIZE;
    }
    
    @Override
    public Rectangle getHandleBounds() {
        Rectangle bounds = getMainFigure().getBounds().getCopy();
        translateToParent(bounds);
        return bounds;
    }
}