/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RoundedRectangleFigureDelegate;
import com.archimatetool.editor.ui.IIconDelegate;




/**
 * Event Figure
 * 
 * @author Phillip Beauvoir
 */
public class EventFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    protected IFigureDelegate fMainFigureDelegate;

    public EventFigure() {
        super(TEXT_FLOW_CONTROL);
        fMainFigureDelegate = new RoundedRectangleFigureDelegate(this);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
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
        
        int indent = Math.min(bounds.height / 3, bounds.width / 3);
        int centre_y = bounds.y + bounds.height / 2 - 1;
        int arc_startx = bounds.x + bounds.width - indent;
        
        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        // Main Fill
        Path path = new Path(null);
        path.moveTo(bounds.x, bounds.y);
        path.lineTo(bounds.x + indent, centre_y);
        path.lineTo(bounds.x, bounds.y + bounds.height);
        path.lineTo(arc_startx, bounds.y + bounds.height);
        path.addArc(arc_startx - indent, bounds.y, indent * 2, bounds.height, -90, 180);
        path.lineTo(bounds.x, bounds.y);
        path.lineTo(bounds.x + indent, centre_y);
        
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
        drawIconImage(graphics, bounds);

        graphics.popState();
    }

    /**
     * Draw the icon
     */
    protected void drawIcon(Graphics graphics) {
        if(isIconVisible()) {
            getIconDelegate().drawIcon(graphics, getIconColor(), null, getIconOrigin());
        }
    }
    
    private static IIconDelegate iconDelegate = new IIconDelegate() {
        @Override
        public void drawIcon(Graphics graphics, Color foregroundColor, Color backgroundColor, Point pt) {
            graphics.pushState();
            
            graphics.setLineWidth(1);
            
            if(foregroundColor != null) {
                graphics.setForegroundColor(foregroundColor);
            }
            
            if(backgroundColor != null) {
                graphics.setBackgroundColor(backgroundColor);
            }
            
            if(backgroundColor != null) {
                Path path = new Path(null);
                
                path.addArc(pt.x + 8, pt.y, 8, 9, 270, 180);
                path.addRectangle(pt.x, pt.y, 12, 9);
                path.addArc(pt.x - 4, pt.y, 8, 9, 270, 180);
                
                graphics.fillPath(path);
                path.dispose();
            }
            
            // arc
            Path path = new Path(null);
            path.moveTo(pt.x, pt.y);
            path.addArc(pt.x - 4, pt.y, 8, 9, 270, 180);
            graphics.drawPath(path);
            path.dispose();
            
            // arc 2
            path = new Path(null);
            path.addArc(pt.x + 8, pt.y, 8, 9, 270, 180);

            // lines
            path.moveTo(pt.x, pt.y);
            path.lineTo(pt.x + 12, pt.y);
            
            path.moveTo(pt.x, pt.y + 9);
            path.lineTo(pt.x + 12, pt.y + 9);
            
            graphics.drawPath(path);
            path.dispose();
            
            graphics.popState();
        }
    };
    
    public static IIconDelegate getIconDelegate() {
        return iconDelegate;
    }

    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 19 - getLineWidth(), bounds.y + 7);
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? fMainFigureDelegate : null;
    }
    
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 22 : 0;
    }
}
