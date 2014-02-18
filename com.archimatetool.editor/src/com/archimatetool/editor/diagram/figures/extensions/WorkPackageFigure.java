/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.extensions;

import org.eclipse.draw2d.geometry.Dimension;

import com.archimatetool.editor.diagram.figures.AbstractArchimateFigure;
import com.archimatetool.editor.diagram.figures.RoundedRectangleFigureDelegate;
import com.archimatetool.model.IDiagramModelArchimateObject;




/**
 * Work Package Figure
 * 
 * @author Phillip Beauvoir
 */
public class WorkPackageFigure
extends AbstractArchimateFigure {

    public WorkPackageFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
        
        // Use a Rounded Rectangle Figure Delegate to Draw
        RoundedRectangleFigureDelegate figureDelegate = new RoundedRectangleFigureDelegate(this);
        figureDelegate.setArc(new Dimension(10, 10));
        setFigureDelegate(figureDelegate);
    }
}
