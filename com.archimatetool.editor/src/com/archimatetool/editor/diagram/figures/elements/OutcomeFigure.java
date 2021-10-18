/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;


/**
 * Figure for an Outcome
 * 
 * @author Phillip Beauvoir
 */
public class OutcomeFigure extends AbstractMotivationFigure {
    
    public OutcomeFigure() {
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
        Point center = getCenter(rect);
        
        path.addArc((float)center.preciseX() - radius, (float)center.preciseY() - radius, (radius * 2), (radius * 2), 0, 360);
        
        graphics.fillPath(path);

        disposeGradientPattern(graphics, gradient);
        
        // Lines
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        
        graphics.drawPath(path);
        
        path.dispose();
        
        graphics.setBackgroundColor(getLineColor());
        
        int radius2 = Math.round(radius * 2.0f / 3.0f - (graphics.getLineWidth() / 2));
        graphics.drawOval(center.x - radius2, center.y - radius2, 2 * radius2, 2 * radius2);

        int radius3 = Math.round(radius / 3.0f - (graphics.getLineWidth() / 2));
        graphics.drawOval(center.x - radius3, center.y - radius3, 2 * radius3, 2 * radius3);
        
        //int arrowLineWidth = Math.round(graphics.getLineWidth() * 1.2f);
        //graphics.setLineWidth(arrowLineWidth);
        
        int arrowLength = (int)(radius3 * 0.8f);
        int arrowLineWidth = Math.max(2, arrowLength / 6); // calculate this now but don't set it in graphics
        
        // Small adjustment for the arrow head
        Rectangle rectTemp = rect.getCopy();
        rectTemp.width--;
        Point centerTemp = getCenter(rectTemp);

        graphics.fillPolygon(new int[] { centerTemp.x - arrowLineWidth, centerTemp.y + arrowLineWidth,
                                         centerTemp.x + arrowLength, centerTemp.y, centerTemp.x, centerTemp.y - arrowLength });

        graphics.setLineWidth(arrowLineWidth); // Now set this
        
        double ratio = 1.2d;
        graphics.drawLine(center.x, center.y,
                         (int)(center.x + radius * ratio - 0.5d * arrowLength),
                         (int)(center.y - radius * ratio + 0.5d * arrowLength));
        
        graphics.drawLine((int)(center.x + ratio * (radius - arrowLength * 1.2d)),
                               (int)(center.y + ratio * (arrowLength * 1.2d - radius)),
                               (int)(center.x + radius * ratio),
                               (int)(center.y - radius * ratio) + arrowLength + arrowLineWidth);
        
        graphics.drawLine((int)(center.x + ratio * (radius - arrowLength * 1.2d)),
                          (int)(center.y + ratio * (arrowLength * 1.2d - radius)),
                          (int)(center.x + radius * ratio) - arrowLength - arrowLineWidth,
                          (int)(center.y - radius * ratio));
        
        // Image Icon
        drawIconImage(graphics, imageBounds, 0, 0, 0, 0);
        
        graphics.popState();
    }
    
    private Point getCenter(Rectangle rect) {
        int radius = getRadius(rect);
        int figureWidth = (int)(radius * 2.2f);
        int figureHeight = (int)(radius * 2.2f);
        return new PrecisionPoint(rect.x + radius + (rect.width - figureWidth) / 2, rect.y + rect.height - radius - (rect.height - figureHeight) / 2);
    }

    private int getRadius(Rectangle rect) {
        int r1 = Math.round(rect.height / 2.2F);
        int r2 = Math.round(rect.width / 2.2F);
        return Math.min(r1, r2);
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
        
        // circles
        path.addArc(pt.x, pt.y, 13, 13, 0, 360);
        path.addArc(pt.x + 2.5f, pt.y + 2.5f, 8, 8, 0, 360);
        path.addArc(pt.x + 5f, pt.y + 5f, 3, 3, 0, 360);
        path.addArc(pt.x + 6f, pt.y + 6f, 1f, 1f, 0, 360);
        graphics.drawPath(path);
        path.dispose();
        
        graphics.setLineWidth(1);

        // lines
        path = new Path(null);
        
        path.moveTo(pt.x + 6.0f, pt.y + 7.0f);
        path.lineTo(pt.x + 15.5f, pt.y - 2.5f);
        
        path.moveTo(pt.x + 13.0f, pt.y + 0.0f);
        path.lineTo(pt.x + 14.0f, pt.y - 5.0f);
        
        path.moveTo(pt.x + 13.0f, pt.y + 0.0f);
        path.lineTo(pt.x + 18.0f, pt.y - 1.0f);
        
        graphics.drawPath(path);
        path.dispose();

        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    private Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 25, bounds.y + 9);
    }
    
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 27 : 0;
    }
    
    @Override
    protected int getTextControlMarginHeight() {
        return getDiagramModelArchimateObject().getType() == 0 ? super.getTextControlMarginHeight() : 0;
    }
}