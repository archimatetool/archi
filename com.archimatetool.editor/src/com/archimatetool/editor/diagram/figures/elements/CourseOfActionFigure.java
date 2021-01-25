/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.RoundedRectangleFigureDelegate;


/**
 * Figure for a Resource
 * 
 * @author Phillip Beauvoir
 */
public class CourseOfActionFigure extends AbstractTextControlContainerFigure {
    
    public CourseOfActionFigure() {
        super(TEXT_FLOW_CONTROL);
        // Use a Rounded Rectangle Figure Delegate to Draw
        setFigureDelegate(new RoundedRectangleFigureDelegate(this, 25 - getTextControlMarginWidth()));
    }
    
    @Override
    protected void drawFigure(Graphics graphics) {
        super.drawFigure(graphics);
        drawIcon(graphics);
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
        graphics.setBackgroundColor(isEnabled() ? ColorConstants.black : ColorConstants.gray);
        
        // triangle of shortcut glyph
        Path path = new Path(null);
        Point pt = getIconOrigin();
        float x = pt.x - 5.4f, y = pt.y + 9f;

        path.moveTo(x, y);
        path.lineTo(x + 6f, y + 1f);
        path.lineTo(x + 3f, y + 6.2f);
        
        graphics.fillPath(path);
        path.dispose();
        
        // line of shortcut glyph
        path = new Path(null);
        graphics.setLineWidthFloat(2f);
        path.addArc(pt.x - 7.5f, pt.y + 12f, 10, 10, 90, 80);
        graphics.drawPath(path);
        path.dispose();
        
        // 2 circles and blob
        graphics.setLineWidthFloat(1.2f);
        path = new Path(null);
        pt = getIconOrigin();
        
        path.addArc(pt.x, pt.y, 13, 13, 0, 360);
        path.addArc(pt.x + 2.5f, pt.y + 2.5f, 8, 8, 0, 360);
        path.addArc(pt.x + 5f, pt.y + 5f, 3, 3, 0, 360);
        path.addArc(pt.x + 6f, pt.y + 6f, 1f, 1f, 0, 360);
        
        graphics.drawPath(path);
        path.dispose();

        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.getRight().x - 17, bounds.y + 3);
    }

}