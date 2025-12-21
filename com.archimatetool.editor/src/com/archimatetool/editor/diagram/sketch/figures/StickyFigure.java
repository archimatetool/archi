/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.sketch.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ISketchModelSticky;


/**
 * Sticky Figure
 * 
 * @author Phillip Beauvoir
 */
public class StickyFigure extends AbstractTextControlContainerFigure {
    
    public StickyFigure(ISketchModelSticky diagramModelSticky) {
        super(diagramModelSticky, TEXT_FLOW_CONTROL);
    }
    
    @Override
    public void setText() {
        String text = getDiagramModelObject().getContent();
        ((TextFlow)getTextControl()).setText(StringUtils.safeString(text));
    }

    @Override
    public ISketchModelSticky getDiagramModelObject() {
        return (ISketchModelSticky)super.getDiagramModelObject();
    }

    @Override
    protected void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        graphics.setAlpha(getAlpha());
        
        Rectangle rect = getBounds().getCopy();
        
        // Reduce width and height by 1 pixel
        rect.resize(-1, -1);
        
        boolean drawOutline = getLineStyle() != IDiagramModelObject.LINE_STYLE_NONE;
        
        if(drawOutline) {
            // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
            setLineWidth(graphics, rect);
            setLineStyle(graphics);
        }

        graphics.setBackgroundColor(getFillColor());

        Pattern gradient = applyGradientPattern(graphics, rect);
        
        graphics.fillRectangle(rect.x, rect.y, rect.width, rect.height);
        
        disposeGradientPattern(graphics, gradient);

        // Icon
        drawIconImage(graphics, rect);

        // Outline
        if(drawOutline) {
            graphics.setAlpha(getLineAlpha());
            graphics.setForegroundColor(getLineColor());
            graphics.drawRectangle(rect.x, rect.y, rect.width, rect.height);
        }
        
        graphics.popState();
    }
    
    @Override
    protected int getTextControlMarginWidth() {
        return 10;
    }
}
