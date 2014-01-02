/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.junctions;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

import com.archimatetool.model.IDiagramModelArchimateObject;



/**
 * And Junction Figure
 * 
 * @author Phillip Beauvoir
 */
public class AndJunctionFigure extends OrJunctionFigure {
    
    public AndJunctionFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
    }

    @Override
    public void paintFigure(Graphics graphics) {
        graphics.pushState();
        
        graphics.setAntialias(SWT.ON);
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setBackgroundColor(getFillColor());
        graphics.fillRectangle(bounds.getCopy());
        
        graphics.popState();
    }
    
    @Override
    public Color getFillColor() {
        return ColorConstants.black;
    }
}
