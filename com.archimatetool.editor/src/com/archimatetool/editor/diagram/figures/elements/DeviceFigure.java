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
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.ui.ColorFactory;


/**
 * Figure for a Device
 * 
 * @author Phillip Beauvoir
 */
public class DeviceFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    protected static final int INDENT = 15;

    protected IFigureDelegate fFigureDelegate;
    
    public DeviceFigure() {
        super(TEXT_FLOW_CONTROL);
        fFigureDelegate = new BoxFigureDelegate(this);
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
        setLineWidth(graphics, 1, bounds);
        
        int height_indent = bounds.height / 6;
        
        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        // Bottom part
        graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));

        Path path = new Path(null);
        path.moveTo(bounds.x, bounds.y + bounds.height);
        path.lineTo(bounds.x + INDENT + 1, bounds.y + bounds.height - height_indent);
        path.lineTo(bounds.x + bounds.width - INDENT, bounds.y + bounds.height - height_indent);
        path.lineTo(bounds.x + bounds.width, bounds.y + bounds.height);
        path.lineTo(bounds.x, bounds.y + bounds.height);
        graphics.fillPath(path);
        path.dispose();
        
        graphics.setForegroundColor(getLineColor());
        graphics.setAlpha(getLineAlpha());
        graphics.drawLine(bounds.x, bounds.y + bounds.height, bounds.x + bounds.width, bounds.y + bounds.height);
        graphics.drawLine(bounds.x, bounds.y + bounds.height, bounds.x + INDENT + 1, bounds.y + bounds.height - height_indent);
        graphics.drawLine(bounds.x + bounds.width, bounds.y + bounds.height, bounds.x + bounds.width - INDENT + 1, bounds.y + bounds.height - height_indent);

        // Top part
        Rectangle rect = new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height - height_indent);

        graphics.setBackgroundColor(getFillColor());
        graphics.setAlpha(getAlpha());

        Pattern gradient = applyGradientPattern(graphics, bounds);
        
        graphics.fillRoundRectangle(rect, 30, 30);
        
        disposeGradientPattern(graphics, gradient);

        graphics.setForegroundColor(getLineColor());
        graphics.setAlpha(getLineAlpha());
        rect = new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height - height_indent);
        graphics.drawRoundRectangle(rect, 30, 30);
        
        // Image icon
        Rectangle imageArea = new Rectangle(bounds.x + 3, bounds.y + 3, bounds.width - 6, bounds.height - height_indent - 6);
        drawIconImage(graphics, bounds, imageArea, 0, 0, 0, 0);
        
        graphics.popState();
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
        graphics.setForegroundColor(isEnabled() ? ColorConstants.black : ColorConstants.gray);
        
        Point pt = getIconOrigin();
        
        graphics.drawRoundRectangle(new Rectangle(pt.x, pt.y, 11, 8), 3, 3);
        
        graphics.drawPolygon(new int[] {
                pt.x - 1, pt.y + 12,
                pt.x + 2, pt.y + 8,
                pt.x + 9, pt.y + 8,
                pt.x + 12, pt.y + 12
        });
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 31, bounds.y + 20);
    }
    
    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 1 ? fFigureDelegate : null;
    }
    
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 1 ? 20 : 0;
    }
}