/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.editor.ui.IIconDelegate;


/**
 * Gap Figure
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
public class GapFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private IFigureDelegate rectangleDelegate;
    
    public GapFigure() {
        super(TEXT_FLOW_CONTROL);
        rectangleDelegate = new RectangleFigureDelegate(this);
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
        
        setFigurePositionFromTextPosition(rect, 5/3.0); // Should match 'widthFraction' formula
        
        float widthFraction = 3f * (rect.width / 10f); // 3/10ths of width
        float circleRadius = widthFraction;
        
        // height < width
        if(rect.height < rect.width) {
            circleRadius = Math.min(rect.height / 2f, widthFraction); // half height or 3/10ths
        }

        float xCenter = rect.x + rect.width / 2f;
        float yCenter = rect.y + rect.height / 2f;
        //Rectangle circleRect = new Rectangle(xCenter - circleRadius, yCenter - circleRadius, circleRadius * 2, circleRadius * 2);
        
        // Fill
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        FigureUtils.fillOvalPath(graphics, xCenter - circleRadius, yCenter - circleRadius, circleRadius * 2, circleRadius * 2);
        disposeGradientPattern(graphics, gradient);
        
        // Image Icon
        drawIconImage(graphics, getBounds().getCopy());
        
        // Outline
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.setLineWidth(getLineWidth());
        FigureUtils.drawOvalPath(graphics, xCenter - circleRadius, yCenter - circleRadius, circleRadius * 2, circleRadius * 2, getLineWidth());

        int x1 = (int)(xCenter - (circleRadius + circleRadius / 2));
        int x2 = (int)(xCenter + (circleRadius + circleRadius / 2));
        int y1 = (int)(yCenter - circleRadius / 4);
        int y2 = (int)(yCenter + circleRadius / 4);
        
        graphics.drawLine(x1, y1, x2, y1);
        graphics.drawLine(x1, y2, x2, y2);
        
        graphics.popState();
    }
    
    /**
     * Draw the icon
     */
    private void drawIcon(Graphics graphics) {
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

            graphics.setLineWidth(1);
            
            if(foregroundColor != null) {
                graphics.setForegroundColor(foregroundColor);
            }
            
            if(backgroundColor != null) {
                graphics.setBackgroundColor(backgroundColor);
            }
            
            if(backgroundColor != null) {
                graphics.fillOval(pt.x, pt.y, 13, 13);
            }
            graphics.drawOval(pt.x, pt.y, 13, 13);
            
            pt.translate(-2, 5);
            graphics.drawLine(pt.x, pt.y, pt.x + 17, pt.y);
            
            pt.translate(0, 3);
            graphics.drawLine(pt.x, pt.y, pt.x + 17, pt.y);
            
            graphics.popState();
        }
    };
    
    public static IIconDelegate getIconDelegate() {
        return iconDelegate;
    }

    /**
     * @return The icon start position
     */
    private Point getIconOrigin() {
        Rectangle rect = getBounds();
        return new Point(rect.x + rect.width - 18, rect.y + 6);
    }
    
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 23 : 0;
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? rectangleDelegate : null;
    }

    @Override
    protected int getTextControlMarginHeight() {
        return getDiagramModelArchimateObject().getType() == 0 ? super.getTextControlMarginHeight() : 0;
    }
}
