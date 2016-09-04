/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.diagram;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.Color;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.model.IDiagramModelObject;


/**
 * Empty Figure
 * 
 * @author Phillip Beauvoir
 */
public class EmptyFigure
extends AbstractTextControlContainerFigure {
    
    public EmptyFigure(IDiagramModelObject diagramModelObject) {
        super(diagramModelObject, TEXT_FLOW_CONTROL);
        
        // Use a Rectangle Figure Delegate to Draw
        setFigureDelegate(new RectangleFigureDelegate(this));
    }

    @Override
    public Color getFillColor() {
        return ColorConstants.white;
    }
}