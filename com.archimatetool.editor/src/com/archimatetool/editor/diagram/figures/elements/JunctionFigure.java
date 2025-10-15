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
import org.eclipse.swt.graphics.Color;

import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigure;
import com.archimatetool.editor.ui.IIconDelegate;
import com.archimatetool.model.IJunction;



/**
 * Junction Figure
 * 
 * @author Phillip Beauvoir
 */
public class JunctionFigure extends AbstractDiagramModelObjectFigure implements IArchimateFigure {
    
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
        bounds.width--;
        bounds.height--;
        
        switch(((IJunction)getDiagramModelArchimateObject().getArchimateElement()).getType()) {
            case IJunction.AND_JUNCTION_TYPE:
            default:
                graphics.setBackgroundColor(getFillColor());
                graphics.fillOval(bounds);
                break;

            case IJunction.OR_JUNCTION_TYPE:
                setLineWidth(graphics, bounds);
                graphics.setForegroundColor(getFillColor());
                graphics.drawOval(bounds);
                break;
        }
        
        graphics.popState();
    }
    
    private static IIconDelegate iconDelegate = new IIconDelegate() {
        @Override
        public void drawIcon(Graphics graphics, Color foregroundColor, Color backgroundColor, Point pt) {
            graphics.pushState();
            
            if(foregroundColor != null) {
                graphics.setForegroundColor(foregroundColor);
            }
            
            graphics.setBackgroundColor(backgroundColor != null ? backgroundColor : graphics.getForegroundColor());
            
            graphics.fillOval(pt.x, pt.y, 12, 12);
            
            graphics.popState();
        }
    };
    
    public static IIconDelegate getIconDelegate() {
        return iconDelegate;
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
