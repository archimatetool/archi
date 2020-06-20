/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.connections;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.swt.graphics.Path;

import com.archimatetool.editor.diagram.figures.FigureUtils;



/**
 * A PolygonDecoration that uses a SWT Path to draw the fill and outline
 * 
 * @author Phillip Beauvoir
 */
public class PathDrawnPolygonDecoration extends PolygonDecoration {
    
    public PathDrawnPolygonDecoration() {
    }
    
    @Override
    protected void outlineShape(Graphics g) {
        Path path = FigureUtils.createPathFromPoints(getPoints());
        g.drawPath(path);
        path.dispose();
    }
    
    @Override
    protected void fillShape(Graphics g) {
        Path path = FigureUtils.createPathFromPoints(getPoints());
        g.fillPath(path);
        path.dispose();
    }
}
