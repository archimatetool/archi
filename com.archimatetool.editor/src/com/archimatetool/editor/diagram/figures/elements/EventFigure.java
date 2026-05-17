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
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RoundedRectangleFigureDelegate;
import com.archimatetool.editor.ui.IIconDelegate;




/**
 * Event Figure
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
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
        
        Rectangle rect = getBounds().getCopy();
        
        Path path = createPath(rect);
        
        // Main Fill
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        graphics.fillPath(path);
        disposeGradientPattern(graphics, gradient);

        // Icon
        drawIconImage(graphics, getBounds().getCopy());

        // Outline
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        FigureUtils.drawPath(graphics, path, getLineWidth());
        
        path.dispose();
        
        graphics.popState();
    }
    
    private Path createPath(Rectangle rect) {
        int indent = Math.min(rect.height / 3, rect.width / 3);
        int centre_y = rect.y + rect.height / 2 - 1;
        int arc_startx = rect.x + rect.width - indent;
        
        Path path = new Path(null);
        path.moveTo(rect.x, rect.y);
        path.lineTo(rect.x + indent, centre_y);
        path.lineTo(rect.x, rect.y + rect.height);
        path.lineTo(arc_startx, rect.y + rect.height);
        path.addArc(arc_startx - indent, rect.y, indent * 2, rect.height, -90, 180);
        path.close();
        return path;
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
        Rectangle rect = getBounds();
        return new Point(rect.x + rect.width - 20, rect.y + 6);
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
