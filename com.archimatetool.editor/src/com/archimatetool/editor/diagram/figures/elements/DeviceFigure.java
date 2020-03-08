/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.FigureUtils.Direction;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelObject;


/**
 * Figure for a Device
 * 
 * @author Phillip Beauvoir
 */
public class DeviceFigure extends AbstractTextControlContainerFigure {
    
    protected static final int INDENT = 15;

    protected IFigureDelegate fFigureDelegate;
    
    public DeviceFigure() {
        super(TEXT_FLOW_CONTROL);
        fFigureDelegate = new BoxFigureDelegate(this, 20 - getTextControlMarginWidth());
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        if(getFigureDelegate() != null) {
            getFigureDelegate().drawFigure(graphics);
            drawIcon(graphics);
            return;
        }

        graphics.pushState();
        
        Rectangle bounds = getBounds().getCopy();
        
        int height_indent = bounds.height / 6;
        
        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        // Bottom part
        PointList points1 = new PointList();
        points1.addPoint(bounds.x, bounds.y + bounds.height - 1);
        points1.addPoint(bounds.x + INDENT , bounds.y + bounds.height - height_indent - 1);
        points1.addPoint(bounds.x + bounds.width - INDENT, bounds.y + bounds.height - height_indent - 1);
        points1.addPoint(bounds.x + bounds.width, bounds.y + bounds.height - 1);
                
        graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));
        graphics.fillPolygon(points1);
        
        graphics.setForegroundColor(getLineColor());
        graphics.setAlpha(getLineAlpha());
        graphics.drawLine(bounds.x, bounds.y + bounds.height - 1,
                bounds.x + bounds.width, bounds.y + bounds.height - 1);
        graphics.drawLine(bounds.x, bounds.y + bounds.height - 1,
                bounds.x + INDENT, bounds.y + bounds.height - height_indent - 1);
        graphics.drawLine(bounds.x + bounds.width, bounds.y + bounds.height - 1,
                bounds.x + bounds.width - INDENT, bounds.y + bounds.height - height_indent - 1);

        // Top part
        Rectangle rect = new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height - height_indent);

        graphics.setBackgroundColor(getFillColor());
        graphics.setAlpha(getAlpha());

        Pattern gradient = null;
        if(getGradient() != IDiagramModelObject.GRADIENT_NONE) {
            gradient = FigureUtils.createGradient(graphics, bounds, getFillColor(), getAlpha(), Direction.get(getGradient()));
            graphics.setBackgroundPattern(gradient);
        }
        
        graphics.fillRoundRectangle(rect, 30, 30);
        
        if(gradient != null) {
            gradient.dispose();
        }

        graphics.setForegroundColor(getLineColor());
        graphics.setAlpha(getLineAlpha());
        rect = new Rectangle(bounds.x, bounds.y, bounds.width - 1, bounds.height - height_indent - 1);
        graphics.drawRoundRectangle(rect, 30, 30);
        
        graphics.popState();
    }
    
    /**
     * Draw the icon
     */
    protected void drawIcon(Graphics graphics) {
        graphics.pushState();
        
        graphics.setLineWidth(1);
        graphics.setForegroundColor(isEnabled() ? ColorConstants.black : ColorConstants.gray);
        
        Point pt = getIconOrigin();
        
        graphics.drawRoundRectangle(new Rectangle(pt.x, pt.y, 11, 8), 3, 3);
        
        graphics.drawPolygon(new int[] {
                pt.x - 1, pt.y + 12,
                pt.x + 2, pt.y + 8,
                pt.x + 9, pt.y + 8,
                pt.x + 12, pt.y + 12
        });
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 31, bounds.y + 20);
    }
    
    @Override
    public IFigureDelegate getFigureDelegate() {
        int type = getDiagramModelObject().getType();
        return type == 1 ? fFigureDelegate : null;
    }
    
    @Override
    public IDiagramModelArchimateObject getDiagramModelObject() {
        return (IDiagramModelArchimateObject)super.getDiagramModelObject();
    }

}