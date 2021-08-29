/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Path;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;




/**
 * Communication Network Figure
 * 
 * @author Phillip Beauvoir
 */
public class CommunicationNetworkFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private static final double ARROW_ANGLE = Math.cos(Math.toRadians(60));

    private IFigureDelegate rectangleDelegate;
    
    public CommunicationNetworkFigure() {
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
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        
        Dimension arrowSize = getArrowSize(rect);
        int lineWidth = (int)(Math.sqrt(rect.width * rect.height) / 22);
        rect.shrink(lineWidth, lineWidth);
        arrowSize = getArrowSize(rect);
        
        graphics.setLineWidth(lineWidth);
        
        drawArrows(graphics, rect, arrowSize);
        
        drawHorizontalLine(graphics, rect, arrowSize);
        
        // Image Icon
        drawIconImage(graphics, rect, 0, 0, 0, 0);
        
        graphics.popState();
    }
    
    protected void drawArrows(Graphics graphics, Rectangle rect, Dimension arrow) {
        graphics.setLineCap(SWT.CAP_ROUND);

        graphics.drawLine(rect.x + arrow.width,
                          rect.y + rect.height / 2 - arrow.height / 2,
                          rect.x,
                          rect.y + rect.height / 2);
        
        graphics.drawLine(rect.x,
                          rect.y + rect.height / 2,
                          rect.x + arrow.width,
                          rect.y + rect.height / 2 + arrow.height / 2);
        
        graphics.drawLine(rect.x + rect.width - arrow.width,
                          rect.y + rect.height / 2 - arrow.height / 2,
                          rect.x + rect.width,
                          rect.y + rect.height / 2);
        
        graphics.drawLine(rect.x + rect.width,
                          rect.y + rect.height / 2,
                          rect.x + rect.width - arrow.width,
                          rect.y + rect.height / 2 + arrow.height / 2);
    }
    
    protected void drawHorizontalLine(Graphics graphics, Rectangle rect, Dimension arrow) {
        graphics.setLineCap(SWT.CAP_ROUND);
        
        graphics.drawLine(rect.x,
                          rect.y + rect.height / 2,
                          rect.x + rect.width,
                          rect.y + rect.height / 2);
    }
    
    private Dimension getArrowSize(Rectangle rect) {
        int width = (int)(rect.width / (1 + ARROW_ANGLE) / 2);
        int size = Math.min(rect.height, width);
        return new Dimension((int)(size * ARROW_ANGLE), size);
    }
    
    /**
     * Draw the icon
     */
    protected void drawIcon(Graphics graphics) {
        if(!isIconVisible()) {
            return;
        }
        
        graphics.pushState();
        
        graphics.setLineWidthFloat(1);
        graphics.setForegroundColor(isEnabled() ? ColorConstants.black : ColorConstants.gray);
        
        Point pt = getIconOrigin();
        
        Path path = new Path(null);
        
        path.addArc(pt.x, pt.y, 5, 5, 0, 360);
        path.addArc(pt.x + 2, pt.y - 8, 5, 5, 0, 360);
        path.addArc(pt.x + 10, pt.y - 8, 5, 5, 0, 360);
        path.addArc(pt.x + 8, pt.y, 5, 5, 0, 360);
        
        path.moveTo(pt.x + 3, pt.y);
        path.lineTo(pt.x + 4, pt.y - 3);
        
        path.moveTo(pt.x + 11, pt.y);
        path.lineTo(pt.x + 12, pt.y - 3);
        
        path.moveTo(pt.x + 5, pt.y + 2.5f);
        path.lineTo(pt.x + 8, pt.y + 2.5f);
        
        path.moveTo(pt.x + 7, pt.y - 5.5f);
        path.lineTo(pt.x + 10, pt.y - 5.5f);
        
        graphics.drawPath(path);
        path.dispose();
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 20, bounds.y + 14);
    }

    @Override
    public int getIconOffset() {
        return 22;
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? rectangleDelegate : null;
    }
}
