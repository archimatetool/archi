/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.model.IDiagramModelObject;




/**
 * Rounded Rectangle Figure Delegate
 * 
 * @author Phillip Beauvoir
 */
public class RoundedRectangleFigureDelegate extends AbstractFigureDelegate
implements IRoundedRectangleFigure {

    private Dimension arc = new Dimension(20, 20);
    
    public RoundedRectangleFigureDelegate(AbstractDiagramModelObjectFigure owner) {
        super(owner);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle rect = getBounds();
        
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        //FigureUtils.fillRoundRectanglePath(graphics, rect, arc.width, arc.height);
        graphics.fillRoundRectangle(rect, arc.width, arc.height);
        disposeGradientPattern(graphics, gradient);
        
        // Image Icon
        //Rectangle imageArea = new Rectangle(rect.x + 2, rect.y + 2, rect.width - 4, rect.height - 4);
        //getOwner().drawIconImage(graphics, getBounds().getCopy(), imageArea);
        getOwner().drawIconImage(graphics, getBounds().getCopy());
        
        // Outline
        if(getLineStyle() != IDiagramModelObject.LINE_STYLE_NONE) {
            setLineStyle(graphics);
            graphics.setAlpha(getLineAlpha());
            graphics.setForegroundColor(getLineColor());
            FigureUtils.drawRoundRectanglePath(graphics, rect, arc.width, arc.height, getLineWidth());
        }
        
        graphics.popState();
    }
    
    @Override
    public void setArc(Dimension arc) {
        this.arc = arc.getCopy();
    }
    
    @Override
    public Dimension getArc() {
        return arc.getCopy();
    }
}
