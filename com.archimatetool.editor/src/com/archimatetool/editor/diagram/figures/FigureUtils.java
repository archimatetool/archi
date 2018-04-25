/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ScalableFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.widgets.Display;


/**
 * Utils for Figures
 * 
 * @author Phillip Beauvoir
 */
public class FigureUtils {

    /**
     * @param figure
     * @return The Current Zoom drawing scale for a Figure
     */
    public static double getFigureScale(IFigure figure) {
        if(figure instanceof ScalableFigure) {
            return ((ScalableFigure)figure).getScale();
        }
        
        return figure == null ? 1.0 : getFigureScale(figure.getParent());
    }

    /**
     * Gradient Direction
     */
    public static enum Direction {
        TOP, LEFT, RIGHT, BOTTOM
    }

    /**
     * Create a Pattern class with consideration to the scale of the Graphics class
     * Adapted from https://www.eclipse.org/forums/index.php?t=msg&th=198946&goto=894610&#msg_894610
     */
    public static Pattern createGradient(Graphics graphics, Device device, float x1, float y1, float x2, float y2, Color color1,
            int alpha1, Color color2, int alpha2) {
        
        double scale = graphics.getAbsoluteScale();
        
        return new Pattern(device, (int)(x1 * scale), (int)(y1 * scale), (int)(x2 * scale), (int)(y2 * scale), color1, alpha1, color2,
                alpha2);
    }

    /**
     * Create a Pattern class with consideration to the scale of the Graphics class
     * Adapted from https://www.eclipse.org/forums/index.php?t=msg&th=198946&goto=894610&#msg_894610
     */
    public static Pattern createGradient(Graphics graphics, Device device, float x1, float y1, float x2, float y2, Color color1,
            Color color2) {
        
        double scale = graphics.getAbsoluteScale();
        
        return new Pattern(device, (int)(x1 * scale), (int)(y1 * scale), (int)(x2 * scale), (int)(y2 * scale), color1, color2);
    }
    
    /**
     * Create a Pattern class with consideration to the scale of the Graphics class using the default gradient direction and default gradient end color
     */
    public static Pattern createGradient(Graphics graphics, Rectangle r, Color color) {
        return createGradient(graphics, r, color, 255);
    }

    /**
     * Create a Pattern class with consideration to the scale of the Graphics class using the default gradient direction and default gradient end color and alpha transparency
     */
    public static Pattern createGradient(Graphics graphics, Rectangle r, Color color, int alpha) {
        Direction direction = Direction.TOP;
        Color endColor = ColorConstants.white;
        
        // Gradienting all the way to pure white is too much, this extends the gradient area to cover that
        float deltaFactor = 0.15f;

        switch(direction) {
            case TOP:
            default:
                int delta = (int) (r.height * deltaFactor); 
                return createGradient(graphics, Display.getDefault(), r.x, r.y, r.x, r.getBottom().y + delta, color, alpha, endColor, alpha);
            
            case LEFT:
                delta = (int) (r.width * deltaFactor); 
                return createGradient(graphics, Display.getDefault(), r.x, r.y, r.getRight().x + delta, r.y, color, alpha, endColor, alpha);
            
            case RIGHT:
                delta = (int) (r.width * deltaFactor); 
                return createGradient(graphics, Display.getDefault(), r.getRight().x, r.y, r.x - delta, r.y, color, alpha, endColor, alpha);
            
            case BOTTOM:
                delta = (int) (r.height * deltaFactor); 
                return createGradient(graphics, Display.getDefault(), r.x, r.getBottom().y, r.x, r.y - delta, color, alpha, endColor, alpha);
        }
    }
}
