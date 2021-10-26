/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.PolarPoint;


/**
 * Equipment Figure
 * 
 * @author Phillip Beauvoir
 */
public class EquipmentFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {
    
    private IFigureDelegate boxDelegate;
    
    public EquipmentFigure() {
        super(TEXT_FLOW_CONTROL);
        boxDelegate = new BoxFigureDelegate(this);
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
        rect.width--;
        rect.height--;
        
        Rectangle imageBounds = rect.getCopy();
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, 1, rect);
        
        setFigurePositionFromTextPosition(rect);
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }
        
        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        
        int xCenter = rect.x + rect.width / 2;
        int yCenter = rect.y + rect.height / 2;
        
        int width1 = Math.min(rect.width, rect.height) * 2 / 3;
        Rectangle rect1 = new Rectangle(xCenter - width1 * 2 / 3, (yCenter - width1 / 2) + (width1 / 4), width1, width1);
        
        Path path1 = getPathShape(rect1);
        graphics.fillPath(path1);
        
        int width2 = Math.min(rect.width, rect.height) * 1 / 2;
        Rectangle rect2 = new Rectangle(xCenter, (int)(yCenter - (width2 * 0.96f)), width2, width2);        
        
        Path path2 = getPathShape(rect2);
        graphics.fillPath(path2);
        
        disposeGradientPattern(graphics, gradient);
        
        // Lines
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());

        graphics.drawPath(path1);
        graphics.drawPath(path2);
        
        path1.dispose();
        path2.dispose();
        
        drawCircle(graphics, rect1);
        drawCircle(graphics, rect2);

        // Image Icon
        drawIconImage(graphics, imageBounds, 0, 0, 0, 0);
        
        graphics.popState();
    }
    
    private void drawCircle(Graphics graphics, Rectangle rect) {
        graphics.drawOval(rect.getCenter().x - (rect.width / 2) + (rect.width / 3),
                rect.getCenter().y - (rect.height / 2) + (rect.width / 3),
                rect.width / 3,
                rect.width / 3);
    }
    
    private Path getPathShape(Rectangle rect) {
        Path path = new Path(null);
        
        int figureWidth = rect.width;
        int figureHeight = rect.height;
        
        if(rect.width > rect.height) {
            figureWidth = rect.height;
            figureHeight = rect.height;
        }
        else {
            figureWidth = rect.width;
            figureHeight = rect.width;
        }
        
        PrecisionPoint center = new PrecisionPoint();
        center.setPreciseLocation((rect.x + figureWidth / 2), (rect.y + figureHeight / 2));
        
        int alphaFirstCircle = 14;
        int betaFirstCircle = 31;
        int alphaSecondCircle = 22;
        int betaSecondCirlce = 23;
        int xMargin = (rect.width - figureWidth) / 2;
        int yMargin = (rect.height - figureHeight) / 2;
        
        PrecisionPoint firstPoint = new PrecisionPoint();
        firstPoint.setPreciseLocation((rect.x + figureWidth / 2), rect.y);
        
        PrecisionPoint secondPoint = new PrecisionPoint();
        secondPoint.setPreciseLocation((rect.x + figureWidth / 2) - figureWidth / 5.9d, rect.y + figureHeight / 6.5d);
        
        PrecisionPoint firstCirclePoint = new PrecisionPoint();
        firstCirclePoint = rotatePoint(firstPoint, center, alphaFirstCircle);
        
        PrecisionPoint secondCirclePoint = new PrecisionPoint();
        secondCirclePoint = rotatePoint(secondPoint, center, alphaSecondCircle);
        secondCirclePoint = rotatePoint(secondCirclePoint, center, betaSecondCirlce);
        
        path.moveTo((float)firstPoint.preciseX() + xMargin, (float)firstPoint.preciseY() + yMargin);
        path.lineTo((float)firstCirclePoint.preciseX() + xMargin, (float)firstCirclePoint.preciseY() + yMargin);
        path.lineTo((float)secondCirclePoint.preciseX() + xMargin, (float)secondCirclePoint.preciseY() + yMargin);
        
        secondCirclePoint = rotatePoint(secondCirclePoint, center, alphaSecondCircle);
        
        path.lineTo((float)secondCirclePoint.preciseX() + xMargin, (float)secondCirclePoint.preciseY() + yMargin);
        
        for(int i = 0; i < 7; i++) {
            firstCirclePoint = rotatePoint(firstCirclePoint, center, betaFirstCircle);
            path.lineTo((float)firstCirclePoint.preciseX() + xMargin, (float)firstCirclePoint.preciseY() + yMargin);
            
            firstCirclePoint = rotatePoint(firstCirclePoint, center, alphaFirstCircle);
            path.lineTo((float)firstCirclePoint.preciseX() + xMargin, (float)firstCirclePoint.preciseY() + yMargin);
            
            secondCirclePoint = rotatePoint(secondCirclePoint, center, betaSecondCirlce);
            path.lineTo((float)secondCirclePoint.preciseX() + xMargin, (float)secondCirclePoint.preciseY() + yMargin);
            
            secondCirclePoint = rotatePoint(secondCirclePoint, center, alphaSecondCircle);
            path.lineTo((float)secondCirclePoint.preciseX() + xMargin, (float)secondCirclePoint.preciseY() + yMargin);
        }
        
        path.close();

        return path;
    }
    
    private PrecisionPoint rotatePoint(PrecisionPoint point, PrecisionPoint rotationCenter, double degreeAngle) {
        double radAngle = degreeAngle * Math.PI / 180d;
        double xM = point.preciseX() - rotationCenter.preciseX();
        double yM = point.preciseY() - rotationCenter.preciseY();
        
        PrecisionPoint newPoint = new PrecisionPoint();
        newPoint.setPreciseLocation(xM * Math.cos(radAngle) - yM * Math.sin(radAngle) + rotationCenter.preciseX(),
                xM * Math.sin(radAngle) + yM * Math.cos(radAngle) + rotationCenter.preciseY());
        
        return newPoint;
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
        graphics.setForegroundColor(isEnabled() ? ColorConstants.black : ColorConstants.gray);
        
        Point pt = getIconOrigin();
        
        drawIconCog(graphics, pt.getTranslated(5, 3), 8, 3, 6, 8);
        drawIconCog(graphics, pt.getTranslated(10, -8), 6, 2, 4, 5);
        
        graphics.popState();
    }
    
    /**
     * 
     * Draw a Cog with choosen number of "segments"
     */
    private void drawIconCog(Graphics graphics, Point center, int segments, int r1, int r2, int r3) {
    	// Draw outer Cog
    	PointList outer = new PointList();
    	final double halfSeg = Math.PI / (2*segments); 
    	final double delta = halfSeg / 4;
    	
    	for(int i = 0; i<segments; i++) {
    		outer.addPoint(new PolarPoint(r2, 2*Math.PI*i/segments-halfSeg).toAbsolutePoint(center));
    		outer.addPoint(new PolarPoint(r3, 2*Math.PI*i/segments-halfSeg+delta).toAbsolutePoint(center));
    		outer.addPoint(new PolarPoint(r3, 2*Math.PI*i/segments+halfSeg-delta).toAbsolutePoint(center));
    		outer.addPoint(new PolarPoint(r2, 2*Math.PI*i/segments+halfSeg).toAbsolutePoint(center));
    	}
    	graphics.drawPolygon(outer);
    	
    	// Draw inner circle
    	Path path = new Path(null);
        path.addArc(center.x - r1, center.y - r1, 2*r1, 2*r1, 0, 360);
        graphics.drawPath(path);
        path.dispose();
    }
    
    /**
     * @return The icon start position
     */
    private Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 31, bounds.y + 30);
    }
    
    @Override
    public int getIconOffset() {
        return getDiagramModelArchimateObject().getType() == 0 ? 22 : 0;
    }

    @Override
    protected int getTextControlMarginHeight() {
        return getDiagramModelArchimateObject().getType() == 0 ? super.getTextControlMarginHeight() : 0;
    }

    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? boxDelegate : null;
    }
}
