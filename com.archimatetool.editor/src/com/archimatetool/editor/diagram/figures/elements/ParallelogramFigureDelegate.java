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


/**
 * Parallelogram Figure Delegate
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
public class ParallelogramFigureDelegate extends AbstractFigureDelegate {

    protected static final int FLANGE = 16;
    protected static final int TEXT_INDENT = 20;
    
    protected boolean withSlash;
    
    public ParallelogramFigureDelegate(AbstractDiagramModelObjectFigure owner, boolean withSlash) {
        super(owner);
        this.withSlash = withSlash;
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();

        Rectangle rect = getBounds();
        
        Path path = createPath(rect);

        // Fill
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        graphics.fillPath(path);
        disposeGradientPattern(graphics, gradient);

        // Icon
        getOwner().drawIconImage(graphics, getBounds());

        // Outline
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        FigureUtils.drawPath(graphics, path, getLineWidth());
        path.dispose();
        
        if(withSlash) {
            graphics.setLineWidth(getLineWidth());
            path = new Path(null);
            path.moveTo(rect.x + FLANGE + 20, rect.y);
            path.lineTo(rect.x + 20, rect.y + rect.height);
            graphics.drawPath(path);
            path.dispose();
        }

        path.dispose();
        
        graphics.popState();
    }
    
    private Path createPath(Rectangle rect) {
        Path path = new Path(null);
        path.moveTo(rect.x + FLANGE, rect.y);
        path.lineTo(rect.x + rect.width, rect.y);
        path.lineTo(rect.x + rect.width - FLANGE, rect.y + rect.height);
        path.lineTo(rect.x, rect.y + rect.height);
        path.close();
        return path;
    }
}
