/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.diagram.figures.diagram;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.Color;

import uk.ac.bolton.archimate.editor.diagram.figures.AbstractTextFlowFigure;
import uk.ac.bolton.archimate.editor.diagram.figures.RectangleFigureDelegate;
import uk.ac.bolton.archimate.model.IDiagramModelObject;

/**
 * Empty Figure
 * 
 * @author Phillip Beauvoir
 */
public class EmptyFigure
extends AbstractTextFlowFigure {
    
    public EmptyFigure(IDiagramModelObject diagramModelObject) {
        super(diagramModelObject);
        
        // Use a Rectangle Figure Delegate to Draw
        setFigureDelegate(new RectangleFigureDelegate(this));
    }

    @Override
    public Color getFillColor() {
        return ColorConstants.white;
    }
}