/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.connections;

import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.geometry.PointList;

import com.archimatetool.model.IDiagramModelArchimateConnection;


/**
 * Assignment Connection Figure class
 * 
 * @author Phillip Beauvoir
 */
public class AssignmentConnectionFigure extends AbstractArchimateConnectionFigure {
	
    /**
     * @return Decoration to use on Target Node
     */
    public static PolygonDecoration createFigureTargetDecoration() {
        return new PolygonDecoration() {
            {
                setScale(2, 1.3);
                
                PointList decorationPointList = new PointList();
                decorationPointList.addPoint(0, 0);
                decorationPointList.addPoint(0, -1);
                decorationPointList.addPoint(-1, -1);
                decorationPointList.addPoint(-1, 0);
                decorationPointList.addPoint(-1, 1);
                decorationPointList.addPoint(0, 1);
                setTemplate(decorationPointList);
            }
        };
    }
    
    /**
     * @return Decoration to use on Source Node
     */
    public static PolygonDecoration createFigureSourceDecoration() {
        return createFigureTargetDecoration();
    }

    public AssignmentConnectionFigure(IDiagramModelArchimateConnection connection) {
        super(connection);
    }
	
    @Override
    protected void setFigureProperties() {
        setSourceDecoration(createFigureSourceDecoration());
        setTargetDecoration(createFigureTargetDecoration()); 
    }
}
