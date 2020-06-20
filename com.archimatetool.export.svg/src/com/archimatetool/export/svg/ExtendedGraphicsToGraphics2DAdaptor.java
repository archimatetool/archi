package com.archimatetool.export.svg;

import java.awt.Graphics2D;
import java.awt.geom.Path2D;

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
    
    @Override
    public void translate(float dx, float dy) {
        // Not sure if we need to do this
        getSWTGraphics().translate(dx, dy);
        // Or perhaps this?
        //super.translate((int)dx, (int)dy);
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
}
