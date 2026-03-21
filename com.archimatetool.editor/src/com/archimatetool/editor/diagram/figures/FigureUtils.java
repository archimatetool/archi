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
import org.eclipse.swt.widgets.Display;


/**
 * Utils for Figures
 * 
 * @author Phillip Beauvoir
 */
public class FigureUtils {

    /**
     * Get the current scale from the ancestor ScalableFigure.
     * This will be when we have zoomed in/out in a GEF diagram
     * @return The Current GEF Zoom scale for a figure's ancestor ScalableFigure or 1.0 if no parent
     */
    public static double getFigureScale(IFigure figure) {
        if(figure instanceof ScalableFigure scalableFigure) {
            return scalableFigure.getScale();
        }
        
        return figure == null ? 1.0 : getFigureScale(figure.getParent());
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
     * Draw a path by setting line width x2 and setClip to path
     */
    public static void drawPath(Graphics graphics, Path path, int lineWidth) {
        graphics.pushState();
        graphics.setLineWidth(lineWidth * 2);
        graphics.setClip(path);
        graphics.drawPath(path);
        graphics.popState();
    }
    
    /**
     * Draw a rectangle using best option.
     * If lineWidth is 1,3,5 use a Path else use simple drawRectangle
     */
    public static void drawRectangle(Graphics graphics, Rectangle rect, int lineWidth) {
        if((lineWidth & 1) == 0) {
            graphics.setLineWidth(lineWidth);
            graphics.drawRectangle(rect.getCopy().shrink(lineWidth / 2, lineWidth / 2));
        }
        else {
            graphics.pushState();
            graphics.setLineWidth(lineWidth * 2);
            // Use a Path. Don't use graphics.setClip(rect) and graphics.drawRectangle(rect) because it doesn't work on SVG export
            Path path = new Path(null);
            path.addRectangle(rect.x, rect.y, rect.width, rect.height);
            graphics.setClip(path);
            graphics.drawPath(path);
            path.dispose();
            graphics.popState();
        }
    }

    /**
     * Draw a round rectangle using a Path
     */
    public static void drawRoundRectanglePath(Graphics graphics, Rectangle rect, int arcWidth, int arcHeight, int lineWidth) {
        Path path = createRoundRectanglePath(rect, arcWidth, arcHeight);
        graphics.pushState();
        graphics.setLineWidth(lineWidth * 2);
        graphics.setClip(path);
        graphics.drawPath(path);
        path.dispose();
        graphics.popState();
    }
    
    /**
     * Fill a round rectangle using a Path
     */
    public static void fillRoundRectanglePath(Graphics graphics, Rectangle rect, int arcWidth, int arcHeight) {
        Path path = createRoundRectanglePath(rect, arcWidth, arcHeight);
        graphics.fillPath(path);
        path.dispose();
    }
    
    private static Path createRoundRectanglePath(Rectangle rect, int arcWidth, int arcHeight) {
        float rx = arcWidth / 2f;
        float ry = arcHeight / 2f;
        
        int x = rect.x;
        int y = rect.y;
        int width = rect.width;
        int height = rect.height;
        
        Path path = new Path(null);
        
        // Start at top side, after top-left corner arc
        path.moveTo(x + rx, y);
        
        // Top line
        path.lineTo(x + width - rx, y);
        
        // Top-right arc (quarter circle)
        path.addArc(x + width - arcWidth, y, arcWidth, arcHeight, 90, -90);
        
        // Right line
        path.lineTo(x + width, y + height - ry);
        
        // Bottom-right arc
        path.addArc(x + width - arcWidth, y + height - arcHeight, arcWidth, arcHeight, 0, -90);
        
        // Bottom line
        path.lineTo(x + rx, y + height);
        
        // Bottom-left arc
        path.addArc(x, y + height - arcHeight, arcWidth, arcHeight, 270, -90);
        
        // Left line
        path.lineTo(x, y + ry);
        
        // Top-left arc — closes the path
        path.addArc(x, y, arcWidth, arcHeight, 180, -90);
        
        return path;
    }

    /**
     * Draw an oval using a Path
     */
    public static void drawOvalPath(Graphics graphics, Rectangle rect, int lineWidth) {
        drawOvalPath(graphics, rect.x, rect.y, rect.width, rect.height, lineWidth);
    }

    /**
     * Draw an oval using a Path
     */
    public static void drawOvalPath(Graphics graphics, float x, float y, float width, float height, int lineWidth) {
        Path path = createOvalPath(x, y, width, height);
        graphics.pushState();
        graphics.setLineWidth(lineWidth * 2);
        graphics.setClip(path);
        graphics.drawPath(path);
        path.dispose();
        graphics.popState();
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
