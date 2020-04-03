package com.archimatetool.export.svg;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.export.svg.graphiti.GraphicsToGraphics2DAdaptor;

/**
 * This over-rides GraphicsToGraphics2DAdaptor
 * Changes go here
 */
public class ExtendedGraphicsToGraphics2DAdaptor extends GraphicsToGraphics2DAdaptor {
    
    public ExtendedGraphicsToGraphics2DAdaptor(Graphics2D graphics, Rectangle viewPort) {
        super(graphics, viewPort);
        
        // This is mangling some fonts on Mac so set this to false
        paintNotCompatibleStringsAsBitmaps = false;
    }

    /**
     * Some Draw2d figures (DiagramImageFigure and IconicDelegate) set interpolation
     */
    @Override
    public void setInterpolation(int interpolation) {
        getSWTGraphics().setInterpolation(interpolation);
    }

    /**
     * Just ignore, so no exception is thrown
     */
    @Override
    public void setBackgroundPattern(Pattern pattern) {
    }
    
    /**
     * JB fix - Draw polylines as a Path2D
     */
    @Override
    public void drawPolyline(PointList pointList) {
        Path2D path = new Path2D.Float();
        
        if (pointList.size() > 1) {
            Point origin = pointList.getPoint(0);
            path.moveTo(origin.x + transX, origin.y + transY);
            
            // Draw polylines as a Path2D
            for(int x = 1; x < pointList.size(); x++) {
                Point p2 = pointList.getPoint(x);
                path.lineTo(p2.x + transX, p2.y + transY);
            }
            
            checkState();
            getGraphics2D().setPaint(getColor(getSWTGraphics().getForegroundColor()));
            getGraphics2D().setStroke(createStroke());
            getGraphics2D().draw(path);
        }
    }
    
    /**
     * JB fix - decrease width and height by 1 pixel
     */
    @Override
    public void fillRectangle(int x, int y, int width, int height) {
        Rectangle2D rect = new Rectangle2D.Float(x + transX, y + transY, width - 1, height - 1);

        checkState();
        getGraphics2D().setPaint(getColor(getSWTGraphics().getBackgroundColor()));
        getGraphics2D().fill(rect);
    }
    
    /**
     * JB fix - decrease width and height by 1 pixel
     */
    @Override
    public void fillRoundRectangle(Rectangle rect, int arcWidth, int arcHeight) {
        RoundRectangle2D roundRect = new RoundRectangle2D.Float(rect.x + transX, rect.y + transY, rect.width - 1, rect.height - 1, arcWidth,
                arcHeight);

        checkState();
        getGraphics2D().setPaint(getColor(getSWTGraphics().getBackgroundColor()));
        getGraphics2D().fill(roundRect);
    }

}
