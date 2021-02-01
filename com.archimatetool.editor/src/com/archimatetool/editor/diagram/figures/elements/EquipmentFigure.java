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
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.PolarPoint;


/**
 * Equipment Figure
 * 
 * @author Phillip Beauvoir
 */
public class EquipmentFigure extends AbstractTextControlContainerFigure {
    
    public EquipmentFigure() {
        super(TEXT_FLOW_CONTROL);
        setFigureDelegate(new BoxFigureDelegate(this, 22 - getTextControlMarginWidth()));
    }
    
    
    @Override
    protected void drawFigure(Graphics graphics) {
        super.drawFigure(graphics);
        drawIcon(graphics);
    }
    
    /**
     * Draw the icon
     */
    protected void drawIcon(Graphics graphics) {
        if(!getDiagramModelObject().isIconVisible()) {
            return;
        }
        
        graphics.pushState();
        
        graphics.setLineWidth(1);
        graphics.setForegroundColor(isEnabled() ? ColorConstants.black : ColorConstants.gray);
        Point pt = getIconOrigin();
        
        drawCog(graphics, pt.getTranslated(5, 3), 8, 3, 6, 8);
        drawCog(graphics, pt.getTranslated(10, -8), 6, 2, 4, 5);
        
        graphics.popState();
    }
    
    /**
     * 
     * Draw a Cog with choosen number of "segments"
     */
    protected void drawCog(Graphics graphics, Point center, int segments, int r1, int r2, int r3) {
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
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 31, bounds.y + 30);
    }
}
