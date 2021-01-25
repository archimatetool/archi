/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.EllipseFigureDelegate;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.model.IDiagramModelArchimateObject;


/**
 * Figure for an Interface
 * 
 * @author Phillip Beauvoir
 */
public class InterfaceFigure extends AbstractTextControlContainerFigure {
    
    protected IFigureDelegate fRectangleDelegate, fEllipseDelegate;
    
    public InterfaceFigure() {
        super(TEXT_FLOW_CONTROL);
        fRectangleDelegate = new RectangleFigureDelegate(this, 22 - getTextControlMarginWidth());
        fEllipseDelegate = new EllipseFigureDelegate(this);
    }
    
    @Override
    protected void drawFigure(Graphics graphics) {
        super.drawFigure(graphics);
        
        int type = getDiagramModelObject().getType();
        if(type == 0) {
            drawIcon(graphics);
        }
    }
    
    /**
     * Draw the icon
     */
    protected void drawIcon(Graphics graphics) {
        if(hasIconImage()) {
            return;
        }
        
        graphics.pushState();
        
        graphics.setLineWidth(1);
        graphics.setForegroundColor(isEnabled() ? ColorConstants.black : ColorConstants.gray);
        
        Point pt = getIconOrigin();

        Rectangle rect = new Rectangle(pt.x, pt.y, 10, 10);
        
        // circle
        graphics.drawOval(rect);
        
        // line
        graphics.drawLine(pt.x, pt.y + 5, pt.x - 7, pt.y + 5);
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 14, bounds.y + 8);
    }
    
    @Override
    public IFigureDelegate getFigureDelegate() {
        int type = getDiagramModelObject().getType();
        return type == 0 ? fRectangleDelegate : fEllipseDelegate;
    }
    
    @Override
    public ConnectionAnchor getDefaultConnectionAnchor() {
        int type = getDiagramModelObject().getType();
        return type == 0 ? new ChopboxAnchor(this) : new EllipseAnchor(this);
    }

    @Override
    public IDiagramModelArchimateObject getDiagramModelObject() {
        return (IDiagramModelArchimateObject)super.getDiagramModelObject();
    }
}