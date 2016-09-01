/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.connections;

import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.geometry.PointList;



/**
 * Composition Connection Figure
 * 
 * @author Phillip Beauvoir
 */
public class CompositionConnectionFigure extends AbstractArchimateConnectionFigure {
	
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
        };
    }
    
    public CompositionConnectionFigure() {
    }

    @Override
    protected void setFigureProperties() {
        setSourceDecoration(createFigureSourceDecoration());
    }
}
