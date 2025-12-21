/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.editor.ui.IIconDelegate;




/**
 * DistributionNetwork Figure
 * 
 * @author Phillip Beauvoir
 */
public class DistributionNetworkFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private static final double ARROW_ANGLE = Math.cos(Math.toRadians(60));

    private IFigureDelegate rectangleDelegate;
    
    public DistributionNetworkFigure() {
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
        
        setFigurePositionFromTextPosition(rect, 5/3.0);
        
        // Calculate line width depending on size
        int lineWidth = (int)Math.max(3, Math.sqrt(rect.width * rect.height) / 24);

        // Shrink the arrow size area depending on line width
        Dimension arrowSize = getArrowSize(rect);
        rect.shrink(lineWidth, lineWidth);
        arrowSize = getArrowSize(rect);

        // Fill
        fillSection(graphics, rect, arrowSize);

        // Lines
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        
        graphics.setLineWidth(lineWidth);
        
        drawArrows(graphics, rect, arrowSize);
        
        drawHorizontalLine(graphics, rect, arrowSize);
        
        // Image Icon
        drawIconImage(graphics, rect, 0, 0, 0, 0);
        
        graphics.popState();
    }
    
    protected void fillSection(Graphics graphics, Rectangle rect, Dimension arrow) {
        graphics.setBackgroundColor(getFillColor());
        graphics.setAlpha(getAlpha());
        Pattern gradient = applyGradientPattern(graphics, rect);
        
        Path path = new Path(null);
        
        Rectangle line1 = getLine1(rect, arrow);
        Rectangle line2 = getLine2(rect, arrow);
        
        path.moveTo(line1.x, line1.y);
        
        // Left to right horizontal
        path.lineTo(line1.width, line1.height);
        
        // Down/Right at angle
        path.lineTo(rect.x + rect.width, rect.y + rect.height / 2);
        
        // Down/Left at angle
        path.lineTo(line1.width, line2.y);
        
        // Right to left horizontal
        path.lineTo(line2.x, line2.y);
        
        // Up/Left at angle
        path.lineTo(rect.x, rect.y + rect.height / 2);
        
        path.close();
        
        graphics.fillPath(path);
        
        path.dispose();
        
        disposeGradientPattern(graphics, gradient);
    }
    
    private Dimension getArrowSize(Rectangle rect) {
        int width = (int)(rect.width / (1 + ARROW_ANGLE) / 2);
        int size = Math.min(rect.height, width);
        return new Dimension((int)(size * ARROW_ANGLE), size);
    }

    private void drawArrows(Graphics graphics, Rectangle rect, Dimension arrow) {
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

    private Rectangle getLine1(Rectangle rect, Dimension arrow) {
        return new Rectangle(rect.x + arrow.height / 5,
                rect.y + rect.height / 2 - arrow.height / 5,
                rect.x + rect.width - arrow.height / 5,
                rect.y + rect.height / 2 - arrow.height / 5);
    }
    
    private Rectangle getLine2(Rectangle rect, Dimension arrow) {
        return new Rectangle(rect.x + arrow.height / 5,
                rect.y + rect.height / 2 + arrow.height / 5,
                rect.x + rect.width - arrow.height / 5,
                rect.y + rect.height / 2 + arrow.height / 5);
    }

    protected void drawHorizontalLine(Graphics graphics, Rectangle rect, Dimension arrow) {
        graphics.setLineCap(SWT.CAP_ROUND);
        
        Rectangle line1 = getLine1(rect, arrow);
        graphics.drawLine(line1.x, line1.y, line1.width, line1.height);
        
        Rectangle line2 = getLine2(rect, arrow);
        graphics.drawLine(line2.x, line2.y, line2.width, line2.height);
    }
    
    protected void drawIcon(Graphics graphics) {
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

            graphics.setLineWidthFloat(1.2f);
            
            if(foregroundColor != null) {
                graphics.setForegroundColor(foregroundColor);
            }

            if(backgroundColor != null) {
                graphics.setBackgroundColor(backgroundColor);
            }
            
            Path path = new Path(null);
            
            path.moveTo(pt.x + 1, pt.y - 2);
            path.lineTo(pt.x + 14, pt.y - 2);
            
            path.moveTo(pt.x + 1, pt.y + 2);
            path.lineTo(pt.x + 14, pt.y + 2);

            if(backgroundColor != null) {
                Path path2 = new Path(null);
                
                path2.moveTo(pt.x + 1, pt.y - 2);
                path2.lineTo(pt.x + 14, pt.y - 2);
                path2.lineTo(pt.x + 16, pt.y + 1);
                path2.lineTo(pt.x + 14, pt.y + 2);
                path2.lineTo(pt.x + 1, pt.y + 2);
                path2.lineTo(pt.x, pt.y + 2);
                path2.lineTo(pt.x, pt.y - 2);
                
                graphics.fillPath(path2);
                path2.dispose();
            }
            
            path.moveTo(pt.x + 4, pt.y - 5);
            path.lineTo(pt.x - 1, pt.y);
            path.lineTo(pt.x + 4, pt.y + 5);
            
            path.moveTo(pt.x + 11, pt.y - 5);
            path.lineTo(pt.x + 16, pt.y);
            path.lineTo(pt.x + 11, pt.y + 5);
     
            graphics.drawPath(path);
            path.dispose();

            graphics.popState();
        }
    };
    
    public static IIconDelegate getIconDelegate() {
        return iconDelegate;
    }

    private Point getIconOrigin() {
        Rectangle rect = getBounds();
        return new Point(rect.x + rect.width - 19 - getLineWidth(), rect.y + 12);
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
