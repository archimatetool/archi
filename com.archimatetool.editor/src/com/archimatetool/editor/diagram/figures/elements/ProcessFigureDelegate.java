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

import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.figures.AbstractFigureDelegate;
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.model.ITextPosition;




/**
 * Process Figure Delegate
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
public class ProcessFigureDelegate extends AbstractFigureDelegate {

    public ProcessFigureDelegate(AbstractDiagramModelObjectFigure owner) {
        super(owner);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle rect = getBounds();
        
        Path path = createPath(rect);

        // Fill
        graphics.setBackgroundColor(getFillColor());
        graphics.setAlpha(getAlpha());
        Pattern gradient = applyGradientPattern(graphics, rect);
        graphics.fillPath(path);
        disposeGradientPattern(graphics, gradient);
        
        // Icon
        getOwner().drawIconImage(graphics, getBounds(), rect.height / 5 + 1, (int)-(rect.width * 0.2f), -(rect.height / 5 + 1), 0);

        // Line
        graphics.setForegroundColor(getLineColor());
        graphics.setAlpha(getLineAlpha());
        FigureUtils.drawPath(graphics, path, getLineWidth());
        
        path.dispose();
        
        graphics.popState();
    }
    
    private Path createPath(Rectangle rect) {
        float x1 = rect.x + (rect.width * 0.7f);
        float y1 = rect.y + (rect.height / 5);
        float y2 = rect.y + rect.height - (rect.height / 5);
        
        Path path = new Path(null);
        path.moveTo(rect.x, y1);
        path.lineTo(x1, y1);
        path.lineTo(x1, rect.y);
        path.lineTo(rect.x + rect.width, rect.y + (rect.height / 2));
        path.lineTo(x1, rect.y + rect.height);
        path.lineTo(x1, y2);
        path.lineTo(rect.x, y2);
        path.close();
        return path;
    }
    
    @Override
    public Rectangle calculateTextControlBounds() {
        Rectangle rect = getBounds();
        
        if(((ITextPosition)getOwner().getDiagramModelObject()).getTextPosition() == ITextPosition.TEXT_POSITION_TOP) {
            rect.y += rect.height / 5;
        }
        
        return rect;
    }

}
