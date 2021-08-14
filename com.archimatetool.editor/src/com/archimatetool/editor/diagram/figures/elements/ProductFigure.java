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
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.model.IIconic;
import com.archimatetool.model.ITextPosition;




/**
 * Product Figure
 * 
 * @author Phillip Beauvoir
 */
public class ProductFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    protected static final int TOP_MARGIN = 12;

    public ProductFigure() {
        super(TEXT_FLOW_CONTROL);
        
        // Use a Rectangle Figure Delegate to Draw
        RectangleFigureDelegate figureDelegate = new RectangleFigureDelegate(this) {
            @Override
            public void drawFigure(Graphics graphics) {
                graphics.pushState();
                
                Rectangle bounds = getBounds();
                
                bounds.width--;
                bounds.height--;

                // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
                setLineWidth(graphics, 1, bounds);

                graphics.setAlpha(getAlpha());
                
                if(!isEnabled()) {
                    setDisabledState(graphics);
                }
                
                // Fill
                graphics.setBackgroundColor(getFillColor());
                
                Pattern gradient = applyGradientPattern(graphics, bounds);

                graphics.fillRectangle(bounds);
                
                disposeGradientPattern(graphics, gradient);
                
                // Outline
                graphics.setForegroundColor(getLineColor());
                graphics.setAlpha(getLineAlpha());

                graphics.drawRectangle(bounds);
                
                Path path = new Path(null);
                path.moveTo(bounds.x, bounds.y + TOP_MARGIN);
                path.lineTo(bounds.getCenter().x, bounds.y + TOP_MARGIN);
                path.lineTo(bounds.getCenter().x, bounds.y);
                graphics.drawPath(path);
                path.dispose();
                
                // Icon
                // getOwner().drawIconImage(graphics, bounds);
                int topOffset = ((IIconic)getDiagramModelObject()).getImagePosition() == IIconic.ICON_POSITION_TOP_LEFT ? TOP_MARGIN : 0;
                drawIconImage(graphics, bounds, topOffset, 0, 0, 0);

                graphics.popState();
            }

            @Override
            public Rectangle calculateTextControlBounds() {
                Rectangle bounds = getBounds();
                
                int textPosition = ((ITextPosition)getDiagramModelObject()).getTextPosition();
                
                if(textPosition == ITextPosition.TEXT_POSITION_TOP) {
                    bounds.y += TOP_MARGIN - getTextControlMarginHeight() - 1;
                }
                
                return bounds;
            }
        };
        
        setFigureDelegate(figureDelegate);
    }
}
