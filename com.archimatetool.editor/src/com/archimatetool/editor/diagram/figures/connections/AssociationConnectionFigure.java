/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.connections;

import org.eclipse.draw2d.PolylineDecoration;
import org.eclipse.draw2d.RotatableDecoration;
import org.eclipse.draw2d.geometry.PointList;

import com.archimatetool.model.IAssociationRelationship;

/**
 * Association Connection Figure
 * 
 * @author Phillip Beauvoir
 */
public class AssociationConnectionFigure extends AbstractArchimateConnectionFigure {
    
    private static final PointList POINTS = new PointList();

    static {
        POINTS.addPoint(0, 0);
        POINTS.addPoint(-1, -1);
    }
    
    /**
     * @return Decoration to use on Target Node
     */
    public static RotatableDecoration createFigureTargetDecoration() {
        PolylineDecoration dec = new PolylineDecoration();
        dec.setTemplate(POINTS);
        dec.setScale(9, 4);
        return dec;
    }

    private RotatableDecoration fDecoratorTarget = createFigureTargetDecoration();
    
    public AssociationConnectionFigure() {
    }
	
    @Override
    protected void setFigureProperties() {
    }
    
    @Override
    public void refreshVisuals() {
        // Access type
        IAssociationRelationship relation = (IAssociationRelationship)getDiagramModelArchimateConnection().getArchimateRelationship();
        
        if(relation.isDirected()) {
            setTargetDecoration(fDecoratorTarget); // half-arrow at target endpoint
        }
        else {
            setTargetDecoration(null);
        }

        // This last
        super.refreshVisuals();
    }
}
