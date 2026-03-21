/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.model.IDiagramModelObject;



/**
 * Ellipse Figure Delegate
 * 
 * @author Phillip Beauvoir
 */
public class EllipseFigureDelegate extends AbstractFigureDelegate {
    
    public EllipseFigureDelegate(AbstractDiagramModelObjectFigure owner) {
        super(owner);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle rect = getBounds();

        boolean drawOutline = getLineStyle() != IDiagramModelObject.LINE_STYLE_NONE;
        
        // Fill
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        //FigureUtils.fillOvalPath(graphics, rect, drawOutline);
        graphics.fillOval(rect);
        disposeGradientPattern(graphics, gradient);
        
        // Outline
        if(drawOutline) {
            setLineStyle(graphics);
            graphics.setAlpha(getLineAlpha());
            graphics.setForegroundColor(getLineColor());
            FigureUtils.drawOvalPath(graphics, rect, getLineWidth());
        }
        
        // Image Icon
        Rectangle imageArea = new Rectangle(rect.x + (rect.width / 6), rect.y + (rect.height / 6),
                rect.width - (rect.width / 3), rect.height - (rect.height / 3));
        getOwner().drawIconImage(graphics, getBounds().getCopy(), imageArea);

        graphics.popState();
    }

}
