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
import org.eclipse.swt.graphics.Pattern;


/**
 * Figure for a Goal
 * 
 * @author Phillip Beauvoir
 */
public class GoalFigure extends AbstractMotivationFigure {
    
    public GoalFigure() {
    }
    
    @Override
    protected void drawFigure(Graphics graphics) {
        if(getDiagramModelArchimateObject().getType() == 0) {
            super.drawFigure(graphics);
            drawIcon(graphics);
            return;
        }

        graphics.pushState();
        
        Rectangle rect = getBounds().getCopy();
        rect.width--;
        rect.height--;
        
        Rectangle imageBounds = rect.getCopy();
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, 1, rect);
        
        setFigurePositionFromTextPosition(rect);

        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        // Fill
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        
        Path path = new Path(null);
        
        int radius = getRadius(rect);
        Point center = rect.getCenter();
        path.addArc((float)center.preciseX() - radius, (float)center.preciseY() - radius, radius * 2, radius * 2, 0, 360);
        path.close();
        
        graphics.fillPath(path);

        disposeGradientPattern(graphics, gradient);
        
        // Lines
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        
        graphics.drawPath(path);
        
        path.dispose();
        
        graphics.setBackgroundColor(getLineColor());

        radius = Math.round(radius * 2.0f / 3.0f - (graphics.getLineWidth() / 2));
        graphics.drawOval(center.x - radius, center.y - radius, 2 * radius, 2 * radius);
        
        radius = Math.round(radius / 3.0f - (graphics.getLineWidth() / 2));
        graphics.fillOval(center.x - radius, center.y - radius, 2 * radius, 2 * radius);
        
        // Image Icon
        drawIconImage(graphics, imageBounds, 0, 0, 0, 0);
        
        graphics.popState();
    }
    
    private int getRadius(Rectangle rect) {
        int r1 = rect.height / 2;
        int r2 = rect.width / 2;
        int radius = Math.min(r1, r2);
        return radius - radius % 2;
    }
    
    /**
     * Draw the icon
     */
    private void drawIcon(Graphics graphics) {
        if(!isIconVisible()) {
            return;
        }
        
        graphics.pushState();
        
        graphics.setLineWidth(1);
        graphics.setForegroundColor(isEnabled() ? ColorConstants.black : ColorConstants.gray);
        graphics.setBackgroundColor(isEnabled() ? ColorConstants.black : ColorConstants.gray);
        
        Point pt = getIconOrigin();
        
        Path path = new Path(null);

        graphics.setLineWidthFloat(1.2f);
        
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
    private Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 20, bounds.y + 6);
    }
    
    @Override
    protected int getTextControlMarginHeight() {
        return getDiagramModelArchimateObject().getType() == 0 ? super.getTextControlMarginHeight() : 0;
    }

    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 22 : 0;
    }
}