/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.business;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import com.archimatetool.editor.diagram.figures.AbstractArchimateFigure;
import com.archimatetool.editor.diagram.figures.EllipseFigureDelegate;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IInterfaceElement;


/**
 * Figure for a Business Interface
 * 
 * @author Phillip Beauvoir
 */
public class BusinessInterfaceFigure
extends AbstractArchimateFigure {
    
    protected IFigureDelegate fRectangleDelegate, fEllipseDelegate;
    
    public BusinessInterfaceFigure(IDiagramModelArchimateObject diagramModelObject) {
        super(diagramModelObject);
        
        fRectangleDelegate = new RectangleFigureDelegate(this);
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
        graphics.setLineWidth(1);
        graphics.setForegroundColor(ColorConstants.black);
        
        Point pt = getIconOrigin();

        Rectangle rect = new Rectangle(pt.x, pt.y, 10, 10);
        
        IInterfaceElement element = (IInterfaceElement)getDiagramModelObject().getArchimateElement();
        if(element.getInterfaceType() == IInterfaceElement.PROVIDED) {
            // circle
            graphics.drawOval(rect);
        }
        else {
            // arc
            graphics.drawArc(rect, 90, 180);
        }
        
        // line
        graphics.drawLine(pt.x, pt.y + 5, pt.x - 7, pt.y + 5);
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 15, bounds.y + 8);
    }
    
    @Override
    public void refreshVisuals() {
        super.refreshVisuals();
        repaint(); // repaint on figure change
    }
    
    @Override
    public IFigureDelegate getFigureDelegate() {
        int type = getDiagramModelObject().getType();
        return type == 0 ? fRectangleDelegate : fEllipseDelegate;
    }
    
    @Override
    public Dimension getDefaultSize() {
        int type = getDiagramModelObject().getType();
        return type == 0 ? super.getDefaultSize() : new Dimension(60, 60);
    }
}