/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;

import com.archimatetool.editor.diagram.editparts.RoundedRectangleAnchor;
import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RoundedRectangleFigureDelegate;
import com.archimatetool.editor.ui.IIconDelegate;




/**
 * Work Package Figure
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
public class WorkPackageFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private RoundedRectangleFigureDelegate figureDelegate;

    public WorkPackageFigure() {
        super(TEXT_FLOW_CONTROL);
        
        // Use a Rounded Rectangle Figure Delegate to Draw
        figureDelegate = new RoundedRectangleFigureDelegate(this);
    }
    
    @Override
    public ConnectionAnchor getDefaultConnectionAnchor() {
        return getDiagramModelArchimateObject().getType() == 0 ? new RoundedRectangleAnchor(this) : super.getDefaultConnectionAnchor();
    }
    
    @Override
    protected void drawFigure(Graphics graphics) {
        if(getFigureDelegate() != null) {
            getFigureDelegate().drawFigure(graphics);
            drawIcon(graphics);
            return;
        }
        
        graphics.pushState();
        
        Rectangle rect = getBounds().getCopy();
        
        Rectangle imageBounds = rect.getCopy();
        
        setFigurePositionFromTextPosition(rect, 8/7.0);
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        graphics.setBackgroundColor(getLineColor());
        
        /*
         * WorkPackage icon is drawn inside a 8x7 grid:
         *  -------------------------
         *  :  :  ://:\\:  :  :  :  :
         *  -------------------------
         *  :  ://:  :  :\\:  :  :  :
         *  -------------------------
         *  :(|:  :  :  :  :|):  :  :
         *  -------------------------
         *  :(|:  :  :  :  :|):  :  :
         *  -------------------------
         *  :  :\\:  :  :  :  :|\:  :
         *  -------------------------
         *  :  :  :\\:==:==:==:+=:=>:
         *  -------------------------
         *  :  :  :  :  :  :  :|/:  :
         *  -------------------------
         */
   
        float gridUnit = (float)(rect.width / 8.0);
        int lineWidth = (int) gridUnit;
        graphics.setLineWidth(lineWidth);
        // Use rounded ends when drawing
        graphics.setLineCap(SWT.CAP_ROUND);
        
        Path path = new Path(null);
        
        // Semi-circle is drawn inside a 6x6 square but because of line width radius is only 2,5
        float radius = (int) (2.5f * gridUnit);
        path.addArc(rect.x + gridUnit / 2,
                rect.y + gridUnit / 2,
                radius * 2,
                radius * 2,
                -25,
                295);	// this added to previous number must be equal to 270
        
        // Save current position
        float[] currentPoint = new float[2];
        path.getCurrentPoint(currentPoint);
        
        // Line
        path.lineTo(currentPoint[0] + 3 * gridUnit, currentPoint[1]);
        
        // Draw semi-circle and line segment
        graphics.drawPath(path);
        
        // Update current position before disposing the path
        path.getCurrentPoint(currentPoint);
        path.dispose();
        
        // Triangle
        path = new Path(null);
        path.moveTo(currentPoint[0], currentPoint[1]);
        path.lineTo(currentPoint[0], currentPoint[1] - 1.5f * gridUnit);
        path.lineTo(currentPoint[0] + 2 * gridUnit, currentPoint[1]);
        path.lineTo(currentPoint[0], currentPoint[1] + 1.5f * gridUnit);
        
        // Draw triangle
        path.close();
        graphics.fillPath(path);
        path.dispose();

        // Image Icon
        drawIconImage(graphics, imageBounds, 0, 0, 0, 0);
        
        graphics.popState();
    }
    
    /**
     * Draw the icon
     */
    private void drawIcon(Graphics graphics) {
        if(isIconVisible()) {
            getIconDelegate().drawIcon(graphics, getIconColor(), null, getIconOrigin());
        }
    }
    
    private static IIconDelegate iconDelegate = new IIconDelegate() {
        @Override
        public void drawIcon(Graphics graphics, Color foregroundColor, Color backgroundColor, Point pt) {
            graphics.pushState();
            
            // Ensure this is set
            graphics.setAntialias(SWT.ON);

            graphics.setLineWidth(1);
            
            if(foregroundColor != null) {
                graphics.setForegroundColor(foregroundColor);
            }
            
            float circleWidth = 9;
            float circleHalf = circleWidth / 2;
            
            // If the circle part should be filled, set this to true
            boolean doFill = false;
            if(doFill && backgroundColor != null) {
                graphics.setBackgroundColor(backgroundColor);
                Path path = new Path(null);
                path.addArc(pt.x, pt.y, circleWidth, circleWidth, 0, 360);
                graphics.fillPath(path);
                path.dispose();
            }
            
            Path path = new Path(null);
            
            // Circle
            path.addArc(pt.x, pt.y, circleWidth, circleWidth, 340, 295);
            
            // Line
            path.moveTo(pt.x + circleHalf, pt.y + circleWidth);
            path.lineTo(pt.x + 11, pt.y + circleWidth);
            
            graphics.drawPath(path);
            path.dispose();
            
            // Triangle
            path = new Path(null);
            
            path.moveTo(pt.x + 11, pt.y + circleWidth - 3);
            path.lineTo(pt.x + 15, pt.y + circleWidth);
            path.lineTo(pt.x + 11, pt.y + circleWidth + 3);
            
            path.close();
            
            if(foregroundColor != null) {
                graphics.setBackgroundColor(foregroundColor);
            }
            graphics.fillPath(path);
            path.dispose();
            
            graphics.popState();
        }
    };
    
    public static IIconDelegate getIconDelegate() {
        return iconDelegate;
    }

    /**
     * @return The icon start position
     */
    private Point getIconOrigin() {
        Rectangle rect = getBounds();
        return new Point(rect.x + rect.width - 17 - getLineWidth(), rect.y + 6);
    }
    
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 19 : 0;
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? figureDelegate : null;
    }
}
