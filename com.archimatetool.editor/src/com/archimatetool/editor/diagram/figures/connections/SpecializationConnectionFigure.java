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
}
