/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

import com.archimatetool.editor.diagram.editparts.RoundedRectangleAnchor;
import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RoundedRectangleFigureDelegate;
import com.archimatetool.editor.ui.IIconDelegate;




/**
 * Process Figure
 * 
 * @author Phillip Beauvoir
 */
public class ProcessFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    protected IFigureDelegate fFigureDelegate1;
    protected IFigureDelegate fFigureDelegate2;

    public ProcessFigure() {
        super(TEXT_FLOW_CONTROL);
        fFigureDelegate1 = new RoundedRectangleFigureDelegate(this);
        fFigureDelegate2 = new ProcessFigureDelegate(this);
    }

    @Override
    protected void drawFigure(Graphics graphics) {
        super.drawFigure(graphics);
        
        if(getDiagramModelArchimateObject().getType() == 0 ) {
            drawIcon(graphics);
        }
    }
    
    /**
     * Draw the icon
     */
    protected void drawIcon(Graphics graphics) {
        if(isIconVisible()) {
            getIconDelegate().drawIcon(graphics, getIconColor(), null, getIconOrigin());
        }
    }
    
    private static IIconDelegate iconDelegate = new IIconDelegate() {
        @Override
        public void drawIcon(Graphics graphics, Color foregroundColor, Color backgroundColor, Point pt) {
            graphics.pushState();
            
            graphics.setLineWidth(1);
            
            if(foregroundColor != null) {
                graphics.setForegroundColor(foregroundColor);
            }
            
            if(backgroundColor != null) {
                graphics.setBackgroundColor(backgroundColor);
            }
            
            PointList points = new PointList();
            
            // Start at top left
            points.addPoint(pt);
            
            pt.translate(8, 0);
            points.addPoint(pt);
            
            pt.translate(0, -3);
            points.addPoint(pt);
            
            pt.translate(6, 5);
            points.addPoint(pt);
            
            pt.translate(-6, 5);
            points.addPoint(pt);
            
            pt.translate(0, -3);
            points.addPoint(pt);
            
            pt.translate(-8, 0);
            points.addPoint(pt);
            
            if(backgroundColor != null) {
                graphics.fillPolygon(points);
            }
            graphics.drawPolygon(points);
            
            graphics.popState();
        }
    };
    
    public static IIconDelegate getIconDelegate() {
        return iconDelegate;
    }

    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 17 - getLineWidth(), bounds.y + 11);
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? fFigureDelegate1 : fFigureDelegate2;
    }

    @Override
    public ConnectionAnchor getDefaultConnectionAnchor() {
        return getDiagramModelArchimateObject().getType() == 0 ? new RoundedRectangleAnchor(this) : new ChopboxAnchor(this);
    }
    
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 20 : 0;
    }
}
