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
import com.archimatetool.editor.diagram.figures.FigureUtils;
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
    protected void drawFigure(Graphics graphics) {
        if(getFigureDelegate() != null) {
            getFigureDelegate().drawFigure(graphics);
            return;
        }

        graphics.pushState();
        
        Rectangle rect = getBounds().getCopy();
        
        Path path = createFigurePath(rect);
        
        // Fill
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        graphics.fillPath(path);
        disposeGradientPattern(graphics, gradient);

        // Image Icon
        Rectangle imageArea = new Rectangle(rect.x + INSET / 2, rect.y + INSET / 2, rect.width - INSET, rect.height - INSET);
        drawIconImage(graphics, getBounds().getCopy(), imageArea);

        // Line
        if(getLineStyle() != IDiagramModelObject.LINE_STYLE_NONE) {
            setLineStyle(graphics);
            graphics.setAlpha(getLineAlpha());
            graphics.setForegroundColor(getLineColor());
            FigureUtils.drawPath(graphics, path, getLineWidth());
        }
        
        path.dispose();

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