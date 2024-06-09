/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;




/**
 * Deliverable Figure
 * 
 * @author Phillip Beauvoir
 */
public class DeliverableFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private IFigureDelegate rectangleDelegate;
    
    public DeliverableFigure() {
        super(TEXT_FLOW_CONTROL);
        rectangleDelegate = new RectangleFigureDelegate(this);
    }
    
    @Override
    protected void drawFigure(Graphics graphics) {
        if(getFigureDelegate() != null) {
            getFigureDelegate().drawFigure(graphics);
            drawIcon(graphics);
            return;
        }
        
        graphics.pushState();
        
        Rectangle bounds = getBounds().getCopy();
        
        bounds.width--;
        bounds.height--;
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, bounds);
        
        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        Path path = getFigurePath(8, bounds, (float)getLineWidth() / 2);
        
        // Main Fill
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, bounds);
        graphics.fillPath(path);
        disposeGradientPattern(graphics, gradient);

        // Outline
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.drawPath(path);
        
        path.dispose();
        
        // Icon
        // drawIconImage(graphics, bounds);
        drawIconImage(graphics, bounds, 0, 0, -14, 0);

        graphics.popState();
    }
    
    protected Path getFigurePath(float curveHeight, Rectangle rect, float lineOffset) {
        float curveY = rect.bottom() - curveHeight;
        
        Path path = new Path(null);
        
        path.moveTo(rect.x, rect.y);
        path.lineTo(rect.x, curveY - 1);
        
        path.quadTo(rect.x + (rect.width / 4), rect.bottom() + curveHeight,
                    rect.x + (rect.width / 2) + 1, curveY);

        path.quadTo(rect.right() - (rect.width / 4), curveY - curveHeight - 1,
                    rect.right(), curveY);
        
        path.lineTo(rect.x + rect.width, rect.y);
        path.lineTo(rect.x - lineOffset, rect.y);
        
        return path;
    }
    
    /**
     * Draw the icon
     */
    protected void drawIcon(Graphics graphics) {
        if(!isIconVisible()) {
            return;
        }
        
        graphics.pushState();
        
        graphics.setLineWidth(1);
        graphics.setForegroundColor(getIconColor());
        
        Point pt = getIconOrigin();
        Rectangle rect = new Rectangle(pt.x, pt.y, 14, 10);
        
        Path path = getFigurePath(1.5f, rect, 0.5f);
        graphics.drawPath(path);
        path.dispose();
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 17 - getLineWidth(), bounds.y + 6);
    }
    
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 21 : 0;
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? rectangleDelegate : null;
    }
}
