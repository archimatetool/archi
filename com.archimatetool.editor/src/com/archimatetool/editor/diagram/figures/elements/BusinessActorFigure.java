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
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;


/**
 * Figure for a Business Actor
 * 
 * @author Phillip Beauvoir
 */
public class BusinessActorFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private IFigureDelegate rectangleDelegate;
    
    public BusinessActorFigure() {
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
        
        Rectangle rect = getBounds().getCopy();
        rect.width--;
        rect.height--;
        
        Rectangle imageBounds = rect.getCopy();
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, 1, rect);
        
        setFigurePositionFromTextPosition(rect, 2/3.0);
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        
        Path path = new Path(null);
        
        int diameter = Math.min(rect.width / 2, rect.height / 3);
        
        path.addArc((rect.x + rect.width / 2 - diameter / 2), (rect.y + rect.height / 2 - diameter - diameter / 2), diameter, diameter, 0, 360);
        graphics.fillPath(path);
        
        disposeGradientPattern(graphics, gradient);
        
        // Lines
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        
        graphics.drawPath(path);
        path.dispose();
        
        graphics.setLineCap(SWT.CAP_ROUND);
        
        graphics.drawLine(rect.x + rect.width / 2,
                          rect.y + rect.height / 2 - diameter / 2,
                          rect.x + rect.width / 2,
                          rect.y + rect.height / 2 - diameter / 2 + diameter);
        
        graphics.drawLine(rect.x + rect.width / 2,
                          rect.y + rect.height / 2 - diameter / 2 + diameter,
                          rect.x + rect.width / 2 - diameter,
                          rect.y + rect.height / 2 - diameter / 2 + diameter + diameter);
        
        graphics.drawLine(rect.x + rect.width / 2,
                          rect.y + rect.height / 2 - diameter / 2 + diameter,
                          rect.x + rect.width / 2 + diameter,
                          rect.y + rect.height / 2 - diameter / 2 + diameter + diameter);
        
        graphics.drawLine(rect.x + rect.width / 2 - diameter,
                          rect.y + rect.height / 2 - diameter / 4,
                          rect.x + rect.width / 2 + diameter,
                          rect.y + rect.height / 2 - diameter / 4);
        
        // Image Icon
        drawIconImage(graphics, imageBounds, 0, 0, 0, 0);
        
        graphics.popState();
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
        
        Point pt = getIconOrigin();
        
        // head
        graphics.drawOval(pt.x, pt.y, 6, 6);
        
        // body
        pt.translate(3, 6);
        graphics.drawLine(pt.x, pt.y, pt.x, pt.y + 6);
        
        // legs
        pt.translate(0, 6);
        graphics.drawLine(pt.x, pt.y, pt.x - 4, pt.y + 5);
        graphics.drawLine(pt.x, pt.y, pt.x + 4, pt.y + 5);
        
        // arms
        pt.translate(-4, -3);
        graphics.drawLine(pt.x, pt.y, pt.x + 8, pt.y);
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    private Point getIconOrigin() {
        Rectangle bounds = getBounds().getCopy();
        return new Point(bounds.x + bounds.width - 13, bounds.y + 4);
    }

    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 17 : 0;
    }
    
    @Override
    protected int getTextControlMarginHeight() {
        return getDiagramModelArchimateObject().getType() == 0 ? super.getTextControlMarginHeight() : 0;
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? rectangleDelegate : null;
    }
}