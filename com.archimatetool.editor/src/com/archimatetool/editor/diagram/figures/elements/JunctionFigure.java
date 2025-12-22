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
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        Rectangle rect = getBounds().getCopy();
        
        // Reduce width and height by 1 pixel
        rect.resize(-1, -1);
        
        switch(((IJunction)getDiagramModelArchimateObject().getArchimateElement()).getType()) {
            case IJunction.AND_JUNCTION_TYPE:
            default:
                graphics.setAlpha(getAlpha());
                graphics.setBackgroundColor(getFillColor());
                graphics.fillOval(rect);
                break;

            case IJunction.OR_JUNCTION_TYPE:
                setLineWidth(graphics, rect);
                graphics.setAlpha(getLineAlpha());
                graphics.setForegroundColor(getLineColor());
                graphics.drawOval(rect);
                break;
        }
        
        graphics.popState();
    }
    
    private static IIconDelegate iconDelegate = new IIconDelegate() {
        @Override
        public void drawIcon(Graphics graphics, Color foregroundColor, Color backgroundColor, Point pt) {
            graphics.pushState();
            
            // Ensure this is set
            graphics.setAntialias(SWT.ON);

            graphics.setLineWidth(1);
            
            if(backgroundColor != null) {
                graphics.setForegroundColor(backgroundColor);
                graphics.setBackgroundColor(backgroundColor);
            }
            else if(foregroundColor != null) {
                graphics.setForegroundColor(foregroundColor);
                graphics.setBackgroundColor(foregroundColor);
            }
            
            graphics.drawRectangle(pt.x + 2, pt.y + 2, 2, 2);
            graphics.drawRectangle(pt.x + 2, pt.y + 12, 2, 2);
            graphics.drawRectangle(pt.x + 14, pt.y + 7, 2, 2);
            
            graphics.drawLine(pt.x + 4, pt.y + 4, pt.x + 6, pt.y + 6);
            graphics.drawLine(pt.x + 10, pt.y + 8, pt.x + 14, pt.y + 8);
            graphics.drawLine(pt.x + 4, pt.y + 12, pt.x + 6, pt.y + 10);
            
            graphics.fillOval(pt.x + 5, pt.y + 5, 6, 6);
            
            graphics.popState();
        }
    };
    
    public static IIconDelegate getIconDelegate() {
        return iconDelegate;
    }

    @Override
    public IFigure getTextControl() {
        return null;
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
