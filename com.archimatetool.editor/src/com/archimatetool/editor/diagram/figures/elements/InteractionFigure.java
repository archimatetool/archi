/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.editparts.RoundedRectangleAnchor;
import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RoundedRectangleFigureDelegate;




/**
 * Interaction Figure
 * 
 * @author Phillip Beauvoir
 */
public class InteractionFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private IFigureDelegate roundedRectangleDelegate;

    public InteractionFigure() {
        super(TEXT_FLOW_CONTROL);
        roundedRectangleDelegate = new RoundedRectangleFigureDelegate(this);
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
        rect.width--;
        rect.height--;
        
        Rectangle imageBounds = rect.getCopy();
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, 1, rect);
        
        setFigurePositionFromTextPosition(rect, 1 / 0.86); // Should match 'FRACTION' defined in getFigurePath()
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        
        Path path = getFigurePath(rect);
        
        graphics.fillPath(path);
        
        disposeGradientPattern(graphics, gradient);
        
        // Line
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        
        graphics.drawPath(path);
        
        path.dispose();
        
        // Image Icon
        drawIconImage(graphics, imageBounds, 0, 0, 0, 0);
        
        graphics.popState();
    }
    
    private Path getFigurePath(Rectangle rect) {
        final float FRACTION = 0.86f;    // fraction to use for diameter in landscape bounds
        final float GAP = 1 - FRACTION;  // gap to use for diameter in landscape bounds
        
        float diameter;
        int x1 = rect.x, x2;

        // width < height or same
        if(rect.width <= rect.height) {
            diameter = rect.width * FRACTION;
            x2 = rect.x + rect.width;
        }
        // height < width
        else {
            diameter = Math.min(rect.height, rect.width * 0.85f); // minimum of height or 85% of width
            x1 += ((rect.width - diameter) / 2) - (diameter * GAP / 2);
            x2 = (int)(x1 + diameter + (diameter * GAP));
        }

        int y = (int)(rect.y + (rect.height - diameter) / 2);
        
        Path path = new Path(null);
        
        path.addArc(x1, y, diameter, diameter, 90, 180);
        path.close();
        
        path.moveTo(x1 + (diameter / 2), y);
        
        path.addArc(x2 - diameter, y, diameter, diameter, 270, 180);
        path.close();
        
        return path;
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
        
        // Start at top
        Point pt = getIconOrigin();
        
        Path path = new Path(null);
        path.addArc(pt.x - 5, pt.y, 10, 12, 90, 180);
        path.lineTo(pt.x, pt.y - 0.5f);
        graphics.drawPath(path);
        path.dispose();
        
        path = new Path(null);
        path.addArc(pt.x - 2, pt.y, 10, 12, -90, 180);
        path.lineTo(pt.x + 3, pt.y + 12.5f);
        graphics.drawPath(path);
        path.dispose();
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    private Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 13, bounds.y + 6);
    }
    
    @Override
    public ConnectionAnchor getDefaultConnectionAnchor() {
        return getDiagramModelArchimateObject().getType() == 0 ? new RoundedRectangleAnchor(this) : super.getDefaultConnectionAnchor();
    }
    
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 20 : 0;
    }

    @Override
    protected int getTextControlMarginHeight() {
        return getDiagramModelArchimateObject().getType() == 0 ? super.getTextControlMarginHeight() : 0;
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? roundedRectangleDelegate : null;
    }
}
