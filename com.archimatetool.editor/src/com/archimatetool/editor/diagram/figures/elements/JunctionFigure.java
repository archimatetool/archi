/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigure;
import com.archimatetool.model.IJunction;
import com.archimatetool.model.impl.DiagramModelArchimateObject;



/**
 * Junction Figure
 * 
 * @author Phillip Beauvoir
 */
public class JunctionFigure extends AbstractDiagramModelObjectFigure {
    
    public JunctionFigure() {
    }

    @Override
    public void paintFigure(Graphics graphics) {
        graphics.pushState();
        
        graphics.setAntialias(SWT.ON);
        
        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        Rectangle bounds = getBounds().getCopy();
        
        String type = ((IJunction)((DiagramModelArchimateObject)getDiagramModelObject()).getArchimateElement()).getType();
        switch(type) {
            case IJunction.AND_JUNCTION_TYPE:
            default:
                graphics.setBackgroundColor(getFillColor());
                graphics.fillOval(bounds.getCopy());                
                break;

            case IJunction.OR_JUNCTION_TYPE:
                graphics.setForegroundColor(getLineColor());
                graphics.setBackgroundColor(getFillColor());
                graphics.drawOval(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1);
                break;
        }
        
        graphics.popState();
    }
    
    @Override
    public void refreshVisuals() {
        // Fill Color
        setFillColor();
        
        // Line Color
        setLineColor();
        
        // Repaint
        repaint();
    }
    
    @Override
    public IFigure getTextControl() {
        return null;
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean didClickTextControl(Point requestLoc) {
        return false;
    }

    @Override
    protected void setUI() {
    }
    
    @Override
    public ConnectionAnchor getDefaultConnectionAnchor() {
        return new EllipseAnchor(this);
    }
}
