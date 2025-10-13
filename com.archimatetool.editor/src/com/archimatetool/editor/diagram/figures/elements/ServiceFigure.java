/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

import com.archimatetool.editor.diagram.editparts.RoundedRectangleAnchor;
import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RoundedRectangleFigureDelegate;




/**
 * Service Figure
 * 
 * @author Phillip Beauvoir
 */
public class ServiceFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    protected IFigureDelegate fFigureDelegate1;
    protected IFigureDelegate fFigureDelegate2;

    public ServiceFigure() {
        super(TEXT_FLOW_CONTROL);
        fFigureDelegate1 = new RoundedRectangleFigureDelegate(this);
        fFigureDelegate2 = new ServiceFigureDelegate(this);
    }
    
    @Override
    protected void drawFigure(Graphics graphics) {
        super.drawFigure(graphics);
        
        if(getDiagramModelArchimateObject().getType() == 0) {
            drawIcon(graphics);
        }
    }
    
    /**
     * Draw the icon
     */
    protected void drawIcon(Graphics graphics) {
        if(isIconVisible()) {
            drawIcon(graphics, getIconColor(), null, getIconOrigin());
        }
    }
    
    public static void drawIcon(Graphics graphics, Color foregroundColor, Color backgroundColor, Point pt) {
        graphics.pushState();
        
        graphics.setLineWidth(1);
        graphics.setForegroundColor(foregroundColor);
        if(backgroundColor != null) {
            graphics.setBackgroundColor(backgroundColor);
        }
        
        Rectangle rect = new Rectangle(pt.x, pt.y, 16, 9);
        if(backgroundColor != null) {
            graphics.fillRoundRectangle(rect, 8, 8);
        }
        graphics.drawRoundRectangle(rect, 8, 8);
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 20 - getLineWidth(), bounds.y + 7);
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? fFigureDelegate1 : fFigureDelegate2;
    }
    
    @Override
    public ConnectionAnchor getDefaultConnectionAnchor() {
        return new RoundedRectangleAnchor(this);
    }

    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 22 : 0;
    }
}
