/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.geometry.Dimension;

import com.archimatetool.editor.diagram.editparts.RoundedRectangleAnchor;
import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.RoundedRectangleFigureDelegate;




/**
 * Work Package Figure
 * 
 * @author Phillip Beauvoir
 */
public class WorkPackageFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {

    public WorkPackageFigure() {
        super(TEXT_FLOW_CONTROL);
        
        // Use a Rounded Rectangle Figure Delegate to Draw
        RoundedRectangleFigureDelegate figureDelegate = new RoundedRectangleFigureDelegate(this);
        figureDelegate.setArc(new Dimension(10, 10));
        setFigureDelegate(figureDelegate);
    }
    
    @Override
    public ConnectionAnchor getDefaultConnectionAnchor() {
        return new RoundedRectangleAnchor(this);
    }
}
