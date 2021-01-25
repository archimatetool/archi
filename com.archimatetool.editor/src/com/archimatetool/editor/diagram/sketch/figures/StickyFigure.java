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
        
        Rectangle bounds = getBounds().getCopy();
        
        bounds.width--;
        bounds.height--;
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, 1, bounds);

        graphics.setBackgroundColor(getFillColor());

        Pattern gradient = applyGradientPattern(graphics, bounds);
        
        graphics.fillRectangle(bounds.x, bounds.y, bounds.width, bounds.height);
        
        disposeGradientPattern(graphics, gradient);

        // Icon
        drawIconImage(graphics, bounds);

        // Outline
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.drawRectangle(bounds.x, bounds.y, bounds.width, bounds.height);
        
        graphics.popState();
    }
    
    @Override
    protected int getTextControlMarginWidth() {
        return 10;
    }
}
