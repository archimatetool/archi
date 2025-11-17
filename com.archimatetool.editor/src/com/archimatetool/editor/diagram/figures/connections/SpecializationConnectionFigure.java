/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.connections;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;

import com.archimatetool.editor.ui.IIconDelegate;



/**
 * Specialization Connection Figure
 * 
 * @author Phillip Beauvoir
 */
public class SpecializationConnectionFigure extends AbstractArchimateConnectionFigure {
	
    /**
     * @return Decoration to use on Target Node
     */
    public static RotatableDecoration createFigureTargetDecoration() {
        PolygonDecoration decoration = new PolygonDecoration() {
            @Override
            protected void fillShape(Graphics g) {
                // Draw this as white in case it is disabled
                g.setBackgroundColor(ColorConstants.white);
                super.fillShape(g);
            }
        };
        
        decoration.setScale(10, 7);
        decoration.setBackgroundColor(ColorConstants.white);
        return decoration;
    }

    public SpecializationConnectionFigure() {
    }
    
    @Override
    protected void setFigureProperties() {
        setTargetDecoration(createFigureTargetDecoration());
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
            
            Path path = new Path(null);
            
            path.moveTo(pt.x, pt.y + 13);
            path.lineTo(pt.x + 10, pt.y + 3);
            
            path.moveTo(pt.x + 7, pt.y);
            path.lineTo(pt.x + 13, pt.y);
            path.lineTo(pt.x + 13, pt.y + 6);
            path.close();
            graphics.drawPath(path);
            
            path.dispose();
            
            graphics.popState();
        }
    };
    
    public static IIconDelegate getIconDelegate() {
        return iconDelegate;
    }
}
