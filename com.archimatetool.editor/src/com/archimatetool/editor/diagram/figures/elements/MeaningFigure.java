/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;




/**
 * Meaning Figure
 * 
 * @author Phillip Beauvoir
 */
public class MeaningFigure extends AbstractMotivationFigure {
    
    public MeaningFigure() {
    }
    
    @Override
    public void drawFigure(Graphics graphics) {
        if(getDiagramModelArchimateObject().getType() == 0) {
            super.drawFigure(graphics);
            drawIcon(graphics);
            return;
        }
        
        graphics.pushState();
        
        Rectangle bounds = getBounds().getCopy();
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, 1, bounds);
        
        // The following is the most awful code to draw a cloud...
        
        graphics.setAlpha(getAlpha());
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        // Main fill
        graphics.setBackgroundColor(getFillColor());
        
        Pattern gradient = applyGradientPattern(graphics, bounds);
        
        Path path = new Path(null);
        path.addArc(bounds.x, bounds.y, bounds.width/3 * 2, bounds.height/3 * 2, 0, 360);
        graphics.fillPath(path);
        path.dispose();
        
        path = new Path(null);
        path.addArc(bounds.x + bounds.width/3 - 1, bounds.y, bounds.width/3 * 2, bounds.height/3 * 2, 0, 360);
        graphics.fillPath(path);
        path.dispose();
        
        path = new Path(null);
        path.addArc(bounds.x, bounds.y + bounds.height/3, bounds.width/5 * 3, bounds.height/3 * 2 - 1, 0, 360);
        graphics.fillPath(path);
        path.dispose();
        
        path = new Path(null);
        path.addArc(bounds.x + bounds.width/3, bounds.y + bounds.height/4, bounds.width/5 * 3, bounds.height/3 * 2, 0, 360);
        graphics.fillPath(path);
        path.dispose();
        
        disposeGradientPattern(graphics, gradient);
        
        // Outline
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        
        path = new Path(null);
        path.addArc(bounds.x, bounds.y, bounds.width/3 * 2, bounds.height/3 * 2, 60, 149);
        graphics.drawPath(path);
        path.dispose();
        
        path = new Path(null);
        path.addArc(bounds.x + bounds.width/3 - 1, bounds.y, bounds.width/3 * 2, bounds.height/3 * 2, -38, 157);
        graphics.drawPath(path);
        path.dispose();
        
        path = new Path(null);
        path.addArc(bounds.x, bounds.y + bounds.height / 3, bounds.width/5 * 3, bounds.height/3 * 2 - 1, -41, -171);
        graphics.drawPath(path);
        path.dispose();
        
        path = new Path(null);
        path.addArc(bounds.x + bounds.width/3, bounds.y + bounds.height/4, bounds.width/5 * 3, bounds.height/3 * 2, 7, -120);
        graphics.drawPath(path);
        path.dispose();
        
        // Icon
        drawIconImage(graphics, bounds);

        graphics.popState();
    }
    
    /**
     * Draw the icon
     */
    private void drawIcon(Graphics graphics) {
        if(!isIconVisible()) {
            return;
        }
        
        graphics.pushState();
        
        graphics.setLineWidth(1);
        graphics.setForegroundColor(getIconColor());
        
        // TODO - Draw icon...

        //Point pt = getIconOrigin();
        
        graphics.popState();
    }

    /**
     * @return The icon start position
     */
    private Point getIconOrigin() {
        Rectangle bounds = getBounds().getCopy();
        return new Point(bounds.x + bounds.width - 13, bounds.y + 4);
    }

    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 17 : 0;
    }

}
