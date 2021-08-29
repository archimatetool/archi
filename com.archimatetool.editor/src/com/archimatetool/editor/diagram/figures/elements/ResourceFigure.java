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
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;


/**
 * Figure for a Resource
 * 
 * @author Phillip Beauvoir
 */
public class ResourceFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private IFigureDelegate rectangleDelegate;
    
    public ResourceFigure() {
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
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, 1, rect);
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        
        Path path = getFigurePath(rect);
        graphics.fillPath(path);
        
        disposeGradientPattern(graphics, gradient);
        
        // Lines
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        
        graphics.drawPath(path);
        
        path.dispose();
        
        Dimension nubSize = new Dimension(rect.width / 10, rect.height / 3);
        
        graphics.drawLine(rect.x + rect.width - nubSize.width,
                rect.y + (rect.height - nubSize.height) / 2,
                rect.x + rect.width - nubSize.width,
                rect.y + (rect.height - nubSize.height) / 2 + nubSize.height);

        
        int lineTop = rect.y + rect.height / 5;
        int lineBottom = rect.y + rect.height * 4 / 5;
        int lineGap = rect.width / 6;
        
        graphics.drawLine(rect.x + lineGap, lineTop, rect.x + lineGap, lineBottom);
        graphics.drawLine(rect.x + lineGap * 2, lineTop, rect.x + lineGap * 2, lineBottom);
        graphics.drawLine(rect.x + lineGap * 3, lineTop, rect.x + lineGap * 3, lineBottom);
        
        // Image Icon
        drawIconImage(graphics, rect, 0, 0, 0, 0);
        
        graphics.popState();
    }

    private Path getFigurePath(Rectangle rect) {
        Dimension nubSize = new Dimension(rect.width / 10, rect.height / 3);
        int arc1 = 5;
        int arc2 = 3;
        
        Path path = new Path(null);
        
        path.moveTo(rect.x + arc1, rect.y);
        path.lineTo(rect.x + rect.width - nubSize.width - arc1, rect.y);
        
        path.cubicTo(rect.x + rect.width - nubSize.width - arc1,
                     rect.y,
                     rect.x + rect.width - nubSize.width,
                     rect.y,
                     rect.x + rect.width - nubSize.width,
                     rect.y + arc1);
        
        path.lineTo(rect.x + rect.width - nubSize.width, rect.y + (rect.height - nubSize.height) / 2);
        path.lineTo(rect.x + rect.width - arc1, rect.y + (rect.height - nubSize.height) / 2);
        
        path.cubicTo(rect.x + rect.width - arc2,
                     rect.y + (rect.height - nubSize.height) / 2,
                     rect.x + rect.width,
                     rect.y + (rect.height - nubSize.height) / 2,
                     rect.x + rect.width,
                     rect.y + (rect.height - nubSize.height) / 2 + arc2);
        
        path.lineTo(rect.x + rect.width, rect.y + (rect.height - nubSize.height) / 2 + nubSize.height - arc1);
        
        path.cubicTo(rect.x + rect.width,
                     rect.y + (rect.height - nubSize.height) / 2 + nubSize.height - arc2,
                     rect.x + rect.width,
                     rect.y + (rect.height - nubSize.height) / 2 + nubSize.height,
                     rect.x + rect.width - arc2,
                     rect.y + (rect.height - nubSize.height) / 2 + nubSize.height);
        
        path.lineTo(rect.x + rect.width - nubSize.width, rect.y + (rect.height - nubSize.height) / 2 + nubSize.height);
        path.lineTo(rect.x + rect.width - nubSize.width, rect.y + rect.height - arc1);
        
        path.cubicTo(rect.x + rect.width - nubSize.width,
                     rect.y + rect.height - arc1,
                     rect.x + rect.width - nubSize.width,
                     rect.y + rect.height,
                     rect.x + rect.width - nubSize.width - arc1,
                     rect.y + rect.height);
        
        path.lineTo(rect.x + arc1, rect.y + rect.height);
        path.cubicTo(rect.x + arc1, rect.y + rect.height, rect.x, rect.y + rect.height, rect.x, rect.y + rect.height - arc1);
        path.lineTo(rect.x, rect.y + arc1);
        path.cubicTo(rect.x, rect.y + arc1, rect.x, rect.y, rect.x + arc1, rect.y);
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
        
        Point pt = getIconOrigin();
        
        // main rectangle
        graphics.drawRoundRectangle(new Rectangle(pt.x, pt.y, 15, 10), 3, 3);
        
        // nub
        pt.translate(15, 3);
        graphics.drawRoundRectangle(new Rectangle(pt.x, pt.y, 2, 4), 1, 1);
        
        // lines
        pt = getIconOrigin();
        Path path = new Path(null);
        
        path.moveTo(pt.x + 3f, pt.y + 2);
        path.lineTo(pt.x + 3f, pt.y + 8);
        
        path.moveTo(pt.x + 6f, pt.y + 2);
        path.lineTo(pt.x + 6f, pt.y + 8);
        
        path.moveTo(pt.x + 9f, pt.y + 2);
        path.lineTo(pt.x + 9f, pt.y + 8);
        
        graphics.drawPath(path);
        path.dispose();

        // Alternate method of drawing lines
//        pt.translate(-12, -1);
//        graphics.drawLine(pt.x, pt.y, pt.x, pt.y + 6);
//        pt.translate(3, 0);
//        graphics.drawLine(pt.x, pt.y, pt.x, pt.y + 6);
//        pt.translate(3, 0);
//        graphics.drawLine(pt.x, pt.y, pt.x, pt.y + 6);
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    private Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.getRight().x - 20, bounds.y + 7);
    }

    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 22 : 0;
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? rectangleDelegate : null;
    }
}