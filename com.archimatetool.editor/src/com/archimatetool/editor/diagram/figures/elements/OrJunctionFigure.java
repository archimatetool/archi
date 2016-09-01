/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.swt.SWT;



/**
 * Or Junction Figure
 * 
 * @author Phillip Beauvoir
 */
public class OrJunctionFigure extends AndJunctionFigure {
    
    public OrJunctionFigure() {
    }
    
    @Override
    public void paintFigure(Graphics graphics) {
        graphics.pushState();
        
        graphics.setAntialias(SWT.ON);
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setBackgroundColor(getFillColor());
        graphics.drawOval(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1);
        
        graphics.popState();
    }

}
