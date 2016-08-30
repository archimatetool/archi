/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.widgets.Display;


/**
 * Gradient Utils for Figures
 * 
 * @author Phillip Beauvoir
 */
public class GradientUtils {

    /**
     * Gradient Direction
     */
    public static enum Direction {
        LEFT, RIGHT, TOP, BOTTOM
    }

    /**
     * Create a Pattern class with consideration to the scale of the Graphics class
     * Adapted from https://www.eclipse.org/forums/index.php?t=msg&th=198946&goto=894610&#msg_894610
     */
    public static Pattern createScaledPattern(Graphics graphics, Device device, float x1, float y1, float x2, float y2, Color color1,
            int alpha1, Color color2, int alpha2) {
        
        double scale = graphics.getAbsoluteScale();
        
        return new Pattern(device, (int)(x1 * scale), (int)(y1 * scale), (int)(x2 * scale), (int)(y2 * scale), color1, alpha1, color2,
                alpha2);
    }

    /**
     * Create a Pattern class with consideration to the scale of the Graphics class
     * Adapted from https://www.eclipse.org/forums/index.php?t=msg&th=198946&goto=894610&#msg_894610
     */
    public static Pattern createScaledPattern(Graphics graphics, Device device, float x1, float y1, float x2, float y2, Color color1,
            Color color2) {
        
        double scale = graphics.getAbsoluteScale();
        
        return new Pattern(device, (int)(x1 * scale), (int)(y1 * scale), (int)(x2 * scale), (int)(y2 * scale), color1, color2);
    }
    
    /**
     * Create a Pattern class with consideration to the scale of the Graphics class using the default gradient direction and default gradient end color
     */
    public static Pattern createScaledPattern(Graphics graphics, Rectangle r, Color color) {
        Direction direction = Direction.TOP;
        Color endColor = ColorConstants.white;

        switch(direction) {
            case LEFT:
                return createScaledPattern(graphics, Display.getDefault(), r.x, r.y, r.getRight().x, r.y, color, endColor);
            case RIGHT:
                return createScaledPattern(graphics, Display.getDefault(), r.getRight().x, r.y, r.x, r.y, color, endColor);
            default:
            case TOP:
                return createScaledPattern(graphics, Display.getDefault(), r.x, r.y, r.x, r.getBottomRight().y, color, endColor);
            case BOTTOM:
                return createScaledPattern(graphics, Display.getDefault(), r.x, r.getBottomRight().y, r.x, r.y, color, endColor);
        }
    }

}
