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
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.internal.DPIUtil;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.utils.PlatformUtils;


/**
 * Utils for Figures
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class FigureUtils {

    /**
     * Get the current scale from the Figure's ancestor Figure
     * This will be the zoom factor in a GEF diagram or the scale set in Graphics.scale(scale) in DiagramUtils
     */
    public static double getFigureScale(IFigure figure) {
        while(figure != null) {
            if(figure instanceof ScalableFigure scalableFigure) {
                return scalableFigure.getScale();
            }
            figure = figure.getParent();
        }
        
        return 1.0;
    }
    
    /**
     * Whether Draw2d scaling is enabled on Windows
     */
    public static boolean isAutoScaleEnabled() {
        return PlatformUtils.isWindows() && Boolean.parseBoolean(System.getProperty("draw2d.enableAutoscale", Boolean.TRUE.toString()));
    }

    /**
     * @return the display scaling if on Windows and draw2d enableAutoscale is true, else return 1
     */
    @SuppressWarnings("restriction")
    public static float getDisplayScale() {
        return isAutoScaleEnabled() ? DPIUtil.getDeviceZoom() / 100f : 1f;
    }

    /**
     * Gradient Direction
     */
    public static enum Direction {
        TOP, LEFT, RIGHT, BOTTOM;
        
        public static Direction get(int value) {
            switch(value) {
                default:
                case 0: return TOP;
                case 1: return LEFT;
                case 2: return RIGHT;
                case 3: return BOTTOM;
            }
        }
    }

    /**
     * Create a Gradient Pattern
     */
    public static Pattern createGradient(Graphics graphics, Device device, float x1, float y1, float x2, float y2, Color color1, int alpha1, Color color2, int alpha2) {
        return new Pattern(device, x1, y1, x2, y2, color1, alpha1, color2, alpha2);
    }

    /**
     * Create a Gradient Pattern
     */
    public static Pattern createGradient(Graphics graphics, Device device, float x1, float y1, float x2, float y2, Color color1, Color color2) {
        return new Pattern(device, x1, y1, x2, y2, color1, color2);
    }
    
    /**
     * Create a Gradient Pattern
     */
    public static Pattern createGradient(Graphics graphics, Rectangle r, Color color, Direction direction) {
        return createGradient(graphics, r, color, 255, direction);
    }

    /**
     * Create a Gradient Pattern using the given gradient direction and default gradient end color and alpha transparency
     */
    public static Pattern createGradient(Graphics graphics, Rectangle r, Color color, int alpha, Direction direction) {
        if(direction == null) {
            return null;
        }
        
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

    /**
     * Create a Path from a points list
     * @param points The points list
     * @return The Path - callers should dispose of it
     */
    public static Path createPathFromPoints(PointList points) {
        return createPathFromPoints(points.toIntArray());
    }
    
    /**
     * Create a Path from a points list
     * @param points The points as x,y
     * @return The Path - callers should dispose of it
     */
    public static Path createPathFromPoints(int[] points) {
        Path path = new Path(null);
        
        path.moveTo(points[0], points[1]);
        
        for(int i = 2; i < points.length; i += 2) {
            path.lineTo(points[i], points[i + 1]);
        }
        
        path.close();
        
        return path;
    }
    
    /**
     * Draw an oval using a Path
     */
    public static void drawOvalPath(Graphics graphics, Rectangle rect) {
        drawOvalPath(graphics, rect.x, rect.y, rect.width, rect.height);
    }

    /**
     * Draw an oval using a Path
     */
    public static void drawOvalPath(Graphics graphics, float x, float y, float width, float height) {
        Path path = createOvalPath(x, y, width, height);
        graphics.drawPath(path);
        path.dispose();
    }
    
    /**
     * Fill an oval using a Path
     */
    public static void fillOvalPath(Graphics graphics, Rectangle rect) {
        fillOvalPath(graphics, rect.x, rect.y, rect.width, rect.height);
    }

    /**
     * Fill an oval using a Path
     */
    public static void fillOvalPath(Graphics graphics, float x, float y, float width, float height) {
        Path path = createOvalPath(x, y, width, height);
        graphics.fillPath(path);
        path.dispose();
    }
    
    private static Path createOvalPath(float x, float y, float width, float height) {
        Path path = new Path(null);
        path.addArc(x, y, width, height, 0, 360);
        return path;
    }
}
