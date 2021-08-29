/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

import com.archimatetool.editor.diagram.editparts.RoundedRectangleAnchor;
import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RoundedRectangleFigureDelegate;




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
        if(!isIconVisible()) {
            return;
        }
        
        graphics.pushState();
        
        graphics.setLineWidth(1);
        graphics.setForegroundColor(isEnabled() ? ColorConstants.black : ColorConstants.gray);
        
        PointList points = new PointList();
        
        // Start at top left
        Point pt = getIconOrigin();
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
        
        graphics.drawPolygon(points);
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 18, bounds.y + 11);
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
