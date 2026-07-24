/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.model.IDiagramModelObject;


/**
 * Figure for a Motiviation Element
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
public abstract class AbstractMotivationFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    protected static final int INSET = 10;
    
    protected AbstractMotivationFigure() {
        super(TEXT_FLOW_CONTROL);
    }

    @Override
    protected boolean supportsOutlineShapeStyle() {
        return true;
    }

    @Override
    protected void drawFigure(Graphics graphics) {
        if(getFigureDelegate() != null) {
            getFigureDelegate().drawFigure(graphics);
            return;
        }

        graphics.pushState();
        
        final boolean drawOutline = getLineStyle() != IDiagramModelObject.LINE_STYLE_NONE;
        
        // Have to apply the offset for the fill also so it lines up with the outline
        Rectangle rect = drawOutline ? applyLineWidthOffset(graphics) : getBounds().getCopy();
        
        // Fill
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        Path path = createFigurePath(rect);
        graphics.fillPath(path);
        path.dispose();
        disposeGradientPattern(graphics, gradient);

        // Image Icon
        Rectangle imageArea = new Rectangle(rect.x + INSET / 2, rect.y + INSET / 2, rect.width - INSET, rect.height - INSET);
        drawIconImage(graphics, getBounds().getCopy(), imageArea);

        // Line
        if(drawOutline) {
            setLineStyle(graphics);
            graphics.setAlpha(getLineAlpha());
            graphics.setLineWidth(getLineWidth());
            graphics.setForegroundColor(getLineColor());
            path = createFigurePath(rect);
            graphics.drawPath(path);
            path.dispose();
        }

        graphics.popState();
    }
    
    private Path createFigurePath(Rectangle rect) {
        Path path = new Path(null);
        path.moveTo(rect.x + INSET, rect.y);
        path.lineTo(rect.x + rect.width - INSET, rect.y);
        path.lineTo(rect.x + rect.width, rect.y + INSET);
        path.lineTo(rect.x + rect.width, rect.y + rect.height - INSET);
        path.lineTo(rect.x + rect.width - INSET, rect.y + rect.height);
        path.lineTo(rect.x + INSET, rect.y + rect.height);
        path.lineTo(rect.x, rect.y + rect.height - INSET);
        path.lineTo(rect.x, rect.y + INSET);
        path.close();
        return path;
    }
}