/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.connections;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;

import com.archimatetool.editor.ui.IIconDelegate;



/**
 * Connection Figure class
 * 
 * @author Phillip Beauvoir
 */
public class AggregationConnectionFigure extends AbstractArchimateConnectionFigure {
	
    /**
     * @return Decoration to use on Source Node
     */
    public static RotatableDecoration createFigureSourceDecoration() {
        return new PathDrawnPolygonDecoration() {
            {
                setScale(5, 3);
                PointList decorationPointList = new PointList();
                decorationPointList.addPoint(0, 0);
                decorationPointList.addPoint(-2, 2);
                decorationPointList.addPoint(-4, 0);
                decorationPointList.addPoint(-2, -2);
                setTemplate(decorationPointList);
            }
            
            @Override
            protected void fillShape(Graphics g) {
                // Draw this as white in case it is disabled
                g.setBackgroundColor(ColorConstants.white);
                super.fillShape(g);
            }
        };
    }
    
    public AggregationConnectionFigure() {
    }

    @Override
    protected void setFigureProperties() {
        setSourceDecoration(createFigureSourceDecoration());
    }
    
    private static IIconDelegate iconDelegate = new IIconDelegate() {
        @Override
        public void drawIcon(Graphics graphics, Color foregroundColor, Color backgroundColor, Point pt) {
            graphics.pushState();
            
            // Ensure this is set
            graphics.setAntialias(SWT.ON);
            
            if(foregroundColor != null) {
                graphics.setForegroundColor(foregroundColor);
            }
            
            graphics.setLineWidth(1);
            
            graphics.drawRectangle(pt.x, pt.y + 9, 4, 4);
            
            Path path = new Path(null);
            path.moveTo(pt.x + 4, pt.y + 9);
            path.lineTo(pt.x + 4 + 9, pt.y);
            graphics.drawPath(path);
            path.dispose();
            
            graphics.popState();
        }

        @Override
        public Rectangle getBounds() {
            // Mirrors the drawRectangle() + Path calls in drawIcon() above (with pt = (0, 0))
            // Small square: drawRectangle(0, 9, 4, 4)
            Rectangle bounds = new Rectangle(0, 9, 4, 4);
            // Diagonal line from (4, 9) to (13, 0)
            bounds = bounds.union(new Rectangle(4, 0, 9, 9));
            return bounds;
        }
    };

    public static IIconDelegate getIconDelegate() {
        return iconDelegate;
    }
}
