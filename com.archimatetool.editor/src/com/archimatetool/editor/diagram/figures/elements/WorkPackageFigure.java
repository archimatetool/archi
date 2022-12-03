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
import org.eclipse.swt.graphics.Path;

import com.archimatetool.editor.diagram.editparts.RoundedRectangleAnchor;
import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RoundedRectangleFigureDelegate;




/**
 * Work Package Figure
 * 
 * @author Phillip Beauvoir
 */
public class WorkPackageFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private RoundedRectangleFigureDelegate figureDelegate;

    public WorkPackageFigure() {
        super(TEXT_FLOW_CONTROL);
        
        // Use a Rounded Rectangle Figure Delegate to Draw
        figureDelegate = new RoundedRectangleFigureDelegate(this);
    }
    
    @Override
    public ConnectionAnchor getDefaultConnectionAnchor() {
        return getDiagramModelArchimateObject().getType() == 0 ? new RoundedRectangleAnchor(this) : super.getDefaultConnectionAnchor();
    }
    
    @Override
    protected void drawFigure(Graphics graphics) {
        if(getFigureDelegate() != null) {
            getFigureDelegate().drawFigure(graphics);
            drawIcon(graphics);
            return;
        }
        
        graphics.pushState();
        
        Rectangle rect = getBounds().getCopy();
        
        Rectangle imageBounds = rect.getCopy();
        
        setFigurePositionFromTextPosition(rect);
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.setBackgroundColor(getLineColor());
        
        int lineWidth = (int)(Math.sqrt(rect.width * rect.height) / 10);
        graphics.setLineWidth(lineWidth);
        
        Path path = new Path(null);
        
        int radius = getRadius(rect);
        int actualRadius = getRadius(rect) - Math.round(radius / 10.0f) - lineWidth / 2;
        Point center = rect.getCenter();
        
        // Semi-circle
        path.addArc((float)center.preciseX() - actualRadius,
                    (float)center.preciseY() - actualRadius,
                    actualRadius * 2,
                    actualRadius * 2,
                    320,
                    315);
        
        // Line
        path.moveTo(center.x, center.y + actualRadius);
        path.lineTo(center.x + radius, center.y + actualRadius);
        
        graphics.drawPath(path);
        path.dispose();
        
        // Triangle
        path = new Path(null);
        
        path.moveTo(center.x + radius, center.y + actualRadius - lineWidth);
        path.lineTo(center.x + radius + lineWidth, center.y + actualRadius);
        path.lineTo(center.x + radius, center.y + actualRadius + lineWidth);
        
        path.close();
        
        graphics.fillPath(path);
        path.dispose();

        // Image Icon
        drawIconImage(graphics, imageBounds, 0, 0, 0, 0);
        
        graphics.popState();
    }
    
    private int getRadius(Rectangle rect) {
        int r1 = rect.height / 2;
        int r2 = rect.width / 3;
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
        graphics.setForegroundColor(getIconColor());
        graphics.setBackgroundColor(getIconColor());
        
        Point pt = getIconOrigin();
        
        float circleWidth = 9;
        float circleHalf = circleWidth / 2;
        
        Path path = new Path(null);
        
        // Circle
        path.addArc(pt.x, pt.y, circleWidth, circleWidth, 320, 315);
        
        // Line
        path.moveTo(pt.x + circleHalf, pt.y + circleWidth);
        path.lineTo(pt.x + 10, pt.y + circleWidth);
        
        graphics.drawPath(path);
        path.dispose();
        
        // Triangle
        path = new Path(null);
        
        path.moveTo(pt.x + 10, pt.y + circleWidth - 2);
        path.lineTo(pt.x + 12, pt.y + circleWidth);
        path.lineTo(pt.x + 10, pt.y + circleWidth + 2);
        
        path.close();
        
        graphics.fillPath(path);
        path.dispose();
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    private Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 17, bounds.y + 6);
    }
    
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 18 : 0;
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? figureDelegate : null;
    }
}
