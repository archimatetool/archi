/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;


/**
 * Abstract Figure Delegate
 * 
 * @author Phillip Beauvoir
 */
public class AbstractFigureDelegate implements IFigureDelegate {
    
    private IDiagramModelObjectFigure fOwner;
    
    protected AbstractFigureDelegate(IDiagramModelObjectFigure owner) {
        fOwner = owner;
    }
    
    protected IDiagramModelObjectFigure getOwner() {
        return fOwner;
    }

    @Override
    public void drawFigure(Graphics graphics) {
    }

    @Override
    public Rectangle calculateTextControlBounds() {
        return null;
    }

    @Override
    public void drawTargetFeedback(Graphics graphics) {
    }

}
