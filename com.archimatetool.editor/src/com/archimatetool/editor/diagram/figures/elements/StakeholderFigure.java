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

import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.ui.IIconDelegate;


/**
 * Figure for a Stakeholder
 * 
 * @author Phillip Beauvoir
 */
public class StakeholderFigure extends AbstractMotivationFigure {
    
    private IFigureDelegate cylinderDelegate;
    
    public StakeholderFigure() {
        cylinderDelegate = new CylinderFigureDelegate(this);
    }

    @Override
    protected void drawFigure(Graphics graphics) {
        if(getFigureDelegate() != null) {
            getFigureDelegate().drawFigure(graphics);
            return;
        }
        
        super.drawFigure(graphics);
        drawIcon(graphics);
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
            
            path.addArc(pt.x, pt.y, 8, 7, 90, 180);
            if(backgroundColor != null) {
                graphics.fillPath(path);
                graphics.fillRectangle(pt.x + 3, pt.y, 6, 7);
            }
            
            path.lineTo(pt.x + 11, pt.y + 7);
            
            path.moveTo(pt.x + 3.5f, pt.y);
            path.lineTo(pt.x + 11, pt.y);
            
            graphics.drawPath(path);
            
            path.dispose();
            
            Rectangle rect = new Rectangle(pt.x + 8, pt.y, 7, 7);
            if(backgroundColor != null) {
                graphics.fillOval(rect);
            }
            graphics.drawOval(rect);
            
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
        return new Point(rect.x + rect.width - 20 - getLineWidth(), rect.y + 9);
    }
    
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 23 : 0;
    }
    
    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? null : cylinderDelegate;
    }
}