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
import org.eclipse.draw2d.geometry.PointList;



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
        return new PolygonDecoration() {
            {
                setScale(5, 3);
                PointList decorationPointList = new PointList();
                decorationPointList.addPoint( 0, 0);
                decorationPointList.addPoint(-2, 2);
                decorationPointList.addPoint(-4, 0);
                decorationPointList.addPoint(-2,-2);
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
}
