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
 * Cylinder Figure Delegate
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
public class CylinderFigureDelegate extends AbstractFigureDelegate {
    
    private final int OFFSET = 4;

    public CylinderFigureDelegate(AbstractDiagramModelObjectFigure owner) {
        super(owner);
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle rect = getBounds();
        
        // Fill
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        
        Path path = createPath(rect);
        graphics.fillPath(path);
        disposeGradientPattern(graphics, gradient);
        
        // Image Icon
        getOwner().drawIconImage(graphics, getBounds());
        
        // Lines
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.setLineWidth(getLineWidth());
        
        FigureUtils.drawPath(graphics, path, getLineWidth());
        path.dispose();
        
        Path path2 = new Path(null);
        path2.addArc((rect.x + rect.width) - (rect.width / OFFSET), rect.y, rect.width / OFFSET, rect.height, 90, 180);
        FigureUtils.drawPath(graphics, path2, getLineWidth());
        path2.dispose();
        
        graphics.popState();
    }
    
    private Path createPath(Rectangle rect) {
        Path path = new Path(null);
        path.addArc(rect.x, rect.y, rect.width / OFFSET, rect.height, 90, 180);
        path.addArc((rect.x + rect.width) - (rect.width / OFFSET), rect.y, rect.width / OFFSET, rect.height, 270, 180);
        path.close();
        return path;
    }
}
