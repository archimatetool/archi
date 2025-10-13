/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;


/**
 * Figure for a Node
 * 
 * @author Phillip Beauvoir
 */
public class NodeFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    protected IFigureDelegate fFigureDelegate1;
    protected IFigureDelegate fFigureDelegate2;
    
    public NodeFigure() {
        super(TEXT_FLOW_CONTROL);
        fFigureDelegate1 = new RectangleFigureDelegate(this);
        fFigureDelegate2 = new BoxFigureDelegate(this);
    }
    
    @Override
    protected void drawFigure(Graphics graphics) {
        super.drawFigure(graphics);
        
        int type = getDiagramModelArchimateObject().getType();
        if(type == 0) {
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
        
        Path path = new Path(null);
        
        path.addRectangle(pt.x, pt.y, 11, 11);
        if(backgroundColor != null) {
            graphics.fillPath(path);
        }
        
        path.moveTo(pt.x - 0.2f, pt.y);
        path.lineTo(pt.x + 3.2f, pt.y - 3);
        path.lineTo(pt.x + 14, pt.y - 3);
        path.lineTo(pt.x + 14f, pt.y + 8);
        path.lineTo(pt.x + 11f, pt.y + 11.2f);
        
        if(backgroundColor != null) {
            graphics.fillPath(path);
        }
        
        path.moveTo(pt.x + 11, pt.y);
        path.lineTo(pt.x + 14, pt.y - 3);
        
        graphics.drawPath(path);
        path.dispose();
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 17 - getLineWidth(), bounds.y + 8);
    }
    
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 20 : 0;
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? fFigureDelegate1 : fFigureDelegate2;
    }
}