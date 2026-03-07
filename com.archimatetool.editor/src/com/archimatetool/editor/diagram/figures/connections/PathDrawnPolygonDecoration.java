/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.connections;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.swt.graphics.Path;



/**
 * A PolygonDecoration that uses a SWT Path to draw the fill and outline
 * 
 * @author Phillip Beauvoir
 */
public class PathDrawnPolygonDecoration extends PolygonDecoration {
    
    public PathDrawnPolygonDecoration() {
    }
    
    @Override
    protected void outlineShape(Graphics graphics) {
        Path path = createPathFromPoints();
        if(path != null) {
            graphics.drawPath(path);
            path.dispose();
        }
    }
    
    @Override
    protected void fillShape(Graphics graphics) {
        Path path = createPathFromPoints();
        if(path != null) {
            graphics.fillPath(path);
            path.dispose();
        }
    }
    
    protected Path createPathFromPoints() {
        PointList points = getPoints();
        
        if(points.size() == 0) {
            return null;
        }
        
        Path path = new Path(null);
        
        for(int i = 0; i < points.size(); i++) {
            Point p = points.getPoint(i);
            if(i == 0) {
                path.moveTo(p.x(), p.y());
            }
            else {
                path.lineTo(p.x(), p.y());
            }
        }
        
        path.close();
        
        return path;
    }
}
