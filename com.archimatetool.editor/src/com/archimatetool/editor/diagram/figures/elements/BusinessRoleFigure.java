/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.editor.ui.IIconDelegate;


/**
 * Figure for a Business Role
 * 
 * @author Phillip Beauvoir
 */
public class BusinessRoleFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private IFigureDelegate rectangleDelegate;
    private IFigureDelegate cylinderDelegate;
    
    public BusinessRoleFigure() {
        super(TEXT_FLOW_CONTROL);
        rectangleDelegate = new RectangleFigureDelegate(this);
        cylinderDelegate = new CylinderFigureDelegate(this);
    }
    
    @Override
    protected void drawFigure(Graphics graphics) {
        IFigureDelegate figureDelegate = getFigureDelegate();
        figureDelegate.drawFigure(graphics);
        if(figureDelegate == rectangleDelegate) {
            drawIcon(graphics);
        }
    }
    
    /**
     * Draw the icon
     */
    private void drawIcon(Graphics graphics) {
        if(isIconVisible()) {
            getIconDelegate().drawIcon(graphics, getIconColor(), null, getIconOrigin());
        }
    }
    
    private static IIconDelegate iconDelegate = new IIconDelegate() {
        @Override
        public void drawIcon(Graphics graphics, Color foregroundColor, Color backgroundColor, Point pt) {
            graphics.pushState();
            
            // Ensure this is set
            graphics.setAntialias(SWT.ON);

            graphics.setLineWidth(1);
            
            if(foregroundColor != null) {
                graphics.setForegroundColor(foregroundColor);
            }
            
            if(backgroundColor != null) {
                graphics.setBackgroundColor(backgroundColor);
            }
            
            Path path = new Path(null);
            
            path.addArc(pt.x, pt.y, 5, 8, 90, 180);
            
            path.lineTo(pt.x + 12, pt.y + 8);
            
            path.moveTo(pt.x + 2f, pt.y);
            path.lineTo(pt.x + 12, pt.y);
            
            if(backgroundColor != null) {
                path.lineTo(pt.x + 12, pt.y + 8);
                graphics.fillPath(path);
            }
            
            graphics.drawPath(path);
            path.dispose();
            
            if(backgroundColor != null) {
                graphics.fillOval(pt.x + 10, pt.y, 5, 8);
            }
            graphics.drawOval(pt.x + 10, pt.y, 5, 8);
            
            graphics.popState();
        }
    };
    
    public static IIconDelegate getIconDelegate() {
        return iconDelegate;
    }

    /**
     * @return The icon start position
     */
    private Point getIconOrigin() {
        Rectangle rect = getBounds();
        return new Point(rect.getRight().x - 17 - getLineWidth(), rect.y + 7);
    }
    
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 20 : 0;
    }
    
    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? rectangleDelegate : cylinderDelegate;
    }
}