/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.connections;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;


/**
 * Triggering Connection Figure class
 * 
 * @author Phillip Beauvoir
 */
public class TriggeringConnectionFigure extends AbstractArchimateConnectionFigure {
	
    /**
     * @return Decoration to use on Target Node
     */
    public static RotatableDecoration createFigureTargetDecoration() {
        return new PolygonDecoration();
    }
    
    private RotatableDecoration fDecoratorTarget = createFigureTargetDecoration();

    public TriggeringConnectionFigure() {
    }

    @Override
    public void refreshVisuals() {
        setTargetDecoration(usePlainJunctionTargetDecoration() ? null : fDecoratorTarget);
        
        // This last
        super.refreshVisuals();
    }
    
    public static void drawIcon(Graphics graphics, Color color, Point pt) {
        graphics.pushState();
        graphics.setForegroundColor(color);
        graphics.setBackgroundColor(color);
        graphics.setLineWidth(1);
        
        Path path = new Path(null);
        
        path.moveTo(pt.x, pt.y + 13);
        path.lineTo(pt.x + 13, pt.y);
        
        path.moveTo(pt.x + 8, pt.y);
        path.lineTo(pt.x + 13, pt.y);
        path.lineTo(pt.x + 13, pt.y + 5);
        path.close();
        graphics.drawPath(path); // need to draw and fill so it's centred
        graphics.fillPath(path);
        
        path.dispose();
        
        graphics.popState();
    }
}
